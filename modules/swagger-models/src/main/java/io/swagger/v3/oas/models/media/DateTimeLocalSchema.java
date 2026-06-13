package io.swagger.v3.oas.models.media;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * DateTimeLocalSchema
 * <p>
 * Schema for date-time-local format as defined in the Format Registry
 * <a href="https://spec.openapis.org/registry/format/date-time-local.html">date-time-local</a>
 */
public class DateTimeLocalSchema extends Schema<LocalDateTime> {

    public DateTimeLocalSchema() {
        super("string", "date-time-local");
    }

    @Override
    public DateTimeLocalSchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public DateTimeLocalSchema format(String format) {
        super.setFormat(format);
        return this;
    }

    public DateTimeLocalSchema _default(LocalDateTime _default) {
        super.setDefault(_default);
        return this;
    }

    @Override
    protected LocalDateTime cast(Object value) {
        if (value != null) {
            try {
                if (value instanceof String) {
                    return LocalDateTime.parse((String) value);
                } else if (value instanceof LocalDateTime) {
                    return (LocalDateTime) value;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public DateTimeLocalSchema _enum(List<LocalDateTime> _enum) {
        super.setEnum(_enum);
        return this;
    }

    public DateTimeLocalSchema addEnumItem(LocalDateTime _enumItem) {
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
        sb.append("class DateTimeLocalSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
