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

import com.google.common.truth.Fact.simpleFact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.IterableSubject
import com.google.common.truth.StringSubject
import com.google.common.truth.Subject
import io.t28.kotlinify.lang.TypeNode.TypeKind.CLASS
import io.t28.kotlinify.lang.TypeNode.TypeKind.ENUM
import io.t28.kotlinify.lang.TypeNode.TypeKind.INTERFACE

/**
 * [Subject] subject implementation for [TypeNode].
 */
class TypeNodeSubject(metadata: FailureMetadata, private val actual: TypeNode) : Subject(metadata, actual) {
    fun hasName(expectedName: String) {
        return name().isEqualTo(expectedName)
    }

    fun isClass() {
        if (actual.kind != CLASS) {
            failWithActual(simpleFact("expected kind to be ${CLASS}, but was ${actual.kind}"))
        }
    }

    fun isEnum() {
        if (actual.kind != ENUM) {
            failWithActual(simpleFact("expected kind to be ${ENUM}, but was ${actual.kind}"))
        }
    }

    fun isInterface() {
        if (actual.kind != INTERFACE) {
            failWithActual(simpleFact("expected kind to be ${INTERFACE}, but was ${actual.kind}"))
        }
    }

    fun name(): StringSubject {
        return check("name").that(actual.name)
    }

    fun properties(): IterableSubject {
        return check("properties").that(actual.children())
    }

    fun propertyAt(index: Int): PropertyNodeSubject {
        return check("properties[%s]", index)
            .about(PropertyNodeSubject.factory())
            .that(actual.properties.toList()[index])
    }

    fun annotations(): IterableSubject {
        return check("annotations").that(actual.annotations)
    }

    fun annotationAt(index: Int): AnnotationValueSubject {
        return check("properties[%s]", index)
            .about(AnnotationValueSubject.factory())
            .that(actual.annotations[index])
    }

    companion object {
        fun factory() = Factory<TypeNodeSubject, TypeNode> { metadata, actual ->
            TypeNodeSubject(metadata, actual)
        }
    }
}
