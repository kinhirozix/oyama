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
package oyama.fabric

import net.fabricmc.api.ModInitializer
import net.minecraft.world.entity.decoration.ItemFrame
import oyama.event.BuiltinModEventHandler
import oyama.event.core.EventManager
import oyama.fabric.event.FabricEventHandler
import oyama.event.tick.EntityTickEvent

object OyamaMod : ModInitializer {
    override fun onInitialize() {
        FabricEventHandler.init()
        BuiltinModEventHandler.init()
        EventManager.addListener<EntityTickEvent.Start> { event ->
            if (event.entity is ItemFrame) println("Entity tick event is fired!")
        }
    }
}
