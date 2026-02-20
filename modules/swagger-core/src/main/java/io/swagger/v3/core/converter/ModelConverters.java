package io.swagger.v3.core.converter;

import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModelConverters {
    private static ModelConverters SINGLETON = null;
    private static ModelConverters SINGLETON31 = null;
    static Logger LOGGER = LoggerFactory.getLogger(ModelConverters.class);
    private final List<ModelConverter> converters;
    private final Set<String> skippedPackages = new HashSet<>();
    private final Set<String> skippedClasses = new HashSet<>();

    public ModelConverters() {
        converters = new CopyOnWriteArrayList<>();
        converters.add(new ModelResolver(Json.mapper()));
    }

    public ModelConverters(boolean openapi31) {
        converters = new CopyOnWriteArrayList<>();
        if (openapi31) {
            converters.add(new ModelResolver(Json31.mapper()).openapi31(true));
        } else {
            converters.add(new ModelResolver(Json.mapper()));
        }
    }

    public ModelConverters(boolean openapi31, Schema.SchemaResolution schemaResolution) {
        converters = new CopyOnWriteArrayList<>();
        if (openapi31) {
            converters.add(new ModelResolver(Json31.mapper()).openapi31(true).schemaResolution(schemaResolution));
        } else {
            converters.add(new ModelResolver(Json.mapper()).schemaResolution(schemaResolution));
        }
    }

    public ModelConverters(Configuration configuration) {
        converters = new CopyOnWriteArrayList<>();
        boolean openapi31 =configuration != null && configuration.isOpenAPI31() != null && configuration.isOpenAPI31();
        if (openapi31) {
            converters.add(new ModelResolver(Json31.mapper()).configuration(configuration));
        } else {
            converters.add(new ModelResolver(Json.mapper()).configuration(configuration));
        }
    }

    public Set<String> getSkippedPackages() {
        return skippedPackages;
    }

    public static ModelConverters getInstance(boolean openapi31) {
        if (openapi31) {
            if (SINGLETON31 == null) {
                SINGLETON31 = new ModelConverters(openapi31);
                init(SINGLETON31);
            }
            return SINGLETON31;
        }
        if (SINGLETON == null) {
            SINGLETON = new ModelConverters(openapi31);
            init(SINGLETON);
        }
        return SINGLETON;
    }

    public static void reset() {
        synchronized (ModelConverters.class) {
            SINGLETON = null;
            SINGLETON31 = null;
        }
    }

    public static ModelConverters getInstance(boolean openapi31, Schema.SchemaResolution schemaResolution) {
        synchronized (ModelConverters.class) {
            if (openapi31) {
                if (SINGLETON31 == null) {
                    boolean applySchemaResolution = Boolean.parseBoolean(System.getProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY, "false")) || Boolean.parseBoolean(System.getenv(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY));
                    SINGLETON31 = new ModelConverters(openapi31, applySchemaResolution ? schemaResolution : Schema.SchemaResolution.DEFAULT);
                    init(SINGLETON31);
                }
                return SINGLETON31;
            }
            if (SINGLETON == null) {
                SINGLETON = new ModelConverters(openapi31, schemaResolution);
                init(SINGLETON);
            }
            return SINGLETON;
        }
    }

    public static ModelConverters getInstance(Configuration configuration) {
        synchronized (ModelConverters.class) {
            boolean openapi31 = configuration != null && configuration.isOpenAPI31() != null && configuration.isOpenAPI31();
            if (openapi31) {
                if (SINGLETON31 == null) {
                    boolean applySchemaResolution = Boolean.parseBoolean(System.getProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY, "false")) || Boolean.parseBoolean(System.getenv(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY));
                    if (!applySchemaResolution) {
                        configuration.schemaResolution(Schema.SchemaResolution.DEFAULT);
                    }
                    SINGLETON31 = new ModelConverters(configuration);
                    init(SINGLETON31);
                }
                return SINGLETON31;
            }
            if (SINGLETON == null) {
                SINGLETON = new ModelConverters(configuration);
                init(SINGLETON);
            }
            return SINGLETON;
        }
    }

    private static void init(ModelConverters converter) {
        converter.addPackageToSkip("java.lang");
        converter.addPackageToSkip("groovy.lang");

        ServiceLoader<ModelConverter> loader = ServiceLoader.load(ModelConverter.class);
        Iterator<ModelConverter> itr = loader.iterator();
        while (itr.hasNext()) {
            ModelConverter ext = itr.next();
            if (ext == null) {
                LOGGER.error("failed to load extension {}", ext);
            } else {
                converter.addConverter(ext);
                LOGGER.debug("adding ModelConverter: {}", ext);
            }
        }

    }
    public static ModelConverters getInstance() {
        return getInstance(false);
    }


    public void addConverter(ModelConverter converter) {
        converters.add(0, converter);
    }

    public void removeConverter(ModelConverter converter) {
        converters.remove(converter);
    }

    public List<ModelConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }

    public void addPackageToSkip(String pkg) {
        this.skippedPackages.add(pkg);
    }

    public void addClassToSkip(String cls) {
        LOGGER.warn("skipping class {}", cls);
        this.skippedClasses.add(cls);
    }

    public Map<String, Schema> read(Type type) {
        return read(new AnnotatedType().type(type));
    }

    public Map<String, Schema> read(AnnotatedType type) {
        Map<String, Schema> modelMap = new HashMap<>();
        if (shouldProcess(type.getType())) {
            ModelConverterContextImpl context = new ModelConverterContextImpl(
                    converters);
            Schema resolve = context.resolve(type);
            for (Entry<String, Schema> entry : context.getDefinedModels()
                    .entrySet()) {
                if (entry.getValue().equals(resolve)) {
                    modelMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return modelMap;
    }

    public Map<String, Schema> readAll(Type type) {
        return readAll(new AnnotatedType().type(type));
    }

    public Map<String, Schema> readAll(AnnotatedType type) {
        if (shouldProcess(type.getType())) {
            ModelConverterContextImpl context = new ModelConverterContextImpl(
                    converters);

            LOGGER.debug("ModelConverters readAll from {}", type);
            context.resolve(type);
            return context.getDefinedModels();
        }
        return new HashMap<>();
    }

    public ResolvedSchema readAllAsResolvedSchema(Type type) {
        return readAllAsResolvedSchema(new AnnotatedType().type(type));
    }
    public ResolvedSchema readAllAsResolvedSchema(AnnotatedType type) {
        if (shouldProcess(type.getType())) {
            return resolveAsResolvedSchema(type);
        }
        return null;
    }

    public ResolvedSchema resolveAsResolvedSchema(AnnotatedType type) {
        ModelConverterContextImpl context = new ModelConverterContextImpl(
                converters);

        ResolvedSchema resolvedSchema = new ResolvedSchema();
        resolvedSchema.schema = context.resolve(type);
        resolvedSchema.referencedSchemas = context.getDefinedModels();

        return resolvedSchema;
    }

    public boolean isRegisteredAsSkippedClass(String className) {
        return skippedClasses.contains(className);
    }

    private boolean shouldProcess(Type type) {
        final Class<?> cls = TypeFactory.defaultInstance().constructType(type).getRawClass();
        if (cls.isPrimitive()) {
            return false;
        }
        String className = cls.getName();
        for (String packageName : skippedPackages) {
            if (className.startsWith(packageName)) {
                return false;
            }
        }
        return !skippedClasses.contains(className);
    }
}
