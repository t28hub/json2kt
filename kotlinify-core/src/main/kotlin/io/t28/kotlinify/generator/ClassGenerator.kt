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

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.t28.kotlinify.lang.PropertyNode
import io.t28.kotlinify.lang.TypeNode

internal class ClassGenerator(packageName: String) : TypeSpecGenerator(packageName) {
    override fun generate(node: TypeNode): TypeSpec {
        return TypeSpec.classBuilder(node.name).apply {
            if (node.hasChildren) {
                modifiers += KModifier.DATA
            }

            annotationSpecs += node.annotations.map { annotation ->
                annotation.asAnnotationSpec()
            }

            propertySpecs += node.children().map { property ->
                property.asPropertySpec()
            }

            primaryConstructor(FunSpec.constructorBuilder().apply {
                parameters += propertySpecs.map { propertySpec ->
                    ParameterSpec.builder(propertySpec.name, propertySpec.type).build()
                }
            }.build())
        }.build()
    }

    private fun PropertyNode.asPropertySpec(): PropertySpec {
        return PropertySpec.builder(name, value.asTypeName(packageName)).apply {
            modifiers += KModifier.PUBLIC
            mutable(isMutable)
            initializer(name)
            annotations += this@asPropertySpec.annotations.map { annotation ->
                annotation.asAnnotationSpec()
            }
        }.build()
    }
}
