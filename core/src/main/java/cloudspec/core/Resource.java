package cloudspec.core;

import java.util.List;
import java.util.Optional;

public interface Resource {
    default Optional<ResourceAttribute> getAttribute(String attributeName) {
        return getAttributes().stream().filter(attribute -> attribute.getName().equals(attributeName)).findFirst();
    }

    List<ResourceAttribute> getAttributes();

    default Optional<ResourceFunction> getFunction(String functionName) {
        return getFunctions().stream().filter(function -> function.getName().equals(functionName)).findFirst();
    }

    List<ResourceFunction> getFunctions();
}
