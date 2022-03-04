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

import io.t28.kotlinify.parser.JsonParser
import io.t28.kotlinify.parser.JsonSchemaParser

class Kotlinify private constructor(private val configuration: Configuration) {
    constructor(init: Configuration.Builder.() -> Unit) : this(
        configuration = Configuration.builder().apply(init).build()
    )

    fun fromJson(content: String): FileBuilder {
        return FileBuilder(
            parser = JsonParser(
                json = configuration.json,
                typeNameStrategy = configuration.typeNameStrategy,
                propertyNameStrategy = configuration.propertyNameStrategy
            ),
            indent = configuration.indent,
            content = content
        )
    }

    fun fromJsonSchema(content: String): FileBuilder {
        return FileBuilder(
            parser = JsonSchemaParser(
                json = configuration.json,
                typeNameStrategy = configuration.typeNameStrategy,
                propertyNameStrategy = configuration.propertyNameStrategy
            ),
            indent = configuration.indent,
            content = content
        )
    }
}
