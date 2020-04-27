package cloudspec.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Provider {
    private String providerName;
    private final Map<String, ResourceDef> resourceDefMap = new HashMap<>();

    public Provider(String providerName, List<ResourceDef> resourceDefs) {
        this.providerName = providerName;
        addResourceDefs(resourceDefs);
    }

    public String getProviderName() {
        return providerName;
    }

    public ResourceDef getResourceDef(String resourceFQName) {
        return resourceDefMap.get(resourceFQName);
    }

    private void addResourceDefs(List<ResourceDef> resourceDefs) {
        if (resourceDefs != null) {
            resourceDefs.forEach(resourceDef -> {
                resourceDefMap.put(resourceDef.getFQName(), resourceDef);
            });
        }
    }
}
