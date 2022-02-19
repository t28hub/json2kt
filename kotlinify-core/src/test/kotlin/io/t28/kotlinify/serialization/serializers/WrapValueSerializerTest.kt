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
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class WrapValueSerializerTest {
    @Nested
    inner class DeserializationTest {
        @Test
        fun `should deserialize null as an empty list`() {
            // Arrange
            @Language("json")
            val string = """
              {
                "items": null
              }
            """.trimIndent()

            // Act
            val actual = Json.decodeFromString<Wrapper>(string)

            // Assert
            assertThat(actual.items).apply {
                isEmpty()
            }
        }

        @Test
        fun `should deserialize single value as a list`() {
            // Arrange
            @Language("json")
            val string = """
              {
                "items": "valueA"
              }
            """.trimIndent()

            // Act
            val actual = Json.decodeFromString<Wrapper>(string)

            // Assert
            assertThat(actual.items).apply {
                hasSize(1)
                containsExactly("valueA")
            }
        }

        @Test
        fun `should deserialize array as a list`() {
            // Arrange
            @Language("json")
            val string = """
              {
                "items": ["valueA", "valueB"]
              }
            """.trimIndent()

            // Act
            val actual = Json.decodeFromString<Wrapper>(string)

            // Assert
            assertThat(actual.items).apply {
                hasSize(2)
                containsExactly("valueA", "valueB")
            }
        }
    }

    @Nested
    inner class SerializationTest {
        @Test
        fun `should serialize empty list as null`() {
            // Arrange
            val wrapper = Wrapper(emptyList())

            // Act
            val actual = Json.encodeToString(wrapper)

            // Assert
            assertThat(actual).isEqualTo("""
                {"items":null}
            """.trimIndent())
        }

        @Test
        fun `should serialize single value list as a value`() {
            // Arrange
            val wrapper = Wrapper(listOf("valueA"))

            // Act
            val actual = Json.encodeToString(wrapper)

            // Assert
            assertThat(actual).isEqualTo("""
                {"items":"valueA"}
            """.trimIndent())
        }

        @Test
        fun `should serialize multiple values list as an array`() {
            // Arrange
            val wrapper = Wrapper(listOf("valueA", "valueB"))

            // Act
            val actual = Json.encodeToString(wrapper)

            // Assert
            assertThat(actual).isEqualTo("""
                {"items":["valueA","valueB"]}
            """.trimIndent())
        }
    }

    @Serializable
    data class Wrapper(
        @Serializable(with = WrapValueSerializer::class)
        val items: List<String>
    )
}
