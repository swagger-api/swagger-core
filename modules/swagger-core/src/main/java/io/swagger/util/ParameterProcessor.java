package io.swagger.util;

import io.swagger.converter.ModelConverters;
import io.swagger.converter.ResolvedSchema;
import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.Components;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParameterProcessor {
    static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);


    public static Parameter applyAnnotations(Parameter parameter, Type type, List<Annotation> annotations, Components components, String[] classTypes, String[] methodTypes) {


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
        ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAnnotatedType(type, reworkedAnnotations, "");

        if (resolvedSchema.schema != null) {
            parameter.setSchema(resolvedSchema.schema);
        }
        resolvedSchema.referencedSchemas.forEach((key, schema) -> components.addSchemas(key, schema));

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
                    AnnotationsUtils.getExample(exampleObject).ifPresent(example -> exampleMap.put(exampleObject.name(), example));
                }
                if (exampleMap.size() > 0) {
                    parameter.setExamples(exampleMap);
                }

                Optional<Content> content = getContent(p.content(), classTypes, methodTypes, parameter.getSchema());
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
            }
        }
        final String defaultValue = helper.getDefaultValue();

        Schema paramSchema = parameter.getSchema();
        if (paramSchema == null) {
            if (parameter.getContent() != null && parameter.getContent().values().size() > 0) {
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


    public static Annotation getParamSchemaAnnotation(List<Annotation> annotations) {
        if (annotations == null) {
            return null;
        }
        io.swagger.oas.annotations.media.Schema rootSchema = null;
        io.swagger.oas.annotations.media.ArraySchema rootArraySchema = null;
        io.swagger.oas.annotations.media.Schema contentSchema = null;
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

    public static Type getParameterType(io.swagger.oas.annotations.Parameter paramAnnotation) {
        if (paramAnnotation == null) {
            return null;
        }
        io.swagger.oas.annotations.media.Schema contentSchema = null;
        io.swagger.oas.annotations.media.Schema paramSchema = null;
        io.swagger.oas.annotations.media.ArraySchema paramArraySchema = null;

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
            return getSchemaType(contentSchema);
        }
        if (paramSchema != null) {
            return getSchemaType(paramSchema);
        }
        if (paramArraySchema != null) {
            return getSchemaType(paramArraySchema.schema());
        }
        return String.class;
    }

    public static Type getSchemaType(io.swagger.oas.annotations.media.Schema schema) {
        if (schema == null) {
            return String.class;
        }
        String schemaType = schema.type();
        Class schemaImplementation = schema.implementation();

        if (!schemaImplementation.equals(Void.class)) {
            return schemaImplementation;
        } else if (StringUtils.isBlank(schemaType)) {
            return String.class;
        }
        switch (schemaType) {
            case "number":
                return BigDecimal.class;
            case "integer":
                return Long.class;
            case "boolean":
                return Boolean.class;
            default:
                return String.class;
        }
    }


    public static Optional<Content> getContent(io.swagger.oas.annotations.media.Content[] annotationContents, String[] classTypes, String[] methodTypes, Schema schema) {
        if (annotationContents == null || annotationContents.length == 0) {
            return Optional.empty();
        }

        //Encapsulating Content model
        Content content = new Content();

        io.swagger.oas.annotations.media.Content annotationContent = annotationContents[0];
        MediaType mediaType = new MediaType();
        mediaType.setSchema(schema);

        ExampleObject[] examples = annotationContent.examples();
        for (ExampleObject example : examples) {
            AnnotationsUtils.getExample(example).ifPresent(exampleObject -> mediaType.addExamples(example.name(), exampleObject));
        }
        io.swagger.oas.annotations.media.Encoding[] encodings = annotationContent.encoding();
        for (io.swagger.oas.annotations.media.Encoding encoding : encodings) {
            AnnotationsUtils.addEncodingToMediaType(mediaType, encoding);
        }
        if (StringUtils.isNotBlank(annotationContent.mediaType())) {
            content.addMediaType(annotationContent.mediaType(), mediaType);
        } else {
            if (mediaType.getSchema() != null) {
                applyTypes(classTypes, methodTypes, content, mediaType);
            }
        }
        if (content.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(content);
    }

    public static final String MEDIA_TYPE = "*/*";

    public static void applyTypes(String[] classTypes, String[] methodTypes, Content content, MediaType mediaType) {
        if (methodTypes != null && methodTypes.length > 0) {
            for (String value : methodTypes) {
                content.addMediaType(value, mediaType);
            }
        } else if (classTypes != null && classTypes.length > 0) {
            for (String value : classTypes) {
                content.addMediaType(value, mediaType);
            }
        } else {
            content.addMediaType(MEDIA_TYPE, mediaType);
        }

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
