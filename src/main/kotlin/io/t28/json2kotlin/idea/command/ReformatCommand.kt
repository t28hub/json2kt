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
package io.t28.json2kotlin.idea.command

import com.intellij.lang.Language
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.project.Project
import io.t28.json2kotlin.idea.util.getCodeStyleManager
import io.t28.json2kotlin.idea.util.getPsiFileFactory

/**
 * Reformat text as a language with [SelectionModel].
 *
 * @param project Current project.
 * @param text Text to be reformatted.
 * @param language Language for the text.
 * @param selectionModel Selection model if exists.
 */
class ReformatCommand(
    project: Project,
    private val text: String,
    private val language: Language,
    private val selectionModel: SelectionModel? = null
) : WriteCommand<String>(project) {
    override fun compute(): String {
        val textFile = project.getPsiFileFactory().createFileFromText(FILE_NAME, language, text)
        val codeStyleManager = project.getCodeStyleManager()
        if (selectionModel != null && selectionModel.hasSelection()) {
            codeStyleManager.reformatText(textFile, selectionModel.selectionStart, selectionModel.selectionEnd)
        } else {
            codeStyleManager.reformat(textFile)
        }
        return textFile.text
    }

    companion object {
        private const val FILE_NAME = "dummy.txt"
    }
}
