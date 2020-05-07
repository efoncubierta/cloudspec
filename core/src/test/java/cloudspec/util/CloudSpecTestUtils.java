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
import cloudspec.lang.PropertyStatement;
import org.apache.tinkerpop.gremlin.process.traversal.P;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CloudSpecTestUtils {
    public static final String TEST_SPEC_NAME = "myspec";
    public static final String TEST_SPEC_GROUP_NAME = "mygroup";
    public static final String TEST_SPEC_RULE_NAME = "myrule";

    public static final List<WithExpr> TEST_WITH_EXPRS = ModelTestUtils.RESOURCE_DEF.getProperties().stream()
            .map(propertyDef -> new WithExpr(new PropertyStatement(propertyDef.getName(), P.eq(1))))
            .collect(Collectors.toList());

    public static final List<AssertExpr> TEST_ASSERT_EXPRS = ModelTestUtils.RESOURCE_DEF.getProperties().stream()
            .map(propertyDef -> new AssertExpr(new PropertyStatement(propertyDef.getName(), P.eq(1))))
            .collect(Collectors.toList());

    public static final RuleExpr TEST_RULE_EXPR = new RuleExpr(
            TEST_SPEC_RULE_NAME,
            ModelTestUtils.RESOURCE_DEF_REF.toString(),
            TEST_WITH_EXPRS.get(0),
            TEST_ASSERT_EXPRS.get(0));

    public static final GroupExpr TEST_GROUP_EXPR = new GroupExpr(
            TEST_SPEC_GROUP_NAME,
            Collections.singletonList(TEST_RULE_EXPR)
    );

    public static final CloudSpec TEST_SPEC = new CloudSpec(
            TEST_SPEC_NAME,
            Collections.singletonList(TEST_GROUP_EXPR)
    );
}
