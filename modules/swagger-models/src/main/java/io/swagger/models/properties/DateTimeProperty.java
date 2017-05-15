package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class DateTimeProperty extends AbstractProperty implements Property {
    protected List<String> _enum;

    public DateTimeProperty() {
        super.type = "string";
        super.format = "date-time";
    }

    public DateTimeProperty _enum(String value) {
        if (this._enum == null) {
            this._enum = new ArrayList<String>();
        }
        if (!_enum.contains(value)) {
            _enum.add(value);
        }
        return this;
    }

    public DateTimeProperty _enum(List<String> value) {
        this._enum = value;
        return this;
    }

    public static boolean isType(String type, String format) {
        if ("string".equals(type) && "date-time".equals(format)) {
            return true;
        } else {
            return false;
        }
    }

    public DateTimeProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public DateTimeProperty example(String example) {
        this.setExample(example);
        return this;
    }

    public DateTimeProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public List<String> getEnum() {
        return _enum;
    }

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    public DateTimeProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }
}