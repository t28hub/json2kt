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

import io.t28.kotlinify.interceptor.PropertyInterceptor
import io.t28.kotlinify.interceptor.TypeInterceptor
import io.t28.kotlinify.parser.naming.NameStrategy
import io.t28.kotlinify.parser.naming.PropertyNameStrategy
import io.t28.kotlinify.parser.naming.TypeNameStrategy
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.json.Json

/**
 * Configuration class of [Kotlinify]
 *
 * @param json The [Json] instance for parsing.
 * @param indentSize The size of indent.
 * @param typeNameStrategy The naming strategy for type.
 * @param propertyNameStrategy The naming strategy for property.
 */
class Configuration internal constructor(
    val json: Json,
    val indentSize: Int,
    val typeNameStrategy: NameStrategy,
    val propertyNameStrategy: NameStrategy,
    val typeInterceptors: List<TypeInterceptor>,
    val propertyInterceptors: List<PropertyInterceptor>
) {
    val indent: String
        get() = " ".repeat(indentSize)

    init {
        require(indentSize > 0) { "Size of indent '$indentSize' must be positive" }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder internal constructor(
        val typeInterceptors: MutableList<TypeInterceptor> = mutableListOf(),
        val propertyInterceptors: MutableList<PropertyInterceptor> = mutableListOf()
    ) {
        var json: Json = Json {
            ignoreUnknownKeys = true
        }

        var indentSize: Int = DEFAULT_INDENT_SIZE

        var typeNameStrategy: NameStrategy = TypeNameStrategy

        var propertyNameStrategy: NameStrategy = PropertyNameStrategy

        internal fun build(): Configuration {
            return Configuration(
                json = json,
                indentSize = indentSize,
                typeNameStrategy = typeNameStrategy,
                propertyNameStrategy = propertyNameStrategy,
                typeInterceptors = typeInterceptors.toImmutableList(),
                propertyInterceptors = propertyInterceptors.toImmutableList()
            )
        }

        companion object {
            private const val DEFAULT_INDENT_SIZE = 4
        }
    }
}
