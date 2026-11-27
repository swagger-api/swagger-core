package io.swagger.v3.core.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.util.ReferenceTypeUtils;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ModelConverterContextImpl implements ModelConverterContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelConverterContextImpl.class);

    private final List<ModelConverter> converters;
    private final Map<String, Schema> modelByName;
    private final HashMap<AnnotatedType, Schema> modelByType;
    private final Set<AnnotatedType> processedTypes;
    private final Map<String, JavaType> typeByName;

    public ModelConverterContextImpl(List<ModelConverter> converters) {
        this.converters = converters;
        modelByName = new TreeMap<>();
        modelByType = new HashMap<>();
        processedTypes = new HashSet<>();
        typeByName = new HashMap<>();
    }

    public ModelConverterContextImpl(ModelConverter converter) {
        this(new ArrayList<ModelConverter>());
        converters.add(converter);
    }

    @Override
    public Iterator<ModelConverter> getConverters() {
        return converters.iterator();
    }

    @Override
    public void defineModel(String name, Schema model) {
        AnnotatedType aType = null;
        defineModel(name, model, aType, null);
    }

    @Override
    public void defineModel(String name, Schema model, Type type, String prevName) {
        defineModel(name, model, new AnnotatedType().type(type), prevName);
    }
    @Override
    public void defineModel(String name, Schema model, AnnotatedType type, String prevName) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("defineModel %s %s", name, model));
        }
        modelByName.put(name, model);
        registerType(name, type);

        if (StringUtils.isNotBlank(prevName) && !prevName.equals(name)) {
            modelByName.remove(prevName);
            typeByName.remove(prevName);
        }

        if (type != null && type.getType() != null) {
            modelByType.put(type, model);
        }
    }

    @Override
    public Map<String, Schema> getDefinedModels() {
        return Collections.unmodifiableMap(modelByName);
    }

    @Override
    public Schema resolve(AnnotatedType type) {

        AnnotatedType aType = ReferenceTypeUtils.unwrapReference(type);
        if (aType != null) {
            return resolve(aType);
        }

        if (processedTypes.contains(type)) {
            return modelByType.get(type);
        } else {
            processedTypes.add(type);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("resolve %s from %s", type.getType(), System.identityHashCode(this)));
        }
        Iterator<ModelConverter> converters = this.getConverters();
        Schema resolved = null;
        if (converters.hasNext()) {
            ModelConverter converter = converters.next();
            LOGGER.trace("trying extension {}", converter);
            resolved = converter.resolve(type, this, converters);
        }
        if (resolved != null) {
            modelByType.put(type, resolved);

            Schema resolvedImpl = resolved;
            if (resolvedImpl.getName() != null) {
                modelByName.put(resolvedImpl.getName(), resolved);
            }
        } else {
            processedTypes.remove(type);
        }

        return resolved;
    }

    private void registerType(String name, AnnotatedType type) {
        if (name == null || type == null || type.getType() == null) {
            return;
        }
        JavaType javaType = TypeFactory.defaultInstance().constructType(type.getType());
        JavaType previousType = typeByName.putIfAbsent(name, javaType);
        if (previousType != null && !previousType.equals(javaType)) {
            LOGGER.warn("Schema name \"{}\" is used for {} and {}. The schema of {} replaces the schema of {}. "
                    + "Set @Schema(name = ...) on one of the types or configure a TypeNameResolver.",
                    name, previousType.toCanonical(), javaType.toCanonical(), javaType.toCanonical(), previousType.toCanonical());
            typeByName.put(name, javaType);
        }
    }
}
