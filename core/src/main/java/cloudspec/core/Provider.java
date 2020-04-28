package cloudspec.core;

import java.util.List;
import java.util.Optional;

public interface Provider {
    String getProviderName();

    default Optional<ResourceDef> getResourceDef(String resourceFQName) {
        return getResourceDefs().stream().filter(resourceDef -> resourceDef.getFQName().equals(resourceFQName)).findFirst();
    }

    List<ResourceDef> getResourceDefs();
}
