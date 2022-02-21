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
package io.t28.kotlinify

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
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
import io.t28.kotlinify.parser.AnyType
import io.t28.kotlinify.parser.ArrayType
import io.t28.kotlinify.parser.BooleanType
import io.t28.kotlinify.parser.ElementType
import io.t28.kotlinify.parser.FloatType
import io.t28.kotlinify.parser.IntType
import io.t28.kotlinify.parser.JsonElementParser
import io.t28.kotlinify.parser.NullType
import io.t28.kotlinify.parser.ObjectElement
import io.t28.kotlinify.parser.ObjectType
import io.t28.kotlinify.parser.ScalarType
import io.t28.kotlinify.parser.StringType

object Kotlinify {
    fun fromJson(json: String): KotlinBuilder {
        return KotlinBuilder(JsonElementParser(), json)
    }

    class KotlinBuilder internal constructor(
        private val parser: JsonElementParser,
        private val content: String
    ) {
        fun toKotlin(packageName: String, fileName: String): String {
            val typeSpecs = parser.parse(content).map { element ->
                write(packageName, element.name ?: fileName, element)
            }

            val fileSpec = FileSpec.builder(packageName, fileName).apply {
                typeSpecs.forEach(this::addType)
            }.build()
            return fileSpec.toString()
        }

        private fun write(packageName: String, className: String, element: ObjectElement): TypeSpec {
            return TypeSpec.classBuilder(className).apply {
                if (element.properties.isNotEmpty()) {
                    modifiers += KModifier.DATA
                }

                propertySpecs += element.properties.map { property ->
                    val type = property.type.asTypeName(packageName)
                    PropertySpec.builder(property.name, type).apply {
                        modifiers += KModifier.PUBLIC
                        mutable(false)
                        initializer(property.name)
                    }.build()
                }

                primaryConstructor(FunSpec.constructorBuilder().apply {
                    parameters += propertySpecs.map { propertySpec ->
                        ParameterSpec.builder(propertySpec.name, propertySpec.type).build()
                    }
                }.build())
            }.build()
        }
    }

    private fun ElementType.asTypeName(packageName: String): TypeName {
        val type = when (this) {
            is NullType -> ANY.copy(nullable = true)
            is ArrayType -> List::class.asTypeName().parameterizedBy(type.asTypeName(packageName))
            is ObjectType -> ClassName(packageName, names)
            is ScalarType -> asTypeName()
            else -> ANY
        }
        return type.copy(nullable = isNullable)
    }

    private fun ScalarType.asTypeName(): TypeName {
        val type = when (this) {
            is AnyType -> ANY
            is BooleanType -> BOOLEAN
            is IntType -> INT
            is FloatType -> FLOAT
            is StringType -> STRING
        }
        return type.copy(nullable = isNullable)
    }
}
