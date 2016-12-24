package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class EmailProperty extends StringProperty {
    public EmailProperty() {
        super.type = "string";
        super.format = "email";
    }

    public EmailProperty(StringProperty prop) {
        this();
        this._enum = prop.getEnum();
        this.minLength = prop.getMinLength();
        this.maxLength = prop.getMaxLength();
        this.pattern = prop.getPattern();
        this.name = prop.getName();
        this.type = prop.getType();
        this.example = prop.getExample();
        this._default = prop.getDefault();
        this.xml = prop.getXml();
        this.required = prop.getRequired();
        this.position = prop.getPosition();
        this.description = prop.getDescription();
        this.title = prop.getTitle();
        this.readOnly = prop.getReadOnly();
    }

    public EmailProperty _enum(String value) {
        if (this._enum == null) {
            this._enum = new ArrayList<String>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public EmailProperty _enum(List<String> value) {
        this._enum = value;
        return this;
    }

    public static boolean isType(String type, String format) {
        if ("string".equals(type) && "email".equals(format)) {
            return true;
        } else {
            return false;
        }
    }

    public EmailProperty xml(Xml xml) {
        super.xml(xml);
        return this;
    }

    public EmailProperty example(String example) {
        super.example(example);
        return this;
    }

    public EmailProperty minLength(Integer minLength) {
        super.minLength(minLength);
        return this;
    }

    public EmailProperty maxLength(Integer maxLength) {
        super.maxLength(maxLength);
        return this;
    }

    public EmailProperty pattern(String pattern) {
        super.pattern(pattern);
        return this;
    }

    public EmailProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public EmailProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }
}