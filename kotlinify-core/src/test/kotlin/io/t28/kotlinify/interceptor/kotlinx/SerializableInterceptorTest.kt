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
package io.t28.kotlinify.interceptor.kotlinx

import io.t28.kotlinify.assertThat
import io.t28.kotlinify.lang.impl.TypeElementImpl
import io.t28.kotlinify.lang.TypeKind.CLASS
import io.t28.kotlinify.lang.annotation
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

internal class SerializableInterceptorTest {
    @Test
    fun `intercept should add @Serializable`() {
        // Arrange
        val type = TypeElementImpl(
            name = "Example",
            kind = CLASS
        )

        // Act
        val actual = SerializableInterceptor.intercept(type)

        // Assert
        assertThat(actual).apply {
            hasName("Example")
            isClass()

            annotations().hasSize(1)
            annotationAt(0).apply {
                members().isEmpty()
            }
        }
    }

    @Test
    fun `intercept should not add @Serializable when type has @Serializable`() {
        // Arrange
        val type = TypeElementImpl(
            name = "Example",
            kind = CLASS,
            annotations = listOf(
                annotation<Serializable>()
            )
        )

        // Act
        val actual = SerializableInterceptor.intercept(type)

        // Assert
        assertThat(actual).isEqualTo(type)
    }
}
