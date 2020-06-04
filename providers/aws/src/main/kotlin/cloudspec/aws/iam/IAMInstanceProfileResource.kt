/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
