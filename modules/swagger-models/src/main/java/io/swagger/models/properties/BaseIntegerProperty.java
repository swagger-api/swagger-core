package io.swagger.models.properties;

/**
 * The <code>BaseIntegerProperty</code> class defines property for integers without specific format, or with a custom
 * format.
 */
public class BaseIntegerProperty extends AbstractNumericProperty {
    public static final String TYPE = "integer";

    public BaseIntegerProperty() {
        this(null);
    }

    public BaseIntegerProperty(String format) {
        super.type = TYPE;
        super.format = format;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type);
    }

    @Override
    public void setExample(Object example) {
        if (example instanceof String) {
            try {
                this.example = Long.parseLong((String)example);
            } catch (NumberFormatException e) {
                this.example = example;
            }
        } else {
            this.example = example;
        }
    }
}
