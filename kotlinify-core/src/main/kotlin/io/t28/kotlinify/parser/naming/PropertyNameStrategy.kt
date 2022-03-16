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
package io.t28.kotlinify.parser.naming

/**
 * Strategy for applying property naming rules.
 */
object PropertyNameStrategy : JavaNameStrategy() {
    override fun apply(name: String): String {
        val javaName = name.toJavaIdentifier(delimiter)
        val parts = javaName.split(delimiter).filter(String::isNotEmpty)
        val typeName = parts.withIndex().joinToString(separator = "") { (index, name) ->
            if (index == INITIAL_INDEX) {
                name.replaceFirstChar(Char::lowercaseChar)
            } else {
                name.replaceFirstChar(Char::uppercaseChar)
            }
        }

        if (typeName.isEmpty()) {
            throw IllegalArgumentException("Name '$name' contains only invalid Java identifiers")
        }

        return if (typeName[0].isJavaIdentifierStart()) {
            typeName
        } else {
            "$delimiter$typeName"
        }
    }
}
