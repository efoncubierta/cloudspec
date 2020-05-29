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
package cloudspec.aws;

import cloudspec.annotation.ProviderDefinition;
import cloudspec.aws.ec2.loader.*;
import cloudspec.aws.ec2.resource.*;
import cloudspec.aws.iam.IAMInstanceProfileResource;
import cloudspec.aws.s3.S3BucketLoader;
import cloudspec.aws.s3.S3BucketResource;
import cloudspec.aws.sns.SNSTopicLoader;
import cloudspec.aws.sns.SNSTopicResource;
import cloudspec.aws.sqs.SQSQueueLoader;
import cloudspec.aws.sqs.SQSQueueResource;
import cloudspec.model.BaseProvider;
import cloudspec.model.ResourceDefRef;

import java.util.*;

@ProviderDefinition(
        name = "aws",
        description = "Amazon Web Services",
        resources = {
                EC2ImageResource.class,
                EC2CapacityReservationResource.class,
                EC2DhcpOptionsResource.class,
                EC2ElasticGpuResource.class,
                EC2FlowLogResource.class,
                EC2InstanceResource.class,
                EC2InternetGatewayResource.class,
                EC2LocalGatewayResource.class,
                EC2NatGatewayResource.class,
                EC2NetworkAclResource.class,
                EC2NetworkInterfaceResource.class,
                EC2ReservedInstancesResource.class,
                EC2RouteTableResource.class,
                EC2SecurityGroupResource.class,
                EC2SnapshotResource.class,
                EC2SubnetResource.class,
                EC2TransitGatewayResource.class,
                EC2VolumeResource.class,
                EC2VpcPeeringConnectionResource.class,
                EC2VpcResource.class,
                IAMInstanceProfileResource.class,
                S3BucketResource.class,
                SQSQueueResource.class,
                SNSTopicResource.class
        }
)
public class AWSProvider extends BaseProvider {
    public static final String PROVIDER_NAME = "aws";

    private final Map<String, AWSResourceLoader<?>> loaders = new HashMap<>();

    public AWSProvider(IAWSClientsProvider clientsProvider) {
        loaders.put(
                EC2ImageResource.RESOURCE_DEF_REF.toString(),
                new EC2ImageLoader(clientsProvider)
        );
        loaders.put(
                EC2CapacityReservationResource.RESOURCE_DEF_REF.toString(),
                new EC2CapacityReservationLoader(clientsProvider)
        );
        loaders.put(
                EC2DhcpOptionsResource.RESOURCE_DEF_REF.toString(),
                new EC2DhcpOptionsLoader(clientsProvider)
        );
        loaders.put(
                EC2ElasticGpuResource.RESOURCE_DEF_REF.toString(),
                new EC2ElasticGpuLoader(clientsProvider)
        );
        loaders.put(
                EC2FlowLogResource.RESOURCE_DEF_REF.toString(),
                new EC2FlowLogLoader(clientsProvider)
        );
        loaders.put(
                EC2InstanceResource.RESOURCE_DEF_REF.toString(),
                new EC2InstanceLoader(clientsProvider)
        );
        loaders.put(
                EC2InternetGatewayResource.RESOURCE_DEF_REF.toString(),
                new EC2InternetGatewayLoader(clientsProvider)
        );
        loaders.put(
                EC2LocalGatewayResource.RESOURCE_DEF_REF.toString(),
                new EC2LocalGatewayLoader(clientsProvider)
        );
        loaders.put(
                EC2NatGatewayResource.RESOURCE_DEF_REF.toString(),
                new EC2NatGatewayLoader(clientsProvider)
        );
        loaders.put(
                EC2NetworkAclResource.RESOURCE_DEF_REF.toString(),
                new EC2NetworkAclLoader(clientsProvider)
        );
        loaders.put(
                EC2NetworkInterfaceResource.RESOURCE_DEF_REF.toString(),
                new EC2NetworkInterfaceLoader(clientsProvider)
        );
        loaders.put(
                EC2ReservedInstancesResource.RESOURCE_DEF_REF.toString(),
                new EC2ReservedInstancesLoader(clientsProvider)
        );
        loaders.put(
                EC2RouteTableResource.RESOURCE_DEF_REF.toString(),
                new EC2RouteTableLoader(clientsProvider)
        );
        loaders.put(
                EC2SecurityGroupResource.RESOURCE_DEF_REF.toString(),
                new EC2SecurityGroupLoader(clientsProvider)
        );
        loaders.put(
                EC2SnapshotResource.RESOURCE_DEF_REF.toString(),
                new EC2SnapshotLoader(clientsProvider)
        );
        loaders.put(
                EC2SubnetResource.RESOURCE_DEF_REF.toString(),
                new EC2SubnetLoader(clientsProvider)
        );
        loaders.put(
                EC2TransitGatewayResource.RESOURCE_DEF_REF.toString(),
                new EC2TransitGatewayLoader(clientsProvider)
        );
        loaders.put(
                EC2VolumeResource.RESOURCE_DEF_REF.toString(),
                new EC2VolumeLoader(clientsProvider)
        );
        loaders.put(
                EC2VpcPeeringConnectionResource.RESOURCE_DEF_REF.toString(),
                new EC2VpcPeeringConnectionLoader(clientsProvider)
        );
        loaders.put(
                EC2VpcResource.RESOURCE_DEF_REF.toString(),
                new EC2VpcLoader(clientsProvider)
        );
        loaders.put(
                S3BucketResource.RESOURCE_DEF_REF.toString(),
                new S3BucketLoader(clientsProvider)
        );
        loaders.put(
                SQSQueueResource.RESOURCE_DEF_REF.toString(),
                new SQSQueueLoader(clientsProvider)
        );
        loaders.put(
                SNSTopicResource.RESOURCE_DEF_REF.toString(),
                new SNSTopicLoader(clientsProvider)
        );
    }

    @Override
    public List<?> getResources(ResourceDefRef resourceDefRef) {
        return getLoader(resourceDefRef)
                .map(AWSResourceLoader::getAll)
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<?> getResource(ResourceDefRef resourceDefRef, String resourceId) {
        return getLoader(resourceDefRef)
                .flatMap(loader -> loader.getById(resourceId));
    }

    private Optional<AWSResourceLoader<?>> getLoader(ResourceDefRef resourceDefRef) {
        return Optional.ofNullable(loaders.get(resourceDefRef.toString()));
    }
}

