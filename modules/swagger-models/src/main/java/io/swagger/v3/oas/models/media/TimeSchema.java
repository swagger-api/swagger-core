package io.swagger.v3.oas.models.media;

import java.time.OffsetTime;
import java.util.Objects;

/**
 * TimeSchema
 */
public class TimeSchema extends Schema<OffsetTime> {

    public TimeSchema() {
        super("string", "time");
    }

    @Override
    public TimeSchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public TimeSchema format(String format) {
        super.setFormat(format);
        return this;
    }

    @Override
    protected OffsetTime cast(Object value) {
        if (value != null) {
            try {
                if (value instanceof OffsetTime) {
                    return (OffsetTime) value;
                } else if (value instanceof String) {
                    return OffsetTime.parse((String) value);
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
        sb.append("class TimeSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
