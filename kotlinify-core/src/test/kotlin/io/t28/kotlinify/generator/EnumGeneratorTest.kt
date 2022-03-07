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
package io.t28.kotlinify.generator

import com.google.common.truth.Truth.assertThat
import io.t28.kotlinify.lang.AnnotationValue.Companion.annotation
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.TypeNode.TypeKind.ENUM
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EnumGeneratorTest {
    private lateinit var enumGenerator: EnumGenerator

    @BeforeEach
    fun setUp() {
        enumGenerator = EnumGenerator(PACKAGE_NAME)
    }

    @Test
    fun `should generate an enum class`() {
        // Arrange
        val node = TypeNode(name = "EnumTest", kind = ENUM, enumConstants = emptySet())

        // Act
        val actual = enumGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo("""
        |public enum class EnumTest
        |
        """.trimMargin())
    }

    @Test
    fun `should generate an enum class with constants`() {
        // Arrange
        val node = TypeNode(
            name = "EnumTest",
            kind = ENUM,
            enumConstants = setOf("FOO", "BAR", "BAZ")
        )

        // Act
        val actual = enumGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo("""
        |public enum class EnumTest {
        |  FOO,
        |  BAR,
        |  BAZ,
        |}
        |
        """.trimMargin())
    }


    @Test
    fun `should generate an enum class with annotations`() {
        // Arrange
        val node = TypeNode(
            name = "EnumTest",
            kind = ENUM,
            enumConstants = setOf("FOO", "BAR", "BAZ"),
            annotations = listOf(
                annotation<Deprecated>(
                    """
                    |member = "This enum class is deprecated"
                    """.trimMargin()
                )
            )
        )

        // Act
        val actual = enumGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo("""
        |@kotlin.Deprecated(member = "This enum class is deprecated")
        |public enum class EnumTest {
        |  FOO,
        |  BAR,
        |  BAZ,
        |}
        |
        """.trimMargin())
    }

    companion object {
        private const val PACKAGE_NAME = "io.t28.kotlinify.test"
    }
}
