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
import io.t28.kotlinify.lang.AnnotationValue
import io.t28.kotlinify.lang.AnnotationValue.Companion.annotation
import io.t28.kotlinify.lang.IntegerValue
import io.t28.kotlinify.lang.PropertyNode
import io.t28.kotlinify.lang.StringValue
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.TypeNode.TypeKind.CLASS
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ClassGeneratorTest {
    private lateinit var classGenerator: ClassGenerator

    @BeforeEach
    fun setUp() {
        classGenerator = ClassGenerator(PACKAGE_NAME)
    }

    @Test
    fun `should generate an empty class`() {
        // Arrange
        val node = TypeNode(name = "EmptyClass", kind = CLASS, properties = emptyList())

        // Act
        val actual = classGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo(
            """
        |public class EmptyClass()
        |
        """.trimMargin()
        )
    }

    @Test
    fun `should generate a class with annotations`() {
        // Arrange
        val node = TypeNode(
            name = "DeprecatedClass",
            kind = CLASS,
            annotations = listOf(
                annotation<Deprecated>(
                    AnnotationValue.Member(
                        name = "message",
                        value = """
                            "This class is deprecated"
                        """.trimIndent()
                    )
                )
            )
        )

        // Act
        val actual = classGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo(
            """
        |@kotlin.Deprecated(message = "This class is deprecated")
        |public class DeprecatedClass()
        |
        """.trimMargin()
        )
    }

    @Test
    fun `should generate a class with properties`() {
        // Arrange
        val node = TypeNode(
            name = "User",
            kind = CLASS,
            properties = listOf(
                PropertyNode(
                    name = "id",
                    originalName = "id",
                    value = IntegerValue(),
                ),
                PropertyNode(
                    name = "name",
                    originalName = "name",
                    value = StringValue(isNullable = true)
                )
            )
        )

        // Act
        val actual = classGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo(
            """
        |public data class User(
        |  public val id: kotlin.Int,
        |  public val name: kotlin.String?
        |)
        |
        """.trimMargin()
        )
    }

    @Test
    fun `should generate a class with annotations and properties`() {
        // Arrange
        val node = TypeNode(
            name = "User",
            kind = CLASS,
            annotations = listOf(
                AnnotationValue(
                    type = Serializable::class
                )
            ),
            properties = listOf(
                PropertyNode(
                    name = "id",
                    originalName = "_id",
                    value = IntegerValue(),
                    annotations = persistentListOf(
                        annotation<SerialName>(
                            AnnotationValue.Member(
                                name = "value",
                                value = """
                                    "_id"
                                """.trimIndent()
                            )
                        )
                    )
                ),
                PropertyNode(
                    name = "name",
                    originalName = "name",
                    value = StringValue(isNullable = true)
                )
            )
        )

        // Act
        val actual = classGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo(
            """
        |@kotlinx.serialization.Serializable
        |public data class User(
        |  @kotlinx.serialization.SerialName(value = "_id")
        |  public val id: kotlin.Int,
        |  public val name: kotlin.String?
        |)
        |
        """.trimMargin()
        )
    }

    companion object {
        private const val PACKAGE_NAME = "io.t28.kotlinify.test"
    }
}
