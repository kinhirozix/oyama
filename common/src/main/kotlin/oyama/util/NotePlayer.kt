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

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level

object NotePlayer {
    private val NOTE_BLOCK_HARP: SoundEvent = SoundEvents.NOTE_BLOCK_HARP.value()

    @JvmStatic
    fun blockPlayHarp(level: Level, pos: BlockPos, pitch: String) {
        // https://minecraft.wiki/w/Note_Block
        when (pitch.uppercase()) {
            // Octave 1
            "F#" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.5f)
            "G" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.529732f)
            "G#" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.561231f)
            "A" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.594604f)
            "A#" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.629961f)
            "B" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.667420f)
            "C" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.707107f)
            "C#" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.749154f)
            "D" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.793701f)
            "D#" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.840896f)
            "E" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.890899f)
            "F" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 0.943874f)
            // Octave 2
            "F#2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1f)
            "G2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.059463f)
            "G#2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.122462f)
            "A2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.189207f)
            "A#2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.259921f)
            "B2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.334840f)
            "C2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.414214f)
            "C#2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.498307f)
            "D2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.587401f)
            "D#2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.681793f)
            "E2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.781797f)
            "F2" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 1.887749f)
            "F#3" -> level.playSound(null, pos, NOTE_BLOCK_HARP, SoundSource.BLOCKS, 0.5f, 2f)
        }
    }
}
