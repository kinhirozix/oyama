@file:Suppress("SpellCheckingInspection")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.neoforged.net/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

if (JavaVersion.current().ordinal + 1 < 21) throw IllegalStateException("Please use Java 21+!")

rootProject.name = "oyama"

include(":common", ":fabric", ":neoforge")
