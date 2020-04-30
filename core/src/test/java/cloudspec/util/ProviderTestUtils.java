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
package cloudspec.util;

import cloudspec.ProvidersRegistry;
import cloudspec.model.Provider;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProviderTestUtils {
    public static final String TEST_PROVIDER_NAME = "myprovider";

    public static final Provider TEST_PROVIDER = mock(Provider.class);
    public static final ProvidersRegistry TEST_PROVIDERS_REGISTRY = mock(ProvidersRegistry.class);

    static {
        // test provider
        when(TEST_PROVIDER.getProviderName()).thenReturn(TEST_PROVIDER_NAME);
        when(TEST_PROVIDER.getResourceDefs()).thenReturn(Collections.singletonList(ResourceTestUtils.TEST_RESOURCE_DEF));
    }

    static {
        when(TEST_PROVIDERS_REGISTRY.getProvider(TEST_PROVIDER_NAME)).thenReturn(Optional.of(TEST_PROVIDER));
        when(TEST_PROVIDERS_REGISTRY.getProviders()).thenReturn(Collections.singletonList(TEST_PROVIDER));
    }
}
