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
package cloudspec.graph

import cloudspec.lang.AssociationStatement
import cloudspec.lang.KeyValueStatement
import cloudspec.lang.NestedStatement
import cloudspec.lang.PropertyStatement
import cloudspec.util.ModelTestUtils
import cloudspec.validator.AssertValidationContainError
import cloudspec.validator.AssertValidationKeyNotFoundError
import cloudspec.validator.AssertValidationMemberNotFoundError
import cloudspec.validator.AssertValidationMismatchError
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GraphResourceValidatorTest {
    private val graph: Graph = TinkerGraph.open()
    private val resourceDefStore = GraphResourceDefStore(graph)
    private val resourceStore = GraphResourceStore(graph)
    private val validator = GraphResourceValidator(graph)

    @Before
    fun before() {
        graph.traversal().V().drop().iterate()
        resourceDefStore.saveResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF)
        resourceDefStore.saveResourceDef(ModelTestUtils.RESOURCE_DEF)
        resourceStore.saveResource(
                ModelTestUtils.TARGET_RESOURCE_DEF_REF,
                ModelTestUtils.TARGET_RESOURCE_ID,
                ModelTestUtils.TARGET_PROPERTIES,
                ModelTestUtils.TARGET_ASSOCIATIONS
        )
        resourceStore.saveResource(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                ModelTestUtils.PROPERTIES,
                ModelTestUtils.ASSOCIATIONS
        )
    }

    @Test
    fun shouldExistResourceById() {
        assertTrue(
                validator.existById(
                        ModelTestUtils.RESOURCE.resourceDefRef,
                        ModelTestUtils.RESOURCE.resourceId
                )
        )
    }

    @Test
    fun shouldNotExistResourceByRandomId() {
        assertFalse(
                validator.existById(
                        ModelTestUtils.RESOURCE.resourceDefRef,
                        UUID.randomUUID().toString()
                )
        )
    }

    @Test
    fun shouldExistResourceByProperty() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF, listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                ))
        )
    }

    @Test
    fun shouldNotExistResourceByProperty() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF, listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq("zzz")
                        )
                ))
        )
    }

    @Test
    fun shouldExistResourceByKeyValuePropertyFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF, listOf(
                        KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                                P.eq(ModelTestUtils.PROP_KEY_VALUE_VALUE.value)
                        )
                ))
        )
    }

    @Test
    fun shouldNotExistResourceByKeyValuePropertyFiltering() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF, listOf(
                        KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                                P.eq("zzz")
                        )
                ))
        )
    }

    @Test
    fun shouldNotExistResourceByNestedPropertyFiltering() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF, listOf(
                        NestedStatement(
                                ModelTestUtils.PROP_NESTED_NAME, listOf(
                                PropertyStatement(
                                        ModelTestUtils.PROP_STRING_NAME,
                                        P.eq("zzz")
                                )
                        ))
                ))
        )
    }

    @Test
    fun shouldExistResourceByMultiplePropertiesFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Arrays.asList(
                                PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                        )
                )
        )
    }

    @Test
    fun shouldNotExistResourceByMultiplePropertiesFiltering() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Arrays.asList(
                                PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(100))
                        )
                )
        )
    }

    @Test
    fun shouldExistResourceByAssociationFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF, listOf(
                        AssociationStatement(
                                ModelTestUtils.ASSOC_NAME,
                                Arrays.asList(
                                        PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                        PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                                ))
                ))
        )
    }

    @Test
    fun shouldExistResourceByPropertyFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF, listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                ))
        )
    }

    @Test
    fun shouldReturnMemberNotFoundErrorOnPropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                PropertyStatement(
                        "unknown",
                        P.eq("zzz")
                )
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnMemberNotFoundErrorOnKeyValuePropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                KeyValueStatement(
                        "unknown",
                        "unknown",
                        P.eq("zzz")
                )
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnKeyNotFoundErrorOnKeyValuePropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                KeyValueStatement(
                        ModelTestUtils.PROP_KEY_VALUE_NAME,
                        "unknown",
                        P.eq("zzz")
                )
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationKeyNotFoundError)
    }

    @Test
    fun shouldReturnMemberNotFoundErrorOnNestedPropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                NestedStatement(
                        "unknown", listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                ))
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnMemberNotFoundErrorOnAssociationAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                AssociationStatement(
                        "unknown", listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                ))
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnMistMatchErrorOnPropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                PropertyStatement(
                        ModelTestUtils.PROP_STRING_NAME,
                        P.eq("zzz")
                )
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMismatchError)
    }

    @Test
    fun shouldReturnMistMatchErrorOnNestedPropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                NestedStatement(
                        ModelTestUtils.PROP_NESTED_NAME, listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq("zzz")
                        )
                ))
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMismatchError)
    }

    @Test
    fun shouldReturnMisMatchErrorOnKeyValuePropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                KeyValueStatement(
                        ModelTestUtils.PROP_KEY_VALUE_NAME,
                        ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                        P.eq("zzz")
                )
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMismatchError)
    }

    @Test
    fun shouldReturnContainErrorOnPropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                PropertyStatement(
                        ModelTestUtils.PROP_STRING_NAME,
                        P.within("zzz")
                )
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationContainError)
    }

    @Test
    fun shouldReturnContainErrorOnKeyValuePropertyAssertion() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, emptyList(), listOf(
                KeyValueStatement(
                        ModelTestUtils.PROP_KEY_VALUE_NAME,
                        ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                        P.within("zzz")
                )
        ))
        assertNotNull(result)
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationContainError)
    }

    @Test
    fun shouldSuccessAssertingPropertiesOfResourceById() {
        val result = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID, listOf(
                PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
        ), listOf(
                NestedStatement(
                        ModelTestUtils.PROP_NESTED_NAME, listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                ))
        ))
        assertNotNull(result)
        assertTrue(result.isSuccess)
        assertEquals(ModelTestUtils.RESOURCE_DEF_REF, result.resourceDefRef)
        assertNotNull(result.resourceId)
        assertEquals(1, result.assertResults.size)
        assertTrue(result.assertResults[0].success)
        assertEquals(
                "${ModelTestUtils.PROP_NESTED_NAME}->${ModelTestUtils.PROP_STRING_NAME}",
                result.assertResults[0].path.joinToString("->")
        )
    }

    @Test
    fun shouldSuccessAssertingProperties() {
        val results = validator.validateAll(
                ModelTestUtils.RESOURCE_DEF_REF, listOf(
                PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
        ), listOf(
                NestedStatement(
                        ModelTestUtils.PROP_NESTED_NAME, listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                ))
        ))
        assertNotNull(results)
        assertTrue(results.isNotEmpty())
        results.forEach { r ->
            assertTrue(r.isSuccess)
            assertEquals(ModelTestUtils.RESOURCE_DEF_REF, r.resourceDefRef)
            assertNotNull(r.resourceId)
            assertEquals(1, r.assertResults.size)
            assertTrue(r.assertResults[0].success)
            assertEquals(
                    "${ModelTestUtils.PROP_NESTED_NAME}->${ModelTestUtils.PROP_STRING_NAME}",
                    r.assertResults[0].path.joinToString("->")
            )
        }
    }

    @Test
    fun shouldSuccessAssertingAssociations() {
        val results = validator.validateAll(
                ModelTestUtils.RESOURCE_DEF_REF, listOf(
                PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
        ), listOf(
                AssociationStatement(
                        ModelTestUtils.ASSOC_NAME, listOf(
                        PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE))
                ))
        ))
        assertNotNull(results)
        assertTrue(results.isNotEmpty())
        results.forEach(Consumer { r ->
            assertTrue(r.isSuccess)
            assertEquals(ModelTestUtils.RESOURCE_DEF_REF, r.resourceDefRef)
            assertNotNull(r.resourceId)
            assertEquals(1, r.assertResults.size)
            assertTrue(r.assertResults[0].success)
            assertEquals(
                    "${ModelTestUtils.ASSOC_NAME}->${ModelTestUtils.PROP_STRING_NAME}",
                    r.assertResults[0].path.joinToString("->")
            )
        })
    }
}
