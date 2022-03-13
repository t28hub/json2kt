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

import io.t28.kotlinify.lang.ArrayValue
import io.t28.kotlinify.lang.ValueElement
import io.t28.kotlinify.util.firstOrElse

/**
 * Implementation of [ArrayValue].
 *
 * @param components The components of this value.
 * @param isNullable Whether this value is nullable.
 */
internal class ArrayValueImpl(
    private val components: List<ValueElement> = emptyList(),
    override val isNullable: Boolean = false
) : ArrayValue {
    constructor(
        component: ValueElement,
        isNullable: Boolean = false
    ) : this(listOf(component), isNullable)

    override val componentType: ValueElement
        get() = components.firstOrElse(NullValue)

    override fun toString(): String = buildString {
        append(ArrayValue::class.simpleName)
        append("{")
        append("componentType=$componentType,")
        append("isNullable=$isNullable")
        append("}")
    }
}
