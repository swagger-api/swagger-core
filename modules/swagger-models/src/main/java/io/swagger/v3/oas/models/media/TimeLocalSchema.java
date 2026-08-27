package io.swagger.v3.oas.models.media;

import java.time.LocalTime;
import java.util.Objects;

/**
 * TimeLocalSchema
 */
public class TimeLocalSchema extends Schema<LocalTime> {

    public TimeLocalSchema() {
        super("string", "time-local");
    }

    @Override
    public TimeLocalSchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public TimeLocalSchema format(String format) {
        super.setFormat(format);
        return this;
    }

    @Override
    protected LocalTime cast(Object value) {
        if (value != null) {
            try {
                if (value instanceof LocalTime) {
                    return (LocalTime) value;
                } else if (value instanceof String) {
                    return LocalTime.parse((String) value);
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
        sb.append("class TimeLocalSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
