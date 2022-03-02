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
import io.t28.kotlinify.parser.jsonschema.EnumDefinition
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class EnumDefinitionSerializerTest {
    @Nested
    inner class DecodingTest {
        @Test
        fun `should decode enum from JSON Schema`() {
            // Arrange
            @Language("json")
            val json = """
                ["Alice", "Bob", "Charlie"]
            """.trimIndent()

            // Act
            val actual = Json.decodeFromString(EnumDefinitionSerializer, json)

            // Assert
            with(actual) {
                assertThat(values).apply {
                    hasSize(3)
                    containsExactly("Alice", "Bob", "Charlie")
                }
            }
        }

        @Test
        fun `should throw Exception when array is empty`() {
            // Arrange
            @Language("json")
            val json = """
                []
            """.trimIndent()

            // Act
            val actual = assertThrows<SerializationException> {
                Json.decodeFromString(EnumDefinitionSerializer, json)
            }

            // Assert
            assertThat(actual).hasMessageThat().contains("Empty array is not allowed")
        }

        @Test
        fun `should throw Exception when json is not an array`() {
            // Arrange
            @Language("json")
            val json = """
                {}
            """.trimIndent()

            // Act
            val actual = assertThrows<SerializationException> {
                Json.decodeFromString(EnumDefinitionSerializer, json)
            }

            // Assert
            assertThat(actual).hasMessageThat().contains("Non-array element is not allowed")
        }
    }

    @Nested
    inner class EncodingTest {
        @Test
        fun `should encode EnumDefinition as array`() {
            // Arrange
            val definition = EnumDefinition(values = setOf("Alice", "Bob"))

            // Act
            val actual = Json.encodeToString(EnumDefinitionSerializer, definition)

            // Assert
            assertThat(actual).isEqualTo(
                """
                ["Alice","Bob"]
                """.trimIndent()
            )
        }
    }
}
