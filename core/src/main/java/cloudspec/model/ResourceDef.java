package cloudspec.model;

import java.util.List;
import java.util.Optional;

public class ResourceDef {
    private final String providerName;
    private final String groupName;
    private final String resourceName;
    private final List<ResourceAttributeDef> attributesDefinitions;
    private final List<ResourceFunctionDef> functionsDefinitions;
    private final ResourceLoader resourceLoader;

    public ResourceDef(String providerName, String groupName, String resourceName, List<ResourceAttributeDef> attributesDefinitions, List<ResourceFunctionDef> functionsDefinitions, ResourceLoader resourceLoader) {
        this.providerName = providerName;
        this.groupName = groupName;
        this.resourceName = resourceName;
        this.attributesDefinitions = attributesDefinitions;
        this.functionsDefinitions = functionsDefinitions;
        this.resourceLoader = resourceLoader;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFQName() {
        return String.format("%s:%s:%s", getProviderName(), getGroupName(), getResourceName());
    }

    public Optional<ResourceAttributeDef> getAttributeDefinition(String attributeName) {
        return attributesDefinitions.stream().filter(def -> def.getName().equals(attributeName)).findFirst();
    }

    public List<ResourceAttributeDef> getAttributesDefinitions() {
        return attributesDefinitions;
    }

    public Optional<ResourceFunctionDef> getFunctionDefinition(String functionName) {
        return functionsDefinitions.stream().filter(def -> def.getName().equals(functionName)).findFirst();
    }

    public List<ResourceFunctionDef> getFunctionsDefinitions() {
        return functionsDefinitions;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
