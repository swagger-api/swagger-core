package io.swagger.v3.oas.models.media;

import java.util.Objects;

/**
 * EmailSchema
 */

public class EmailSchema extends Schema<String> {

    public EmailSchema() {
        super("string", "email");
    }

    @Override
    public EmailSchema type(String type) {
        super.setType(type);
        return this;
    }

    @Override
    public EmailSchema format(String format) {
        super.setFormat(format);
        return this;
    }

    public EmailSchema _default(String _default) {
        super.setDefault(_default);
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

    public EmailSchema addEnumItem(String _enumItem) {
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
        sb.append("class EmailSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
