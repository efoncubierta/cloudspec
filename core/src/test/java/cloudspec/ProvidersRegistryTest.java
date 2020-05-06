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

import cloudspec.model.MyProvider;
import cloudspec.model.Provider;
import cloudspec.model.ProviderTest;
import cloudspec.util.ModelTestUtils;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProvidersRegistryTest {
    @Test
    public void shouldRegisterProvider() {
        ProvidersRegistry registry = new ProvidersRegistry();

        registry.register(ProviderTest.TEST_PROVIDER);

        Optional<Provider> providerOpt = registry.getProvider(ModelTestUtils.PROVIDER_NAME);
        assertTrue(providerOpt.isPresent());
        assertEquals(ModelTestUtils.PROVIDER_NAME, providerOpt.get().getName());

        List<Provider> providers = registry.getProviders();
        assertEquals(1, providers.size());
        assertEquals(ModelTestUtils.PROVIDER_NAME, providers.get(0).getName());
    }
}
