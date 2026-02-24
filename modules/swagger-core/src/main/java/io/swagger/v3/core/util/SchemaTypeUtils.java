package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.media.Schema;

public class SchemaTypeUtils {

    private static final String OBJECT_TYPE = "object";
    private static final String ARRAY_TYPE = "array";
    private static final String STRING_TYPE = "string";
    private static final String NUMBER_TYPE = "number";
    private static final String INTEGER_TYPE = "integer";

    public static boolean isObjectSchema(Schema schema) {
        return isSchemaType(schema, OBJECT_TYPE) || (schema.getType() == null && (hasProperties(schema) || hasPatternProperties(schema)));
    }

    public static boolean isArraySchema(Schema schema) {
        return isSchemaType(schema, ARRAY_TYPE);
    }

    public static boolean isStringSchema(Schema schema) {
        return isSchemaType(schema, STRING_TYPE);
    }

    public static boolean isNumberSchema(Schema schema) {
        return isSchemaType(schema, NUMBER_TYPE) || isSchemaType(schema, INTEGER_TYPE);
    }

    private static boolean isSchemaType(Schema schema, String type) {
        return type.equals(schema.getType()) || isSchemaType31(schema, type);
    }

    private static boolean isSchemaType31(Schema schema, String type) {
        return schema.getTypes() != null && schema.getTypes().contains(type);
    }

    private static boolean hasProperties(Schema schema) {
        return schema.getProperties() != null && !schema.getProperties().isEmpty();
    }

    private static boolean hasPatternProperties(Schema schema) {
        return schema.getPatternProperties() != null && !schema.getPatternProperties().isEmpty();
    }

}
