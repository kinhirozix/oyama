/*
 * Copyright 2024 Kinhiro Zix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oyama.neoforge.platform

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import oyama.platform.Mod
import oyama.platform.Platform
import oyama.platform.Side
import java.nio.file.Path

class NeoForgePlatform : Platform {
    override val isNeoForge: Boolean get() = true
    override fun isModLoaded(namespace: String): Boolean = ModList.get().isLoaded(namespace)
    override val side: Side
        get() = when (FMLEnvironment.dist) {
            Dist.CLIENT -> Side.CLIENT
            Dist.DEDICATED_SERVER -> Side.SERVER
            else -> throw IllegalStateException() // This might never be reached, but it is the desired outcome.
        }

    override val gamePath: Path get() = FMLPaths.GAMEDIR.get()
    override val modsPath: Path get() = FMLPaths.MODSDIR.get()
    override val configPath: Path get() = FMLPaths.CONFIGDIR.get()
    override val isClientSide: Boolean get() = FMLEnvironment.dist.isClient
    override val isServerSide: Boolean get() = FMLEnvironment.dist.isDedicatedServer
    override val isDevelopment: Boolean get() = !FMLLoader.isProduction()
    override val isProduction: Boolean get() = FMLLoader.isProduction()
    override val mods: Map<String, Mod>
        get() = ModList.get().mods.map(::NeoForgeMod).associateBy { it.namespace }
}
