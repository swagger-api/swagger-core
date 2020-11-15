package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParameterProcessor {
    static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);

    public static Parameter applyAnnotations(
            Parameter parameter,
            Type type,
            List<Annotation> annotations,
            Components components,
            String[] classTypes,
            String[] methodTypes,
            JsonView jsonViewAnnotation) {

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
        AnnotatedType annotatedType = new AnnotatedType()
                .type(type)
                .resolveAsRef(true)
                .skipOverride(true)
                .jsonViewAnnotation(jsonViewAnnotation)
                .ctxAnnotations(reworkedAnnotations.toArray(new Annotation[reworkedAnnotations.size()]));
        ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(annotatedType);

        if (resolvedSchema.schema != null) {
            parameter.setSchema(resolvedSchema.schema);
        }
        resolvedSchema.referencedSchemas.forEach(components::addSchemas);

        // handle first FormParam as it affects Explode resolving
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals("javax.ws.rs.FormParam")) {
                try {
                    String name = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                    if (StringUtils.isNotBlank(name)) {
                        parameter.setName(name);
                    }
                } catch (Exception e) {
                }
                // set temporarily to "form" to inform caller that we need to further process along other form parameters
                parameter.setIn("form");
            } else if (annotation.annotationType().getName().endsWith("FormDataParam")) {
                try {
                    String name = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                    if (StringUtils.isNotBlank(name)) {
                        parameter.setName(name);
                    }
                } catch (Exception e) {
                }
                // set temporarily to "form" to inform caller that we need to further process along other form parameters
                parameter.setIn("form");
            }
        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof io.swagger.v3.oas.annotations.Parameter) {
                io.swagger.v3.oas.annotations.Parameter p = (io.swagger.v3.oas.annotations.Parameter) annotation;
                if (p.hidden()) {
                    return null;
                }
                if (StringUtils.isNotBlank(p.ref())) {
                    parameter = new Parameter().$ref(p.ref());
                    return parameter;
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

                Map<String, Example> exampleMap = new LinkedHashMap<>();
                if (p.examples().length == 1 && StringUtils.isBlank(p.examples()[0].name())) {
                    Optional<Example> exampleOptional = AnnotationsUtils.getExample(p.examples()[0], true);
                    if (exampleOptional.isPresent()) {
                        parameter.setExample(exampleOptional.get());
                    }
                } else {
                    for (ExampleObject exampleObject : p.examples()) {
                        AnnotationsUtils.getExample(exampleObject).ifPresent(example -> exampleMap.put(exampleObject.name(), example));
                    }
                }
                if (exampleMap.size() > 0) {
                    parameter.setExamples(exampleMap);
                }

                if (p.extensions().length > 0) {
                    Map<String, Object> extensionMap = AnnotationsUtils.getExtensions(p.extensions());
                    if (extensionMap != null && ! extensionMap.isEmpty()) {
                        extensionMap.forEach(parameter::addExtension);
                    }
                }

                Optional<Content> content = AnnotationsUtils.getContent(p.content(), classTypes, methodTypes, parameter.getSchema(), null, jsonViewAnnotation);
                if (content.isPresent()) {
                    parameter.setContent(content.get());
                    parameter.setSchema(null);
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
            } else if (ModelResolver.NOT_NULL_ANNOTATIONS.contains(annotation.annotationType().getSimpleName())) {
                parameter.setRequired(true);
            }
        }
        final String defaultValue = helper.getDefaultValue();

        Schema paramSchema = parameter.getSchema();
        if (paramSchema == null) {
            if (parameter.getContent() != null && !parameter.getContent().values().isEmpty()) {
                paramSchema = parameter.getContent().values().iterator().next().getSchema();
            }
        }
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

    public static void setParameterExplode(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
        if (isExplodable(p, parameter)) {
            if (Explode.TRUE.equals(p.explode())) {
                parameter.setExplode(Boolean.TRUE);
            } else if (Explode.FALSE.equals(p.explode())) {
                parameter.setExplode(Boolean.FALSE);
            }
        }
    }

    private static boolean isExplodable(io.swagger.v3.oas.annotations.Parameter p, Parameter parameter) {
        io.swagger.v3.oas.annotations.media.Schema schema = p.schema();
        boolean explode = true;
        if ("form".equals(parameter.getIn())){
            return true;
        }
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

    public static void setParameterStyle(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
        if (StringUtils.isNotBlank(p.style().toString())) {
            parameter.setStyle(Parameter.StyleEnum.valueOf(p.style().toString().toUpperCase()));
        }
    }

    public static Annotation getParamSchemaAnnotation(List<Annotation> annotations) {
        if (annotations == null) {
            return null;
        }
        io.swagger.v3.oas.annotations.media.Schema rootSchema = null;
        io.swagger.v3.oas.annotations.media.ArraySchema rootArraySchema = null;
        io.swagger.v3.oas.annotations.media.Schema contentSchema = null;
        io.swagger.v3.oas.annotations.media.Schema paramSchema = null;
        io.swagger.v3.oas.annotations.media.ArraySchema paramArraySchema = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof io.swagger.v3.oas.annotations.media.Schema) {
                rootSchema = (io.swagger.v3.oas.annotations.media.Schema) annotation;
            } else if (annotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema) {
                rootArraySchema = (io.swagger.v3.oas.annotations.media.ArraySchema) annotation;
            } else if (annotation instanceof io.swagger.v3.oas.annotations.Parameter) {
                io.swagger.v3.oas.annotations.Parameter paramAnnotation = (io.swagger.v3.oas.annotations.Parameter) annotation;
                if (paramAnnotation.content().length > 0) {
                    if (AnnotationsUtils.hasSchemaAnnotation(paramAnnotation.content()[0].schema())) {
                        contentSchema = paramAnnotation.content()[0].schema();
                    }
                }
                if (AnnotationsUtils.hasSchemaAnnotation(paramAnnotation.schema())) {
                    paramSchema = paramAnnotation.schema();
                }
                if (AnnotationsUtils.hasArrayAnnotation(paramAnnotation.array())) {
                    paramArraySchema = paramAnnotation.array();
                }
            }
        }
        if (rootSchema != null || rootArraySchema != null) {
            return null;
        }
        if (contentSchema != null) {
            return contentSchema;
        }
        if (paramSchema != null) {
            return paramSchema;
        }
        if (paramArraySchema != null) {
            return paramArraySchema;
        }
        return null;
    }

    public static Type getParameterType(io.swagger.v3.oas.annotations.Parameter paramAnnotation) {
        return getParameterType(paramAnnotation, false);
    }
    public static Type getParameterType(io.swagger.v3.oas.annotations.Parameter paramAnnotation, boolean nullIfNotFound) {
        if (paramAnnotation == null) {
            return null;
        }
        io.swagger.v3.oas.annotations.media.Schema contentSchema = null;
        io.swagger.v3.oas.annotations.media.Schema paramSchema = null;
        io.swagger.v3.oas.annotations.media.ArraySchema paramArraySchema = null;

        if (paramAnnotation.content().length > 0) {
            if (AnnotationsUtils.hasSchemaAnnotation(paramAnnotation.content()[0].schema())) {
                contentSchema = paramAnnotation.content()[0].schema();
            }
        }
        if (AnnotationsUtils.hasSchemaAnnotation(paramAnnotation.schema())) {
            paramSchema = paramAnnotation.schema();
        }
        if (AnnotationsUtils.hasArrayAnnotation(paramAnnotation.array())) {
            paramArraySchema = paramAnnotation.array();
        }
        if (contentSchema != null) {
            return AnnotationsUtils.getSchemaType(contentSchema, nullIfNotFound);
        }
        if (paramSchema != null) {
            return AnnotationsUtils.getSchemaType(paramSchema, nullIfNotFound);
        }
        if (paramArraySchema != null) {
            return AnnotationsUtils.getSchemaType(paramArraySchema.schema(), nullIfNotFound);
        }
        if (nullIfNotFound) {
            return null;
        }
        return String.class;
    }

    public static final String MEDIA_TYPE = "*/*";

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
