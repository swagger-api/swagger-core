package io.swagger.jaxrs2;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;
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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by RafaelLopez on 5/27/17.
 */
public class OperationParser {

    public static Optional<List<Parameter>> getParametersList(io.swagger.oas.annotations.Parameter[] parameters) {
        if (parameters == null) {
            return Optional.empty();
        }
        List<Parameter> parametersObject = new ArrayList<>();
        for (io.swagger.oas.annotations.Parameter parameter : parameters) {
            getParameter(parameter).ifPresent(parametersObject::add);

        }
        return Optional.of(parametersObject);
    }

    public static Optional<Parameter> getParameter(io.swagger.oas.annotations.Parameter parameter) {
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
        parameterObject.setDeprecated(parameter.deprecated());
        if (parameter.required()) {
            parameterObject.setRequired(parameter.required());
            isEmpty = false;
        }
        parameterObject.setStyle(StringUtils.isNoneBlank(parameter.style()) ? Parameter.StyleEnum.valueOf(parameter.style()) : null);
        if (parameter.allowEmptyValue()) {
            parameterObject.setAllowEmptyValue(parameter.allowEmptyValue());
            isEmpty = false;
        }
        if (parameter.allowReserved()) {
            parameterObject.setAllowReserved(parameter.allowReserved());
            isEmpty = false;
        }
        if (parameter.explode()) {
            parameterObject.setExplode(parameter.explode());
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }

        getContents(parameter.content()).ifPresent(parameterObject::setContent);

        return Optional.of(parameterObject);
    }

    public static Optional<Map<String, Schema>> getSchema(io.swagger.oas.annotations.media.Schema schema) {
        if (schema == null) {
            return Optional.empty();
        }
        if (schema.implementation() != Void.class) {
            return Optional.of(ModelConverters.getInstance().readAll(schema.implementation()));
        }
        return Optional.empty();
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

    public static Optional<RequestBody> getRequestBody(io.swagger.oas.annotations.parameters.RequestBody requestBody) {
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
        getContents(requestBody.content()).ifPresent(requestBodyObject::setContent);
        return Optional.of(requestBodyObject);
    }

    public static Optional<ApiResponses> getApiResponses(final io.swagger.oas.annotations.responses.ApiResponse[] responses, io.swagger.oas.annotations.links.Link links) {
        if (responses == null) {
            return Optional.empty();
        }
        ApiResponses apiResponsesObject = new ApiResponses();
        for (io.swagger.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponseObject = new ApiResponse();
            getContent(response.content()).ifPresent(apiResponseObject::content);
            if (StringUtils.isNotBlank(response.description())) {
                apiResponseObject.setDescription(response.description());
            }

            apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
        }
        return Optional.of(apiResponsesObject);
    }

    public static Optional<Content> getContents(io.swagger.oas.annotations.media.Content[] contents) {
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
        return Optional.of(contentObject);
    }

    public static Optional<Content> getContent(io.swagger.oas.annotations.media.Content annotationContent) {
        if (annotationContent == null) {
            return Optional.empty();
        }
        if (StringUtils.isBlank(annotationContent.mediaType())) {
            return Optional.empty();
        }
        Content content = new Content();
        if (annotationContent != null) {
            MediaType mediaType = new MediaType();
            Class<?> schemaImplementation = annotationContent.schema().implementation();
            if (schemaImplementation != Void.class) {
                Map<String, Schema> schemaMap = ModelConverters.getInstance().readAll(schemaImplementation);
                schemaMap.forEach((k, v) -> mediaType.setSchema(v));
                content.addMediaType(annotationContent.mediaType(), mediaType);
            }
            ExampleObject[] examples = annotationContent.examples();
            for (ExampleObject example : examples) {
                getMediaType(mediaType, example).ifPresent(mediaTypeObject -> content.addMediaType(annotationContent.mediaType(), mediaTypeObject));
            }
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
                exampleObject.setValue(example.value());
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
        Info infoObject = new Info();
        if (StringUtils.isNotBlank(info.description())) {
            infoObject.setDescription(info.description());
        }
        if (StringUtils.isNotBlank(info.termsOfService())) {
            infoObject.setTermsOfService(info.termsOfService());
        }
        if (StringUtils.isNotBlank(info.title())) {
            infoObject.setTitle(info.title());
        }
        if (StringUtils.isNotBlank(info.version())) {
            infoObject.setVersion(info.version());
        }
        getContact(info.contact()).ifPresent(infoObject::setContact);
        getLicense(info.license()).ifPresent(infoObject::setLicense);

        return Optional.of(infoObject);
    }

    public static Optional<Contact> getContact(io.swagger.oas.annotations.info.Contact contact) {
        if (contact == null) {
            return Optional.empty();
        }
        Contact contactObject = new Contact();
        if (StringUtils.isNotBlank(contact.email())) {
            contactObject.setEmail(contact.email());
        }
        if (StringUtils.isNotBlank(contact.name())) {
            contactObject.setName(contact.name());
        }
        if (StringUtils.isNotBlank(contact.url())) {
            contactObject.setUrl(contact.url());
        }
        return Optional.of(contactObject);
    }

    public static Optional<License> getLicense(io.swagger.oas.annotations.info.License license) {
        if (license == null) {
            return Optional.empty();
        }
        License licenseObject = new License();
        if (StringUtils.isNotBlank(license.name())) {
            licenseObject.setName(license.name());
        }
        if (StringUtils.isNotBlank(license.url())) {
            licenseObject.setUrl(license.url());
        }
        return Optional.of(licenseObject);
    }
}