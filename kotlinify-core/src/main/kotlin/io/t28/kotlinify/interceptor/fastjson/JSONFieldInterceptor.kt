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
import io.t28.kotlinify.interceptor.PropertyInterceptor
import io.t28.kotlinify.lang.PropertyElement
import io.t28.kotlinify.lang.annotation
import io.t28.kotlinify.lang.hasAnnotation
import kotlinx.collections.immutable.toImmutableList

object JSONFieldInterceptor : PropertyInterceptor {
    override fun intercept(node: PropertyElement): PropertyElement {
        if (node.hasAnnotation<JSONField>()) {
            return node
        }

        if (node.name == node.originalName) {
            return node
        }

        val annotations = node.annotations.toMutableList()
        annotations += annotation<JSONField>(
            """
            |name = "${node.originalName}"
            """.trimMargin()
        )
        return node.copy(annotations = annotations.toImmutableList())
    }
}
