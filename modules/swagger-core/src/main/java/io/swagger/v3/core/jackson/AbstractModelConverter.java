package io.swagger.v3.core.jackson;

import tools.jackson.core.Version;
import tools.jackson.databind.AnnotationIntrospector;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyName;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.introspect.AccessorNamingStrategy;
import tools.jackson.databind.introspect.BasicBeanDescription;
import tools.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import tools.jackson.databind.jsontype.NamedType;
import tools.jackson.databind.module.SimpleModule;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractModelConverter implements ModelConverter {
    protected final ObjectMapper _mapper;
    protected final TypeNameResolver _typeNameResolver;
    /**
     * Minor optimization: no need to keep on resolving same types over and over
     * again.
     */
    protected Map<JavaType, String> _resolvedTypeNames = new ConcurrentHashMap<>();

    protected AbstractModelConverter(ObjectMapper mapper) {
        this (mapper, TypeNameResolver.std);
    }

    protected AbstractModelConverter(ObjectMapper mapper, TypeNameResolver typeNameResolver) {
        MapperBuilder<ObjectMapper, ?> builder = mapper.rebuild()
                .addModule(new SimpleModule("swagger", Version.unknownVersion()) {
                    @Override
                    public void setupModule(SetupContext context) {
                        context.insertAnnotationIntrospector(new SwaggerAnnotationIntrospector());
                    }
                });
        // Only impose Swagger's own accessor naming when the caller has not configured one.
        // Overwriting it unconditionally discards the caller's strategy, so schema property names
        // diverge from the JSON the caller's mapper actually produces -- e.g. properties read via
        // non-bean-convention accessors go missing from the schema.
        if (usesDefaultAccessorNaming(mapper)) {
            builder.accessorNaming(new DefaultAccessorNamingStrategy.Provider()
                    .withFirstCharAcceptance(true, true));
        }
        _mapper = builder.build();
        _typeNameResolver = typeNameResolver;
    }

    /**
     * Whether the mapper still carries Jackson's stock accessor naming provider, meaning the caller
     * expressed no preference of their own. A custom provider -- including a subclass of
     * {@link DefaultAccessorNamingStrategy.Provider} -- is left alone.
     *
     * @param mapper the mapper handed to this converter
     * @return true when Swagger's accessor naming may safely be applied
     */
    private static boolean usesDefaultAccessorNaming(ObjectMapper mapper) {
        AccessorNamingStrategy.Provider provider = mapper.serializationConfig().getAccessorNaming();
        return provider == null || provider.getClass() == DefaultAccessorNamingStrategy.Provider.class;
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the current AnnotationIntrospector from the ObjectMapper's serialization configuration.
     * We do not cache the value of _intr because users can load jackson modules later,
     * and we want to use their annotation inspection.
     * 
     * @return the current AnnotationIntrospector
     */
    protected AnnotationIntrospector _intr() {
        return _mapper.serializationConfig().getAnnotationIntrospector();
    }

    protected String _typeName(JavaType type) {
        return _typeName(type, null);
    }

    protected String _typeName(JavaType type, BeanDescription beanDesc) {
        String name = _resolvedTypeNames.get(type);
        if (name != null) {
            return name;
        }
        name = _findTypeName(type, beanDesc);
        _resolvedTypeNames.put(type, name);
        return name;
    }

    protected String _findTypeName(JavaType type, BeanDescription beanDesc) {
        // First, handle container types; they require recursion
        if (type.isArrayType()) {
            return "Array";
        }

        if (type.isMapLikeType() && ReflectionUtils.isSystemType(type)) {
            return "Map";
        }

        if (type.isContainerType() && ReflectionUtils.isSystemType(type)) {
            if (Set.class.isAssignableFrom(type.getRawClass())) {
                return "Set";
            }
            return "List";
        }
        if (beanDesc == null) {
            beanDesc = _mapper._serializationContext().introspectBeanDescription(type);
        }

        PropertyName rootName = _intr().findRootName(_mapper.serializationConfig(), beanDesc.getClassInfo());
        if (rootName != null && rootName.hasSimpleName()) {
            return rootName.getSimpleName();
        }
        return _typeNameResolver.nameForType(type);
    }

    protected String _typeQName(JavaType type) {
        return type.getRawClass().getName();
    }

    protected String _subTypeName(NamedType type) {
        return type.getType().getName();
    }

    protected boolean _isSetType(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        if (java.util.Set.class.isAssignableFrom(cls)) {
            return true;
        }
        // check for scala Set as well - to avoid bringing in scala runtime
        for (Class<?> a : cls.getInterfaces()) {
            if ("interface scala.collection.Set".equals(a.toString())) {
                return true;
            }
        }
        return false;
    }
}
