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

import io.t28.kotlinify.parser.jsonschema.ArrayDefinition
import io.t28.kotlinify.parser.jsonschema.DataType
import io.t28.kotlinify.parser.jsonschema.Definition
import io.t28.kotlinify.parser.jsonschema.EnumDefinition
import io.t28.kotlinify.parser.jsonschema.NullDefinition
import io.t28.kotlinify.parser.jsonschema.ObjectDefinition
import io.t28.kotlinify.parser.jsonschema.PrimitiveDefinition
import io.t28.kotlinify.parser.jsonschema.RefDefinition
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
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
@Serializer(forClass = Definition::class)
object DefinitionSerializer : KSerializer<Definition> {
    private const val REF_KEY = "\$ref"
    private const val ENUM_KEY = "enum"
    private const val TYPE_KEY = "type"

    override val descriptor = buildSerialDescriptor("${Definition::class.java}", PolymorphicKind.SEALED) {
        element("RefDefinition", RefDefinition.serializer().descriptor)
        element("EnumDefinition", EnumDefinition.serializer().descriptor)
        element("NullDefinition", NullDefinition.serializer().descriptor)
        element("PrimitiveDefinition", PrimitiveDefinition.serializer().descriptor)
        element("ArrayDefinition", ArrayDefinition.serializer().descriptor)
        element("ObjectDefinition", ObjectDefinition.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Definition {
        val jsonDecoder = decoder as JsonDecoder
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        if (jsonObject.containsKey(REF_KEY)) {
            val deserializer = RefDefinition.serializer()
            return jsonDecoder.json.decodeFromJsonElement(deserializer, jsonObject)
        }

        if (jsonObject.containsKey(ENUM_KEY)) {
            return jsonDecoder.json.decodeFromJsonElement<EnumDefinition>(jsonObject)
        }

        val dataType = jsonObject[TYPE_KEY]?.jsonPrimitive?.content?.let { typeName ->
            DataType.findByTypeName(typeName)
        }
        val deserializer = when (dataType) {
            DataType.NULL -> NullDefinition.serializer()
            DataType.ARRAY -> ArrayDefinition.serializer()
            DataType.OBJECT -> ObjectDefinition.serializer()
            else -> PrimitiveDefinition.serializer()
        }
        return jsonDecoder.json.decodeFromJsonElement(deserializer, jsonObject)
    }

    override fun serialize(encoder: Encoder, value: Definition) {
        when (value) {
            is RefDefinition -> RefDefinition.serializer().serialize(encoder, value)
            is EnumDefinition -> EnumDefinition.serializer().serialize(encoder, value)
            is NullDefinition -> NullDefinition.serializer().serialize(encoder, value)
            is PrimitiveDefinition -> PrimitiveDefinition.serializer().serialize(encoder, value)
            is ArrayDefinition -> ArrayDefinition.serializer().serialize(encoder, value)
            is ObjectDefinition -> ObjectDefinition.serializer().serialize(encoder, value)
            else -> throw SerializationException("Unsupported definition '${value::class.simpleName}'")
        }
    }
}
