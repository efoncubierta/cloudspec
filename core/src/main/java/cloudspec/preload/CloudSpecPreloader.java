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
package cloudspec.preload;

import cloudspec.core.Provider;
import cloudspec.core.ProvidersRegistry;
import cloudspec.core.ResourceDef;
import cloudspec.core.spec.CloudSpec;
import cloudspec.core.spec.GroupExpr;
import cloudspec.core.spec.RuleExpr;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CloudSpecPreloader {
    private final ProvidersRegistry providersRegistry;

    public CloudSpecPreloader(ProvidersRegistry providersRegistry) {
        this.providersRegistry = providersRegistry;
    }

    public void preload(CloudSpec spec) {
        System.out.println("CloudSpec Preloader");

        preloadGroups(spec.getGroups());
    }

    private void preloadGroups(List<GroupExpr> groups) {
        groups.stream().forEach(this::preloadGroup);
    }

    private void preloadGroup(GroupExpr group) {
        preloadRules(group.getRules());
    }

    private void preloadRules(List<RuleExpr> rules) {
        rules.stream().forEach(this::preloadRule);
    }

    private void preloadRule(RuleExpr rule) {
        String providerName = rule.getResourceTypeFQName().split(":")[0];

        // lookup provider
        Provider provider = providersRegistry.getProvider(providerName);
        if (Objects.isNull(provider)) {
            throw new CloudSpecPreloaderException(String.format("Provider '%s' not found.", providerName));
        }

        // lookup resource definition
        Optional<ResourceDef> resourceDefOpt = provider.getResourceDef(rule.getResourceTypeFQName());
        if (!resourceDefOpt.isPresent()) {
            throw new CloudSpecPreloaderException(String.format("Rule validator for resource of type %s not found.", rule.getResourceTypeFQName()));
        }

        // load resources
        resourceDefOpt.get()
                .getResourceLoader()
                .load();
    }
}
