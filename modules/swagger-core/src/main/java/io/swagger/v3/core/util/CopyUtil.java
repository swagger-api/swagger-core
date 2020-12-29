package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.media.XML;
import io.swagger.v3.oas.models.parameters.CookieParameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import io.swagger.v3.oas.models.tags.Tag;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public abstract class CopyUtil {

    private static Object copy(Object o) {
        if (o == null) {
            return null;
        }
        // Immutable types
        if (o instanceof String || o instanceof UUID || o instanceof Enum || o instanceof Boolean
                || o instanceof Integer || o instanceof BigDecimal || o instanceof Float || o instanceof Double) {
            return o;
        }
        // Mutable types
        else if (o instanceof JsonNode) {
            return ((JsonNode) o).deepCopy();
        } else if (o instanceof Date) {
            return ((Date) o).clone();
        } else if (o instanceof byte[]) {
            return ((byte[]) o).clone();
        }
        try {
            return Json.mapper().readTree(Json.pretty(o));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static ApiResponse copy(ApiResponse response) {
        return copy(response, CopyUtil::copy);
    }

    public static ApiResponse copy(ApiResponse response, Function<Object, Object> objectCopy) {
        if (response == null) {
            return null;
        }
        ApiResponse newResponse = new ApiResponse()
                .description(response.getDescription())
                .content(copy(response.getContent(), objectCopy))
                .$ref(response.get$ref());

        if (response.getHeaders() != null) {
            newResponse.setHeaders(new LinkedHashMap<>());
            response.getHeaders().forEach((k, v) -> newResponse.addHeaderObject(k, copy(v, objectCopy)));
        }
        if (response.getLinks() != null) {
            newResponse.setLinks(new LinkedHashMap<>());
            response.getLinks().forEach((k, v) -> newResponse.link(k, copy(v, objectCopy)));
        }
        if (response.getExtensions() != null) {
            newResponse.setExtensions(new LinkedHashMap<>());
            response.getExtensions().forEach((k, v) -> newResponse.addExtension(k, objectCopy.apply(v)));
        }
        return newResponse;
    }

    public static ApiResponses copy(ApiResponses responses) {
        return copy(responses, CopyUtil::copy);
    }

    public static ApiResponses copy(ApiResponses responses, Function<Object, Object> objectCopy) {
        if (responses == null) {
            return null;
        }
        ApiResponses newResponses = new ApiResponses();
        responses.forEach((k, v) -> newResponses.addApiResponse(k, copy(v, objectCopy)));
        if (responses.getExtensions() != null) {
            newResponses.setExtensions(new LinkedHashMap<>());
            responses.getExtensions().forEach((k, v) -> newResponses.addExtension(k, objectCopy.apply(v)));
        }
        return newResponses;
    }

    public static Callback copy(Callback callback) {
        return copy(callback, CopyUtil::copy);
    }

    public static Callback copy(Callback callback, Function<Object, Object> objectCopy) {
        if (callback == null) {
            return null;
        }
        Callback newCallback = new Callback()
                .$ref(callback.get$ref());

        callback.forEach((k, v) -> newCallback.addPathItem(k, copy(v, objectCopy)));

        if (callback.getExtensions() != null) {
            newCallback.setExtensions(new LinkedHashMap<>());
            callback.getExtensions().forEach((k, v) -> newCallback.addExtension(k, objectCopy.apply(v)));
        }
        return newCallback;
    }

    public static Components copy(Components components) {
        return copy(components, CopyUtil::copy);
    }

    public static Components copy(Components components, Function<Object, Object> objectCopy) {
        if (components == null) {
            return null;
        }
        Components newComponents = new Components();

        if (components.getSchemas() != null) {
            newComponents.setSchemas(new LinkedHashMap<>());
            components.getSchemas().forEach((k, v) -> newComponents.addSchemas(k, copy(v, objectCopy)));
        }
        if (components.getResponses() != null) {
            newComponents.setResponses(new LinkedHashMap<>());
            components.getResponses().forEach((k, v) -> newComponents.addResponses(k, copy(v, objectCopy)));
        }
        if (components.getParameters() != null) {
            newComponents.setParameters(new LinkedHashMap<>());
            components.getParameters().forEach((k, v) -> newComponents.addParameters(k, copy(v, objectCopy)));
        }
        if (components.getExamples() != null) {
            newComponents.setExamples(new LinkedHashMap<>());
            components.getExamples().forEach((k, v) -> newComponents.addExamples(k, copy(v, objectCopy)));
        }
        if (components.getRequestBodies() != null) {
            newComponents.setRequestBodies(new LinkedHashMap<>());
            components.getRequestBodies().forEach((k, v) -> newComponents.addRequestBodies(k, copy(v, objectCopy)));
        }
        if (components.getHeaders() != null) {
            newComponents.setHeaders(new LinkedHashMap<>());
            components.getHeaders().forEach((k, v) -> newComponents.addHeaders(k, copy(v, objectCopy)));
        }
        if (components.getSecuritySchemes() != null) {
            newComponents.setSecuritySchemes(new LinkedHashMap<>());
            components.getSecuritySchemes().forEach((k, v) -> newComponents.addSecuritySchemes(k, copy(v, objectCopy)));
        }
        if (components.getLinks() != null) {
            newComponents.setLinks(new LinkedHashMap<>());
            components.getLinks().forEach((k, v) -> newComponents.addLinks(k, copy(v, objectCopy)));
        }
        if (components.getCallbacks() != null) {
            newComponents.setCallbacks(new LinkedHashMap<>());
            components.getCallbacks().forEach((k, v) -> newComponents.addCallbacks(k, copy(v, objectCopy)));
        }
        if (components.getExtensions() != null) {
            newComponents.setExtensions(new LinkedHashMap<>());
            components.getExtensions().forEach((k, v) -> newComponents.addExtension(k, objectCopy.apply(v)));
        }
        return newComponents;
    }

    public static Contact copy(Contact contact) {
        return copy(contact, CopyUtil::copy);
    }

    public static Contact copy(Contact contact, Function<Object, Object> objectCopy) {
        if (contact == null) {
            return null;
        }
        Contact newContact = new Contact()
                .name(contact.getName())
                .url(contact.getUrl())
                .email(contact.getEmail());

        if (contact.getExtensions() != null) {
            newContact.setExtensions(new LinkedHashMap<>());
            contact.getExtensions().forEach((k, v) -> newContact.addExtension(k, objectCopy.apply(v)));
        }
        return newContact;
    }

    public static Content copy(Content content) {
        return copy(content, CopyUtil::copy);
    }

    public static Content copy(Content content, Function<Object, Object> objectCopy) {
        if (content == null) {
            return null;
        }
        Content newContent = new Content();
        content.forEach((k, v) -> newContent.addMediaType(k, copy(v, objectCopy)));
        return newContent;
    }

    public static Discriminator copy(Discriminator discriminator) {
        if (discriminator == null) {
            return null;
        }
        Discriminator newDiscriminator = new Discriminator()
                .propertyName(discriminator.getPropertyName());

        if (discriminator.getMapping() != null) {
            newDiscriminator.setMapping(new LinkedHashMap<>());
            discriminator.getMapping().forEach(newDiscriminator::mapping);
        }
        return newDiscriminator;
    }

    public static Encoding copy(Encoding encoding) {
        return copy(encoding, CopyUtil::copy);
    }

    public static Encoding copy(Encoding encoding, Function<Object, Object> objectCopy) {
        if (encoding == null) {
            return null;
        }
        Encoding newEncoding = new Encoding()
                .contentType(encoding.getContentType())
                .style(encoding.getStyle())
                .explode(encoding.getExplode())
                .allowReserved(encoding.getAllowReserved());

        if (encoding.getHeaders() != null) {
            newEncoding.setHeaders(new LinkedHashMap<>());
            encoding.getHeaders().forEach((k, v) -> newEncoding.addHeader(k, copy(v, objectCopy)));
        }
        if (encoding.getExtensions() != null) {
            newEncoding.setExtensions(new LinkedHashMap<>());
            encoding.getExtensions().forEach((k, v) -> newEncoding.addExtension(k, objectCopy.apply(v)));
        }
        return newEncoding;
    }

    public static Example copy(Example example) {
        return copy(example, CopyUtil::copy);
    }

    public static Example copy(Example example, Function<Object, Object> objectCopy) {
        if (example == null) {
            return null;
        }
        Example newExample = new Example()
                .summary(example.getSummary())
                .description(example.getDescription())
                .value(objectCopy.apply(example.getValue()))
                .externalValue(example.getExternalValue())
                .$ref(example.get$ref());

        if (example.getExtensions() != null) {
            newExample.setExtensions(new LinkedHashMap<>());
            example.getExtensions().forEach((k, v) -> newExample.addExtension(k, objectCopy.apply(v)));
        }
        return newExample;
    }

    public static ExternalDocumentation copy(ExternalDocumentation documentation) {
        return copy(documentation, CopyUtil::copy);
    }

    public static ExternalDocumentation copy(ExternalDocumentation documentation, Function<Object, Object> objectCopy) {
        if (documentation == null) {
            return null;
        }
        ExternalDocumentation newDocumentation = new ExternalDocumentation()
                .description(documentation.getDescription())
                .url(documentation.getUrl());

        if (documentation.getExtensions() != null) {
            newDocumentation.setExtensions(new LinkedHashMap<>());
            documentation.getExtensions().forEach((k, v) -> newDocumentation.addExtension(k, objectCopy.apply(v)));
        }
        return newDocumentation;
    }

    public static Header copy(Header header) {
        return copy(header, CopyUtil::copy);
    }

    public static Header copy(Header header, Function<Object, Object> objectCopy) {
        if (header == null) {
            return null;
        }
        Header newHeader = new Header()
                .description(header.getDescription())
                .$ref(header.get$ref())
                .required(header.getRequired())
                .deprecated(header.getDeprecated())
                .style(header.getStyle())
                .explode(header.getExplode())
                .schema(copy(header.getSchema(), objectCopy))
                .example(objectCopy.apply(header.getExample()))
                .content(copy(header.getContent(), objectCopy));

        if (header.getExtensions() != null) {
            newHeader.setExtensions(new LinkedHashMap<>());
            header.getExtensions().forEach((k, v) -> newHeader.addExtension(k, objectCopy.apply(v)));
        }
        return newHeader;
    }

    public static Info copy(Info info) {
        return copy(info, CopyUtil::copy);
    }

    public static Info copy(Info info, Function<Object, Object> objectCopy) {
        if (info == null) {
            return null;
        }
        Info newInfo = new Info()
                .title(info.getTitle())
                .description(info.getDescription())
                .termsOfService(info.getTermsOfService())
                .contact(copy(info.getContact(), objectCopy))
                .license(copy(info.getLicense(), objectCopy))
                .version(info.getVersion());

        if (info.getExtensions() != null) {
            newInfo.setExtensions(new LinkedHashMap<>());
            info.getExtensions().forEach((k, v) -> newInfo.addExtension(k, objectCopy.apply(v)));
        }
        return newInfo;
    }

    public static License copy(License license) {
        return copy(license, CopyUtil::copy);
    }

    public static License copy(License license, Function<Object, Object> objectCopy) {
        if (license == null) {
            return null;
        }
        License newLicense = new License()
                .name(license.getName())
                .url(license.getUrl());

        if (license.getExtensions() != null) {
            newLicense.setExtensions(new LinkedHashMap<>());
            license.getExtensions().forEach((k, v) -> newLicense.addExtension(k, objectCopy.apply(v)));
        }
        return newLicense;
    }

    public static Link copy(Link link) {
        return copy(link, CopyUtil::copy);
    }

    public static Link copy(Link link, Function<Object, Object> objectCopy) {
        if (link == null) {
            return null;
        }
        Link newLink = new Link()
                .operationRef(link.getOperationRef())
                .operationId(link.getOperationId())
                .requestBody(objectCopy.apply(link.getRequestBody()))
                .description(link.getDescription())
                .$ref(link.get$ref())
                .server(copy(link.getServer(), objectCopy));

        if (link.getParameters() != null) {
            newLink.setParameters(new LinkedHashMap<>());
            link.getParameters().forEach(newLink::parameters);
        }
        if (link.getHeaders() != null) {
            newLink.setHeaders(new LinkedHashMap<>());
            link.getHeaders().forEach((k, v) -> newLink.addHeaderObject(k, copy(v, objectCopy)));
        }
        if (link.getExtensions() != null) {
            newLink.setExtensions(new LinkedHashMap<>());
            link.getExtensions().forEach((k, v) -> newLink.addExtension(k, objectCopy.apply(v)));
        }
        return newLink;
    }

    public static MediaType copy(MediaType mediaType) {
        return copy(mediaType, CopyUtil::copy);
    }

    public static MediaType copy(MediaType mediaType, Function<Object, Object> objectCopy) {
        if (mediaType == null) {
            return null;
        }
        MediaType newMediaType = new MediaType()
                .schema(copy(mediaType.getSchema(), objectCopy))
                .example(objectCopy.apply(mediaType.getExample()));

        if (mediaType.getExamples() != null) {
            newMediaType.setExamples(new LinkedHashMap<>());
            mediaType.getExamples().forEach((k, v) -> newMediaType.addExamples(k, copy(v, objectCopy)));
        }
        if (mediaType.getEncoding() != null) {
            newMediaType.setEncoding(new LinkedHashMap<>());
            mediaType.getEncoding().forEach((k, v) -> newMediaType.addEncoding(k, copy(v, objectCopy)));
        }
        if (mediaType.getExtensions() != null) {
            newMediaType.setExtensions(new LinkedHashMap<>());
            mediaType.getExtensions().forEach((k, v) -> newMediaType.addExtension(k, objectCopy.apply(v)));
        }
        return newMediaType;
    }

    public static OAuthFlow copy(OAuthFlow flow) {
        return copy(flow, CopyUtil::copy);
    }

    public static OAuthFlow copy(OAuthFlow flow, Function<Object, Object> objectCopy) {
        if (flow == null) {
            return null;
        }
        OAuthFlow newFlow = new OAuthFlow()
                .authorizationUrl(flow.getAuthorizationUrl())
                .tokenUrl(flow.getTokenUrl())
                .refreshUrl(flow.getRefreshUrl())
                .scopes(copy(flow.getScopes(), objectCopy));

        if (flow.getExtensions() != null) {
            newFlow.setExtensions(new LinkedHashMap<>());
            flow.getExtensions().forEach((k, v) -> newFlow.addExtension(k, objectCopy.apply(v)));
        }
        return newFlow;
    }

    public static OAuthFlows copy(OAuthFlows flows) {
        return copy(flows, CopyUtil::copy);
    }

    public static OAuthFlows copy(OAuthFlows flows, Function<Object, Object> objectCopy) {
        if (flows == null) {
            return null;
        }
        OAuthFlows newFlows = new OAuthFlows()
                .implicit(copy(flows.getImplicit(), objectCopy))
                .password(copy(flows.getPassword(), objectCopy))
                .clientCredentials(copy(flows.getClientCredentials(), objectCopy))
                .authorizationCode(copy(flows.getAuthorizationCode(), objectCopy));

        if (flows.getExtensions() != null) {
            newFlows.setExtensions(new LinkedHashMap<>());
            flows.getExtensions().forEach((k, v) -> newFlows.addExtension(k, objectCopy.apply(v)));
        }
        return newFlows;
    }

    public static OpenAPI copy(OpenAPI openAPI) {
        return copy(openAPI, CopyUtil::copy);
    }

    public static OpenAPI copy(OpenAPI openAPI, Function<Object, Object> objectCopy) {
        if (openAPI == null) {
            return null;
        }
        OpenAPI newOpenAPI = new OpenAPI()
                .openapi(openAPI.getOpenapi())
                .info(copy(openAPI.getInfo(), objectCopy))
                .externalDocs(copy(openAPI.getExternalDocs(), objectCopy))
                .paths(copy(openAPI.getPaths(), objectCopy))
                .components(copy(openAPI.getComponents(), objectCopy));

        if (openAPI.getServers() != null) {
            newOpenAPI.setServers(new ArrayList<>());
            openAPI.getServers().forEach(v -> newOpenAPI.addServersItem(copy(v, objectCopy)));
        }
        if (openAPI.getSecurity() != null) {
            newOpenAPI.setSecurity(new ArrayList<>());
            openAPI.getSecurity().forEach(v -> newOpenAPI.addSecurityItem(copy(v)));
        }
        if (openAPI.getTags() != null) {
            newOpenAPI.setTags(new ArrayList<>());
            openAPI.getTags().forEach(v -> newOpenAPI.addTagsItem(copy(v, objectCopy)));
        }
        if (openAPI.getExtensions() != null) {
            newOpenAPI.setExtensions(new LinkedHashMap<>());
            openAPI.getExtensions().forEach((k, v) -> newOpenAPI.addExtension(k, objectCopy.apply(v)));
        }
        return newOpenAPI;
    }

    public static Operation copy(Operation operation) {
        return copy(operation, CopyUtil::copy);
    }

    public static Operation copy(Operation operation, Function<Object, Object> objectCopy) {
        if (operation == null) {
            return null;
        }
        Operation newOperation = new Operation()
                .summary(operation.getSummary())
                .description(operation.getDescription())
                .externalDocs(copy(operation.getExternalDocs(), objectCopy))
                .operationId(operation.getOperationId())
                .requestBody(copy(operation.getRequestBody(), objectCopy))
                .responses(copy(operation.getResponses(), objectCopy))
                .deprecated(operation.getDeprecated());

        if (operation.getTags() != null) {
            newOperation.setTags(new ArrayList<>());
            operation.getTags().forEach(newOperation::addTagsItem);
        }
        if (operation.getParameters() != null) {
            newOperation.setParameters(new ArrayList<>());
            operation.getParameters().forEach(v -> newOperation.addParametersItem(copy(v, objectCopy)));
        }
        if (operation.getCallbacks() != null) {
            newOperation.setCallbacks(new LinkedHashMap<>());
            operation.getCallbacks().forEach((k, v) -> newOperation.addCallbackItem(k, copy(v, objectCopy)));
        }
        if (operation.getSecurity() != null) {
            newOperation.setSecurity(new ArrayList<>());
            operation.getSecurity().forEach(v -> newOperation.addSecurityItem(copy(v)));
        }
        if (operation.getServers() != null) {
            newOperation.setServers(new ArrayList<>());
            operation.getServers().forEach(v -> newOperation.addServersItem(copy(v, objectCopy)));
        }
        if (operation.getExtensions() != null) {
            newOperation.setExtensions(new LinkedHashMap<>());
            operation.getExtensions().forEach((k, v) -> newOperation.addExtension(k, objectCopy.apply(v)));
        }
        return newOperation;
    }

    public static CookieParameter copy(CookieParameter parameter) {
        return copy(parameter, CopyUtil::copy);
    }

    public static CookieParameter copy(CookieParameter parameter, Function<Object, Object> objectCopy) {
        if (parameter == null) {
            return null;
        }
        CookieParameter newParameter = new CookieParameter();
        mapParameterProperties(parameter, newParameter, objectCopy);
        return newParameter;
    }

    public static PathParameter copy(PathParameter parameter) {
        return copy(parameter, CopyUtil::copy);
    }

    public static PathParameter copy(PathParameter parameter, Function<Object, Object> objectCopy) {
        if (parameter == null) {
            return null;
        }
        PathParameter newParameter = new PathParameter();
        mapParameterProperties(parameter, newParameter, objectCopy);
        return newParameter;
    }

    public static QueryParameter copy(QueryParameter parameter) {
        return copy(parameter, CopyUtil::copy);
    }

    public static QueryParameter copy(QueryParameter parameter, Function<Object, Object> objectCopy) {
        if (parameter == null) {
            return null;
        }
        QueryParameter newParameter = new QueryParameter();
        mapParameterProperties(parameter, newParameter, objectCopy);
        return newParameter;
    }

    public static HeaderParameter copy(HeaderParameter parameter) {
        return copy(parameter, CopyUtil::copy);
    }

    public static HeaderParameter copy(HeaderParameter parameter, Function<Object, Object> objectCopy) {
        if (parameter == null) {
            return null;
        }
        HeaderParameter newParameter = new HeaderParameter();
        mapParameterProperties(parameter, newParameter, objectCopy);
        return newParameter;
    }

    private static void mapParameterProperties(Parameter from, Parameter to, Function<Object, Object> objectCopy) {
        to
                .name(from.getName())
                .in(from.getIn())
                .description(from.getDescription())
                .required(from.getRequired())
                .deprecated(from.getDeprecated())
                .allowEmptyValue(from.getAllowEmptyValue())
                .$ref(from.get$ref())
                .style(from.getStyle())
                .explode(from.getExplode())
                .allowReserved(from.getAllowReserved())
                .schema(copy(from.getSchema(), objectCopy))
                .example(objectCopy.apply(from.getExample()))
                .content(copy(from.getContent(), objectCopy));

        if (from.getExamples() != null) {
            to.setExamples(new LinkedHashMap<>());
            from.getExamples().forEach((k, v) -> to.addExample(k, copy(v, objectCopy)));
        }
        if (from.getExtensions() != null) {
            to.setExtensions(new LinkedHashMap<>());
            from.getExtensions().forEach((k, v) -> to.addExtension(k, objectCopy.apply(v)));
        }
    }

    public static Parameter copy(Parameter parameter) {
        return copy(parameter, CopyUtil::copy);
    }

    public static Parameter copy(Parameter parameter, Function<Object, Object> objectCopy) {
        if (parameter == null) {
            return null;
        } else if (parameter instanceof CookieParameter) {
            return copy((CookieParameter) parameter, objectCopy);
        } else if (parameter instanceof HeaderParameter) {
            return copy((HeaderParameter) parameter, objectCopy);
        } else if (parameter instanceof PathParameter) {
            return copy((PathParameter) parameter, objectCopy);
        } else if (parameter instanceof QueryParameter) {
            return copy((QueryParameter) parameter, objectCopy);
        }
        Parameter newParameter = new Parameter();
        mapParameterProperties(parameter, newParameter, objectCopy);
        return newParameter;
    }

    public static Paths copy(Paths paths) {
        return copy(paths, CopyUtil::copy);
    }

    public static Paths copy(Paths paths, Function<Object, Object> objectCopy) {
        if (paths == null) {
            return null;
        }
        Paths newPaths = new Paths();
        paths.forEach((k, v) -> newPaths.addPathItem(k, copy(v, objectCopy)));

        if (paths.getExtensions() != null) {
            newPaths.setExtensions(new LinkedHashMap<>());
            paths.getExtensions().forEach((k, v) -> newPaths.addExtension(k, objectCopy.apply(v)));
        }
        return newPaths;
    }

    public static PathItem copy(PathItem pathItem) {
        return copy(pathItem, CopyUtil::copy);
    }

    public static PathItem copy(PathItem pathItem, Function<Object, Object> objectCopy) {
        if (pathItem == null) {
            return null;
        }
        PathItem newPathItem = new PathItem()
                .summary(pathItem.getSummary())
                .description(pathItem.getDescription())
                .get(copy(pathItem.getGet(), objectCopy))
                .put(copy(pathItem.getPut(), objectCopy))
                .post(copy(pathItem.getPost(), objectCopy))
                .delete(copy(pathItem.getDelete(), objectCopy))
                .options(copy(pathItem.getOptions(), objectCopy))
                .head(copy(pathItem.getHead(), objectCopy))
                .patch(copy(pathItem.getPatch(), objectCopy))
                .trace(copy(pathItem.getTrace(), objectCopy))
                .$ref(pathItem.get$ref());

        if (pathItem.getServers() != null) {
            newPathItem.setServers(new ArrayList<>());
            pathItem.getServers().forEach(v -> newPathItem.addServersItem(copy(v, objectCopy)));
        }
        if (pathItem.getParameters() != null) {
            newPathItem.setParameters(new ArrayList<>());
            pathItem.getParameters().forEach(v -> newPathItem.addParametersItem(copy(v, objectCopy)));
        }
        if (pathItem.getExtensions() != null) {
            newPathItem.setExtensions(new LinkedHashMap<>());
            pathItem.getExtensions().forEach((k, v) -> newPathItem.addExtension(k, objectCopy.apply(v)));
        }
        return newPathItem;
    }

    public static RequestBody copy(RequestBody requestBody) {
        return copy(requestBody, CopyUtil::copy);
    }

    public static RequestBody copy(RequestBody requestBody, Function<Object, Object> objectCopy) {
        if (requestBody == null) {
            return null;
        }
        RequestBody newRequestBody = new RequestBody()
                .description(requestBody.getDescription())
                .content(copy(requestBody.getContent(), objectCopy))
                .required(requestBody.getRequired())
                .$ref(requestBody.get$ref());

        if (requestBody.getExtensions() != null) {
            newRequestBody.setExtensions(new LinkedHashMap<>());
            requestBody.getExtensions().forEach((k, v) -> newRequestBody.addExtension(k, objectCopy.apply(v)));
        }
        return newRequestBody;
    }

    public static Scopes copy(Scopes scopes) {
        return copy(scopes, CopyUtil::copy);
    }

    public static Scopes copy(Scopes scopes, Function<Object, Object> objectCopy) {
        if (scopes == null) {
            return null;
        }
        Scopes newScopes = new Scopes();
        scopes.forEach(newScopes::addString);
        if (scopes.getExtensions() != null) {
            newScopes.setExtensions(new LinkedHashMap<>());
            scopes.getExtensions().forEach((k, v) -> newScopes.addExtension(k, objectCopy.apply(v)));
        }
        return newScopes;
    }

    public static SecurityRequirement copy(SecurityRequirement security) {
        if (security == null) {
            return null;
        }
        SecurityRequirement newSecurity = new SecurityRequirement();
        security.forEach((k, v) -> newSecurity.addList(k, new ArrayList<>(v)));
        return newSecurity;
    }

    public static SecurityScheme copy(SecurityScheme securityScheme) {
        return copy(securityScheme, CopyUtil::copy);
    }

    public static SecurityScheme copy(SecurityScheme securityScheme, Function<Object, Object> objectCopy) {
        if (securityScheme == null) {
            return null;
        }
        SecurityScheme newSecurityScheme = new SecurityScheme()
                .type(securityScheme.getType())
                .description(securityScheme.getDescription())
                .name(securityScheme.getName())
                .$ref(securityScheme.get$ref())
                .in(securityScheme.getIn())
                .scheme(securityScheme.getScheme())
                .bearerFormat(securityScheme.getBearerFormat())
                .flows(copy(securityScheme.getFlows(), objectCopy))
                .openIdConnectUrl(securityScheme.getOpenIdConnectUrl());

        if (securityScheme.getExtensions() != null) {
            newSecurityScheme.setExtensions(new LinkedHashMap<>());
            securityScheme.getExtensions().forEach((k, v) -> newSecurityScheme.addExtension(k, objectCopy.apply(v)));
        }
        return newSecurityScheme;
    }

    public static Server copy(Server server) {
        return copy(server, CopyUtil::copy);
    }

    public static Server copy(Server server, Function<Object, Object> objectCopy) {
        if (server == null) {
            return null;
        }
        Server newServer = new Server()
                .url(server.getUrl())
                .description(server.getDescription())
                .variables(copy(server.getVariables(), objectCopy));

        if (server.getExtensions() != null) {
            newServer.setExtensions(new LinkedHashMap<>());
            server.getExtensions().forEach((k, v) -> newServer.addExtension(k, objectCopy.apply(v)));
        }
        return newServer;
    }

    public static ServerVariable copy(ServerVariable variable) {
        return copy(variable, CopyUtil::copy);
    }

    public static ServerVariable copy(ServerVariable variable, Function<Object, Object> objectCopy) {
        if (variable == null) {
            return null;
        }
        ServerVariable newVariable = new ServerVariable()
                ._enum(new ArrayList<>(variable.getEnum()))
                ._default(variable.getDefault())
                .description(variable.getDescription());

        if (variable.getExtensions() != null) {
            newVariable.setExtensions(new LinkedHashMap<>());
            variable.getExtensions().forEach((k, v) -> newVariable.addExtension(k, objectCopy.apply(v)));
        }
        return newVariable;
    }

    public static ServerVariables copy(ServerVariables variables) {
        return copy(variables, CopyUtil::copy);
    }

    public static ServerVariables copy(ServerVariables variables, Function<Object, Object> objectCopy) {
        if (variables == null) {
            return null;
        }
        ServerVariables newVariables = new ServerVariables();
        variables.forEach((k, v) -> newVariables.addServerVariable(k, copy(v, objectCopy)));

        if (variables.getExtensions() != null) {
            newVariables.setExtensions(new LinkedHashMap<>());
            variables.getExtensions().forEach((k, v) -> newVariables.addExtension(k, objectCopy.apply(v)));
        }
        return newVariables;
    }

    public static Schema copy(Schema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static Schema copy(Schema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        } else if (schema instanceof ArraySchema) {
            return copy((ArraySchema) schema, objectCopy);
        } else if (schema instanceof BinarySchema) {
            return copy((BinarySchema) schema, objectCopy);
        } else if (schema instanceof BooleanSchema) {
            return copy((BooleanSchema) schema, objectCopy);
        } else if (schema instanceof ByteArraySchema) {
            return copy((ByteArraySchema) schema, objectCopy);
        } else if (schema instanceof ComposedSchema) {
            return copy((ComposedSchema) schema, objectCopy);
        } else if (schema instanceof DateSchema) {
            return copy((DateSchema) schema, objectCopy);
        } else if (schema instanceof DateTimeSchema) {
            return copy((DateTimeSchema) schema, objectCopy);
        } else if (schema instanceof EmailSchema) {
            return copy((EmailSchema) schema, objectCopy);
        } else if (schema instanceof FileSchema) {
            return copy((FileSchema) schema, objectCopy);
        } else if (schema instanceof IntegerSchema) {
            return copy((IntegerSchema) schema, objectCopy);
        } else if (schema instanceof MapSchema) {
            return copy((MapSchema) schema, objectCopy);
        } else if (schema instanceof NumberSchema) {
            return copy((NumberSchema) schema, objectCopy);
        } else if (schema instanceof ObjectSchema) {
            return copy((ObjectSchema) schema, objectCopy);
        } else if (schema instanceof PasswordSchema) {
            return copy((PasswordSchema) schema, objectCopy);
        } else if (schema instanceof StringSchema) {
            return copy((StringSchema) schema, objectCopy);
        } else if (schema instanceof UUIDSchema) {
            return copy((UUIDSchema) schema, objectCopy);
        }
        Schema newSchema = new Schema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static BinarySchema copy(BinarySchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static BinarySchema copy(BinarySchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        BinarySchema newSchema = new BinarySchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static BooleanSchema copy(BooleanSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static BooleanSchema copy(BooleanSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        BooleanSchema newSchema = new BooleanSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static ByteArraySchema copy(ByteArraySchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static ByteArraySchema copy(ByteArraySchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        ByteArraySchema newSchema = new ByteArraySchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static DateSchema copy(DateSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static DateSchema copy(DateSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        DateSchema newSchema = new DateSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static DateTimeSchema copy(DateTimeSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static DateTimeSchema copy(DateTimeSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        DateTimeSchema newSchema = new DateTimeSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static EmailSchema copy(EmailSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static EmailSchema copy(EmailSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        EmailSchema newSchema = new EmailSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static FileSchema copy(FileSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static FileSchema copy(FileSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        FileSchema newSchema = new FileSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static IntegerSchema copy(IntegerSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static IntegerSchema copy(IntegerSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        IntegerSchema newSchema = new IntegerSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static MapSchema copy(MapSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static MapSchema copy(MapSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        MapSchema newSchema = new MapSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static NumberSchema copy(NumberSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static NumberSchema copy(NumberSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        NumberSchema newSchema = new NumberSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static ObjectSchema copy(ObjectSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static ObjectSchema copy(ObjectSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        ObjectSchema newSchema = new ObjectSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static PasswordSchema copy(PasswordSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static PasswordSchema copy(PasswordSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        PasswordSchema newSchema = new PasswordSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static StringSchema copy(StringSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static StringSchema copy(StringSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        StringSchema newSchema = new StringSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static UUIDSchema copy(UUIDSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static UUIDSchema copy(UUIDSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        UUIDSchema newSchema = new UUIDSchema();
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static ArraySchema copy(ArraySchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static ArraySchema copy(ArraySchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        ArraySchema newSchema = new ArraySchema()
                .items(copy(schema.getItems(), objectCopy));
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    public static ComposedSchema copy(ComposedSchema schema) {
        return copy(schema, CopyUtil::copy);
    }

    public static ComposedSchema copy(ComposedSchema schema, Function<Object, Object> objectCopy) {
        if (schema == null) {
            return null;
        }
        ComposedSchema newSchema = new ComposedSchema();
        if (schema.getAllOf() != null) {
            newSchema.setAllOf(new ArrayList<>());
            schema.getAllOf().forEach(v -> newSchema.addAllOfItem(copy(v, objectCopy)));
        }
        if (schema.getOneOf() != null) {
            newSchema.setOneOf(new ArrayList<>());
            schema.getOneOf().forEach(v -> newSchema.addOneOfItem(copy(v, objectCopy)));
        }
        if (schema.getAnyOf() != null) {
            newSchema.setAnyOf(new ArrayList<>());
            schema.getAnyOf().forEach(v -> newSchema.addAnyOfItem(copy(v, objectCopy)));
        }
        mapSchemaProperties(schema, newSchema, objectCopy);
        return newSchema;
    }

    private static void mapSchemaProperties(Schema from, Schema to, Function<Object, Object> objectCopy) {
        to
                .name(from.getName())
                .title(from.getTitle())
                .multipleOf(from.getMultipleOf())
                .maximum(from.getMaximum())
                .exclusiveMaximum(from.getExclusiveMaximum())
                .minimum(from.getMinimum())
                .exclusiveMinimum(from.getExclusiveMinimum())
                .maxLength(from.getMaxLength())
                .minLength(from.getMinLength())
                .pattern(from.getPattern())
                .maxItems(from.getMaxItems())
                .minItems(from.getMinItems())
                .uniqueItems(from.getUniqueItems())
                .maxProperties(from.getMaxProperties())
                .minProperties(from.getMinProperties())
                .type(from.getType())
                .not(copy(from.getNot(), objectCopy))
                .additionalProperties(objectCopy.apply(from.getAdditionalProperties()))
                .description(from.getDescription())
                .format(from.getFormat())
                .$ref(from.get$ref())
                .nullable(from.getNullable())
                .readOnly(from.getReadOnly())
                .writeOnly(from.getWriteOnly())
                .example(objectCopy.apply(from.getExample()))
                .externalDocs(copy(from.getExternalDocs(), objectCopy))
                .deprecated(from.getDeprecated())
                .xml(copy(from.getXml(), objectCopy))
                .discriminator(copy(from.getDiscriminator()));
        to.setDefault(objectCopy.apply(from.getDefault()));

        if (from.getRequired() != null) {
            to.setRequired(new ArrayList<>());
            ((List<String>) from.getRequired()).forEach(to::addRequiredItem);
        }
        if (from.getProperties() != null) {
            to.setProperties(new LinkedHashMap<>());
            ((Map<String, Schema>) from.getProperties()).forEach((k, v) -> to.addProperties(k, copy(v, objectCopy)));
        }
        if (from.getExtensions() != null) {
            to.setExtensions(new LinkedHashMap<>());
            ((Map<String, Object>) from.getExtensions()).forEach((k, v) -> to.addExtension(k, objectCopy.apply(v)));
        }
        if (from.getEnum() != null) {
            to.setEnum(new ArrayList());
            from.getEnum().forEach(v -> to.addEnumItemObject(objectCopy.apply(v)));
        }
    }

    public static Tag copy(Tag tag) {
        return copy(tag, CopyUtil::copy);
    }

    public static Tag copy(Tag tag, Function<Object, Object> objectCopy) {
        if (tag == null) {
            return null;
        }
        Tag newTag = new Tag()
                .name(tag.getName())
                .description(tag.getDescription())
                .externalDocs(copy(tag.getExternalDocs(), objectCopy));

        if (tag.getExtensions() != null) {
            newTag.setExtensions(new LinkedHashMap<>());
            tag.getExtensions().forEach((k, v) -> newTag.addExtension(k, objectCopy.apply(v)));
        }
        return newTag;

    }

    public static XML copy(XML xml) {
        return copy(xml, CopyUtil::copy);
    }

    public static XML copy(XML xml, Function<Object, Object> objectCopy) {
        if (xml == null) {
            return null;
        }
        XML newXml = new XML()
                .name(xml.getName())
                .namespace(xml.getNamespace())
                .prefix(xml.getPrefix())
                .attribute(xml.getAttribute())
                .wrapped(xml.getWrapped());

        if (xml.getExtensions() != null) {
            newXml.setExtensions(new LinkedHashMap<>());
            xml.getExtensions().forEach((k, v) -> newXml.addExtension(k, objectCopy.apply(v)));
        }
        return newXml;
    }
}
