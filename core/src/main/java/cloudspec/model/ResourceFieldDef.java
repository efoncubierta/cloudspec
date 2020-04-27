package cloudspec.model;

public abstract class ResourceFieldDef {
    private final String name;
    private final String description;

    protected ResourceFieldDef(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
