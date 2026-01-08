package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import io.swagger.v3.core.jackson.ExampleSerializer;
import io.swagger.v3.core.jackson.Schema31Serializer;
import io.swagger.v3.core.jackson.MediaTypeSerializer;
import io.swagger.v3.core.jackson.SchemaSerializer;
import io.swagger.v3.core.jackson.mixin.Components31Mixin;
import io.swagger.v3.core.jackson.mixin.ComponentsMixin;
import io.swagger.v3.core.jackson.mixin.DateSchemaMixin;
import io.swagger.v3.core.jackson.mixin.Discriminator31Mixin;
import io.swagger.v3.core.jackson.mixin.DiscriminatorMixin;
import io.swagger.v3.core.jackson.mixin.ExampleMixin;
import io.swagger.v3.core.jackson.mixin.ExtensionsMixin;
import io.swagger.v3.core.jackson.mixin.InfoMixin;
import io.swagger.v3.core.jackson.mixin.LicenseMixin;
import io.swagger.v3.core.jackson.mixin.MediaTypeMixin;
import io.swagger.v3.core.jackson.mixin.OpenAPI31Mixin;
import io.swagger.v3.core.jackson.mixin.OpenAPIMixin;
import io.swagger.v3.core.jackson.mixin.OperationMixin;
import io.swagger.v3.core.jackson.mixin.Schema31Mixin;
import io.swagger.v3.core.jackson.mixin.SchemaConverterMixin;
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
import io.swagger.v3.oas.models.media.Discriminator;
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
import tools.jackson.core.TokenStreamFactory;
import tools.jackson.databind.ser.ValueSerializerModifier;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLFactoryBuilder;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ObjectMapperFactory {

    public static ObjectMapper createJson(JsonFactory jsonFactory) {
        return create(jsonFactory, false);
    }

    public static ObjectMapper createJson() {
        return create(null, false);
    }

    public static ObjectMapper createJson(Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer) {
        return create(null, false, mapperBuilderCustomizer);
    }

    public static ObjectMapper createYaml(YAMLFactory yamlFactory) {
        return create(yamlFactory, false);
    }

    public static ObjectMapper createYaml() {
        return createYaml(false);
    }

    public static ObjectMapper createYaml(Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer) {
        return createYaml(false, mapperBuilderCustomizer);
    }

    public static ObjectMapper createYaml(boolean openapi31) {
        return createYaml(openapi31, mapperBuilder -> {});
    }

    public static ObjectMapper createYaml(boolean openapi31, Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer) {
        YAMLFactory factory = YAMLFactory.builder()
                .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
                .enable(YAMLWriteFeature.MINIMIZE_QUOTES)
                .enable(YAMLWriteFeature.SPLIT_LINES)
                .enable(YAMLWriteFeature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
                .build();

        return create(factory, openapi31, mapperBuilderCustomizer);
    }

    public static ObjectMapper createJson31(TokenStreamFactory jsonFactory) {
        return create(jsonFactory, true);
    }

    public static ObjectMapper createJson31() {
        return create(null, true);
    }

    public static ObjectMapper createJson31(Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer) {
        return create(null, true, mapperBuilderCustomizer);
    }

    public static ObjectMapper createYaml31(YAMLFactory yamlFactory) {
        return create(yamlFactory, true);
    }

    public static ObjectMapper createYaml31() {
        return createYaml(true);
    }

    public static ObjectMapper create(TokenStreamFactory jsonFactory, boolean openapi31) {
        return create(jsonFactory, openapi31, mapperBuilder -> {});
    }

    public static ObjectMapper create(TokenStreamFactory jsonFactory, boolean openapi31, Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer) {
        MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>> mapperBuilder;
        if (jsonFactory instanceof JsonFactory factory) {
            mapperBuilder = JsonMapper.builder(factory);
        } else if (jsonFactory instanceof YAMLFactory factory) {
            mapperBuilder = YAMLMapper.builder(factory);
        } else {
            mapperBuilder = new ObjectMapper().rebuild();
        }

        if (!openapi31) {
            // handle ref schema serialization skipping all other props
            mapperBuilder.addModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) {
                    super.setupModule(context);
                    context.addSerializerModifier(new ValueSerializerModifier() {
                        @Override
                        public ValueSerializer<?> modifySerializer(
                                SerializationConfig config, BeanDescription.Supplier desc, ValueSerializer<?> serializer) {
                            if (Schema.class.isAssignableFrom(desc.getBeanClass())) {
                                return new SchemaSerializer((ValueSerializer<Object>) serializer);
                            } else if (MediaType.class.isAssignableFrom(desc.getBeanClass())) {
                                return new MediaTypeSerializer((ValueSerializer<Object>) serializer);
                            } else if (Example.class.isAssignableFrom(desc.getBeanClass())) {
                                return new ExampleSerializer((ValueSerializer<Object>) serializer);
                            }
                            return serializer;
                        }
                    });
                }
            });
        } else {
            mapperBuilder.addModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) {
                    super.setupModule(context);
                    context.addSerializerModifier(new ValueSerializerModifier() {
                        @Override
                        public ValueSerializer<?> modifySerializer(
                                SerializationConfig config, BeanDescription.Supplier desc, ValueSerializer<?> serializer) {
                            if (Schema.class.isAssignableFrom(desc.getBeanClass())) {
                                return new Schema31Serializer((ValueSerializer<Object>) serializer);
                            } else if (MediaType.class.isAssignableFrom(desc.getBeanClass())) {
                                return new MediaTypeSerializer((ValueSerializer<Object>) serializer);
                            } else if (Example.class.isAssignableFrom(desc.getBeanClass())) {
                                return new ExampleSerializer((ValueSerializer<Object>) serializer);
                            }
                            return serializer;
                        }
                    });
                }
            });
        }

        if (!openapi31) {
            JacksonModule deserializerModule = new DeserializationModule();
            mapperBuilder.addModule(deserializerModule);
        } else {
            JacksonModule deserializerModule = new DeserializationModule31();
            mapperBuilder.addModule(deserializerModule);
        }

        Map<Class<?>, Class<?>> sourceMixins = new LinkedHashMap<>();

        sourceMixins.put(ApiResponses.class, ExtensionsMixin.class);
        sourceMixins.put(Contact.class, ExtensionsMixin.class);
        sourceMixins.put(Encoding.class, ExtensionsMixin.class);
        sourceMixins.put(EncodingProperty.class, ExtensionsMixin.class);
        sourceMixins.put(Example.class, ExampleMixin.class);
        sourceMixins.put(ExternalDocumentation.class, ExtensionsMixin.class);
        sourceMixins.put(Link.class, ExtensionsMixin.class);
        sourceMixins.put(LinkParameter.class, ExtensionsMixin.class);
        sourceMixins.put(MediaType.class, MediaTypeMixin.class);
        sourceMixins.put(OAuthFlow.class, ExtensionsMixin.class);
        sourceMixins.put(OAuthFlows.class, ExtensionsMixin.class);
        sourceMixins.put(Operation.class, OperationMixin.class);
        sourceMixins.put(PathItem.class, ExtensionsMixin.class);
        sourceMixins.put(Paths.class, ExtensionsMixin.class);
        sourceMixins.put(Scopes.class, ExtensionsMixin.class);
        sourceMixins.put(Server.class, ExtensionsMixin.class);
        sourceMixins.put(ServerVariable.class, ExtensionsMixin.class);
        sourceMixins.put(ServerVariables.class, ExtensionsMixin.class);
        sourceMixins.put(Tag.class, ExtensionsMixin.class);
        sourceMixins.put(XML.class, ExtensionsMixin.class);
        sourceMixins.put(ApiResponse.class, ExtensionsMixin.class);
        sourceMixins.put(Parameter.class, ExtensionsMixin.class);
        sourceMixins.put(RequestBody.class, ExtensionsMixin.class);
        sourceMixins.put(Header.class, ExtensionsMixin.class);
        sourceMixins.put(SecurityScheme.class, ExtensionsMixin.class);
        sourceMixins.put(Callback.class, ExtensionsMixin.class);


        if (!openapi31) {
            sourceMixins.put(Schema.class, SchemaMixin.class);
            sourceMixins.put(DateSchema.class, DateSchemaMixin.class);
            sourceMixins.put(Components.class, ComponentsMixin.class);
            sourceMixins.put(Info.class, InfoMixin.class);
            sourceMixins.put(License.class, LicenseMixin.class);
            sourceMixins.put(OpenAPI.class, OpenAPIMixin.class);
            sourceMixins.put(Discriminator.class, DiscriminatorMixin.class);
        } else {
            sourceMixins.put(Info.class, ExtensionsMixin.class);
            sourceMixins.put(Schema.class, Schema31Mixin.class);
            sourceMixins.put(Components.class, Components31Mixin.class);
            sourceMixins.put(OpenAPI.class, OpenAPI31Mixin.class);
            sourceMixins.put(DateSchema.class, DateSchemaMixin.class);
            sourceMixins.put(Discriminator.class, Discriminator31Mixin.class);
        }
        mapperBuilder.addMixIns(sourceMixins);
        mapperBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        mapperBuilder.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        mapperBuilder.changeDefaultPropertyInclusion(incl -> incl
                .withContentInclusion(JsonInclude.Include.NON_NULL)
                .withValueInclusion(JsonInclude.Include.NON_NULL));
        mapperBuilder.accessorNaming(new DefaultAccessorNamingStrategy.Provider()
                .withFirstCharAcceptance(true, true));

        mapperBuilderCustomizer.accept(mapperBuilder);

        return mapperBuilder.build();
    }

    public static ObjectMapper createJsonConverter() {

        JsonMapper.Builder builder = JsonMapper.builder();


        JacksonModule deserializerModule = new DeserializationModule();
        builder.addModule(deserializerModule);

        Map<Class<?>, Class<?>> sourceMixins = new LinkedHashMap<>();

        sourceMixins.put(ApiResponses.class, ExtensionsMixin.class);
        sourceMixins.put(ApiResponse.class, ExtensionsMixin.class);
        sourceMixins.put(Callback.class, ExtensionsMixin.class);
        sourceMixins.put(Components.class, ComponentsMixin.class);
        sourceMixins.put(Contact.class, ExtensionsMixin.class);
        sourceMixins.put(Encoding.class, ExtensionsMixin.class);
        sourceMixins.put(EncodingProperty.class, ExtensionsMixin.class);
        sourceMixins.put(Example.class, ExampleMixin.class);
        sourceMixins.put(ExternalDocumentation.class, ExtensionsMixin.class);
        sourceMixins.put(Header.class, ExtensionsMixin.class);
        sourceMixins.put(Info.class, ExtensionsMixin.class);
        sourceMixins.put(License.class, ExtensionsMixin.class);
        sourceMixins.put(Link.class, ExtensionsMixin.class);
        sourceMixins.put(LinkParameter.class, ExtensionsMixin.class);
        sourceMixins.put(MediaType.class, MediaTypeMixin.class);
        sourceMixins.put(OAuthFlow.class, ExtensionsMixin.class);
        sourceMixins.put(OAuthFlows.class, ExtensionsMixin.class);
        sourceMixins.put(OpenAPI.class, OpenAPIMixin.class);
        sourceMixins.put(Operation.class, OperationMixin.class);
        sourceMixins.put(Parameter.class, ExtensionsMixin.class);
        sourceMixins.put(PathItem.class, ExtensionsMixin.class);
        sourceMixins.put(Paths.class, ExtensionsMixin.class);
        sourceMixins.put(RequestBody.class, ExtensionsMixin.class);
        sourceMixins.put(Scopes.class, ExtensionsMixin.class);
        sourceMixins.put(SecurityScheme.class, ExtensionsMixin.class);
        sourceMixins.put(Server.class, ExtensionsMixin.class);
        sourceMixins.put(ServerVariable.class, ExtensionsMixin.class);
        sourceMixins.put(ServerVariables.class, ExtensionsMixin.class);
        sourceMixins.put(Tag.class, ExtensionsMixin.class);
        sourceMixins.put(XML.class, ExtensionsMixin.class);

        sourceMixins.put(Schema.class, SchemaConverterMixin.class);
        builder.addMixIns(sourceMixins);
        builder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        builder.changeDefaultPropertyInclusion(incl -> incl
                .withContentInclusion(JsonInclude.Include.NON_NULL)
                .withValueInclusion(JsonInclude.Include.NON_NULL));

        return builder.build();
    }


    public static ObjectMapper buildStrictGenericObjectMapper() {
        JsonMapper.Builder builder = JsonMapper.builder();
        builder.changeDefaultPropertyInclusion(incl -> incl
                .withContentInclusion(JsonInclude.Include.NON_NULL)
                .withValueInclusion(JsonInclude.Include.NON_NULL));
        return builder.build();
    }
}
