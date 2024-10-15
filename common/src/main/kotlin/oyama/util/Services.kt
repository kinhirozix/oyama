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

import oyama.library.Oyama
import java.util.*

object Services {
    @JvmSynthetic
    inline fun <reified T> setup(): T {
        val services = ServiceLoader.load(T::class.java).toList()
        return when (services.size) {
            0 -> throw NoSuchElementException("No implementation found for service ${T::class.java.name}.")
            1 -> services[0]
            else -> throw IllegalStateException(
                "Multiple implementations found for service ${T::class.java.name}. " +
                    "Only one implementation is allowed."
            )
        }
    }

    @JvmStatic
    fun <T> find(service: Class<T>): T = ServiceLoader.load(service).findFirst().orElseThrow {
        Oyama.LOGGER.error("No implementation found for service ${service.name}.")
        IllegalStateException("No implementation found for service ${service.name}.")
    }
}
