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
import io.t28.kotlinify.parser.JsonParser
import io.t28.kotlinify.generator.ClassGenerator
import io.t28.kotlinify.parser.JsonSchemaParser
import io.t28.kotlinify.parser.Parser
import io.t28.kotlinify.parser.naming.PropertyNamingStrategy
import io.t28.kotlinify.parser.naming.TypeNamingStrategy
import io.t28.kotlinify.util.getFilename
import io.t28.kotlinify.util.removeFileExtension

object Kotlinify {
    fun fromJson(json: String): KotlinBuilder {
        return KotlinBuilder(
            parser = JsonParser(
                typeNameStrategy = TypeNamingStrategy,
                propertyNameStrategy = PropertyNamingStrategy
            ),
            content = json
        )
    }

    fun fromJsonSchema(jsonSchema: String): KotlinBuilder {
        return KotlinBuilder(
            parser = JsonSchemaParser(
                typeNameStrategy = TypeNamingStrategy,
                propertyNameStrategy = PropertyNamingStrategy
            ),
            content = jsonSchema
        )
    }

    class KotlinBuilder internal constructor(
        private val parser: Parser,
        private val content: String
    ) {
        fun toKotlin(packageName: String, fileName: String): String {
            val classGenerator = ClassGenerator(packageName)
            val typeName = fileName.getFilename().removeFileExtension()
            val typeSpecs = parser.parse(typeName, content).map { node ->
                classGenerator.generate(node)
            }
            val fileSpec = FileSpec.builder(packageName, fileName).apply {
                indent("    ")
                typeSpecs.forEach(this::addType)
            }.build()
            return fileSpec.toString()
        }

    }
}
