/*
 * Copyright (c) 2022 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.t28.kotlinify.parser

import io.t28.kotlinify.lang.ArrayValue
import io.t28.kotlinify.lang.BooleanValue
import io.t28.kotlinify.lang.FloatValue
import io.t28.kotlinify.lang.IntegerValue
import io.t28.kotlinify.lang.NullValue
import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.PrimitiveValue
import io.t28.kotlinify.lang.PropertyNode
import io.t28.kotlinify.lang.RootNode
import io.t28.kotlinify.lang.StringValue
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.TypeNode.TypeKind.CLASS
import io.t28.kotlinify.lang.ValueNode
import io.t28.kotlinify.parser.naming.NamingStrategy
import io.t28.kotlinify.parser.naming.UniqueNamingStrategy
import io.t28.kotlinify.util.firstOrElse
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull

/**
 * [Parser] implementation for JSON.
 *
 * @param json The instance of Json for deserialization.
 */
class JsonParser(
    private val json: Json = Json,
    private val typeNameStrategy: NamingStrategy,
    private val propertyNameStrategy: NamingStrategy
) : Parser<String> {
    override fun parse(rootName: String, content: String): RootNode {
        val element = try {
            json.parseToJsonElement(content)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

        val internalParser = InternalParser(
            typeNameStrategy = UniqueNamingStrategy(typeNameStrategy),
            propertyNameStrategy = propertyNameStrategy
        )
        return internalParser.parse(rootName, element)
    }

    // Define [InternalParser] to prevent pollution of [JsonParser]
    internal class InternalParser(
        private val typeNameStrategy: NamingStrategy,
        private val propertyNameStrategy: NamingStrategy,
    ) : Parser<JsonElement> {
        private lateinit var rootNode: RootNode

        override fun parse(rootName: String, content: JsonElement): RootNode {
            rootNode = RootNode()
            parseAsValue(rootName, content)
            return rootNode
        }

        private fun parseAsValue(typeName: String, element: JsonElement): ValueNode {
            return when (element) {
                is JsonArray -> parseAsValue(typeName, element)
                is JsonObject -> parseAsValue(typeName, element)
                is JsonPrimitive -> parseAsValue(element)
            }
        }

        private fun parseAsValue(childTypeName: String, element: JsonArray): ArrayValue {
            val child = element.firstOrElse(JsonNull)
            val component = parseAsValue(childTypeName, child)
            val isNullable = element.isEmpty() or element.contains(JsonNull)
            return ArrayValue(component, isNullable)
        }

        private fun parseAsValue(typeName: String, element: JsonObject): ObjectValue {
            val propertyNamingStrategy = UniqueNamingStrategy(this.propertyNameStrategy)
            // Use reversed entries to generate classes order by defined by the JSON.
            val properties = element.entries.reversed().map { (key, child) ->
                val childTypeName = typeNameStrategy.apply(key)
                val propertyValue = parseAsValue(childTypeName, child)
                val propertyName = propertyNamingStrategy.apply(key)
                PropertyNode(value = propertyValue, name = propertyName, originalName = key)
            }

            val typeNode = TypeNode(
                name = typeName,
                kind = CLASS,
                properties = properties.reversed().toImmutableList()
            )
            val typeNodeRef = rootNode.add(typeNode)
            return ObjectValue(reference = typeNodeRef)
        }

        private fun parseAsValue(element: JsonPrimitive): PrimitiveValue {
            return when {
                element.isString -> StringValue()
                element.intOrNull != null -> IntegerValue()
                element.floatOrNull != null -> FloatValue()
                element.booleanOrNull != null -> BooleanValue()
                else -> NullValue
            }
        }
    }
}
