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
import org.junit.jupiter.api.Test

internal class CollectionsTest {
    @Test
    fun `firstOrElse should return first element`() {
        // Act
        val actual = listOf("Alice", "Bob", "Charlie").firstOrElse("Zoe")

        // Assert
        assertThat(actual).isEqualTo("Alice")
    }

    @Test
    fun `firstOrElse should return default value when the list is empty`() {
        // Act
        val actual = emptyList<String>().firstOrElse("Zoe")

        // Assert
        assertThat(actual).isEqualTo("Zoe")
    }

    @Test
    fun `addFirst should insert element at first index`() {
        // Act
        val list = mutableListOf("Charlie")
        list.addFirst("Bob")
        list.addFirst("Alice")

        // Assert
        assertThat(list).hasSize(3)
        assertThat(list[0]).isEqualTo("Alice")
        assertThat(list[1]).isEqualTo("Bob")
        assertThat(list[2]).isEqualTo("Charlie")
    }
}
