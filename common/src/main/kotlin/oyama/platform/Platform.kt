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
@file:Suppress("NOTHING_TO_INLINE")

package oyama.platform

import oyama.util.Services
import java.nio.file.Path

interface Platform {
    val isFabric: Boolean get() = !isNeoForge
    val isNeoForge: Boolean get() = !isFabric
    fun isModLoaded(namespace: String): Boolean
    val side: Side
    val gamePath: Path
    val modsPath: Path
    val configPath: Path
    val isClientSide: Boolean
    val isServerSide: Boolean
    val isDevelopment: Boolean
    val isProduction: Boolean
    val mods: Map<String, Mod>

    companion object {
        @get:JvmName("$\$Platform")
        private val implementation: Platform by lazy { Services.setup() }

        @JvmStatic
        fun get(): Platform = implementation
        inline val isFabric: Boolean get() = get().isFabric
        inline val isNeoForge: Boolean get() = get().isNeoForge
        inline fun isModLoaded(namespace: String): Boolean = get().isModLoaded(namespace)
        inline val side: Side get() = get().side
        inline val gamePath: Path get() = get().gamePath
        inline val modsPath: Path get() = get().modsPath
        inline val configPath: Path get() = get().configPath
        inline val isClientSide: Boolean get() = get().isClientSide
        inline val isServerSide: Boolean get() = get().isServerSide
        inline val isDevelopment: Boolean get() = get().isDevelopment
        inline val isProduction: Boolean get() = get().isProduction
        inline val mods: Map<String, Mod> get() = get().mods
    }
}
