/*-
 * #%L
 * CloudSpec AWS Provider
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
package cloudspec.aws

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.syntax.collections.flatten
import cloudspec.annotation.ResourceReflectionUtil
import cloudspec.model.*

interface AWSGroup {
    val definition: GroupDef
    val loaders: Map<ResourceDefRef, AWSResourceLoader<*>>

    fun resourcesByRef(sets: SetValues, ref: ResourceDefRef): List<Resource> {
        return getLoader(ref).map { loader ->
            loader.all(sets).map {
                ResourceReflectionUtil.toResource(it)
            }.flatten()
        }.getOrElse { emptyList() }
    }

    fun resource(sets: SetValues, ref: ResourceRef): Option<Resource> {
        return Option.fx {
            val (loader) = getLoader(ref.defRef)
            val (awsResource) = loader.byId(sets, ref.id)
            val (resource) = ResourceReflectionUtil.toResource(awsResource)

            resource
        }
    }

    fun getLoader(resourceDefRef: ResourceDefRef): Option<AWSResourceLoader<*>> {
        return loaders[resourceDefRef].toOption()
    }
}
