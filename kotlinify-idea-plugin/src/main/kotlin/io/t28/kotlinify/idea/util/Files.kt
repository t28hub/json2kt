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
package io.t28.kotlinify.idea.util

import com.intellij.openapi.fileTypes.FileType

private const val DIRECTORY_DELIMITER = '/'
private const val EXTENSION_DELIMITER = '.'
private const val NO_INDEX = -1

/**
 * Returns the string with an extension of given [FileType].
 *
 * @param fileType The extension of [FileType].
 */
fun String.appendFileExtension(fileType: FileType): String {
    if (isEmpty()) {
        return this
    }

    val extensionIndex = lastIndexOf(EXTENSION_DELIMITER)
    if (extensionIndex == NO_INDEX) {
        return buildString {
            append(this@appendFileExtension)
            append(EXTENSION_DELIMITER)
            append(fileType.defaultExtension)
        }
    }

    val directoryIndex = lastIndexOf(DIRECTORY_DELIMITER)
    if (directoryIndex > extensionIndex) {
        return this
    }

    val fileName = substring(0, extensionIndex)
    return buildString {
        append(fileName)
        append(EXTENSION_DELIMITER)
        append(fileType.defaultExtension)
    }
}
