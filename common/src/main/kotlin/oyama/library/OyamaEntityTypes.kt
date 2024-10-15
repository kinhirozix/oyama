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
package oyama.library

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import oyama.entity.VoidTimeDomain

object OyamaEntityTypes {
    @JvmField
    val TIME_ACCELERATION_MAGIC_CIRCLE: EntityType<VoidTimeDomain> =
        Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            "time_acceleration_magic_circle",
            EntityType.Builder.of(::VoidTimeDomain, MobCategory.MISC)
                .sized(0.1f, 0.1f)
                .build("oyama:time_acceleration_magic_circle")
        )
}
