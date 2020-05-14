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
