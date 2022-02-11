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

import com.google.common.truth.Truth.assertThat
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class ClassNameValidatorTest : BasePlatformTestCase() {
    private lateinit var validator: ClassNameValidator

    @BeforeEach
    override fun setUp() {
        super.setUp()
        validator = ClassNameValidator(project)
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
    }

    @ParameterizedTest(name = "should return null when string is {0}")
    @MethodSource("provideValidFixtures")
    fun `getErrorText should return null when string is valid Java name`(input: String) {
        // Act
        val actual = validator.getErrorText(input)

        // Assert
        assertThat(actual).isNull()
    }

    @ParameterizedTest(name = "should return error text when string is {0}")
    @MethodSource("provideInvalidFixtures")
    fun `getErrorText should return error text when string is invalid Java name`(input: String) {
        // Act
        val actual = validator.getErrorText(input)

        // Assert
        assertThat(actual).isNotNull()
    }

    companion object {
        @JvmStatic
        fun provideValidFixtures() = listOf(
            "a",
            "A",
            "a1",
            "a$",
            "_a",
            "a_",
            "_a_",
            "a.b.c",
            "username",
            "userName",
            "UserName",
            "user_name",
            "USER_NAME",
            "user1name",
            "userName1",
            "user\$name",
            "UserName\$",
            "UserName\$\$",
        )

        @JvmStatic
        fun provideInvalidFixtures() = listOf(
            "",
            " ",
            "1",
            "@",
            "1A",
            "1\$",
            "a@",
            "@userName",
            "user-name",
            "user name",
            "1userName",
            "         ",
            "        1",
            "        @",
        )
    }
}
