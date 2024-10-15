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
package oyama.neoforge.platform

import net.neoforged.fml.i18n.MavenVersionTranslator
import net.neoforged.fml.loading.moddiscovery.ModFileInfo
import net.neoforged.neoforgespi.language.IModInfo
import oyama.platform.Mod
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class NeoForgeMod(private val info: IModInfo) : Mod {
    override val namespace: String get() = info.modId
    override val displayName: String get() = info.displayName
    override val description: String get() = info.description
    override val version: String get() = MavenVersionTranslator.artifactVersionToString(info.version)
    override val licenses: Collection<String> get() = listOf(info.owningFile.license)
    override val authors: Collection<String>
        get() = info.config.getConfigElement<Any?>("authors").map { listOf(it.toString()) }.orElseGet { emptyList() }

    override val credits: Collection<String>
        get() = info.config.getConfigElement<Any?>("credits").map { listOf(it.toString()) }.orElseGet { emptyList() }

    override val homepage: Optional<String> get() = info.config.getConfigElement("displayURL")
    override val sources: Optional<String> get() = Optional.empty()
    override val issues: Optional<String>
        get() = info.owningFile.let { modFileInfo ->
            if (modFileInfo is ModFileInfo) Optional.ofNullable(modFileInfo.issueURL).map { it.toString() }
            else Optional.empty()
        }

    override fun findResource(vararg paths: String): Optional<Path> =
        Optional.of(info.owningFile.file.findResource(*paths)).filter(Files::exists)
}
