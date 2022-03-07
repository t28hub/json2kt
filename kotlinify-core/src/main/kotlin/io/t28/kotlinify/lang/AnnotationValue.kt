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

import kotlinx.collections.immutable.toImmutableList
import kotlin.reflect.KClass

class AnnotationValue private constructor(
    internal val type: KClass<out Annotation>,
    internal val members: List<String>
) {
    override fun toString() = buildString {
        append(AnnotationValue::class.simpleName)
        append("{")
        append("type=${type.simpleName},")
        append("members=$members")
        append("}")
    }

    companion object {
        inline fun <reified T : Annotation> annotation(vararg members: String): AnnotationValue {
            return annotation(T::class, members.toList())
        }

        fun <T : Annotation> annotation(type: KClass<T>, members: List<String>): AnnotationValue {
            return AnnotationValue(type = type, members = members.toImmutableList())
        }
    }
}
