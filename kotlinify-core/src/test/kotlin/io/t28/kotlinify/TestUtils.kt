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
package io.t28.kotlinify

import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.jvm.Throws

@Throws(IOException::class)
fun Any.readResourceAsStream(path: String): InputStream {
    val stream = this::class.java.getResourceAsStream(path)
    requireNotNull(stream) { "Path '$path' does not exists" }
    return stream
}

@Throws(IOException::class)
fun Any.readResourceAsString(path: String, charset: Charset = Charsets.UTF_8): String {
    return readResourceAsStream(path).use { input ->
        input.readBytes().toString(charset)
    }
}
