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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.EC2DhcpConfiguration;
import cloudspec.aws.ec2.resource.nested.EC2Resource;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.DhcpOptions;

import java.util.List;
import java.util.Objects;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2DhcpOptionsResource.RESOURCE_NAME,
        description = "DHCP Options"
)
public class EC2DhcpOptionsResource extends EC2Resource {
    public static final String RESOURCE_NAME = "dhcp_options";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "dhcp_configurations",
            description = "One or more DHCP options in the set"
    )
    private final List<EC2DhcpConfiguration> dhcpConfigurations;

    @IdDefinition
    @PropertyDefinition(
            name = "dhcp_options_id",
            description = "The ID of the set of DHCP options"
    )
    private final String dhcpOptionsId;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the DHCP options set"
    )
    private final List<KeyValue> tags;

    public EC2DhcpOptionsResource(String ownerId, String region, List<EC2DhcpConfiguration> dhcpConfigurations,
                                  String dhcpOptionsId, List<KeyValue> tags) {
        super(ownerId, region);
        this.dhcpConfigurations = dhcpConfigurations;
        this.dhcpOptionsId = dhcpOptionsId;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2DhcpOptionsResource that = (EC2DhcpOptionsResource) o;
        return Objects.equals(dhcpConfigurations, that.dhcpConfigurations) &&
                Objects.equals(dhcpOptionsId, that.dhcpOptionsId) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dhcpConfigurations, dhcpOptionsId, tags);
    }

    public static EC2DhcpOptionsResource fromSdk(String regionName, DhcpOptions dhcpOptions) {
        return new EC2DhcpOptionsResource(
                dhcpOptions.ownerId(),
                regionName,
                EC2DhcpConfiguration.fromSdk(dhcpOptions.dhcpConfigurations()),
                dhcpOptions.dhcpOptionsId(),
                tagsFromSdk(dhcpOptions.tags())
        );
    }
}
