package io.swagger.util;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.BinarySchema;
import io.swagger.oas.models.media.ByteArraySchema;
import io.swagger.oas.models.media.DateSchema;
import io.swagger.oas.models.media.DateTimeSchema;
import io.swagger.oas.models.media.EmailSchema;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.PasswordSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.oas.models.media.UUIDSchema;
import io.swagger.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class ParameterProcessor {
    static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);

    public static Parameter applyAnnotations(OpenAPI openAPI, Parameter parameter, Type type, List<Annotation> annotations) {
        final AnnotationsHelper helper = new AnnotationsHelper(annotations, type);
        if (helper.isContext()) {
            return null;
        }
        if (parameter == null) {
            // consider it to be body param
            parameter = new Parameter();
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof io.swagger.oas.annotations.media.Schema) {
                Schema schema = processSchema((io.swagger.oas.annotations.media.Schema) annotation);
                if (schema != null) {
                    parameter.setSchema(schema);
                }
            }
            if (annotation instanceof io.swagger.oas.annotations.Parameter) {
                io.swagger.oas.annotations.Parameter p = (io.swagger.oas.annotations.Parameter) annotation;
                if (StringUtils.isNotBlank(p.description())) {
                    parameter.setDescription(p.description());
                }
                if (StringUtils.isNotBlank(p.name())) {
                    parameter.setName(p.name());
                }
                if (StringUtils.isNotBlank(p.in())) {
                    parameter.setIn(p.in());
                }
                if (p.deprecated()) {
                    parameter.setDeprecated(p.deprecated());
                }
                if (p.required()) {
                    parameter.setRequired(p.required());
                }
                if (p.allowEmptyValue()) {
                    parameter.setAllowEmptyValue(p.allowEmptyValue());
                }
                if (p.allowReserved()) {
                    parameter.setAllowReserved(p.allowReserved());
                }

                setParameterStyle(parameter, p);
                setParameterExplode(parameter, p);

                if (hasSchemaAnnotation(p.schema())) {
                    Schema schema = processSchema(p.schema());
                    if (schema != null) {
                        parameter.setSchema(schema);
                    }
                } else if (hasArrayAnnotation(p.array())) {
                 Schema arraySchema = processArraySchema(p.array());
                 if (arraySchema != null) {
                     parameter.setSchema(arraySchema);
                 }
                }
            } else if (annotation.annotationType().getName().equals("javax.ws.rs.PathParam")) {
                try {
                    String name = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                    if (StringUtils.isNotBlank(name)) {
                        parameter.setName(name);
                    }
                } catch (Exception e) {
                }
            } else if (annotation.annotationType().getName().equals("javax.validation.constraints.Size")) {
                try {
                    if (parameter.getSchema() == null) {
                        parameter.setSchema(new ArraySchema());
                    }
                    if (parameter.getSchema() instanceof ArraySchema) {
                        ArraySchema as = (ArraySchema) parameter.getSchema();
                        Integer min = (Integer) annotation.annotationType().getMethod("min").invoke(annotation);
                        if (min != null) {
                            as.setMinItems(min);
                        }
                        Integer max = (Integer) annotation.annotationType().getMethod("max").invoke(annotation);
                        if (max != null) {
                            as.setMaxItems(max);
                        }
                    }

                } catch (Exception e) {
                    LOGGER.error("failed on " + annotation.annotationType().getName(), e);
                }
            }
        }
        if (type != null) {
            Schema filled = fillSchema(parameter.getSchema(), type);
            if (filled != null) {
                parameter.setSchema(filled);
            }
        }
        final String defaultValue = helper.getDefaultValue();

        Schema paramSchema = parameter.getSchema();

        if (paramSchema != null) {
            if (paramSchema instanceof ArraySchema) {
                ArraySchema as = (ArraySchema) paramSchema;
                if (defaultValue != null) {
                    as.getItems().setDefault(defaultValue);
                }
            } else {
                if (defaultValue != null) {
                    paramSchema.setDefault(defaultValue);
                }
            }
        }
        return parameter;
    }
    
    private static boolean hasArrayAnnotation(io.swagger.oas.annotations.media.ArraySchema array) {
         if (array.uniqueItems() == false
                 && array.maxItems() == Integer.MIN_VALUE
                 && array.minItems() == Integer.MAX_VALUE
                 && !hasSchemaAnnotation(array.schema())
                 ) {
             return false;
         }
         return true;
     }
      
    private static Schema processArraySchema(io.swagger.oas.annotations.media.ArraySchema array) {
         ArraySchema output = new ArraySchema();
 
        Schema schema = processSchema(array.schema());
 
         output.setItems(schema);
 
         return output;
     }    
    
    public static void setParameterExplode(Parameter parameter, io.swagger.oas.annotations.Parameter p) {
        if (isExplodable(p)) {
            if (Explode.TRUE.equals(p.explode())) {
                parameter.setExplode(Boolean.TRUE);
            } else if (Explode.FALSE.equals(p.explode())) {
                parameter.setExplode(Boolean.FALSE);
            }
        }
    }

    private static boolean isExplodable(io.swagger.oas.annotations.Parameter p) {
        io.swagger.oas.annotations.media.Schema schema = p.schema();
        boolean explode = true;
        if (schema != null) {
            Class implementation = schema.implementation();
            if (implementation == Void.class) {
                if (!schema.type().equals("object") && !schema.type().equals("array")) {
                    explode = false;
                }
            }
        }
        return explode;
    }

    public static void setParameterStyle(Parameter parameter, io.swagger.oas.annotations.Parameter p) {
        if (StringUtils.isNotBlank(p.style())) {
            parameter.setStyle(Parameter.StyleEnum.valueOf(p.style().toUpperCase()));
        }
    }

    public static Schema fillSchema(Schema schema, Type type) {
        if (schema != null) {
            if (schema != null && StringUtils.isBlank(schema.getType())) {
                PrimitiveType pt = PrimitiveType.fromType(type);
                if (pt != null) {
                    Schema inner = pt.createProperty();
                    return merge(schema, inner);
                } else {
                    return ModelConverters.getInstance().resolveProperty(type);
                }
            } else if ("array".equals(schema.getType())) {
                Schema inner = fillSchema(((ArraySchema) schema).getItems(), type);
                ArraySchema as = (ArraySchema) schema;
                as.setItems(inner);
                as.setMinItems(schema.getMinItems());
                as.setMaxItems(schema.getMaxItems());
                return as;
            }
        } else {
            PrimitiveType pt = PrimitiveType.fromType(type);
            if (pt != null) {
                Schema inner = pt.createProperty();
                return merge(schema, inner);
            } else {
                return ModelConverters.getInstance().resolveProperty(type);
            }
        }
        return schema;
    }

    public static Schema merge(Schema from, Schema to) {
        if (from == null) {
            return to;
        }
        if (to.getDefault() == null) {
            to.setDefault(from.getDefault());
        }
        if (to.getDeprecated() == null) {
            to.setDeprecated(from.getDeprecated());
        }
        if (to.getDescription() == null) {
            to.setDescription(from.getDescription());
        }
        if (to.getEnum() == null) {
            to.setEnum(from.getEnum());
        }
        if (to.getExample() == null) {
            to.setExample(from.getExample());
        }
        if (to.getExclusiveMaximum() == null) {
            to.setExclusiveMaximum(from.getExclusiveMaximum());
        }
        if (to.getExclusiveMinimum() == null) {
            to.setExclusiveMinimum(from.getExclusiveMinimum());
        }
        if (to.getExtensions() == null) {
            to.setExtensions(from.getExtensions());
        }
        if (to.getExternalDocs() == null) {
            to.setExternalDocs(from.getExternalDocs());
        }
        if (to.getFormat() == null) {
            to.setFormat(from.getFormat());
        }
        if (to.getMaximum() == null) {
            to.setMaximum(from.getMaximum());
        }
        if (to.getMaxLength() == null) {
            to.setMaxLength(from.getMaxLength());
        }
        if (to.getMinimum() == null) {
            to.setMinimum(from.getMinimum());
        }
        if (to.getMinLength() == null) {
            to.setMinLength(from.getMinLength());
        }
        if (to.getMultipleOf() == null) {
            to.setMultipleOf(from.getMultipleOf());
        }
        if (to.getNullable() == null) {
            to.setNullable(from.getNullable());
        }
        if (to.getPattern() == null) {
            to.setPattern(from.getPattern());
        }
        if (to.getReadOnly() == null) {
            to.setReadOnly(from.getReadOnly());
        }
        if (to.getRequired() == null) {
            to.setRequired(from.getRequired());
        }
        if (to.getTitle() == null) {
            to.setTitle(from.getTitle());
        }
        if (to.getXml() == null) {
            to.setXml(from.getXml());
        }
        if (to.getWriteOnly() == null) {
            to.setWriteOnly(from.getWriteOnly());
        }
        return to;
    }

    private static boolean hasSchemaAnnotation(io.swagger.oas.annotations.media.Schema schema) {
        if (StringUtils.isBlank(schema.type())
                && StringUtils.isBlank(schema.format())
                && StringUtils.isBlank(schema.title())
                && StringUtils.isBlank(schema.description())
                && StringUtils.isBlank(schema.ref())
                && StringUtils.isBlank(schema.name())
                && schema.multipleOf() == 0
                && StringUtils.isBlank(schema.maximum())
                && StringUtils.isBlank(schema.minimum())
                && !schema.exclusiveMinimum()
                && !schema.exclusiveMaximum()
                && schema.maxLength() == Integer.MIN_VALUE
                && schema.minLength() == Integer.MAX_VALUE
                && schema.minProperties() == 0
                && schema.maxProperties() == 0
                && schema.requiredProperties().length == 1 && StringUtils.isBlank(schema.requiredProperties()[0])
                && !schema.required()
                && !schema.nullable()
                && !schema.readOnly()
                && !schema.writeOnly()
                && schema.examples().length == 1 && StringUtils.isBlank(schema.examples()[0])
                && !schema.deprecated()
                && schema.allowableValues().length == 1 && StringUtils.isBlank(schema.allowableValues()[0])
                && StringUtils.isBlank(schema.defaultValue())
                && StringUtils.isBlank(schema.example())
                && StringUtils.isBlank(schema.pattern())
                && schema.not().equals(Void.class)
                && schema.oneOf().length == 1 && schema.oneOf()[0].equals(Void.class)
                && schema.anyOf().length == 1 && schema.anyOf()[0].equals(Void.class)
                ) {
            return false;
        }
        return true;
    }

    private static Schema processSchema(io.swagger.oas.annotations.media.Schema schema) {
        Schema output = null;
        if (schema.type() != null) {
            if ("integer".equals(schema.type())) {
                output = new IntegerSchema();
                if (StringUtils.isNotBlank(schema.format())) {
                    output.format(schema.format());
                }
            } else if ("string".equals(schema.type())) {
            	if ("password".equals(schema.format())) {
                    output = new PasswordSchema();
                } else if ("binary".equals(schema.format())) {
                    output = new BinarySchema();
                } else if ("byte".equals(schema.format())) {
                    output = new ByteArraySchema();
                } else if ("date".equals(schema.format())) {
                    output = new DateSchema();
                } else if ("date-time".equals(schema.format())) {
                    output = new DateTimeSchema();
                } else if ("email".equals(schema.format())) {
                    output = new EmailSchema();
                } else if ("uuid".equals(schema.format())) {
                    output = new UUIDSchema();
                } else {
                    output = new StringSchema();
                }
            } else {
                output = new Schema();
            }

            // TODO: #2312 other types
        }
        if (output != null) {
            if (StringUtils.isNotBlank(schema.defaultValue())) {
                output.setDefault(schema.defaultValue());
            }

            if (StringUtils.isNotBlank(schema.pattern())) {
                output.setPattern(schema.pattern());
            }
            if (StringUtils.isNotBlank(schema.format())) {
                output.setFormat(schema.format());
            }
            if (StringUtils.isNotBlank(schema.description())) {
                output.setDescription(schema.description());
            }
            if (schema.allowableValues() != null) {
                for (String v : schema.allowableValues()) {
                    if (StringUtils.isNotBlank(v)) {
                        output.addEnumItemObject(v);
                    }
                }
            }
            if (schema.exclusiveMinimum()) {
                output.exclusiveMinimum(true);
            }
            if (schema.exclusiveMaximum()) {
                output.exclusiveMaximum(true);
            }
            if (schema.readOnly()) {
                output.readOnly(true);
            }
            if (StringUtils.isNotBlank(schema.minimum())) {
                output.minimum(new BigDecimal(schema.minimum()));
            }
            if (StringUtils.isNotBlank(schema.maximum())) {
                output.maximum(new BigDecimal(schema.maximum()));
            }
            if (schema.minProperties() > 0) {
                output.minProperties(schema.minProperties());
            }
            if (schema.maxProperties() > 0) {
                output.maxProperties(schema.maxProperties());
            }
        }
         
        return output;
    }

    /**
     * The <code>AnnotationsHelper</code> class defines helper methods for
     * accessing supported parameter annotations.
     */
    private static class AnnotationsHelper {
        //        private static final ApiParam DEFAULT_API_PARAM = getDefaultApiParam(null);
        private boolean context;
        //        private ParamWrapper<?> apiParam = new ApiParamWrapper(DEFAULT_API_PARAM);
        private String type;
        private String format;
        private String defaultValue;
        private Integer minItems;
        private Integer maxItems;
        private Boolean required;
        private BigDecimal min;
        private boolean minExclusive = false;
        private BigDecimal max;
        private boolean maxExclusive = false;
        private Integer minLength;
        private Integer maxLength;
        private String pattern;
        private Boolean allowEmptyValue;
        private String collectionFormat;

        /**
         * Constructs an instance.
         *
         * @param annotations array or parameter annotations
         */
        public AnnotationsHelper(List<Annotation> annotations, Type _type) {
            String rsDefault = null;
            Size size = null;
            if (annotations != null) {
                for (Annotation item : annotations) {
                    if ("javax.ws.rs.core.Context".equals(item.annotationType().getName())) {
                        context = true;
                    } else if ("javax.ws.rs.DefaultValue".equals(item.annotationType().getName())) {
                        try {
                            rsDefault = (String) item.annotationType().getMethod("value").invoke(item);
                        } catch (Exception ex) {
                            LOGGER.error("Invocation of value method failed", ex);
                        }
                    } else if (item instanceof Size) {
                        size = (Size) item;
                        /**
                         * This annotation is handled after the loop, as the allow multiple field of the
                         * ApiParam annotation can affect how the Size annotation is translated
                         * Swagger property constraints
                         */
                    } else if (item instanceof NotNull) {
                        required = true;
                    } else if (item instanceof Min) {
                        min = new BigDecimal(((Min) item).value());
                    } else if (item instanceof Max) {
                        max = new BigDecimal(((Max) item).value());
                    } else if (item instanceof DecimalMin) {
                        DecimalMin decimalMinAnnotation = (DecimalMin) item;
                        min = new BigDecimal(decimalMinAnnotation.value());
                        minExclusive = !decimalMinAnnotation.inclusive();
                    } else if (item instanceof DecimalMax) {
                        DecimalMax decimalMaxAnnotation = (DecimalMax) item;
                        max = new BigDecimal(decimalMaxAnnotation.value());
                        maxExclusive = !decimalMaxAnnotation.inclusive();
                    } else if (item instanceof Pattern) {
                        pattern = ((Pattern) item).regexp();
                    }
                }
            }
            defaultValue = rsDefault;

        }

        private boolean isAssignableToNumber(Class<?> clazz) {
            return Number.class.isAssignableFrom(clazz)
                    || int.class.isAssignableFrom(clazz)
                    || short.class.isAssignableFrom(clazz)
                    || long.class.isAssignableFrom(clazz)
                    || float.class.isAssignableFrom(clazz)
                    || double.class.isAssignableFrom(clazz);
        }

        /**
         */
        public boolean isContext() {
            return context;
        }

        /**
         * Returns default value from annotation.
         *
         * @return default value from annotation
         */
        public String getDefaultValue() {
            return defaultValue;
        }

        public Integer getMinItems() {
            return minItems;
        }

        public Integer getMaxItems() {
            return maxItems;
        }

        public Boolean isRequired() {
            return required;
        }

        public BigDecimal getMax() {
            return max;
        }

        public boolean isMaxExclusive() {
            return maxExclusive;
        }

        public BigDecimal getMin() {
            return min;
        }

        public String getType() {
            return type;
        }

        public String getFormat() {
            return format;
        }

        public boolean isMinExclusive() {
            return minExclusive;
        }

        public Integer getMinLength() {
            return minLength;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        public String getPattern() {
            return pattern;
        }

        public Boolean getAllowEmptyValue() {
            return allowEmptyValue;
        }

        public String getCollectionFormat() {
            return collectionFormat;
        }
    }

}
