package io.swagger.models.properties;

import io.swagger.models.Xml;

public class DecimalProperty extends AbstractNumericProperty implements Property {
    public DecimalProperty() {
        super.type = "number";
    }

    public static boolean isType(String type, String format) {
        if ("number".equals(type) && format == null) {
            return true;
        } else {
            return false;
        }
    }

    public DecimalProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public DecimalProperty example(String example) {
        this.setExample(example);
        return this;
    }
}