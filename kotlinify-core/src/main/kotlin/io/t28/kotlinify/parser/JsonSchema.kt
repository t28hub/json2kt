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
package io.t28.kotlinify.parser

import io.t28.kotlinify.serialization.serializers.DataTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonSchema(
    @SerialName("\$id")
    val id: String = "",
    @SerialName("\$schema")
    val schema: String,
    @SerialName("\$comment")
    val comment: String = "",
    @SerialName("\$defs")
    val defs: Map<String, Definition> = emptyMap(),
    val properties: Map<String, Definition> = emptyMap(),
    val required: Set<String> = emptySet(),
    @Serializable(with = DataTypeSerializer::class)
    override val type: DataType,
    override val title: String = "",
    override val description: String = "",
) : DataDefinition()
