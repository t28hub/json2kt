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
package io.t28.kotlinify.serialization.serializers

import com.google.common.truth.Truth.assertThat
import io.t28.kotlinify.isInstanceOf
import io.t28.kotlinify.parser.jsonschema.ArrayDefinition
import io.t28.kotlinify.parser.jsonschema.BooleanDefinition
import io.t28.kotlinify.parser.jsonschema.Definition
import io.t28.kotlinify.parser.jsonschema.EnumDefinition
import io.t28.kotlinify.parser.jsonschema.IntegerDefinition
import io.t28.kotlinify.parser.jsonschema.NullDefinition
import io.t28.kotlinify.parser.jsonschema.NumberDefinition
import io.t28.kotlinify.parser.jsonschema.ObjectDefinition
import io.t28.kotlinify.parser.jsonschema.RefDefinition
import io.t28.kotlinify.parser.jsonschema.StringDefinition
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.reflect.KClass

internal class DefinitionSerializerTest {
    @ParameterizedTest(name = "should decode to {1}")
    @ArgumentsSource(DecodingFixtures::class)
    fun `should decode from string representation`(string: String, expected: KClass<out Definition>) {
        // Act
        val actual = Json.decodeFromString(DefinitionSerializer, string)

        // Assert
        assertThat(actual).isInstanceOf(expected)
    }

    @ParameterizedTest(name = "should encode from {0}")
    @ArgumentsSource(EncodingFixtures::class)
    fun `should encode to string representation`(definition: Definition, expected: String) {
        // Act
        val actual = Json.encodeToString(DefinitionSerializer, definition)

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        private class DecodingFixtures : ArgumentsProvider {
            @Suppress("LongMethod")
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments(
                        // language=json
                        """
                          {
                            "${'$'}ref": "http://example.com/custom-email-validator.json#"
                          }
                        """.trimIndent(),
                        RefDefinition::class
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "enum": [
                              "Alice",
                              "Bob"
                            ]
                          }
                        """.trimIndent(),
                        EnumDefinition::class
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "null"
                          }
                        """.trimIndent(),
                        NullDefinition::class
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "array",
                            "items": {
                              "type": "integer"
                            }
                          }
                        """.trimIndent(),
                        ArrayDefinition::class
                    ),
                    arguments(
                        // language=json
                        """
                          {
                            "type": "object",
                            "properties": {
                              "a": {
                                "type": "string"
                              },
                              "b": {
                                "type": "number"
                              },
                              "c": {
                                "type": "boolean"
                              }
                            }
                          }
                        """.trimIndent(),
                        ObjectDefinition::class
                    )
                )
            }
        }

        private class EncodingFixtures : ArgumentsProvider {
            @Suppress("LongMethod")
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments(
                        RefDefinition(`$ref` = "#/\$defs/name"),
                        // language=json
                        """
                            {"${'$'}ref":"#/${'$'}defs/name"}
                        """.trimIndent()
                    ),
                    arguments(
                        EnumDefinition(values = setOf("BR", "CA", "DE", "FR", "IT", "JP", "US")),
                        // language=json
                        """
                            {"enum":["BR","CA","DE","FR","IT","JP","US"]}
                        """.trimIndent()
                    ),
                    arguments(
                        NullDefinition(),
                        // language=json
                        """
                            {"type":"null"}
                        """.trimIndent()
                    ),
                    arguments(
                        BooleanDefinition(),
                        // language=json
                        """
                            {"type":"boolean"}
                        """.trimIndent()
                    ),
                    arguments(
                        IntegerDefinition(),
                        // language=json
                        """
                            {"type":"integer"}
                        """.trimIndent()
                    ),
                    arguments(
                        NumberDefinition(),
                        // language=json
                        """
                            {"type":"number"}
                        """.trimIndent()
                    ),
                    arguments(
                        StringDefinition(),
                        // language=json
                        """
                            {"type":"string"}
                        """.trimIndent()
                    ),
                    arguments(
                        ArrayDefinition(),
                        // language=json
                        """
                            {"items":{"type":"null"},"type":"array"}
                        """.trimIndent()
                    ),
                    arguments(
                        ArrayDefinition(items = listOf(StringDefinition())),
                        // language=json
                        """
                            {"items":{"type":"string"},"type":"array"}
                        """.trimIndent()
                    ),
                    arguments(
                        ArrayDefinition(items = listOf(BooleanDefinition(), StringDefinition())),
                        // language=json
                        """
                            {"items":[{"type":"boolean"},{"type":"string"}],"type":"array"}
                        """.trimIndent()
                    ),
                    arguments(
                        ObjectDefinition(),
                        // language=json
                        """
                            {"properties":{},"required":[],"type":"object"}
                        """.trimIndent()
                    ),
                    arguments(
                        ObjectDefinition(
                            properties = mapOf(
                                "foo" to BooleanDefinition(),
                                "bar" to StringDefinition()
                            ),
                            required = setOf("foo", "bar")
                        ),
                        // language=json
                        """
                            {"properties":{"foo":{"type":"boolean"},"bar":{"type":"string"}},"required":["foo","bar"],"type":"object"}
                        """.trimIndent()
                    )
                )
            }
        }
    }
}
