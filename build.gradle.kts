@file:Suppress("SpellCheckingInspection")

import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    base
    idea
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforge.moddev) apply false
    alias(libs.plugins.shadowjar) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.spotless) apply false
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

group = "com.kinhiro.mcmod"
version = "${property("version")}+${libs.versions.minecraft.get()}"
base.archivesName = property("namespace").toString()
description = property("description").toString()

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "com.diffplug.spotless")

    group = rootProject.group
    version = rootProject.version
    base.archivesName = rootProject.base.archivesName
    description = rootProject.description

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }

        withSourcesJar()
        withJavadocJar()
    }

    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(21)

        sourceSets.all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlin.contracts.ExperimentalContracts")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }
    }

    extensions.configure<SpotlessExtension> {
        java {
            target("**/*.java")
            licenseHeaderFile("$rootDir/spotless/license-header.java")
        }

        kotlin {
            target("**/*.kt")
            licenseHeaderFile("$rootDir/spotless/license-header.kt")
        }
    }

    repositories {
        mavenCentral()

        maven("https://maven.parchmentmc.org") {
            content {
                // Parchment Mappings
                val minecraftVersion = rootProject.libs.versions.parchment.minecraft.get()
                includeModule("org.parchmentmc.data", "parchment-$minecraftVersion")
            }
        }

        maven("https://repo.spongepowered.org/repository/maven-public") {
            content {
                // SpongePowered Mixin
                includeModule("org.spongepowered", "mixin")
            }
        }
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release = 21
        }

        withType<KotlinCompile> {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_21
                freeCompilerArgs = listOf("-Xjvm-default=all", "-Xlambdas=indy")
            }
        }

        "jar"(Jar::class) {
            from(rootProject.file("LICENSE"))

            manifest {
                attributes(
                    mapOf(
                        "Specification-Title" to project.property("name"),
                        "Specification-Vender" to project.property("authors"),
                        "Specification-Version" to project.version,
                        "Implementation-Title" to project.property("name"),
                        "Implementation-Vendor" to project.property("authors"),
                        "Implementation-Version" to project.version,
                        "Implementation-Timestamp" to ZonedDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")),
                        "Timestamp" to System.currentTimeMillis()
                    )
                )
            }
        }

        "sourcesJar"(Jar::class) {
            from(rootProject.file("LICENSE"))
        }

        "processResources"(ProcessResources::class) {
            val expandProperties = mapOf(
                "namespace" to project.property("namespace"),
                "version" to project.version,
                "name" to project.property("name"),
                "description" to project.description,
                "license" to project.property("license"),
                "authors" to project.property("authors"),
                "homepage" to project.property("homepage"),
                "sources" to project.property("sources"),
                "issues" to project.property("issues")
            )

            inputs.properties(expandProperties)

            filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
                expand(expandProperties)
            }
        }
    }
}
