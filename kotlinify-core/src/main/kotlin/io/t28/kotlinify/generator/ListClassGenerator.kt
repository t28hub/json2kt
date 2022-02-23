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
package io.t28.kotlinify.generator

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.t28.kotlinify.element.ArrayNode
import io.t28.kotlinify.util.addFirst

class ListClassGenerator(packageName: String) : ClassGenerator<ArrayNode>(packageName) {
    override fun generate(className: String, node: ArrayNode): Collection<TypeSpec> {
        val itemClassName = "$className$ITEM_CLASS_SUFFIX"
        val generated = node.asTypeSpecs(itemClassName)

        val rootTypeSpec = TypeSpec.classBuilder(className).apply {
            val componentNode = node.componentNode().named(itemClassName, itemClassName)
            val superclass = ArrayList::class.asTypeName().parameterizedBy(componentNode.asTypeName(packageName))
            superclass(superclass)
        }.build()

        return generated.toMutableList().apply {
            addFirst(rootTypeSpec)
        }.toList()
    }

    companion object {
        private const val ITEM_CLASS_SUFFIX = "_Item"
    }
}
