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
package io.t28.kotlinify.util

/**
 * References of instance of [T].
 *
 * @param T The type of instance.
 * @param value The instance to be referenced.
 */
class Ref<T : Any> private constructor(private var value: T) {
    override fun toString() = buildString {
        append(Ref::class.simpleName)
        append("<${value::class.simpleName}>")
        append("{")
        append("value=$value")
        append("}")
    }

    /**
     * Returns the referenced value.
     */
    fun get(): T {
        return value
    }

    /**
     * Sets the new value.
     *
     * @param newValue The new value to be referenced.
     */
    fun set(newValue: T) {
        value = newValue
    }

    companion object {
        /**
         * Creates a reference from instance.
         *
         * @param T The type of instance.
         */
        fun <T : Any> T.ref(): Ref<T> {
            return Ref(this)
        }
    }
}
