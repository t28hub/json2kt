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

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import io.t28.kotlinify.generator.ClassGenerator
import io.t28.kotlinify.generator.EnumGenerator
import io.t28.kotlinify.lang.ClassType
import io.t28.kotlinify.lang.EnumType
import io.t28.kotlinify.lang.InterfaceType
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.parser.Parser
import io.t28.kotlinify.util.getFilename
import io.t28.kotlinify.util.removeFileExtension

class FileBuilder internal constructor(
    private val parser: Parser,
    private val content: String,
    private val indent: String,
) {
    fun toKotlin(packageName: String, fileName: String): String {
        val classGenerator = ClassGenerator(packageName)
        val enumGenerator = EnumGenerator(packageName)
        val typeName = fileName.getFilename().removeFileExtension()
        val typeSpecs = parser.parse(typeName, content).map { node ->
            node.accept(object : TypeNode.Visitor<Unit, TypeSpec> {
                override fun visitClass(node: ClassType, parameter: Unit): TypeSpec {
                    return classGenerator.generate(node)
                }

                override fun visitEnum(node: EnumType, parameter: Unit): TypeSpec {
                    return enumGenerator.generate(node)
                }

                override fun visitInterface(node: InterfaceType, parameter: Unit): TypeSpec {
                    TODO("Not yet implemented")
                }
            }, Unit)
        }
        val fileSpec = FileSpec.builder(packageName, fileName).apply {
            indent(indent)
            typeSpecs.forEach(this::addType)
        }.build()
        return fileSpec.toString()
    }
}
