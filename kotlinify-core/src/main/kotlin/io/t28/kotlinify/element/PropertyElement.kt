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

/**
 * Represents a property element.
 *
 * @param name The name of property.
 * @param type The type of property.
 */
data class PropertyElement(
    val name: String,
    private val type: ElementType
) : Element {
    override fun asType(): ElementType {
        return type
    }
}
