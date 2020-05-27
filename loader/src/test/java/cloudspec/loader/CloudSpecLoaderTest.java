/*-
 * #%L
 * CloudSpec Loader Library
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

import cloudspec.lang.CloudSpec;
import cloudspec.lang.GroupExpr;
import cloudspec.lang.RuleExpr;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CloudSpecLoaderTest {
    private final CloudSpecLoader cloudSpecLoader = new CloudSpecLoader();

    @Test
    public void shouldLoadFullSpec() throws Exception {
        CloudSpec cloudSpecOriginal = CloudSpecGenerator.fullSpec();
        CloudSpec cloudSpecLoaded = cloudSpecLoader.load(
                new ByteArrayInputStream(cloudSpecOriginal.toCloudSpecSyntax().getBytes())
        );

        assertNotNull(cloudSpecLoaded);
        compareSpecs(cloudSpecOriginal, cloudSpecLoaded);
    }

    private void compareSpecs(CloudSpec expected, CloudSpec actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getGroups().size(), actual.getGroups().size());
        expected.getGroups().sort(Comparator.comparing(GroupExpr::getName));
        actual.getGroups().sort(Comparator.comparing(GroupExpr::getName));
        for (var i = 0; i < expected.getGroups().size(); i++) {
            compareGroups(expected.getGroups().get(i), actual.getGroups().get(i));
        }
    }

    private void compareGroups(GroupExpr expected, GroupExpr actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getRules().size(), actual.getRules().size());
        expected.getRules().sort(Comparator.comparing(RuleExpr::getName));
        actual.getRules().sort(Comparator.comparing(RuleExpr::getName));
        for (var i = 0; i < expected.getRules().size(); i++) {
            compareRules(expected.getRules().get(i), actual.getRules().get(i));
        }
    }

    private void compareRules(RuleExpr expected, RuleExpr actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getResourceDefRef(), actual.getResourceDefRef());
        for (var i = 0; i < expected.getWithExpr().getStatements().size(); i++) {
            assertEquals(expected.getWithExpr().getStatements().get(i), actual.getWithExpr().getStatements().get(i));
        }

        for (var i = 0; i < expected.getAssertExpr().getStatements().size(); i++) {
            assertEquals(expected.getAssertExpr().getStatements().get(i), actual.getAssertExpr().getStatements().get(i));
        }
    }
}
