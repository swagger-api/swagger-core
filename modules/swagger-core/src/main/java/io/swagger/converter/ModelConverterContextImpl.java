package io.swagger.converter;

import io.swagger.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
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
    private final HashMap<Type, Schema> modelByType;
    private final Set<Type> processedTypes;

    public ModelConverterContextImpl(List<ModelConverter> converters) {
        this.converters = converters;
        modelByName = new TreeMap<String, Schema>();
        modelByType = new HashMap<Type, Schema>();
        processedTypes = new HashSet<Type>();
    }

    public ModelConverterContextImpl(ModelConverter converter) {
        this(new ArrayList<ModelConverter>());
        converters.add(converter);
    }

    public Iterator<ModelConverter> getConverters() {
        return converters.iterator();
    }

    @Override
    public void defineModel(String name, Schema model) {
        defineModel(name, model, null, null);
    }

    @Override
    public void defineModel(String name, Schema model, Type type, String prevName) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("defineModel %s %s", name, model));
        }
        modelByName.put(name, model);

        if (StringUtils.isNotBlank(prevName)) {
            modelByName.remove(prevName);
        }

        if (type != null) {
            modelByType.put(type, model);
        }
    }

    public Map<String, Schema> getDefinedModels() {
        return Collections.unmodifiableMap(modelByName);
    }

    @Override
    public Schema resolve(Type type) {
        LOGGER.error("CONTEXT resolve" + type.getTypeName());
        if (type.equals(Class.class)) throw new RuntimeException("ASDDDDDDDDDDDDDD");
        if (processedTypes.contains(type)) {
            LOGGER.error("CONTEXT returning processed for " + type.getTypeName());
            return modelByType.get(type);
        } else {
            processedTypes.add(type);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("resolve %s", type));
        }
        Iterator<ModelConverter> converters = this.getConverters();
        Schema resolved = null;
        if (converters.hasNext()) {
            ModelConverter converter = converters.next();
            LOGGER.debug("trying extension " + converter);
            LOGGER.error("CONTEXT converter resolving for " + type.getTypeName());
            resolved = converter.resolve(type, this, converters);
        }
        if (resolved != null) {
            LOGGER.error("CONTEXT resolved not null for " + type.getTypeName());
            LOGGER.error("CONTEXT resolved class " + resolved.getClass().getName());
            LOGGER.error("CONTEXT resolved title " + resolved.getTitle());
            modelByType.put(type, resolved);

            Schema resolvedImpl = resolved;
            // TODO look at composed models
//            if (resolvedImpl instanceof ComposedModel) {
//                resolvedImpl = ((ComposedModel) resolved).getChild();
//            }
//            if (resolvedImpl instanceof ModelImpl) {
//                ModelImpl impl = (ModelImpl) resolvedImpl

//                if (impl.getName() != null) {
//                    modelByName.put(impl.getName(), resolved);
//                }
//            }
            if(resolvedImpl.getTitle() != null) {
                modelByName.put(resolvedImpl.getTitle(), resolved);
            }
        }

        return resolved;
    }
    @Override
    public Schema resolve(Type type, Annotation[] annotations) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("resolveProperty %s", type));
        }
        Iterator<ModelConverter> converters = this.getConverters();
        if (converters.hasNext()) {
            ModelConverter converter = converters.next();
            return converter.resolve(type, this, annotations, converters);
        }
        return null;
    }
}