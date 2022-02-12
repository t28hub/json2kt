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
package io.t28.kotlinify.idea.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidatorEx
import com.intellij.psi.PsiNameHelper
import io.t28.kotlinify.idea.message
import io.t28.kotlinify.idea.util.getPsiNameHelper

/**
 * Implementation of [InputValidatorEx] for checking whether input string is valid class name.
 */
class ClassNameValidator(private val nameHelper: PsiNameHelper) : InputValidatorEx {
    constructor(project: Project) : this(project.getPsiNameHelper())

    override fun getErrorText(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return message("action.new.file.dialog.error.name.empty")
        }

        if (!nameHelper.isQualifiedName(trimmed)) {
            return message("action.new.file.dialog.error.name.invalid", trimmed)
        }
        return null
    }
}
