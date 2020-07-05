/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.util

import arrow.core.Option
import arrow.core.none
import arrow.fx.IO
import arrow.syntax.collections.flatten
import cloudspec.annotation.ResourceDefReflectionUtil
import cloudspec.model.*

class TestProvider : Provider() {
    override val name
        get() = ProviderDataUtil.PROVIDER_NAME

    override val description
        get() = ProviderDataUtil.PROVIDER_DESCRIPTION

    override val groupDefs: GroupDefs
        get() = listOf(GroupDef("test",
                                "test",
                                listOf(TestResource::class)
                                    .map { ResourceDefReflectionUtil.toResourceDef(it) }
                                    .flatten()))

    override val resourceDefs
        get() = groupDefs.flatMap { it.resourceDefs }

    override
    val configDefs: ConfigDefs
        get() = setOf(
                ConfigDef(ConfigRef(ProviderDataUtil.PROVIDER_NAME, "myconfig"),
                          "My config",
                          SetValueType.STRING,
                          false)
        )

    override fun resourcesByDef(sets: SetValues, defRef: ResourceDefRef): IO<Resources> {
        return IO { emptyList<Resource>() }
    }

    override fun resource(sets: SetValues, ref: ResourceRef): IO<ResourceO> {
        return IO { none<Resource>() }
    }
}
