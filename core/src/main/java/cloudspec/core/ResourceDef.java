package cloudspec.core;

import java.util.List;
import java.util.Optional;

public interface ResourceDef {
    String getProviderName();

    String getGroupName();

    String getResourceName();

    default String getFQName() {
        return String.format("%s:%s:%s", getProviderName(), getGroupName(), getResourceName());
    }

    default Optional<ResourceAttributeDef> getAttributeDefinition(String attributeName) {
        return getAttributesDefinitions().stream().filter(def -> def.getName().equals(attributeName)).findFirst();
    }

    List<ResourceAttributeDef> getAttributesDefinitions();

    default Optional<ResourceFunctionDef> getFunctionDefinition(String functionName) {
        return getFunctionsDefinitions().stream().filter(def -> def.getName().equals(functionName)).findFirst();
    }

    List<ResourceFunctionDef> getFunctionsDefinitions();

    ResourceLoader getResourceLoader();
}
