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
import cloudspec.model.ConfigRef
import cloudspec.model.Module
import cloudspec.model.Rule
import cloudspec.model.StringSetValue
import org.apache.tinkerpop.gremlin.process.traversal.P

object CloudSpecTestUtils {
    val TEST_PLAN_NAME = "plan"
    val TEST_MODULE_NAME = "myspec"
    val TEST_SPEC_GROUP_NAME = "mygroup"
    val TEST_SPEC_RULE_NAME = "myrule"

    val TEST_CONFIG_REF = ConfigRef(ProviderDataUtil.PROVIDER_NAME, "myconfig")
    val TEST_CONFIG_VALUE = StringSetValue(TEST_CONFIG_REF, "foo")

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

    val TEST_USE_DECL = UseDecl("", "")

    val TEST_MODULE = Module(TEST_MODULE_NAME,
                             emptyList(),
                             listOf(TEST_RULE))

    val TEST_MODULE_DECL = ModuleDecl(emptyList(),
                                      listOf(
                                              SetDecl(TEST_CONFIG_REF_STR, TEST_CONFIG_VALUE_RAW)
                                      ),
                                      listOf(
                                              TEST_USE_DECL
                                      ),
                                      listOf(TEST_RULE_DECL))
}
