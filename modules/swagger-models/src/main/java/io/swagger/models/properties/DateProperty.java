package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class DateProperty extends AbstractProperty implements Property {
    protected List<String> _enum;

    public DateProperty() {
        super.type = "string";
        super.format = "date";
    }

    public DateProperty _enum(String value) {
        if (this._enum == null) {
            this._enum = new ArrayList<String>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public DateProperty _enum(List<String> value) {
        this._enum = value;
        return this;
    }

    public static boolean isType(String type, String format) {
        if ("string".equals(type) && "date".equals(format)) {
            return true;
        } else {
            return false;
        }
    }

    public DateProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public DateProperty example(String example) {
        this.setExample(example);
        return this;
    }

    public DateProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public List<String> getEnum() {
        return _enum;
    }

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    public DateProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }
}