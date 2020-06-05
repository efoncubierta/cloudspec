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
