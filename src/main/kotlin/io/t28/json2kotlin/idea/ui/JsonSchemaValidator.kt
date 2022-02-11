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
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * Implementation of [InputValidatorEx] for checking whether input string is valid JSON Schema.
 */
object JsonSchemaValidator : InputValidatorEx {
    private const val JSON_SCHEMA_KEYWORD = "\$schema"

    override fun getErrorText(input: String): String? {
        if (input.isEmpty()) {
            return message("action.new.file.dialog.error.text.empty")
        }

        val json = try {
            Json.parseToJsonElement(input)
        } catch (e: SerializationException) {
            return message("action.new.file.dialog.error.text.unparseable")
        }

        if (json !is JsonObject) {
            return message("action.new.file.dialog.error.text.invalid.jsonschema")
        }

        if (!json.containsKey(JSON_SCHEMA_KEYWORD)) {
            return message("action.new.file.dialog.error.text.invalid.jsonschema")
        }
        return null
    }
}
