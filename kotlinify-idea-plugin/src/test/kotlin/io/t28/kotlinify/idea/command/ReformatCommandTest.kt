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
package io.t28.kotlinify.idea.command

import com.google.common.truth.Truth.assertThat
import com.intellij.json.JsonFileType
import com.intellij.json.JsonLanguage
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.t28.kotlinify.idea.util.runWriteAction
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReformatCommandTest : BasePlatformTestCase() {
    @BeforeEach
    override fun setUp() {
        super.setUp()
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun `should reformat JSON string`() {
        // Act
        val inputText = """
            {"name":"Alice"}
        """.trimIndent()
        val action = ReformatCommand(project, inputText, JsonLanguage.INSTANCE)
        val actual = action.execute()

        // Assert
        assertThat(actual).isEqualTo("""
            {
              "name": "Alice"
            }
        """.trimIndent())
    }

    @Test
    fun `should reformat JSON string with SelectionModel`() {
        // Arrange
        val inputText = """
            {
                "name":"Alice"}
        """.trimIndent()
        myFixture.configureByText(JsonFileType.INSTANCE, inputText)
        runWriteAction(project) {
            with(myFixture.editor) {
                val startOffset = logicalPositionToOffset(LogicalPosition(1, 1))
                val endOffset = logicalPositionToOffset(LogicalPosition(2, 1))
                selectionModel.setSelection(startOffset, endOffset)
            }
        }

        // Act
        val action = ReformatCommand(project, inputText, JsonLanguage.INSTANCE, myFixture.editor.selectionModel)
        val actual = action.execute()

        // Assert
        assertThat(actual).isEqualTo("""
            {
              "name": "Alice"
            }
        """.trimIndent())
    }

    @Test
    fun `should reformat Kotlin string`() {
        // Act
        val inputText = """
            data class User(
            val name:String
            )
        """.trimIndent()
        val action = ReformatCommand(project, inputText, KotlinLanguage.INSTANCE)
        val actual = action.execute()

        // Assert
        assertThat(actual).isEqualTo("""
            data class User(
                    val name: String
            )
        """.trimIndent())
    }
}
