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
import io.t28.kotlinify.lang.impl.IntegerValue
import io.t28.kotlinify.lang.impl.PropertyElementImpl
import io.t28.kotlinify.lang.impl.StringValue
import io.t28.kotlinify.lang.impl.TypeElementImpl
import io.t28.kotlinify.lang.TypeKind.INTERFACE
import io.t28.kotlinify.lang.annotation
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InterfaceGeneratorTest {
    private lateinit var interfaceGenerator: InterfaceGenerator

    @BeforeEach
    fun setUp() {
        interfaceGenerator = InterfaceGenerator(PACKAGE_NAME)
    }

    @Test
    fun `should generate an interface`() {
        // Arrange
        val node = TypeElementImpl(name = "InterfaceTest", kind = INTERFACE)

        // Act
        val actual = interfaceGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo("""
        |public interface InterfaceTest
        |
        """.trimMargin()
        )
    }

    @Test
    fun `should generate an interface with properties`() {
        // Arrange
        val node = TypeElementImpl(
            name = "InterfaceTest",
            kind = INTERFACE,
            properties = listOf(
                PropertyElementImpl(
                    type = StringValue(),
                    name = "name",
                    originalName = "name"
                ),
                PropertyElementImpl(
                    type = IntegerValue(isNullable = true),
                    name = "age",
                    originalName = "age"
                )
            )
        )

        // Act
        val actual = interfaceGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo("""
        |public interface InterfaceTest {
        |  public val name: kotlin.String
        |
        |  public val age: kotlin.Int?
        |}
        |
        """.trimMargin()
        )
    }

    @Test
    fun `should generate an interface with annotations and properties`() {
        // Arrange
        val node = TypeElementImpl(
            name = "InterfaceTest",
            kind = INTERFACE,
            properties = listOf(
                PropertyElementImpl(
                    type = StringValue(),
                    name = "name",
                    originalName = "name"
                ),
                PropertyElementImpl(
                    type = IntegerValue(isNullable = true),
                    name = "age",
                    originalName = "age"
                )
            ),
            annotations = listOf(
                annotation<Serializable>()
            )
        )

        // Act
        val actual = interfaceGenerator.generate(node)

        // Assert
        assertThat(actual.toString()).isEqualTo("""
        |@kotlinx.serialization.Serializable
        |public interface InterfaceTest {
        |  public val name: kotlin.String
        |
        |  public val age: kotlin.Int?
        |}
        |
        """.trimMargin())
    }

    companion object {
        private const val PACKAGE_NAME = "io.t28.kotlinify.test"
    }
}
