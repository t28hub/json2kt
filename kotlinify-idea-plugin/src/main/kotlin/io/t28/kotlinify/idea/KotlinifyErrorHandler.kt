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

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.util.Consumer
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel.ERROR
import io.sentry.protocol.Message
import io.t28.kotlinify.idea.error.ErrorReporter
import java.awt.Component

class KotlinifyErrorHandler(
    private val errorReporter: ErrorReporter = service()
) : ErrorReportSubmitter() {
    init {
        Sentry.init { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.environment = BuildConfig.SENTRY_ENVIRONMENT
            options.tracesSampleRate = BuildConfig.SENTRY_SAMPLE_RATE
        }
    }

    override fun getReportActionText(): String {
        return "Send to Kotlinify"
    }

    override fun submit(
        events: Array<out IdeaLoggingEvent>,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<in SubmittedReportInfo>
    ): Boolean {
        events.forEach { event ->
            errorReporter.error(event)
        }
        return true
    }
}
