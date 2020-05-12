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
        name = EC2SubnetResource.RESOURCE_NAME,
        description = "VPC Subnet"
)
public class EC2SubnetResource extends EC2Resource {
    public static final String RESOURCE_NAME = "subnet";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "vpc_id",
            description = "VPC ID"
    )
    private String vpcId;

    @IdDefinition
    @PropertyDefinition(
            name = "subnet_id",
            description = "VPC Subnet ID"
    )
    private String subnetId;

    @PropertyDefinition(
            name = "availability_zone",
            description = "AWS Availability Zone"
    )
    private String availabilityZone;

    @PropertyDefinition(
            name = "cidr_block",
            description = "CIDR Block"
    )
    private String cidrBlock;

    @PropertyDefinition(
            name = "state",
            description = "State"
    )
    private String state;

    public String getVpcId() {
        return vpcId;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getCidrBlock() {
        return cidrBlock;
    }

    public String getState() {
        return state;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String vpcId;
        private String subnetId;
        private String availabilityZone;
        private String cidrBlock;
        private String state;

        public Builder setVpcId(String vpcId) {
            this.vpcId = vpcId;
            return this;
        }

        public Builder setSubnetId(String subnetId) {
            this.subnetId = subnetId;
            return this;
        }

        public Builder setAvailabilityZone(String availabilityZone) {
            this.availabilityZone = availabilityZone;
            return this;
        }

        public Builder setCidrBlock(String cidrBlock) {
            this.cidrBlock = cidrBlock;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public EC2SubnetResource build() {
            EC2SubnetResource resource = new EC2SubnetResource();
            resource.vpcId = vpcId;
            resource.subnetId = subnetId;
            resource.availabilityZone = availabilityZone;
            resource.cidrBlock = cidrBlock;
            resource.state = state;
            return resource;
        }
    }
}
