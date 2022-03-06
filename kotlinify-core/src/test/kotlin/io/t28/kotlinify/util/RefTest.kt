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
package io.t28.kotlinify.util

import com.google.common.truth.Truth.assertThat
import io.t28.kotlinify.util.Ref.Companion.ref
import org.junit.jupiter.api.Test

internal class RefTest {
    @Test
    fun `toString should return string representation`() {
        // Arrange
        val alice = User(name = "Alice", isOwner = true)
        val userRef = alice.ref()

        // Act
        val actual = userRef.toString()

        // Assert
        assertThat(actual).isEqualTo("""
            Ref<User>{value=User(name=Alice, isOwner=true)}
        """.trimIndent())
    }

    @Test
    fun `get should return referenced instance`() {
        // Arrange
        val alice = User(name = "Alice", isOwner = true)
        val userRef = alice.ref()

        // Act
        val actual = userRef.get()

        // Assert
        assertThat(actual).isEqualTo(alice)
    }

    @Test
    fun `set should update referenced instance`() {
        // Arrange
        val alice = User(name = "Alice", isOwner = true)
        val bob = User(name = "Bob", isOwner = false)
        val userRef = alice.ref()

        // Act
        userRef.set(bob)

        // Assert
        assertThat(userRef.get()).apply {
            isEqualTo(bob)
            isNotEqualTo(alice)
        }
    }

    data class User(
        val name: String,
        val isOwner: Boolean
    )
}
