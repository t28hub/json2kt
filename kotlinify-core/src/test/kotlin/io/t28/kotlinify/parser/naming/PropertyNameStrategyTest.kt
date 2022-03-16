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

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class PropertyNameStrategyTest {
    @ParameterizedTest(name = "apply(\"{0}\") should return \"{1}\"")
    @ArgumentsSource(ValidNameFixtures::class)
    fun `should return valid property name`(input: String, expected: String) {
        // Act
        val actual = PropertyNameStrategy.apply(input)

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest(name = "apply(\"{0}\") should throw Exception")
    @ArgumentsSource(InvalidNameFixtures::class)
    fun `should throw Exception when input string is invalid`(input: String) {
        // Assert
        assertThrows<IllegalArgumentException> {
            // Act
            PropertyNameStrategy.apply(input)
        }
    }

    companion object {
        class ValidNameFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("a", "a"),
                    arguments("1", "_1"),
                    arguments("name", "name"),
                    arguments("_name", "name"),
                    arguments("user_name", "userName"),
                    arguments("_user_name_", "userName"),
                    arguments("userName", "userName"),
                    arguments("UserName", "userName"),
                    arguments("github_user_name", "githubUserName"),
                )
            }
        }

        class InvalidNameFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments(""),
                    arguments("!"),
                    arguments("!!!"),
                )
            }
        }
    }
}
