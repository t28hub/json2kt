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
package io.t28.kotlinify

import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertAbout
import io.t28.kotlinify.lang.AnnotationValue
import io.t28.kotlinify.lang.AnnotationValueSubject
import io.t28.kotlinify.lang.PropertyElement
import io.t28.kotlinify.lang.PropertyNodeSubject
import io.t28.kotlinify.lang.RootElement
import io.t28.kotlinify.lang.RootNodeSubject
import io.t28.kotlinify.lang.TypeElement
import io.t28.kotlinify.lang.TypeNodeSubject
import io.t28.kotlinify.util.Ref
import io.t28.kotlinify.util.RefSubject
import kotlin.reflect.KClass

fun Subject.isInstanceOf(clazz: KClass<*>) {
    isInstanceOf(clazz.java)
}

inline fun <reified T> Subject.isInstanceOf() {
    isInstanceOf(T::class.java)
}

fun assertThat(actual: AnnotationValue): AnnotationValueSubject {
    return assertAbout(AnnotationValueSubject.factory()).that(actual)
}

fun assertThat(actual: PropertyElement): PropertyNodeSubject {
    return assertAbout(PropertyNodeSubject.factory()).that(actual)
}

fun assertThat(actual: RootElement): RootNodeSubject {
    return assertAbout(RootNodeSubject.factory()).that(actual)
}

fun assertThat(actual: TypeElement): TypeNodeSubject {
    return assertAbout(TypeNodeSubject.factory()).that(actual)
}

fun <T : Any> assertThat(actual: Ref<T>): RefSubject<T> {
    return assertAbout(RefSubject.factory<T>()).that(actual)
}
