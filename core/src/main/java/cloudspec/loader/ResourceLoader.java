/*-
 * #%L
 * CloudSpec Core Library
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
package cloudspec.loader;

import cloudspec.ProvidersRegistry;
import cloudspec.annotation.ResourceReflectionUtil;
import cloudspec.lang.*;
import cloudspec.model.Association;
import cloudspec.model.Associations;
import cloudspec.model.Resource;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceDefStore;
import cloudspec.store.ResourceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class ResourceLoader {
    private final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

    private final Set<ResourceDefRef> loadedResourceDefs = new HashSet<>();

    private final ProvidersRegistry providersRegistry;
    private final ResourceDefStore resourceDefStore;
    private final ResourceStore resourceStore;

    public ResourceLoader(ProvidersRegistry providersRegistry,
                          ResourceDefStore resourceDefStore,
                          ResourceStore resourceStore) {
        this.providersRegistry = providersRegistry;
        this.resourceDefStore = resourceDefStore;
        this.resourceStore = resourceStore;
    }

    public void load(CloudSpec spec) {
        LOGGER.info("Loading resources required by the cloud spec");

        spec.getGroups().stream()
                .flatMap(groupExpr -> groupExpr.getRules().stream())
                .forEach(this::loadFromRule);
    }

    private void loadFromRule(RuleExpr ruleExpr) {
        Optional<ResourceDefRef> resourceDefRefOpt = ResourceDefRef.fromString(ruleExpr.getResourceDefRef());
        if (!resourceDefRefOpt.isPresent()) {
            LOGGER.error("Malformed resource definition '{}'. Ignoring it.", resourceDefRefOpt);
            return;
        }

        // Load all resource definitions to the plan
        getAllResources(resourceDefRefOpt.get())
                .forEach(resource -> {
                    // load dependent resources from each statement
                    ruleExpr.getWithExpr()
                            .getStatements()
                            .forEach(statement -> loadFromStatement(resource, statement));

                    // load dependent resources from each statement
                    ruleExpr.getAssertExpr()
                            .getStatements()
                            .forEach(statement -> loadFromStatement(resource, statement));
                });
    }

    private void loadFromStatement(Resource resource, Statement statement) {
        if (statement instanceof NestedStatement) {
            loadFromStatement(resource, ((NestedStatement) statement).getStatement());
        } else if (statement instanceof AssociationStatement) {
            loadFromAssociationStatement(resource, (AssociationStatement) statement);
        }
    }

    private void loadFromAssociationStatement(Resource resource, AssociationStatement statement) {
        LOGGER.debug("Loading resources for association '{}' in resource '{}' with id '{}'",
                statement.getAssociationName(), resource.getResourceDefRef(), resource.getResourceId());
        Optional<Association> associationOpt = resource.getAssociation(statement.getAssociationName());
        if (!associationOpt.isPresent()) {
            LOGGER.error(
                    "Association '{}' does not exist in resource '{}' with id '{}'. Ignoring it.",
                    statement.getAssociationName(),
                    resource.getResourceDefRef(),
                    resource.getResourceId()
            );
            return;
        }

        // load associated resource
        Association association = associationOpt.get();
        Optional<Resource> associatedResourceOpt = getResourceById(association.getResourceDefRef(), association.getResourceId());
        if (!associatedResourceOpt.isPresent()) {
            LOGGER.error(
                    "Associated resource '{}' in resource '{}' with id '{}' does not exist. Ignoring it.",
                    statement.getAssociationName(),
                    resource.getResourceDefRef(),
                    resource.getResourceId()
            );
            return;
        }

        // create association to target resource
        resourceStore.setAssociation(
                resource.getResourceDefRef(),
                resource.getResourceId(),
                association
        );

        // load resources from each sub statement
        statement.getStatements()
                .forEach(stmt ->
                        loadFromStatement(associatedResourceOpt.get(), stmt)
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
                    .peek(resource -> resourceStore.createResource(
                            resource.getResourceDefRef(),
                            resource.getResourceId(),
                            resource.getProperties(),
                            new Associations()
                    ));
        } else {
            return resourceStore.getResourcesByDefinition(resourceDefRef).stream();
        }
    }

    private Optional<Resource> getResourceById(ResourceDefRef resourceDefRef, String resourceId) {
        Optional<Resource> resourceOpt = resourceStore.getResource(resourceDefRef, resourceId);
        if (!resourceOpt.isPresent()) {
            LOGGER.debug("Loading resource of type '{}' with id '{}'", resourceDefRef, resourceId);

            resourceOpt = providersRegistry.getProvider(resourceDefRef.getProviderName())
                    .map(provider -> provider.getResource(resourceDefRef, resourceId))
                    .flatMap(ResourceReflectionUtil::toResource);

            resourceOpt.ifPresent(resource -> resourceStore.createResource(
                    resource.getResourceDefRef(),
                    resource.getResourceId(),
                    resource.getProperties(),
                    new Associations()
            ));

            return resourceOpt;
        }
        return Optional.empty();
    }
}
