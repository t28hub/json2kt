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
package io.t28.kotlinify.util

private const val DIRECTORY_DELIMITER = '/'
private const val EXTENSION_DELIMITER = '.'
private const val NO_INDEX = -1

/**
 * Returns filename from given string.
 */
fun String.getFilename(): String {
    val directoryIndex = lastIndexOf(DIRECTORY_DELIMITER)
    if (directoryIndex == NO_INDEX) {
        return this
    }
    return substring(directoryIndex + 1)
}

/**
 * Removes file extension from given string.
 */
fun String.removeFileExtension(): String {
    val extensionIndex = lastIndexOf(EXTENSION_DELIMITER)
    if (extensionIndex == NO_INDEX) {
        return this
    }

    val directoryIndex = lastIndexOf(DIRECTORY_DELIMITER)
    if (directoryIndex > extensionIndex) {
        return this
    }
    return substring(0, extensionIndex)
}
