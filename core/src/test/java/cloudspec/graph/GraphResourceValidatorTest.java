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

import cloudspec.lang.*;
import cloudspec.util.ModelTestUtils;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphResourceValidatorTest {
    private final Graph graph = TinkerGraph.open();
    private final GraphResourceStore store = new GraphResourceStore(graph);
    private final GraphResourceValidator validator = new GraphResourceValidator(graph);

    {
        store.addResource(ModelTestUtils.TARGET_RESOURCE);
        store.addResource(ModelTestUtils.RESOURCE);
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
                        new PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                )
        );
    }

    @Test
    public void shouldNotExistResourceByPropertyFiltering() {
        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        new PropertyStatement(
                                ModelTestUtils.PROP_STRING_NAME,
                                P.eq(ModelTestUtils.PROP_STRING_VALUE)
                        )
                )
        );
    }

    @Test
    public void shouldExistResourceByAndCombinedPropertiesFiltering() {
        Statement filterStatement = new CombinedStatement(
                LogicalOperator.AND,
                Arrays.asList(
                        new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                        new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(ModelTestUtils.PROP_INTEGER_VALUE))
                )
        );

        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        filterStatement
                )
        );
    }

    @Test
    public void shouldNotExistResourceByAndCombinedPropertiesFiltering() {
        Statement filterStatement = new CombinedStatement(
                LogicalOperator.AND,
                Arrays.asList(
                        new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                        new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(100))
                )
        );

        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        filterStatement
                )
        );
    }

    @Test
    public void shouldExistResourceByOrCombinedPropertiesFiltering() {
        Statement filterStatement = new CombinedStatement(
                LogicalOperator.OR,
                Arrays.asList(
                        new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq("unknown")),
                        new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(ModelTestUtils.PROP_INTEGER_VALUE))
                )
        );

        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        filterStatement
                )
        );
    }

    @Test
    public void shouldNotExistResourceByOrCombinedPropertiesFiltering() {
        Statement filterStatement = new CombinedStatement(
                LogicalOperator.OR,
                Arrays.asList(
                        new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq("unknown")),
                        new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(123))
                )
        );

        assertFalse(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        filterStatement
                )
        );
    }

    @Test
    public void shouldExistResourceByAssociationFiltering() {
        Statement filterStatement = new AssociationStatement(
                ModelTestUtils.ASSOC_NAME,
                new CombinedStatement(
                        LogicalOperator.AND,
                        Arrays.asList(
                                new PropertyStatement(ModelTestUtils.PROP_STRING_NAME, P.eq(ModelTestUtils.PROP_STRING_VALUE)),
                                new PropertyStatement(ModelTestUtils.PROP_INTEGER_NAME, P.eq(ModelTestUtils.PROP_INTEGER_VALUE))
                        )
                ));

        assertTrue(
                validator.existAny(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        filterStatement
                )
        );
    }
}
