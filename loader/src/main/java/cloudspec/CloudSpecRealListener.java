package cloudspec;

import cloudspec.evaluator.EqualExprEvaluator;
import cloudspec.evaluator.ExprEvaluator;
import cloudspec.evaluator.InExprEvaluator;
import cloudspec.spec.*;

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
    private String currentRuleTitle;
    private String currentRuleResourceTypeFQName;
    private List<WithExpr> currentRuleWiths;
    private List<AssertExpr> currentRuleAsserts;

    // current width
    private String currentWithAttribute;

    // current assert
    private String currentAssertAttribute;

    // evaluator
    private ExprEvaluator<Object> currentEvaluator;
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
        currentRuleTitle = stripQuotes(ctx.STRING().getText());
        currentRuleWiths = new ArrayList<>();
        currentRuleAsserts = new ArrayList<>();
    }

    @Override
    public void exitRuleDecl(CloudSpecParser.RuleDeclContext ctx) {
        currentGroupRules.add(new RuleExpr(currentRuleTitle, currentRuleResourceTypeFQName, currentRuleWiths, currentRuleAsserts));
    }

    @Override
    public void enterOnDecl(CloudSpecParser.OnDeclContext ctx) {
        currentRuleResourceTypeFQName = ctx.ENTITY_REF().getText();
    }

    @Override
    public void enterWithDecl(CloudSpecParser.WithDeclContext ctx) {
        currentWithAttribute = stripQuotes(ctx.ATTRIBUTE_NAME().getText());
        currentEvaluator = null;
        currentValues = new ArrayList<Object>();
    }

    @Override
    public void exitWithDecl(CloudSpecParser.WithDeclContext ctx) {
        currentRuleWiths.add(new WithExpr(currentWithAttribute, currentEvaluator));
    }

    @Override
    public void enterAssertDecl(CloudSpecParser.AssertDeclContext ctx) {
        currentAssertAttribute = stripQuotes(ctx.ATTRIBUTE_NAME().getText());
        currentEvaluator = null;
        currentValues = new ArrayList<Object>();
    }

    @Override
    public void exitAssertDecl(CloudSpecParser.AssertDeclContext ctx) {
        currentRuleAsserts.add(new AssertExpr(currentAssertAttribute, currentEvaluator));
    }

    @Override
    public void exitWithEqualExpr(CloudSpecParser.WithEqualExprContext ctx) {
        currentEvaluator = new EqualExprEvaluator<Object>(currentValues.get(0), Boolean.FALSE);
    }

    @Override
    public void exitWithNotEqualExpr(CloudSpecParser.WithNotEqualExprContext ctx) {
        currentEvaluator = new EqualExprEvaluator<Object>(currentValues.get(0), Boolean.TRUE);
    }

    @Override
    public void exitWithInExpr(CloudSpecParser.WithInExprContext ctx) {
        currentEvaluator = new InExprEvaluator<Object>(currentValues, Boolean.FALSE);
    }

    @Override
    public void exitWithNotInExpr(CloudSpecParser.WithNotInExprContext ctx) {
        currentEvaluator = new InExprEvaluator<Object>(currentValues, Boolean.TRUE);
    }

    @Override
    public void exitAssertEqualExpr(CloudSpecParser.AssertEqualExprContext ctx) {
        currentEvaluator = new EqualExprEvaluator<Object>(currentValues.get(0), Boolean.FALSE);
    }

    @Override
    public void exitAssertNotEqualExpr(CloudSpecParser.AssertNotEqualExprContext ctx) {
        currentEvaluator = new EqualExprEvaluator<Object>(currentValues.get(0), Boolean.TRUE);
    }

    @Override
    public void exitAssertInExpr(CloudSpecParser.AssertInExprContext ctx) {
        currentEvaluator = new InExprEvaluator<Object>(currentValues, Boolean.FALSE);
    }

    @Override
    public void exitAssertNotInExpr(CloudSpecParser.AssertNotInExprContext ctx) {
        currentEvaluator = new InExprEvaluator<Object>(currentValues, Boolean.TRUE);
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
