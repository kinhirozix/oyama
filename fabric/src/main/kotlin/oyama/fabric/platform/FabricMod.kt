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
package oyama.fabric.platform

import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata
import oyama.platform.Mod
import java.nio.file.Path
import java.util.*

class FabricMod(private val container: ModContainer) : Mod {
    private val metadata: ModMetadata = container.metadata

    override val namespace: String get() = metadata.id
    override val displayName: String get() = metadata.name
    override val description: String get() = metadata.description
    override val version: String get() = metadata.version.friendlyString
    override val licenses: Collection<String> get() = metadata.license
    override val authors: Collection<String> get() = metadata.authors.map { it.name }
    override val credits: Collection<String> get() = metadata.contributors.map { it.name }
    override val homepage: Optional<String> get() = metadata.contact["homepage"]
    override val sources: Optional<String> get() = metadata.contact["sources"]
    override val issues: Optional<String> get() = metadata.contact["issues"]
    override fun findResource(vararg paths: String): Optional<Path> = container.findPath(paths.joinToString("/"))
}
