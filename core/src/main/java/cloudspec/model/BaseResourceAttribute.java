package cloudspec.model;

public abstract class BaseResourceAttribute<V> extends BaseResourceField implements ResourceAttribute<V> {
    private final V value;

    public BaseResourceAttribute(String name, V value) {
        super(name);

        this.value = value;
    }

    @Override
    public V getValue() {
        return value;
    }
}
