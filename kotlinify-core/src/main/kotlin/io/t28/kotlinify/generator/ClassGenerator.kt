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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.t28.kotlinify.lang.ArrayValue
import io.t28.kotlinify.lang.ClassType
import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.PrimitiveValue
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.ValueNode

class ClassGenerator(override val packageName: String): TypeGenerator<ClassType> {
    override fun generate(node: ClassType): TypeSpec {
        return node.asTypeSpec()
    }

    private fun TypeNode.asTypeSpec(): TypeSpec {
        return TypeSpec.classBuilder(name).apply {
            if (hasChildren) {
                modifiers += KModifier.DATA
            }

            propertySpecs += children().map { property ->
                val propertyName = property.name
                val propertyType = property.value.asTypeName(packageName)
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
    }

    private fun ValueNode.asTypeName(packageName: String): TypeName {
        val typeName = when (this) {
            is ArrayValue -> {
                val typeArgument = componentType().asTypeName(packageName)
                List::class.asTypeName().parameterizedBy(typeArgument)
            }
            is ObjectValue -> ClassName(packageName, reference.name)
            is PrimitiveValue -> type.asTypeName()
        }
        return typeName.copy(nullable = isNullable)
    }
}
