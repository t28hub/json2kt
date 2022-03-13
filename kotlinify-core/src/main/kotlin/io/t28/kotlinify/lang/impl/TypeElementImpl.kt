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
package io.t28.kotlinify.lang.impl

import io.t28.kotlinify.lang.AnnotationValue
import io.t28.kotlinify.lang.PropertyElement
import io.t28.kotlinify.lang.TypeKind
import io.t28.kotlinify.lang.TypeElement
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

/**
 * Implementation of [TypeElement].
 *
 * @param name The name of this type.
 * @param kind The kind of this type.
 * @param properties The properties of this type.
 * @param enumConstants The enum constants of this type.
 * @param annotations The annotated annotations of this type.
 */
internal class TypeElementImpl(
    override val name: String,
    override val kind: TypeKind,
    override val properties: List<PropertyElement> = emptyList(),
    override val enumConstants: Set<String> = emptySet(),
    override val annotations: List<AnnotationValue> = emptyList(),
) : TypeElement {
    internal constructor(name: String, kind: TypeKind) : this(
        name = name,
        kind = kind,
        properties = emptyList(),
        annotations = emptyList(),
        enumConstants = emptySet()
    )

    override fun toString(): String = buildString {
        append(this@TypeElementImpl::class.simpleName)
        append("{")
        append("name=$name,")
        append("kind=$kind,")
        append("properties=$properties,")
        append("annotations=$annotations,")
        append("enumConstants=$enumConstants")
        append("}")
    }

    override fun copy(
        name: String,
        kind: TypeKind,
        properties: List<PropertyElement>,
        annotations: List<AnnotationValue>,
        enumConstants: Set<String>
    ): TypeElementImpl {
        return TypeElementImpl(
            name = name,
            kind = kind,
            properties = properties.toImmutableList(),
            annotations = annotations.toImmutableList(),
            enumConstants = enumConstants.toImmutableSet()
        )
    }
}
