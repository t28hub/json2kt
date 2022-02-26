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
import io.t28.kotlinify.lang.IntValue
import io.t28.kotlinify.lang.NullValue
import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.PrimitiveValue
import io.t28.kotlinify.lang.PropertyNode
import io.t28.kotlinify.lang.StringValue
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.ValueNode
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
class JsonParser(private val json: Json = Json) : Parser {
    override fun parse(rootName: String, content: String): Collection<TypeNode> {
        val element = try {
            json.parseToJsonElement(content)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

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
        typeNodes.reverse()
        return typeNodes.toList()
    }

    private fun parseRoot(typeName: String, element: JsonObject): Collection<TypeNode> {
        val typeNodes = mutableListOf<TypeNode>()
        val properties = element.entries.map { (key, child) ->
            val value = parse(key.toTypeName(), child, typeNodes)
            PropertyNode(value = value, name = key.toPropertyName(), originalName = key)
        }
        typeNodes.reverse()

        val rootType = TypeNode(name = typeName, properties = properties.toImmutableList())
        typeNodes.addFirst(rootType)
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
        val properties = element.entries.map { (key, child) ->
            val value = parse(key.toTypeName(), child, typeNodes)
            PropertyNode(value = value, name = key.toPropertyName(), originalName = key)
        }

        val typeElement = TypeNode(name = typeName, properties = properties.toImmutableList())
        typeNodes.addFirst(typeElement)
        return ObjectValue(reference = typeElement)
    }

    private fun parse(element: JsonPrimitive): PrimitiveValue {
        return when {
            element.isString -> StringValue()
            element.intOrNull != null -> IntValue()
            element.floatOrNull != null -> FloatValue()
            element.booleanOrNull != null -> BooleanValue()
            else -> NullValue
        }
    }

    private fun String.toTypeName(): String {
        return replaceFirstChar { it.uppercaseChar() }
    }

    private fun String.toPropertyName(): String {
        return replaceFirstChar { it.lowercaseChar() }
    }
}
