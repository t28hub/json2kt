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

import io.t28.kotlinify.parser.jsonschema.EnumDefinition
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import kotlinx.serialization.json.JsonArray

@OptIn(ExperimentalSerializationApi::class)
object EnumDefinitionSerializer : KSerializer<EnumDefinition> {
    private val serializer = ListSerializer(String.serializer())

    override val descriptor = serializer.descriptor

    override fun deserialize(decoder: Decoder): EnumDefinition {
        val jsonDecoder = decoder.asJsonDecoder()
        val element = jsonDecoder.decodeJsonElement()
        if (element !is JsonArray) {
            throw SerializationException("Non-array element is not allowed")
        }

        val values = jsonDecoder.json.decodeFromJsonElement(serializer, element)
        if (values.isEmpty()) {
            throw SerializationException("Empty array is not allowed as an enum")
        }
        return EnumDefinition(values.toSet())
    }

    override fun serialize(encoder: Encoder, value: EnumDefinition) {
        encoder.encodeCollection(descriptor, value.values) { index, item ->
            encodeStringElement(descriptor, index, item)
        }
    }
}
