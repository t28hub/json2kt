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
import io.t28.kotlinify.parser.ArrayDefinition
import io.t28.kotlinify.parser.BooleanDefinition
import io.t28.kotlinify.parser.Definition
import io.t28.kotlinify.parser.EnumDefinition
import io.t28.kotlinify.parser.IntegerDefinition
import io.t28.kotlinify.parser.NullDefinition
import io.t28.kotlinify.parser.NumberDefinition
import io.t28.kotlinify.parser.ObjectDefinition
import io.t28.kotlinify.parser.RefDefinition
import io.t28.kotlinify.parser.StringDefinition
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

internal class DefinitionSerializerTest {
    @ParameterizedTest(name = "should decode to {1}")
    @MethodSource("provideValidDeserializationFixtures")
    fun `should decode from string representation`(string: String, definition: Definition) {
        // Act
        val actual = Json.decodeFromString(DefinitionSerializer, string)

        // Assert
        assertThat(actual).isInstanceOf(definition::class.java)
    }

    @ParameterizedTest(name = "should encode from {0}")
    @MethodSource("provideValidDeserializationFixtures")
    fun `should encode to string representation`(string: String, definition: Definition) {
        // Act
        val actual = Json.encodeToString(DefinitionSerializer, definition)

        // Assert
        assertThat(actual).isEqualTo(string)
    }

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        @JvmStatic
        fun provideValidDeserializationFixtures() = listOf(
            arguments(
                // language=json
                """
                {"${'$'}ref":"http://example.com/custom-email-validator.json#"}
                """.trimIndent(),
                RefDefinition(ref = "http://example.com/custom-email-validator.json#")
            ),
            arguments(
                // language=json
                """
                {"enum":["Alice","Bob"]}
                """.trimIndent(),
                EnumDefinition(values = setOf("Alice", "Bob"))
            ),
            arguments(
                // language=json
                """
                {"type":"null"}
                """.trimIndent(),
                NullDefinition()
            ),
            arguments(
                // language=json
                """
                {"type":"number"}
                """.trimIndent(),
                NumberDefinition()
            ),
            arguments(
                // language=json
                """
                {"items":{"type":"integer"},"type":"array"}
                """.trimIndent(),
                ArrayDefinition(items = listOf(IntegerDefinition()))
            ),
            arguments(
                // language=json
                """
                {"properties":{"a":{"type":"string"},"b":{"type":"boolean"}},"type":"object"}
                """.trimIndent(),
                ObjectDefinition(
                    properties = mapOf(
                        "a" to StringDefinition(),
                        "b" to BooleanDefinition()
                    )
                )
            ),
        )
    }
}
