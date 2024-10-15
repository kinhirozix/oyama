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
package oyama.platform

import oyama.util.function.NonnullSupplier
import java.util.*

object SidedExecutor {
    @JvmStatic
    fun run(side: Side, runnable: NonnullSupplier<Runnable>) {
        if (Platform.side == side) runnable.get().run()
    }

    @JvmStatic
    fun <R : Any> get(side: Side, runnable: NonnullSupplier<NonnullSupplier<R>>): Optional<R> =
        if (Platform.side == side) Optional.of(runnable.get().get()) else Optional.empty()

    @JvmStatic
    fun <T : Any> where(client: NonnullSupplier<NonnullSupplier<T>>, server: NonnullSupplier<NonnullSupplier<T>>): T =
        if (Platform.isClientSide) client.get().get() else server.get().get()
}
