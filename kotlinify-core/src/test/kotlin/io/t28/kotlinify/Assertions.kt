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
import com.google.common.truth.Truth
import io.t28.kotlinify.lang.PropertyNode
import io.t28.kotlinify.lang.PropertyNodeSubject
import io.t28.kotlinify.lang.TypeNode
import io.t28.kotlinify.lang.TypeNodeSubject
import kotlin.reflect.KClass

fun Subject.isInstanceOf(clazz: KClass<*>) {
    isInstanceOf(clazz.java)
}

inline fun <reified T> Subject.isInstanceOf() {
    isInstanceOf(T::class.java)
}

fun assertThat(actual: PropertyNode): PropertyNodeSubject {
    return Truth.assertAbout(PropertyNodeSubject.factory()).that(actual)
}

fun assertThat(actual: TypeNode): TypeNodeSubject {
    return Truth.assertAbout(TypeNodeSubject.factory()).that(actual)
}
