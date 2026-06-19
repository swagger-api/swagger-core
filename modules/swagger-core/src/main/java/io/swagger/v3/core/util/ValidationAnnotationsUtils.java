package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
    public static final String JAVAX_POSITIVE = "javax.validation.constraints.Positive";
    public static final String JAVAX_POSITIVE_OR_ZERO = "javax.validation.constraints.PositiveOrZero";
    public static final String JAVAX_NEGATIVE = "javax.validation.constraints.Negative";
    public static final String JAVAX_NEGATIVE_OR_ZERO = "javax.validation.constraints.NegativeOrZero";

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
        if (!isNumberSchema(schema)) {
            return false;
        }
        BigDecimal value = new BigDecimal(annotation.value());
        if (schema.getSpecVersion().equals(SpecVersion.V31)) {
            if (!annotation.inclusive()) {
                schema.setExclusiveMinimumValue(value);
                BigDecimal minimum = schema.getMinimum();
                if (minimum != null && minimum.compareTo(value) <= 0) {
                    schema.setMinimum(null);
                }
            } else {
                schema.setMinimum(value);
            }
        } else {
            schema.setMinimum(value);
            schema.setExclusiveMinimum(!annotation.inclusive());
        }
        return true;
    }

    /**
     * @param schema     the schema
     * @param annotation the schema's {@link DecimalMax} annotation
     * @return whether the schema has been modified or not
     */
    public static boolean applyDecimalMaxConstraint(Schema schema, DecimalMax annotation) {
        if (!isNumberSchema(schema)) {
            return false;
        }
        BigDecimal value = new BigDecimal(annotation.value());
        if (schema.getSpecVersion().equals(SpecVersion.V31)) {
            if (!annotation.inclusive()) {
                schema.setExclusiveMaximumValue(value);
                BigDecimal maximum = schema.getMaximum();
                if (maximum != null && maximum.compareTo(value) >= 0) {
                    schema.setMaximum(null);
                }
            } else {
                schema.setMaximum(value);
            }
        } else {
            schema.setMaximum(value);
            schema.setExclusiveMaximum(!annotation.inclusive());
        }
        return true;
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

    public static boolean applyPositiveConstraint(Schema schema) {
        if (!isNumberSchema(schema)) {
            return false;
        }
        if (schema.getSpecVersion().equals(SpecVersion.V30)) {
            return applyPositiveConstraintV30(schema);
        }
        return applyPositiveConstraintV31(schema);
    }

    private static boolean applyPositiveConstraintV30(Schema schema) {
        BigDecimal minimum = schema.getMinimum();
        if (currentMinimumOutsidePositiveRange(minimum)) {
            schema.setMinimum(BigDecimal.ZERO);
            schema.setExclusiveMinimum(true);
        } else if (minimum.compareTo(BigDecimal.ZERO) == 0 && !Boolean.TRUE.equals(schema.getExclusiveMinimum())) {
            schema.setExclusiveMinimum(true);
        }
        return true;
    }

    private static boolean applyPositiveConstraintV31(Schema schema) {
        BigDecimal exclusiveMinimum = schema.getExclusiveMinimumValue();
        if (exclusiveMinimum != null) {
            if (exclusiveMinimum.compareTo(BigDecimal.ZERO) < 0) {
                BigDecimal minimum = schema.getMinimum();
                if (minimum != null && minimum.compareTo(BigDecimal.ZERO) > 0) {
                    schema.setExclusiveMinimumValue(null);
                } else {
                    schema.setMinimum(null);
                    schema.setExclusiveMinimumValue(BigDecimal.ZERO);
                }
                return true;
            }
            return false;
        }
        BigDecimal minimum = schema.getMinimum();
        if (minimum != null && minimum.compareTo(BigDecimal.ZERO) > 0) {
            return false;
        }
        schema.setMinimum(null);
        schema.setExclusiveMinimumValue(BigDecimal.ZERO);
        return true;
    }

    public static boolean applyPositiveOrZeroConstraint(Schema schema) {
        if (isNumberSchema(schema)) {
            BigDecimal current = schema.getMinimum();
            if (current == null || current.compareTo(BigDecimal.ZERO) < 0) {
                schema.setMinimum(BigDecimal.ZERO);
            }
            return true;
        }
        return false;
    }

    public static boolean applyNegativeConstraint(Schema schema) {
        if (!isNumberSchema(schema)) {
            return false;
        }
        if (schema.getSpecVersion().equals(SpecVersion.V30)) {
            return applyNegativeConstraintV30(schema);
        }
        return applyNegativeConstraintV31(schema);
    }

    private static boolean applyNegativeConstraintV30(Schema schema) {
        BigDecimal maximum = schema.getMaximum();
        if (currentMaximumOutsideNegativeRange(maximum)) {
            schema.setMaximum(BigDecimal.ZERO);
            schema.setExclusiveMaximum(true);
        } else if (maximum.compareTo(BigDecimal.ZERO) == 0 && !Boolean.TRUE.equals(schema.getExclusiveMaximum())) {
            schema.setExclusiveMaximum(true);
        }
        return true;
    }

    private static boolean applyNegativeConstraintV31(Schema schema) {
        BigDecimal exclusiveMaximum = schema.getExclusiveMaximumValue();
        if (exclusiveMaximum != null) {
            if (exclusiveMaximum.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal maximum = schema.getMaximum();
                if (maximum != null && maximum.compareTo(BigDecimal.ZERO) < 0) {
                    schema.setExclusiveMaximumValue(null);
                } else {
                    schema.setMaximum(null);
                    schema.setExclusiveMaximumValue(BigDecimal.ZERO);
                }
                return true;
            }
            return false;
        }
        BigDecimal maximum = schema.getMaximum();
        if (maximum != null && maximum.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        schema.setMaximum(null);
        schema.setExclusiveMaximumValue(BigDecimal.ZERO);
        return true;
    }

    /**
     * Expands annotations to include bean-validation constraint annotations present as meta-annotations
     * on custom composed constraints (e.g. a custom {@code @ValidStoreId} annotated with {@code @Min}/{@code @Max}).
     * Direct annotations take priority over meta-annotations (putIfAbsent).
     */
    public static Annotation[] expandValidationMetaAnnotations(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return annotations;
        }
        Map<String, Annotation> merged = new LinkedHashMap<>();
        for (Annotation a : annotations) {
            if (a != null) {
                merged.put(a.annotationType().getName(), a);
            }
        }
        try {
            Set<Class<?>> visited = new HashSet<>();
            Queue<Annotation> queue = new ArrayDeque<>();
            for (Annotation a : annotations) {
                if (a != null) queue.add(a);
            }
            while (!queue.isEmpty()) {
                Annotation a = queue.poll();
                if (!visited.add(a.annotationType())) continue;
                for (Annotation meta : a.annotationType().getAnnotations()) {
                    if (meta == null) continue;
                    String name = meta.annotationType().getName();
                    if (name.startsWith("javax.validation.constraints")) {
                        merged.putIfAbsent(name, meta);
                    } else {
                        queue.add(meta);
                    }
                }
            }
        } catch (Throwable t) {
            return annotations;
        }
        return merged.values().toArray(new Annotation[0]);
    }

    public static boolean applyNegativeOrZeroConstraint(Schema schema) {
        if (isNumberSchema(schema)) {
            BigDecimal current = schema.getMaximum();
            if (current == null || current.compareTo(BigDecimal.ZERO) > 0) {
                schema.setMaximum(BigDecimal.ZERO);
            }
            return true;
        }
        return false;
    }

    private static boolean currentMinimumOutsidePositiveRange(BigDecimal currentMinimum) {
        return currentMinimum == null || currentMinimum.compareTo(BigDecimal.ZERO) < 0;
    }

    private static boolean currentMaximumOutsideNegativeRange(BigDecimal currentMaximum) {
        return currentMaximum == null || currentMaximum.compareTo(BigDecimal.ZERO) > 0;
    }

}
