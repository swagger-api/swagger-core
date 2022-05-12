package io.swagger.v3.oas.models.media;

import java.util.Objects;

/**
 * ObjectSchema
 */

public class ObjectSchema extends Schema<Object> {

    public ObjectSchema() {
        super("object", null);
    }

    @Override
    public ObjectSchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public ObjectSchema example(Object example) {
        if (example != null) {
            super.setExample(example.toString());
        } else {
            super.setExample(example);
        }
        return this;
    }

    @Override
    protected Object cast(Object value) {
        return value;
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
        sb.append("class ObjectSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
