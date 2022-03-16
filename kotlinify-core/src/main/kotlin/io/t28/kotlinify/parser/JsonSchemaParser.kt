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

import io.t28.kotlinify.lang.ArrayValue
import io.t28.kotlinify.lang.impl.BooleanValue
import io.t28.kotlinify.lang.impl.DoubleValue
import io.t28.kotlinify.lang.impl.IntegerValue
import io.t28.kotlinify.lang.impl.NullValue
import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.PrimitiveValue
import io.t28.kotlinify.lang.impl.PropertyElementImpl
import io.t28.kotlinify.lang.RootElement
import io.t28.kotlinify.lang.impl.StringValue
import io.t28.kotlinify.lang.impl.TypeElementImpl
import io.t28.kotlinify.lang.TypeKind.CLASS
import io.t28.kotlinify.lang.TypeKind.ENUM
import io.t28.kotlinify.lang.ValueElement
import io.t28.kotlinify.lang.impl.ArrayValueImpl
import io.t28.kotlinify.lang.impl.ObjectValueImpl
import io.t28.kotlinify.parser.jsonschema.ArrayDefinition
import io.t28.kotlinify.parser.jsonschema.BooleanDefinition
import io.t28.kotlinify.parser.jsonschema.DataType.ARRAY
import io.t28.kotlinify.parser.jsonschema.DataType.OBJECT
import io.t28.kotlinify.parser.jsonschema.Definition
import io.t28.kotlinify.parser.jsonschema.Document
import io.t28.kotlinify.parser.jsonschema.EnumDefinition
import io.t28.kotlinify.parser.jsonschema.IntegerDefinition
import io.t28.kotlinify.parser.jsonschema.NullDefinition
import io.t28.kotlinify.parser.jsonschema.NumberDefinition
import io.t28.kotlinify.parser.jsonschema.ObjectDefinition
import io.t28.kotlinify.parser.jsonschema.PrimitiveDefinition
import io.t28.kotlinify.parser.jsonschema.StringDefinition
import io.t28.kotlinify.parser.naming.NameStrategy
import io.t28.kotlinify.parser.naming.UniqueNameStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * [Parser] implementation for JSON Schema.
 */
class JsonSchemaParser(
    private val json: Json = Json { ignoreUnknownKeys = true },
    private val typeNameStrategy: NameStrategy,
    private val propertyNameStrategy: NameStrategy
) : Parser<String> {
    override fun parse(rootName: String, content: String): RootElement {
        val document = try {
            json.decodeFromString<Document>(content)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON Schema string", e)
        }

        val internalParser = InternalParser(
            typeNameStrategy = UniqueNameStrategy(typeNameStrategy),
            propertyNameStrategy = propertyNameStrategy
        )
        return internalParser.parse(rootName, document)
    }

    // Define [InternalParser] to prevent pollution of [JsonSchemaParser]
    internal class InternalParser(
        private val typeNameStrategy: NameStrategy,
        private val propertyNameStrategy: NameStrategy,
    ) : Parser<Document> {
        private lateinit var rootElement: RootElement

        override fun parse(rootName: String, content: Document): RootElement {
            rootElement = RootElement()
            when (content.type) {
                ARRAY -> parse(rootName, content.asArray())
                OBJECT -> parse(rootName, content.asObject())
                else -> {
                    // do nothing
                }
            }
            return rootElement
        }

        private fun parse(typeName: String, definition: Definition): ValueElement {
            return when (definition) {
                is ArrayDefinition -> parse(typeName, definition)
                is EnumDefinition -> parse(typeName, definition)
                is ObjectDefinition -> parse(typeName, definition)
                is PrimitiveDefinition -> parse(definition)
                is NullDefinition -> NullValue
                else -> throw IllegalStateException("Type '$definition' is not supported")
            }
        }

        private fun parse(typeName: String, definition: ArrayDefinition): ArrayValue {
            val firstItem: Definition = definition.firstOrElse(NullDefinition())
            val component = parse(typeName, firstItem)
            val isNullable = definition.isEmpty() or definition.containsNull()
            return ArrayValueImpl(component, isNullable)
        }

        private fun parse(typeName: String, definition: EnumDefinition): ObjectValue {
            val typeElement = TypeElementImpl(
                name = typeName,
                kind = ENUM,
                enumConstants = definition.values
            )
            val typeElementRef = rootElement.add(typeElement)
            return ObjectValueImpl(reference = typeElementRef)
        }

        private fun parse(typeName: String, definition: ObjectDefinition): ObjectValue {
            val propertyNameStrategy = UniqueNameStrategy(this.propertyNameStrategy)
            val properties = definition.properties.map { (name, property) ->
                val childTypeName = typeNameStrategy.apply(name)
                val propertyName = propertyNameStrategy.apply(name)
                val propertyType = parse(childTypeName, property)
                PropertyElementImpl(type = propertyType, name = propertyName, originalName = name)
            }

            val typeElement = TypeElementImpl(
                name = typeName,
                kind = CLASS,
                properties = properties
            )
            val typeElementRef = rootElement.add(typeElement)
            return ObjectValueImpl(reference = typeElementRef)
        }

        private fun parse(definition: PrimitiveDefinition): PrimitiveValue {
            return when (definition) {
                is BooleanDefinition -> BooleanValue()
                is IntegerDefinition -> IntegerValue()
                is NumberDefinition -> DoubleValue()
                is StringDefinition -> StringValue()
            }
        }
    }
}
