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

@ResourceDefinition(
        provider = "aws",
        group = "iam",
        name = "iam_instance_profile",
        description = "Amazon Machine Image"
)
data class IAMInstanceProfileResource(
        @IdDefinition
        @PropertyDefinition(
                name = "id",
                description = "Instance profile ID"
        )
        val id: String,

        @PropertyDefinition(
                name = "name",
                description = "Instance profile name"
        )
        val name: String?,

        @PropertyDefinition(
                name = "path",
                description = "Path"
        )
        val path: String?,

//    @AssociationDefinition(
//            name = "roles",
//            description = "IAM Roles",
//            targetClass = IAMRoleResource.class
//    )
        val roleIds: List<String>?
) : IAMResource()
