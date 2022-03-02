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
import io.t28.kotlinify.lang.StringValue
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.ValueNode
import io.t28.kotlinify.parser.naming.NamingStrategy
import io.t28.kotlinify.parser.naming.UniqueNamingStrategy
import io.t28.kotlinify.util.addFirst
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
) : Parser {
    private lateinit var uniqueTypeNameStrategy: NamingStrategy

    override fun parse(rootName: String, content: String): Collection<TypeNode> {
        val element = try {
            json.parseToJsonElement(content)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

        uniqueTypeNameStrategy = UniqueNamingStrategy(typeNameStrategy)
        val types = when (element) {
            is JsonArray -> parseRoot(rootName, element)
            is JsonObject -> parseRoot(rootName, element)
            is JsonPrimitive -> emptyList()
        }
        return types.toImmutableList()
    }

    private fun parseRoot(typeName: String, element: JsonArray): Collection<TypeNode> {
        val typeNodes = mutableListOf<TypeNode>()
        parse(typeName, element.firstOrElse(JsonNull), typeNodes)
        return typeNodes.toList()
    }

    private fun parseRoot(typeName: String, element: JsonObject): Collection<TypeNode> {
        val typeNodes = mutableListOf<TypeNode>()
        parse(typeName, element, typeNodes)
        return typeNodes.toList()
    }

    private fun parse(typeName: String, element: JsonElement, typeNodes: MutableList<TypeNode>): ValueNode {
        return when (element) {
            is JsonArray -> parse(typeName, element, typeNodes)
            is JsonObject -> parse(typeName, element, typeNodes)
            is JsonPrimitive -> parse(element)
        }
    }

    private fun parse(childTypeName: String, element: JsonArray, typeNodes: MutableList<TypeNode>): ArrayValue {
        val child = element.firstOrElse(JsonNull)
        val component = parse(childTypeName, child, typeNodes)
        val isNullable = element.isEmpty() or element.contains(JsonNull)
        return ArrayValue(component, isNullable)
    }

    private fun parse(typeName: String, element: JsonObject, typeNodes: MutableList<TypeNode>): ObjectValue {
        val propertyNamingStrategy = UniqueNamingStrategy(this.propertyNameStrategy)
        // Use reversed entries to generate classes order by defined by the JSON.
        val properties = element.entries.reversed().map { (key, child) ->
            val childTypeName = uniqueTypeNameStrategy.apply(key)
            val propertyValue = parse(childTypeName, child, typeNodes)
            val propertyName = propertyNamingStrategy.apply(key)
            PropertyNode(value = propertyValue, name = propertyName, originalName = key)
        }

        val typeNode = TypeNode(name = typeName, properties = properties.reversed().toImmutableList())
        typeNodes.addFirst(typeNode)
        return ObjectValue(reference = typeNode)
    }

    private fun parse(element: JsonPrimitive): PrimitiveValue {
        return when {
            element.isString -> StringValue()
            element.intOrNull != null -> IntegerValue()
            element.floatOrNull != null -> FloatValue()
            element.booleanOrNull != null -> BooleanValue()
            else -> NullValue
        }
    }
}
