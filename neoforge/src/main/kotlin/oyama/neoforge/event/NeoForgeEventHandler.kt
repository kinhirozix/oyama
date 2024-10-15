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
package oyama.neoforge.event

import net.neoforged.bus.api.EventPriority
import net.neoforged.neoforge.common.NeoForge
import oyama.event.core.EventManager
import oyama.event.tick.EntityTickEvent
import oyama.platform.Platform

object NeoForgeEventHandler {
    private var initialized: Boolean = false

    @JvmStatic
    fun init() {
        if (initialized) return
        initialized = true
        initCommonEvents()
        if (Platform.isClientSide) initClientEvents()
        if (Platform.isServerSide) initServerEvents()
    }

    private fun initCommonEvents() {
        NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.event.tick.EntityTickEvent.Pre>(EventPriority.HIGH) { event ->
            EventManager.fire(EntityTickEvent.Start(event.entity))
        }

        NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.event.tick.EntityTickEvent.Post> { event ->
            EventManager.fire(EntityTickEvent.End(event.entity))
        }
    }

    private fun initClientEvents() = Unit
    private fun initServerEvents() = Unit
}
