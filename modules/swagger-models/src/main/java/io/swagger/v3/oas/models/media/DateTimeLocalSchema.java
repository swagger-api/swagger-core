package io.swagger.v3.oas.models.media;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DateTimeLocalSchema
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

    @Override
    protected LocalDateTime cast(Object value) {
        if (value != null) {
            try {
                if (value instanceof LocalDateTime) {
                    return (LocalDateTime) value;
                } else if (value instanceof String) {
                    return LocalDateTime.parse((String) value);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
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
