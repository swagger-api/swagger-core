package io.swagger.models.properties;

import io.swagger.models.Xml;

/**
 * The DecimalProperty class defines properties for (decimal) numbers without a specific format, or with a custom
 * format. The two standard formats are defined in {@link DoubleProperty} and {@link FloatProperty}.
 */
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
        return TYPE.equals(type);
    }

    public DecimalProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public DecimalProperty example(String example) {
        this.setExample(example);
        return this;
    }

    public DecimalProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public DecimalProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    @Override
    public void setExample(Object example) {
        if (example instanceof String) {
            try {
                this.example = Double.parseDouble((String)example);
            } catch (NumberFormatException e) {
                this.example = example;
            }
        } else {
            this.example = example;
        }
    }
}
