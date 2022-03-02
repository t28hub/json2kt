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

import io.t28.kotlinify.serialization.serializers.WrapValueSerializer
import io.t28.kotlinify.util.firstOrElse
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.EncodeDefault.Mode.ALWAYS
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
class ArrayDefinition(
    override val title: String? = null,
    override val description: String? = null,
    @Serializable(with = WrapValueSerializer::class)
    @EncodeDefault(ALWAYS)
    internal val items: List<Definition> = listOf(NullDefinition())
) : DataDefinition() {
    @EncodeDefault(ALWAYS)
    override val type: DataType = DataType.ARRAY

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun containsNull(): Boolean {
        return items.any { item ->
            item is NullDefinition
        }
    }

    fun firstOrElse(default: Definition): Definition {
        return items.firstOrElse(default)
    }
}
