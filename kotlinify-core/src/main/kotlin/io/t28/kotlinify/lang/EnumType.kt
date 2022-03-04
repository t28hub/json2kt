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

class EnumType(
    name: String,
    annotations: Collection<AnnotationValue> = emptyList(),
    properties: Collection<PropertyNode> = emptyList(),
    val constants: Collection<String> = emptySet()
) : TypeNode(name, annotations.toImmutableList(), properties.toImmutableList()) {
    override fun toString(): String = buildString {
        append(EnumType::class.simpleName)
        append("{")
        append("name=$name,")
        append("annotations=$annotations,")
        append("properties=$properties,")
        append("constants=$constants")
        append("}")
    }

    override fun <P, R> accept(visitor: Visitor<P, R>, parameter: P): R {
        return visitor.visitEnum(this, parameter)
    }
}
