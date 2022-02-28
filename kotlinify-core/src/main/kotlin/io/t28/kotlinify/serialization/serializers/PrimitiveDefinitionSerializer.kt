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
package io.t28.kotlinify.serialization.serializers

import io.t28.kotlinify.parser.jsonschema.BooleanDefinition
import io.t28.kotlinify.parser.jsonschema.DataType
import io.t28.kotlinify.parser.jsonschema.IntegerDefinition
import io.t28.kotlinify.parser.jsonschema.NumberDefinition
import io.t28.kotlinify.parser.jsonschema.PrimitiveDefinition
import io.t28.kotlinify.parser.jsonschema.StringDefinition
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
@Serializer(forClass = PrimitiveDefinition::class)
object PrimitiveDefinitionSerializer : KSerializer<PrimitiveDefinition> {
    private const val TYPE_FIELD = "type"

    override val descriptor =
        buildSerialDescriptor("${PrimitiveDefinition::class.qualifiedName}", PolymorphicKind.SEALED) {
            element("BooleanDefinition", BooleanDefinition.serializer().descriptor)
            element("IntegerDefinition", IntegerDefinition.serializer().descriptor)
            element("NumberDefinition", NumberDefinition.serializer().descriptor)
            element("StringDefinition", StringDefinition.serializer().descriptor)
        }

    override fun deserialize(decoder: Decoder): PrimitiveDefinition {
        val jsonDecoder = decoder as JsonDecoder
        val decodedJson = jsonDecoder.decodeJsonElement()
        if (decodedJson !is JsonObject) {
            throw SerializationException("")
        }

        val dataType = decodedJson[TYPE_FIELD]?.jsonPrimitive?.content?.let {
            DataType.findByTypeName(it)
        }
        val deserializer = when (dataType) {
            DataType.BOOLEAN -> BooleanDefinition.serializer()
            DataType.INTEGER -> IntegerDefinition.serializer()
            DataType.NUMBER -> NumberDefinition.serializer()
            DataType.STRING -> StringDefinition.serializer()
            else -> throw SerializationException("Invalid data type '$dataType")
        }
        return jsonDecoder.json.decodeFromJsonElement(deserializer, decodedJson)
    }

    override fun serialize(encoder: Encoder, value: PrimitiveDefinition) {
        when (value) {
            is BooleanDefinition -> BooleanDefinition.serializer().serialize(encoder, value)
            is IntegerDefinition -> IntegerDefinition.serializer().serialize(encoder, value)
            is NumberDefinition -> NumberDefinition.serializer().serialize(encoder, value)
            is StringDefinition -> StringDefinition.serializer().serialize(encoder, value)
        }
    }
}
