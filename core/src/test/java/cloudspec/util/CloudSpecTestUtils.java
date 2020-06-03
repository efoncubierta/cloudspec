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
package cloudspec.util;

import cloudspec.lang.*;
import org.apache.tinkerpop.gremlin.process.traversal.P;

import java.util.Arrays;
import java.util.Collections;

public class CloudSpecTestUtils {
    public static final String TEST_SPEC_NAME = "myspec";
    public static final String TEST_SPEC_GROUP_NAME = "mygroup";
    public static final String TEST_SPEC_RULE_NAME = "myrule";

    public static final WithExpr TEST_WITH_EXPR = new WithExpr(
            Arrays.asList(
                    new PropertyStatement(
                            ModelTestUtils.PROP_NUMBER_NAME,
                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                    ),
                    new PropertyStatement(
                            ModelTestUtils.PROP_STRING_NAME,
                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                    ),
                    new PropertyStatement(
                            ModelTestUtils.PROP_BOOLEAN_NAME,
                            P.eq(ModelTestUtils.PROP_BOOLEAN_VALUE)
                    ),
                    new KeyValueStatement(
                            ModelTestUtils.PROP_KEY_VALUE_NAME,
                            ModelTestUtils.PROP_KEY_VALUE_VALUE.getKey(),
                            P.eq(ModelTestUtils.PROP_KEY_VALUE_VALUE.getValue())
                    ),
                    new NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            Collections.singletonList(
                                    new PropertyStatement(
                                            ModelTestUtils.PROP_NUMBER_NAME,
                                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                                    )
                            )
                    ),
                    new NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            Collections.singletonList(
                                    new PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    ),
                    new NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            Collections.singletonList(
                                    new PropertyStatement(
                                            ModelTestUtils.PROP_BOOLEAN_NAME,
                                            P.eq(ModelTestUtils.PROP_BOOLEAN_VALUE)
                                    )
                            )
                    ),
                    new NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            Collections.singletonList(
                                    new AssociationStatement(
                                            ModelTestUtils.ASSOC_NAME,
                                            Collections.singletonList(
                                                    new PropertyStatement(
                                                            ModelTestUtils.PROP_STRING_NAME,
                                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                                    )
                                            )
                                    )
                            )
                    ),
                    new AssociationStatement(
                            ModelTestUtils.ASSOC_NAME,
                            Collections.singletonList(
                                    new PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    )
            )
    );

    public static final AssertExpr TEST_ASSERT_EXPR = new AssertExpr(
            Arrays.asList(
                    new PropertyStatement(
                            ModelTestUtils.PROP_NUMBER_NAME,
                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                    ),
                    new PropertyStatement(
                            ModelTestUtils.PROP_STRING_NAME,
                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                    ),
                    new PropertyStatement(
                            ModelTestUtils.PROP_BOOLEAN_NAME,
                            P.eq(ModelTestUtils.PROP_BOOLEAN_VALUE)
                    ),
                    new KeyValueStatement(
                            ModelTestUtils.PROP_KEY_VALUE_NAME,
                            ModelTestUtils.PROP_KEY_VALUE_VALUE.getKey(),
                            P.eq(ModelTestUtils.PROP_KEY_VALUE_VALUE.getValue())
                    ),
                    new NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            Collections.singletonList(
                                    new PropertyStatement(
                                            ModelTestUtils.PROP_NUMBER_NAME,
                                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                                    )
                            )
                    ),
                    new NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            Collections.singletonList(
                                    new PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    ),
                    new NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            Collections.singletonList(
                                    new AssociationStatement(
                                            ModelTestUtils.ASSOC_NAME,
                                            Collections.singletonList(
                                                    new PropertyStatement(
                                                            ModelTestUtils.PROP_STRING_NAME,
                                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                                    )
                                            )
                                    )
                            )
                    ),
                    new AssociationStatement(
                            ModelTestUtils.ASSOC_NAME,
                            Collections.singletonList(
                                    new PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    )
            )
    );

    public static final RuleExpr TEST_RULE_EXPR = new RuleExpr(
            TEST_SPEC_RULE_NAME,
            ModelTestUtils.RESOURCE_DEF_REF.toString(),
            TEST_WITH_EXPR,
            TEST_ASSERT_EXPR
    );

    public static final GroupExpr TEST_GROUP_EXPR = new GroupExpr(
            TEST_SPEC_GROUP_NAME,
            Collections.singletonList(TEST_RULE_EXPR)
    );

    public static final CloudSpec TEST_SPEC = new CloudSpec(
            TEST_SPEC_NAME,
            Collections.singletonList(TEST_GROUP_EXPR)
    );
}
