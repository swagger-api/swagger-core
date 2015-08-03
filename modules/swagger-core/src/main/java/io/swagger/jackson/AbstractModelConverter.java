package io.swagger.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

import javax.xml.bind.annotation.XmlElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractModelConverter implements ModelConverter {
    protected final ObjectMapper _mapper;
    protected final AnnotationIntrospector _intr;
    protected final TypeNameResolver _typeNameResolver = TypeNameResolver.std;
    /**
     * Minor optimization: no need to keep on resolving same types over and over
     * again.
     */
    protected Map<JavaType, String> _resolvedTypeNames = new ConcurrentHashMap<JavaType, String>();

    protected AbstractModelConverter(ObjectMapper mapper) {
        mapper.registerModule(
                new SimpleModule("swagger", Version.unknownVersion()) {
                    @Override
                    public void setupModule(SetupContext context) {
                        context.insertAnnotationIntrospector(new SwaggerAnnotationIntrospector());
                    }
                });
        _mapper = mapper;
        _intr = mapper.getSerializationConfig().getAnnotationIntrospector();

    }

    protected static Comparator<Property> getPropertyComparator() {
        return new Comparator<Property>() {
            @Override
            public int compare(Property one, Property two) {
                if (one.getPosition() == null && two.getPosition() == null) {
                    return 0;
                }
                if (one.getPosition() == null) {
                    return -1;
                }
                if (two.getPosition() == null) {
                    return 1;
                }
                return one.getPosition().compareTo(two.getPosition());
            }
        };
    }

    @Override
    public Property resolveProperty(Type type,
                                    ModelConverterContext context,
                                    Annotation[] annotations,
                                    Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolveProperty(type, context, annotations, chain);
        } else {
            return null;
        }
    }

    protected String _description(Annotated ann) {
        // while name suggests it's only for properties, should work for any Annotated thing.
        // also; with Swagger introspector's help, should get it from ApiModel/ApiModelProperty
        return _intr.findPropertyDescription(ann);
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

        if (type.isMapLikeType()) {
            return "Map";
        }

        if (type.isContainerType()) {
            if (Set.class.isAssignableFrom(type.getRawClass())) {
                return "Set";
            }
            return "List";
        }
        if (beanDesc == null) {
            beanDesc = _mapper.getSerializationConfig().introspectClassAnnotations(type);
        }

        PropertyName rootName = _intr.findRootName(beanDesc.getClassInfo());
        if (rootName != null && rootName.hasSimpleName()) {
            return rootName.getSimpleName();
        }
        return _typeNameResolver.nameForType(type);
    }

    protected String _typeQName(JavaType type) {
        return type.getRawClass().getName();
    }

    protected String _subTypeName(NamedType type) {
        // !!! TODO: should this use 'name' instead?
        return type.getType().getName();
    }

    protected String _findDefaultValue(Annotated a) {
        XmlElement elem = a.getAnnotation(XmlElement.class);
        if (elem != null) {
            if (!elem.defaultValue().isEmpty() && !"\u0000".equals(elem.defaultValue())) {
                return elem.defaultValue();
            }
        }
        return null;
    }

    protected String _findExampleValue(Annotated a) {
        ApiModelProperty prop = a.getAnnotation(ApiModelProperty.class);
        if (prop != null) {
            if (!prop.example().isEmpty()) {
                return prop.example();
            }
        }
        return null;
    }

    protected Boolean _findReadOnly(Annotated a) {
        ApiModelProperty prop = a.getAnnotation(ApiModelProperty.class);
        if (prop != null) {
            return prop.readOnly();
        }
        return null;
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

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }
}