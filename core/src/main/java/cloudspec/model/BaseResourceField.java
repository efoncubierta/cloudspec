package cloudspec.model;

public abstract class BaseResourceField implements ResourceField {
    private final String name;

    protected BaseResourceField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
