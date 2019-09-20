package io.swagger.converter;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Sets;
import io.swagger.jackson.ModelResolver;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
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

    public Property readAsProperty(Type type) {
        return readAsProperty(type, null);
    }

    public Property readAsProperty(Type type, JsonView jsonView) {
        ModelConverterContextImpl context = new ModelConverterContextImpl(converters);
        context.setJsonView(jsonView);
        return context.resolveProperty(type, null);
    }

    public Map<String, Model> read(Type type) {
        return read(type, null);
    }

    public Map<String, Model> read(Type type, JsonView jsonView) {
        Map<String, Model> modelMap = new HashMap<String, Model>();
        if (shouldProcess(type)) {
            ModelConverterContextImpl context = new ModelConverterContextImpl(
                    converters);
            context.setJsonView(jsonView);
            Model resolve = context.resolve(type);
            for (Entry<String, Model> entry : context.getDefinedModels()
                    .entrySet()) {
                if (entry.getValue().equals(resolve)) {
                    modelMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return modelMap;
    }

    public Map<String, Model> readAll(Type type) {
        return readAll(type, null);
    }

    public Map<String, Model> readAll(Type type, JsonView annotation) {
        if (shouldProcess(type)) {
            ModelConverterContextImpl context = new ModelConverterContextImpl(
                    converters);
            context.setJsonView(annotation);

            LOGGER.debug("ModelConverters readAll with JsonView annotation from " + type);
            context.resolve(type);
            Map<String, Model> definedModels = context.getDefinedModels();

            resolveAncestorModels(context, definedModels);

            defineInheritance(context, definedModels);

            return definedModels;
        }
        return new HashMap<String, Model>();
    }

    private void defineInheritance(ModelConverterContextImpl context, Map<String, Model> definedModels) {
        for (String key : definedModels.keySet()) {
            Model childModel = definedModels.get(key);

            if (childModel instanceof ModelImpl) {
                final ModelImpl impl = (ModelImpl) childModel;
                Class parent = impl.getParent();

                if (parent != null) {
                    String parentName = parent.getSimpleName();
                    Model parentModel = definedModels.get(parentName);

                    // remove shared properties defined in the parent
                    removeParentPropertiesFromChild(childModel, parentModel, definedModels);

                    impl.setDiscriminator(null);
                    ComposedModel child = new ComposedModel().parent(new RefModel(parentName)).child(impl);
                    context.defineModel(impl.getName(), child);
                }
            }
        }
    }

    private void resolveAncestorModels(ModelConverterContextImpl context, Map<String, Model> definedModels) {
        Set<Class> unresolvedParents;
        do {
            unresolvedParents = Sets.newHashSet();
            for (String key : definedModels.keySet()) {
                Model childModel = definedModels.get(key);
                if(childModel instanceof ModelImpl) {
                    Class parent = ((ModelImpl)childModel).getParent();
                    if (parent != null) {
                        String parentName = parent.getSimpleName();
                        Model parentModel = definedModels.get(parentName);

                        if (parentModel == null) {
                            // Parent was not defined as a model
                            unresolvedParents.add(parent);
                        }
                    }
                }
            }

            for (Class unresolvedParent : unresolvedParents) {
                context.resolve(unresolvedParent);
            }
        } while (!unresolvedParents.isEmpty());
    }

    private void removeParentPropertiesFromChild(Model child, Model parent, Map<String, Model> definedModels) {
        if(parent != null) {
            while (parent instanceof ComposedModel){
                parent = ((ComposedModel)parent).getChild();
            }
            final Map<String, Property> baseProps = parent.getProperties();
            final Map<String, Property> subtypeProps = child.getProperties();
            if (baseProps != null && subtypeProps != null) {
                for (Entry<String, Property> entry : baseProps.entrySet()) {
                    if (entry.getValue().equals(subtypeProps.get(entry.getKey()))) {
                        subtypeProps.remove(entry.getKey());
                    }
                }
            }

            if (parent instanceof ModelImpl) {
                ModelImpl parentImpl = (ModelImpl) parent;
                if (parentImpl.getParent() != null) {
                    Class nextParentClass = parentImpl.getParent();
                    String nextParentName = nextParentClass.getSimpleName();
                    Model nextParent = definedModels.get(nextParentName);

                    removeParentPropertiesFromChild(child, nextParent, definedModels);
                }

            }
        }
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
