package io.swagger.models.properties;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.swagger.models.Xml;

public class ComposedProperty extends AbstractProperty implements Property {

    public static final String TYPE = "object";
    private List<Property> allOf = new LinkedList<Property>();

    public ComposedProperty() {
        super.type = TYPE;
    }

    public ComposedProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public ComposedProperty access(String access) {
        this.setAccess(access);
        return this;
    }

    @Override
    public ComposedProperty description(String description) {
        this.setDescription(description);
        return this;
    }

    public ComposedProperty name(String name) {
        this.setName(name);
        return this;
    }

    @Override
    public ComposedProperty title(String title) {
        this.setTitle(title);
        return this;
    }

    public ComposedProperty _default(String _default) {
        this.setDefault(_default);
        return this;
    }

    public ComposedProperty readOnly(boolean readOnly) {
        this.setReadOnly(readOnly);
        return this;
    }

    public ComposedProperty required(boolean required) {
        this.setRequired(required);
        return this;
    }

    public List<Property> getAllOf() {
        return allOf;
    }

    public void setAllOf(List<Property> allOf) {
        this.allOf = allOf;
    }

    @Override
    public ComposedProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    public ComposedProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public ComposedProperty example(Object example) {
        this.setExample(example);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ComposedProperty)) {
            return false;
        }

        return Objects.equals(allOf, ((ComposedProperty) o).allOf);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (allOf != null ? allOf.hashCode() : 0);

        return result;
    }
}
