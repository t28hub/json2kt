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

import kotlin.jvm.Throws

/**
 * Represents a type element.
 *
 * @param name The name of type.
 * @param supertype The supertype of type.
 * @param properties The properties of type.
 */
data class TypeElement(
    val name: String?,
    val supertype: ElementType = AnyType(),
    val properties: List<PropertyElement> = emptyList()
) : Element {
    @Throws(IllegalStateException::class)
    override fun asType(): ElementType {
        return name?.let {
            ObjectType(it)
        } ?: throw IllegalStateException("Type does not exist")
    }
}
