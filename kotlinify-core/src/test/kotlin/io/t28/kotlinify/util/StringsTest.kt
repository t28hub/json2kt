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

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class StringsTest {
    @ParameterizedTest(name = "\"{0}\".getFilename should return \"{1}\"")
    @ArgumentsSource(GetFilenameFixtures::class)
    fun `getFilename should return filename`(input: String, expected: String) {
        // Act
        val actual = input.getFilename()

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest(name = "\"{0}\".removeFileExtension should return \"{1}\"")
    @ArgumentsSource(RemoveFileExtensionFixtures::class)
    fun `removeFileExtension should return path without extension`(input: String, expected: String) {
        // Act
        val actual = input.removeFileExtension()

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        class GetFilenameFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("", ""),
                    arguments("/", ""),
                    arguments("../", ""),
                    arguments("example", "example"),
                    arguments("example.txt", "example.txt"),
                    arguments("path/to/example", "example"),
                    arguments("path/to/example.txt", "example.txt"),
                    arguments("/path/to/example", "example"),
                    arguments("/path/to/example.txt", "example.txt"),
                    arguments("../path/to/example", "example"),
                    arguments("../path/to/example.txt", "example.txt")
                )
            }
        }

        class RemoveFileExtensionFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("", ""),
                    arguments("/", "/"),
                    arguments("../", "../"),
                    arguments("example", "example"),
                    arguments("example.txt", "example"),
                    arguments("path/to/example", "path/to/example"),
                    arguments("path/to/example.txt", "path/to/example"),
                    arguments("/path/to/example", "/path/to/example"),
                    arguments("/path/to/example.txt", "/path/to/example"),
                    arguments("../path/to/example", "../path/to/example"),
                    arguments("../path/to/example.txt", "../path/to/example")
                )
            }
        }
    }
}
