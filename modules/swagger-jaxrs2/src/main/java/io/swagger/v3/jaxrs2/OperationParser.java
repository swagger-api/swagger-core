package io.swagger.v3.jaxrs2;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.Map;
import java.util.Optional;

public class OperationParser {

    public static final String COMPONENTS_REF = Components.COMPONENTS_SCHEMAS_REF;

    public static Optional<RequestBody> getRequestBody(io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, Consumes classConsumes, Consumes methodConsumes, Components components, JsonView jsonViewAnnotation) {
        return getRequestBody(requestBody, classConsumes, methodConsumes, components, jsonViewAnnotation, false);
    }
    public static Optional<RequestBody> getRequestBody(io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, Consumes classConsumes, Consumes methodConsumes, Components components, JsonView jsonViewAnnotation, boolean openapi31) {
        if (requestBody == null) {
            return Optional.empty();
        }
        RequestBody requestBodyObject = new RequestBody();
        boolean isEmpty = true;

        if (StringUtils.isNotBlank(requestBody.ref())) {
            requestBodyObject.set$ref(requestBody.ref());
            return Optional.of(requestBodyObject);
        }

        if (StringUtils.isNotBlank(requestBody.description())) {
            requestBodyObject.setDescription(requestBody.description());
            isEmpty = false;
        }
        if (requestBody.required()) {
            requestBodyObject.setRequired(requestBody.required());
            isEmpty = false;
        }
        if (requestBody.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(openapi31, requestBody.extensions());
            if (extensions != null) {
                extensions.forEach(requestBodyObject::addExtension);
            }
            isEmpty = false;
        }

        if (requestBody.content().length > 0) {
            isEmpty = false;
        }

        if (isEmpty) {
            return Optional.empty();
        }
        AnnotationsUtils.getContent(requestBody.content(), classConsumes == null ? new String[0] : classConsumes.value(),
                methodConsumes == null ? new String[0] : methodConsumes.value(), null, components, jsonViewAnnotation, openapi31).ifPresent(requestBodyObject::setContent);
        return Optional.of(requestBodyObject);
    }

    public static Optional<ApiResponses> getApiResponses(final io.swagger.v3.oas.annotations.responses.ApiResponse[] responses, Produces classProduces, Produces methodProduces, Components components, JsonView jsonViewAnnotation) {
        return getApiResponses(responses, classProduces, methodProduces, components, jsonViewAnnotation, false, ApiResponses.DEFAULT);
    }

    public static Optional<ApiResponses> getApiResponses(final io.swagger.v3.oas.annotations.responses.ApiResponse[] responses, Produces classProduces, Produces methodProduces, Components components, JsonView jsonViewAnnotation, boolean openapi31, String defaultResponseKey) {
        if (responses == null) {
            return Optional.empty();
        }
        ApiResponses apiResponsesObject = new ApiResponses();
        for (io.swagger.v3.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponseObject = new ApiResponse();
            if (StringUtils.isNotBlank(response.ref())) {
                apiResponseObject.set$ref(response.ref());
                if (StringUtils.isNotBlank(response.responseCode())) {
                    apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
                } else {
                    apiResponsesObject._default(apiResponseObject);
                }
                continue;
            }
            if (StringUtils.isNotBlank(response.description())) {
                apiResponseObject.setDescription(response.description());
            }
            if (response.extensions().length > 0) {
                Map<String, Object> extensions = AnnotationsUtils.getExtensions(openapi31, response.extensions());
                if (extensions != null) {
                    extensions.forEach(apiResponseObject::addExtension);
                }
            }

            AnnotationsUtils.getContent(response.content(), classProduces == null ? new String[0] : classProduces.value(),
                    methodProduces == null ? new String[0] : methodProduces.value(), null, components, jsonViewAnnotation, openapi31).ifPresent(apiResponseObject::content);
            AnnotationsUtils.getHeaders(response.headers(), components, jsonViewAnnotation).ifPresent(apiResponseObject::headers);
            if (StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null || apiResponseObject.getHeaders() != null) {

                Map<String, Link> links = AnnotationsUtils.getLinks(response.links());
                if (links.size() > 0) {
                    apiResponseObject.setLinks(links);
                }
                if (StringUtils.isNotBlank(response.responseCode())) {
                    apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
                } else {
                    apiResponsesObject.addApiResponse(defaultResponseKey, apiResponseObject);
                }
            }
        }

        if (apiResponsesObject.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(apiResponsesObject);
    }

}
