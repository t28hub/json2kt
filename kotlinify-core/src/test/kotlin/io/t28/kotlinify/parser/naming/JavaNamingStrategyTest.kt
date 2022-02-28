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
import io.t28.kotlinify.parser.naming.JavaNamingStrategy.Companion.isJavaIdentifier
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class JavaNamingStrategyTest {
    private lateinit var namingStrategy: JavaNamingStrategy

    @BeforeEach
    fun setUp() {
        namingStrategy = JavaNamingStrategy()
    }

    @Test
    fun `should not throw Exception when delimiter is valid Java identifier char`() {
        // Act
        assertThrows<IllegalArgumentException> {
            // Act
            JavaNamingStrategy('@')
        }
    }

    @Test
    fun `should throw Exception when delimiter is invalid Java identifier char`() {
        // Act
        assertThrows<IllegalArgumentException> {
            // Act
            JavaNamingStrategy('@')
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ApplyTest {
        @ParameterizedTest(name = "apply(\"{0}\") should return \"{1}\"")
        @ArgumentsSource(JavaNameFixtures::class)
        fun `should return valid Java name`(name: String, expected: String) {
            // Act
            val actual = namingStrategy.apply(name)

            // Assert
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should throw Exception when the given string is empty`() {
            // Assert
            assertThrows<IllegalArgumentException> {
                // Act
                namingStrategy.apply("")
            }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class IsJavaIdentifierTest {
        @ParameterizedTest(name = "\"{0}\".isJavaIdentifier() should return true")
        @ArgumentsSource(ValidJavaIdentifierFixtures::class)
        fun `should return true when the given string is valid Java identifier`(name: String) {
            // Act
            val actual = name.isJavaIdentifier()

            // Assert
            assertThat(actual).isTrue()
        }

        @ParameterizedTest(name = "\"{0}\".isJavaIdentifier() should return false")
        @ArgumentsSource(InvalidJavaIdentifierFixtures::class)
        fun `should return false when the given string is invalid Java identifier`(name: String) {
            // Act
            val actual = name.isJavaIdentifier()

            // Assert
            assertThat(actual).isFalse()
        }
    }

    companion object {
        class JavaNameFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("a", "a"),
                    arguments("@", "_"),
                    arguments("1", "_1"),
                    arguments("a_", "a_"),
                    arguments("!@#", "___"),
                    arguments("1value", "_1value"),
                    arguments("value1", "value1"),
                    arguments("user_name", "user_name"),
                    arguments("user-name", "user_name"),
                    arguments("-user-name-", "_user_name_"),
                )
            }
        }

        class ValidJavaIdentifierFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("a"),
                    arguments("i1"),
                    arguments("variable"),
                    arguments("_variable"),
                    arguments("\$variable"),
                    arguments("variable\$"),
                    arguments("MyClass"),
                    arguments("MyClass\$InnerClass"),
                )
            }
        }

        class InvalidJavaIdentifierFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments(""),
                    arguments("1"),
                    arguments("@"),
                    arguments("kebab-case"),
                    arguments("letter case"),
                    arguments("java.util"),
                    arguments("MyClass#InnerClass"),
                )
            }
        }
    }
}
