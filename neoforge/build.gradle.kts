@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.neoforge.moddev)
}

neoForge {
    version = libs.versions.neoforge.get()

    parchment {
        minecraftVersion = libs.versions.parchment.minecraft.get()
        mappingsVersion = libs.versions.parchment.mappings.get()
    }

    runs {
        all {
            systemProperty("neoforge.enabledGameTestNamespaces", property("namespace").toString())
            ideName = "NeoForge ${name.replaceFirstChar { it.uppercase() }}"
        }

        val client by creating {
            client()
            gameDirectory = file("runs/client")
        }

        val server by creating {
            server()
            gameDirectory = file("runs/server")
            programArguments = listOf("-nogui")
        }
    }

    mods {
        create(property("namespace").toString()) {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    implementation(project(":common"))

    implementation(libs.kotlin.stdlib)
    additionalRuntimeClasspath(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    additionalRuntimeClasspath(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.core)
    additionalRuntimeClasspath(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    additionalRuntimeClasspath(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    additionalRuntimeClasspath(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.io.core)
    additionalRuntimeClasspath(libs.kotlinx.io.core)
    implementation(libs.kotlinx.io.bytestring)
    additionalRuntimeClasspath(libs.kotlinx.io.bytestring)
    implementation(libs.kotlinx.collections.immutable)
    additionalRuntimeClasspath(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.atomicfu)
    additionalRuntimeClasspath(libs.kotlinx.atomicfu)
    implementation(libs.kotlinx.datetime)
    additionalRuntimeClasspath(libs.kotlinx.datetime)
}

tasks {
    withType(JavaCompile::class).matching { !it.name.startsWith("neo") }.all {
        source(project(":common").sourceSets.main.get().allSource)
    }

    withType(KotlinCompile::class).matching { !it.name.startsWith("neo") }.all {
        source(project(":common").sourceSets.main.get().allSource)
    }

    sourcesJar {
        from(project(":common").sourceSets.main.get().allJava)
    }

    withType(Javadoc::class).matching { !it.name.startsWith("neo") }.all {
        source(project(":common").sourceSets.main.get().allJava)
    }

    withType(ProcessResources::class).matching { !it.name.startsWith("neo") }.all {
        from(project(":common").sourceSets.main.get().resources)
    }
}
