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

import com.fasterxml.jackson.annotation.JsonProperty
import io.t28.kotlinify.interceptor.PropertyInterceptor
import io.t28.kotlinify.lang.AnnotationValue.Companion.annotation
import io.t28.kotlinify.lang.PropertyNode
import kotlinx.collections.immutable.toImmutableList

object JsonPropertyInterceptor : PropertyInterceptor {
    override fun intercept(node: PropertyNode): PropertyNode {
        if (node.hasAnnotation<JsonProperty>()) {
            return node
        }

        val annotations = node.annotations.toMutableList()
        annotations += if (node.hasSameOriginalName()) {
            annotation<JsonProperty>()
        } else {
            annotation<JsonProperty>(
                """
                |value = "${node.originalName}"
                """.trimMargin()
            )
        }
        return node.copy(annotations = annotations.toImmutableList())
    }
}
