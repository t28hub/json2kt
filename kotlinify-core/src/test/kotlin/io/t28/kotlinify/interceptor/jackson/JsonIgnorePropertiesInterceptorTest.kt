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
package io.t28.kotlinify.interceptor.jackson

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.t28.kotlinify.assertThat
import io.t28.kotlinify.lang.AnnotationValue.Companion.annotation
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.TypeNode.TypeKind.CLASS
import org.junit.jupiter.api.Test

internal class JsonIgnorePropertiesInterceptorTest {
    @Test
    fun `intercept should add @JsonIgnoreProperties`() {
        // Arrange
        val type = TypeNode(
            name = "Example",
            kind = CLASS
        )

        // Act
        val actual = JsonIgnorePropertiesInterceptor.intercept(type)

        // Assert
        assertThat(actual).apply {
            hasName("Example")
            isClass()
            annotations().hasSize(1)
            annotationAt(0).apply {
                hasType<JsonIgnoreProperties>()
                members().hasSize(1)
                memberAt(0).isEqualTo(
                    """
                    |ignoreUnknown = true
                    """.trimMargin()
                )
            }
        }
    }

    @Test
    fun `intercept should skip when type has @JsonIgnoreProperties`() {
        // Arrange
        val type = TypeNode(
            name = "Example",
            kind = CLASS,
            annotations = listOf(
                annotation<JsonIgnoreProperties>()
            )
        )

        // Act
        val actual = JsonIgnorePropertiesInterceptor.intercept(type)

        // Assert
        assertThat(actual).isEqualTo(type)
    }
}
