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

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.t28.kotlinify.lang.PropertyElement
import io.t28.kotlinify.lang.TypeElement

internal class InterfaceGenerator(packageName: String) : TypeSpecGenerator(packageName) {
    override fun generate(node: TypeElement): TypeSpec {
        return TypeSpec.interfaceBuilder(node.name).apply {
            annotationSpecs += node.annotations.map { annotation ->
                annotation.asAnnotationSpec()
            }

            propertySpecs += node.properties.map { property ->
                property.asPropertySpec()
            }
        }.build()
    }

    private fun PropertyElement.asPropertySpec(): PropertySpec {
        return PropertySpec.builder(name, type.asTypeName(packageName)).apply {
            modifiers += KModifier.PUBLIC
            mutable(isMutable)
            annotations += this@asPropertySpec.annotations.map { annotation ->
                annotation.asAnnotationSpec()
            }
        }.build()
    }
}
