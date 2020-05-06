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
package cloudspec;

import cloudspec.model.Provider;
import cloudspec.model.ResourceDefRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProvidersRegistry {
    private final List<Provider> providers = new ArrayList<>();

    public Optional<Provider> getProvider(ResourceDefRef resourceDefRef) {
        return getProvider(resourceDefRef.getProviderName());
    }

    public Optional<Provider> getProvider(String providerName) {
        return getProviders().stream().filter(provider -> provider.getName().equals(providerName)).findFirst();
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void register(Provider provider) {
        // filter out existing provider
        providers.removeIf(p -> provider.getName().equals(p.getName()));

        // register new provider
        providers.add(provider);
    }
}
