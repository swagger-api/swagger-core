package io.swagger.models.properties;

import io.swagger.models.Xml;

public class ByteArrayProperty extends StringProperty implements Property {


    public ByteArrayProperty() {
        super.type = "string";
        super.format = "byte";
    }

    public static boolean isType(String type, String format) {
        if ("string".equals(type) && "byte".equals(format))
            return true;
        else return false;
    }

    public ByteArrayProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public ByteArrayProperty example(String example) {
        this.setExample(example);
        return this;
    }

    public ByteArrayProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public ByteArrayProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }
}