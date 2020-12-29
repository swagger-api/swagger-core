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

    public static Object copy(Object o) {
        if(o == null) {
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
        }
        else if (o instanceof Date) {
            return ((Date) o).clone();
        }
        else if (o instanceof byte[]) {
            return ((byte[]) o).clone();
        }
        try {
            return Json.mapper().readTree(Json.pretty(o));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static ApiResponse copy(ApiResponse response) {
        if (response == null) {
            return null;
        }
        ApiResponse newResponse = new ApiResponse()
                .description(response.getDescription())
                .content(copy(response.getContent()))
                .$ref(response.get$ref());

        if (response.getHeaders() != null) {
            newResponse.setHeaders(new LinkedHashMap<>());
            response.getHeaders().forEach((k,v) -> newResponse.addHeaderObject(k, copy(v)));
        }
        if (response.getLinks() != null) {
            newResponse.setLinks(new LinkedHashMap<>());
            response.getLinks().forEach((k,v) -> newResponse.link(k, copy(v)));
        }
        if (response.getExtensions() != null) {
            newResponse.setExtensions(new LinkedHashMap<>());
            response.getExtensions().forEach((k,v) -> newResponse.addExtension(k, copy(v)));
        }
        return newResponse;
    }

    public static ApiResponses copy(ApiResponses responses) {
        if(responses == null) {
            return null;
        }
        ApiResponses newResponses = new ApiResponses();
        responses.forEach((k,v) -> newResponses.addApiResponse(k, copy(v)));
        if (responses.getExtensions() != null) {
            newResponses.setExtensions(new LinkedHashMap<>());
            responses.getExtensions().forEach((k,v) -> newResponses.addExtension(k, copy(v)));
        }
        return newResponses;
    }

    public static Callback copy(Callback callback) {
        if (callback == null) {
            return null;
        }
        Callback newCallback = new Callback()
                .$ref(callback.get$ref());

        callback.forEach((k,v) -> newCallback.addPathItem(k, copy(v)));

        if (callback.getExtensions() != null) {
            newCallback.setExtensions(new LinkedHashMap<>());
            callback.getExtensions().forEach((k,v) -> newCallback.addExtension(k, copy(v)));
        }
        return newCallback;
    }

    public static Components copy(Components components) {
        if (components == null) {
            return null;
        }
        Components newComponents = new Components();

        if (components.getSchemas() != null) {
            newComponents.setSchemas(new LinkedHashMap<>());
            components.getSchemas().forEach((k,v) -> newComponents.addSchemas(k, copy(v)));
        }
        if (components.getResponses() != null) {
            newComponents.setResponses(new LinkedHashMap<>());
            components.getResponses().forEach((k,v) -> newComponents.addResponses(k, copy(v)));
        }
        if (components.getParameters() != null) {
            newComponents.setParameters(new LinkedHashMap<>());
            components.getParameters().forEach((k,v) -> newComponents.addParameters(k, copy(v)));
        }
        if (components.getExamples() != null) {
            newComponents.setExamples(new LinkedHashMap<>());
            components.getExamples().forEach((k,v) -> newComponents.addExamples(k, copy(v)));
        }
        if (components.getRequestBodies() != null) {
            newComponents.setRequestBodies(new LinkedHashMap<>());
            components.getRequestBodies().forEach((k,v) -> newComponents.addRequestBodies(k, copy(v)));
        }
        if (components.getHeaders() != null) {
            newComponents.setHeaders(new LinkedHashMap<>());
            components.getHeaders().forEach((k,v) -> newComponents.addHeaders(k, copy(v)));
        }
        if (components.getSecuritySchemes() != null) {
            newComponents.setSecuritySchemes(new LinkedHashMap<>());
            components.getSecuritySchemes().forEach((k,v) -> newComponents.addSecuritySchemes(k, copy(v)));
        }
        if (components.getLinks() != null) {
            newComponents.setLinks(new LinkedHashMap<>());
            components.getLinks().forEach((k,v) -> newComponents.addLinks(k, copy(v)));
        }
        if (components.getCallbacks() != null) {
            newComponents.setCallbacks(new LinkedHashMap<>());
            components.getCallbacks().forEach((k,v) -> newComponents.addCallbacks(k, copy(v)));
        }
        if (components.getExtensions() != null) {
            newComponents.setExtensions(new LinkedHashMap<>());
            components.getExtensions().forEach((k,v) -> newComponents.addExtension(k, copy(v)));
        }
        return newComponents;
    }

    public static Contact copy(Contact contact) {
        if (contact == null) {
            return null;
        }
        Contact newContact = new Contact()
                .name(contact.getName())
                .url(contact.getUrl())
                .email(contact.getEmail());

        if (contact.getExtensions() != null) {
            newContact.setExtensions(new LinkedHashMap<>());
            contact.getExtensions().forEach((k,v) -> newContact.addExtension(k, copy(v)));
        }
        return newContact;
    }

    public static Content copy(Content content) {
        if (content == null) {
            return null;
        }
        Content newContent = new Content();
        content.forEach((k,v) -> newContent.addMediaType(k, copy(v)));
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
            encoding.getHeaders().forEach((k,v) -> newEncoding.addHeader(k, copy(v)));
        }
        if (encoding.getExtensions() != null) {
            newEncoding.setExtensions(new LinkedHashMap<>());
            encoding.getExtensions().forEach((k,v) -> newEncoding.addExtension(k, copy(v)));
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
            example.getExtensions().forEach((k,v) -> newExample.addExtension(k, objectCopy.apply(v)));
        }
        return newExample;
    }

    public static ExternalDocumentation copy(ExternalDocumentation documentation) {
        if (documentation == null) {
            return null;
        }
        ExternalDocumentation newDocumentation = new ExternalDocumentation()
                .description(documentation.getDescription())
                .url(documentation.getUrl());

        if (documentation.getExtensions() != null) {
            newDocumentation.setExtensions(new LinkedHashMap<>());
            documentation.getExtensions().forEach((k,v) -> newDocumentation.addExtension(k, copy(v)));
        }
        return newDocumentation;
    }

    public static Header copy(Header header) {
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
                .schema(copy(header.getSchema()))
                .example(copy(header.getExample()))
                .content(copy(header.getContent()));

        if (header.getExtensions() != null) {
            newHeader.setExtensions(new LinkedHashMap<>());
            header.getExtensions().forEach((k,v) -> newHeader.addExtension(k, copy(v)));
        }
        return newHeader;
    }

    public static Info copy(Info info) {
        if (info == null) {
            return null;
        }
        Info newInfo = new Info()
                .title(info.getTitle())
                .description(info.getDescription())
                .termsOfService(info.getTermsOfService())
                .contact(copy(info.getContact()))
                .license(copy(info.getLicense()))
                .version(info.getVersion());

        if (info.getExtensions() != null) {
            newInfo.setExtensions(new LinkedHashMap<>());
            info.getExtensions().forEach((k,v) -> newInfo.addExtension(k, copy(v)));
        }
        return newInfo;
    }

    public static License copy(License license) {
        if (license == null) {
            return null;
        }
        License newLicense = new License()
                .name(license.getName())
                .url(license.getUrl());

        if (license.getExtensions() != null) {
            newLicense.setExtensions(new LinkedHashMap<>());
            license.getExtensions().forEach((k,v) -> newLicense.addExtension(k, copy(v)));
        }
        return newLicense;
    }

    public static Link copy(Link link) {
        if (link == null) {
            return null;
        }
        Link newLink = new Link()
                .operationRef(link.getOperationRef())
                .operationId(link.getOperationId())
                .requestBody(copy(link.getRequestBody()))
                .description(link.getDescription())
                .$ref(link.get$ref())
                .server(copy(link.getServer()));

        if (link.getParameters() != null) {
            newLink.setParameters(new LinkedHashMap<>());
            link.getParameters().forEach(newLink::parameters);
        }
        if (link.getHeaders() != null) {
            newLink.setHeaders(new LinkedHashMap<>());
            link.getHeaders().forEach((k,v) -> newLink.addHeaderObject(k, copy(v)));
        }
        if (link.getExtensions() != null) {
            newLink.setExtensions(new LinkedHashMap<>());
            link.getExtensions().forEach((k,v) -> newLink.addExtension(k, copy(v)));
        }
        return newLink;
    }

    public static MediaType copy(MediaType mediaType) {
        if (mediaType == null) {
            return null;
        }
        MediaType newMediaType = new MediaType()
                .schema(copy(mediaType.getSchema()))
                .example(copy(mediaType.getExample()));

        if (mediaType.getExamples() != null) {
            newMediaType.setExamples(new LinkedHashMap<>());
            mediaType.getExamples().forEach((k,v) -> newMediaType.addExamples(k, copy(v)));
        }
        if (mediaType.getEncoding() != null) {
            newMediaType.setEncoding(new LinkedHashMap<>());
            mediaType.getEncoding().forEach((k,v) -> newMediaType.addEncoding(k, copy(v)));
        }
        if (mediaType.getExtensions() != null) {
            newMediaType.setExtensions(new LinkedHashMap<>());
            mediaType.getExtensions().forEach((k,v) -> newMediaType.addExtension(k, copy(v)));
        }
        return newMediaType;
    }

    public static OAuthFlow copy(OAuthFlow flow) {
        if (flow == null) {
            return null;
        }
        OAuthFlow newFlow = new OAuthFlow()
                .authorizationUrl(flow.getAuthorizationUrl())
                .tokenUrl(flow.getTokenUrl())
                .refreshUrl(flow.getRefreshUrl())
                .scopes(copy(flow.getScopes()));

        if (flow.getExtensions() != null) {
            newFlow.setExtensions(new LinkedHashMap<>());
            flow.getExtensions().forEach((k,v) -> newFlow.addExtension(k, copy(v)));
        }
        return newFlow;
    }

    public static OAuthFlows copy(OAuthFlows flows) {
        if (flows == null) {
            return null;
        }
        OAuthFlows newFlows = new OAuthFlows()
                .implicit(copy(flows.getImplicit()))
                .password(copy(flows.getPassword()))
                .clientCredentials(copy(flows.getClientCredentials()))
                .authorizationCode(copy(flows.getAuthorizationCode()));

        if (flows.getExtensions() != null) {
            newFlows.setExtensions(new LinkedHashMap<>());
            flows.getExtensions().forEach((k,v) -> newFlows.addExtension(k, copy(v)));
        }
        return newFlows;
    }

    public static OpenAPI copy(OpenAPI openAPI) {
        if (openAPI == null) {
            return null;
        }
        OpenAPI newOpenAPI = new OpenAPI()
                .openapi(openAPI.getOpenapi())
                .info(copy(openAPI.getInfo()))
                .externalDocs(copy(openAPI.getExternalDocs()))
                .paths(copy(openAPI.getPaths()))
                .components(copy(openAPI.getComponents()));

        if (openAPI.getServers() != null) {
            newOpenAPI.setServers(new ArrayList<>());
            openAPI.getServers().forEach(v -> newOpenAPI.addServersItem(copy(v)));
        }
        if (openAPI.getSecurity() != null) {
            newOpenAPI.setSecurity(new ArrayList<>());
            openAPI.getSecurity().forEach(v -> newOpenAPI.addSecurityItem(copy(v)));
        }
        if (openAPI.getTags() != null) {
            newOpenAPI.setTags(new ArrayList<>());
            openAPI.getTags().forEach(v -> newOpenAPI.addTagsItem(copy(v)));
        }
        if (openAPI.getExtensions() != null) {
            newOpenAPI.setExtensions(new LinkedHashMap<>());
            openAPI.getExtensions().forEach((k,v) -> newOpenAPI.addExtension(k, copy(v)));
        }
        return newOpenAPI;
    }

    public static Operation copy(Operation operation) {
        if (operation == null) {
            return null;
        }
        Operation newOperation = new Operation()
                .summary(operation.getSummary())
                .description(operation.getDescription())
                .externalDocs(copy(operation.getExternalDocs()))
                .operationId(operation.getOperationId())
                .requestBody(copy(operation.getRequestBody()))
                .responses(copy(operation.getResponses()))
                .deprecated(operation.getDeprecated());

        if (operation.getTags() != null) {
            newOperation.setTags(new ArrayList<>());
            operation.getTags().forEach(newOperation::addTagsItem);
        }
        if (operation.getParameters() != null) {
            newOperation.setParameters(new ArrayList<>());
            operation.getParameters().forEach(v -> newOperation.addParametersItem(copy(v)));
        }
        if (operation.getCallbacks() != null) {
            newOperation.setCallbacks(new LinkedHashMap<>());
            operation.getCallbacks().forEach((k,v) -> newOperation.addCallbackItem(k, copy(v)));
        }
        if (operation.getSecurity() != null) {
            newOperation.setSecurity(new ArrayList<>());
            operation.getSecurity().forEach(v -> newOperation.addSecurityItem(copy(v)));
        }
        if (operation.getServers() != null) {
            newOperation.setServers(new ArrayList<>());
            operation.getServers().forEach(v -> newOperation.addServersItem(copy(v)));
        }
        if (operation.getExtensions() != null) {
            newOperation.setExtensions(new LinkedHashMap<>());
            operation.getExtensions().forEach((k,v) -> newOperation.addExtension(k, copy(v)));
        }
        return newOperation;
    }

    public static CookieParameter copy(CookieParameter parameter) {
        if (parameter == null) {
            return null;
        }
        CookieParameter newParameter = new CookieParameter();
        mapParameterProperties(parameter, newParameter);
        return newParameter;
    }

    public static PathParameter copy(PathParameter parameter) {
        if (parameter == null) {
            return null;
        }
        PathParameter newParameter = new PathParameter();
        mapParameterProperties(parameter, newParameter);
        return newParameter;
    }

    public static QueryParameter copy(QueryParameter parameter) {
        if (parameter == null) {
            return null;
        }
        QueryParameter newParameter = new QueryParameter();
        mapParameterProperties(parameter, newParameter);
        return newParameter;
    }

    public static HeaderParameter copy(HeaderParameter parameter) {
        if (parameter == null) {
            return null;
        }
        HeaderParameter newParameter = new HeaderParameter();
        mapParameterProperties(parameter, newParameter);
        return newParameter;
    }

    private static void mapParameterProperties(Parameter from, Parameter to) {
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
                .schema(copy(from.getSchema()))
                .example(copy(from.getExample()))
                .content(copy(from.getContent()));

        if (from.getExamples() != null) {
            to.setExamples(new LinkedHashMap<>());
            from.getExamples().forEach((k,v) -> to.addExample(k, copy(v)));
        }
        if (from.getExtensions() != null) {
            to.setExtensions(new LinkedHashMap<>());
            from.getExtensions().forEach((k,v) -> to.addExtension(k, copy(v)));
        }
    }

    public static Parameter copy(Parameter parameter) {
        if (parameter == null) {
            return null;
        } else if (parameter instanceof CookieParameter) {
            return copy((CookieParameter) parameter);
        } else if (parameter instanceof HeaderParameter) {
            return copy((HeaderParameter) parameter);
        } else if (parameter instanceof PathParameter) {
            return copy((PathParameter) parameter);
        } else if (parameter instanceof QueryParameter) {
            return copy((QueryParameter) parameter);
        }
        Parameter newParameter = new Parameter();
        mapParameterProperties(parameter, newParameter);
        return newParameter;
    }

    public static Paths copy(Paths paths) {
        if (paths == null) {
            return null;
        }
        Paths newPaths = new Paths();
        paths.forEach((k,v) -> newPaths.addPathItem(k, copy(v)));

        if (paths.getExtensions() != null) {
            newPaths.setExtensions(new LinkedHashMap<>());
            paths.getExtensions().forEach((k,v) -> newPaths.addExtension(k, copy(v)));
        }
        return newPaths;
    }

    public static PathItem copy(PathItem pathItem) {
        if (pathItem == null) {
            return null;
        }
        PathItem newPathItem = new PathItem()
                .summary(pathItem.getSummary())
                .description(pathItem.getDescription())
                .get(copy(pathItem.getGet()))
                .put(copy(pathItem.getPut()))
                .post(copy(pathItem.getPost()))
                .delete(copy(pathItem.getDelete()))
                .options(copy(pathItem.getOptions()))
                .head(copy(pathItem.getHead()))
                .patch(copy(pathItem.getPatch()))
                .trace(copy(pathItem.getTrace()))
                .$ref(pathItem.get$ref());

        if (pathItem.getServers() != null) {
            newPathItem.setServers(new ArrayList<>());
            pathItem.getServers().forEach(v -> newPathItem.addServersItem(copy(v)));
        }
        if (pathItem.getParameters() != null) {
            newPathItem.setParameters(new ArrayList<>());
            pathItem.getParameters().forEach(v -> newPathItem.addParametersItem(copy(v)));
        }
        if (pathItem.getExtensions() != null) {
            newPathItem.setExtensions(new LinkedHashMap<>());
            pathItem.getExtensions().forEach((k,v) -> newPathItem.addExtension(k, copy(v)));
        }
        return newPathItem;
    }

    public static RequestBody copy(RequestBody requestBody) {
        if (requestBody == null) {
            return null;
        }
        RequestBody newRequestBody = new RequestBody()
                .description(requestBody.getDescription())
                .content(copy(requestBody.getContent()))
                .required(requestBody.getRequired())
                .$ref(requestBody.get$ref());

        if (requestBody.getExtensions() != null) {
            newRequestBody.setExtensions(new LinkedHashMap<>());
            requestBody.getExtensions().forEach((k,v) -> newRequestBody.addExtension(k, copy(v)));
        }
        return newRequestBody;
    }

    public static Scopes copy(Scopes scopes) {
        if (scopes == null) {
            return null;
        }
        Scopes newScopes = new Scopes();
        scopes.forEach(newScopes::addString);
        if (scopes.getExtensions() != null) {
            newScopes.setExtensions(new LinkedHashMap<>());
            scopes.getExtensions().forEach((k,v) -> newScopes.addExtension(k, copy(v)));
        }
        return newScopes;
    }

    public static SecurityRequirement copy(SecurityRequirement security) {
        if (security == null) {
            return null;
        }
        SecurityRequirement newSecurity = new SecurityRequirement();
        security.forEach((k,v) -> newSecurity.addList(k, new ArrayList<>(v)));
        return newSecurity;
    }

    public static SecurityScheme copy(SecurityScheme securityScheme) {
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
                .flows(copy(securityScheme.getFlows()))
                .openIdConnectUrl(securityScheme.getOpenIdConnectUrl());

        if (securityScheme.getExtensions() != null) {
            newSecurityScheme.setExtensions(new LinkedHashMap<>());
            securityScheme.getExtensions().forEach((k,v) -> newSecurityScheme.addExtension(k, copy(v)));
        }
        return newSecurityScheme;
    }

    public static Server copy(Server server) {
        if (server == null) {
            return null;
        }
        Server newServer = new Server()
                .url(server.getUrl())
                .description(server.getDescription())
                .variables(copy(server.getVariables()));

        if (server.getExtensions() != null) {
            newServer.setExtensions(new LinkedHashMap<>());
            server.getExtensions().forEach((k,v) -> newServer.addExtension(k, copy(v)));
        }
        return newServer;
    }

    public static ServerVariable copy(ServerVariable variable) {
        if (variable == null) {
            return null;
        }
        ServerVariable newVariable = new ServerVariable()
                ._enum(new ArrayList<>(variable.getEnum()))
                ._default(variable.getDefault())
                .description(variable.getDescription());

        if (variable.getExtensions() != null) {
            newVariable.setExtensions(new LinkedHashMap<>());
            variable.getExtensions().forEach((k,v) -> newVariable.addExtension(k, copy(v)));
        }
        return newVariable;
    }

    public static ServerVariables copy(ServerVariables variables) {
        if (variables == null) {
            return null;
        }
        ServerVariables newVariables = new ServerVariables();
        variables.forEach((k,v) -> newVariables.addServerVariable(k, copy(v)));

        if (variables.getExtensions() != null) {
            newVariables.setExtensions(new LinkedHashMap<>());
            variables.getExtensions().forEach((k,v) -> newVariables.addExtension(k, copy(v)));
        }
        return newVariables;
    }

    public static Schema copy(Schema schema) {
        if (schema == null) {
            return null;
        } else if (schema instanceof ArraySchema) {
            return copy((ArraySchema) schema);
        } else if (schema instanceof BinarySchema) {
            return copy((BinarySchema) schema);
        } else if (schema instanceof BooleanSchema) {
            return copy((BooleanSchema) schema);
        } else if (schema instanceof ByteArraySchema) {
            return copy((ByteArraySchema) schema);
        } else if (schema instanceof ComposedSchema) {
            return copy((ComposedSchema) schema);
        } else if (schema instanceof DateSchema) {
            return copy((DateSchema) schema);
        } else if (schema instanceof DateTimeSchema) {
            return copy((DateTimeSchema) schema);
        } else if (schema instanceof EmailSchema) {
            return copy((EmailSchema) schema);
        } else if (schema instanceof FileSchema) {
            return copy((FileSchema) schema);
        } else if (schema instanceof IntegerSchema) {
            return copy((IntegerSchema) schema);
        } else if (schema instanceof MapSchema) {
            return copy((MapSchema) schema);
        } else if (schema instanceof NumberSchema) {
            return copy((NumberSchema) schema);
        } else if (schema instanceof ObjectSchema) {
            return copy((ObjectSchema) schema);
        } else if (schema instanceof PasswordSchema) {
            return copy((PasswordSchema) schema);
        } else if (schema instanceof StringSchema) {
            return copy((StringSchema) schema);
        } else if (schema instanceof UUIDSchema) {
            return copy((UUIDSchema) schema);
        }
        Schema newSchema = new Schema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static BinarySchema copy(BinarySchema schema) {
        if (schema == null) {
            return null;
        }
        BinarySchema newSchema = new BinarySchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static BooleanSchema copy(BooleanSchema schema) {
        if (schema == null) {
            return null;
        }
        BooleanSchema newSchema = new BooleanSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static ByteArraySchema copy(ByteArraySchema schema) {
        if (schema == null) {
            return null;
        }
        ByteArraySchema newSchema = new ByteArraySchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static DateSchema copy(DateSchema schema) {
        if (schema == null) {
            return null;
        }
        DateSchema newSchema = new DateSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static DateTimeSchema copy(DateTimeSchema schema) {
        if (schema == null) {
            return null;
        }
        DateTimeSchema newSchema = new DateTimeSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static EmailSchema copy(EmailSchema schema) {
        if (schema == null) {
            return null;
        }
        EmailSchema newSchema = new EmailSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static FileSchema copy(FileSchema schema) {
        if (schema == null) {
            return null;
        }
        FileSchema newSchema = new FileSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static IntegerSchema copy(IntegerSchema schema) {
        if (schema == null) {
            return null;
        }
        IntegerSchema newSchema = new IntegerSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static MapSchema copy(MapSchema schema) {
        if (schema == null) {
            return null;
        }
        MapSchema newSchema = new MapSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static NumberSchema copy(NumberSchema schema) {
        if (schema == null) {
            return null;
        }
        NumberSchema newSchema = new NumberSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static ObjectSchema copy(ObjectSchema schema) {
        if (schema == null) {
            return null;
        }
        ObjectSchema newSchema = new ObjectSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static PasswordSchema copy(PasswordSchema schema) {
        if (schema == null) {
            return null;
        }
        PasswordSchema newSchema = new PasswordSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static StringSchema copy(StringSchema schema) {
        if (schema == null) {
            return null;
        }
        StringSchema newSchema = new StringSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static UUIDSchema copy(UUIDSchema schema) {
        if (schema == null) {
            return null;
        }
        UUIDSchema newSchema = new UUIDSchema();
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static ArraySchema copy(ArraySchema schema) {
        if (schema == null) {
            return null;
        }
        ArraySchema newSchema = new ArraySchema()
                .items(copy(schema.getItems()));
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    public static ComposedSchema copy(ComposedSchema schema) {
        if (schema == null) {
            return null;
        }
        ComposedSchema newSchema = new ComposedSchema();
        if (schema.getAllOf() != null) {
            newSchema.setAllOf(new ArrayList<>());
            schema.getAllOf().forEach(v -> newSchema.addAllOfItem(copy(v)));
        }
        if (schema.getOneOf() != null) {
            newSchema.setOneOf(new ArrayList<>());
            schema.getOneOf().forEach(v -> newSchema.addOneOfItem(copy(v)));
        }
        if (schema.getAnyOf() != null) {
            newSchema.setAnyOf(new ArrayList<>());
            schema.getAnyOf().forEach(v -> newSchema.addAnyOfItem(copy(v)));
        }
        mapSchemaProperties(schema, newSchema);
        return newSchema;
    }

    private static void mapSchemaProperties(Schema from, Schema to) {
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
                .not(copy(from.getNot()))
                .additionalProperties(copy(from.getAdditionalProperties()))
                .description(from.getDescription())
                .format(from.getFormat())
                .$ref(from.get$ref())
                .nullable(from.getNullable())
                .readOnly(from.getReadOnly())
                .writeOnly(from.getWriteOnly())
                .example(copy(from.getExample()))
                .externalDocs(copy(from.getExternalDocs()))
                .deprecated(from.getDeprecated())
                .xml(copy(from.getXml()))
                .discriminator(copy(from.getDiscriminator()));
        to.setDefault(copy(from.getDefault()));

        if (from.getRequired() != null) {
            to.setRequired(new ArrayList<>());
            ((List<String>)from.getRequired()).forEach(to::addRequiredItem);
        }
        if (from.getProperties() != null) {
            to.setProperties(new LinkedHashMap<>());
            ((Map<String, Schema>)from.getProperties()).forEach((k,v) -> to.addProperties(k, copy(v)));
        }
        if (from.getExtensions() != null) {
            to.setExtensions(new LinkedHashMap<>());
            ((Map<String, Object>)from.getExtensions()).forEach((k,v) -> to.addExtension(k, copy(v)));
        }
        if (from.getEnum() != null) {
            to.setEnum(new ArrayList());
            from.getEnum().forEach(v -> to.addEnumItemObject(copy(v)));
        }
    }

    public static Tag copy(Tag tag) {
        if (tag == null) {
            return null;
        }
        Tag newTag = new Tag()
                .name(tag.getName())
                .description(tag.getDescription())
                .externalDocs(copy(tag.getExternalDocs()));

        if (tag.getExtensions() != null) {
            newTag.setExtensions(new LinkedHashMap<>());
            tag.getExtensions().forEach((k,v) -> newTag.addExtension(k, copy(v)));
        }
        return newTag;

    }

    public static XML copy(XML xml) {
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
            xml.getExtensions().forEach((k,v) -> newXml.addExtension(k, copy(v)));
        }
        return newXml;
    }
}
