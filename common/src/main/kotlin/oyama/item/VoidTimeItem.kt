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
package oyama.item

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import oyama.entity.VoidTimeDomain
import oyama.library.OyamaDataComponents
import oyama.util.NotePlayer
import kotlin.math.pow

class VoidTimeItem : Item(Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC)) {
    fun playSound(level: Level, pos: BlockPos, rate: Int) {
        when (rate) {
            1 -> NotePlayer.blockPlayHarp(level, pos, "C")
            2 -> NotePlayer.blockPlayHarp(level, pos, "D")
            4 -> NotePlayer.blockPlayHarp(level, pos, "E")
            8 -> NotePlayer.blockPlayHarp(level, pos, "F")
            16 -> NotePlayer.blockPlayHarp(level, pos, "G2")
            32 -> NotePlayer.blockPlayHarp(level, pos, "A2")
            64 -> NotePlayer.blockPlayHarp(level, pos, "B2")
            128 -> NotePlayer.blockPlayHarp(level, pos, "C2")
            256 -> NotePlayer.blockPlayHarp(level, pos, "D2")
            512 -> NotePlayer.blockPlayHarp(level, pos, "E2")
            1024 -> NotePlayer.blockPlayHarp(level, pos, "F2")
            else -> NotePlayer.blockPlayHarp(level, pos, "F#")
        }
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) return InteractionResult.PASS
        val pos = context.clickedPos
        val state = level.getBlockState(pos)
        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity == null && !state.isRandomlyTicking) return InteractionResult.FAIL
        var nextRate = 1
        val currentDomain = level.getEntitiesOfClass(VoidTimeDomain::class.java, AABB(pos)).firstOrNull()
        if (currentDomain != null) {
            val currentRate = currentDomain.timeRate
            if (currentRate >= 2.0.pow(10.0)) return InteractionResult.SUCCESS
            nextRate = currentRate * 2
            currentDomain.timeRate = nextRate
        } else {
            val newDomain = VoidTimeDomain(level, pos, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
            level.addFreshEntity(newDomain)
        }

        playSound(level, pos, nextRate)
        return InteractionResult.SUCCESS
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltips, flag)
        tooltips.add(
            Component.translatable(
                "tooltip.oyama.void_item.soul_bound",
                stack.get(OyamaDataComponents.SOUL_BOUND)
            )
        )
    }
}
