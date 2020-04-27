package cloudspec.spec;

import java.util.List;

public class CloudSpec {
    private final String name;
    private final List<GroupExpr> groups;

    public CloudSpec(String name, List<GroupExpr> groups) {
        this.name = name;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public List<GroupExpr> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "CloudSpec{" +
                "name='" + name + '\'' +
                ", groups=" + groups +
                '}';
    }
}
