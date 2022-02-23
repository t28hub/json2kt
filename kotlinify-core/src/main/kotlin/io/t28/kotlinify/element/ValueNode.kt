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
package io.t28.kotlinify.element

/**
 * Represents a value node.
 */
sealed class ValueNode<T> : Node {
    /**
     * The value of this node.
     */
    abstract val value: T

    override fun toString(): String = buildString {
        append(this@ValueNode::class.simpleName)
        append("{")
        append("value=$value")
        append("}")
    }

    override fun children(): Collection<Node> {
        return emptyList()
    }
}

/**
 * Represents a null value node.
 */
object NullValue : ValueNode<Any?>() {
    override val value: Any? = null

    override val isNullable: Boolean = true
}

/**
 * Represents a boolean value node.
 *
 * @param value The value of this node.
 * @param isNullable Whether this node is nullable.
 */
data class BooleanValue(
    override val value: Boolean,
    override val isNullable: Boolean = false
) : ValueNode<Boolean>()

/**
 * Represents an int value node.
 *
 * @param value The value of this node.
 * @param isNullable Whether this node is nullable.
 */
data class IntValue(
    override val value: Int,
    override val isNullable: Boolean = false
) : ValueNode<Int>()

/**
 * Represents a float value node.
 *
 * @param value The value of this node.
 * @param isNullable Whether this node is nullable.
 */
data class FloatValue(
    override val value: Float,
    override val isNullable: Boolean = false
) : ValueNode<Float>()

/**
 * Represents a string value node.
 *
 * @param value The value of this node.
 * @param isNullable Whether this node is nullable.
 */
data class StringValue(
    override val value: String,
    override val isNullable: Boolean = false
) : ValueNode<String>()
