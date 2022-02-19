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
package io.t28.kotlinify.serialization.serializers

import kotlin.jvm.Throws
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder

/**
 * Convert decoder as a [JsonDecoder]
 *
 * @throws IllegalArgumentException [Decoder] is not instance of [JsonDecoder]
 */
@Throws(IllegalArgumentException::class)
fun Decoder.asJsonDecoder(): JsonDecoder {
    require(this is JsonDecoder) {
        "Decoder '${this::class.simpleName}' is not an instance of JsonDecoder"
    }
    return this
}
