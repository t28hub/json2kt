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
 * Represents a node.
 */
sealed interface Node {
    /**
     * Whether this [Node] is nullable.
     */
    val isNullable: Boolean

    /**
     * Whether this [Node] has children.
     */
    val hasChildren: Boolean
        get() = children().isNotEmpty()

    /**
     * Return a collection of the children of this [Node].
     *
     * @return The collection of the children nodes.
     */
    fun children(): Collection<Node>

    /**
     * Wrap this [Node] as a [NamedNode] with name and simple name.
     *
     * @param name The name of this node.
     * @param simpleName The simple name of this node.
     */
    fun named(name: String, simpleName: String): NamedNode<out Node> {
        return NamedNode(this, name = name, simpleName = simpleName)
    }
}
