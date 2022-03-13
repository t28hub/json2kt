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

package io.t28.kotlinify.lang.impl

import io.t28.kotlinify.lang.AnnotationValue
import kotlin.reflect.KClass

/**
 * Implementation of [AnnotationValue].
 *
 * @param type The type of [Annotation].
 * @param members The members of this annotation value.
 */
internal class AnnotationValueImpl(
    override val type: KClass<out Annotation>,
    override val members: List<String>
): AnnotationValue {
    override fun toString() = buildString {
        append(AnnotationValue::class.simpleName)
        append("{")
        append("type=${type.simpleName},")
        append("members=$members")
        append("}")
    }
}
