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
package io.t28.json2kotlin.idea

import com.intellij.AbstractBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.Json2KotlinBundle"

/**
 * Resource bundle for Json2Kotlin plugin.
 */
object Json2KotlinBundle : AbstractBundle(BUNDLE)

/**
 * Retrieve a message from resource bundle.
 *
 * @param key Property key defined in [BUNDLE] file.
 * @param args Arguments referenced by the format.
 */
@Nls
fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg args: Any): String {
    "".format()
    return Json2KotlinBundle.getMessage(key, *args)
}
