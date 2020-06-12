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
package cloudspec

import cloudspec.aws.AWSClientsProvider
import cloudspec.aws.AWSProvider
import cloudspec.aws.IAWSClientsProvider
import cloudspec.graph.GraphResourceDefStore
import cloudspec.graph.GraphResourceStore
import cloudspec.graph.GraphResourceValidator
import cloudspec.store.ResourceDefStore
import cloudspec.store.ResourceStore
import cloudspec.validator.ResourceValidator
import dagger.Module
import dagger.Provides
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import javax.inject.Singleton

@Module
object CloudSpecRunnerModule {
    @Provides
    @Singleton
    fun provideProvidersRegistry(awsProvider: AWSProvider): ProvidersRegistry {
        val registry = ProvidersRegistry()
        registry.register(awsProvider)
        return registry
    }

    @Provides
    @Singleton
    fun provideAWSProvider(clientsProvider: IAWSClientsProvider): AWSProvider {
        return AWSProvider(clientsProvider)
    }

    @Provides
    @Singleton
    fun provideAWSClientsProvider(): IAWSClientsProvider {
        return AWSClientsProvider()
    }

    @Provides
    @Singleton
    fun provideCloudSpecManager(providersRegistry: ProvidersRegistry,
                                resourceDefStore: ResourceDefStore,
                                resourceStore: ResourceStore,
                                resourceValidator: ResourceValidator): CloudSpecManager {
        return CloudSpecManager(providersRegistry,
                                resourceDefStore,
                                resourceStore,
                                resourceValidator)
    }

    @Provides
    @Singleton
    fun provideResourceDefStore(graph: Graph): ResourceDefStore {
        return GraphResourceDefStore(graph)
    }

    @Provides
    @Singleton
    fun provideResourceStore(graph: Graph): ResourceStore {
        return GraphResourceStore(graph)
    }

    @Provides
    @Singleton
    fun provideResourceValidator(graph: Graph): ResourceValidator {
        return GraphResourceValidator(graph)
    }

    @Provides
    @Singleton
    fun provideGraph(): Graph {
        return TinkerGraph.open()
    }

    @Provides
    @Singleton
    fun provideVersion(): String {
        return "0.0.1"
    }
}
