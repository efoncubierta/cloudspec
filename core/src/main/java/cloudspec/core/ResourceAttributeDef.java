package cloudspec.core;

public class ResourceAttributeDef extends ResourceFieldDef {
    private final ResourceAttributeType attributeType;
    private final Boolean isArray;

    public ResourceAttributeDef(String name, String description, ResourceAttributeType attributeType, Boolean isArray) {
        super(name, description);

        this.attributeType = attributeType;
        this.isArray = isArray;
    }

    public ResourceAttributeType getAttributeType() {
        return attributeType;
    }

    public Boolean isArray() {
        return isArray;
    }
}
