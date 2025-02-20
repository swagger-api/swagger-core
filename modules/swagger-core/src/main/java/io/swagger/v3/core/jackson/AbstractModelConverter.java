package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        mapper.registerModule(
                new SimpleModule("swagger", Version.unknownVersion()) {
                    @Override
                    public void setupModule(SetupContext context) {
                        context.insertAnnotationIntrospector(new SwaggerAnnotationIntrospector());
                    }
                });
        _mapper = mapper;
        _typeNameResolver = typeNameResolver;
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
        return _mapper.getSerializationConfig().getAnnotationIntrospector();
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
        // Check if the resolved type name already exists in the map
        if (_resolvedTypeNames.containsValue(name)) {
            // Determine if the type is from the Java standard library
            final boolean isFromJava = type.getRawClass().getCanonicalName().contains("java.lang.");
            // If the type is not a collection, map, primitive, or from Java standard library
            if (!type.isCollectionLikeType() && !type.isMapLikeType() && !type.isPrimitive() && !isFromJava) {
                // Iterate through the resolved type names to find conflicts
                for (Map.Entry<JavaType, String> entry : _resolvedTypeNames.entrySet()) {
                    JavaType key = entry.getKey();
                    String value = entry.getValue();
                    // If a conflict is found (same name but different type)
                    if (value.equals(name)) {
                        if (key != type) {
                            // Generate a new name postfix to resolve the conflict
                            name = genNamePostfix(name);
                        }
                    }
                }
            }
        }
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
            beanDesc = _mapper.getSerializationConfig().introspectClassAnnotations(type);
        }

        PropertyName rootName = _intr().findRootName(beanDesc.getClassInfo());
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
        if (cls != null) {

            if (java.util.Set.class.equals(cls)) {
                return true;
            } else {
                for (Class<?> a : cls.getInterfaces()) {
                    // this is dirty and ugly and needs to be extended into a scala model converter.  But to avoid bringing in scala runtime...
                    if (java.util.Set.class.equals(a) || "interface scala.collection.Set".equals(a.toString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String genNamePostfix(String name) {
        // Regular expression to match a number at the end of the string
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(name);

        if (matcher.find()) {
            // Extract the number, increment it, and replace it in the name
            String numberStr = matcher.group(1);
            int number = Integer.parseInt(numberStr);
            number++;
            return name.substring(0, matcher.start(1)) + number;
        } else {
            // If no number at the end, append "2"
            return name + "2";
        }
    }
}
