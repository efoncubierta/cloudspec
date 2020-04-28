package cloudspec.core;

public abstract class BaseResourceAttribute extends BaseResourceField implements ResourceAttribute {
    private final Object value;

    public BaseResourceAttribute(String name, Object value) {
        super(name);

        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
