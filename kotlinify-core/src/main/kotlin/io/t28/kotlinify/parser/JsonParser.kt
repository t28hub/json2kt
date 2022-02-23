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

import io.t28.kotlinify.element.AnyType
import io.t28.kotlinify.element.ArrayType
import io.t28.kotlinify.element.BooleanType
import io.t28.kotlinify.element.ElementType
import io.t28.kotlinify.element.FloatType
import io.t28.kotlinify.element.IntType
import io.t28.kotlinify.element.NullType
import io.t28.kotlinify.element.ObjectType
import io.t28.kotlinify.element.PropertyElement
import io.t28.kotlinify.element.StringType
import io.t28.kotlinify.element.TypeElement
import io.t28.kotlinify.serialization.firstOrElse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
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
    override fun parse(string: String): Collection<TypeElement> {
        val element = try {
            json.parseToJsonElement(string)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

        return when (element) {
            is JsonArray -> element.asTypeElement()
            is JsonObject -> element.asTypeElement()
            is JsonPrimitive -> throw ParseException("Invalid root JSON type 'primitive'")
        }
    }

    private fun JsonArray.asTypeElement(name: String? = null): List<TypeElement> {
        val created = mutableListOf<TypeElement>()
        when (val firstElement = firstOrElse(JsonNull)) {
            is JsonArray -> {
                val typeName = name.toTypeName(suffix = "List")
                created += firstElement.asTypeElement(typeName)
                ArrayType(componentType = ObjectType(typeName))
            }
            is JsonObject -> {
                val typeName = name.toTypeName(suffix = "Item")
                created += firstElement.asTypeElement(typeName)
                ObjectType(typeName)
            }
            is JsonPrimitive -> {
                firstElement.asElementType()
            }
        }
        return created.toList()
    }

    private fun JsonObject.asTypeElement(name: String? = null): List<TypeElement> {
        val created = mutableListOf<TypeElement>()
        val properties = entries.map { (key, child) ->
            val type = when (child) {
                is JsonArray -> {
                    created += child.asTypeElement(key.toTypeName())
                    child.asElementType(created)
                }
                is JsonObject -> {
                    created += child.asTypeElement(key.toTypeName())
                    val simpleName = created.firstOrNull()?.name ?: throw IllegalArgumentException("First element is missing")
                    ObjectType(simpleName)
                }
                is JsonPrimitive -> {
                    child.asElementType()
                }
            }
            PropertyElement(type = type, name = key)
        }
        created.add(0, TypeElement(name = name, properties = properties))
        return created.toList()
    }

    private fun JsonArray.asElementType(types: List<TypeElement>): ElementType {
        val elementType = when (val firstElement = firstOrElse(JsonNull)) {
            is JsonArray -> firstElement.asElementType(types)
            is JsonObject -> firstElement.asElementType(types)
            is JsonPrimitive -> firstElement.asElementType()
        }
        return ArrayType(componentType = elementType)
    }

    @Suppress("unused")
    private fun JsonObject.asElementType(types: List<TypeElement>): ElementType {
        val simpleName = types.lastOrNull() ?: throw IllegalArgumentException("Last element is missing")
        return simpleName.asType()
    }

    private fun JsonPrimitive.asElementType(): ElementType {
        return when {
            this is JsonNull -> NullType
            this.isString -> StringType()
            this.intOrNull != null -> IntType()
            this.floatOrNull != null -> FloatType()
            this.booleanOrNull != null -> BooleanType()
            else -> AnyType(isNullable = true)
        }
    }

    private fun String?.toTypeName(prefix: String = "", suffix: String = ""): String {
        return "$prefix${this?.replaceFirstChar { it.uppercaseChar() }}$suffix"
    }
}
