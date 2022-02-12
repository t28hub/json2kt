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

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts

/**
 * Wrapper class for building and showing a [Notification].
 */
@Suppress("UnstableApiUsage")
class Notifications internal constructor(
    @NlsContexts.NotificationTitle private val title: String,
    @NlsContexts.NotificationContent private val content: String?,
    private val type: NotificationType
) {
    companion object {
        /**
         * Notification group id defined in the plugin.xml
         */
        private const val GROUP_ID = "io.t28.kotlinify.idea"

        /**
         * Create an information notification
         *
         * @param title Notification title text
         * @param content Notification content text
         */
        fun info(
            @NlsContexts.NotificationTitle title: String,
            @NlsContexts.NotificationContent content: String? = null,
        ): Notifications {
            return Notifications(title, content, NotificationType.INFORMATION)
        }
    }

    /**
     * Notify a notification.
     *
     * @param project Current project.
     */
    fun notify(project: Project?) {
        val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup(GROUP_ID)
        val notification = when (content) {
            null -> notificationGroup.createNotification(title, type)
            else -> notificationGroup.createNotification(title, content, type)
        }
        notification.notify(project)
    }
}
