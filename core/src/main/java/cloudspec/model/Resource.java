package cloudspec.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resource {
    private final Map<String, ResourceAttribute> attributesMap = new HashMap<>();
    private final Map<String, ResourceFunction> functionsMap = new HashMap<>();

    public Resource(List<ResourceAttribute> attributes, List<ResourceFunction> functions) {
        addAttributes(attributes);
        addFunctions(functions);
    }

    public ResourceAttribute getAttribute(String attributeName) {
        return attributesMap.get(attributeName);
    }

    public List<ResourceAttribute> getAttributes() {
        return new ArrayList<>(attributesMap.values());
    }

    public ResourceFunction getFunction(String functionName) {
        return functionsMap.get(functionName);
    }

    public List<ResourceFunction> getFunctions() {
        return new ArrayList<>(functionsMap.values());
    }

    private void addAttributes(List<ResourceAttribute> attributes) {
        if (attributes != null) {
            attributes.forEach(attribute -> {
                attributesMap.put(attribute.getName(), attribute);
            });
        }
    }

    private void addFunctions(List<ResourceFunction> functions) {
        if (functions != null) {
            functions.forEach(function -> {
                functionsMap.put(function.getName(), function);
            });
        }
    }

    @Override
    public String toString() {
        return "Resource{" +
                "attributesMap=" + attributesMap +
                ", functionsMap=" + functionsMap +
                '}';
    }
}
