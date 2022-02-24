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
import kotlinx.kover.api.KoverTaskExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.kotlinx.serialization)
}

dependencies {
    implementation(deps.kotlin.stdlib)
    implementation(deps.kotlin.reflect)
    implementation(deps.kotlinx.collections.immutable)
    implementation(deps.kotlinx.serialization.json)
    implementation(deps.kotlinpoet)

    testImplementation(deps.junit)
    testImplementation(deps.kotlin.test)
    testImplementation(deps.truth)
}

tasks {
    test {
        useJUnitPlatform()

        configure<KoverTaskExtension> {
            isEnabled = true
            includes = listOf("io.t28.kotlinify.*")
            excludes = listOf()
        }
        finalizedBy(koverReport)
    }

    koverHtmlReport {
        isEnabled = true
        htmlReportDir.set(layout.buildDirectory.dir("reports/kover"))
    }

    koverXmlReport {
        isEnabled = true
        xmlReportFile.set(layout.buildDirectory.file("reports/kover/reports.xml"))
    }
}
