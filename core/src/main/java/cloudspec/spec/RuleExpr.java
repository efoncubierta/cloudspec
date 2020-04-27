package cloudspec.spec;

import java.util.List;

public class RuleExpr {
    private final String title;
    private final String resourceTypeFQName;
    private final List<WithExpr> withs;
    private final List<AssertExpr> asserts;

    public RuleExpr(String title, String resourceTypeFQName, List<WithExpr> withs, List<AssertExpr> asserts) {
        this.title = title;
        this.resourceTypeFQName = resourceTypeFQName;
        this.withs = withs;
        this.asserts = asserts;
    }

    public String getTitle() {
        return title;
    }

    public String getResourceTypeFQName() {
        return resourceTypeFQName;
    }

    public List<WithExpr> getWiths() {
        return withs;
    }

    public List<AssertExpr> getAsserts() {
        return asserts;
    }

    @Override
    public String toString() {
        return "SpecRule{" +
                "title='" + title + '\'' +
                ", resourceFQName='" + resourceTypeFQName + '\'' +
                ", withs=" + withs +
                ", asserts=" + asserts +
                '}';
    }
}
