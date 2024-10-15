@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    id("net.neoforged.moddev")
}

neoForge {
    neoFormVersion = libs.versions.neoform.get()
    addModdingDependenciesTo(sourceSets.test.get())

    parchment {
        minecraftVersion = libs.versions.parchment.minecraft.get()
        mappingsVersion = libs.versions.parchment.mappings.get()
    }
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.kotlin.reflect)
    compileOnly(libs.kotlinx.serialization.core)
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.kotlinx.coroutines.core)
    compileOnly(libs.kotlinx.io.core)
    compileOnly(libs.kotlinx.io.bytestring)
    compileOnly(libs.kotlinx.collections.immutable)
    compileOnly(libs.kotlinx.atomicfu)
    compileOnly(libs.kotlinx.datetime)

    compileOnly(libs.fabric.mixin)
    annotationProcessor(libs.mixinextras.common)

    compileOnly(libs.annotations)
    compileOnly(libs.typetools)
    compileOnly(libs.asm)
}
