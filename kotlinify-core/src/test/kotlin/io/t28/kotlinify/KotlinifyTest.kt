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
import org.junit.jupiter.api.Test

internal class KotlinifyTest {
    @Test
    fun `toKotlin should generate kotlin class`() {
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
              }
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

            public data class User(
              public val id: Int,
              public val login: String,
              public val site_admin: Boolean,
              public val plan: Plan
            )

            public data class Plan(
              public val name: String,
              public val space: Int,
              public val private_repos: Int,
              public val collaborators: Int
            )

        """.trimIndent()
        )
    }
}
