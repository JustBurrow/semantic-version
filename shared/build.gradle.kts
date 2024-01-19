import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm()
    listOf(
        iosArm64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.logging)
            implementation(libs.logback.classic)
        }
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/JustBurrow/semantic-version")
                credentials {
                    username = properties["github.actor"] as String?
                        ?: System.getenv("GITHUB_ACTOR")
                    password = properties["github.token"] as String?
                        ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }

        publications {
            withType<MavenPublication> {
                groupId = "kr.lul"
                artifactId = rootProject.name
                version = "0.0.1"

                pom {
                    scm {
                        url = "https://github.com/JustBurrow/semantic-version"
                    }
                }
            }
        }
    }
}

android {
    namespace = "kr.lul.version"
    compileSdk = 34

    defaultConfig {
        minSdk = 27
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    dependencies {
        testImplementation(libs.junit)
        testImplementation(libs.kotlin.logging)
        testImplementation(libs.logback.classic)
    }
}
