@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

plugins {
    id("fabric-loom")
}

loom {
    mixin {
        useLegacyMixinAp.set(false)
    }

    runs {
        val client by getting {
            client()
            configName = "Fabric Client"
            runDir("runs/client")
        }

        val server by getting {
            server()
            configName = "Fabric Server"
            programArg("-nogui")
            runDir("runs/server")
        }
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
}

tasks {
    compileJava {
        source(project(":common").sourceSets.main.get().allSource)
    }

    processResources {
        from(project(":common").sourceSets.main.get().output.resourcesDir)
        dependsOn(project(":common").tasks.processResources)
    }

    remapJar {
        archiveClassifier = project.name
    }
}
