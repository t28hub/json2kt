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
import com.google.common.truth.Subject
import com.google.common.truth.Subject.Factory

/**
 * [Subject] implementation for [RootElement].
 */
class RootNodeSubject(metadata: FailureMetadata, private val actual: RootElement) : Subject(metadata, actual) {
    fun hasSize(expectedSize: Int) {
        return children().hasSize(expectedSize)
    }

    fun children(): IterableSubject {
        return check("children").that(actual.children())
    }

    fun childrenAt(index: Int): TypeNodeSubject {
        return check("children[$index]")
            .about(TypeNodeSubject.factory())
            .that(actual.children().toList()[index])
    }

    companion object {
        fun factory() = Factory<RootNodeSubject, RootElement> { metadata, actual ->
            RootNodeSubject(metadata, actual)
        }
    }
}
