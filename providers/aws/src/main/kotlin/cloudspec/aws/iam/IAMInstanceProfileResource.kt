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
package cloudspec.aws.iam

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = IAMResource.GROUP_NAME,
        name = IAMInstanceProfileResource.RESOURCE_NAME,
        description = IAMInstanceProfileResource.RESOURCE_DESCRIPTION
)
data class IAMInstanceProfileResource(
        @property:IdDefinition
        @property:PropertyDefinition(
                name = "id",
                description = "Instance profile ID"
        )
        val id: String,

        @property:PropertyDefinition(
                name = "name",
                description = "Instance profile name"
        )
        val name: String?,

        @property:PropertyDefinition(
                name = "path",
                description = "Path"
        )
        val path: String?,

//    @property:AssociationDefinition(
//            name = "roles",
//            description = "IAM Roles",
//            targetClass = IAMRoleResource.class
//    )
        val roleIds: List<String>?
) : IAMResource() {
    companion object {
        const val RESOURCE_NAME = "iam_instance_profile"
        const val RESOURCE_DESCRIPTION = "IAM Instance Profile"
    }
}
