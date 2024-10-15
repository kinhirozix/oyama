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
package oyama.library

import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import oyama.util.SoulBound

object OyamaDataComponents {
    @JvmField
    val SOUL_BOUND: DataComponentType<SoulBound> =
        register("soul_bound") {
            it.persistent(SoulBound.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(SoulBound.CODEC))
        }

    private fun <T> register(
        name: String,
        builder: (DataComponentType.Builder<T>) -> DataComponentType.Builder<T>
    ): DataComponentType<T> = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        name,
        builder(DataComponentType.builder()).build()
    )
}
