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
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;

import java.util.List;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2VpcResource.RESOURCE_NAME,
        description = "Virtual Private Cloud"
)
public class EC2VpcResource extends EC2Resource {
    public static final String RESOURCE_NAME = "vpc";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "AWS region"
    )
    public String region;

    @IdDefinition
    @PropertyDefinition(
            name = "vpc_id",
            description = "VPC ID"
    )
    public String vpcId;

    @PropertyDefinition(
            name = "vpc_name",
            description = "VPC Name"
    )
    public String vpcName;

    @PropertyDefinition(
            name = "cidr_block",
            description = "CIDR Block"
    )
    public String cidrBlock;

    @PropertyDefinition(
            name = "state",
            description = "State"
    )
    public String state;

    @PropertyDefinition(
            name = "default",
            description = "Indicate if the VPC is the default"
    )
    public Boolean isDefault;

    @PropertyDefinition(
            name = "dns_hostnames",
            description = "Indicate if the the DNS Hostnames flag is enabled"
    )
    public Boolean dnsHostnamesEnabled;

    @PropertyDefinition(
            name = "dns_support",
            description = "Indicate if the the DNS Support flag is enabled"
    )
    public Boolean dnsSupportEnabled;

    @PropertyDefinition(
            name = "tags",
            description = "List of tags"
    )
    public List<KeyValue> tags;
}
