@file:Suppress("SpellCheckingInspection")

import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    base
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforge.moddev) apply false
    alias(libs.plugins.shadowjar) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.spotless) apply false
}

group = "com.kinhiro.mcmod"
version = "${property("version")}+${libs.versions.minecraft.get()}"
base.archivesName = property("namespace").toString()
description = property("description").toString()

tasks {
    val collectModJars by registering(Copy::class) {
        val tasks = subprojects.filter { it.path != "common" }.map { it.tasks.named("jar") }
        dependsOn(tasks)
        from(tasks)
        into(layout.buildDirectory.dir("libs"))
    }

    assemble {
        dependsOn(collectModJars)
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "com.diffplug.spotless")

    group = rootProject.group
    version = rootProject.version
    base.archivesName = rootProject.base.archivesName
    description = rootProject.description

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }

        withSourcesJar()
        withJavadocJar()
    }

    configure<KotlinJvmProjectExtension> {
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

    configure<SpotlessExtension> {
        java {
            target("*/src/main/java/oyama/**/*.java")
            licenseHeaderFile("$rootDir/spotless/license-header.java")
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }

        kotlin {
            target("*/src/main/kotlin/oyama/**/*.kt")
            licenseHeaderFile("$rootDir/spotless/license-header.kt")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    repositories {
        mavenCentral()

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
                        "Specification-Title" to project.property("name").toString(),
                        "Specification-Vender" to project.property("authors").toString(),
                        "Specification-Version" to project.version.toString(),
                        "Implementation-Title" to project.property("name").toString(),
                        "Implementation-Vendor" to project.property("authors").toString(),
                        "Implementation-Version" to project.version.toString(),
                        "Implementation-Timestamp" to ZonedDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
                    )
                )
            }
        }

        "sourcesJar"(Jar::class) {
            from(rootProject.file("LICENSE"))
        }

        "processResources"(ProcessResources::class) {
            val expandProperties = mapOf(
                "namespace" to project.property("namespace").toString(),
                "version" to project.version.toString(),
                "name" to project.property("name").toString(),
                "description" to project.description!!,
                "license" to project.property("license").toString(),
                "authors" to project.property("authors").toString(),
                "homepage" to project.property("homepage").toString(),
                "sources" to project.property("sources").toString(),
                "issues" to project.property("issues").toString()
            )

            inputs.properties(expandProperties)
            filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "pack.mcmeta")) {
                expand(expandProperties)
            }
        }

        withType<GenerateModuleMetadata> {
            enabled = false
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
        }
    }
}
