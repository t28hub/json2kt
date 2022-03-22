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
package io.t28.kotlinify.idea

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.JavaDirectoryService
import io.t28.kotlinify.idea.ui.NewKotlinFileDialog
import io.t28.kotlinify.idea.util.getProjectRootManager
import io.t28.kotlinify.idea.util.ideView
import io.t28.kotlinify.idea.util.isAvailable
import io.t28.kotlinify.Kotlinify
import io.t28.kotlinify.idea.ui.Format
import io.t28.kotlinify.idea.util.appendFileExtension
import io.t28.kotlinify.idea.util.getPsiFileFactory
import io.t28.kotlinify.idea.util.runWriteAction
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.KotlinIcons

/**
 * Action class for creating a Kotlin file from JSON or JSON Schema.
 */
class NewKotlinFileAction : AnAction(KotlinIcons.FILE) {
    @Suppress("ReturnCount")
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        if (!project.isAvailable()) {
            return
        }

        val logger = Logger.getInstance("Kotlinify[${project.name}]")
        val selected = e.ideView?.orChooseDirectory
        if (selected == null) {
            logger.info("Selected directory does not exist")
            return
        }

        val packageName = JavaDirectoryService.getInstance().getPackage(selected)
        if (packageName == null) {
            logger.info("Cannot get a package name from ${selected.name}")
            return
        }

        val dialog = NewKotlinFileDialog(project)
        if (!dialog.showAndGet()) {
            return
        }

        val state = dialog.getCurrentState()
        val builder = when (state.format) {
            Format.JSON -> Kotlinify {}.fromJson(state.content)
            Format.JSON_SCHEMA -> Kotlinify {}.fromJsonSchema(state.content)
        }

        val fileType = KotlinFileType.INSTANCE
        val fileName = state.name.appendFileExtension(fileType)
        logger.info("Creating a ${fileType.name} file '$fileName'")

        runWriteAction(project) {
            val classFile = builder.toKotlin(packageName.qualifiedName, fileName = fileName)
            val fileFactory = project.getPsiFileFactory()
            val createdFile = fileFactory.createFileFromText(fileName, fileType, classFile)
            selected.add(createdFile)
            logger.warn("File '$fileName' is created")
        }
        TODO()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = isEnabled(e)
    }

    private fun isEnabled(e: AnActionEvent): Boolean {
        val project = e.project
        if (!project.isAvailable()) {
            return false
        }

        val ideView = e.ideView ?: return false
        val projectFileIndex = project.getProjectRootManager().fileIndex
        return ideView.directories.any { directory ->
            projectFileIndex.isInSource(directory.virtualFile)
        }
    }

}
