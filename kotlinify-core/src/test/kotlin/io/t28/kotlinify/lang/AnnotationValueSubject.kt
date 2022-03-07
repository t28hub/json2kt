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

import com.google.common.truth.FailureMetadata
import com.google.common.truth.IterableSubject
import com.google.common.truth.StringSubject
import com.google.common.truth.Subject
import com.google.common.truth.Truth
import kotlin.reflect.KClass

/**
 * [Subject] implementation for [AnnotationValue].
 */
class AnnotationValueSubject(
    metadata: FailureMetadata,
    private val actual: AnnotationValue
) : Subject(metadata, actual) {
    inline fun <reified T : Annotation> hasType() {
        hasType(T::class)
    }

    fun hasType(expectedType: KClass<out Annotation>) {
        type().isEqualTo(expectedType)
    }

    fun type(): Subject {
        return check("type").that(actual.type)
    }

    fun members(): IterableSubject {
        return check("members").that(actual.members)
    }

    fun memberAt(index: Int): StringSubject {
        return check("members[%s]", index).that(actual.members[index])
    }

    companion object {
        fun factory() = Factory<AnnotationValueSubject, AnnotationValue> { metadata, actual ->
            AnnotationValueSubject(metadata, actual)
        }
    }
}
