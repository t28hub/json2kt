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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class UniqueNameStrategyTest {
    @Test
    fun `should not throw Exception with maxRetries`() {
        assertDoesNotThrow {
            UniqueNameStrategy(
                nameStrategy = JavaNameStrategy(),
                maxRetries = 4,
                reserved = setOf("as", "is", "in")
            )
        }
    }

    @Test
    fun `should throw Exception when maxRetries is 0`() {
        assertThrows<IllegalArgumentException> {
            UniqueNameStrategy(
                nameStrategy = JavaNameStrategy(),
                maxRetries = 0,
                reserved = setOf("as", "is", "in")
            )
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ApplyTest {
        private val namingStrategy = UniqueNameStrategy(
            nameStrategy = JavaNameStrategy()
        )

        @ParameterizedTest(name = "should transform \"{0}\" to \"{1}\"")
        @ArgumentsSource(UniqueNameFixtures::class)
        fun `should return an unique name`(name: String, expected: String) {
            // Act
            val actual = namingStrategy.apply(name)

            // Assert
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should throw Exception when number of retries exceeded maxRetries`() {
            // Arrange
            repeat(UniqueNameStrategy.DEFAULT_MAX_RETRIES) {
                namingStrategy.apply("user_name")
            }

            // Assert
            assertThrows<IllegalStateException> {
                namingStrategy.apply("user_name")
            }
        }
    }

    companion object {
        private class UniqueNameFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("foo", "foo"),
                    arguments("foo", "foo\$"),
                    arguments("foo", "foo\$\$"),
                    arguments("qux", "qux"),
                )
            }
        }
    }
}
