package cloudspec;

import cloudspec.aws.AWSProvider;
import cloudspec.provider.ProvidersRegistry;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class CloudSpecRunnerModule {
    @Provides
    @Singleton
    public static ProvidersRegistry provideProvidersRegistry() {
        ProvidersRegistry registry = new ProvidersRegistry();

        // register providers
        registry.register(AWSProvider.provider());

        return registry;
    }

    @Provides
    public static String provideVersion() {
        return "0.0.1";
    }
}
