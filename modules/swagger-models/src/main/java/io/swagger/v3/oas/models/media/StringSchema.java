package io.swagger.v3.oas.models.media;

import java.util.List;
import java.util.Objects;

/**
 * StringSchema
 */

public class StringSchema extends Schema<String> {

    public StringSchema() {
        super("string", null);
    }

    @Override
    public StringSchema type(String type) {
        super.setType(type);
        return this;
    }

    public StringSchema _default(String _default) {
        super.setDefault(_default);
        return this;
    }

    public StringSchema _enum(List<String> _enum) {
        super.setEnum(_enum);
        return this;
    }

    public StringSchema addEnumItem(String _enumItem) {
        super.addEnumItemObject(_enumItem);
        return this;
    }

    @Override
    protected String cast(Object value) {
        if (value != null) {
            try {
                return value.toString();
            } catch (Exception e) {
            }
        }
        return null;
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
        sb.append("class StringSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
