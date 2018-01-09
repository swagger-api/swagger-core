package io.swagger.converter;

import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.Property;
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
    private final Map<String, Model> modelByName;
    private final HashMap<Type, Model> modelByType;
    private final Set<Type> processedTypes;

    public ModelConverterContextImpl(List<ModelConverter> converters) {
        this.converters = converters;
        modelByName = new TreeMap<String, Model>();
        modelByType = new HashMap<Type, Model>();
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
    public void defineModel(String name, Model model) {
        defineModel(name, model, null, null);
    }

    @Override
    public void defineModel(String name, Model model, Type type, String prevName) {
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

    public Map<String, Model> getDefinedModels() {
        return Collections.unmodifiableMap(modelByName);
    }

    @Override
    public Property resolveProperty(Type type, Annotation[] annotations) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("resolveProperty %s", type));
        }
        Iterator<ModelConverter> converters = this.getConverters();
        if (converters.hasNext()) {
            ModelConverter converter = converters.next();
            return converter.resolveProperty(type, this, annotations, converters);
        }
        return null;
    }

    @Override
    public Model resolve(Type type) {
        if (processedTypes.contains(type)) {
            return modelByType.get(type);
        } else {
            processedTypes.add(type);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("resolve %s", type));
        }
        Iterator<ModelConverter> converters = this.getConverters();
        Model resolved = null;
        if (converters.hasNext()) {
            ModelConverter converter = converters.next();
            LOGGER.debug("trying extension " + converter);
            resolved = converter.resolve(type, this, converters);
        }
        if (resolved != null) {
            modelByType.put(type, resolved);

            Model resolvedImpl = resolved;
            if (resolvedImpl instanceof ComposedModel) {
                resolvedImpl = ((ComposedModel) resolved).getChild();
            }
            if (resolvedImpl instanceof ModelImpl) {
                ModelImpl impl = (ModelImpl) resolvedImpl;
                if (impl.getName() != null) {
                    modelByName.put(impl.getName(), resolved);
                }
            }
        }

        return resolved;
    }
}