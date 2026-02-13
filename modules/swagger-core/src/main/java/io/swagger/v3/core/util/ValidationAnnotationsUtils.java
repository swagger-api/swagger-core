package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.media.Schema;

import javax.validation.constraints.*;
import java.math.BigDecimal;

import static io.swagger.v3.core.util.SchemaTypeUtils.*;

public class ValidationAnnotationsUtils {

    public static final String JAVAX_NOT_NULL = "javax.validation.constraints.NotNull";
    public static final String JAVAX_NOT_EMPTY = "javax.validation.constraints.NotEmpty";
    public static final String JAVAX_NOT_BLANK = "javax.validation.constraints.NotBlank";
    public static final String JAVAX_MIN = "javax.validation.constraints.Min";
    public static final String JAVAX_MAX = "javax.validation.constraints.Max";
    public static final String JAVAX_SIZE = "javax.validation.constraints.Size";
    public static final String JAVAX_DECIMAL_MIN = "javax.validation.constraints.DecimalMin";
    public static final String JAVAX_DECIMAL_MAX = "javax.validation.constraints.DecimalMax";
    public static final String JAVAX_PATTERN = "javax.validation.constraints.Pattern";
    public static final String JAVAX_EMAIL = "javax.validation.constraints.Email";

    private static final String SCHEMA_EMAIL_FORMAT_NAME = "email";

    /**
     * @param schema         the schema
     * @param ctxSchema      the schema's {@link io.swagger.v3.oas.annotations.media.Schema} annotation
     * @param ctxArraySchema the schema's {@link io.swagger.v3.oas.annotations.media.ArraySchema} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyNotEmptyConstraint(Schema schema,
                                                  io.swagger.v3.oas.annotations.media.Schema ctxSchema,
                                                  io.swagger.v3.oas.annotations.media.ArraySchema ctxArraySchema) {
        if (isArraySchema(schema)) {
            if (ctxArraySchema == null || ctxArraySchema.minItems() == Integer.MAX_VALUE) {
                schema.setMinItems(1);
                return true;
            }
        } else if (isStringSchema(schema)) {
            if (ctxSchema == null || ctxSchema.minLength() == 0) {
                schema.setMinLength(1);
                return true;
            }
        } else if (isObjectSchema(schema)) {
            if (ctxSchema == null || ctxSchema.minProperties() == 0) {
                schema.setMinProperties(1);
                return true;
            }
        }
        return false;
    }

    /**
     * @param schema    the schema
     * @param ctxSchema the schema's {@link io.swagger.v3.oas.annotations.media.Schema} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyNotBlankConstraint(Schema schema, io.swagger.v3.oas.annotations.media.Schema ctxSchema) {
        if (isStringSchema(schema)) {
            if (ctxSchema == null || ctxSchema.minLength() == 0) {
                schema.setMinLength(1);
                return true;
            }
        }
        return false;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link Min} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyMinConstraint(Schema schema, Min annotation) {
        if (isNumberSchema(schema)) {
            schema.setMinimum(new BigDecimal(annotation.value()));
            return true;
        }
        return false;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link Max} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyMaxConstraint(Schema schema, Max annotation) {
        if (isNumberSchema(schema)) {
            schema.setMaximum(new BigDecimal(annotation.value()));
            return true;
        }
        return false;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link Size} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applySizeConstraint(Schema schema, Size annotation) {
        if (isNumberSchema(schema)) {
            schema.setMinimum(new BigDecimal(annotation.min()));
            schema.setMaximum(new BigDecimal(annotation.max()));
            return true;
        }
        if (isStringSchema(schema)) {
            schema.setMinLength(annotation.min());
            schema.setMaxLength(annotation.max());
            return true;
        }
        if (isArraySchema(schema)) {
            schema.setMinItems(annotation.min());
            schema.setMaxItems(annotation.max());
            return true;
        }
        return false;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link DecimalMin} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyDecimalMinConstraint(Schema schema, DecimalMin annotation) {
        if (isNumberSchema(schema)) {
            schema.setMinimum(new BigDecimal(annotation.value()));
            schema.setExclusiveMinimum(!annotation.inclusive());
            return true;
        }
        return false;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link DecimalMax} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyDecimalMaxConstraint(Schema schema, DecimalMax annotation) {
        if (isNumberSchema(schema)) {
            schema.setMaximum(new BigDecimal(annotation.value()));
            schema.setExclusiveMaximum(!annotation.inclusive());
            return true;
        }
        return false;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link Pattern} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyPatternConstraint(Schema schema, Pattern annotation) {
        if (isStringSchema(schema)) {
            schema.setPattern(annotation.regexp());
            return true;
        }
        if (schema.getItems() != null && isStringSchema(schema.getItems())) {
            schema.getItems().setPattern(annotation.regexp());
            return true;
        }
        return false;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link Email} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyEmailConstraint(Schema schema, Email annotation) {
        if (isStringSchema(schema)) {
            schema.setFormat(SCHEMA_EMAIL_FORMAT_NAME);
            return true;
        }
        if (schema.getItems() != null && isStringSchema(schema.getItems())) {
            schema.getItems().setFormat(SCHEMA_EMAIL_FORMAT_NAME);
            return true;
        }
        return false;
    }

}
