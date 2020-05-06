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
import cloudspec.model.ProviderTest;
import cloudspec.store.ResourceDefStore;
import cloudspec.store.ResourceStore;
import cloudspec.validator.ResourceValidator;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CloudSpecManagerTest {
    public static final ProvidersRegistry PROVIDERS_REGISTRY = mock(ProvidersRegistry.class);
    public static final ResourceDefStore RESOURCE_DEF_STORE = mock(ResourceDefStore.class);
    public static final ResourceStore RESOURCE_STORE = mock(ResourceStore.class);
    public static final ResourceValidator RESOURCE_VALIDATOR = mock(ResourceValidator.class);

    static {
        when(PROVIDERS_REGISTRY.getProviders()).thenReturn(Collections.singletonList(new MyProvider()));
    }

    @Test
    public void shouldInit() {
        ProvidersRegistry registry = new ProvidersRegistry();

        registry.register(ProviderTest.TEST_PROVIDER);

        new CloudSpecManager(registry, RESOURCE_DEF_STORE, RESOURCE_STORE, RESOURCE_VALIDATOR);

        // TODO add assertions
    }
}
