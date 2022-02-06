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
package io.t28.json2kotlin.idea.ui

import com.intellij.json.JsonFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.text.Strings
import com.intellij.ui.dsl.builder.COLUMNS_SHORT
import com.intellij.ui.dsl.builder.LabelPosition
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.ui.layout.toBinding
import io.t28.json2kotlin.idea.message
import org.jetbrains.annotations.Nls
import org.jetbrains.kotlin.idea.util.application.runReadAction
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import javax.swing.JComponent

@Suppress("UnstableApiUsage")
class NewKotlinFileDialog(
    private val project: Project,
    state: InputState = InputState.empty()
) : DialogWrapper(project, true) {
    private val state: InputState

    private lateinit var jsonEditor: Editor

    init {
        title = message("action.new.file.dialog.title")
        isResizable = true
        setOKButtonText(message("action.new.file.dialog.button.ok"))
        setCancelButtonText(message("action.new.file.dialog.button.cancel"))
        this.state = state.copy()
        init()
    }

    override fun createCenterPanel(): JComponent {
        jsonEditor = EditorFactory.getInstance().let { factory ->
            val document = factory.createDocument(Strings.EMPTY_CHAR_SEQUENCE)
            val editor = factory.createEditor(document, project, JsonFileType.INSTANCE, false)
            editor.initialize()
        }

        return panel {
            nameInputRow()
            typeInputRow()
            textInputRow()
        }.apply {
            withMinimumWidth(MIN_DIALOG_WIDTH)
            withMinimumHeight(MIN_DIALOG_HEIGHT)
        }
    }

    private fun Panel.nameInputRow() = row(message("action.new.file.dialog.name.title")) {
        textField().apply {
            // Set Validation Input
            columns(COLUMNS_SHORT)
            bindText(state::name)
            focused()
        }
    }.layout(RowLayout.PARENT_GRID)

    fun getInputState(): InputState {
        return state.copy()
    }

    private fun Panel.typeInputRow() = row(message("action.new.file.dialog.type.title")) {
        val items = InputType.values().map { type ->
            type.displayName()
        }.toTypedArray()
        comboBox(items).apply {
            bindItem(state::type)
        }
    }.layout(RowLayout.PARENT_GRID)

    private fun Panel.textInputRow() = row {
        cell(jsonEditor.component).apply {
            label(message("action.new.file.dialog.text.title"), LabelPosition.TOP)
            horizontalAlign(HorizontalAlign.FILL)
            verticalAlign(VerticalAlign.FILL)
            resizableColumn()
            bind(
                componentGet = {
                    runReadAction { jsonEditor.document.text }
                },
                componentSet = { _, value ->
                    runWriteAction { jsonEditor.document.setText(value) }
                },
                state::text.toBinding()
            )
        }
    }.resizableRow()

    companion object {
        private const val MIN_DIALOG_WIDTH = 600
        private const val MIN_DIALOG_HEIGHT = 450

        private fun Editor.initialize(): Editor {
            if (this is EditorEx) {
                isEmbeddedIntoDialogWrapper = true
            }

            with(settings) {
                isWhitespacesShown = false
                isIndentGuidesShown = true
                isLineMarkerAreaShown = false
                isLineNumbersShown = true
                isFoldingOutlineShown = false
                isRightMarginShown = false
                isVirtualSpace = false
                additionalColumnsCount = 1
                additionalLinesCount = 0
            }

            with(colorsScheme) {
                setColor(EditorColors.CARET_ROW_COLOR, defaultBackground)
            }
            return this
        }
    }

    data class InputState(
        var name: String,
        var type: String,
        var text: String
    ) {
        companion object {
            fun empty(): InputState {
                return InputState(name = "", type = "JSON", text = "")
            }
        }
    }

    enum class InputType {
        JSON {
            override fun displayName(): String {
                return message("action.new.file.dialog.type.json")
            }
        },
        JSON_SCHEMA {
            override fun displayName(): String {
                return message("action.new.file.dialog.type.jsonschema")
            }
        };

        @Nls
        abstract fun displayName(): String
    }
}
