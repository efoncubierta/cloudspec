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

import cloudspec.util.CloudSpecTestUtils
import cloudspec.util.ModelTestUtils
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ModuleDeclValidatorTest {
    companion object {
        val resourceValidator = mockk<ResourceValidator>()
        val resourceValidationResults = ResourceResult(ModelTestUtils.RESOURCE_REF, emptyList())

        init {
            every {
                resourceValidator.validateAll(any(), any(), any())
            } returns listOf(resourceValidationResults)
        }
    }

    @Test
    fun shouldValidatePlan() {
        val validator = CloudSpecValidator(resourceValidator)
        val result = validator.validate(CloudSpecTestUtils.TEST_MODULE)
        validateResult(result)
    }

    private fun validateResult(result: Result) {
        when (result) {
            is ModuleResult -> validateResult(result)
            is RuleResult -> validateResult(result)
        }
    }

    private fun validateResult(moduleResult: ModuleResult) {
        assertEquals(CloudSpecTestUtils.TEST_MODULE_NAME, moduleResult.name)
        assertEquals(1, moduleResult.results.size)

        moduleResult.results.forEach { validateResult(it) }
    }

    private fun validateResult(ruleResult: RuleResult) {
        assertEquals(CloudSpecTestUtils.TEST_SPEC_RULE_NAME, ruleResult.name)
        assertTrue(ruleResult.success)
        assertNull(ruleResult.error)
    }
}
