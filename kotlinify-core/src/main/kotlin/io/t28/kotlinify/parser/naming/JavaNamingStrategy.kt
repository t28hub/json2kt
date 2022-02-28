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
 * Strategy for applying Java naming rules.
 *
 * @param delimiter The delimiter for joining names.
 */
open class JavaNamingStrategy(protected val delimiter: Char = NAME_DELIMITER) : NamingStrategy {
    init {
        require(delimiter.isJavaIdentifierStart()) {
            "Delimiter '$delimiter' is invalid Java identifier"
        }
    }

    override fun apply(name: String): String {
        return name.toJavaIdentifier(delimiter)
    }

    companion object {
        const val INITIAL_INDEX = 0

        const val NAME_DELIMITER = '_'

        private const val NO_INDEX = -1

        private const val PACKAGE_SEPARATOR = '.'

        /**
         * Return whether the [CharSequence] is a valid Java identifier.
         */
        fun CharSequence.isJavaIdentifier(): Boolean {
            if (isEmpty()) {
                return false
            }

            return chars().toArray().withIndex().all { (index, code) ->
                val char = code.toChar()
                if (index == INITIAL_INDEX) {
                    Character.isJavaIdentifierStart(char)
                } else {
                    Character.isJavaIdentifierPart(char)
                }
            }
        }

        /**
         * Return whether the [CharSequence] is a valid qualified name.
         */
        fun CharSequence.isQualifiedName(): Boolean {
            var startIndex = INITIAL_INDEX
            while (startIndex < length) {
                var nextIndex = indexOf(PACKAGE_SEPARATOR, startIndex)
                if (nextIndex == NO_INDEX) {
                    nextIndex = length
                }

                val part = substring(startIndex, nextIndex)
                if (!part.isJavaIdentifier()) {
                    return false
                }
                startIndex = nextIndex + 1
            }
            return true
        }

        /**
         * Transform this [CharSequence] to the valid Java identifier .
         *
         * @param replacement The replacement character.
         */
        @Throws(IllegalArgumentException::class)
        fun CharSequence.toJavaIdentifier(replacement: Char = NAME_DELIMITER): String {
            require(isNotEmpty()) { "CharSequence is empty" }
            require(replacement.isJavaIdentifierStart()) { "Character '$replacement' is invalid Java identifier" }

            val originLength = length
            return buildString(originLength) {
                var i = 0
                while (i < originLength) {
                    val char = this@toJavaIdentifier[i]
                    if ((i == INITIAL_INDEX) and !char.isJavaIdentifierStart() and char.isJavaIdentifierPart()) {
                        append(replacement)
                    }

                    if (char.isJavaIdentifierPart()) {
                        append(char)
                    } else {
                        append(replacement)
                    }
                    i += Character.charCount(char.code)
                }
            }
        }
    }
}
