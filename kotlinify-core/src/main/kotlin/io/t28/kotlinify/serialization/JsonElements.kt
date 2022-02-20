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
package io.t28.kotlinify.serialization

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

/**
 * Returns the first element, or [default] if the [JsonArray] is empty.
 *
 * @param default Default value if the [JsonArray] is empty.
 * @return The first element or [default].
 */
fun JsonArray.firstOrElse(default: JsonElement): JsonElement {
    return firstOrNull() ?: default
}
