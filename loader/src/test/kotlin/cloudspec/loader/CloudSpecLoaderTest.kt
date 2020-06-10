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
package cloudspec.loader

import arrow.core.Some
import cloudspec.lang.CloudSpec
import cloudspec.lang.GroupExpr
import cloudspec.lang.RuleExpr
import org.junit.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CloudSpecLoaderTest {
    private val cloudSpecLoader = CloudSpecLoader()

    @Test
    fun shouldLoadFullSpec() {
        val cloudSpecOriginal = CloudSpecGenerator.fullSpec()

        val cloudSpecLoadedOpt = cloudSpecLoader.load(
                ByteArrayInputStream(cloudSpecOriginal.toCloudSpecSyntax().toByteArray())
        )

        assertTrue(cloudSpecLoadedOpt is Some<CloudSpec>)
        compareSpecs(cloudSpecOriginal, cloudSpecLoadedOpt.t)
    }

    private fun compareSpecs(expected: CloudSpec, actual: CloudSpec) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.groups.size, actual.groups.size)
        val expectedS = expected.groups.sortedBy { it.name }
        val actualS = actual.groups.sortedBy { it.name }
        for (i in expectedS.indices) {
            compareGroups(expectedS[i], actualS[i])
        }
    }

    private fun compareGroups(expected: GroupExpr, actual: GroupExpr) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.rules.size, actual.rules.size)
        val expectedS = expected.rules.sortedBy { it.name }
        val actualS = actual.rules.sortedBy { it.name }
        for (i in expectedS.indices) {
            compareRules(expectedS[i], actualS[i])
        }
    }

    private fun compareRules(expected: RuleExpr, actual: RuleExpr) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.resourceDefRef, actual.resourceDefRef)
        for (i in expected.withExpr.statements.indices) {
            assertEquals(expected.withExpr.statements[i], actual.withExpr.statements[i])
        }
        for (i in expected.assertExpr.statements.indices) {
            assertEquals(expected.assertExpr.statements[i], actual.assertExpr.statements[i])
        }
    }
}
