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
package cloudspec.loader

import cloudspec.lang.GroupDecl
import cloudspec.lang.ModuleDecl
import cloudspec.lang.PlanDecl
import cloudspec.lang.RuleDecl
import org.junit.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals

class ModuleDeclLoaderTest {
    private val cloudSpecLoader = CloudSpecLoader()

    @Test
    fun shouldLoadFullSpec() {
        val planOriginal = CloudSpecGenerator.fullPlan()

        val planLoaded = cloudSpecLoader.load(
                ByteArrayInputStream(planOriginal.toCloudSpecSyntax().toByteArray())
        )

        comparePlans(planOriginal, planLoaded)
    }

    private fun comparePlans(expected: PlanDecl, actual: PlanDecl) {
        assertEquals(expected.configs, actual.configs)
        assertEquals(expected.modules.size, actual.modules.size)
        val expectedS = expected.modules.sortedBy { it.name }
        val actualS = actual.modules.sortedBy { it.name }
        for (i in expectedS.indices) {
            compareModules(expectedS[i], actualS[i])
        }
    }

    private fun compareModules(expected: ModuleDecl, actual: ModuleDecl) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.configs, actual.configs)
        assertEquals(expected.groups.size, actual.groups.size)
        val expectedS = expected.groups.sortedBy { it.name }
        val actualS = actual.groups.sortedBy { it.name }
        for (i in expectedS.indices) {
            compareGroups(expectedS[i], actualS[i])
        }
    }

    private fun compareGroups(expected: GroupDecl, actual: GroupDecl) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.configs, actual.configs)
        assertEquals(expected.rules.size, actual.rules.size)
        val expectedS = expected.rules.sortedBy { it.name }
        val actualS = actual.rules.sortedBy { it.name }
        for (i in expectedS.indices) {
            compareRules(expectedS[i], actualS[i])
        }
    }

    private fun compareRules(expected: RuleDecl, actual: RuleDecl) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.defRef, actual.defRef)
        assertEquals(expected.configs, actual.configs)
        for (i in expected.withs.statements.indices) {
            assertEquals(expected.withs.statements[i], actual.withs.statements[i])
        }
        for (i in expected.asserts.statements.indices) {
            assertEquals(expected.asserts.statements[i], actual.asserts.statements[i])
        }
    }
}
