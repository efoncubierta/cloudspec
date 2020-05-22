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

import java.util.ArrayList;
import java.util.List;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2AmiResource.RESOURCE_NAME,
        description = "Amazon Machine Image"
)
public class EC2AmiResource extends EC2Resource {
    public static final String RESOURCE_NAME = "ami";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @IdDefinition
    @PropertyDefinition(
            name = "id",
            description = "AMI ID"
    )
    public String id;

    @PropertyDefinition(
            name = "region",
            description = "AWS Region"
    )
    public String region;

    @PropertyDefinition(
            name = "type",
            description = "AMI Type"
    )
    public String type;

    @PropertyDefinition(
            name = "name",
            description = "AMI Name"
    )
    public String name;

    @PropertyDefinition(
            name = "location",
            description = "AMI Location"
    )
    public String location;

    @PropertyDefinition(
            name = "architecture",
            description = "Type of architecture"
    )
    public String architecture;

    @PropertyDefinition(
            name = "kernel_id",
            description = "ID of the Kernel"
    )
    public String kernelId;

    @PropertyDefinition(
            name = "platform",
            description = "OS Platform"
    )
    public String platform;

    @PropertyDefinition(
            name = "root_device",
            description = "Root Device"
    )
    public RootDevice rootDevice = new RootDevice();

    @PropertyDefinition(
            name = "devices",
            description = "Attached devices"
    )
    public List<Device> devices = new ArrayList<>();

    @PropertyDefinition(
            name = "ena_support",
            description = "Flag indicating ENA support"
    )
    public Boolean enaSupport;

    @PropertyDefinition(
            name = "state",
            description = "State of the image"
    )
    public String state;

    @PropertyDefinition(
            name = "product_codes",
            description = "Product codes"
    )
    public List<ProductCode> productCodes = new ArrayList<>();

    @PropertyDefinition(
            name = "tags",
            description = "List of tags"
    )
    public List<KeyValue> tags;

    public static class RootDevice {
        @PropertyDefinition(
                name = "name",
                description = "Device name"
        )
        public String name;

        @PropertyDefinition(
                name = "type",
                description = "Device type"
        )
        public String type;
    }

    public static class Device {
        @PropertyDefinition(
                name = "name",
                description = "Device name"
        )
        public String name;

        @PropertyDefinition(
                name = "virtual_name",
                description = "Device virtual name"
        )
        public String virtualName;

        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Delete on termination"
        )
        public Boolean deleteOnTermination;

        @PropertyDefinition(
                name = "volume_type",
                description = "EBS volume type"
        )
        public String volumeType;

        @PropertyDefinition(
                name = "volume_size",
                description = "EBS volume size"
        )
        public Integer volumeSize;

        @PropertyDefinition(
                name = "encrypted",
                description = "Flag indicating if the volume is encrypted"
        )
        public Boolean encrypted;
    }

    public static class ProductCode {
        @PropertyDefinition(
                name = "id",
                description = "Product code ID"
        )
        public String id;

        @PropertyDefinition(
                name = "type",
                description = "Product code type"
        )
        public String type;
    }
}
