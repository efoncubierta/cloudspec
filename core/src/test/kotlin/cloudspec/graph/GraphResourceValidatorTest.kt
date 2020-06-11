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

import arrow.core.Some
import cloudspec.lang.AssociationStatement
import cloudspec.lang.KeyValueStatement
import cloudspec.lang.NestedStatement
import cloudspec.lang.PropertyStatement
import cloudspec.model.ResourceRef
import cloudspec.util.ModelTestUtils
import cloudspec.validator.*
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
                ModelTestUtils.TARGET_RESOURCE_REF,
                ModelTestUtils.TARGET_PROPERTIES,
                ModelTestUtils.TARGET_ASSOCIATIONS
        )
        resourceStore.saveResource(
                ModelTestUtils.RESOURCE_REF,
                ModelTestUtils.PROPERTIES,
                ModelTestUtils.ASSOCIATIONS
        )
    }

    @Test
    fun shouldExistResourceById() {
        assertTrue(
                validator.exist(ModelTestUtils.RESOURCE.ref)
        )
    }

    @Test
    fun shouldNotExistResourceByRandomId() {
        assertFalse(
                validator.exist(
                        ResourceRef(
                                ModelTestUtils.RESOURCE_DEF_REF,
                                UUID.randomUUID().toString()
                        )
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
                        listOf(
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
                        listOf(
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
                                listOf(
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
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        PropertyStatement(
                                "unknown",
                                P.eq("zzz")
                        )
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnMemberNotFoundErrorOnKeyValuePropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        KeyValueStatement(
                                "unknown",
                                "unknown",
                                P.eq("zzz")
                        )
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnKeyNotFoundErrorOnKeyValuePropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                "unknown",
                                P.eq("zzz")
                        )
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationKeyNotFoundError)
    }

    @Test
    fun shouldReturnMemberNotFoundErrorOnNestedPropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        NestedStatement(
                                "unknown", listOf(
                                PropertyStatement(
                                        ModelTestUtils.PROP_STRING_NAME,
                                        P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                )
                        ))
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size)
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnMemberNotFoundErrorOnAssociationAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        AssociationStatement(
                                "unknown", listOf(
                                PropertyStatement(
                                        ModelTestUtils.PROP_STRING_NAME,
                                        P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                )
                        ))
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMemberNotFoundError)
    }

    @Test
    fun shouldReturnMistMatchErrorOnPropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq("zzz")
                        )
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMismatchError)
    }

    @Test
    fun shouldReturnMistMatchErrorOnNestedPropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        NestedStatement(
                                ModelTestUtils.PROP_NESTED_NAME, listOf(
                                PropertyStatement(
                                        ModelTestUtils.PROP_STRING_NAME,
                                        P.eq("zzz")
                                )
                        ))
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMismatchError)
    }

    @Test
    fun shouldReturnMisMatchErrorOnKeyValuePropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                                P.eq("zzz")
                        )
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationMismatchError)
    }

    @Test
    fun shouldReturnContainErrorOnPropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.within("zzz")
                        )
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationContainError)
    }

    @Test
    fun shouldReturnContainErrorOnKeyValuePropertyAssertion() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                emptyList(),
                listOf(
                        KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                                P.within("zzz")
                        )
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertFalse(result.isSuccess)
        assertEquals(1, result.assertResults.size.toLong())
        assertFalse(result.assertResults[0].success)
        assertNotNull(result.assertResults[0].error)
        assertTrue(result.assertResults[0].error is AssertValidationContainError)
    }

    @Test
    fun shouldSuccessAssertingPropertiesOfResourceById() {
        val resultOpt = validator.validate(
                ModelTestUtils.RESOURCE_REF,
                listOf(
                        PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                ),
                listOf(
                        NestedStatement(
                                ModelTestUtils.PROP_NESTED_NAME,
                                listOf(
                                        PropertyStatement(
                                                ModelTestUtils.PROP_STRING_NAME,
                                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                        )
                                ))
                ))
        assertTrue(resultOpt is Some<ResourceValidationResult>)

        val result = resultOpt.t
        assertTrue(result.isSuccess)
        assertEquals(ModelTestUtils.RESOURCE_REF, result.ref)
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
                ModelTestUtils.RESOURCE_DEF_REF,
                listOf(
                        PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                ),
                listOf(
                        NestedStatement(
                                ModelTestUtils.PROP_NESTED_NAME,
                                listOf(
                                        PropertyStatement(
                                                ModelTestUtils.PROP_STRING_NAME,
                                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                        )
                                ))
                ))

        assertTrue(results.isNotEmpty())
        results.forEach { r ->
            assertTrue(r.isSuccess)
            assertEquals(ModelTestUtils.RESOURCE_REF, r.ref)
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
                ModelTestUtils.RESOURCE_DEF_REF,
                listOf(
                        PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                ),
                listOf(
                        AssociationStatement(
                                ModelTestUtils.ASSOC_NAME,
                                listOf(
                                        PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE))
                                ))
                ))

        assertTrue(results.isNotEmpty())
        results.forEach(Consumer { r ->
            assertTrue(r.isSuccess)
            assertEquals(ModelTestUtils.RESOURCE_REF, r.ref)
            assertEquals(1, r.assertResults.size)
            assertTrue(r.assertResults[0].success)
            assertEquals(
                    "${ModelTestUtils.ASSOC_NAME}->${ModelTestUtils.PROP_STRING_NAME}",
                    r.assertResults[0].path.joinToString("->")
            )
        })
    }
}
