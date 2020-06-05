/*-
 * #%L
 * CloudSpec Loader Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
