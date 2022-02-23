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
package io.t28.kotlinify.generator

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.t28.kotlinify.element.ArrayNode
import io.t28.kotlinify.element.BooleanValue
import io.t28.kotlinify.element.FloatValue
import io.t28.kotlinify.element.IntValue
import io.t28.kotlinify.element.NamedNode
import io.t28.kotlinify.element.Node
import io.t28.kotlinify.element.NullValue
import io.t28.kotlinify.element.ObjectNode
import io.t28.kotlinify.element.StringValue
import io.t28.kotlinify.element.ValueNode
import io.t28.kotlinify.util.addFirst

sealed class ClassGenerator<T : Node>(protected val packageName: String) {
    abstract fun generate(className: String, node: T): Collection<TypeSpec>

    protected fun ArrayNode.asTypeSpecs(className: String): Collection<TypeSpec> {
        return when (val componentNode = componentNode()) {
            is ArrayNode -> componentNode.asTypeSpecs(className)
            is ObjectNode -> componentNode.asTypeSpecs(className)
            else -> emptyList()
        }
    }

    protected fun ObjectNode.asTypeSpecs(className: String): Collection<TypeSpec> {
        val generated = mutableListOf<TypeSpec>()
        val rootTypeSpec = TypeSpec.classBuilder(className).apply {
            if (hasChildren) {
                modifiers += KModifier.DATA
            }

            generated += children().flatMap { childNode ->
                when (childNode.node) {
                    is ArrayNode -> childNode.node.asTypeSpecs(childNode.simpleName)
                    is ObjectNode -> childNode.node.asTypeSpecs(childNode.simpleName)
                    else -> emptyList()
                }
            }

            propertySpecs += children().map { childNode ->
                val propertyName = childNode.name
                val propertyType = childNode.asTypeName(packageName)
                PropertySpec.builder(propertyName, propertyType).apply {
                    modifiers += KModifier.PUBLIC
                    mutable(false)
                    initializer(propertyName)
                }.build()
            }

            primaryConstructor(FunSpec.constructorBuilder().apply {
                parameters += propertySpecs.map { propertySpec ->
                    ParameterSpec.builder(propertySpec.name, propertySpec.type).build()
                }
            }.build())
        }.build()

        generated.addFirst(rootTypeSpec)
        return generated.toList()
    }

    protected fun NamedNode<*>.asTypeName(packageName: String): TypeName {
        val (node, name, simpleName) = this
        val typeName = when (node) {
            is ArrayNode -> {
                val componentNode = node.componentNode().named(name, simpleName)
                List::class.asTypeName().parameterizedBy(componentNode.asTypeName(packageName))
            }
            is ObjectNode -> ClassName(packageName, simpleName)
            is ValueNode<*> -> node.asTypeName()
            else -> ANY
        }
        return typeName.copy(nullable = isNullable)
    }

    protected fun ValueNode<*>.asTypeName(): TypeName {
        val typeName = when (this) {
            is NullValue -> ANY
            is BooleanValue -> BOOLEAN
            is IntValue -> INT
            is FloatValue -> FLOAT
            is StringValue -> STRING
        }
        return typeName.copy(nullable = isNullable)
    }
}
