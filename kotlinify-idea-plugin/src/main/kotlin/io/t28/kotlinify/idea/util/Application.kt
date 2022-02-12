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

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import kotlin.jvm.Throws

/**
 * Run an action inside read action.
 *
 * @param action An action to be executed inside read action.
 */
@Throws(Throwable::class)
fun <T> runReadAction(action: () -> T): T {
    return ApplicationManager.getApplication().runReadAction<T>(action)
}

/**
 * Run an action inside write action.
 *
 * @param project Current project.
 * @param action An action to be executed inside write action.
 */
@Throws(Throwable::class)
fun <T> runWriteAction(project: Project, action: () -> T): T {
    return WriteCommandAction.writeCommandAction(project).compute<T, Throwable>(action)
}
