package io.swagger.v3.oas.models.media;

import java.util.Objects;

/**
 * ArbitrarySchema
 */

public class ArbitrarySchema extends Schema<Object> {

    public ArbitrarySchema() {
    }

    @Override
    public ArbitrarySchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public ArbitrarySchema example(Object example) {
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
        sb.append("class ArbitrarySchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
