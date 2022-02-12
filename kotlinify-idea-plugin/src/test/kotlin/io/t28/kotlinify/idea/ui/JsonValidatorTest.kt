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
package io.t28.kotlinify.idea.ui

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class JsonValidatorTest {
    @ParameterizedTest(name = "should return null when string is {0}")
    @MethodSource("provideValidFixtures")
    fun `getErrorText should return null when string is valid JSON`(input: String) {
        // Act
        val actual = JsonValidator.getErrorText(input)

        // Assert
        assertThat(actual).isNull()
    }

    @ParameterizedTest(name = "should return error text when string is {0}")
    @MethodSource("provideInvalidFixtures")
    fun `getErrorText should return error text when string is invalid JSON`(input: String) {
        // Act
        val actual = JsonValidator.getErrorText(input)

        // Assert
        assertThat(actual).isNotNull()
    }

    companion object {
        @JvmStatic
        fun provideValidFixtures() = listOf(
            // language=JSON
            """
                []
            """.trimIndent(),
            // language=JSON
            """
                {}
            """.trimIndent(),
            // language=JSON
            """
                {
                  "name": "Alice"
                }
            """.trimIndent(),
            // language=JSON
            """
                {
                  "name": "Alice",
                  "nested": {}
                }
            """.trimIndent(),
            // language=JSON
            """
                [[[]],[[]],[[]]]
            """.trimIndent(),
        )

        @JvmStatic
        fun provideInvalidFixtures() = listOf(
            "true",
            "1",
            "0.1",
            "\"text\"",
            "[[[",
            "]]]",
            "{{{",
            "}}}",
            "{[]}",
        )
    }
}
