package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.core.jackson.SchemaSerializer;
import io.swagger.v3.core.jackson.mixin.ComponentsMixin;
import io.swagger.v3.core.jackson.mixin.DateSchemaMixin;
import io.swagger.v3.core.jackson.mixin.ExampleMixin;
import io.swagger.v3.core.jackson.mixin.ExtensionsMixin;
import io.swagger.v3.core.jackson.mixin.MediaTypeMixin;
import io.swagger.v3.core.jackson.mixin.OpenAPIMixin;
import io.swagger.v3.core.jackson.mixin.OperationMixin;
import io.swagger.v3.core.jackson.mixin.SchemaMixin;
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
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.EncodingProperty;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class ObjectMapperFactory {

    private static AtomicReference<JsonMapper.Builder> jsonMapperBuilderReference =
            new AtomicReference<>(createDefaultJsonMapperBuilder());
    private static AtomicReference<YAMLMapper.Builder> yamlMapperBuilderReference =
            new AtomicReference<>(createDefaultYAMLMapperBuilder());

    protected static ObjectMapper createJson() {
        return jsonMapperBuilderReference.get().build();
    }

    protected static ObjectMapper createYaml() {
        return yamlMapperBuilderReference.get().build();
    }

    public static void modifyJsonMapperBuilder(Function<JsonMapper.Builder, JsonMapper.Builder> function) {
        jsonMapperBuilderReference.set(
            function.apply(jsonMapperBuilderReference.get())
        );
    }

    public static void resetJsonMapperBuilder() {
        jsonMapperBuilderReference.set(createDefaultJsonMapperBuilder());
    }

    public static void modifyYamlMapperBuilder(Function<YAMLMapper.Builder, YAMLMapper.Builder> function) {
        yamlMapperBuilderReference.set(
                function.apply(yamlMapperBuilderReference.get())
        );
    }

    public static void resetYamlMapperBuilder() {
        yamlMapperBuilderReference.set(createDefaultYAMLMapperBuilder());
    }

    public static ObjectMapper buildStrictGenericObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        try {
            mapper.configure(DeserializationFeature.valueOf("FAIL_ON_TRAILING_TOKENS"), true);
        } catch (Throwable e) {
            // add only if supported by Jackson version 2.9+
        }
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    private static JsonMapper.Builder createDefaultJsonMapperBuilder() {
        JsonMapper.Builder builder = JsonMapper.builder();
        addDefaultSetupToMapperBuilder(builder);
        return builder;
    }

    private static YAMLMapper.Builder createDefaultYAMLMapperBuilder() {
        YAMLFactory factory = new YAMLFactory();
        factory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        factory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        factory.enable(YAMLGenerator.Feature.SPLIT_LINES);
        factory.enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS);
        YAMLMapper.Builder builder = YAMLMapper.builder(factory);
        addDefaultSetupToMapperBuilder(builder);
        return builder;
    }

    private static void addDefaultSetupToMapperBuilder(MapperBuilder<?, ?> builder) {
        // handle ref schema serialization skipping all other props
        builder.addModule(new SimpleModule() {
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

        Module deserializerModule = new DeserializationModule();
        builder.addModule(deserializerModule);
        builder.addModule(new JavaTimeModule());

        builder.addMixIn(ApiResponses.class, ExtensionsMixin.class);
        builder.addMixIn(ApiResponse.class, ExtensionsMixin.class);
        builder.addMixIn(Callback.class, ExtensionsMixin.class);
        builder.addMixIn(Components.class, ComponentsMixin.class);
        builder.addMixIn(Contact.class, ExtensionsMixin.class);
        builder.addMixIn(Encoding.class, ExtensionsMixin.class);
        builder.addMixIn(EncodingProperty.class, ExtensionsMixin.class);
        builder.addMixIn(Example.class, ExampleMixin.class);
        builder.addMixIn(ExternalDocumentation.class, ExtensionsMixin.class);
        builder.addMixIn(Header.class, ExtensionsMixin.class);
        builder.addMixIn(Info.class, ExtensionsMixin.class);
        builder.addMixIn(License.class, ExtensionsMixin.class);
        builder.addMixIn(Link.class, ExtensionsMixin.class);
        builder.addMixIn(LinkParameter.class, ExtensionsMixin.class);
        builder.addMixIn(MediaType.class, MediaTypeMixin.class);
        builder.addMixIn(OAuthFlow.class, ExtensionsMixin.class);
        builder.addMixIn(OAuthFlows.class, ExtensionsMixin.class);
        builder.addMixIn(OpenAPI.class, OpenAPIMixin.class);
        builder.addMixIn(Operation.class, OperationMixin.class);
        builder.addMixIn(Parameter.class, ExtensionsMixin.class);
        builder.addMixIn(PathItem.class, ExtensionsMixin.class);
        builder.addMixIn(Paths.class, ExtensionsMixin.class);
        builder.addMixIn(RequestBody.class, ExtensionsMixin.class);
        builder.addMixIn(Scopes.class, ExtensionsMixin.class);
        builder.addMixIn(SecurityScheme.class, ExtensionsMixin.class);
        builder.addMixIn(Server.class, ExtensionsMixin.class);
        builder.addMixIn(ServerVariable.class, ExtensionsMixin.class);
        builder.addMixIn(ServerVariables.class, ExtensionsMixin.class);
        builder.addMixIn(Tag.class, ExtensionsMixin.class);
        builder.addMixIn(XML.class, ExtensionsMixin.class);
        builder.addMixIn(Schema.class, SchemaMixin.class);
        builder.addMixIn(DateSchema.class, DateSchemaMixin.class);

        builder.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        builder.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        builder.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        builder.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        builder.defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL));
    }

}
