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

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Represents a property node.
 *
 * @param value The value of this property.
 * @param name The name of this property.
 * @param originalName The original name of this property.
 * @param isMutable Whether the property is mutable.
 * @param annotations The annotated annotations fo this type.
 */
class PropertyNode(
    val value: ValueNode,
    val name: String,
    val originalName: String,
    val isMutable: Boolean = false,
    override val annotations: ImmutableList<AnnotationValue> = persistentListOf()
) : AnnotatedNode() {
    init {
        require(name.isNotEmpty()) { "Name is empty string" }
        require(originalName.isNotEmpty()) { "Original name is empty string" }
    }

    override fun toString(): String = buildString {
        append(PropertyNode::class.simpleName)
        append("{")
        append("value=$value,")
        append("name=$name,")
        append("originalName=$originalName,")
        append("isMutable=$isMutable,")
        append("annotations=$annotations")
        append("}")
    }

    override fun children(): Collection<Node> {
        return emptyList()
    }

    fun hasSameOriginalName(): Boolean {
        return name == originalName
    }

    fun copy(
        name: String = this.name,
        value: ValueNode = this.value,
        isMutable: Boolean = this.isMutable,
        annotations: List<AnnotationValue> = this.annotations
    ): PropertyNode {
        return PropertyNode(
            name = name,
            originalName = this.originalName,
            value = value,
            isMutable = isMutable,
            annotations = annotations.toImmutableList()
        )
    }
}
