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

import com.google.common.truth.Truth.assertThat
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class FilesTest : BasePlatformTestCase() {
    @BeforeEach
    override fun setUp() {
        super.setUp()
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
    }

    @Nested
    inner class AppendFileExtension {
        @ParameterizedTest(name = "\"{0}\".appendFileExtension({1}) should return \"{2}\"")
        @ArgumentsSource(FilenameFixtures::class)
        fun `should return a filename with given extension`(filePath: String, fileType: FileType, expected: String) {
            // Act
            val actual = filePath.appendFileExtension(fileType)

            // Assert
            assertThat(actual).isEqualTo(expected)
        }
    }

    companion object {
        class FilenameFixtures : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    arguments("", JavaFileType.INSTANCE, ""),
                    arguments("./", JavaFileType.INSTANCE, "./"),
                    arguments("../", JavaFileType.INSTANCE, "../"),
                    arguments("User", JavaFileType.INSTANCE, "User.java"),
                    arguments("User.java", JavaFileType.INSTANCE, "User.java"),
                    arguments("User.txt", JavaFileType.INSTANCE, "User.java"),
                )
            }
        }
    }
}
