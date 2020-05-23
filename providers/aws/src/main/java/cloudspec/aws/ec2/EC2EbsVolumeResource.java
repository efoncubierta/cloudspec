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
        name = EC2EbsVolumeResource.RESOURCE_NAME,
        description = "EBS Volume"
)
public class EC2EbsVolumeResource extends EC2Resource {
    public static final String RESOURCE_NAME = "ebs_volume";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @IdDefinition
    @PropertyDefinition(
            name = "id",
            description = "Volume ID"
    )
    public String id;

    @PropertyDefinition(
            name = "region",
            description = "AWS Region"
    )
    public String region;

    @PropertyDefinition(
            name = "type",
            description = "Volume type"
    )
    public String type;

    @PropertyDefinition(
            name = "availability_zone",
            description = "Availability zone"
    )
    public String availabilityZone;

    @PropertyDefinition(
            name = "size",
            description = "Volume size in GB"
    )
    public Integer size;

    @PropertyDefinition(
            name = "iops",
            description = "Volume IOPs"
    )
    public Integer iops;

    @PropertyDefinition(
            name = "encrypted",
            description = "Flag indicating whether the volume is encrypted"
    )
    public Boolean encrypted;

    @PropertyDefinition(
            name = "multi_attach_enabled",
            description = "Flag indicated whether the volume has multi-attach enabled"
    )
    public Boolean multiAttachEnabled;

    @PropertyDefinition(
            name = "state",
            description = "State"
    )
    public String state;

    @PropertyDefinition(
            name = "tags",
            description = "List of tags"
    )
    public List<KeyValue> tags;
}