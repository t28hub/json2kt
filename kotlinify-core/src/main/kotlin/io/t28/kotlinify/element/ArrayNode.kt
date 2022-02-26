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

import io.t28.kotlinify.util.firstOrElse
import kotlinx.collections.immutable.toImmutableList

/**
 * Represents an array node.
 *
 * @param isNullable Whether this node is nullable.
 * @param items The items of this node.
 */
data class ArrayNode(
    override val isNullable: Boolean = false,
    private val items: List<Node> = emptyList()
) : Node {
    override fun toString(): String = buildString {
        append(ArrayNode::class.simpleName)
        append("{")
        append("isNullable=$isNullable,")
        append("items=$items")
        append("}")
    }

    override fun children(): Collection<Node> {
        return items.toImmutableList()
    }

    fun componentNode(): Node {
        return items.firstOrElse(NullValue)
    }
}
