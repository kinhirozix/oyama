@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

plugins {
    idea
    id("fabric-loom")
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

loom {
    accessWidenerPath = file("src/main/resources/oyama.accessWidener")

    mixin {
        defaultRefmapName = "oyama.refmap.json"
    }

    runs {
        all {
            isIdeConfigGenerated = true
        }

        named("client") {
            client()
            configName = "Fabric Client"
            runDir("runs/client")
        }

        named("server") {
            server()
            configName = "Fabric Server"
            programArg("-nogui")
            runDir("runs/server")
        }
    }
}

repositories {
    exclusiveContent {
        // Parchment Mappings
        forRepository { maven("https://maven.parchmentmc.org") }
        filter { includeGroup("org.parchmentmc.data") }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        val minecraftVersion = libs.versions.parchment.minecraft.get()
        val mappingsVersion = libs.versions.parchment.mappings.get()
        parchment("org.parchmentmc.data:parchment-$minecraftVersion:$mappingsVersion@zip")
    })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.kotlin)

    implementation(project(":common"))

    implementation(libs.annotations)
    implementation(libs.typetools)
}

tasks {
    compileJava {
        source(project(":common").sourceSets.main.get().java)
    }

    compileKotlin {
        source(project(":common").sourceSets.main.get().kotlin)
    }

    sourcesJar {
        from(project(":common").sourceSets.main.get().java)
        from(project(":common").sourceSets.main.get().kotlin)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    processResources {
        from(project(":common").sourceSets.main.get().resources)
    }
}
