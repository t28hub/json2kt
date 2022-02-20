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
import io.t28.kotlinify.parser.DataType
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

internal class DataTypeSerializerTest {
    @ParameterizedTest(name = "should decode {1}")
    @MethodSource("provideValidFixtures")
    fun `should decode from string representation`(string: String, type: DataType) {
        // Arrange
        @Language("json")
        val json = """
            "$string"
        """.trimIndent()

        // Act
        val actual = Json.decodeFromString(DataTypeSerializer, json)

        // Assert
        assertThat(actual).isEqualTo(type)
    }

    @Test
    fun `should throw Exception when invalid type`() {
        // Arrange
        @Language("json")
        val json = """
            "double"
        """.trimIndent()

        // Act
        val actual = assertThrows<SerializationException> {
            Json.decodeFromString(DataTypeSerializer, json)
        }

        // Assert
        assertThat(actual).hasMessageThat().isEqualTo("Invalid type name 'double'")
    }

    @ParameterizedTest(name = "should encode {1}")
    @MethodSource("provideValidFixtures")
    fun `should encode from decoded type`(string: String, type: DataType) {
        // Act
        val actual = Json.encodeToString(DataTypeSerializer, type)

        // Assert
        assertThat(actual).isEqualTo("""
            "$string"
        """.trimIndent())
    }

    companion object {
        @JvmStatic
        fun provideValidFixtures() = listOf(
            arguments("boolean", DataType.BOOLEAN),
            arguments("integer", DataType.INTEGER),
            arguments("number", DataType.NUMBER),
            arguments("string", DataType.STRING),
            arguments("array", DataType.ARRAY),
            arguments("object", DataType.OBJECT),
            arguments("null", DataType.NULL)
        )
    }
}
