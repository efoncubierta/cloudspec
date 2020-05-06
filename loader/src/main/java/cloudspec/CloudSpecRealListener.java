/*-
 * #%L
 * CloudSpec Loader Library
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
package cloudspec;

import cloudspec.lang.*;
import cloudspec.lang.predicate.EqualPredicate;
import cloudspec.lang.predicate.NotPredicate;
import cloudspec.lang.predicate.Predicate;
import cloudspec.lang.predicate.WithinPredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CloudSpecRealListener extends CloudSpecBaseListener {
    private static List<String> BOOLEAN_TRUE_VALUES = Arrays.asList("true", "enabled");
    // private static List<String> BOOLEAN_FALSE_VALUES = Arrays.asList("false", "disabled");

    // current cloud spec
    private CloudSpec currentCloudSpec;

    // current spec
    private String currentSpecName;
    private List<GroupExpr> currentSpecGroups;

    // current group
    private String currentGroupName;
    private List<RuleExpr> currentGroupRules;

    // current rule
    private String currentRuleName;
    private String currentRuleResourceTypeFqn;
    private List<WithExpr> currentRuleWithExprs;
    private List<AssertExpr> currentRuleAssertExprs;

    // current width
    private String currentWithPropertyName;

    // current assert
    private String currentAssertPropertyName;

    // evaluator
    private Predicate currentPredicate;
    private List<Object> currentValues;

    public CloudSpec getCloudSpec() {
        return currentCloudSpec;
    }

    @Override
    public void enterSpecDecl(CloudSpecParser.SpecDeclContext ctx) {
        currentSpecName = stripQuotes(ctx.STRING().getText());
        currentSpecGroups = new ArrayList<GroupExpr>();
    }

    @Override
    public void exitSpecDecl(CloudSpecParser.SpecDeclContext ctx) {
        currentCloudSpec = new CloudSpec(currentSpecName, currentSpecGroups);
    }

    @Override
    public void enterGroupDecl(CloudSpecParser.GroupDeclContext ctx) {
        currentGroupName = stripQuotes(ctx.STRING().getText());
        currentGroupRules = new ArrayList<RuleExpr>();
    }

    @Override
    public void exitGroupDecl(CloudSpecParser.GroupDeclContext ctx) {
        currentSpecGroups.add(new GroupExpr(currentGroupName, currentGroupRules));
    }

    @Override
    public void enterRuleDecl(CloudSpecParser.RuleDeclContext ctx) {
        currentRuleName = stripQuotes(ctx.STRING().getText());
        currentRuleWithExprs = new ArrayList<>();
        currentRuleAssertExprs = new ArrayList<>();
    }

    @Override
    public void exitRuleDecl(CloudSpecParser.RuleDeclContext ctx) {
        currentGroupRules.add(new RuleExpr(currentRuleName, currentRuleResourceTypeFqn, currentRuleWithExprs, currentRuleAssertExprs));
    }

    @Override
    public void enterOnDecl(CloudSpecParser.OnDeclContext ctx) {
        currentRuleResourceTypeFqn = ctx.RESOURCE_TYPE_FQN().getText();
    }

    @Override
    public void enterWithDecl(CloudSpecParser.WithDeclContext ctx) {
        currentWithPropertyName = stripQuotes(ctx.PROPERTY_NAME().getText());
        currentPredicate = null;
        currentValues = new ArrayList<Object>();
    }

    @Override
    public void exitWithDecl(CloudSpecParser.WithDeclContext ctx) {
        currentRuleWithExprs.add(new WithExpr(currentWithPropertyName, currentPredicate));
    }

    @Override
    public void enterAssertDecl(CloudSpecParser.AssertDeclContext ctx) {
        currentAssertPropertyName = stripQuotes(ctx.PROPERTY_NAME().getText());
        currentPredicate = null;
        currentValues = new ArrayList<>();
    }

    @Override
    public void exitAssertDecl(CloudSpecParser.AssertDeclContext ctx) {
        currentRuleAssertExprs.add(new AssertExpr(currentAssertPropertyName, currentPredicate));
    }

    @Override
    public void exitWithEqualPredicate(CloudSpecParser.WithEqualPredicateContext ctx) {
        currentPredicate = new EqualPredicate<>(currentValues.get(0));
    }

    @Override
    public void exitWithNotEqualPredicate(CloudSpecParser.WithNotEqualPredicateContext ctx) {
        currentPredicate = new NotPredicate(new EqualPredicate<>(currentValues.get(0)));
    }

    @Override
    public void exitWithWithinPredicate(CloudSpecParser.WithWithinPredicateContext ctx) {
        currentPredicate = new WithinPredicate<>(currentValues);
    }

    @Override
    public void exitWithNotWithinPredicate(CloudSpecParser.WithNotWithinPredicateContext ctx) {
        currentPredicate = new NotPredicate(new WithinPredicate<>(currentValues));
    }

    @Override
    public void exitAssertEqualPredicate(CloudSpecParser.AssertEqualPredicateContext ctx) {
        currentPredicate = new EqualPredicate<>(currentValues.get(0));
    }

    @Override
    public void exitAssertNotEqualPredicate(CloudSpecParser.AssertNotEqualPredicateContext ctx) {
        currentPredicate = new NotPredicate(new EqualPredicate<>(currentValues.get(0)));
    }

    @Override
    public void exitAssertWithinPredicate(CloudSpecParser.AssertWithinPredicateContext ctx) {
        currentPredicate = new WithinPredicate<>(currentValues);
    }

    @Override
    public void exitAssertNotWithinPredicate(CloudSpecParser.AssertNotWithinPredicateContext ctx) {
        currentPredicate = new NotPredicate(new WithinPredicate<>(currentValues));
    }

    @Override
    public void exitStringValue(CloudSpecParser.StringValueContext ctx) {
        currentValues.add(stripQuotes(ctx.STRING().getText()));
    }

    @Override
    public void exitBooleanValue(CloudSpecParser.BooleanValueContext ctx) {
        currentValues.add(BOOLEAN_TRUE_VALUES.contains(ctx.BOOLEAN().getText().toLowerCase()));
    }

    @Override
    public void exitIntegerValue(CloudSpecParser.IntegerValueContext ctx) {
        currentValues.add(Integer.parseInt(ctx.INTEGER().getText()));
    }

    private String stripQuotes(String s) {
        if (s == null || s.charAt(0) != '"') return s;
        return s.substring(1, s.length() - 1);
    }
}
