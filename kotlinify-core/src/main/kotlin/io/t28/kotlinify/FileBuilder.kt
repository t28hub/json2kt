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
import io.t28.kotlinify.generator.ClassGenerator
import io.t28.kotlinify.generator.EnumGenerator
import io.t28.kotlinify.generator.InterfaceGenerator
import io.t28.kotlinify.interceptor.Execution
import io.t28.kotlinify.lang.TypeKind.CLASS
import io.t28.kotlinify.lang.TypeKind.ENUM
import io.t28.kotlinify.lang.TypeKind.INTERFACE
import io.t28.kotlinify.parser.Parser
import io.t28.kotlinify.util.getFilename
import io.t28.kotlinify.util.removeFileExtension

class FileBuilder internal constructor(
    private val parser: Parser<String>,
    private val execution: Execution,
    private val content: String,
    private val indent: String,
) {
    fun toKotlin(packageName: String, fileName: String): String {
        val typeName = fileName.getFilename().removeFileExtension()
        val rootNode = parser.parse(typeName, content)
        val children = rootNode.references().map { nodeRef ->
            val typeNode = nodeRef.get()
            val proceeded = execution.execute(typeNode)
            nodeRef.set(proceeded)
            nodeRef
        }

        val classGenerator = ClassGenerator(packageName)
        val enumGenerator = EnumGenerator(packageName)
        val interfaceGenerator = InterfaceGenerator(packageName)
        val typeSpecs = children.map { nodeRef ->
            val typeNode = nodeRef.get()
            val generator = when (typeNode.kind) {
                CLASS -> classGenerator
                ENUM -> enumGenerator
                INTERFACE -> interfaceGenerator
            }
            generator.generate(typeNode)
        }

        val fileSpec = FileSpec.builder(packageName, fileName).apply {
            indent(indent)
            typeSpecs.forEach(this::addType)
        }.build()
        return fileSpec.toString()
    }
}
