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

/**
 * Represents a property node.
 */
interface PropertyElement : AnnotatedElement {
    /**
     * The original name of this property.
     */
    val originalName: String

    /**
     * The name of this property.
     */
    val name: String

    /**
     * The type of this property.
     */
    val type: ValueElement

    /**
     * Whether this property is mutable.
     */
    val isMutable: Boolean

    /**
     * Returns a copied property with given parameters.
     *
     * @param name The new name of this property.
     * @param type The new type of this property.
     * @param isMutable Whether the new property is mutable.
     * @param annotations The new annotations of this property.
     */
    fun copy(
        name: String = this.name,
        type: ValueElement = this.type,
        isMutable: Boolean = this.isMutable,
        annotations: List<AnnotationValue> = this.annotations
    ): PropertyElement
}
