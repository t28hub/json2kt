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
package io.t28.kotlinify.lang

import com.google.common.truth.Truth.assertThat
import io.t28.kotlinify.assertThat
import io.t28.kotlinify.lang.TypeKind.CLASS
import io.t28.kotlinify.lang.impl.TypeElementImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RootElementTest {
    private lateinit var rootNode: RootElement

    @BeforeEach
    fun setUp() {
        rootNode = RootElement()
    }

    @Test
    fun `toString should return string representation`() {
        // Arrange
        rootNode.add(TypeElementImpl(name = "MyClass", kind = CLASS))

        // Act
        val actual = rootNode.toString()

        // Assert
        assertThat(actual).isEqualTo("""
        |RootElement{[TypeElementImpl{name=MyClass,kind=CLASS,properties=[],annotations=[],enumConstants=[]}]}
        """.trimMargin())
    }

    @Test
    fun `children should return child nodes`() {
        // Arrange
        val childNode = TypeElementImpl(name = "MyClass", kind = CLASS)
        rootNode.add(childNode)

        // Act
        val actual = rootNode.children()

        // Assert
        assertThat(actual).apply {
            hasSize(1)
            contains(childNode)
        }
    }

    @Test
    fun `children should return empty collection when no child node`() {
        // Act
        val actual = rootNode.children()

        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun `add should add a child node and reference`() {
        // Arrange
        val childNode = TypeElementImpl(name = "MyClass", kind = CLASS)

        // Act
        val actual = rootNode.add(childNode)

        // Assert
        assertThat(actual).apply {
            hasValue(childNode)
        }
    }
}
