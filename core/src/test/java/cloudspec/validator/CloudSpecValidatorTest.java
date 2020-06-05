/*-
 * #%L
 * CloudSpec Core Library
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
package cloudspec.validator;

import cloudspec.model.ResourceDefRef;
import cloudspec.util.CloudSpecTestUtils;
import cloudspec.util.ModelTestUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CloudSpecValidatorTest {
    public static final ResourceValidator resourceValidator = mock(ResourceValidator.class);
    public static final ResourceValidationResult resourceValidationResuls = new ResourceValidationResult(
            ModelTestUtils.RESOURCE_DEF_REF,
            ModelTestUtils.RESOURCE_ID,
            Collections.emptyList()
    );

    static {
        when(resourceValidator.validateAll(any(ResourceDefRef.class), any(List.class), any(List.class)))
                .thenReturn(Collections.singletonList(resourceValidationResuls));
    }

    @Test
    public void shouldValidateCloudSpec() {
        CloudSpecValidator validator = new CloudSpecValidator(resourceValidator);

        CloudSpecValidatorResult result = validator.validate(CloudSpecTestUtils.TEST_SPEC);

        assertEquals(CloudSpecTestUtils.TEST_SPEC_NAME, result.getSpecName());
        assertEquals(1, result.getGroupResults().size());

        result.getGroupResults().forEach(groupResult -> {
            assertEquals(CloudSpecTestUtils.TEST_SPEC_GROUP_NAME, groupResult.getGroupName());
            assertEquals(1, groupResult.getRuleResults().size());

            groupResult.getRuleResults().forEach(ruleResult -> {
                assertEquals(CloudSpecTestUtils.TEST_SPEC_RULE_NAME, ruleResult.getRuleName());
                assertTrue(ruleResult.isSuccess());
                assertFalse(ruleResult.getThrowable().isPresent());
            });
        });
    }
}
