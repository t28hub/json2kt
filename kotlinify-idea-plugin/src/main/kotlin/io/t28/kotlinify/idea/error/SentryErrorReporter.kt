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
package io.t28.kotlinify.idea.error

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.protocol.Message
import io.t28.kotlinify.idea.BuildConfig

@Service(Level.APP)
class SentryErrorReporter(
    private val applicationInfo: ApplicationInfo = service()
) : ErrorReporter {
    override fun error(error: Throwable, message: String?) {
        val sentryEvent = SentryEvent(error).apply {
            level = SentryLevel.ERROR
            this.message = message?.let {
                Message().apply { this.message = it }
            }
            release = "${BuildConfig.PLUGIN_NAME}:${BuildConfig.PLUGIN_VERSION}"
            setTag(TAG_IDE_VERSION, applicationInfo.fullVersion)
            setTag(TAG_IDE_API_VERSION, applicationInfo.apiVersion)
        }
        report(sentryEvent)
    }

    override fun error(event: IdeaLoggingEvent) {
        val sentryEvent = SentryEvent(event.throwable).apply {
            level = SentryLevel.ERROR
            message = Message().apply {
                message = event.message
            }
            release = "${BuildConfig.PLUGIN_NAME}:${BuildConfig.PLUGIN_VERSION}"
            setTag(TAG_IDE_VERSION, applicationInfo.fullVersion)
            setTag(TAG_IDE_API_VERSION, applicationInfo.apiVersion)
        }
        report(sentryEvent)
    }

    private fun report(event: SentryEvent) {
        Sentry.captureEvent(event)
    }

    companion object {
        private const val TAG_IDE_VERSION = "ide.version"
        private const val TAG_IDE_API_VERSION = "ide.api.version"
    }
}
