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
import io.t28.kotlinify.lang.BooleanValue
import io.t28.kotlinify.lang.ClassType
import io.t28.kotlinify.lang.DoubleValue
import io.t28.kotlinify.lang.EnumType
import io.t28.kotlinify.lang.IntegerValue
import io.t28.kotlinify.lang.NullValue
import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.PrimitiveValue
import io.t28.kotlinify.lang.PropertyNode
import io.t28.kotlinify.lang.StringValue
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.ValueNode
import io.t28.kotlinify.parser.jsonschema.ArrayDefinition
import io.t28.kotlinify.parser.jsonschema.BooleanDefinition
import io.t28.kotlinify.parser.jsonschema.DataType
import io.t28.kotlinify.parser.jsonschema.Definition
import io.t28.kotlinify.parser.jsonschema.Document
import io.t28.kotlinify.parser.jsonschema.EnumDefinition
import io.t28.kotlinify.parser.jsonschema.IntegerDefinition
import io.t28.kotlinify.parser.jsonschema.NullDefinition
import io.t28.kotlinify.parser.jsonschema.NumberDefinition
import io.t28.kotlinify.parser.jsonschema.ObjectDefinition
import io.t28.kotlinify.parser.jsonschema.PrimitiveDefinition
import io.t28.kotlinify.parser.jsonschema.StringDefinition
import io.t28.kotlinify.parser.naming.NamingStrategy
import io.t28.kotlinify.parser.naming.UniqueNamingStrategy
import io.t28.kotlinify.util.addFirst
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * [Parser] implementation for JSON Schema.
 *
 */
class JsonSchemaParser(
    private val json: Json = Json { ignoreUnknownKeys = true },
    private val typeNameStrategy: NamingStrategy,
    private val propertyNameStrategy: NamingStrategy
) : Parser {
    override fun parse(rootName: String, content: String): Collection<TypeNode> {
        val document = try {
            json.decodeFromString<Document>(content)
        } catch (e: SerializationException) {
            throw ParseException("Invalid JSON Schema string", e)
        }

        return when (document.type) {
            DataType.ARRAY -> parseArray(rootName, document.asArray())
            DataType.OBJECT -> parseObject(rootName, document.asObject())
            else -> emptyList()
        }
    }

    private fun parseArray(typeName: String, definition: ArrayDefinition): Collection<TypeNode> {
        val typeNodes = mutableListOf<TypeNode>()
        parse(typeName, definition, typeNodes)
        return typeNodes.toImmutableList()
    }

    private fun parseObject(typeName: String, definition: ObjectDefinition): Collection<TypeNode> {
        val typeNodes = mutableListOf<TypeNode>()
        parse(typeName, definition, typeNodes)
        return typeNodes.toImmutableList()
    }

    private fun parse(typeName: String, definition: Definition, typeNodes: MutableList<TypeNode>): ValueNode {
        return when (definition) {
            is ArrayDefinition -> parse(typeName, definition, typeNodes)
            is EnumDefinition -> parse(typeName, definition, typeNodes)
            is ObjectDefinition -> parse(typeName, definition, typeNodes)
            is PrimitiveDefinition -> parse(definition)
            is NullDefinition -> NullValue
            else -> throw IllegalStateException("Type '$definition' is not supported")
        }
    }

    private fun parse(typeName: String, definition: ArrayDefinition, typeNodes: MutableList<TypeNode>): ArrayValue {
        val firstItem: Definition = definition.firstOrElse(NullDefinition())
        val component = parse(typeName, firstItem, typeNodes)
        val isNullable = definition.isEmpty() or definition.containsNull()
        return ArrayValue(component, isNullable)
    }

    private fun parse(typeName: String, definition: EnumDefinition, typeNodes: MutableList<TypeNode>): ObjectValue {
        val typeNode = EnumType(name = typeName, constants = definition.values.toImmutableList())
        typeNodes.addFirst(typeNode)
        return ObjectValue(reference = typeNode)
    }

    private fun parse(typeName: String, definition: ObjectDefinition, typeNodes: MutableList<TypeNode>): ObjectValue {
        val propertyNamingStrategy = UniqueNamingStrategy(this.propertyNameStrategy)
        val properties = definition.properties.map { (name, property) ->
            val childTypeName = typeNameStrategy.apply(name)
            val propertyName = propertyNamingStrategy.apply(name)
            val propertyValue = parse(childTypeName, property, typeNodes)
            PropertyNode(value = propertyValue, name = propertyName, originalName = name)
        }

        val typeNode = ClassType(name = typeName, properties = properties.toImmutableList())
        typeNodes.addFirst(typeNode)
        return ObjectValue(reference = typeNode)
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
