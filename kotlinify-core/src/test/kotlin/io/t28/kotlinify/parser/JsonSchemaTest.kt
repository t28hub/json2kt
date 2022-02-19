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
package io.t28.kotlinify.parser

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
internal class JsonSchemaTest {
    private lateinit var json: Json

    @BeforeEach
    fun setUp() {
        json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            prettyPrintIndent = "  "
        }
    }

    @Test
    fun `should deserialize from JSON Schema`() {
        // Arrange
        // https://json-schema.org/learn/
        @Language("json")
        val string = """
            {
              "${'$'}id": "https://example.com/geographical-location.schema.json",
              "${'$'}schema": "https://json-schema.org/draft/2020-12/schema",
              "title": "Longitude and Latitude Values",
              "description": "A geographical coordinate.",
              "required": [ "latitude", "longitude" ],
              "type": "object",
              "properties": {
                "latitude": {
                  "type": "number",
                  "minimum": -90,
                  "maximum": 90
                },
                "longitude": {
                  "type": "number",
                  "minimum": -180,
                  "maximum": 180
                }
              }
            }
        """.trimIndent()

        // Act
        val actual =  json.decodeFromString<JsonSchema>(string)

        // Assert
        with(actual) {
            assertThat(id).isEqualTo("https://example.com/geographical-location.schema.json")
            assertThat(schema).isEqualTo("https://json-schema.org/draft/2020-12/schema")

            assertThat(type).isEqualTo(DataType.OBJECT)
            assertThat(title).isEqualTo("Longitude and Latitude Values")
            assertThat(description).isEqualTo("A geographical coordinate.")

            assertThat(required).apply {
                hasSize(2)
                containsExactly("latitude", "longitude")
            }

            assertThat(properties).apply {
                hasSize(2)
                containsEntry("latitude", NumberDefinition())
                containsEntry("longitude", NumberDefinition())
            }
        }
    }
}
