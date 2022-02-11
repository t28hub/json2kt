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

import com.intellij.openapi.ui.InputValidatorEx
import io.t28.json2kotlin.idea.message
import org.jetbrains.annotations.Nls

enum class Format {
    JSON {
        override fun displayName(): String {
            return message("action.new.file.dialog.format.json")
        }

        override fun inputValidator(): InputValidatorEx {
            return JsonValidator
        }
    },
    JSON_SCHEMA {
        override fun displayName(): String {
            return message("action.new.file.dialog.format.jsonschema")
        }

        override fun inputValidator(): InputValidatorEx {
            return JsonSchemaValidator
        }
    };

    @Nls
    abstract fun displayName(): String

    abstract fun inputValidator(): InputValidatorEx

    companion object {
        internal fun findByDisplayName(name: String): Format {
            return values().first { type -> type.displayName() == name }
        }
    }
}
