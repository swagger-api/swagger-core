package io.swagger.jaxrs2;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs2.util.ReaderUtils;
import io.swagger.oas.annotations.enums.Explode;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.Components;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;
import io.swagger.oas.models.links.Link;
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
import io.swagger.util.ParameterProcessor;
import io.swagger.util.Json;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.Produces;
import java.io.IOException;
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

    public static final String RESPONSE_DEFAULT = "default";
    public static final String MEDIA_TYPE = "*/*";
    public static final String COMPONENTS_REF = "#/components/schemas/";
    public static final String DEFAULT_DESCRIPTION = "no description";

    public static Optional<List<Parameter>> getParametersList(io.swagger.oas.annotations.Parameter[] parameters, Components components) {
        if (parameters == null) {
            return Optional.empty();
        }
        List<Parameter> parametersObject = new ArrayList<>();
        for (io.swagger.oas.annotations.Parameter parameter : parameters) {
            getParameter(parameter, components).ifPresent(parametersObject::add);
        }
        if (parametersObject.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(parametersObject);
    }

    public static Optional<Parameter> getParameter(io.swagger.oas.annotations.Parameter parameter, Components components) {
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

        ParameterProcessor.setParameterStyle(parameterObject, parameter);
        ParameterProcessor.setParameterExplode(parameterObject, parameter);

        if (!Explode.DEFAULT.equals(parameter.explode())) {
            isEmpty = false;
        }
        if (parameter.schema() != null) {
            if (parameter.schema().implementation() == Void.class) {
                getSchemaFromAnnotation(parameter.schema()).ifPresent(schema -> {
                    if (StringUtils.isNotBlank(schema.getType())) {
                        parameterObject.setSchema(schema);
                        components.addSchemas(schema.getType(), schema);
                    }
                });
            }
        }
        if (isEmpty) {
            return Optional.empty();
        }

        getContents(parameter.content(), components).ifPresent(parameterObject::setContent);

        return Optional.of(parameterObject);
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
        if (StringUtils.isNotBlank(schema._default())) {
            schemaObject.setDefault(schema._default());
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

        ReaderUtils.getStringListFromStringArray(schema._enum()).ifPresent(schemaObject::setEnum);
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

    public static Optional<RequestBody> getRequestBody(io.swagger.oas.annotations.parameters.RequestBody requestBody, Components components) {
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
        getContents(requestBody.content(), components).ifPresent(requestBodyObject::setContent);
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
            getContent(response.content(), classProduces, methodProduces, components).ifPresent(apiResponseObject::content);
            if (StringUtils.isNotBlank(apiResponseObject.getDescription()) || apiResponseObject.getContent() != null) {

                Map<String, Link> links = getLinks(response.links());
                if (links.size() > 0) {
                    apiResponseObject.setLinks(links);
                }
                if (StringUtils.isNotBlank(response.responseCode())) {
                    apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
                } else {
                    apiResponsesObject.addApiResponse(RESPONSE_DEFAULT, apiResponseObject);
                }
            }
        }
        if (apiResponsesObject.isEmpty()) {
            ApiResponse apiResponseObject = new ApiResponse();
            apiResponseObject.setDescription(DEFAULT_DESCRIPTION);
            apiResponsesObject.addApiResponse(RESPONSE_DEFAULT, apiResponseObject);

        }
        return Optional.of(apiResponsesObject);
    }

    public static Optional<Content> getContents(io.swagger.oas.annotations.media.Content[] contents, Components components) {
        if (contents == null) {
            return Optional.empty();
        }
        Content contentObject = new Content();
        MediaType mediaType = new MediaType();
        for (io.swagger.oas.annotations.media.Content content : contents) {
            ExampleObject[] examples = content.examples();
            for (ExampleObject example : examples) {
                getMediaType(mediaType, example).ifPresent(mediaTypeObject -> contentObject.addMediaType(content.mediaType(), mediaType));
            }
        }
        if (contentObject.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(contentObject);
    }

    public static Optional<Content> getContent(io.swagger.oas.annotations.media.Content annotationContent, Produces classProduces, Produces methodProduces, Components components) {
        if (annotationContent == null) {
            return Optional.empty();
        }

        Content content = new Content();
        MediaType mediaType = new MediaType();
        Class<?> schemaImplementation = annotationContent.schema().implementation();
        Map<String, Schema> schemaMap;
        // TODO #2312 not handling primitive and string types correctly, as it creates
        // anyway a ref schema
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
            mediaType.setSchema(schemaObject);

        } else {
            getSchemaFromAnnotation(annotationContent.schema()).ifPresent(mediaType::setSchema);
        }
        if (StringUtils.isNotBlank(annotationContent.mediaType())) {
            content.addMediaType(annotationContent.mediaType(), mediaType);
        } else {
            if (mediaType.getSchema() != null) {
                if (methodProduces != null) {
                    for (String value : methodProduces.value()) {
                        content.addMediaType(value, mediaType);
                    }
                } else if (classProduces != null) {
                    for (String value : classProduces.value()) {
                        content.addMediaType(value, mediaType);
                    }
                } else {
                    content.addMediaType(MEDIA_TYPE, mediaType);
                }
            }
        }
        ExampleObject[] examples = annotationContent.examples();
        for (ExampleObject example : examples) {
            getMediaType(mediaType, example).ifPresent(mediaTypeObject -> content.addMediaType(annotationContent.mediaType(), mediaTypeObject));
        }
        if (content.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(content);
    }

    public static Optional<MediaType> getMediaType(MediaType mediaType, ExampleObject example) {
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

            mediaType.addExamples(example.name(), exampleObject);
            return Optional.of(mediaType);
        }
        return Optional.empty();
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
        }
        if (StringUtils.isNotBlank(link.operationRef())) {
            linkObject.setOperationRef(link.operationRef());
            isEmpty = false;
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

    public static Map<String, String> getLinkParameters(io.swagger.oas.annotations.links.LinkParameters
                                                                linkParameters) {
        Map<String, String> linkParametersMap = new HashMap<>();
        if (linkParameters == null) {
            return linkParametersMap;
        }
        if (StringUtils.isNotBlank(linkParameters.name())) {
            linkParametersMap.put(linkParameters.name(), linkParameters.expression());
        }

        return linkParametersMap;
    }
}
