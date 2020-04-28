package cloudspec;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = CloudSpecRunnerModule.class)
public interface CloudSpecRunnerComponent {
    CloudSpecRunner buildCloudSpecRunner();
}
