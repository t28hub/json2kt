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
import io.t28.kotlinify.lang.Element
import io.t28.kotlinify.lang.PropertyElement
import io.t28.kotlinify.lang.ValueElement
import kotlinx.collections.immutable.toImmutableList

/**
 * Implementation of [PropertyElement].
 *
 * @param type The type of this property.
 * @param name The name of this property.
 * @param originalName The original name of this property.
 * @param isMutable Whether the property is mutable.
 * @param annotations The annotated annotations fo this type.
 */
internal class PropertyElementImpl(
    override val type: ValueElement,
    override val name: String,
    override val originalName: String,
    override val isMutable: Boolean = false,
    override val annotations: List<AnnotationValue> = emptyList()
) : PropertyElement {
    init {
        require(originalName.isNotEmpty()) { "Original name is empty string" }
        require(name.isNotEmpty()) { "Name is empty string" }
    }

    override fun toString(): String = buildString {
        append(PropertyElementImpl::class.simpleName)
        append("{")
        append("type=$type,")
        append("name=$name,")
        append("originalName=$originalName,")
        append("isMutable=$isMutable,")
        append("annotations=$annotations")
        append("}")
    }

    override fun copy(
        name: String,
        type: ValueElement,
        isMutable: Boolean,
        annotations: List<AnnotationValue>
    ): PropertyElement {
        return PropertyElementImpl(
            originalName = this.originalName,
            name = name,
            type = type,
            isMutable = isMutable,
            annotations = annotations.toImmutableList()
        )
    }
}
