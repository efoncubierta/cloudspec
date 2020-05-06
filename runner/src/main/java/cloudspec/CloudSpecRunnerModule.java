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
