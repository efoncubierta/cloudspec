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
package cloudspec.util

import cloudspec.lang.*
import org.apache.tinkerpop.gremlin.process.traversal.P

object CloudSpecTestUtils {
    const val TEST_SPEC_NAME = "myspec"
    const val TEST_SPEC_GROUP_NAME = "mygroup"
    const val TEST_SPEC_RULE_NAME = "myrule"

    val TEST_WITH_EXPR = WithExpr(
            listOf(
                    PropertyStatement(
                            ModelTestUtils.PROP_NUMBER_NAME,
                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                    ),
                    PropertyStatement(
                            ModelTestUtils.PROP_STRING_NAME,
                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                    ),
                    PropertyStatement(
                            ModelTestUtils.PROP_BOOLEAN_NAME,
                            P.eq(ModelTestUtils.PROP_BOOLEAN_VALUE)
                    ),
                    KeyValueStatement(
                            ModelTestUtils.PROP_KEY_VALUE_NAME,
                            ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                            P.eq(ModelTestUtils.PROP_KEY_VALUE_VALUE.value)
                    ),
                    NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            listOf(
                                    PropertyStatement(
                                            ModelTestUtils.PROP_NUMBER_NAME,
                                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                                    )
                            )
                    ),
                    NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            listOf(
                                    PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    ),
                    NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            listOf(
                                    PropertyStatement(
                                            ModelTestUtils.PROP_BOOLEAN_NAME,
                                            P.eq(ModelTestUtils.PROP_BOOLEAN_VALUE)
                                    )
                            )
                    ),
                    NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            listOf(
                                    AssociationStatement(
                                            ModelTestUtils.ASSOC_NAME,
                                            listOf(
                                                    PropertyStatement(
                                                            ModelTestUtils.PROP_STRING_NAME,
                                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                                    )
                                            )
                                    )
                            )
                    ),
                    AssociationStatement(
                            ModelTestUtils.ASSOC_NAME,
                            listOf(
                                    PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    )
            )
    )
    val TEST_ASSERT_EXPR = AssertExpr(
            listOf(
                    PropertyStatement(
                            ModelTestUtils.PROP_NUMBER_NAME,
                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                    ),
                    PropertyStatement(
                            ModelTestUtils.PROP_STRING_NAME,
                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                    ),
                    PropertyStatement(
                            ModelTestUtils.PROP_BOOLEAN_NAME,
                            P.eq(ModelTestUtils.PROP_BOOLEAN_VALUE)
                    ),
                    KeyValueStatement(
                            ModelTestUtils.PROP_KEY_VALUE_NAME,
                            ModelTestUtils.PROP_KEY_VALUE_VALUE.key,
                            P.eq(ModelTestUtils.PROP_KEY_VALUE_VALUE.value)
                    ),
                    NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            listOf(
                                    PropertyStatement(
                                            ModelTestUtils.PROP_NUMBER_NAME,
                                            P.eq(ModelTestUtils.PROP_NUMBER_VALUE)
                                    )
                            )
                    ),
                    NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            listOf(
                                    PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    ),
                    NestedStatement(
                            ModelTestUtils.PROP_NESTED_NAME,
                            listOf(
                                    AssociationStatement(
                                            ModelTestUtils.ASSOC_NAME,
                                            listOf(
                                                    PropertyStatement(
                                                            ModelTestUtils.PROP_STRING_NAME,
                                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                                    )
                                            )
                                    )
                            )
                    ),
                    AssociationStatement(
                            ModelTestUtils.ASSOC_NAME,
                            listOf(
                                    PropertyStatement(
                                            ModelTestUtils.PROP_STRING_NAME,
                                            P.eq(ModelTestUtils.PROP_STRING_VALUE)
                                    )
                            )
                    )
            )
    )

    val TEST_RULE_EXPR = RuleExpr(
            TEST_SPEC_RULE_NAME,
            ModelTestUtils.RESOURCE_DEF_REF.toString(),
            TEST_WITH_EXPR,
            TEST_ASSERT_EXPR
    )
    val TEST_GROUP_EXPR = GroupExpr(
            TEST_SPEC_GROUP_NAME, listOf(TEST_RULE_EXPR))

    val TEST_SPEC = CloudSpec(TEST_SPEC_NAME, listOf(TEST_GROUP_EXPR))
}
