/*-
 * #%L
 * CloudSpec Core Library
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
package cloudspec.loader;

import cloudspec.ProvidersRegistry;
import cloudspec.annotation.ResourceReflectionUtil;
import cloudspec.lang.*;
import cloudspec.model.Resource;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

public class ResourceLoader {
    private final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

    private final Set<ResourceDefRef> loadedResourceDefs = new HashSet<>();

    private final ProvidersRegistry providersRegistry;
    private final ResourceStore resourceStore;

    public ResourceLoader(ProvidersRegistry providersRegistry,
                          ResourceStore resourceStore) {
        this.providersRegistry = providersRegistry;
        this.resourceStore = resourceStore;
    }

    public void load(CloudSpec spec) {
        LOGGER.info("Loading resources required to run this test");

        spec.getGroups().stream()
                .flatMap(groupExpr -> groupExpr.getRules().stream())
                .forEach(this::loadFromRule);
    }

    private void loadFromRule(RuleExpr ruleExpr) {
        var resourceDefRefOpt = ResourceDefRef.fromString(ruleExpr.getResourceDefRef());
        if (resourceDefRefOpt.isEmpty()) {
            LOGGER.error("Malformed resource definition '{}'. Ignoring it.", resourceDefRefOpt);
            return;
        }

        // Load all resource definitions to the plan
        getAllResources(resourceDefRefOpt.get())
                .forEach(resource -> {
                    // load dependent resources from each statement
                    ruleExpr.getWithExpr()
                            .getStatements()
                            .forEach(statement -> loadFromStatement(resource, statement, new ArrayList<>()));

                    // load dependent resources from each statement
                    ruleExpr.getAssertExpr()
                            .getStatements()
                            .forEach(statement -> loadFromStatement(resource, statement, new ArrayList<>()));
                });
    }

    private void loadFromStatement(Resource resource, Statement statement, List<String> path) {
        if (statement instanceof NestedStatement) {
            var nestedPath = new ArrayList<>(path);
            nestedPath.add(((NestedStatement) statement).getPropertyName());
            ((NestedStatement) statement).getStatements()
                    .forEach(stmt -> loadFromStatement(resource, stmt, nestedPath));
        } else if (statement instanceof AssociationStatement) {
            loadFromAssociationStatement(resource, (AssociationStatement) statement, path);
        }
    }

    private void loadFromAssociationStatement(Resource resource, AssociationStatement statement, List<String> path) {
        var associationPath = new ArrayList<>(path);
        associationPath.add(statement.getAssociationName());

        LOGGER.debug("Loading resources for association '{}' in resource '{}' with id '{}'",
                statement.getAssociationName(), resource.getResourceDefRef(), resource.getResourceId());

        var associationOpt = resource.getAssociationByPath(associationPath);
        if (associationOpt.isEmpty()) {
            LOGGER.error(
                    "Association '{}' does not exist in resource '{}' with id '{}'. Ignoring it.",
                    statement.getAssociationName(),
                    resource.getResourceDefRef(),
                    resource.getResourceId()
            );
            return;
        }

        // load associated resource
        var association = associationOpt.get();
        var associatedResourceOpt = getResourceById(association.getResourceDefRef(), association.getResourceId());
        if (associatedResourceOpt.isEmpty()) {
            LOGGER.error(
                    "Associated resource '{}' in resource '{}' with id '{}' does not exist. Ignoring it.",
                    statement.getAssociationName(),
                    resource.getResourceDefRef(),
                    resource.getResourceId()
            );
            return;
        }

        // load resources from each sub statement
        statement.getStatements()
                .forEach(stmt ->
                        loadFromStatement(associatedResourceOpt.get(), stmt, new ArrayList<>())
                );
    }

    private Stream<Resource> getAllResources(ResourceDefRef resourceDefRef) {
        if (!loadedResourceDefs.contains(resourceDefRef)) {
            LOGGER.debug("Loading all resources of type '{}'", resourceDefRef);

            loadedResourceDefs.add(resourceDefRef);

            return providersRegistry.getProvider(resourceDefRef.getProviderName())
                    .map(provider -> provider.getResources(resourceDefRef).stream())
                    .orElse(Stream.empty())
                    .map(ResourceReflectionUtil::toResource)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(resource -> resourceStore.saveResource(
                            resource.getResourceDefRef(),
                            resource.getResourceId(),
                            resource.getProperties(),
                            resource.getAssociations()
                    ));
        } else {
            return resourceStore.getResourcesByDefinition(resourceDefRef).stream();
        }
    }

    private Optional<Resource> getResourceById(ResourceDefRef resourceDefRef, String resourceId) {
        // TODO load incomplete resource from store
//        Optional<Resource> resourceOpt = resourceStore.getResource(resourceDefRef, resourceId);
//        if (!resourceOpt.isPresent()) {
        LOGGER.debug("Loading resource of type '{}' with id '{}'", resourceDefRef, resourceId);

        var resourceOpt = providersRegistry.getProvider(resourceDefRef.getProviderName())
                .flatMap(provider -> provider.getResource(resourceDefRef, resourceId))
                .flatMap(ResourceReflectionUtil::toResource);

        resourceOpt.ifPresent(resource -> resourceStore.saveResource(
                resource.getResourceDefRef(),
                resource.getResourceId(),
                resource.getProperties(),
                resource.getAssociations()
        ));

        return resourceOpt;
//        }
//        return resourceOpt;
    }
}
