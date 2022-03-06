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
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

/**
 * Represents a type element.
 *
 * @param name The name of this type.
 * @param kind The kind of this type.
 * @param properties The properties of this type.
 * @param enumConstants The enum constants of this type.
 * @param annotations The annotated annotations of this type.
 */
class TypeNode(
    val name: String,
    val kind: TypeKind,
    val properties: ImmutableList<PropertyNode> = persistentListOf(),
    val enumConstants: ImmutableSet<String> = persistentSetOf(),
    override val annotations: ImmutableList<AnnotationValue> = persistentListOf(),
) : AnnotatedNode() {
    constructor(
        name: String,
        kind: TypeKind,
        properties: List<PropertyNode> = emptyList(),
        annotations: List<AnnotationValue> = emptyList(),
        enumConstants: Set<String> = emptySet()
    ) : this(
        name = name,
        kind = kind,
        properties = properties.toImmutableList(),
        annotations = annotations.toImmutableList(),
        enumConstants = enumConstants.toImmutableSet()
    )

    override fun toString(): String = buildString {
        append(this@TypeNode::class.simpleName)
        append("{")
        append("name=$name,")
        append("properties=$properties,")
        append("annotations=$annotations")
        append("}")
    }

    override fun children(): Collection<PropertyNode> {
        return properties.toImmutableList()
    }

    fun copy(
        kind: TypeKind = this.kind,
        properties: List<PropertyNode> = this.properties,
        annotations: List<AnnotationValue> = this.annotations,
        enumConstants: Set<String> = this.enumConstants
    ): TypeNode {
        return TypeNode(
            name = name,
            kind = kind,
            properties = properties.toImmutableList(),
            annotations = annotations.toImmutableList(),
            enumConstants = enumConstants.toImmutableSet()
        )
    }

    enum class TypeKind {
        CLASS,
        ENUM,
        INTERFACE
    }
}
