package io.swagger.v3.core.converter;

import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
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
    private static final ModelConverters SINGLETON = new ModelConverters();
    static Logger LOGGER = LoggerFactory.getLogger(ModelConverters.class);
    private final List<ModelConverter> converters;
    private final Set<String> skippedPackages = new HashSet<String>();
    private final Set<String> skippedClasses = new HashSet<String>();

    public ModelConverters() {
        converters = new CopyOnWriteArrayList<ModelConverter>();
        converters.add(new ModelResolver(Json.mapper()));
    }

    public static ModelConverters getInstance() {
        return SINGLETON;
    }

    public void addConverter(ModelConverter converter) {
        converters.add(0, converter);
    }

    public void removeConverter(ModelConverter converter) {
        converters.remove(converter);
    }

    public void addPackageToSkip(String pkg) {
        this.skippedPackages.add(pkg);
    }

    public void addClassToSkip(String cls) {
        LOGGER.warn("skipping class " + cls);
        this.skippedClasses.add(cls);
    }

    public Map<String, Schema> read(Type type) {
        return read(new AnnotatedType().type(type));
    }

    public Map<String, Schema> read(AnnotatedType type) {
        Map<String, Schema> modelMap = new HashMap<String, Schema>();
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

            LOGGER.debug("ModelConverters readAll from " + type);
            context.resolve(type);
            return context.getDefinedModels();
        }
        return new HashMap<String, Schema>();
    }

    public ResolvedSchema readAllAsResolvedSchema(Type type) {
        return readAllAsResolvedSchema(new AnnotatedType().type(type));
    }
    public ResolvedSchema readAllAsResolvedSchema(AnnotatedType type) {
        if (shouldProcess(type.getType())) {
            ModelConverterContextImpl context = new ModelConverterContextImpl(
                    converters);

            ResolvedSchema resolvedSchema = new ResolvedSchema();
            resolvedSchema.schema = context.resolve(type);
            resolvedSchema.referencedSchemas = context.getDefinedModels();

            return resolvedSchema;
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
        for (String classToSkip : skippedClasses) {
            if (className.equals(classToSkip)) {
                return false;
            }
        }
        return true;
    }

    static {
        SINGLETON.skippedPackages.add("java.lang");

        ServiceLoader<ModelConverter> loader = ServiceLoader.load(ModelConverter.class);
        Iterator<ModelConverter> itr = loader.iterator();
        while (itr.hasNext()) {
            ModelConverter ext = itr.next();
            if (ext == null) {
                LOGGER.error("failed to load extension " + ext);
            } else {
                SINGLETON.addConverter(ext);
                LOGGER.debug("adding ModelConverter: " + ext);
            }
        }
    }
}
