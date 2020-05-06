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
package cloudspec.loader;

import cloudspec.lang.AssertExpr;
import cloudspec.lang.CloudSpec;
import cloudspec.lang.RuleExpr;
import cloudspec.lang.WithExpr;
import cloudspec.model.ResourceDefRef;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ResourceLoaderPlan {
    private final Set<ResourceDefRef> resourceDefRefs = new HashSet<>();

    public ResourceLoaderPlan(CloudSpec spec) {
        spec.getGroups().stream()
                .flatMap(groupExpr -> groupExpr.getRules().stream())
                .forEach(this::processRule);
    }

    private void processRule(RuleExpr ruleExpr) {
        Optional<ResourceDefRef> resourceDefRefOpt = ResourceDefRef.fromString(ruleExpr.getResourceDefRef());
        if (!resourceDefRefOpt.isPresent()) {
            // TODO manage error
            throw new RuntimeException("Error");
        }

        resourceDefRefs.add(resourceDefRefOpt.get());

        ruleExpr.getWiths().forEach(this::processWithExpr);
        ruleExpr.getAsserts().forEach(this::processAssertExpr);
    }

    private void processWithExpr(WithExpr withExpr) {
        // TODO get associations
    }

    private void processAssertExpr(AssertExpr assertExpr) {
        // TODO get associations
    }
}
