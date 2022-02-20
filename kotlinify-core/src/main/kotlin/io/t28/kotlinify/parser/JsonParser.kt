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

import io.t28.kotlinify.ArrayElement
import io.t28.kotlinify.BooleanElement
import io.t28.kotlinify.LongElement
import io.t28.kotlinify.NullElement
import io.t28.kotlinify.NumberElement
import io.t28.kotlinify.ObjectElement
import io.t28.kotlinify.PrimitiveElement
import io.t28.kotlinify.StringElement
import io.t28.kotlinify.Element
import io.t28.kotlinify.serialization.firstOrElse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

class JsonParser(private val json: Json = Json) : Parser {
    override fun parse(string: String): Element {
        val parsed = try {
            json.parseToJsonElement(string)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

        return when (parsed) {
            is JsonArray -> parse(parsed)
            is JsonObject -> parse(parsed)
            is JsonPrimitive -> parse(parsed)
        }
    }

    private fun parse(json: JsonArray): ArrayElement {
        val element = when (val first = json.firstOrElse(JsonNull)) {
            is JsonArray -> parse(first)
            is JsonObject -> parse(first)
            is JsonPrimitive -> parse(first)
        }

        // Check whether item of [json] is nullable.
        if (json.contains(JsonNull)) {
            return ArrayElement(type = element.markNullable())
        }
        return ArrayElement(type = element)
    }

    private fun parse(json: JsonObject): ObjectElement {
        val properties = json.entries.associate { (key, value) ->
            val element = when (value) {
                is JsonArray -> parse(value)
                is JsonObject -> parse(value)
                is JsonPrimitive -> parse(value)
            }
            key to element
        }
        return ObjectElement(properties = properties)
    }

    private fun parse(json: JsonPrimitive): PrimitiveElement {
        return when {
            json.isString -> StringElement()
            json.booleanOrNull != null -> BooleanElement()
            json.longOrNull != null -> LongElement()
            json.doubleOrNull != null -> NumberElement()
            else -> NullElement
        }
    }
}
