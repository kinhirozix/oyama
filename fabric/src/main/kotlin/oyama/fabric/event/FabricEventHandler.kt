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
package oyama.fabric.event

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import oyama.event.core.EventManager
import oyama.event.lifecycle.ClientLifecycleEvent
import oyama.event.lifecycle.ServerLifecycleEvent
import oyama.event.tick.ClientTickEvent
import oyama.event.tick.LevelTickEvent
import oyama.event.tick.ServerTickEvent
import oyama.platform.Platform

object FabricEventHandler {
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
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Lifecycle events
        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            EventManager.fire(ServerLifecycleEvent.Starting(server))
        }

        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            EventManager.fire(ServerLifecycleEvent.Started(server))
        }

        ServerLifecycleEvents.SERVER_STOPPING.register { server ->
            EventManager.fire(ServerLifecycleEvent.Stopping(server))
        }

        ServerLifecycleEvents.SERVER_STOPPED.register { server ->
            EventManager.fire(ServerLifecycleEvent.Stopped(server))
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Tick events
        ServerTickEvents.START_SERVER_TICK.register { server -> EventManager.fire(ServerTickEvent.Start(server)) }
        ServerTickEvents.END_SERVER_TICK.register { server -> EventManager.fire(ServerTickEvent.End(server)) }
        ServerTickEvents.START_WORLD_TICK.register { level -> EventManager.fire(LevelTickEvent.Start(level)) }
        ServerTickEvents.END_WORLD_TICK.register { level -> EventManager.fire(LevelTickEvent.End(level)) }
    }

    private fun initClientEvents() {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Lifecycle events
        ClientLifecycleEvents.CLIENT_STARTED.register { client ->
            EventManager.fire(ClientLifecycleEvent.Started(client))
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register { client ->
            EventManager.fire(ClientLifecycleEvent.Stopping(client))
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Tick events
        ClientTickEvents.START_CLIENT_TICK.register { client -> EventManager.fire(ClientTickEvent.Start(client)) }
        ClientTickEvents.END_CLIENT_TICK.register { client -> EventManager.fire(ClientTickEvent.End(client)) }
        ClientTickEvents.START_WORLD_TICK.register { level -> EventManager.fire(LevelTickEvent.Start(level)) }
        ClientTickEvents.END_WORLD_TICK.register { level -> EventManager.fire(LevelTickEvent.End(level)) }
    }

    private fun initServerEvents() = Unit
}
