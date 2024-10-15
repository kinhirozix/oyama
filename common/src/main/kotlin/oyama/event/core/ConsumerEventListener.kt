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

import oyama.event.api.Event
import java.util.function.Consumer

class ConsumerEventListener<E : Event>(private val consumer: Consumer<E>) : EventListener {
    override fun invoke(event: Event) = @Suppress("UNCHECKED_CAST") consumer.accept(event as E)
}
