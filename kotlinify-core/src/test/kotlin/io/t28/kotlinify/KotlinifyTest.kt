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

import com.google.common.truth.Truth.assertThat
import io.t28.kotlinify.util.getFilename
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class KotlinifyTest {
    private lateinit var kotlinify: Kotlinify

    @BeforeEach
    private fun setUp() {
        kotlinify = Kotlinify { }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class JsonTest {
        @ParameterizedTest(name = "should generate kotlin classes from {0}")
        @ArgumentsSource(JsonClassFixtures::class)
        fun `should generate kotlin classes from JSON`(jsonFilepath: String, kotlinFilepath: String) {
            // Arrange
            val input = readResourceAsString(jsonFilepath)
            val output = readResourceAsString(kotlinFilepath)

            // Act
            val actual = kotlinify.fromJson(input)
                .toKotlin(packageName = PACKAGE_NAME, fileName = kotlinFilepath.getFilename())

            // Assert
            assertThat(actual).isEqualTo(output)
        }

        @ParameterizedTest(name = "should not generate a kotlin class from {0}")
        @ArgumentsSource(JsonEmptyClassFixtures::class)
        fun `should not generate a kotlin class`(json: String) {
            // Act
            val actual = kotlinify.fromJson(json)
                .toKotlin(packageName = PACKAGE_NAME, fileName = "EmptyClass.kt")

            // Assert
            assertThat(actual).isEqualTo(
                """
                package io.t28.kotlinify


                """.trimIndent()
            )
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class JsonSchemaTest {
        @Test
        fun `should generate kotlin classes from JSON Schema`() {
            // Arrange
            val jsonSchema = readResourceAsString("geographical_location.schema.json")
            val expected = readResourceAsString("GeographicalLocation.kt")

            // Act
            val actual = kotlinify.fromJsonSchema(jsonSchema)
                .toKotlin(packageName = PACKAGE_NAME, "GeographicalLocation.kt")

            // Assert
            assertThat(actual).isEqualTo(expected)
        }

        @ParameterizedTest(name = "should not generate a kotlin class from {0}")
        @ArgumentsSource(JsonSchemaEmptyClassFixtures::class)
        fun `should not generate a kotlin class`(jsonSchema: String) {
            // Act
            val actual = kotlinify.fromJsonSchema(jsonSchema)
                .toKotlin(packageName = PACKAGE_NAME, fileName = "EmptyClass.kt")

            // Assert
            assertThat(actual).isEqualTo(
                """
                package io.t28.kotlinify


                """.trimIndent()
            )
        }
    }

    companion object {
        private const val PACKAGE_NAME = "io.t28.kotlinify"

        class JsonClassFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("duplicated_keys_object.json", "DuplicatedKeyObject.kt"),
                    arguments("empty_object.json", "EmptyObject.kt"),
                    arguments("github_search_issues.json", "GitHubSearchIssues.kt"),
                    arguments("github_user.json", "GitHubUser.kt"),
                    arguments("github_user_emails.json", "GitHubUserEmails.kt"),
                    arguments("twitter_tweets_search.json", "TwitterTweetsSearch.kt"),
                    arguments("twitter_users.json", "TwitterUsers.kt")
                )
            }
        }

        class JsonEmptyClassFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("null"),
                    arguments("true"),
                    arguments("23"),
                    arguments("\"Alice\""),
                    arguments("[]"),
                    arguments("[[[]]]"),
                    arguments("[1, 2, 3]"),
                    arguments("[[[1, 2, 3]]]"),
                )
            }
        }

        class JsonSchemaEmptyClassFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments(
                        // language=json
                        """
                          {
                            "type": "null"
                          }
                        """.trimIndent()
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "boolean"
                          }
                        """.trimIndent()
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "integer"
                          }
                        """.trimIndent()
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "number"
                          }
                        """.trimIndent()
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "string"
                          }
                        """.trimIndent()
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "array"
                          }
                        """.trimIndent()
                    ),
                )
            }
        }
    }
}
