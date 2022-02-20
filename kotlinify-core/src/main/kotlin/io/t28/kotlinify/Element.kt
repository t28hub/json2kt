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

sealed interface Element {
    val isNullable: Boolean

    fun markNullable(): Element
}

sealed interface PrimitiveElement : Element

data class BooleanElement(
    override val isNullable: Boolean = false
) : PrimitiveElement {
    override fun markNullable(): Element {
        return BooleanElement(isNullable = true)
    }
}

data class LongElement(
    override val isNullable: Boolean = false
) : PrimitiveElement {
    override fun markNullable(): Element {
        return LongElement(isNullable = true)
    }
}

data class NumberElement(
    override val isNullable: Boolean = false
) : PrimitiveElement {
    override fun markNullable(): Element {
        return NumberElement(isNullable = true)
    }
}

data class StringElement(
    override val isNullable: Boolean = false
) : PrimitiveElement {
    override fun markNullable(): Element {
        return StringElement(isNullable = true)
    }
}

object NullElement : PrimitiveElement {
    override val isNullable: Boolean = true

    override fun markNullable(): Element {
        return NullElement
    }
}

data class ArrayElement(
    val type: Element,
    override val isNullable: Boolean = false
) : Element {
    override fun markNullable(): Element {
        return ArrayElement(type = type, isNullable = true)
    }
}

data class ObjectElement(
    val properties: Map<String, Element>,
    override val isNullable: Boolean = false
) : Element {
    override fun markNullable(): Element {
        return ObjectElement(properties = properties, isNullable = true)
    }
}
