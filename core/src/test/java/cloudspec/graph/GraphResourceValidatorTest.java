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
package cloudspec.graph;

import cloudspec.lang.AssociationStatement;
import cloudspec.lang.KeyValueStatement;
import cloudspec.lang.NestedStatement;
import cloudspec.lang.PropertyStatement;
import cloudspec.util.ModelTestUtils;
import cloudspec.validator.*;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GraphResourceValidatorTest {
    private final Graph graph = TinkerGraph.open();
    private final GraphResourceDefStore resourceDefStore = new GraphResourceDefStore(graph);
    private final GraphResourceStore resourceStore = new GraphResourceStore(graph);
    private final GraphResourceValidator validator = new GraphResourceValidator(graph);

    @Before
    public void before() {
        graph.traversal().V().drop().iterate();

        resourceDefStore.createResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF);
        resourceDefStore.createResourceDef(ModelTestUtils.RESOURCE_DEF);
        resourceStore.saveResource(
                ModelTestUtils.TARGET_RESOURCE_DEF_REF,
                ModelTestUtils.TARGET_RESOURCE_ID,
                ModelTestUtils.TARGET_PROPERTIES,
                ModelTestUtils.TARGET_ASSOCIATIONS
        );
        resourceStore.saveResource(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                ModelTestUtils.PROPERTIES,
                ModelTestUtils.ASSOCIATIONS
        );
    }

    @Test
    public void shouldExistResourceById() {
        assertTrue(
                validator.existById(
                        ModelTestUtils.RESOURCE.getResourceDefRef(),
                        ModelTestUtils.RESOURCE.getResourceId()
                )
        );
    }

    @Test
    public void shouldNotExistResourceByRandomId() {
        assertFalse(
                validator.existById(
                        ModelTestUtils.RESOURCE.getResourceDefRef(),
                        UUID.randomUUID().toString()
                )
        );
    }

    @Test
    public void shouldExistResourceByProperty() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Collections.singletonList(
                                new PropertyStatement(
                                        ModelTestUtils.PROP_STRING_NAME,
                                        P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                )
                        )
                )
        );
    }

    @Test
    public void shouldNotExistResourceByProperty() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Collections.singletonList(
                                new PropertyStatement(
                                        ModelTestUtils.PROP_STRING_NAME,
                                        P.eq("zzz")
                                )
                        )
                )
        );
    }

    @Test
    public void shouldExistResourceByKeyValuePropertyFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Collections.singletonList(
                                new KeyValueStatement(
                                        ModelTestUtils.PROP_KEY_VALUE_NAME,
                                        ModelTestUtils.PROP_KEY_VALUE_VALUE.getKey(),
                                        P.eq(ModelTestUtils.PROP_KEY_VALUE_VALUE.getValue())
                                )
                        )
                )
        );
    }

    @Test
    public void shouldNotExistResourceByKeyValuePropertyFiltering() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Collections.singletonList(
                                new KeyValueStatement(
                                        ModelTestUtils.PROP_KEY_VALUE_NAME,
                                        ModelTestUtils.PROP_KEY_VALUE_VALUE.getKey(),
                                        P.eq("zzz")
                                )
                        )
                )
        );
    }

    @Test
    public void shouldNotExistResourceByNestedPropertyFiltering() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Collections.singletonList(
                                new NestedStatement(
                                        ModelTestUtils.PROP_NESTED_NAME,
                                        Collections.singletonList(
                                                new PropertyStatement(
                                                        ModelTestUtils.PROP_STRING_NAME,
                                                        P.eq("zzz")
                                                )
                                        )
                                )
                        )
                )
        );
    }

    @Test
    public void shouldExistResourceByMultiplePropertiesFiltering() {

        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Arrays.asList(
                                new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                new PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                        )
                )
        );
    }

    @Test
    public void shouldNotExistResourceByMultiplePropertiesFiltering() {
        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Arrays.asList(
                                new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                new PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(100))
                        )
                )
        );
    }

    @Test
    public void shouldExistResourceByAssociationFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Collections.singletonList(
                                new AssociationStatement(
                                        ModelTestUtils.ASSOC_NAME,
                                        Arrays.asList(
                                                new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                                new PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                                        ))
                        )
                )
        );
    }

    @Test
    public void shouldExistResourceByPropertyFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Collections.singletonList(
                                new PropertyStatement(
                                        ModelTestUtils.PROP_STRING_NAME,
                                        P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                )
                        )
                )
        );
    }

    @Test
    public void shouldReturnMemberNotFoundErrorOnPropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new PropertyStatement(
                                "unknown",
                                P.eq("zzz")
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationMemberNotFoundError);
        });
    }

    @Test
    public void shouldReturnMemberNotFoundErrorOnKeyValuePropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new KeyValueStatement(
                                "unknown",
                                "unknown",
                                P.eq("zzz")
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationMemberNotFoundError);
        });
    }

    @Test
    public void shouldReturnKeyNotFoundErrorOnKeyValuePropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                "unknown",
                                P.eq("zzz")
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationKeyNotFoundError);
        });
    }

    @Test
    public void shouldReturnMemberNotFoundErrorOnNestedPropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new NestedStatement(
                                "unknown",
                                Collections.singletonList(
                                        new PropertyStatement(
                                                ModelTestUtils.PROP_STRING_NAME,
                                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                        )
                                )
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationMemberNotFoundError);
        });
    }

    @Test
    public void shouldReturnMemberNotFoundErrorOnAssociationAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new AssociationStatement(
                                "unknown",
                                Collections.singletonList(
                                        new PropertyStatement(
                                                ModelTestUtils.PROP_STRING_NAME,
                                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                        )
                                )
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationMemberNotFoundError);
        });
    }

    @Test
    public void shouldReturnMistMatchErrorOnPropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq("zzz")
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationMismatchError);
        });
    }

    @Test
    public void shouldReturnMistMatchErrorOnNestedPropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new NestedStatement(
                                ModelTestUtils.PROP_NESTED_NAME,
                                Collections.singletonList(
                                        new PropertyStatement(
                                                ModelTestUtils.PROP_STRING_NAME,
                                                P.eq("zzz")
                                        )
                                )
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationMismatchError);
        });
    }

    @Test
    public void shouldReturnMisMatchErrorOnKeyValuePropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                ModelTestUtils.PROP_KEY_VALUE_VALUE.getKey(),
                                P.eq("zzz")
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationMismatchError);
        });
    }

    @Test
    public void shouldReturnContainErrorOnPropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.within("zzz")
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationContainError);
        });
    }

    @Test
    public void shouldReturnContainErrorOnKeyValuePropertyAssertion() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.emptyList(),
                Collections.singletonList(
                        new KeyValueStatement(
                                ModelTestUtils.PROP_KEY_VALUE_NAME,
                                ModelTestUtils.PROP_KEY_VALUE_VALUE.getKey(),
                                P.within("zzz")
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertFalse(result.isSuccess());
            assertEquals(1, result.getAssertResults().size());
            assertFalse(result.getAssertResults().get(0).getSuccess());
            assertTrue(result.getAssertResults().get(0).getError().isPresent());
            assertTrue(result.getAssertResults().get(0).getError().get() instanceof AssertValidationContainError);
        });
    }

    @Test
    public void shouldSuccessAssertingPropertiesOfResourceById() {
        Optional<ResourceValidationResult> resultOpt = validator.validateById(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                Collections.singletonList(
                        new PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                ),
                Collections.singletonList(
                        new NestedStatement(
                                ModelTestUtils.PROP_NESTED_NAME,
                                Collections.singletonList(
                                        new PropertyStatement(
                                                ModelTestUtils.PROP_STRING_NAME,
                                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                        )
                                )
                        )
                )
        );

        assertNotNull(resultOpt);
        assertTrue(resultOpt.isPresent());

        resultOpt.ifPresent(result -> {
            assertTrue(result.isSuccess());
            assertEquals(ModelTestUtils.RESOURCE_DEF_REF, result.getResourceDefRef());
            assertNotNull(result.getResourceId());
            assertEquals(1, result.getAssertResults().size());
            assertTrue(result.getAssertResults().get(0).isSuccess());
            assertEquals(
                    String.format("%s->%s", ModelTestUtils.PROP_NESTED_NAME, ModelTestUtils.PROP_STRING_NAME),
                    String.join("->", result.getAssertResults().get(0).getPath())
            );
        });
    }

    @Test
    public void shouldSuccessAssertingProperties() {
        List<ResourceValidationResult> results = validator.validateAll(
                ModelTestUtils.RESOURCE_DEF_REF,
                Collections.singletonList(
                        new PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                ),
                Collections.singletonList(
                        new NestedStatement(
                                ModelTestUtils.PROP_NESTED_NAME,
                                Collections.singletonList(
                                        new PropertyStatement(
                                                ModelTestUtils.PROP_STRING_NAME,
                                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                        )
                                )
                        )
                )
        );

        assertNotNull(results);
        assertTrue(results.size() > 0);

        results.forEach(result -> {
            assertTrue(result.isSuccess());
            assertEquals(ModelTestUtils.RESOURCE_DEF_REF, result.getResourceDefRef());
            assertNotNull(result.getResourceId());
            assertEquals(1, result.getAssertResults().size());
            assertTrue(result.getAssertResults().get(0).isSuccess());
            assertEquals(
                    String.format("%s->%s", ModelTestUtils.PROP_NESTED_NAME, ModelTestUtils.PROP_STRING_NAME),
                    String.join("->", result.getAssertResults().get(0).getPath())
            );
        });
    }

    @Test
    public void shouldSuccessAssertingAssociations() {
        List<ResourceValidationResult> results = validator.validateAll(
                ModelTestUtils.RESOURCE_DEF_REF,
                Collections.singletonList(
                        new PropertyStatement(ModelTestUtils.PROP_NUMBER_NAME, P.eq(ModelTestUtils.PROP_NUMBER_VALUE))
                ),
                Collections.singletonList(
                        new AssociationStatement(
                                ModelTestUtils.ASSOC_NAME,
                                Collections.singletonList(
                                        new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE))
                                )
                        )
                )
        );

        assertNotNull(results);
        assertTrue(results.size() > 0);

        results.forEach(result -> {
            assertTrue(result.isSuccess());
            assertEquals(ModelTestUtils.RESOURCE_DEF_REF, result.getResourceDefRef());
            assertNotNull(result.getResourceId());
            assertEquals(1, result.getAssertResults().size());
            assertTrue(result.getAssertResults().get(0).isSuccess());
            assertEquals(
                    String.format("%s->%s", ModelTestUtils.ASSOC_NAME, ModelTestUtils.PROP_STRING_NAME),
                    String.join("->", result.getAssertResults().get(0).getPath())
            );
        });
    }
}
