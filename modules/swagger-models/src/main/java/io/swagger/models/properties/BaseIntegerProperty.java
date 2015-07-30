package io.swagger.models.properties;

/**
 * The <code>BaseIntegerProperty</code> class defines property for integers
 * without specific format.
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
        return TYPE.equals(type) && format == null;
    }
}
