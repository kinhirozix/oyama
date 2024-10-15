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
package oyama.event.core

import java.util.*

class EventListenerList : Iterable<EventListenerWrapper> {
    private val listeners: MutableList<EventListenerWrapper> = Collections.synchronizedList(arrayListOf())
    private val children: MutableList<EventListenerList> = Collections.synchronizedList(arrayListOf())
    fun register(listener: EventListenerWrapper) {
        addListener(listener)
        for (child in children) child.addListener(listener)
    }

    fun unregister(listener: EventListenerWrapper) {
        removeListener(listener)
        for (child in children) child.removeListener(listener)
    }

    fun addParent(parent: EventListenerList) {
        parent.children.add(this)
        for (listener in parent.listeners) addListener(listener)
    }

    fun addChild(child: EventListenerList) {
        children.add(child)
        for (listener in listeners) child.addListener(listener)
    }

    override fun iterator(): Iterator<EventListenerWrapper> = listeners.iterator()
    private fun addListener(listener: EventListenerWrapper) {
        if (listeners.contains(listener)) return
        var left = 0
        var right = listeners.size
        while (left < right) {
            val middle = (left + right) ushr 1
            if (compareListener(listener, listeners[middle]) < 0) right = middle else left = middle + 1
        }

        listeners.add(left, listener)
    }

    private fun removeListener(listener: EventListenerWrapper) {
        listeners.remove(listener)
    }

    private fun compareListener(first: EventListenerWrapper, second: EventListenerWrapper): Int =
        first.priority.compareTo(second.priority)
}
