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
    alias(deps.plugins.kotlinx.serialization)
}

dependencies {
    implementation(deps.kotlin.stdlib)
    implementation(deps.kotlin.reflect)
    implementation(deps.kotlinx.serialization.json)
    implementation(deps.kotlinpoet)

    testImplementation(deps.junit)
    testImplementation(deps.kotlin.test)
    testImplementation(deps.truth)
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
    }

    withType<Wrapper> {
        gradleVersion = "7.3"
    }
}
