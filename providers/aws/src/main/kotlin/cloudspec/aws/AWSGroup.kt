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
import arrow.core.extensions.option.monad.flatten
import arrow.core.extensions.sequence
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.applicative.applicative
import arrow.syntax.collections.flatten
import cloudspec.annotation.ResourceReflectionUtil
import cloudspec.model.*

interface AWSGroup {
    val definition: GroupDef
    val loaders: Map<ResourceDefRef, AWSResourceLoader<*>>

    fun resourcesByRef(sets: SetValues, defRef: ResourceDefRef): IO<List<Resource>> {
        return IO.fx {
            val (awsResources) = getLoader(defRef)
                .map { loader -> loader.all(sets) }
                .sequence(IO.applicative())
                .map { it.getOrElse { emptyList() } }

            awsResources.map { ResourceReflectionUtil.toResource(it) }.flatten()
        }
    }

    fun resource(sets: SetValues, ref: ResourceRef): IO<Option<Resource>> {
        return IO.fx {
            val (awsResource) = getLoader(ref.defRef)
                .map { loader -> loader.byId(sets, ref.id) }
                .sequence(IO.applicative())
                .map { it.flatten() }

            awsResource.flatMap { ResourceReflectionUtil.toResource(it) }
        }
    }

    fun getLoader(resourceDefRef: ResourceDefRef): Option<AWSResourceLoader<*>> {
        return loaders[resourceDefRef].toOption()
    }
}
