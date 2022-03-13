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
package io.t28.kotlinify.interceptor.fastjson

import com.alibaba.fastjson.annotation.JSONField
import io.t28.kotlinify.assertThat
import io.t28.kotlinify.lang.annotation
import io.t28.kotlinify.lang.impl.DoubleValue
import io.t28.kotlinify.lang.impl.PropertyElementImpl
import io.t28.kotlinify.lang.impl.StringValue
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

internal class JSONFieldInterceptorTest {
    @Test
    fun `intercept should add @JSONField`() {
        // Arrange
        val property = PropertyElementImpl(
            type = StringValue(),
            name = "name",
            originalName = "_name"
        )

        // Act
        val actual = JSONFieldInterceptor.intercept(property)

        // Assert
        assertThat(actual).apply {
            annotations().hasSize(1)
            annotationAt(0).apply {
                hasType<JSONField>()
                members().hasSize(1)
                memberAt(0).isEqualTo(
                    """
                    |name = "_name"
                    """.trimMargin()
                )
            }
        }
    }

    @Test
    fun `intercept should not add @JSONField when 'name' == 'originalName'`() {
        // Arrange
        val property = PropertyElementImpl(
            type = StringValue(),
            name = "name",
            originalName = "name"
        )

        // Act
        val actual = JSONFieldInterceptor.intercept(property)

        // Assert
        assertThat(actual).isEqualTo(property)
    }

    @Test
    fun `intercept should not add @JSONField when property has @JSONField`() {
        // Arrange
        val property = PropertyElementImpl(
            type = DoubleValue(),
            name = "name",
            originalName = "_name",
            annotations = persistentListOf(
                annotation<JSONField>()
            )
        )

        // Act
        val actual = JSONFieldInterceptor.intercept(property)

        // Assert
        assertThat(actual).isEqualTo(property)
    }
}
