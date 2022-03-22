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
package io.t28.kotlinify.impl

import io.t28.kotlinify.SentryProperties
import org.gradle.api.Project

/**
 * Implementation of [SentryProperties].
 *
 * @param project The current project.
 */
internal class SentryPropertiesImpl(private val project: Project) : SentryProperties {
    override val dsn: String by lazy {
        "${System.getenv()["SENTRY_DNS"]}"
    }

    override val environment: String by lazy {
        "${System.getenv()["SENTRY_ENVIRONMENT"]}"
    }
}
