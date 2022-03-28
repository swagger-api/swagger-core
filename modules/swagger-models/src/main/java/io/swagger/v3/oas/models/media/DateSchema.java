package io.swagger.v3.oas.models.media;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * DateSchema
 */

public class DateSchema extends Schema<Date> {

    public DateSchema() {
        super("string", "date");
    }

    @Override
    public DateSchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public DateSchema format(String format) {
        super.setFormat(format);
        return this;
    }

    public DateSchema _default(Date _default) {
        super.setDefault(_default);
        return this;
    }

    @Override
    protected Date cast(Object value) {
        if (value != null) {
            try {
                if (value instanceof Date) {
                    return (Date) value;
                } else if (value instanceof String) {
                    return new SimpleDateFormat("yyyy-MM-dd Z").parse((String)value + " UTC");
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public DateSchema addEnumItem(Date _enumItem) {
        super.addEnumItemObject(_enumItem);
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DateSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}

