package io.swagger.jaxrs2;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs2.util.ReaderUtils;
import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.annotations.links.LinkParameter;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.Components;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;
import io.swagger.oas.models.links.Link;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.servers.Server;
import io.swagger.oas.models.servers.ServerVariable;
import io.swagger.oas.models.servers.ServerVariables;
import io.swagger.oas.models.tags.Tag;
import io.swagger.util.Json;
import io.swagger.util.ParameterProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by RafaelLopez on 5/27/17.
 */
public class OperationParser {
    private static Logger LOGGER = LoggerFactory.getLogger(OperationParser.class);

    public static final String MEDIA_TYPE = "*/*";
    public static final String COMPONENTS_REF = "#/components/schemas/";
    public static final String DEFAULT_DESCRIPTION = "no description";
    public static final String COMMA = ",";

    public static Optional<List<Parameter>> getParametersList(io.swagger.oas.annotations.Parameter[] parameters, Produces classProduces, Produces methodProduces, Components components) {
        if (parameters == null) {
            return Optional.empty();
        }
        List<Parameter> parametersObject = new ArrayList<>();
        for (io.swagger.oas.annotations.Parameter parameter : parameters) {
            getParameter(parameter, classProduces, methodProduces, components).ifPresent(parametersObject::add);
        }
        if (parametersObject.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(parametersObject);
    }

    public static Optional<Parameter> getParameter(io.swagger.oas.annotations.Parameter parameter, Produces classProduces, Produces methodProduces, Components components) {
        if (parameter == null) {
            return Optional.empty();
        }
        Parameter parameterObject = new Parameter();
        boolean isEmpty = true;
        if (StringUtils.isNotBlank(parameter.description())) {
            parameterObject.setDescription(parameter.description());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(parameter.name())) {
            parameterObject.setName(parameter.name());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(parameter.in())) {
            parameterObject.setIn(parameter.in());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(parameter.example())) {
            parameterObject.setExample(parameter.example());
            isEmpty = false;
        }
        if (parameter.deprecated()) {
            parameterObject.setDeprecated(parameter.deprecated());
        }
        if (parameter.required()) {
            parameterObject.setRequired(parameter.required());
            isEmpty = false;
        }
        if (parameter.allowEmptyValue()) {
            parameterObject.setAllowEmptyValue(parameter.allowEmptyValue());
            isEmpty = false;
        }
        if (parameter.allowReserved()) {
            parameterObject.setAllowReserved(parameter.allowReserved());
            isEmpty = false;
        }

        Map<String, Example> exampleMap = new HashMap<>();
        for (ExampleObject exampleObject : parameter.examples()) {
            ParameterProcessor.getExample(exampleObject).ifPresent(example -> exampleMap.put(exampleObject.name(), example));
        }

        if (exampleMap.size() > 0) {
            parameterObject.setExamples(exampleMap);
        }

        ParameterProcessor.setParameterStyle(parameterObject, parameter);
        ParameterProcessor.setParameterExplode(parameterObject, parameter);

        if (!Explode.DEFAULT.equals(parameter.explode())) {
            isEmpty = false;
        }
        getContent(parameter.content(), classProduces == null ? new String[0] : classProduces.value(),
                methodProduces == null ? new String[0] : methodProduces.value(), components).ifPresent(parameterObject::setContent);
        if (parameterObject.getContent() == null) {
            getArraySchema(parameter.array()).ifPresent(parameterObject::setSchema);
            if (parameterObject.getSchema() == null) {
                if (parameter.schema().implementation() == Void.class) {
                    getSchemaFromAnnotation(parameter.schema()).ifPresent(schema -> {
                        if (StringUtils.isNotBlank(schema.getType())) {
                            parameterObject.setSchema(schema);
                            components.addSchemas(schema.getType(), schema);
                        }
                    });
                }
            }
        }
        if (isEmpty) {
            return Optional.empty();
        }

        return Optional.of(parameterObject);
    }

    public static Optional<ArraySchema> getArraySchema(io.swagger.oas.annotations.media.ArraySchema arraySchema) {
        if (arraySchema == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        ArraySchema arraySchemaObject = new ArraySchema();
        if (arraySchema.uniqueItems()) {
            arraySchemaObject.setUniqueItems(arraySchema.uniqueItems());
            isEmpty = false;
        }
        if (arraySchema.maxItems() > 0) {
            arraySchemaObject.setMaxItems(arraySchema.maxItems());
            isEmpty = false;
        }

        if (arraySchema.minItems() < Integer.MAX_VALUE) {
            arraySchemaObject.setMinItems(arraySchema.minItems());
            isEmpty = false;
        }

        if (arraySchema.schema() != null) {
            if (arraySchema.schema().implementation() == Void.class) {
                getSchemaFromAnnotation(arraySchema.schema()).ifPresent(schema -> {
                    if (StringUtils.isNotBlank(schema.getType())) {
                        arraySchemaObject.setItems(schema);
                    }
                });
            }
        }

        if (isEmpty) {
            return Optional.empty();
        }

        return Optional.of(arraySchemaObject);
    }

    public static Optional<Schema> getSchemaFromAnnotation(io.swagger.oas.annotations.media.Schema schema) {
        if (schema == null) {
            return Optional.empty();
        }
        Schema schemaObject = new Schema();
        boolean isEmpty = true;
        if (StringUtils.isNotBlank(schema.description())) {
            schemaObject.setDescription(schema.description());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(schema.ref())) {
            schemaObject.set$ref(schema.ref());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(schema.type())) {
            schemaObject.setType(schema.type());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(schema.defaultValue())) {
            schemaObject.setDefault(schema.defaultValue());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(schema.example())) {
            schemaObject.setExample(schema.example());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(schema.format())) {
            schemaObject.setFormat(schema.format());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(schema.example())) {
            schemaObject.setExample(schema.example());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(schema.pattern())) {
            schemaObject.setPattern(schema.pattern());
            isEmpty = false;
        }
        if (schema.readOnly()) {
            schemaObject.setReadOnly(schema.readOnly());
            isEmpty = false;
        }
        if (schema.deprecated()) {
            schemaObject.setDeprecated(schema.deprecated());
            isEmpty = false;
        }
        if (schema.exclusiveMaximum()) {
            schemaObject.setExclusiveMaximum(schema.exclusiveMaximum());
            isEmpty = false;
        }
        if (schema.exclusiveMinimum()) {
            schemaObject.setExclusiveMinimum(schema.exclusiveMinimum());
            isEmpty = false;
        }
        if (schema.maxLength() > 0) {
            if (schema.maxProperties() > 0) {
                schemaObject.setMaxProperties(schema.maxProperties());
                isEmpty = false;
            }
        }
        if (schema.minProperties() > 0) {
            schemaObject.setMinProperties(schema.minProperties());
            isEmpty = false;
        }

        if (NumberUtils.isNumber(schema.maximum())) {
            String filteredMaximum = schema.maximum().replaceAll(COMMA, StringUtils.EMPTY);
            schemaObject.setMaximum(new BigDecimal(filteredMaximum));
        }

        if (NumberUtils.isNumber(schema.minimum())) {
            String filteredMinimum = schema.minimum().replaceAll(COMMA, StringUtils.EMPTY);
            schemaObject.setMinimum(new BigDecimal(filteredMinimum));
        }

        ReaderUtils.getStringListFromStringArray(schema.allowableValues()).ifPresent(schemaObject::setEnum);
        getExternalDocumentation(schema.externalDocs()).ifPresent(schemaObject::setExternalDocs);

        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(schemaObject);
    }

    public static Optional<Set<Tag>> getTags(String[] tags) {
        if (tags == null) {
            return Optional.empty();
        }
        Set<Tag> tagsList = new LinkedHashSet<>();
        boolean isEmpty = true;
        for (String tag : tags) {
            Tag tagObject = new Tag();
            if (StringUtils.isNotBlank(tag)) {
                isEmpty = false;
            }
            tagObject.setDescription(tag);
            tagObject.setName(tag);
            tagsList.add(tagObject);
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(tagsList);
    }

    public static Optional<List<Server>> getServers(io.swagger.oas.annotations.servers.Server[] servers) {
        if (servers == null) {
            return Optional.empty();
        }
        List<Server> serverObjects = new ArrayList<>();
        for (io.swagger.oas.annotations.servers.Server server : servers) {
            getServer(server).ifPresent(serverObjects::add);
        }
        if (serverObjects.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(serverObjects);
    }

    public static Optional<Server> getServer(io.swagger.oas.annotations.servers.Server server) {
        if (server == null) {
            return Optional.empty();
        }

        Server serverObject = new Server();
        boolean isEmpty = true;
        if (StringUtils.isNotBlank(server.url())) {
            serverObject.setUrl(server.url());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(server.description())) {
            serverObject.setDescription(server.description());
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }
        io.swagger.oas.annotations.servers.ServerVariable[] serverVariables = server.variables();
        ServerVariables serverVariablesObject = new ServerVariables();
        for (io.swagger.oas.annotations.servers.ServerVariable serverVariable : serverVariables) {
            ServerVariable serverVariableObject = new ServerVariable();
            if (StringUtils.isNotBlank(serverVariable.description())) {
                serverVariableObject.setDescription(serverVariable.description());
            }
            serverVariablesObject.addServerVariable(serverVariable.name(), serverVariableObject);
        }
        serverObject.setVariables(serverVariablesObject);

        return Optional.of(serverObject);
    }

    public static Optional<ExternalDocumentation> getExternalDocumentation(io.swagger.oas.annotations.ExternalDocumentation externalDocumentation) {
        if (externalDocumentation == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        ExternalDocumentation external = new ExternalDocumentation();
        if (StringUtils.isNotBlank(externalDocumentation.description())) {
            isEmpty = false;
            external.setDescription(externalDocumentation.description());
        }
        if (StringUtils.isNotBlank(externalDocumentation.url())) {
            isEmpty = false;
            external.setUrl(externalDocumentation.url());
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(external);
    }

    public static Optional<RequestBody> getRequestBody(io.swagger.oas.annotations.parameters.RequestBody requestBody, Consumes classConsumes, Consumes methodConsumes, Components components) {
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

    public static Optional<ApiResponses> getApiResponses(final io.swagger.oas.annotations.responses.ApiResponse[] responses, Produces classProduces, Produces methodProduces, Components components) {
        if (responses == null) {
            return Optional.empty();
        }
        ApiResponses apiResponsesObject = new ApiResponses();
        for (io.swagger.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponseObject = new ApiResponse();
            if (StringUtils.isNotBlank(response.description())) {
                apiResponseObject.setDescription(response.description());
            }
            getContent(response.content(), classProduces == null ? new String[0] : classProduces.value(),
                    methodProduces == null ? new String[0] : methodProduces.value(), components).ifPresent(apiResponseObject::content);
            if (StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null) {

                Map<String, Link> links = getLinks(response.links());
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
            ApiResponse apiResponseObject = new ApiResponse();
            apiResponseObject.setDescription(DEFAULT_DESCRIPTION);
            apiResponsesObject._default(apiResponseObject);

        }
        return Optional.of(apiResponsesObject);
    }

    public static Optional<Content> getContent(io.swagger.oas.annotations.media.Content[] annotationContents, String[] classTypes, String[] methodTypes, Components components) {
        if (annotationContents == null) {
            return Optional.empty();
        }

        //Encapsulating Content model
        Content content = new Content();

        for (io.swagger.oas.annotations.media.Content annotationContent : annotationContents) {
            MediaType mediaType = new MediaType();
            getSchema(annotationContent, components).ifPresent(mediaType::setSchema);

            ExampleObject[] examples = annotationContent.examples();
            for (ExampleObject example : examples) {
                ParameterProcessor.getExample(example).ifPresent(exampleObject -> mediaType.addExamples(example.name(), exampleObject));
            }
            if (StringUtils.isNotBlank(annotationContent.mediaType())) {
                content.addMediaType(annotationContent.mediaType(), mediaType);
            } else {
                if (mediaType.getSchema() != null) {
                    applyTypes(classTypes, methodTypes, content, mediaType);
                }
            }
        }
        if (content.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(content);
    }

    public static Optional<Schema> getSchema(io.swagger.oas.annotations.media.Content annotationContent, Components components) {
        Class<?> schemaImplementation = annotationContent.schema().implementation();
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
            return Optional.of(schemaObject);

        } else {
            Optional<Schema> schemaFromAnnotation = getSchemaFromAnnotation(annotationContent.schema());
            if (schemaFromAnnotation.isPresent()) {
                return Optional.of(schemaFromAnnotation.get());
            }
        }
        return Optional.empty();
    }

    public static void applyTypes(String[] classTypes, String[] methodTypes, Content content, MediaType mediaType) {
        if (methodTypes.length > 0) {
            for (String value : methodTypes) {
                content.addMediaType(value, mediaType);
            }
        } else if (classTypes.length > 0) {
            for (String value : classTypes) {
                content.addMediaType(value, mediaType);
            }
        } else {
            content.addMediaType(MEDIA_TYPE, mediaType);
        }

    }

    public static Optional<Info> getInfo(io.swagger.oas.annotations.info.Info info) {
        if (info == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        Info infoObject = new Info();
        if (StringUtils.isNotBlank(info.description())) {
            infoObject.setDescription(info.description());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(info.termsOfService())) {
            infoObject.setTermsOfService(info.termsOfService());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(info.title())) {
            infoObject.setTitle(info.title());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(info.version())) {
            infoObject.setVersion(info.version());
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }
        getContact(info.contact()).ifPresent(infoObject::setContact);
        getLicense(info.license()).ifPresent(infoObject::setLicense);

        return Optional.of(infoObject);
    }

    public static Optional<Contact> getContact(io.swagger.oas.annotations.info.Contact contact) {
        if (contact == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        Contact contactObject = new Contact();
        if (StringUtils.isNotBlank(contact.email())) {
            contactObject.setEmail(contact.email());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(contact.name())) {
            contactObject.setName(contact.name());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(contact.url())) {
            contactObject.setUrl(contact.url());
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(contactObject);
    }

    public static Optional<License> getLicense(io.swagger.oas.annotations.info.License license) {
        if (license == null) {
            return Optional.empty();
        }
        License licenseObject = new License();
        boolean isEmpty = true;
        if (StringUtils.isNotBlank(license.name())) {
            licenseObject.setName(license.name());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(license.url())) {
            licenseObject.setUrl(license.url());
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(licenseObject);
    }

    public static Map<String, Link> getLinks(io.swagger.oas.annotations.links.Link[] links) {
        Map<String, Link> linkMap = new HashMap<>();
        if (links == null) {
            return linkMap;
        }
        for (io.swagger.oas.annotations.links.Link link : links) {
            getLink(link).ifPresent(linkResult -> linkMap.put(link.name(), linkResult));
        }
        return linkMap;
    }

    public static Optional<Link> getLink(io.swagger.oas.annotations.links.Link link) {
        if (link == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        Link linkObject = new Link();
        if (StringUtils.isNotBlank(link.description())) {
            linkObject.setDescription(link.description());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(link.operationId())) {
            linkObject.setOperationId(link.operationId());
            isEmpty = false;
            if (StringUtils.isNotBlank(link.operationRef())) {
                LOGGER.debug("OperationId and OperatonRef are mutually exclusive, there must be only one setted");
            }
        } else {
            if (StringUtils.isNotBlank(link.operationRef())) {
                linkObject.setOperationRef(link.operationRef());
                isEmpty = false;
            }
        }
        if (isEmpty) {
            return Optional.empty();
        }
        Map<String, String> linkParameters = getLinkParameters(link.parameters());
        if (linkParameters.size() > 0) {
            linkObject.setParameters(linkParameters);
        }
        return Optional.of(linkObject);
    }

    public static Map<String, String> getLinkParameters(LinkParameter[]
                                                                linkParameter) {
        Map<String, String> linkParametersMap = new HashMap<>();
        if (linkParameter == null) {
            return linkParametersMap;
        }
        for (LinkParameter parameter : linkParameter) {
            if (StringUtils.isNotBlank(parameter.name())) {
                linkParametersMap.put(parameter.name(), parameter.expression());
            }
        }

        return linkParametersMap;
    }
}
