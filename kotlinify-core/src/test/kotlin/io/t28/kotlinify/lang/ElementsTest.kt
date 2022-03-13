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
import io.t28.kotlinify.lang.impl.PropertyElementImpl
import io.t28.kotlinify.lang.impl.StringValue
import io.t28.kotlinify.lang.impl.TypeElementImpl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ElementsTest {
    private lateinit var annotatedElement: AnnotatedElement

    @BeforeEach
    fun setUp() {
        annotatedElement = TypeElementImpl(
            name = "Example",
            kind = TypeKind.CLASS,
            annotations = listOf(
                annotation<Serializable>()
            )
        )
    }

    @Nested
    inner class HasAnnotationTest {
        @Test
        fun `should return true when the annotation exists`() {
            // Act
            val actual = annotatedElement.hasAnnotation<Serializable>()

            // Assert
            assertThat(actual).isTrue()
        }

        @Test
        fun `should return true when the annotation does not exist`() {
            // Act
            val actual = annotatedElement.hasAnnotation<Deprecated>()

            // Assert
            assertThat(actual).isFalse()
        }
    }

    @Nested
    inner class FindAnnotationsTest {
        @Test
        fun `should return matched annotations`() {
            // Act
            val actual = annotatedElement.findAnnotations { annotation ->
                annotation.members.isEmpty()
            }

            // Assert
            assertThat(actual).hasSize(1)
        }
    }

    @Nested
    inner class AnnotationTest {
        @Test
        fun `should instantiate AnnotationValue with KClass`() {
            // Act
            val actual = annotation<SerialName>()

            // Assert
            assertThat(actual).apply {
                hasType<SerialName>()
                members().isEmpty()
            }
        }

        @Test
        fun `should instantiate AnnotationValue with KClass and members`() {
            // Act
            val actual = annotation<SerialName>(
                """
            |value = "_name"
            """.trimMargin()
            )

            // Assert
            assertThat(actual).apply {
                hasType<SerialName>()
                members().hasSize(1)
                memberAt(0).isEqualTo(
                    """
                |value = "_name"
                """.trimMargin()
                )
            }
        }
    }

    @Nested
    inner class HasPropertiesTest {
        @Test
        fun `should return true when property exists`() {
            // Arrange
            val typeElement = TypeElementImpl(
                name = "Example",
                kind = TypeKind.CLASS,
                properties = listOf(
                    PropertyElementImpl(
                        type = StringValue(),
                        name = "name",
                        originalName = "_name"
                    )
                )
            )

            // Act
            val actual = typeElement.hasProperties()

            // Assert
            assertThat(actual).isTrue()
        }

        @Test
        fun `should return false when property does not exist`() {
            // Arrange
            val typeElement = TypeElementImpl(
                name = "Example",
                kind = TypeKind.CLASS,
                properties = emptyList()
            )

            // Act
            val actual = typeElement.hasProperties()

            // Assert
            assertThat(actual).isFalse()
        }
    }
}
