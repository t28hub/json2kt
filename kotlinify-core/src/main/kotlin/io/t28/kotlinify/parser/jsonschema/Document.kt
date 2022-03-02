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
package io.t28.kotlinify.parser.jsonschema

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val `$id`: String? = null,
    val `$schema`: String? = null,
    val `$comment`: String? = null,
    val `$defs`: Map<String, Definition> = emptyMap(),
    val items: List<Definition> = emptyList(),
    val properties: Map<String, Definition> = emptyMap(),
    val required: Set<String> = emptySet(),
    override val type: DataType = DataType.NULL,
    override val title: String? = null,
    override val description: String? = null,
) : DataDefinition() {
    fun asArray(): ArrayDefinition {
        requireType(DataType.ARRAY)
        return ArrayDefinition(
            title = title,
            description = description,
            items = items.toImmutableList()
        )
    }

    fun asObject(): ObjectDefinition {
        requireType(DataType.OBJECT)
        return ObjectDefinition(
            title = title,
            description = description,
            properties = properties.toImmutableMap(),
            required = required.toImmutableSet()
        )
    }

    private fun requireType(requiredType: DataType) {
        require(type == requiredType) { "Type '$type' is not '$requiredType'" }
    }
}
