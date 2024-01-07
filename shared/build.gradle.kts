plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.framework.datatest)
        }

        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
            implementation(libs.kotlin.logging)
            implementation(libs.logback.classic)
        }
    }


    tasks.named<Test>("jvmTest") {
        useJUnitPlatform()
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

            all {
                it.useJUnitPlatform()
            }
        }
    }

    dependencies {
        testImplementation(libs.junit)
        testImplementation(libs.kotest.runner.junit5)
        testImplementation(libs.kotlin.logging)
        testImplementation(libs.logback.classic)
    }
}
