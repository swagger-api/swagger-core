package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.ValueSerializer;
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
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectMapperFactory {

    private static final List<MapperCustomizer> CUSTOMIZERS = new CopyOnWriteArrayList<>();
    private static final AtomicLong GENERATION = new AtomicLong();

    protected ObjectMapperFactory() {
    }

    /**
     * Registers a {@link MapperCustomizer} that is applied, after swagger-core's own configuration, to every
     * mapper subsequently built by this factory. The cached mappers exposed by {@link Json}, {@link Yaml},
     * {@link Json31} and {@link Yaml31} are rebuilt lazily on next access, and the default
     * {@link io.swagger.v3.core.jackson.ModelResolver} held by {@link io.swagger.v3.core.converter.ModelConverters}
     * is refreshed, so customizers registered at any time take effect. Registering them at application startup,
     * before any mapper is used, avoids rebuilding.
     *
     * @param customizer the customizer to add
     * @since 3.0.0
     */
    public static void addCustomizer(MapperCustomizer customizer) {
        CUSTOMIZERS.add(Objects.requireNonNull(customizer, "customizer"));
        GENERATION.incrementAndGet();
    }

    /**
     * Removes a previously registered customizer.
     *
     * @param customizer the customizer to remove
     * @return {@code true} if it was registered
     * @since 3.0.0
     */
    public static boolean removeCustomizer(MapperCustomizer customizer) {
        boolean removed = CUSTOMIZERS.remove(customizer);
        if (removed) {
            GENERATION.incrementAndGet();
        }
        return removed;
    }

    /**
     * Removes all registered customizers, restoring swagger-core's default mapper configuration.
     *
     * @since 3.0.0
     */
    public static void clearCustomizers() {
        if (!CUSTOMIZERS.isEmpty()) {
            CUSTOMIZERS.clear();
            GENERATION.incrementAndGet();
        }
    }

    /**
     * @return an unmodifiable snapshot of the registered customizers, in registration order
     * @since 3.0.0
     */
    public static List<MapperCustomizer> getCustomizers() {
        return List.copyOf(CUSTOMIZERS);
    }

    /**
     * Registers a Jackson module with every mapper built by this factory. Shortcut for
     * {@code addCustomizer((builder, target) -> builder.addModule(module))}; this is the Jackson 3 replacement
     * for {@code Json.mapper().registerModule(module)} (and the same call on {@code Yaml}, {@code Json31} and
     * {@code Yaml31}).
     *
     * @param module the module to add
     * @since 3.0.0
     */
    public static void addModule(JacksonModule module) {
        Objects.requireNonNull(module, "module");
        addCustomizer((builder, target) -> builder.addModule(module));
    }

    /**
     * Registers several Jackson modules with every mapper built by this factory, see {@link #addModule(JacksonModule)}.
     *
     * @param modules the modules to add
     * @since 3.0.0
     */
    public static void addModules(JacksonModule... modules) {
        Objects.requireNonNull(modules, "modules");
        for (JacksonModule module : modules) {
            Objects.requireNonNull(module, "module");
        }
        addCustomizer((builder, target) -> {
            for (JacksonModule module : modules) {
                builder.addModule(module);
            }
        });
    }

    /**
     * A counter incremented every time the set of customizers changes. Holders of cached mappers (such as
     * {@link Json}) compare it with the generation they were built at to know when to rebuild.
     *
     * @return the current generation
     * @since 3.0.0
     */
    public static long generation() {
        return GENERATION.get();
    }

    /**
     * Applies all registered customizers to the given builder.
     *
     * @param builder the builder to customize
     * @param target  which mapper is being built
     * @since 3.0.0
     */
    protected static void applyCustomizers(MapperBuilder<?, ?> builder, MapperTarget target) {
        for (MapperCustomizer customizer : CUSTOMIZERS) {
            customizer.customize(builder, target);
        }
    }

    public static ObjectMapper createJson(JsonFactory jsonFactory) {
        return create(jsonFactory, false);
    }

    public static ObjectMapper createJson() {
        return create(null, false);
    }

    public static ObjectMapper createYaml(YAMLFactory yamlFactory) {
        return create(yamlFactory, false);
    }

    public static ObjectMapper createYaml() {
        return createYaml(false);
    }

    public static ObjectMapper createYaml(boolean openapi31) {
        YAMLFactory factory = YAMLFactory
                .builder()
                .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
                .enable(YAMLWriteFeature.MINIMIZE_QUOTES)
                .enable(YAMLWriteFeature.SPLIT_LINES)
                .enable(YAMLWriteFeature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
                .build();

        return create(factory, openapi31);
    }

    public static ObjectMapper createJson31(TokenStreamFactory jsonFactory) {
        return create(jsonFactory, true);
    }

    public static ObjectMapper createJson31() {
        return create(null, true);
    }

    public static ObjectMapper createYaml31(YAMLFactory yamlFactory) {
        return create(yamlFactory, true);
    }

    public static ObjectMapper createYaml31() {
        return createYaml(true);
    }

    public static ObjectMapper create(TokenStreamFactory jsonFactory, boolean openapi31) {
        MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>> mapperBuilder;
        if (jsonFactory instanceof JsonFactory factory) {
            mapperBuilder = JsonMapper.builder(factory);
        } else if (jsonFactory instanceof YAMLFactory factory) {
            mapperBuilder = YAMLMapper.builder(factory);
        } else if (jsonFactory == null) {
            mapperBuilder = new ObjectMapper().rebuild();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported TokenStreamFactory: " + jsonFactory.getClass().getName()
                    + ". Supported types: JsonFactory, YAMLFactory.");
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

        applyCustomizers(mapperBuilder, targetFor(jsonFactory, openapi31));
        return mapperBuilder.build();
    }

    private static MapperTarget targetFor(TokenStreamFactory factory, boolean openapi31) {
        boolean yaml = factory instanceof YAMLFactory;
        if (yaml) {
            return openapi31 ? MapperTarget.YAML31 : MapperTarget.YAML;
        }
        return openapi31 ? MapperTarget.JSON31 : MapperTarget.JSON;
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
        builder.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        builder.changeDefaultPropertyInclusion(incl -> incl
                .withContentInclusion(JsonInclude.Include.NON_NULL)
                .withValueInclusion(JsonInclude.Include.NON_NULL));
        builder.accessorNaming(new DefaultAccessorNamingStrategy.Provider()
                .withFirstCharAcceptance(true, true));

        applyCustomizers(builder, MapperTarget.JSON_CONVERTER);
        return builder.build();
    }

    /**
     * Builds a plain JSON mapper with no swagger-core modules or mixins, used to parse generic example values.
     * Registered {@link MapperCustomizer}s are intentionally <b>not</b> applied.
     *
     * @return a new mapper
     */
    public static ObjectMapper buildStrictGenericObjectMapper() {
        JsonMapper.Builder builder = JsonMapper.builder();
        builder.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        builder.changeDefaultPropertyInclusion(incl -> incl
                .withContentInclusion(JsonInclude.Include.NON_NULL)
                .withValueInclusion(JsonInclude.Include.NON_NULL));
        return builder.build();
    }

}
