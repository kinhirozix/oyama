@file:Suppress("SpellCheckingInspection")

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
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.io.core)
    implementation(libs.kotlinx.io.bytestring)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.atomicfu)
    implementation(libs.kotlinx.datetime)
}

tasks {
    compileJava {
        source(project(":common").sourceSets.main.get().allSource)
    }

    processResources {
        from(project(":common").sourceSets.main.get().output.resourcesDir)
        dependsOn(project(":common").tasks.processResources)
    }

    jar {
        archiveClassifier = project.name
    }
}
