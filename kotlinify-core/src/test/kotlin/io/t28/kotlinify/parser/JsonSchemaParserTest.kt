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

import io.t28.kotlinify.assertThat
import io.t28.kotlinify.isInstanceOf
import io.t28.kotlinify.lang.BooleanValue
import io.t28.kotlinify.lang.DoubleValue
import io.t28.kotlinify.lang.IntegerValue
import io.t28.kotlinify.lang.StringValue
import io.t28.kotlinify.parser.naming.PropertyNamingStrategy
import io.t28.kotlinify.parser.naming.TypeNamingStrategy
import org.junit.jupiter.api.Test

internal class JsonSchemaParserTest {
    @Test
    fun `should return parsed object2`() {
        // language=json
        val jsonSchema = """
            {
              "type": "array",
              "items": [
                {
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
              ]
            }
        """.trimIndent()
        // Act
        val parser = JsonSchemaParser(
            typeNameStrategy = TypeNamingStrategy,
            propertyNameStrategy = PropertyNamingStrategy
        )
        val actual = parser.parse("Example", jsonSchema)

        assertThat(actual).apply {
            hasSize(1)
            childrenAt(0).apply {
                hasName("Example")
                properties().hasSize(2)

                propertyAt(0).apply {
                    hasName("latitude")
                    value().isInstanceOf<DoubleValue>()
                }

                propertyAt(1).apply {
                    hasName("longitude")
                    value().isInstanceOf<DoubleValue>()
                }
            }
        }
    }

    @Test
    fun `should parse enum type`() {
        // language=json
        val jsonSchema = """
            {
              "type": "object",
              "properties": {
                "country": {
                  "enum": ["FR", "JP", "US"]
                }
              }
            }
        """.trimIndent()

        // Act
        val parser = JsonSchemaParser(
            typeNameStrategy = TypeNamingStrategy,
            propertyNameStrategy = PropertyNamingStrategy
        )
        val actual = parser.parse("Example", jsonSchema)

        // Assert
        assertThat(actual).apply {
            hasSize(2)
            childrenAt(0).apply {
                isClass()
                hasName("Example")
            }
            childrenAt(1).apply {
                isEnum()
                hasName("Country")
            }
        }
    }

    @Test
    fun `should return parsed object`() {
        // language=json
        val jsonSchema = """
            {
              "type":"object",
              "properties": {
                "foo": {
                  "type": "string"
                },
                "bar": {
                  "type": "integer"
                },
                "baz": {
                  "type": "boolean"
                }
              }
            }
        """.trimIndent()

        // Act
        val parser = JsonSchemaParser(
            typeNameStrategy = TypeNamingStrategy,
            propertyNameStrategy = PropertyNamingStrategy
        )
        val actual = parser.parse("Example", jsonSchema)

        // Assert
        assertThat(actual).apply {
            hasSize(1)

            childrenAt(0).apply {
                hasName("Example")
                properties().hasSize(3)

                propertyAt(0).apply {
                    hasName("foo")
                    hasOriginalName("foo")
                    value().isInstanceOf<StringValue>()
                }

                propertyAt(1).apply {
                    hasName("bar")
                    hasOriginalName("bar")
                    value().isInstanceOf<IntegerValue>()
                }

                propertyAt(2).apply {
                    hasName("baz")
                    hasOriginalName("baz")
                    value().isInstanceOf<BooleanValue>()
                }
            }
        }
    }
}
