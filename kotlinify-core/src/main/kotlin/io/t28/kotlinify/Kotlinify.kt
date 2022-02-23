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
import io.t28.kotlinify.element.ArrayNode
import io.t28.kotlinify.parser.JsonParser
import io.t28.kotlinify.element.ObjectNode
import io.t28.kotlinify.generator.DataClassGenerator
import io.t28.kotlinify.generator.ListClassGenerator

object Kotlinify {
    fun fromJson(json: String): KotlinBuilder {
        return KotlinBuilder(JsonParser(), json)
    }

    class KotlinBuilder internal constructor(
        private val parser: JsonParser,
        private val content: String
    ) {
        fun toKotlin(packageName: String, fileName: String): String {
            val nodes = parser.parse(content)
            val typeSpecs = nodes.flatMap { node ->
                when (node) {
                    is ArrayNode -> ListClassGenerator(packageName).generate(fileName, node)
                    is ObjectNode -> DataClassGenerator(packageName).generate(fileName, node)
                    else -> emptyList()
                }
            }

            val fileSpec = FileSpec.builder(packageName, fileName).apply {
                typeSpecs.forEach(this::addType)
            }.build()
            return fileSpec.toString()
        }

    }
}
