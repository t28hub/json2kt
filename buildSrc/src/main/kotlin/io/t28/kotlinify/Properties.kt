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
package io.t28.kotlinify

import groovy.lang.MissingPropertyException
import io.t28.kotlinify.impl.PropertiesImpl
import org.gradle.api.Project
import kotlin.jvm.Throws

/**
 * Definitions of the project root properties.
 */
interface Properties {
    /**
     * The Java properties.
     */
    val java: JavaProperties

    /**
     * The plugin properties.
     */
    val plugin: PluginProperties

    /**
     * The Sentry properties.
     */
    val sentry: SentryProperties
}
