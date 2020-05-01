/*-
 * #%L
 * CloudSpec Runner
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
package cloudspec;

import cloudspec.aws.AWSClientsProvider;
import cloudspec.aws.AWSProvider;
import cloudspec.aws.IAWSClientsProvider;
import cloudspec.preflight.CloudSpecPreflight;
import cloudspec.preload.CloudSpecPreloader;
import cloudspec.service.ResourceService;
import cloudspec.validator.CloudSpecValidator;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class CloudSpecRunnerModule {
    @Provides
    @Singleton
    public static ProvidersRegistry provideProvidersRegistry(AWSProvider awsProvider) {
        ProvidersRegistry registry = new ProvidersRegistry();

        // register providers
        registry.register(awsProvider);

        return registry;
    }

    @Provides
    @Singleton
    public static AWSProvider provideAWSProvider(IAWSClientsProvider clientsProvider) {
        return new AWSProvider(clientsProvider);
    }

    @Provides
    @Singleton
    public static IAWSClientsProvider provideAWSClientsProvider() {
        return new AWSClientsProvider();
    }

    @Provides
    @Singleton
    public static CloudSpecPreflight provideCloudSpecPreflight(ResourceService resourceService) {
        return new CloudSpecPreflight(resourceService);
    }

    @Provides
    @Singleton
    public static CloudSpecPreloader provideCloudSpecPreloader(ResourceService resourceService) {
        return new CloudSpecPreloader(resourceService);
    }

    @Provides
    @Singleton
    public static CloudSpecValidator provideCloudSpecValidator(ResourceService resourceService) {
        return new CloudSpecValidator(resourceService);
    }

    @Provides
    @Singleton
    public static ResourceService provideResourceService(ProvidersRegistry providersRegistry) {
        return new ResourceService(providersRegistry);
    }

    @Provides
    @Singleton
    public static String provideVersion() {
        return "0.0.1";
    }
}
