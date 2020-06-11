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
package cloudspec.validator

import cloudspec.lang.Statement
import cloudspec.model.ResourceDefRef
import cloudspec.util.CloudSpecTestUtils
import cloudspec.util.ModelTestUtils
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CloudSpecValidatorTest {
    companion object {
        val resourceValidator = mockk<ResourceValidator>()
        val resourceValidationResults = ResourceValidationResult(ModelTestUtils.RESOURCE_REF, emptyList())

        init {
            every {
                resourceValidator.validateAll(any(), any(), any())
            } returns listOf(resourceValidationResults)
        }
    }

    @Test
    fun shouldValidateCloudSpec() {
        val validator = CloudSpecValidator(resourceValidator)
        val (specName, groupResults) = validator.validate(CloudSpecTestUtils.TEST_SPEC)

        assertEquals(CloudSpecTestUtils.TEST_SPEC_NAME, specName)
        assertEquals(1, groupResults.size)

        groupResults.forEach { (groupName, ruleResults) ->
            assertEquals(CloudSpecTestUtils.TEST_SPEC_GROUP_NAME, groupName)
            assertEquals(1, ruleResults.size)

            ruleResults.forEach { ruleResult: RuleResult ->
                assertEquals(CloudSpecTestUtils.TEST_SPEC_RULE_NAME, ruleResult.ruleName)
                assertTrue(ruleResult.isSuccess)
                assertNull(ruleResult.throwable)
            }
        }
    }
}
