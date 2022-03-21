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
 * Retrieves declared properties
 */
fun Project.properties(): Properties {
    return PropertiesImpl(this)
}

/**
 * Retrieves a property by key as a String
 *
 * @param key Property key.
 * @return Property value as a String.
 * @throws MissingPropertyException Property key does not exist in properties.
 */
@Throws(MissingPropertyException::class)
internal fun Project.getPropertyAsString(key: String): String {
    val property = findProperty(key)
    return property?.toString() ?: throw MissingPropertyException("Property '$key' does not exist")
}
