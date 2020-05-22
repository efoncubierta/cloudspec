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
import cloudspec.aws.ec2.*;
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
                EC2AmiResource.class,
                EC2EbsVolumeResource.class,
                EC2InstanceResource.class,
                EC2NetworkInterfaceResource.class,
                EC2VpcResource.class,
                EC2SubnetResource.class,
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
        loaders.put(EC2AmiResource.RESOURCE_DEF_REF.toString(), new EC2AmiLoader(clientsProvider));
        loaders.put(EC2EbsVolumeResource.RESOURCE_DEF_REF.toString(), new EC2EbsVolumeLoader(clientsProvider));
        loaders.put(EC2InstanceResource.RESOURCE_DEF_REF.toString(), new EC2InstanceLoader(clientsProvider));
        loaders.put(EC2NetworkInterfaceResource.RESOURCE_DEF_REF.toString(), new EC2NetworkInterfaceLoader(clientsProvider));
        loaders.put(EC2VpcResource.RESOURCE_DEF_REF.toString(), new EC2VpcLoader(clientsProvider));
        loaders.put(EC2SubnetResource.RESOURCE_DEF_REF.toString(), new EC2SubnetLoader(clientsProvider));
        loaders.put(S3BucketResource.RESOURCE_DEF_REF.toString(), new S3BucketLoader(clientsProvider));
        loaders.put(SQSQueueResource.RESOURCE_DEF_REF.toString(), new SQSQueueLoader(clientsProvider));
        loaders.put(SNSTopicResource.RESOURCE_DEF_REF.toString(), new SNSTopicLoader(clientsProvider));
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
                .map(loader -> loader.getById(resourceId));
    }

    private Optional<AWSResourceLoader<?>> getLoader(ResourceDefRef resourceDefRef) {
        return Optional.ofNullable(loaders.get(resourceDefRef.toString()));
    }
}
