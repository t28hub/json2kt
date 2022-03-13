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
 * Represents a type node.
 */
interface TypeElement : AnnotatedElement {
    /**
     * The name of this type.
     */
    val name: String

    /**
     * The kind of this type.
     */
    val kind: TypeKind

    /**
     * The properties of this type.
     */
    val properties: List<PropertyElement>

    /**
     * The enum constants of this type.
     *
     * @see TypeKind.ENUM
     */
    val enumConstants: Set<String>

    /**
     * Returns a copied type with given parameters.
     *
     * @param name The new name of this type.
     * @param kind The new kind of this type.
     * @param properties The new properties of this type.
     * @param annotations The new annotations of this type.
     * @param enumConstants The new enum constants of this type.
     */
    fun copy(
        name: String = this.name,
        kind: TypeKind = this.kind,
        properties: List<PropertyElement> = this.properties,
        annotations: List<AnnotationValue> = this.annotations,
        enumConstants: Set<String> = this.enumConstants
    ): TypeElement
}
