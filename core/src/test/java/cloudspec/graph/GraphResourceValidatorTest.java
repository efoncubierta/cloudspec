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
package cloudspec.graph;

import cloudspec.lang.AssociationStatement;
import cloudspec.lang.PropertyStatement;
import cloudspec.util.ModelTestUtils;
import cloudspec.validator.ResourceValidationResult;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class GraphResourceValidatorTest {
    private final Graph graph = TinkerGraph.open();
    private final GraphResourceDefStore resourceDefStore = new GraphResourceDefStore(graph);
    private final GraphResourceStore resourceStore = new GraphResourceStore(graph);
    private final GraphResourceValidator validator = new GraphResourceValidator(graph);

    {
        resourceDefStore.addResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF);
        resourceDefStore.addResourceDef(ModelTestUtils.RESOURCE_DEF);
        resourceStore.addResource(ModelTestUtils.TARGET_RESOURCE);
        resourceStore.addResource(ModelTestUtils.RESOURCE);
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
    public void shouldNotExistResourceByPropertyFiltering() {
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
    public void shouldExistResourceByMultiplePropertiesFiltering() {

        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        Arrays.asList(
                                new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(ModelTestUtils.PROP_INTEGER_VALUE))
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
                                new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(100))
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
                                                new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(ModelTestUtils.PROP_INTEGER_VALUE))
                                        ))
                        )
                )
        );
    }

    @Test
    public void shouldSuccessAssertingProperties() {
        List<ResourceValidationResult> results = validator.validateAll(
                ModelTestUtils.RESOURCE_DEF_REF,
                Collections.singletonList(
                        new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(ModelTestUtils.PROP_INTEGER_VALUE))
                ),
                Collections.singletonList(
                        new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE))
                )
        );

        assertNotNull(results);
        assertTrue(results.size() > 0);

        results.forEach(result -> {
            System.out.println(result);
            assertTrue(result.isSuccess());
            assertEquals(ModelTestUtils.RESOURCE_DEF_REF, result.getResourceDefRef());
            assertNotNull(result.getResourceId());
            assertEquals(1, result.getAssertResults().size());
            assertTrue(result.getAssertResults().get(0).isSuccess());
            assertEquals(
                    ModelTestUtils.PROP_STRING_NAME,
                    String.join("->", result.getAssertResults().get(0).getPath())
            );
        });
    }

    @Test
    public void shouldSuccessAssertingAssociations() {
        List<ResourceValidationResult> results = validator.validateAll(
                ModelTestUtils.RESOURCE_DEF_REF,
                Collections.singletonList(
                        new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(ModelTestUtils.PROP_INTEGER_VALUE))
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
