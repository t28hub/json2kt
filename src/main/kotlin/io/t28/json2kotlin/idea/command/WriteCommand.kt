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

import com.intellij.openapi.project.Project
import io.t28.json2kotlin.idea.util.runWriteAction
import kotlin.jvm.Throws

/**
 * Command implementation executed inside write action.
 *
 * @param project Current project.
 */
abstract class WriteCommand<T>(protected val project: Project) {
    /**
     * Compute a command.
     *
     * @return Computed result.
     */
    @Throws(Throwable::class)
    protected abstract fun compute(): T

    /**
     * Execute command inside write action.
     *
     * @return Executed result.
     * @see compute
     */
    @Throws(Throwable::class)
    fun execute(): T {
        return runWriteAction(project, this::compute)
    }
}
