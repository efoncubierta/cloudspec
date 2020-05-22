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
package cloudspec.loader;

import cloudspec.CloudSpecBaseListener;
import cloudspec.CloudSpecParser;
import cloudspec.lang.*;
import org.apache.tinkerpop.gremlin.process.traversal.P;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class CloudSpecLoaderListener extends CloudSpecBaseListener {
    private static List<String> BOOLEAN_TRUE_VALUES = Arrays.asList("true", "enabled");
    // private static List<String> BOOLEAN_FALSE_VALUES = Arrays.asList("false", "disabled");

    // current spec
    private CloudSpec.CloudSpecBuilder currentCloudSpecBuilder;

    // current group
    private GroupExpr.GroupExprBuilder currentGroupBuilder;

    // current rule
    private RuleExpr.RuleExprBuilder currentRuleBuilder;

    // current width
    private WithExpr.WithExprBuilder currentWithBuilder;

    // current assert
    private AssertExpr.AssertExprBuilder currentAssertBuilder;

    // evaluator
    private Stack<Stack<String>> currentMemberNames = new Stack<>();
    private Stack<Stack<Statement>> currentStatements = new Stack<>();
    private Stack<Object> currentValues = new Stack<>();
    private P<?> currentPredicate;

    public CloudSpec getCloudSpec() {
        return currentCloudSpecBuilder.build();
    }

    @Override
    public void enterSpecDecl(CloudSpecParser.SpecDeclContext ctx) {
        currentCloudSpecBuilder = CloudSpec.builder().setName(stripQuotes(ctx.STRING().getText()));
    }

    @Override
    public void enterGroupDecl(CloudSpecParser.GroupDeclContext ctx) {
        currentGroupBuilder = GroupExpr.builder().setName(stripQuotes(ctx.STRING().getText()));
    }

    @Override
    public void exitGroupDecl(CloudSpecParser.GroupDeclContext ctx) {
        currentCloudSpecBuilder.addGroups(currentGroupBuilder.build());
    }

    @Override
    public void enterRuleDecl(CloudSpecParser.RuleDeclContext ctx) {
        currentRuleBuilder = RuleExpr.builder().setName(stripQuotes(ctx.STRING().getText()));
    }

    @Override
    public void exitRuleDecl(CloudSpecParser.RuleDeclContext ctx) {
        currentGroupBuilder.addRules(currentRuleBuilder.build());
    }

    @Override
    public void enterOnDecl(CloudSpecParser.OnDeclContext ctx) {
        currentRuleBuilder.setResourceDefRef(ctx.RESOURCE_DEF_REF().getText());
    }

    @Override
    public void enterWithDecl(CloudSpecParser.WithDeclContext ctx) {
        currentWithBuilder = WithExpr.builder();
        currentStatements.push(new Stack<>());
        currentMemberNames.push(new Stack<>());
    }

    @Override
    public void exitWithDecl(CloudSpecParser.WithDeclContext ctx) {
        currentWithBuilder.setStatements(currentStatements.pop());
        currentRuleBuilder.setWithExpr(currentWithBuilder.build());
        currentMemberNames.pop();
    }

    @Override
    public void enterAssertDecl(CloudSpecParser.AssertDeclContext ctx) {
        currentAssertBuilder = AssertExpr.builder();
        currentStatements.push(new Stack<>());
        currentMemberNames.push(new Stack<>());
    }

    @Override
    public void exitAssertDecl(CloudSpecParser.AssertDeclContext ctx) {
        currentAssertBuilder.setStatement(currentStatements.pop());
        currentRuleBuilder.setAssertExp(currentAssertBuilder.build());
        currentMemberNames.pop();
    }

    @Override
    public void enterAndDecl(CloudSpecParser.AndDeclContext ctx) {
        currentMemberNames.push(new Stack<>());
    }

    @Override
    public void exitAndDecl(CloudSpecParser.AndDeclContext ctx) {
        currentMemberNames.pop();
    }

    @Override
    public void enterPropertyStatement(CloudSpecParser.PropertyStatementContext ctx) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().getText()));
    }

    @Override
    public void exitPropertyStatement(CloudSpecParser.PropertyStatementContext ctx) {
        currentStatements.peek().add(
                new PropertyStatement(
                        currentMemberNames.peek().pop(),
                        currentPredicate
                )
        );
    }

    @Override
    public void enterKeyValuePropertyStatement(CloudSpecParser.KeyValuePropertyStatementContext ctx) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().getText()));
    }

    @Override
    public void exitKeyValuePropertyStatement(CloudSpecParser.KeyValuePropertyStatementContext ctx) {
        currentStatements.peek().add(
                new KeyValueStatement(
                        currentMemberNames.peek().pop(),
                        stripQuotes(ctx.STRING().getText()),
                        currentPredicate
                )
        );
    }

    @Override
    public void enterNestedPropertyStatement(CloudSpecParser.NestedPropertyStatementContext ctx) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().getText()));
        currentMemberNames.push(new Stack<>());
        currentStatements.push(new Stack<>());
    }

    @Override
    public void exitNestedPropertyStatement(CloudSpecParser.NestedPropertyStatementContext ctx) {
        currentMemberNames.pop();

        NestedStatement statement = new NestedStatement(
                currentMemberNames.peek().pop(),
                currentStatements.pop()
        );

        currentStatements.peek().add(statement);
    }

    @Override
    public void enterAssociationStatement(CloudSpecParser.AssociationStatementContext ctx) {
        currentMemberNames.peek().add(stripQuotes(ctx.MEMBER_NAME().getText()));
        currentMemberNames.push(new Stack<>());
        currentStatements.push(new Stack<>());
    }

    @Override
    public void exitAssociationStatement(CloudSpecParser.AssociationStatementContext ctx) {
        currentMemberNames.pop();

        AssociationStatement statement = new AssociationStatement(
                currentMemberNames.peek().pop(),
                currentStatements.pop()
        );

        currentStatements.peek().add(statement);
    }

    @Override
    public void exitPropertyEqualPredicate(CloudSpecParser.PropertyEqualPredicateContext ctx) {
        currentPredicate = P.eq(currentValues.pop());
    }

    @Override
    public void exitPropertyNotEqualPredicate(CloudSpecParser.PropertyNotEqualPredicateContext ctx) {
        currentPredicate = P.neq(currentValues.pop());
    }

    @Override
    public void exitPropertyWithinPredicate(CloudSpecParser.PropertyWithinPredicateContext ctx) {
        currentPredicate = P.within(currentValues.toArray());
        currentValues.clear();
    }

    @Override
    public void exitPropertyNotWithinPredicate(CloudSpecParser.PropertyNotWithinPredicateContext ctx) {
        currentPredicate = P.without(currentValues.toArray());
        currentValues.clear();
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

    @Override
    public void exitDoubleValue(CloudSpecParser.DoubleValueContext ctx) {
        currentValues.add(Double.parseDouble(ctx.DOUBLE().getText()));
    }

    private String stripQuotes(String s) {
        if (s == null || s.charAt(0) != '"') return s;
        return s.substring(1, s.length() - 1);
    }
}
