package io.swagger.models.composition;

public class ModelWithValueProperty extends AbstractModelWithApiModel {

    private final String value;

    public ModelWithValueProperty(String type, String value) {
        super(type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
