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
import io.t28.kotlinify.interceptor.TypeInterceptor
import io.t28.kotlinify.lang.TypeElement
import io.t28.kotlinify.lang.annotation
import io.t28.kotlinify.lang.hasAnnotation
import kotlinx.collections.immutable.toImmutableList

object JsonIgnorePropertiesInterceptor : TypeInterceptor {
    override fun intercept(node: TypeElement): TypeElement {
        if (node.hasAnnotation<JsonIgnoreProperties>()) {
            return node
        }

        val annotations = node.annotations.toMutableList()
        annotations += annotation<JsonIgnoreProperties>(
            """
            |ignoreUnknown = true
            """.trimMargin()
        )
        return node.copy(annotations = annotations.toImmutableList())
    }
}
