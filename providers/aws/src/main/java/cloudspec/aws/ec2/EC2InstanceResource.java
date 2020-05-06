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
package cloudspec.aws.ec2;

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.model.ResourceDefRef;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2InstanceResource.RESOURCE_NAME,
        description = "EC2 Instance"
)
public class EC2InstanceResource extends EC2Resource {
    public static final String RESOURCE_NAME = "instance";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "AWS Region"
    )
    private final String region;

    @PropertyDefinition(
            name = "availability_zone",
            description = "AWS Availability Zone"
    )
    private final String availabilityZone;

    @IdDefinition
    @PropertyDefinition(
            name = "instance_id",
            description = "EC2 Instance ID"
    )
    private final String instanceId;

    @PropertyDefinition(
            name = "instance_type",
            description = "EC2 Instance Type"
    )
    private final String instanceType;

    @PropertyDefinition(
            name = "subnet",
            description = "Subnet"
    )
    private final String subnetId;

    public EC2InstanceResource(String accountId, String region, String availabilityZone,
                               String instanceId, String instanceType, String subnetId) {
        super(accountId);
        this.region = region;
        this.availabilityZone = availabilityZone;
        this.instanceId = instanceId;
        this.instanceType = instanceType;
        this.subnetId = subnetId;
    }

    public String getRegion() {
        return region;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getSubnetId() {
        return subnetId;
    }
}
