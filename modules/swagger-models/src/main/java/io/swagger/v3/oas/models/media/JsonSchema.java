package io.swagger.v3.oas.models.media;

import io.swagger.v3.oas.models.SpecVersion;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * JsonSchema
 */

public class JsonSchema extends Schema<Object> {

    private static final Set<String> NUMBER_TYPES = new HashSet<>(Arrays.asList("integer", "number"));

    public JsonSchema (){
        specVersion(SpecVersion.V31);
    }

    private String resolveType() {
        if (this.getTypes() != null) {
            if (this.getTypes().size() == 1) {
                String type = this.getTypes().iterator().next();
                if (NUMBER_TYPES.contains(type)) {
                    return "number";
                }
                return type;
            }
            if (this.getTypes().contains("object")) {
                return "object";
            } else if (this.getTypes().contains("string")) {
                return "string";
            } else if (this.getTypes().contains("array")) {
                return "array";
            } else if (this.getTypes().contains("integer") || this.getTypes().contains("number")) {
                return "number";
            } else if (this.getTypes().contains("boolean")) {
                return "boolean";
            }
            return this.getTypes().iterator().next();
        }
        return "null";
    }

    protected Object cast(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            if (resolveType().equals("number")) {
                try {
                    Number casted = NumberFormat.getInstance().parse(value.toString());
                    if (Integer.MIN_VALUE <= casted.longValue() && casted.longValue() <= Integer.MAX_VALUE) {
                        return Integer.parseInt(value.toString());
                    } else {
                        return Long.parseLong(value.toString());
                    }
                } catch (Exception e) {
                    return value;
                }
            } else if (resolveType().equals("boolean")) {
                return Boolean.parseBoolean(value.toString());
            }

        }
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class JsonSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
