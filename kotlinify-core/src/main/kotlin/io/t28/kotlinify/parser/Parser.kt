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
package io.t28.kotlinify.parser

import io.t28.kotlinify.lang.TypeNode
import kotlin.jvm.Throws

/**
 * Parser to deserialize from string.
 */
interface Parser {
    /**
     * Parse string to collection of [TypeNode].
     *
     * @param rootName The name of root type.
     * @param content The string content to be parsed.
     */
    @Throws(ParseException::class)
    fun parse(rootName: String, content: String): Collection<TypeNode>
}
