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
package io.t28.kotlinify.parser.naming

import io.t28.kotlinify.parser.naming.JavaNameStrategy.Companion.isJavaIdentifier

/**
 * Naming strategy to generate an unique name.
 *
 * @param nameStrategy The [NameStrategy] to be used inside this class.
 * @param maxRetries The maximum number of retries.
 * @param reserved The reserved names.
 */
internal class UniqueNameStrategy internal constructor(
    private val nameStrategy: NameStrategy,
    private val maxRetries: Int = DEFAULT_MAX_RETRIES,
    reserved: Set<String> = emptySet()
) : NameStrategy {
    /**
     * The set of names that have been already used.
     */
    private val allocated: MutableSet<String>

    init {
        require(maxRetries > 0) { "The maximum number of retries is less than 1" }
        allocated = reserved.toMutableSet()
    }

    override fun apply(name: String): String {
        var originalName = name
        var candidateName = nameStrategy.apply(name)
        repeat(maxRetries) {
            if (candidateName.isJavaIdentifier() and allocated.add(candidateName)) {
                return candidateName
            }
            originalName += ADDITIONAL_CHARACTER
            candidateName = nameStrategy.apply(originalName)
        }
        throw IllegalStateException("Could not apply for '$name' due to exceeding max retries")
    }

    companion object {
        const val DEFAULT_MAX_RETRIES = 10
        const val ADDITIONAL_CHARACTER = '\$'
    }
}
