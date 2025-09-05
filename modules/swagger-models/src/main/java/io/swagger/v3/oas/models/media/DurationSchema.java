package io.swagger.v3.oas.models.media;

import java.time.Duration;
import java.util.Objects;

/**
 * DurationSchema
 */

public class DurationSchema extends Schema<Duration> {

    public DurationSchema() {
        super("string", "duration");
    }

    @Override
    public DurationSchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public DurationSchema format(String format) {
        super.setFormat(format);
        return this;
    }

    @Override
    public DurationSchema _default(Duration _default) {
        super.setDefault(_default);
        return this;
    }

    @Override
    protected Duration cast(Object value) {
        if (value != null) {
            try {
                if (value instanceof Duration) {
                    return (Duration) value;
                } else if (value instanceof String) {
                    return Duration.parse((String)value);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public DurationSchema addEnumItem(Duration _enumItem) {
        super.addEnumItemObject(_enumItem);
        return this;
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
        sb.append("class DurationSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}

