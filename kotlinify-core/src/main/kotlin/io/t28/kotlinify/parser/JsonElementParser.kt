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

import io.t28.kotlinify.serialization.firstOrElse
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

sealed interface Element

data class ObjectElement internal constructor(
    val name: String?,
    val supertype: ElementType = AnyType(),
    val properties: List<PropertyElement> = emptyList()
) : Element

data class PropertyElement(
    val type: ElementType,
    val name: String
) : Element

sealed interface ElementType {
    val isNullable: Boolean
}

object NullType : ElementType {
    override val isNullable: Boolean = true
}

sealed interface ScalarType : ElementType

data class AnyType(
    override val isNullable: Boolean = false
) : ScalarType

data class BooleanType(
    override val isNullable: Boolean = false
) : ScalarType

data class IntType(
    override val isNullable: Boolean = false
) : ScalarType

data class FloatType(
    override val isNullable: Boolean = false
) : ScalarType

data class StringType(
    override val isNullable: Boolean = false
) : ScalarType

data class EnumType(
    val names: List<String>,
    val values: List<String>,
    val type: ElementType,
    override val isNullable: Boolean = false
) : ElementType

data class ArrayType(
    val type: ElementType,
    override val isNullable: Boolean = false
) : ElementType

data class ObjectType(
    val names: List<String>,
    override val isNullable: Boolean = false
) : ElementType {
    init {
        require(names.isNotEmpty()) { "Names is empty" }
    }

    constructor(vararg names: String) : this(names.toList())
}

class JsonElementParser(private val json: Json = Json) {
    fun parse(string: String): List<ObjectElement> {
        val element = try {
            json.parseToJsonElement(string)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON string", e)
        }

        val created = mutableListOf<ObjectElement>()
        val queue = ArrayDeque<Pair<String?, JsonElement>>()
        queue.add(null to element)
        while (queue.isNotEmpty()) {
            val (name, value) = queue.removeFirst()
            val parsed = when (value) {
                is JsonArray -> value.asObjectElement(name, queue)
                is JsonObject -> value.asObjectElement(name, queue)
                is JsonPrimitive -> throw ParseException("Invalid JSON type 'primitive'")
            }
            created.add(parsed)
        }
        return created.toList()
    }

    private fun JsonArray.asObjectElement(
        name: String? = null,
        queue: ArrayDeque<Pair<String?, JsonElement>>
    ): ObjectElement {
        val elementType = when (val firstElement = firstOrElse(JsonNull)) {
            is JsonArray -> {
                val objectName = "${name?.replaceFirstChar { it.uppercaseChar() }}List"
                queue.add(objectName to firstElement)
                ArrayType(type = ObjectType(objectName))
            }
            is JsonObject -> {
                val objectName = "${name?.replaceFirstChar { it.uppercaseChar() }}Item"
                queue.add(objectName to firstElement)
                ObjectType(objectName)
            }
            is JsonPrimitive -> firstElement.asElementType()
        }
        return ObjectElement(name = name, supertype = ArrayType(type = elementType))
    }

    @Suppress("unused")
    private fun JsonObject.asObjectElement(
        name: String? = null,
        queue: ArrayDeque<Pair<String?, JsonElement>>
    ): ObjectElement {
        val properties = entries.map { (key, child) ->
            if (child is JsonArray || child is JsonObject) {
                queue.add(key.replaceFirstChar { first -> first.uppercaseChar() } to child)
            }
            child.asPropertyElement(key)
        }
        return ObjectElement(name = name, properties = properties)
    }

    private fun JsonElement.asPropertyElement(name: String): PropertyElement {
        return when (this) {
            is JsonArray -> asPropertyElement(name)
            is JsonObject -> asPropertyElement(name)
            is JsonPrimitive -> asPropertyElement(name)
        }
    }

    @Suppress("unused")
    private fun JsonArray.asPropertyElement(name: String): PropertyElement {
        val elementType = when (val firstElement = firstOrElse(JsonNull)) {
            is JsonArray -> {
                val property = firstElement.asPropertyElement(name)
                ArrayType(type = property.type)
            }
            is JsonObject -> {
                val objectName = "${name}Item"
                ObjectType(objectName)
            }
            is JsonPrimitive -> firstElement.asElementType()
        }
        val type = ArrayType(type = elementType)
        return PropertyElement(type = type, name = name)
    }

    @Suppress("unused")
    private fun JsonObject.asPropertyElement(name: String): PropertyElement {
        val type = ObjectType(name.replaceFirstChar { first -> first.uppercaseChar() })
        return PropertyElement(type = type, name = name)
    }

    private fun JsonPrimitive.asPropertyElement(name: String): PropertyElement {
        return PropertyElement(type = asElementType(), name = name)
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
}
