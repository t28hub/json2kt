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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class KotlinifyTest {
    @Nested
    inner class JsonTest {
        @Test
        fun `should generate a kotlin class from an empty array`() {
            // Act
            val actual = Kotlinify.fromJson("[]")
                .toKotlin(packageName = "io.t28.kotlinify.samples", fileName = "EmptyArray")

            // Assert
            assertThat(actual).isEqualTo(
                """
            package io.t28.kotlinify.samples

            import java.util.ArrayList
            import kotlin.Any

            public class EmptyArray : ArrayList<Any?>()

            """.trimIndent()
            )
        }

        @Test
        fun `should generate a kotlin class from a primitive array`() {
            // Act
            val actual = Kotlinify.fromJson("[1, 2, 3]")
                .toKotlin(packageName = "io.t28.kotlinify.samples", fileName = "PrimitiveArray")

            // Assert
            assertThat(actual).isEqualTo(
                """
            package io.t28.kotlinify.samples

            import java.util.ArrayList
            import kotlin.Int

            public class PrimitiveArray : ArrayList<Int>()

            """.trimIndent()
            )
        }

        @Test
        fun `should generate a kotlin class from an object array`() {
            // Act
            val actual = Kotlinify.fromJson(
                // language=json
                """
                    [
                      {
                        "email": "octocat@octocat.org",
                        "primary": false,
                        "verified": false,
                        "visibility": "public"
                      }
                    ]
                """.trimIndent())
                .toKotlin(packageName = "io.t28.kotlinify.samples", fileName = "Emails")

            // Assert
            assertThat(actual).isEqualTo(
                """
            package io.t28.kotlinify.samples

            import java.util.ArrayList
            import kotlin.Boolean
            import kotlin.String

            public class Emails : ArrayList<Emails_Item>()

            public data class Emails_Item(
              public val email: String,
              public val primary: Boolean,
              public val verified: Boolean,
              public val visibility: String
            )

            """.trimIndent()
            )
        }

        @Test
        fun `should generate a kotlin class from a nested array`() {
            // Act
            val actual = Kotlinify.fromJson("[[[]]]")
                .toKotlin(packageName = "io.t28.kotlinify.samples", fileName = "NestedArray")

            // Assert
            assertThat(actual).isEqualTo(
                """
            package io.t28.kotlinify.samples

            import java.util.ArrayList
            import kotlin.Any
            import kotlin.collections.List

            public class NestedArray : ArrayList<List<List<Any?>>>()

            """.trimIndent()
            )
        }

        @Test
        fun `should generate a kotlin class from an empty object`() {
            // Act
            val actual = Kotlinify.fromJson("{}")
                .toKotlin(packageName = "io.t28.kotlinify.samples", fileName = "EmptyObject")

            // Assert
            assertThat(actual).isEqualTo(
                """
            package io.t28.kotlinify.samples

            public class EmptyObject()

            """.trimIndent()
            )
        }

        @Test
        fun `should generate a kotlin class from a complex object`() {
            // Act
            val actual = Kotlinify.fromJson(
                """
            {
              "id": 1,
              "login": "octocat",
              "site_admin": false,
              "plan": {
                "name": "Medium",
                "space": 400,
                "private_repos": 20,
                "collaborators": 0
              },
              "emails": [
                {
                  "email": "octocat@github.com",
                  "verified": true,
                  "primary": true,
                  "visibility": "public"
                }
              ]
            }
        """.trimIndent()
            ).toKotlin(packageName = "io.t28.kotlinify.samples", "User")

            // Assert
            assertThat(actual).isEqualTo(
                """
            package io.t28.kotlinify.samples

            import kotlin.Boolean
            import kotlin.Int
            import kotlin.String
            import kotlin.collections.List

            public data class User(
              public val id: Int,
              public val login: String,
              public val site_admin: Boolean,
              public val plan: Plan,
              public val emails: List<Emails>
            )

            public data class Plan(
              public val name: String,
              public val space: Int,
              public val private_repos: Int,
              public val collaborators: Int
            )

            public data class Emails(
              public val email: String,
              public val verified: Boolean,
              public val primary: Boolean,
              public val visibility: String
            )

        """.trimIndent()
            )
        }
    }
}
