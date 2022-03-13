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

import io.t28.kotlinify.lang.ObjectValue
import io.t28.kotlinify.lang.TypeElement
import io.t28.kotlinify.lang.TypeElementRef

/**
 * Implementation of [ObjectValue].
 *
 * @param reference The reference element of this value.
 * @param isNullable Whether this value is nullable.
 */
internal class ObjectValueImpl(
    private val reference: TypeElementRef,
    override val isNullable: Boolean = false
) : ObjectValue {
    override val definition: TypeElement
        get() = reference.get()

    override fun toString(): String = buildString {
        append(ObjectValue::class.simpleName)
        append("{")
        append("declaration=$definition,")
        append("isNullable=$isNullable")
        append("}")
    }
}
