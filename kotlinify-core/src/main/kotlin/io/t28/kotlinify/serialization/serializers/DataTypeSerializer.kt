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

import io.t28.kotlinify.parser.jsonschema.DataType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = DataType::class)
object DataTypeSerializer : KSerializer<DataType> {
    override val descriptor = PrimitiveSerialDescriptor("${DataType::class.qualifiedName}", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DataType {
        val typeName = decoder.decodeString()
        return DataType.findByTypeName(typeName)
            ?: throw SerializationException("Invalid type name '$typeName'")
    }

    override fun serialize(encoder: Encoder, value: DataType) {
        return encoder.encodeString(value.typeName)
    }
}
