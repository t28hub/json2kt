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

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.t28.kotlinify.lang.AnnotationValue
import io.t28.kotlinify.lang.ArrayValue
import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.PrimitiveValue
import io.t28.kotlinify.lang.PropertyNode
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.ValueNode

abstract class TypeSpecGenerator(private val packageName: String) {
    abstract fun generate(node: TypeNode): TypeSpec

    protected fun AnnotationValue.asAnnotationSpec(): AnnotationSpec {
        return AnnotationSpec.builder(type.asTypeName()).apply {
            members += this@asAnnotationSpec.members.map { (key, value) ->
                val format = if (value is String) "$key = %S" else "$key = %L"
                CodeBlock.of(format, value)
            }
        }.build()
    }

    protected fun PropertyNode.asPropertySpec(): PropertySpec {
        return PropertySpec.builder(name, value.asTypeName(packageName)).apply {
            modifiers += KModifier.PUBLIC
            mutable(isMutable)
            initializer(name)
            annotations += this@asPropertySpec.annotations.map { annotation ->
                annotation.asAnnotationSpec()
            }
        }.build()
    }

    protected fun ValueNode.asTypeName(packageName: String): TypeName {
        val typeName = when (this) {
            is ArrayValue -> {
                val typeArgument = componentType().asTypeName(packageName)
                List::class.asTypeName().parameterizedBy(typeArgument)
            }
            is ObjectValue -> {
                val typeNode = reference.get()
                ClassName(packageName, typeNode.name)
            }
            is PrimitiveValue -> type.asTypeName()
        }
        return typeName.copy(nullable = isNullable)
    }
}
