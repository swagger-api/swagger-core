package io.swagger.util;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        // first handle schema
        List<Annotation> reworkedAnnotations = new ArrayList<>(annotations);
        Annotation paramSchemaOrArrayAnnotation = getParamSchemaAnnotation(annotations);
        if (paramSchemaOrArrayAnnotation != null) {
            reworkedAnnotations.add(paramSchemaOrArrayAnnotation);
        }
        Schema resolvedSchema = ModelConverters.getInstance().resolveAnnotatedType(type, reworkedAnnotations, "");

        if (resolvedSchema != null) {
            parameter.setSchema(resolvedSchema);
        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof io.swagger.oas.annotations.Parameter) {
                io.swagger.oas.annotations.Parameter p = (io.swagger.oas.annotations.Parameter) annotation;
                if (p.hidden()) {
                    return null;
                }
                if (StringUtils.isNotBlank(p.description())) {
                    parameter.setDescription(p.description());
                }
                if (StringUtils.isNotBlank(p.name())) {
                    parameter.setName(p.name());
                }
                if (StringUtils.isNotBlank(p.in().toString())) {
                    parameter.setIn(p.in().toString());
                }
                if (StringUtils.isNotBlank(p.example())) {
                    try {
                        parameter.setExample(Json.mapper().readTree(p.example()));
                    } catch (IOException e) {
                        parameter.setExample(p.example());
                    }
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

                Map<String, Example> exampleMap = new HashMap<>();
                for (ExampleObject exampleObject : p.examples()) {
                    getExample(exampleObject).ifPresent(example -> exampleMap.put(exampleObject.name(), example));
                }
                if (exampleMap.size() > 0) {
                    parameter.setExamples(exampleMap);
                }

                setParameterStyle(parameter, p);
                setParameterExplode(parameter, p);

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

    public static Optional<Example> getExample(ExampleObject example) {
        if (example == null) {
            return Optional.empty();
        }
        if (StringUtils.isNotBlank(example.name())) {
            Example exampleObject = new Example();
            if (StringUtils.isNotBlank(example.name())) {
                exampleObject.setDescription(example.name());
            }
            if (StringUtils.isNotBlank(example.summary())) {
                exampleObject.setSummary(example.summary());
            }
            if (StringUtils.isNotBlank(example.externalValue())) {
                exampleObject.setExternalValue(example.externalValue());
            }
            if (StringUtils.isNotBlank(example.value())) {
                try {
                    exampleObject.setValue(Json.mapper().readTree(example.value()));
                } catch (IOException e) {
                    exampleObject.setValue(example.value());
                }
            }
            return Optional.of(exampleObject);
        }
        return Optional.empty();
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
        if (StringUtils.isNotBlank(p.style().toString())) {
            parameter.setStyle(Parameter.StyleEnum.valueOf(p.style().toString().toUpperCase()));
        }
    }

    private static boolean hasSchemaAnnotation(io.swagger.oas.annotations.media.Schema schema) {
        if (schema == null) {
            return false;
        }
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

    public static Annotation getParamSchemaAnnotation(List<Annotation> annotations) {
        if (annotations == null) {
            return null;
        }
        io.swagger.oas.annotations.media.Schema rootSchema = null;
        io.swagger.oas.annotations.media.ArraySchema rootArraySchema = null;
        io.swagger.oas.annotations.media.Schema paramSchema = null;
        io.swagger.oas.annotations.media.ArraySchema paramArraySchema = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof io.swagger.oas.annotations.media.Schema) {
                rootSchema = (io.swagger.oas.annotations.media.Schema) annotation;
            }
            else if (annotation instanceof io.swagger.oas.annotations.media.ArraySchema) {
                rootArraySchema = (io.swagger.oas.annotations.media.ArraySchema) annotation;
            }
            else if (annotation instanceof io.swagger.oas.annotations.Parameter) {
                io.swagger.oas.annotations.Parameter paramAnnotation = (io.swagger.oas.annotations.Parameter)annotation;
                if(hasSchemaAnnotation(paramAnnotation.schema())) {
                    paramSchema = paramAnnotation.schema();
                }
                else if(hasArrayAnnotation(paramAnnotation.array())) {
                    paramArraySchema = paramAnnotation.array();
                }
            }
        }
        if (rootSchema != null || rootArraySchema != null) {
            return null;
        }
        if (paramSchema != null) {
            return paramSchema;
        }
        if (paramArraySchema != null) {
            return paramArraySchema;
        }
        return null;
    }

    /**
     * The <code>AnnotationsHelper</code> class defines helper methods for
     * accessing supported parameter annotations.
     */
    private static class AnnotationsHelper {
        private boolean context;
        private String defaultValue;

        /**
         * Constructs an instance.
         *
         * @param annotations array or parameter annotations
         */
        public AnnotationsHelper(List<Annotation> annotations, Type _type) {
            String rsDefault = null;
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
                        // TODO verify if resolved correctly by resolver
                    }
                }
            }
            defaultValue = rsDefault;

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
    }
}
