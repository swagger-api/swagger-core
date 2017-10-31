package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.swagger.v3.core.jackson.SchemaSerializer;
import io.swagger.v3.core.jackson.mixin.ExtensionsMixin;
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
import io.swagger.v3.oas.models.links.LinkParameter;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.EncodingProperty;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectMapperFactory {

    protected static ObjectMapper createJson() {
        return createJson(true, true);
    }

    protected static ObjectMapper createJson(boolean includePathDeserializer, boolean includeResponseDeserializer) {
        return create(null, includePathDeserializer, includeResponseDeserializer);
    }

    protected static ObjectMapper createYaml() {
        return createYaml(true, true);
    }

    protected static ObjectMapper createYaml(boolean includePathDeserializer, boolean includeResponseDeserializer) {
        YAMLFactory factory = new YAMLFactory();
        factory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        factory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        factory.enable(YAMLGenerator.Feature.SPLIT_LINES);
        factory.enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS);

        return create(factory, includePathDeserializer, includeResponseDeserializer);
    }

    private static ObjectMapper create(JsonFactory jsonFactory, boolean includePathDeserializer, boolean includeResponseDeserializer) {
        ObjectMapper mapper = jsonFactory == null ? new ObjectMapper() : new ObjectMapper(jsonFactory);

        // handle ref schema serialization skipping all other props
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer<?> modifySerializer(
                            SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
                        if (Schema.class.isAssignableFrom(desc.getBeanClass())) {
                            return new SchemaSerializer((JsonSerializer<Object>) serializer);
                        }
                        return serializer;
                    }
                });
            }
        });

        Module deserializerModule = new DeserializationModule(includePathDeserializer, includeResponseDeserializer);
        mapper.registerModule(deserializerModule);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        List<Class<?>> mixinTargets = Arrays.asList(
                ApiResponse.class,
                Callback.class,
                Components.class,
                Contact.class,
                Encoding.class,
                EncodingProperty.class,
                Example.class,
                ExternalDocumentation.class,
                Header.class,
                Info.class,
                License.class,
                Link.class,
                LinkParameter.class,
                MediaType.class,
                OAuthFlow.class,
                OAuthFlows.class,
                OpenAPI.class,
                Operation.class,
                Parameter.class,
                PathItem.class,
                Paths.class,
                RequestBody.class,
                Scopes.class,
                SecurityScheme.class,
                Server.class,
                ServerVariable.class,
                ServerVariables.class,
                Tag.class,
                XML.class,
                Schema.class
                );
        mapper.setMixIns(mixinTargets.stream().collect(Collectors.toMap(Function.identity(), c -> ExtensionsMixin.class)));

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }
}
