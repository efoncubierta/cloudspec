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
import cloudspec.aws.dynamodb.DDBGroup
import cloudspec.aws.ec2.EC2Group
import cloudspec.model.*

class AWSProvider(clientsProvider: IAWSClientsProvider) : Provider() {
    private val groups: Map<String, AWSGroup> = mapOf(
            DDBGroup.GROUP_NAME to DDBGroup(clientsProvider),
            EC2Group.GROUP_NAME to EC2Group(clientsProvider)
    )

    override val name
        get() = PROVIDER_NAME

    override val description
        get() = PROVIDER_DESCRIPTION

    override val groupDefs
        get() = groups.values.map { it.definition }

    override val resourceDefs: ResourceDefs
        get() = groupDefs.flatMap { it.resourceDefs }

    override val configDefs: ConfigDefs
        get() = AWSConfig.CONFIG_DEFS

    override fun resourcesByDef(sets: SetValues, defRef: ResourceDefRef): List<Resource> {
        return getGroup(defRef).map { group ->
            group.resourcesByRef(sets, defRef)
        }.getOrElse { emptyList() }
    }

    override fun resource(sets: SetValues, ref: ResourceRef): Option<Resource> {
        return Option.fx {
            val (group) = getGroup(ref.defRef)
            val (resource) = group.resource(sets, ref)

            resource
        }
    }

    private fun getGroup(resourceDefRef: ResourceDefRef): Option<AWSGroup> {
        return groups[resourceDefRef.groupName].toOption()
    }

    companion object {
        const val PROVIDER_NAME = "aws"
        const val PROVIDER_DESCRIPTION = "Amazon Web Services"
    }
}

