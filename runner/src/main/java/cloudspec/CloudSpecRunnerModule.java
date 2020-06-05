/*-
 * #%L
 * CloudSpec Runner
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec;

import cloudspec.aws.AWSClientsProvider;
import cloudspec.aws.AWSProvider;
import cloudspec.aws.IAWSClientsProvider;
import cloudspec.graph.GraphResourceDefStore;
import cloudspec.graph.GraphResourceStore;
import cloudspec.graph.GraphResourceValidator;
import cloudspec.store.ResourceDefStore;
import cloudspec.store.ResourceStore;
import cloudspec.validator.ResourceValidator;
import dagger.Module;
import dagger.Provides;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

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
    public static CloudSpecManager provideCloudSpecManager(ProvidersRegistry providersRegistry,
                                                           ResourceDefStore resourceDefStore,
                                                           ResourceStore resourceStore,
                                                           ResourceValidator resourceValidator) {
        return new CloudSpecManager(providersRegistry, resourceDefStore, resourceStore, resourceValidator);
    }

    @Provides
    @Singleton
    public static ResourceDefStore provideResourceDefStore(Graph graph) {
        return new GraphResourceDefStore(graph);
    }

    @Provides
    @Singleton
    public static ResourceStore provideResourceStore(Graph graph) {
        return new GraphResourceStore(graph);
    }

    @Provides
    @Singleton
    public static ResourceValidator provideResourceValidator(Graph graph) {
        return new GraphResourceValidator(graph);
    }

    @Provides
    @Singleton
    public static Graph provideGraph() {
        return TinkerGraph.open();
    }

    @Provides
    @Singleton
    public static String provideVersion() {
        return "0.0.1";
    }
}
