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
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.intellij)
    alias(deps.plugins.kotlinx.serialization)
}

dependencies {
    implementation(project(":kotlinify-core"))
    implementation(deps.kotlin.stdlib)
    implementation(deps.kotlin.reflect)
    implementation(deps.kotlinx.serialization.json)

    testImplementation(deps.junit)
    testImplementation(deps.kotlin.test)
    testImplementation(deps.truth)
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    pluginName.set("Kotlinify")
    version.set("2021.3.2")
    plugins.addAll(
        "com.intellij.java",
        "org.jetbrains.kotlin"
    )
}

tasks {
    val javaVersion = "${JavaVersion.VERSION_11}"
    compileKotlin {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        kotlinOptions {
            jvmTarget = javaVersion
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }

    compileTestKotlin {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        kotlinOptions {
            jvmTarget = javaVersion
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }

    compileJava {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    compileTestJava {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    test {
        useJUnitPlatform()

        jvmArgs(
            "--add-opens=java.base/java.io=ALL-UNNAMED",
            "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/java.awt.event=ALL-UNNAMED",
            "--add-opens=java.desktop/javax.swing=ALL-UNNAMED",
            "--add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.font=ALL-UNNAMED",
        )
    }

    withType<Wrapper> {
        gradleVersion = "7.3"
    }

    runIde {
        autoReloadPlugins.set(true)
    }

    patchPluginXml {
        changeNotes.set(
            """
            Add change notes here.<br>
            <em>most HTML tags may be used</em>
        """.trimIndent()
        )
    }
}
