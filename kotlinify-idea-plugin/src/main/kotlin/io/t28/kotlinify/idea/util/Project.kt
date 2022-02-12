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

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiNameHelper
import com.intellij.psi.codeStyle.CodeStyleManager
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Whether the current project is available.
 */
@OptIn(ExperimentalContracts::class)
fun Project?.isAvailable(): Boolean {
    contract {
        returns(true) implies (this@isAvailable != null)
    }
    return this != null && isInitialized && !isDisposed
}

/**
 * Retrieve [CodeStyleManager] from current project.
 */
fun Project.getCodeStyleManager(): CodeStyleManager {
    return CodeStyleManager.getInstance(this)
}

/**
 * Retrieve [PsiFileFactory] from current project.
 */
fun Project.getPsiFileFactory(): PsiFileFactory {
    return PsiFileFactory.getInstance(this)
}

/**
 * Retrieve [PsiNameHelper] from current project.
 */
fun Project.getPsiNameHelper(): PsiNameHelper {
    return PsiNameHelper.getInstance(this)
}

/**
 * Retrieve [ProjectRootManager] from current project.
 */
fun Project.getProjectRootManager(): ProjectRootManager {
    return ProjectRootManager.getInstance(this)
}
