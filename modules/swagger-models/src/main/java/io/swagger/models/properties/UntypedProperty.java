package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class UntypedProperty extends AbstractProperty implements Property {
    public static final String TYPE = null;

    public UntypedProperty() {
        super.type = TYPE;
    }

    public UntypedProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public static boolean isType(String type) {
        return TYPE == type;
    }

    public static boolean isType(String type, String format) {
        return isType(type);
    }

    public UntypedProperty access(String access) {
        this.setAccess(access);
        return this;
    }

    public UntypedProperty description(String description) {
        this.setDescription(description);
        return this;
    }

    public UntypedProperty name(String name) {
        this.setName(name);
        return this;
    }

    public UntypedProperty title(String title) {
        this.setTitle(title);
        return this;
    }

    public UntypedProperty _default(String _default) {
        this.setDefault(_default);
        return this;
    }

    public UntypedProperty readOnly(boolean readOnly) {
        this.setReadOnly(readOnly);
        return this;
    }

    public UntypedProperty required(boolean required) {
        this.setRequired(required);
        return this;
    }

    public UntypedProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    public UntypedProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public UntypedProperty example(Object example) {
        this.setExample(example);
        return this;
    }
}
