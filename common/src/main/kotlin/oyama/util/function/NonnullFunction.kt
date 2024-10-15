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
package oyama.util.function

@FunctionalInterface
fun interface NonnullFunction<T : Any, R : Any> {
    fun apply(t: T): R
    infix fun <V : Any> compose(before: NonnullFunction<in V, out T>): NonnullFunction<V, R> =
        NonnullFunction { t -> apply(before.apply(t)) }

    infix fun <V : Any> andThen(after: NonnullFunction<in R, out V>): NonnullFunction<T, V> =
        NonnullFunction { t -> after.apply(apply(t)) }

    companion object {
        @JvmStatic
        fun <T : Any> identity(): NonnullFunction<T, T> = NonnullFunction { t -> t }
    }
}
