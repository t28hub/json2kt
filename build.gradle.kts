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
import groovy.lang.MissingPropertyException
import info.solidsoft.gradle.pitest.PitestPluginExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import kotlin.jvm.Throws
import kotlinx.kover.api.CoverageEngine.INTELLIJ

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.detekt)
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.kotlinx.kover)
    alias(deps.plugins.pitest)
}

/**
 * Retrieve a property by key as a String
 *
 * @param key Property key.
 * @return Property value as a String.
 * @throws MissingPropertyException Property key does not exist in properties.
 */
@Throws(MissingPropertyException::class)
fun properties(key: String): String {
    val property = project.findProperty(key)
    return property?.toString() ?: throw MissingPropertyException("Property '$key' does not exist")
}

apply(plugin = "info.solidsoft.pitest.aggregator")

kover {
    isDisabled = false
    coverageEngine.set(INTELLIJ)
}

allprojects {
    group = properties("plugin.group")
    version = properties("plugin.version")

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "info.solidsoft.pitest")

    tasks {
        val javaVersion = properties("java.version")
        compileKotlin {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
            kotlinOptions {
                jvmTarget = javaVersion
                freeCompilerArgs = listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
                )
            }
        }

        compileTestKotlin {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
            kotlinOptions {
                jvmTarget = javaVersion
                freeCompilerArgs = listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
                )
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

        withType<Detekt>() {
            reports {
                xml.required.set(true)
                html.required.set(false)
                txt.required.set(false)
                sarif.required.set(true)
            }
        }
    }

    configure<DetektExtension> {
        buildUponDefaultConfig = true
        config = files("$rootDir/config/detekt.yml")
        source = files("src/main/kotlin", "src/test/kotlin")
    }

    configure<PitestPluginExtension> {
        testPlugin.set("junit5")
        junit5PluginVersion.set("0.15")
        mutators.set(setOf("DEFAULTS"))
        targetClasses.set(setOf("io.t28.kotlinify.*"))
        avoidCallsTo.set(setOf("kotlin.jvm.internal"))

        outputFormats.set(setOf("HTML", "XML"))
        timestampedReports.set(false)
        exportLineCoverage.set(false)
    }
}

tasks {
    register<ReportMergeTask>("detektMergeXmlReports") {
        group = "verification"
        description = "Merges XML reports from all subprojects in one directory."

        input.from(subprojects.map { "${it.buildDir}/reports/detekt/detekt.xml" })
        output.set(file("$buildDir/reports/detekt/detekt.xml"))
    }

    register<ReportMergeTask>("detektMergeSarifReports") {
        group = "verification"
        description = "Merges Sarif reports from all subprojects in one directory."

        input.from(subprojects.map { "${it.buildDir}/reports/detekt/detekt.sarif" })
        output.set(file("$buildDir/reports/detekt/detekt.sarif"))
    }

    register("detektMergeReports") {
        group = "verification"
        description = "Merges reports from all subprojects in one directory."

        dependsOn("detektMergeXmlReports", "detektMergeSarifReports")
    }

    koverCollectReports {
        outputDir.set(layout.buildDirectory.dir("reports/kover"))
    }
}
