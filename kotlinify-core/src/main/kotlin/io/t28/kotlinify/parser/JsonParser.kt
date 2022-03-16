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
import io.t28.kotlinify.lang.impl.BooleanValue
import io.t28.kotlinify.lang.impl.FloatValue
import io.t28.kotlinify.lang.impl.IntegerValue
import io.t28.kotlinify.lang.impl.NullValue
import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.PrimitiveValue
import io.t28.kotlinify.lang.impl.PropertyElementImpl
import io.t28.kotlinify.lang.RootElement
import io.t28.kotlinify.lang.impl.StringValue
import io.t28.kotlinify.lang.impl.TypeElementImpl
import io.t28.kotlinify.lang.TypeKind.CLASS
import io.t28.kotlinify.lang.ValueElement
import io.t28.kotlinify.lang.impl.ArrayValueImpl
import io.t28.kotlinify.lang.impl.ObjectValueImpl
import io.t28.kotlinify.parser.naming.NameStrategy
import io.t28.kotlinify.parser.naming.UniqueNameStrategy
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
    private val typeNameStrategy: NameStrategy,
    private val propertyNameStrategy: NameStrategy
) : Parser<String> {
    override fun parse(rootName: String, content: String): RootElement {
        val element = try {
            json.parseToJsonElement(content)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

        val internalParser = InternalParser(
            typeNameStrategy = UniqueNameStrategy(typeNameStrategy),
            propertyNameStrategy = propertyNameStrategy
        )
        return internalParser.parse(rootName, element)
    }

    // Define [InternalParser] to prevent pollution of [JsonParser]
    internal class InternalParser(
        private val typeNameStrategy: NameStrategy,
        private val propertyNameStrategy: NameStrategy,
    ) : Parser<JsonElement> {
        private lateinit var rootElement: RootElement

        override fun parse(rootName: String, content: JsonElement): RootElement {
            rootElement = RootElement()
            parseAsValue(rootName, content)
            return rootElement
        }

        private fun parseAsValue(typeName: String, element: JsonElement): ValueElement {
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
            return ArrayValueImpl(component, isNullable)
        }

        private fun parseAsValue(typeName: String, element: JsonObject): ObjectValue {
            val propertyNamingStrategy = UniqueNameStrategy(this.propertyNameStrategy)
            // Use reversed entries to generate classes order by defined by the JSON.
            val properties = element.entries.reversed().map { (key, child) ->
                val childTypeName = typeNameStrategy.apply(key)
                val propertyType = parseAsValue(childTypeName, child)
                val propertyName = propertyNamingStrategy.apply(key)
                PropertyElementImpl(type = propertyType, name = propertyName, originalName = key)
            }

            val typeElement = TypeElementImpl(
                name = typeName,
                kind = CLASS,
                properties = properties.reversed().toImmutableList()
            )
            val typeElementRef = rootElement.add(typeElement)
            return ObjectValueImpl(reference = typeElementRef)
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
