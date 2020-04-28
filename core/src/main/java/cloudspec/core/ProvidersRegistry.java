package cloudspec.core;

import cloudspec.core.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProvidersRegistry {
    private final Map<String, Provider> providerMap = new HashMap<>();

    public Provider getProvider(String providerName) {
        return providerMap.get(providerName);
    }

    public List<Provider> getProviders() {
        return new ArrayList<>(providerMap.values());
    }

    public void register(Provider provider) {
        providerMap.put(provider.getProviderName(), provider);
    }
}
