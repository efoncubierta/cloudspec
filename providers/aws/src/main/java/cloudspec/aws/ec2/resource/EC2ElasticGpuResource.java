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

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.AWSProvider;
import cloudspec.aws.ec2.resource.nested.EC2Resource;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.ElasticGpuHealth;
import software.amazon.awssdk.services.ec2.model.ElasticGpus;

import java.util.List;
import java.util.Objects;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2ElasticGpuResource.RESOURCE_NAME,
        description = "Elastic Graphics Accelerator"
)
public class EC2ElasticGpuResource extends EC2Resource {
    public static final String RESOURCE_NAME = "elastic_gpu";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @IdDefinition
    @PropertyDefinition(
            name = "elastic_gpu_id",
            description = "The ID of the Elastic Graphics accelerator"
    )
    private final String elasticGpuId;

    @PropertyDefinition(
            name = "availability_zone",
            description = "The Availability Zone in the which the Elastic Graphics accelerator resides"
    )
    private final String availabilityZone;

    @PropertyDefinition(
            name = "elastic_gpu_type",
            description = "The type of Elastic Graphics accelerator"
    )
    private final String elasticGpuType;

    @PropertyDefinition(
            name = "elastic_gpu_health",
            description = "The status of the Elastic Graphics accelerator",
            exampleValues = "OK | IMPAIRED"
    )
    private final String elasticGpuHealth;

    @PropertyDefinition(
            name = "elastic_gpu_state",
            description = "The state of the Elastic Graphics accelerator",
            exampleValues = "ATTACHED"
    )
    private final String elasticGpuState;

    @AssociationDefinition(
            name = "instance",
            description = "The instance to which the Elastic Graphics accelerator is attached",
            targetClass = EC2InstanceResource.class
    )
    private final String instanceId;

    @PropertyDefinition(
            name = "tags",
            description = "The tags assigned to the Elastic Graphics accelerator"
    )
    private final List<KeyValue> tags;

    public EC2ElasticGpuResource(String ownerId, String region, String elasticGpuId, String availabilityZone,
                                 String elasticGpuType, String elasticGpuHealth, String elasticGpuState,
                                 String instanceId, List<KeyValue> tags) {
        super(ownerId, region);
        this.elasticGpuId = elasticGpuId;
        this.availabilityZone = availabilityZone;
        this.elasticGpuType = elasticGpuType;
        this.elasticGpuHealth = elasticGpuHealth;
        this.elasticGpuState = elasticGpuState;
        this.instanceId = instanceId;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2ElasticGpuResource that = (EC2ElasticGpuResource) o;
        return Objects.equals(elasticGpuId, that.elasticGpuId) &&
                Objects.equals(availabilityZone, that.availabilityZone) &&
                Objects.equals(elasticGpuType, that.elasticGpuType) &&
                Objects.equals(elasticGpuHealth, that.elasticGpuHealth) &&
                Objects.equals(elasticGpuState, that.elasticGpuState) &&
                Objects.equals(instanceId, that.instanceId) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elasticGpuId, availabilityZone, elasticGpuType, elasticGpuHealth, elasticGpuState,
                instanceId, tags);
    }

    public static EC2ElasticGpuResource fromSdk(String regionName, ElasticGpus elasticGpus) {
        if (Objects.isNull(elasticGpus)) {
            return null;
        }

        return new EC2ElasticGpuResource(
                "",
                regionName,
                elasticGpus.elasticGpuId(),
                elasticGpus.availabilityZone(),
                elasticGpus.elasticGpuType(),
                elasticGpuHealthStatusFromSdk(elasticGpus.elasticGpuHealth()),
                elasticGpus.elasticGpuStateAsString(),
                elasticGpus.instanceId(),
                tagsFromSdk(elasticGpus.tags())
        );
    }

    public static String elasticGpuHealthStatusFromSdk(ElasticGpuHealth elasticGpuHealth) {
        if (Objects.isNull(elasticGpuHealth)) {
            return null;
        }

        return elasticGpuHealth.statusAsString();
    }
}
