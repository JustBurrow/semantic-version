import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    `maven-publish`
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/JustBurrow/packages")
        credentials {
            username = properties["github.actor"] as String?
                ?: System.getenv("GITHUB_ACTOR")
            password = properties["github.token"] as String?
                ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

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
        iosSimulatorArm64(),
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
            implementation(libs.kr.lul.logging)
        }
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/JustBurrow/packages")
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
                artifactId = "${rootProject.name}-$name".lowercase()
                version = "0.0.2"

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
}
