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
package oyama.fabric.platform

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import oyama.platform.Mod
import oyama.platform.Platform
import oyama.platform.Side
import java.nio.file.Path

class FabricPlatform : Platform {
    override val isFabric: Boolean get() = true
    override fun isModLoaded(namespace: String): Boolean = FabricLoader.getInstance().isModLoaded(namespace)
    override val side: Side
        get() = when (FabricLoader.getInstance().environmentType) {
            EnvType.CLIENT -> Side.CLIENT
            EnvType.SERVER -> Side.SERVER
            else -> throw IllegalStateException() // This might never be reached, but it is the desired outcome.
        }

    override val gamePath: Path get() = FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()
    override val modsPath: Path get() = gamePath.resolve("mods")
    override val configPath: Path get() = FabricLoader.getInstance().configDir.toAbsolutePath().normalize()
    override val isClientSide: Boolean get() = FabricLoader.getInstance().environmentType == EnvType.CLIENT
    override val isServerSide: Boolean get() = FabricLoader.getInstance().environmentType == EnvType.SERVER
    override val isDevelopment: Boolean get() = FabricLoader.getInstance().isDevelopmentEnvironment
    override val isProduction: Boolean get() = !FabricLoader.getInstance().isDevelopmentEnvironment
    override val mods: Map<String, Mod>
        get() = FabricLoader.getInstance().allMods.map(::FabricMod).associateBy { it.namespace }
}
