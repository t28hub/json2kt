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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull

@OptIn(ExperimentalSerializationApi::class)
class WrapValueSerializer<T : Any>(
    private val valueSerializer: KSerializer<T>
) : KSerializer<List<T>> {
    override val descriptor: SerialDescriptor = ListSerializer(valueSerializer).descriptor

    override fun deserialize(decoder: Decoder): List<T> {
        val jsonDecoder = decoder as JsonDecoder
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonNull -> emptyList()
            is JsonArray -> element.map { item ->
                jsonDecoder.json.decodeFromJsonElement(valueSerializer, item)
            }
            else -> listOf(jsonDecoder.json.decodeFromJsonElement(valueSerializer, element))
        }
    }

    override fun serialize(encoder: Encoder, value: List<T>) {
        when (value.size) {
            0 -> encoder.encodeNull()
            1 -> valueSerializer.serialize(encoder, value.first())
            else -> encoder.encodeCollection(descriptor, value) { index, item ->
                encodeSerializableElement(descriptor, index, valueSerializer, item)
            }
        }
    }
}
