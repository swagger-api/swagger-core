package io.swagger.jaxrs2;

import io.swagger.jaxrs2.util.ReaderUtils;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.links.Link;
import io.swagger.oas.models.links.LinkParameters;
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
import java.util.List;
import java.util.LinkedHashSet;
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
            Parameter parameterObject = new Parameter();
            parameterObject.setDescription(parameter.description());
            parameterObject.setDeprecated(parameter.deprecated());
            parameterObject.setName(parameter.name());
            parameterObject.setRequired(parameter.required());
            parameterObject.setStyle(StringUtils.isNoneBlank(parameter.style()) ? Parameter.StyleEnum.valueOf(parameter.style()) : null);
            parameterObject.setAllowEmptyValue(parameter.allowEmptyValue());
            parameterObject.setAllowReserved(parameter.allowReserved());
            parameterObject.setExplode(parameter.explode());
            parameterObject.setIn(parameter.in());
            parameterObject.setContent(getContents(parameter.content()).get());

            io.swagger.oas.annotations.media.Schema schema = parameter.schema();
            parameterObject.setSchema(getSchema(schema).get());
            parametersObject.add(parameterObject);

        }
        return Optional.of(parametersObject);
    }

    public static Optional<Schema> getSchema(io.swagger.oas.annotations.media.Schema schema) {
        if (schema == null) {
            return Optional.empty();
        }
        Schema schemaObject = new Schema();

        schemaObject.set$ref(schema.ref());
        schemaObject.setDefault(schema._default());
        schemaObject.setDeprecated(schema.deprecated());
        schemaObject.setDescription(schema.description());
        schemaObject.setEnum(ReaderUtils.getStringListFromStringArray(schema._enum()).get());
        schemaObject.setExample(schema.example());
        schemaObject.setExclusiveMaximum(schema.exclusiveMaximum());
        schemaObject.setExclusiveMinimum(schema.exclusiveMinimum());
        schemaObject.setExternalDocs(getExternalDocumentation(schema.externalDocs()).get());
        schemaObject.setFormat(schema.format());
        schemaObject.setPattern(schema.pattern());
        schemaObject.setMaxLength(schema.maxLength());
        schemaObject.setMaxProperties(schema.maxProperties());
        schemaObject.setMinLength(schema.minLength());
        schemaObject.setMinProperties(schema.minProperties());
        schemaObject.setReadOnly(schema.readOnly());
        schemaObject.setType(schema.type());
        schemaObject.setTitle(schema.title());
        schemaObject.setWriteOnly(schema.writeOnly());

        return Optional.of(schemaObject);
    }

    public static Optional<Set<Tag>> getTags(String[] tags) {
        if (tags == null) {
            return Optional.empty();
        }
        Set<Tag> tagsList = new LinkedHashSet<>();
        for (String tag : tags) {
            Tag tagObject = new Tag();
            tagObject.setDescription(tag);
            tagObject.setName(tag);
            tagsList.add(tagObject);
        }
        return Optional.of(tagsList);
    }

    public static Optional<List<Server>> getServers(io.swagger.oas.annotations.servers.Server[] servers) {
        if (servers == null) {
            return Optional.empty();
        }
        List<Server> serverObjects = new ArrayList<>();

        for (io.swagger.oas.annotations.servers.Server server : servers) {
            serverObjects.add(getServer(server).get());
        }
        return Optional.of(serverObjects);
    }

    public static Optional<Server> getServer(io.swagger.oas.annotations.servers.Server server) {
        if (server == null) {
            return Optional.empty();
        }

        Server serverObject = new Server();
        serverObject.setUrl(server.url());
        serverObject.setDescription(server.description());
        io.swagger.oas.annotations.servers.ServerVariable[] serverVariables = server.variables();
        ServerVariables serverVariablesObject = new ServerVariables();
        for (io.swagger.oas.annotations.servers.ServerVariable serverVariable : serverVariables) {
            ServerVariable serverVariableObject = new ServerVariable();
            serverVariableObject.setDescription(serverVariable.description());
            serverVariablesObject.addServerVariable(serverVariable.name(), serverVariableObject);
        }
        serverObject.setVariables(serverVariablesObject);

        return Optional.of(serverObject);
    }

    public static Optional<ExternalDocumentation> getExternalDocumentation(io.swagger.oas.annotations.ExternalDocumentation externalDocumentation) {
        if (externalDocumentation == null) {
            return Optional.empty();
        }
        ExternalDocumentation external = new ExternalDocumentation();
        external.setDescription(externalDocumentation.description());
        external.setUrl(externalDocumentation.url());
        return Optional.of(external);
    }

    public static Optional<RequestBody> getRequestBody(io.swagger.oas.annotations.parameters.RequestBody requestBody) {
        if (requestBody == null) {
            return Optional.empty();
        }
        RequestBody requestBodyObject = new RequestBody();
        requestBodyObject.setDescription(requestBody.description());
        requestBodyObject.setRequired(requestBody.required());
        requestBodyObject.setContent(getContents(requestBody.content()).get());
        return Optional.of(requestBodyObject);
    }

    public static Optional<ApiResponses> getApiResponses(final io.swagger.oas.annotations.responses.ApiResponse[] responses, io.swagger.oas.annotations.links.Link links) {
        if (responses == null) {
            return Optional.empty();
        }
        ApiResponses apiResponsesObject = new ApiResponses();
        for (io.swagger.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponseObject = new ApiResponse();
            Content content = getContent(response.content()).get();
            apiResponseObject.content(content);
            apiResponseObject.setDescription(response.description());
            getLink(links).ifPresent(link -> apiResponseObject.setLinks(link));

            apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
        }
        return Optional.of(apiResponsesObject);
    }

    public static Optional<Content> getContents(io.swagger.oas.annotations.media.Content[] contents) {
        if (contents == null) {
            return Optional.empty();
        }
        Content contentObject = new Content();
        for (io.swagger.oas.annotations.media.Content content : contents) {
            ExampleObject[] examples = content.examples();
            for (ExampleObject example : examples) {
               getMediaType(example).ifPresent(mediaType -> contentObject.addMediaType(content.mediaType(), mediaType));
            }
        }
        return Optional.of(contentObject);
    }

    public static Optional<Content> getContent(io.swagger.oas.annotations.media.Content annotationContent) {
        if (annotationContent == null) {
            Optional.empty();
        }
        Content content = new Content();
        if (annotationContent != null) {
            ExampleObject[] examples = annotationContent.examples();
            for (ExampleObject example : examples) {
                getMediaType(example).ifPresent(mediaType -> content.addMediaType(annotationContent.mediaType(), mediaType));
            }
        }
        return Optional.of(content);
    }

    public static Optional<MediaType> getMediaType(ExampleObject example) {
        if (example == null) {
            return Optional.empty();
        }
        MediaType mediaType = new MediaType();
        Example exampleObject = new Example();
        exampleObject.setDescription(example.name());
        exampleObject.setSummary(example.summary());
        exampleObject.setExternalValue(example.externalValue());
        exampleObject.setValue(example.value());
        mediaType.addExamples(example.name(), exampleObject);
        return Optional.of(mediaType);
    }

    public static Optional<Link> getLink(io.swagger.oas.annotations.links.Link link) {
        if (link == null) {
            return Optional.empty();
        }
        Link linkObject = new Link();
        linkObject.setParameters(getLinkParameters(link.parameters()).get());
        linkObject.setDescription(link.description());
        linkObject.setOperationId(link.operationId());
        linkObject.setOperationRef(link.operationRef());
        return Optional.of(linkObject);
    }

    public static Optional<LinkParameters> getLinkParameters(io.swagger.oas.annotations.links.LinkParameters linkParameters) {
        if (linkParameters == null) {
            return Optional.empty();
        }
        LinkParameters linkParametersObject = new LinkParameters();
        linkParametersObject.addExtension(linkParameters.name(), linkParameters.expression());
        return Optional.of(linkParametersObject);
    }
}
