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
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.t28.kotlinify.parser.JsonParser
import io.t28.kotlinify.parser.Parser

object Kotlinify {
    fun fromJson(json: String): KotlinBuilder {
        return KotlinBuilder(JsonParser(), json)
    }

    class KotlinBuilder internal constructor(
        private val parser: Parser,
        private val content: String
    ) {
        fun toKotlin(packageName: String, className: String): String {
            val typeSpec = when (val element = parser.parse(content)) {
                is ObjectElement -> write(packageName, className, element)
                else -> throw IllegalArgumentException("Root content is invalid element '${element::class}'")
            }

            val fileSpec = FileSpec.builder(packageName, className).apply {
                addType(typeSpec)
            }.build()
            return fileSpec.toString()
        }

        private fun write(packageName: String, className: String, element: ObjectElement): TypeSpec {
            return TypeSpec.classBuilder(className).apply {
                if (element.properties.isNotEmpty()) {
                    modifiers += KModifier.DATA
                }

                propertySpecs += element.properties.map { (name, property) ->
                    val type = when (property) {
                        is ArrayElement -> List::class.asTypeName().parameterizedBy(ANY)
                        is ObjectElement -> ClassName(packageName = packageName, simpleNames = listOf(name))
                        is PrimitiveElement -> when (property) {
                            is BooleanElement -> BOOLEAN
                            is LongElement -> LONG
                            is NumberElement -> DOUBLE
                            is StringElement -> STRING
                            else -> ANY.copy(nullable = true)
                        }
                    }

                    PropertySpec.builder(name, type).apply {
                        modifiers += KModifier.PUBLIC
                        mutable(false)
                        initializer(name)
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
}
