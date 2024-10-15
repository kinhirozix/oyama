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
package oyama.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import oyama.library.OyamaEntityTypes

class VoidTimeDomain(type: EntityType<*>, level: Level) : Entity(type, level) {
    private var blockPos: BlockPos? = null

    init {
        entityData.set(TIME_RATE_DATA, 1)
    }

    constructor(level: Level, pos: BlockPos, x: Double, y: Double, z: Double) : this(
        OyamaEntityTypes.TIME_ACCELERATION_MAGIC_CIRCLE,
        level
    ) {
        blockPos = pos
        setPos(x, y, z)
    }

    var timeRate: Int
        get() = entityData.get(TIME_RATE_DATA)
        set(rate) {
            entityData.set(TIME_RATE_DATA, rate)
        }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(TIME_RATE_DATA, 1)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        nbt.putInt("TimeRate", timeRate)
        nbt.put("BlockPos", NbtUtils.writeBlockPos(blockPos ?: BlockPos.ZERO))
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        entityData.set(TIME_RATE_DATA, nbt.getInt("TimeRate"))
        blockPos = NbtUtils.readBlockPos(nbt, "BlockPos").orElse(BlockPos.ZERO)
    }

    companion object {
        private val TIME_RATE_DATA: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(VoidTimeDomain::class.java, EntityDataSerializers.INT)
    }
}
