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

import oyama.event.api.CancelableEvent
import oyama.event.api.Event
import oyama.event.api.EventPriority
import oyama.event.api.GenericEvent
import java.lang.reflect.Type

class EventListenerWrapper(
    val priority: EventPriority,
    val eventClass: Class<*>,
    private val genericType: Type?,
    receiveCanceled: Boolean,
    private val listener: EventListener
) {
    private val filterFlags: Int = (if (receiveCanceled) 1 else 0) or (if (genericType != null) 2 else 0)

    fun fire(event: Event) {
        if (filter(event)) listener.invoke(event)
    }

    private fun filter(event: Event): Boolean =
        when (filterFlags) {
            1 -> true
            2 -> isNotCanceled(event) && isSameGenericType(event)
            3 -> isSameGenericType(event)
            else -> isNotCanceled(event)
        }

    private fun isNotCanceled(event: Event): Boolean = event !is CancelableEvent || !event.isCanceled
    private fun isSameGenericType(event: Event): Boolean = (event as? GenericEvent<*>)?.genericType == genericType
}
