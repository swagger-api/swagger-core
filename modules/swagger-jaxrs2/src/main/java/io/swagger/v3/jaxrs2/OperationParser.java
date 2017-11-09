package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.Map;
import java.util.Optional;


public class OperationParser {

    public static final String COMPONENTS_REF = "#/components/schemas/";

    public static Optional<RequestBody> getRequestBody(io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, Consumes classConsumes, Consumes methodConsumes, Components components) {
        if (requestBody == null) {
            return Optional.empty();
        }
        RequestBody requestBodyObject = new RequestBody();
        boolean isEmpty = true;
        if (StringUtils.isNotBlank(requestBody.description())) {
            requestBodyObject.setDescription(requestBody.description());
            isEmpty = false;
        }
        if (requestBody.required()) {
            requestBodyObject.setRequired(requestBody.required());
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }
        getContent(requestBody.content(), classConsumes == null ? new String[0] : classConsumes.value(),
                methodConsumes == null ? new String[0] : methodConsumes.value(), components).ifPresent(requestBodyObject::setContent);
        return Optional.of(requestBodyObject);
    }

    public static Optional<ApiResponses> getApiResponses(final io.swagger.v3.oas.annotations.responses.ApiResponse[] responses, Produces classProduces, Produces methodProduces, Components components) {
        if (responses == null) {
            return Optional.empty();
        }
        ApiResponses apiResponsesObject = new ApiResponses();
        for (io.swagger.v3.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponseObject = new ApiResponse();
            if (StringUtils.isNotBlank(response.description())) {
                apiResponseObject.setDescription(response.description());
            }
            getContent(response.content(), classProduces == null ? new String[0] : classProduces.value(),
                    methodProduces == null ? new String[0] : methodProduces.value(), components).ifPresent(apiResponseObject::content);
            AnnotationsUtils.getHeaders(response.headers()).ifPresent(apiResponseObject::headers);
            if (StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null || apiResponseObject.getHeaders() != null) {

                Map<String, Link> links = AnnotationsUtils.getLinks(response.links());
                if (links.size() > 0) {
                    apiResponseObject.setLinks(links);
                }
                if (StringUtils.isNotBlank(response.responseCode())) {
                    apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
                } else {
                    apiResponsesObject._default(apiResponseObject);
                }
            }
        }
        if (apiResponsesObject.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(apiResponsesObject);
    }

    public static Optional<Content> getContent(io.swagger.v3.oas.annotations.media.Content[] annotationContents, String[] classTypes, String[] methodTypes, Components components) {
        if (annotationContents == null) {
            return Optional.empty();
        }

        //Encapsulating Content model
        Content content = new Content();

        for (io.swagger.v3.oas.annotations.media.Content annotationContent : annotationContents) {
            MediaType mediaType = new MediaType();
            getSchema(annotationContent, components).ifPresent(mediaType::setSchema);

            ExampleObject[] examples = annotationContent.examples();
            for (ExampleObject example : examples) {
                AnnotationsUtils.getExample(example).ifPresent(exampleObject -> mediaType.addExamples(example.name(), exampleObject));
            }
            Encoding[] encodings = annotationContent.encoding();
            for (Encoding encoding : encodings) {
                AnnotationsUtils.addEncodingToMediaType(mediaType, encoding);
            }
            if (StringUtils.isNotBlank(annotationContent.mediaType())) {
                content.addMediaType(annotationContent.mediaType(), mediaType);
            } else {
                if (mediaType.getSchema() != null) {
                    AnnotationsUtils.applyTypes(classTypes, methodTypes, content, mediaType);
                }
            }
        }
        if (content.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(content);
    }

    public static Optional<? extends Schema> getSchema(io.swagger.v3.oas.annotations.media.Content annotationContent, Components components) {
        Class<?> schemaImplementation = annotationContent.schema().implementation();
        boolean isArray = false;
        if (schemaImplementation == Void.class) {
            schemaImplementation = annotationContent.array().schema().implementation();
            if (schemaImplementation != Void.class) {
                isArray = true;
            }
        }
        Map<String, Schema> schemaMap;
        if (schemaImplementation != Void.class) {
            Schema schemaObject = new Schema();
            if (schemaImplementation.getName().startsWith("java.lang")) {
                schemaObject.setType(schemaImplementation.getSimpleName().toLowerCase());
            } else {
                schemaMap = ModelConverters.getInstance().readAll(schemaImplementation);
                schemaMap.forEach((key, schema) -> {
                    components.addSchemas(key, schema);
                });
                schemaObject.set$ref(COMPONENTS_REF + schemaImplementation.getSimpleName());
            }
            if (StringUtils.isBlank(schemaObject.get$ref()) && StringUtils.isBlank(schemaObject.getType())) {
                // default to string
                schemaObject.setType("string");
            }
            if (isArray) {
                Optional<ArraySchema> arraySchema = AnnotationsUtils.getArraySchema(annotationContent.array());
                if (arraySchema.isPresent()) {
                    arraySchema.get().setItems(schemaObject);
                    return arraySchema;
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.of(schemaObject);
            }

        } else {
            Optional<Schema> schemaFromAnnotation = AnnotationsUtils.getSchemaFromAnnotation(annotationContent.schema());
            if (schemaFromAnnotation.isPresent()) {
                if (StringUtils.isBlank(schemaFromAnnotation.get().get$ref()) && StringUtils.isBlank(schemaFromAnnotation.get().getType())) {
                    // default to string
                    schemaFromAnnotation.get().setType("string");
                }
                return Optional.of(schemaFromAnnotation.get());
            } else {
                Optional<ArraySchema> arraySchemaFromAnnotation = AnnotationsUtils.getArraySchema(annotationContent.array());
                if (arraySchemaFromAnnotation.isPresent()) {
                    if (StringUtils.isBlank(arraySchemaFromAnnotation.get().getItems().get$ref()) && StringUtils.isBlank(arraySchemaFromAnnotation.get().getItems().getType())) {
                        // default to string
                        arraySchemaFromAnnotation.get().getItems().setType("string");
                    }
                    return Optional.of(arraySchemaFromAnnotation.get());
                }
            }
        }
        return Optional.empty();
    }

}
