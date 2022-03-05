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

/**
 * Represents an annotated node.
 */
sealed class AnnotatedNode : Node {
    /**
     * Annotated annotations on this node.
     */
    abstract val annotations: List<AnnotationValue>

    inline fun <reified T : Annotation> hasAnnotation(): Boolean {
        return hasAnnotation(T::class)
    }

    fun <T : Annotation> hasAnnotation(annotationClass: KClass<T>): Boolean {
        val found = findAnnotations { annotation ->
            annotation.type == annotationClass
        }
        return found.isNotEmpty()
    }

    /**
     * Finds annotations by using the [filter].
     *
     * @param filter The filter function.
     */
    fun findAnnotations(filter: (AnnotationValue) -> Boolean): List<AnnotationValue> {
        return annotations.filter(filter).toImmutableList()
    }
}
