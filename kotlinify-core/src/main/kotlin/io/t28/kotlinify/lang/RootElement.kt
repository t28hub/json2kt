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

import io.t28.kotlinify.util.Ref
import io.t28.kotlinify.util.Ref.Companion.ref
import io.t28.kotlinify.util.addFirst
import kotlinx.collections.immutable.toImmutableList

typealias TypeElementRef = Ref<TypeElement>

/**
 * Represents a root node.
 *
 * @param references The references of children nodes.
 */
class RootElement internal constructor(
    private val references: MutableList<TypeElementRef> = mutableListOf()
) : Element {
    override fun toString() = buildString {
        append(RootElement::class.simpleName)
        append("{")
        append("${children()}")
        append("}")
    }

    /**
     * Returns children of children elements.
     */
    fun children(): List<TypeElement> {
        return references.map(TypeElementRef::get).toImmutableList()
    }

    /**
     * Returns references of children elements.
     */
    fun references(): List<TypeElementRef> {
        return references.toImmutableList()
    }

    /**
     * Inserts a child node.
     *
     * @param node The node to be added.
     * @return The reference of added node.
     */
    internal fun add(node: TypeElement): TypeElementRef {
        val nodeRef = node.ref()
        references.addFirst(nodeRef)
        return nodeRef
    }
}
