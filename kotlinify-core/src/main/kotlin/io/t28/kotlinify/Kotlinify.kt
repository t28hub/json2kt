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

sealed class Kotlinify {
    fun fromJson(json: String): KotlinBuilder {
        return KotlinBuilder(json)
    }

    companion object Default : Kotlinify() {
        private const val FILE_NAME = "dummy.kt"
    }

    class KotlinBuilder internal constructor(private val content: String) {
        fun toKotlin(packageName: String, className: String): String {
            val fileSpec = FileSpec.builder(packageName = packageName, fileName = FILE_NAME).apply {
                indent("  ")
                addType(TypeSpec.classBuilder(className).build())
            }.build()
            return fileSpec.toString()
        }
    }
}
