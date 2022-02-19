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

import io.t28.kotlinify.serialization.serializers.DefinitionSerializer
import io.t28.kotlinify.serialization.serializers.WrapValueSerializer
import io.t28.kotlinify.serialization.serializers.PrimitiveDefinitionSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.EncodeDefault.Mode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = DefinitionSerializer::class)
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
sealed interface Definition

@Serializable
data class RefDefinition(
    @SerialName("\$ref")
    val ref: String
) : Definition

@Serializable
data class EnumDefinition(
    @SerialName("enum")
    val values: Set<String>
) : Definition {
    init {
        require(values.isNotEmpty()) {
            "Empty values are not allowed"
        }
    }
}

@Serializable
sealed class DataDefinition : Definition {
    abstract val type: DataType
    abstract val title: String?
    abstract val description: String?

    override fun toString() = buildString {
        append(this@DataDefinition::class.simpleName)
        append('(')
        append("type=$type, ")
        append("title=$title, ")
        append("description=$description")
        append(')')
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
class NullDefinition(
    override val title: String? = null,
    override val description: String? = null
) : DataDefinition() {
    @EncodeDefault(Mode.ALWAYS)
    override val type: DataType = DataType.NULL
}

// Define as sealed class for generating serializer
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable(with = PrimitiveDefinitionSerializer::class)
sealed class PrimitiveDefinition : DataDefinition()


@Serializable
@ExperimentalSerializationApi
data class BooleanDefinition(
    override val title: String? = null,
    override val description: String? = null
) : PrimitiveDefinition() {
    @EncodeDefault(Mode.ALWAYS)
    override val type: DataType = DataType.BOOLEAN
}

@Serializable
@ExperimentalSerializationApi
data class IntegerDefinition(
    override val title: String? = null,
    override val description: String? = null
) : PrimitiveDefinition() {
    @EncodeDefault(Mode.ALWAYS)
    override val type: DataType = DataType.INTEGER
}

@Serializable
@ExperimentalSerializationApi
data class NumberDefinition(
    override val title: String? = null,
    override val description: String? = null
) : PrimitiveDefinition() {
    @EncodeDefault(Mode.ALWAYS)
    override val type: DataType = DataType.NUMBER
}

@Serializable
@ExperimentalSerializationApi
data class StringDefinition(
    override val title: String? = null,
    override val description: String? = null
) : PrimitiveDefinition() {
    @EncodeDefault(Mode.ALWAYS)
    override val type: DataType = DataType.STRING
}

@Serializable
@ExperimentalSerializationApi
data class ArrayDefinition(
    override val title: String? = null,
    override val description: String? = null,
    @Serializable(with = WrapValueSerializer::class)
    val items: List<Definition>
) : DataDefinition() {
    @EncodeDefault(Mode.ALWAYS)
    override val type: DataType = DataType.ARRAY
}

@Serializable
@ExperimentalSerializationApi
data class ObjectDefinition(
    override val title: String? = null,
    override val description: String? = null,
    val properties: Map<String, Definition> = mapOf(),
    val required: Set<String> = setOf()
) : DataDefinition() {
    @EncodeDefault(Mode.ALWAYS)
    override val type: DataType = DataType.OBJECT
}
