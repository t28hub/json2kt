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

import kotlinx.collections.immutable.toImmutableList

/**
 * Represents a type element.
 *
 * @param name The name of this type.
 * @param annotations The annotated annotations fo this type.
 * @param properties The children of this type.
 */
abstract class TypeNode(
    val name: String,
    override val annotations: Collection<AnnotationValue>,
    internal val properties: Collection<PropertyNode>,
) : AnnotatedNode() {
    override fun toString(): String = buildString {
        append(this@TypeNode::class.simpleName)
        append("{")
        append("name=$name,")
        append("annotations=$annotations,")
        append("properties=$properties")
        append("}")
    }

    override fun children(): Collection<PropertyNode> {
        return properties.toImmutableList()
    }

    abstract fun <P, R> accept(visitor: Visitor<P, R>, parameter: P): R

    interface Visitor<P, R> {
        fun visitClass(node: ClassType, parameter: P): R

        fun visitEnum(node: EnumType, parameter: P): R

        fun visitInterface(node: InterfaceType, parameter: P): R
    }
}
