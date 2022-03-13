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
package io.t28.kotlinify.interceptor

import io.t28.kotlinify.lang.PropertyElement
import io.t28.kotlinify.lang.TypeElement
import kotlinx.collections.immutable.toImmutableList

typealias TypeInterceptor = Interceptor<TypeElement>
typealias PropertyInterceptor = Interceptor<PropertyElement>

class Execution(
    private val typeInterceptors: List<TypeInterceptor>,
    private val propertyInterceptors: List<PropertyInterceptor>
) {
    fun execute(node: TypeElement): TypeElement {
        val proceeded = typeInterceptors.fold(node) { current, interceptor ->
            interceptor.intercept(current)
        }
        val proceededProperties = node.properties.map { property ->
            execute(property)
        }
        return proceeded.copy(properties = proceededProperties.toImmutableList())
    }

    private fun execute(property: PropertyElement): PropertyElement {
        return propertyInterceptors.fold(property) { current, interceptor ->
            interceptor.intercept(current)
        }
    }
}
