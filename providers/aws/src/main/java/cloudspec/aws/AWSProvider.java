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
import cloudspec.aws.ec2.EC2InstanceLoader;
import cloudspec.aws.ec2.EC2InstanceResource;
import cloudspec.aws.s3.S3BucketLoader;
import cloudspec.aws.s3.S3BucketResource;
import cloudspec.model.BaseProvider;
import cloudspec.model.ResourceDefRef;

import java.util.*;

@ProviderDefinition(
        name = "aws",
        description = "Amazon Web Services",
        resources = {EC2InstanceResource.class, S3BucketResource.class}
)
public class AWSProvider extends BaseProvider {
    public static final String PROVIDER_NAME = "aws";

    private final Map<String, AWSResourceLoader<?>> loaders = new HashMap<>();

    public AWSProvider(IAWSClientsProvider clientsProvider) {
        loaders.put(EC2InstanceResource.RESOURCE_DEF_REF.toString(), new EC2InstanceLoader(clientsProvider));
        loaders.put(S3BucketResource.RESOURCE_DEF_REF.toString(), new S3BucketLoader(clientsProvider));
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
