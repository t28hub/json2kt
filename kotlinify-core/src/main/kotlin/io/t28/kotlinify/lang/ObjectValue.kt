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

/**
 * Represents an object value.
 *
 * @param reference The reference element of this value.
 * @param isNullable Whether this value is nullable.
 */
class ObjectValue(
    val reference: TypeNode,
    override val isNullable: Boolean = false
) : ValueNode() {
    override fun toString(): String = buildString {
        append(ObjectValue::class.simpleName)
        append("{")
        append("reference=$reference,")
        append("isNullable=$isNullable")
        append("}")
    }
}
