package cloudspec;

import cloudspec.aws.AWSProvider;
import cloudspec.core.ProvidersRegistry;
import cloudspec.preflight.CloudSpecPreflight;
import cloudspec.preload.CloudSpecPreloader;
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
    public static AWSProvider provideAWSProvider() {
        return new AWSProvider();
    }

    @Provides
    @Singleton
    public static CloudSpecPreflight provideCloudSpecPreflight(ProvidersRegistry providersRegistry) {
        return new CloudSpecPreflight(providersRegistry);
    }

    @Provides
    @Singleton
    public static CloudSpecPreloader provideCloudSpecPreloader(ProvidersRegistry providersRegistry) {
        return new CloudSpecPreloader(providersRegistry);
    }

    @Provides
    @Singleton
    public static CloudSpecValidator provideCloudSpecValidator(ProvidersRegistry providersRegistry) {
        return new CloudSpecValidator(providersRegistry);
    }

    @Provides
    @Singleton
    public static String provideVersion() {
        return "0.0.1";
    }
}
