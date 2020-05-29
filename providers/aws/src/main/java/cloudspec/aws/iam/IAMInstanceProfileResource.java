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
package cloudspec.aws.iam;

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.model.ResourceDefRef;

import java.util.List;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = IAMResource.GROUP_NAME,
        name = IAMInstanceProfileResource.RESOURCE_NAME,
        description = "Amazon Machine Image"
)
public class IAMInstanceProfileResource extends IAMResource {
    public static final String RESOURCE_NAME = "iam_instance_profile";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @IdDefinition
    @PropertyDefinition(
            name = "id",
            description = "Instance profile ID"
    )
    public String id;

    @PropertyDefinition(
            name = "name",
            description = "Instance profile name"
    )
    public String name;

    @PropertyDefinition(
            name = "path",
            description = "Path"
    )
    public String path;

    //    @AssociationDefinition(
//            name = "roles",
//            description = "IAM Roles",
//            targetClass = IAMRoleResource.class
//    )
    public List<String> roleIds;
}
