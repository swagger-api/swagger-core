package io.swagger.v3.oas.models.media;

import java.time.LocalTime;
import java.util.Objects;

/**
 * TimeLocalSchema
 */
public class TimeLocalSchema extends Schema<LocalTime> {

    /**
     * The example exactly as it was supplied, when it was a valid string.
     * Parsing it into a {@link LocalTime} and printing it back can change its form
     * (for example {@code P7D} becomes {@code PT168H}), so the original string is kept for output.
     */
    private String exampleString;

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
    public void setExample(Object example) {
        super.setExample(example);
        this.exampleString = example instanceof String && super.getExample() != null ? (String) example : null;
    }

    @Override
    public Object getExample() {
        return exampleString != null ? exampleString : super.getExample();
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
        TimeLocalSchema that = (TimeLocalSchema) o;
        return Objects.equals(exampleString, that.exampleString) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), exampleString);
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
