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
package oyama.event.tick

import net.minecraft.world.entity.player.Player
import oyama.event.api.Event

abstract class PlayerTickEvent protected constructor(val player: Player) : Event() {
    @JvmSynthetic
    operator fun component1(): Player = player

    class Start(player: Player) : PlayerTickEvent(player)
    class End(player: Player) : PlayerTickEvent(player)
}
