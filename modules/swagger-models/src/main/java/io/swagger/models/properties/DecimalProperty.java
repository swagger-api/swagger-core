package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.List;

public class DecimalProperty extends AbstractNumericProperty {
    public static final String TYPE = "number";

    public DecimalProperty() {
        this(null);
    }

    public DecimalProperty(String format) {
        super.type = TYPE;
        super.format = format;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && format == null;
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
