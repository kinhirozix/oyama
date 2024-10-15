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
package oyama.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.UUIDUtil
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import oyama.library.OyamaDataComponents
import java.util.*

@JvmRecord
data class SoulBound(
    val uuid: UUID,
    val username: String
) {
    companion object {
        @JvmField
        val CODEC: Codec<SoulBound> =
            RecordCodecBuilder.create {
                it.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(SoulBound::uuid),
                    Codec.STRING.fieldOf("username").forGetter(SoulBound::username)
                ).apply(it, ::SoulBound)
            }

        @JvmStatic
        fun boundTo(player: Player, stack: ItemStack) {
            if (!stack.has(OyamaDataComponents.SOUL_BOUND))
                stack.set(OyamaDataComponents.SOUL_BOUND, SoulBound(player.uuid, player.gameProfile.name))
            else stack.update(OyamaDataComponents.SOUL_BOUND, SoulBound(player.uuid, player.gameProfile.name)) { it }
        }

        @JvmStatic
        fun unbound(stack: ItemStack) {
            if (stack.has(OyamaDataComponents.SOUL_BOUND)) stack.remove(OyamaDataComponents.SOUL_BOUND)
        }

        @JvmStatic
        fun hasSoulBound(stack: ItemStack): Boolean = stack.has(OyamaDataComponents.SOUL_BOUND)
    }
}
