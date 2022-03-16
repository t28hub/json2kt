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
package io.t28.kotlinify.parser.naming

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class UniqueNameStrategyTest {
    @Test
    fun `should not throw Exception with maxRetries`() {
        assertDoesNotThrow {
            UniqueNameStrategy(
                nameStrategy = JavaNameStrategy(),
                maxRetries = 4,
                reserved = setOf("as", "is", "in")
            )
        }
    }

    @Test
    fun `should throw Exception when maxRetries is 0`() {
        assertThrows<IllegalArgumentException> {
            UniqueNameStrategy(
                nameStrategy = JavaNameStrategy(),
                maxRetries = 0,
                reserved = setOf("as", "is", "in")
            )
        }
    }

    private val namingStrategy = UniqueNameStrategy(
        nameStrategy = JavaNameStrategy()
    )

    @Test
    fun `apply should return an unique name`() {
        // Act
        val userA = namingStrategy.apply("user")
        val userB = namingStrategy.apply("user")
        val userC = namingStrategy.apply("user")

        // Assert
        assertThat(userA).isEqualTo("user")
        assertThat(userB).isEqualTo("user\$")
        assertThat(userC).isEqualTo("user\$\$")
    }

    @Test
    fun `apply should throw Exception when number of retries exceeded maxRetries`() {
        // Arrange
        repeat(UniqueNameStrategy.DEFAULT_MAX_RETRIES) {
            namingStrategy.apply("user")
        }

        // Assert
        assertThrows<IllegalStateException> {
            namingStrategy.apply("user")
        }
    }
}
