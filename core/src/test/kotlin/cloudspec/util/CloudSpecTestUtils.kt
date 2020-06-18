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
import cloudspec.model.*
import org.apache.tinkerpop.gremlin.process.traversal.P

object CloudSpecTestUtils {
    val TEST_PLAN_NAME = "plan"
    val TEST_MODULE_NAME = "myspec"
    val TEST_SPEC_GROUP_NAME = "mygroup"
    val TEST_SPEC_RULE_NAME = "myrule"

    val TEST_CONFIG_REF = ConfigRef(ProviderDataUtil.PROVIDER_NAME, "myconfig")
    val TEST_CONFIG_VALUE = StringConfigValue(TEST_CONFIG_REF, "foo")

    val TEST_CONFIG_REF_STR = TEST_CONFIG_REF.toString()
    val TEST_CONFIG_VALUE_RAW = "foo"

    val TEST_FILTERS = listOf(
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

    val TEST_WITH_DECL = WithDecl(TEST_FILTERS)

    val TEST_VALIDATIONS = listOf(
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

    val TEST_ASSERT_DECL = AssertDecl(TEST_VALIDATIONS)

    val TEST_RULE = Rule(
            TEST_SPEC_RULE_NAME,
            ModelTestUtils.RESOURCE_DEF_REF,
            TEST_FILTERS,
            TEST_VALIDATIONS,
            listOf(TEST_CONFIG_VALUE)
    )

    val TEST_RULE_DECL = RuleDecl(
            TEST_SPEC_RULE_NAME,
            ModelTestUtils.RESOURCE_DEF_REF.toString(),
            listOf(
                    SetDecl(TEST_CONFIG_REF_STR, TEST_CONFIG_VALUE_RAW)
            ),
            TEST_WITH_DECL,
            TEST_ASSERT_DECL
    )

    val TEST_USE_MODULE_DECL = UseModuleDecl("")

    val TEST_USE_GROUP_DECL = UseGroupDecl("")

    val TEST_USE_RULE_DECL = UseRuleDecl("")

    val TEST_GROUP = Group(
            TEST_SPEC_GROUP_NAME,
            listOf(TEST_RULE))

    val TEST_GROUP_DECL = GroupDecl(
            TEST_SPEC_GROUP_NAME,
            listOf(
                    SetDecl(TEST_CONFIG_REF_STR, TEST_CONFIG_VALUE_RAW)
            ),
            listOf(
                    TEST_USE_RULE_DECL
            ),
            listOf(TEST_RULE_DECL))

    val TEST_MODULE = Module(TEST_MODULE_NAME,
                             emptyList(),
                             listOf(TEST_GROUP),
                             listOf(TEST_RULE))

    val TEST_MODULE_DECL = ModuleDecl(TEST_MODULE_NAME,
                                      emptyList(),
                                      listOf(
                                              SetDecl(TEST_CONFIG_REF_STR, TEST_CONFIG_VALUE_RAW)
                                      ),
                                      listOf(
                                              TEST_USE_MODULE_DECL
                                      ),
                                      listOf(
                                              TEST_USE_GROUP_DECL
                                      ),
                                      listOf(
                                              TEST_USE_RULE_DECL
                                      ),
                                      listOf(TEST_GROUP_DECL),
                                      listOf(TEST_RULE_DECL))


    val TEST_PLAN = Plan(TEST_PLAN_NAME,
                         listOf(TEST_MODULE),
                         listOf(TEST_GROUP),
                         listOf(TEST_RULE))

    val TEST_PLAN_DECL = PlanDecl(TEST_PLAN_NAME,
                                  emptyList(),
                                  listOf(
                                          SetDecl(TEST_CONFIG_REF_STR, TEST_CONFIG_VALUE_RAW)
                                  ),
                                  listOf(
                                          TEST_USE_MODULE_DECL
                                  ),
                                  listOf(
                                          TEST_USE_GROUP_DECL
                                  ),
                                  listOf(
                                          TEST_USE_RULE_DECL
                                  ),
                                  listOf(TEST_GROUP_DECL),
                                  listOf(TEST_RULE_DECL))
}
