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

import io.t28.kotlinify.element.ArrayNode
import io.t28.kotlinify.element.BooleanValue
import io.t28.kotlinify.element.FloatValue
import io.t28.kotlinify.element.IntValue
import io.t28.kotlinify.element.NamedNode
import io.t28.kotlinify.element.Node
import io.t28.kotlinify.element.NullValue
import io.t28.kotlinify.element.ObjectNode
import io.t28.kotlinify.element.StringValue
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull

/**
 * [Parser] implementation for JSON.
 *
 * @param json The instance of Json for deserialization.
 */
class JsonParser(private val json: Json = Json) : Parser {
    override fun parse(string: String): Collection<Node> {
        val element = try {
            json.parseToJsonElement(string)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

        val rootNode = when (element) {
            is JsonArray -> element.asNode()
            is JsonObject -> element.asNode()
            is JsonPrimitive -> element.asNode()
        }
        return listOf(rootNode)
    }

    private fun JsonElement.asNode(): Node {
        return when (this) {
            is JsonArray -> this.asNode()
            is JsonObject -> this.asNode()
            is JsonPrimitive -> this.asNode()
        }
    }

    private fun JsonArray.asNode(): Node {
        val items = map { childElement -> childElement.asNode() }
        return ArrayNode(items = items)
    }

    private fun JsonObject.asNode(): Node {
        val children = entries.map { (key, element) ->
            val node = when (element) {
                is JsonArray -> element.asNode()
                is JsonObject -> element.asNode()
                is JsonPrimitive -> element.asNode()
            }
            NamedNode(
                node = node,
                name = key,
                simpleName = key.replaceFirstChar { it.uppercaseChar() }
            )
        }
        return ObjectNode(children = children)
    }

    private fun JsonPrimitive.asNode(): Node {
        return when {
            this.isString -> StringValue(content)
            this.intOrNull != null -> IntValue(int)
            this.floatOrNull != null -> FloatValue(float)
            this.booleanOrNull != null -> BooleanValue(boolean)
            else -> NullValue
        }
    }
}
