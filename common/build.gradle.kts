@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    id("net.neoforged.moddev")
}

neoForge {
    neoFormVersion = libs.versions.neoform.get()

    parchment {
        minecraftVersion = libs.versions.parchment.minecraft.get()
        mappingsVersion = libs.versions.parchment.mappings.get()
    }
}

dependencies {
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

    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras.common)

    implementation(libs.annotations)
    implementation(libs.typetools)
}

tasks {
    jar {
        enabled = false
    }
}
