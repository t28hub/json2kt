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

import kotlinx.collections.immutable.toImmutableList

/**
 * Represents an object node.
 *
 * @param isNullable Whether this node is nullable.
 * @param children The children of this node.
 */
data class ObjectNode(
    override val isNullable: Boolean = false,
    private val children: List<NamedNode<out Node>> = emptyList()
) : Node {
    override fun toString(): String = buildString {
        append(ObjectNode::class.simpleName)
        append("{")
        append("isNullable=$isNullable,")
        append("children=$children")
        append("}")
    }

    override fun children(): Collection<NamedNode<out Node>> {
        return children.toImmutableList()
    }
}
