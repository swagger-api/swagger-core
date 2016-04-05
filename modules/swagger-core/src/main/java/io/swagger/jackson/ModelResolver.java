package io.swagger.jackson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Iterables;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.*;
import io.swagger.models.properties.*;
import io.swagger.util.AllowableValues;
import io.swagger.util.AllowableValuesUtils;
import io.swagger.util.PrimitiveType;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

public class ModelResolver extends AbstractModelConverter implements ModelConverter {
    Logger LOGGER = LoggerFactory.getLogger(ModelResolver.class);

    public ModelResolver(ObjectMapper mapper) {
        super(mapper);
    }

    public ObjectMapper objectMapper() {
        return _mapper;
    }

    protected boolean shouldIgnoreClass(Type type) {
        if (type instanceof Class) {
            Class<?> cls = (Class<?>) type;
            if (cls.getName().equals("javax.ws.rs.Response")) {
                return true;
            }
        } else {
            if (type instanceof com.fasterxml.jackson.core.type.ResolvedType) {
                com.fasterxml.jackson.core.type.ResolvedType rt = (com.fasterxml.jackson.core.type.ResolvedType) type;
                LOGGER.debug("Can't check class " + type + ", " + rt.getRawClass().getName());
                if (rt.getRawClass().equals(Class.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Property resolveProperty(Type type,
                                    ModelConverterContext context,
                                    Annotation[] annotations,
                                    Iterator<ModelConverter> next) {
        if (this.shouldIgnoreClass(type)) {
            return null;
        }

        return resolveProperty(_mapper.constructType(type), context, annotations, next);
    }

    public Property resolveProperty(JavaType propType,
                                    ModelConverterContext context,
                                    Annotation[] annotations,
                                    Iterator<ModelConverter> next) {
        LOGGER.debug("resolveProperty " + propType);

        Property property = null;
        if (propType.isContainerType()) {
            JavaType keyType = propType.getKeyType();
            JavaType valueType = propType.getContentType();
            if (keyType != null && valueType != null) {
                property = new MapProperty().additionalProperties(context.resolveProperty(valueType, new Annotation[]{}));
            } else if (valueType != null) {
                ArrayProperty arrayProperty =
                        new ArrayProperty().items(context.resolveProperty(valueType, new Annotation[]{}));
                if (_isSetType(propType.getRawClass())) {
                    arrayProperty.setUniqueItems(true);
                }
                property = arrayProperty;
            }
        } else {
            property = PrimitiveType.createProperty(propType);
        }

        if (property == null) {
            if (propType.isEnumType()) {
                property = new StringProperty();
                _addEnumProps(propType.getRawClass(), property);
            } else if (_isOptionalType(propType)) {
                property = context.resolveProperty(propType.containedType(0), null);
            } else {
                // complex type
                Model innerModel = context.resolve(propType);
                if (innerModel instanceof ModelImpl) {
                    ModelImpl mi = (ModelImpl) innerModel;
                    property = new RefProperty(StringUtils.isNotEmpty(mi.getReference()) ? mi.getReference() : mi.getName());
                }
            }
        }

        return property;
    }

    private boolean _isOptionalType(JavaType propType) {
        return Arrays.asList("com.google.common.base.Optional", "java.util.Optional")
                .contains(propType.getRawClass().getCanonicalName());
    }

    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (this.shouldIgnoreClass(type)) {
            return null;
        }

        return resolve(_mapper.constructType(type), context, next);
    }

    protected void _addEnumProps(Class<?> propClass, Property property) {
        final boolean useIndex = _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        final boolean useToString = _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        @SuppressWarnings("unchecked")
        Class<Enum<?>> enumClass = (Class<Enum<?>>) propClass;
        for (Enum<?> en : enumClass.getEnumConstants()) {
            String n;
            if (useIndex) {
                n = String.valueOf(en.ordinal());
            } else if (useToString) {
                n = en.toString();
            } else {
                n = _intr.findEnumValue(en);
            }
            if (property instanceof StringProperty) {
                StringProperty sp = (StringProperty) property;
                sp._enum(n);
            }
        }
    }

    public Model resolve(JavaType type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (type.isEnumType() || PrimitiveType.fromType(type) != null) {
            // We don't build models for primitive types
            return null;
        }

        final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(type);
        // Couple of possibilities for defining
        String name = _typeName(type, beanDesc);

        if ("Object".equals(name)) {
            return new ModelImpl();
        }

        final ModelImpl model = new ModelImpl().type(ModelImpl.OBJECT).name(name)
                .description(_description(beanDesc.getClassInfo()));

        if (!type.isContainerType()) {
            // define the model here to support self/cyclic referencing of models
            context.defineModel(name, model, type, null);
        }

        if (type.isContainerType()) {
            // We treat collections as primitive types, just need to add models for values (if any)
            context.resolve(type.getContentType());
            return null;
        }
        // if XmlRootElement annotation, construct an Xml object and attach it to the model
        XmlRootElement rootAnnotation = beanDesc.getClassAnnotations().get(XmlRootElement.class);
        if (rootAnnotation != null && !"".equals(rootAnnotation.name()) && !"##default".equals(rootAnnotation.name())) {
            LOGGER.debug(rootAnnotation.toString());
            Xml xml = new Xml()
                    .name(rootAnnotation.name());
            if (rootAnnotation.namespace() != null && !"".equals(rootAnnotation.namespace()) && !"##default".equals(rootAnnotation.namespace())) {
                xml.namespace(rootAnnotation.namespace());
            }
            model.xml(xml);
        }
        final XmlAccessorType xmlAccessorTypeAnnotation = beanDesc.getClassAnnotations().get(XmlAccessorType.class);

        // see if @JsonIgnoreProperties exist
        Set<String> propertiesToIgnore = new HashSet<String>();
        JsonIgnoreProperties ignoreProperties = beanDesc.getClassAnnotations().get(JsonIgnoreProperties.class);
        if (ignoreProperties != null) {
            propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
        }

        final ApiModel apiModel = beanDesc.getClassAnnotations().get(ApiModel.class);
        String disc = (apiModel == null) ? "" : apiModel.discriminator();

        if (apiModel != null && StringUtils.isNotEmpty(apiModel.reference())) {
            model.setReference(apiModel.reference());
        }

        if (disc.isEmpty()) {
            // longer method would involve AnnotationIntrospector.findTypeResolver(...) but:
            JsonTypeInfo typeInfo = beanDesc.getClassAnnotations().get(JsonTypeInfo.class);
            if (typeInfo != null) {
                disc = typeInfo.property();
            }
        }
        if (!disc.isEmpty()) {
            model.setDiscriminator(disc);
        }

        List<Property> props = new ArrayList<Property>();
        for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
            Property property = null;
            String propName = propDef.getName();
            Annotation[] annotations = null;

            // hack to avoid clobbering properties with get/is names
            // it's ugly but gets around https://github.com/swagger-api/swagger-core/issues/415
            if (propDef.getPrimaryMember() != null) {
                java.lang.reflect.Member member = propDef.getPrimaryMember().getMember();
                if (member != null) {
                    String altName = member.getName();
                    if (altName != null) {
                        final int length = altName.length();
                        for (String prefix : Arrays.asList("get", "is")) {
                            final int offset = prefix.length();
                            if (altName.startsWith(prefix) && length > offset
                                    && !Character.isUpperCase(altName.charAt(offset))) {
                                propName = altName;
                                break;
                            }
                        }
                    }
                }
            }

            PropertyMetadata md = propDef.getMetadata();

            boolean hasSetter = false, hasGetter = false;
            if (propDef.getSetter() == null) {
                hasSetter = false;
            } else {
                hasSetter = true;
            }
            if (propDef.getGetter() != null) {
                JsonProperty pd = propDef.getGetter().getAnnotation(JsonProperty.class);
                if (pd != null) {
                    hasGetter = true;
                }
            }
            Boolean isReadOnly = null;
            if (!hasSetter & hasGetter) {
                isReadOnly = Boolean.TRUE;
            } else {
                isReadOnly = Boolean.FALSE;
            }

            final AnnotatedMember member = propDef.getPrimaryMember();

            if (member != null && !ignore(member, xmlAccessorTypeAnnotation, propName, propertiesToIgnore)) {
                List<Annotation> annotationList = new ArrayList<Annotation>();
                for (Annotation a : member.annotations()) {
                    annotationList.add(a);
                }

                annotations = annotationList.toArray(new Annotation[annotationList.size()]);

                ApiModelProperty mp = member.getAnnotation(ApiModelProperty.class);

                if(mp != null && mp.readOnly()) {
                  isReadOnly = mp.readOnly();
                }

                JavaType propType = member.getType(beanDesc.bindingsForBeanType());

                // allow override of name from annotation
                if (mp != null && !mp.name().isEmpty()) {
                    propName = mp.name();
                }

                if (mp != null && !mp.dataType().isEmpty()) {
                    String or = mp.dataType();

                    JavaType innerJavaType = null;
                    LOGGER.debug("overriding datatype from " + propType + " to " + or);

                    if (or.toLowerCase().startsWith("list[")) {
                        String innerType = or.substring(5, or.length() - 1);
                        ArrayProperty p = new ArrayProperty();
                        Property primitiveProperty = PrimitiveType.createProperty(innerType);
                        if (primitiveProperty != null) {
                            p.setItems(primitiveProperty);
                        } else {
                            innerJavaType = getInnerType(innerType);
                            p.setItems(context.resolveProperty(innerJavaType, annotations));
                        }
                        property = p;
                    } else if (or.toLowerCase().startsWith("map[")) {
                        int pos = or.indexOf(",");
                        if (pos > 0) {
                            String innerType = or.substring(pos + 1, or.length() - 1);
                            MapProperty p = new MapProperty();
                            Property primitiveProperty = PrimitiveType.createProperty(innerType);
                            if (primitiveProperty != null) {
                                p.setAdditionalProperties(primitiveProperty);
                            } else {
                                innerJavaType = getInnerType(innerType);
                                p.setAdditionalProperties(context.resolveProperty(innerJavaType, annotations));
                            }
                            property = p;
                        }
                    } else {
                        Property primitiveProperty = PrimitiveType.createProperty(or);
                        if (primitiveProperty != null) {
                            property = primitiveProperty;
                        } else {
                            innerJavaType = getInnerType(or);
                            property = context.resolveProperty(innerJavaType, annotations);
                        }
                    }
                    if (innerJavaType != null) {
                        context.resolve(innerJavaType);
                    }
                }

                // no property from override, construct from propType
                if (property == null) {
                    if (mp != null && StringUtils.isNotEmpty(mp.reference())) {
                        property = new RefProperty(mp.reference());
                    } else if (member.getAnnotation(JsonIdentityInfo.class) != null) {
                        property = GeneratorWrapper.processJsonIdentity(propType, context, _mapper,
                                member.getAnnotation(JsonIdentityInfo.class),
                                member.getAnnotation(JsonIdentityReference.class));
                    }
                    if (property == null) {
                        property = context.resolveProperty(propType, annotations);
                    }
                }

                if (property != null) {
                    property.setName(propName);

                    if (mp != null && !mp.access().isEmpty()) {
                        property.setAccess(mp.access());
                    }

                    Boolean required = md.getRequired();
                    if (required != null) {
                        property.setRequired(required);
                    }

                    String description = _intr.findPropertyDescription(member);
                    if (description != null && !"".equals(description)) {
                        property.setDescription(description);
                    }

                    Integer index = _intr.findPropertyIndex(member);
                    if (index != null) {
                        property.setPosition(index);
                    }
                    property.setDefault(_findDefaultValue(member));
                    property.setExample(_findExampleValue(member));
                    property.setReadOnly(_findReadOnly(member));

                    if (property.getReadOnly() == null) {
                        if (isReadOnly) {
                            property.setReadOnly(isReadOnly);
                        }
                    }
                    if (mp != null) {
                        final AllowableValues allowableValues = AllowableValuesUtils.create(mp.allowableValues());
                        if (allowableValues != null) {
                            final Map<PropertyBuilder.PropertyId, Object> args = allowableValues.asPropertyArguments();
                            PropertyBuilder.merge(property, args);
                        }
                    }
                    JAXBAnnotationsHelper.apply(member, property);
                    applyBeanValidatorAnnotations(property, annotations);
                    props.add(property);
                }
            }
        }


        Collections.sort(props, getPropertyComparator());

        Map<String, Property> modelProps = new LinkedHashMap<String, Property>();
        for (Property prop : props) {
            modelProps.put(prop.getName(), prop);
        }
        model.setProperties(modelProps);

        Class<?> parentClass = null;
        final ApiModel api = beanDesc.getClassInfo().getAnnotation(ApiModel.class);
        if (api != null) {
            parentClass = api.parent();
        }
        if(parentClass != null && parentClass != Void.class){
            model.getVendorExtensions().put("parent", parentClass);
        }

        /**
         * This must be done after model.setProperties so that the model's set
         * of properties is available to filter from any subtypes
         **/
        if (!resolveSubtypes(model, beanDesc, context)) {
            model.setDiscriminator(null);
        }

        return model;
    }

    protected boolean ignore(final Annotated member, final XmlAccessorType xmlAccessorTypeAnnotation, final String propName, final Set<String> propertiesToIgnore) {
        if (propertiesToIgnore.contains(propName)) {
            return true;
        }
        if (xmlAccessorTypeAnnotation == null) {
            return false;
        }
        if (xmlAccessorTypeAnnotation.value().equals(XmlAccessType.NONE)) {
            if (!member.hasAnnotation(XmlElement.class)) {
                return true;
            }
        }
        return false;
    }

    private enum GeneratorWrapper {
        PROPERTY(ObjectIdGenerators.PropertyGenerator.class) {
            @Override
            protected Property processAsProperty(String propertyName, JavaType type,
                                                 ModelConverterContext context, ObjectMapper mapper) {
                /*
                 * When generator = ObjectIdGenerators.PropertyGenerator.class and
                 * @JsonIdentityReference(alwaysAsId = false) then property is serialized
                 * in the same way it is done without @JsonIdentityInfo annotation.
                 */
                return null;
            }

            @Override
            protected Property processAsId(String propertyName, JavaType type,
                                           ModelConverterContext context, ObjectMapper mapper) {
                final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(type);
                for (BeanPropertyDefinition def : beanDesc.findProperties()) {
                    final String name = def.getName();
                    if (name != null && name.equals(propertyName)) {
                        final AnnotatedMember propMember = def.getPrimaryMember();
                        final JavaType propType = propMember.getType(beanDesc.bindingsForBeanType());
                        if (PrimitiveType.fromType(propType) != null) {
                            return PrimitiveType.createProperty(propType);
                        } else {
                            return context.resolveProperty(propType,
                                    Iterables.toArray(propMember.annotations(), Annotation.class));
                        }
                    }
                }
                return null;
            }
        },
        INT(ObjectIdGenerators.IntSequenceGenerator.class) {
            @Override
            protected Property processAsProperty(String propertyName, JavaType type,
                                                 ModelConverterContext context, ObjectMapper mapper) {
                Property id = new IntegerProperty();
                return process(id, propertyName, type, context);
            }

            @Override
            protected Property processAsId(String propertyName, JavaType type,
                                           ModelConverterContext context, ObjectMapper mapper) {
                return new IntegerProperty();
            }
        },
        UUID(ObjectIdGenerators.UUIDGenerator.class) {
            @Override
            protected Property processAsProperty(String propertyName, JavaType type,
                                                 ModelConverterContext context, ObjectMapper mapper) {
                Property id = new UUIDProperty();
                return process(id, propertyName, type, context);
            }

            @Override
            protected Property processAsId(String propertyName, JavaType type,
                                           ModelConverterContext context, ObjectMapper mapper) {
                return new UUIDProperty();
            }
        },
        NONE(ObjectIdGenerators.None.class) {
            // When generator = ObjectIdGenerators.None.class property should be processed as normal property.
            @Override
            protected Property processAsProperty(String propertyName, JavaType type,
                                                 ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }

            @Override
            protected Property processAsId(String propertyName, JavaType type,
                                           ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }
        };

        private final Class<? extends ObjectIdGenerator> generator;

        GeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
            this.generator = generator;
        }

        protected abstract Property processAsProperty(String propertyName, JavaType type,
                                                      ModelConverterContext context, ObjectMapper mapper);

        protected abstract Property processAsId(String propertyName, JavaType type,
                                                ModelConverterContext context, ObjectMapper mapper);

        public static Property processJsonIdentity(JavaType type, ModelConverterContext context,
                                                   ObjectMapper mapper, JsonIdentityInfo identityInfo,
                                                   JsonIdentityReference identityReference) {
            final GeneratorWrapper wrapper = identityInfo != null ? getWrapper(identityInfo.generator()) : null;
            if (wrapper == null) {
                return null;
            }
            if (identityReference != null && identityReference.alwaysAsId()) {
                return wrapper.processAsId(identityInfo.property(), type, context, mapper);
            } else {
                return wrapper.processAsProperty(identityInfo.property(), type, context, mapper);
            }
        }

        private static GeneratorWrapper getWrapper(Class<?> generator) {
            for (GeneratorWrapper value : GeneratorWrapper.values()) {
                if (value.generator.isAssignableFrom(generator)) {
                    return value;
                }
            }
            return null;
        }

        private static Property process(Property id, String propertyName, JavaType type,
                                        ModelConverterContext context) {
            id.setName(propertyName);
            final Model model = context.resolve(type);
            if (model instanceof ModelImpl) {
                ModelImpl mi = (ModelImpl) model;
                mi.getProperties().put(propertyName, id);
                return new RefProperty(StringUtils.isNotEmpty(mi.getReference())
                        ? mi.getReference() : mi.getName());
            }
            return null;
        }
    }

    protected void applyBeanValidatorAnnotations(Property property, Annotation[] annotations) {
        Map<String, Annotation> annos = new HashMap<String, Annotation>();
        if (annotations != null) {
            for (Annotation anno : annotations) {
                annos.put(anno.annotationType().getName(), anno);
            }
        }
        if (annos.containsKey("javax.validation.constraints.NotNull")) {
            property.setRequired(true);
        }
        if (annos.containsKey("javax.validation.constraints.Min")) {
            if (property instanceof AbstractNumericProperty) {
                Min min = (Min) annos.get("javax.validation.constraints.Min");
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                ap.setMinimum(new Double(min.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Max")) {
            if (property instanceof AbstractNumericProperty) {
                Max max = (Max) annos.get("javax.validation.constraints.Max");
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                ap.setMaximum(new Double(max.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Size")) {
            Size size = (Size) annos.get("javax.validation.constraints.Size");
            if (property instanceof AbstractNumericProperty) {
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                ap.setMinimum(new Double(size.min()));
                ap.setMaximum(new Double(size.max()));
            } else if (property instanceof StringProperty) {
                StringProperty sp = (StringProperty) property;
                sp.minLength(new Integer(size.min()));
                sp.maxLength(new Integer(size.max()));
            } else if (property instanceof ArrayProperty) {
                ArrayProperty sp = (ArrayProperty) property;
                sp.setMinItems(size.min());
                sp.setMaxItems(size.max());
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMin")) {
            DecimalMin min = (DecimalMin) annos.get("javax.validation.constraints.DecimalMin");
            if (property instanceof AbstractNumericProperty) {
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                if (min.inclusive()) {
                    ap.setMinimum(new Double(min.value()));
                } else {
                    ap.setExclusiveMinimum(!min.inclusive());
                }
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMax")) {
            DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
            if (property instanceof AbstractNumericProperty) {
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                if (max.inclusive()) {
                    ap.setMaximum(new Double(max.value()));
                } else {
                    ap.setExclusiveMaximum(!max.inclusive());
                }
            }
        }
        if (annos.containsKey("javax.validation.constraints.Pattern")) {
            Pattern pattern = (Pattern) annos.get("javax.validation.constraints.Pattern");
            if (property instanceof StringProperty) {
                StringProperty ap = (StringProperty) property;
                ap.setPattern(pattern.regexp());
            }
        }
    }

    protected JavaType getInnerType(String innerType) {
        try {
            Class<?> innerClass = ReflectionUtils.loadClassByName(innerType);
            if (innerClass != null) {
                TypeFactory tf = _mapper.getTypeFactory();
                return tf.constructType(innerClass);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean resolveSubtypes(ModelImpl model, BeanDescription bean, ModelConverterContext context) {
        final List<NamedType> types = _intr.findSubtypes(bean.getClassInfo());
        if (types == null) {
            return false;
        }
        int count = 0;
        final Class<?> beanClass = bean.getClassInfo().getAnnotated();
        for (NamedType subtype : types) {
            final Class<?> subtypeType = subtype.getType();
            if (!beanClass.isAssignableFrom(subtypeType)) {
                continue;
            }

            final Model subtypeModel = context.resolve(subtypeType);

            if (subtypeModel instanceof ModelImpl) {
                final ModelImpl impl = (ModelImpl) subtypeModel;

                // check if model name was inherited
                if (impl.getName().equals(model.getName())) {
                    impl.setName(_typeNameResolver.nameForType(_mapper.constructType(subtypeType),
                            TypeNameResolver.Options.SKIP_API_MODEL));
                }

                // remove shared properties defined in the parent
                final Map<String, Property> baseProps = model.getProperties();
                final Map<String, Property> subtypeProps = impl.getProperties();
                if (baseProps != null && subtypeProps != null) {
                    for (Map.Entry<String, Property> entry : baseProps.entrySet()) {
                        if (entry.getValue().equals(subtypeProps.get(entry.getKey()))) {
                            subtypeProps.remove(entry.getKey());
                        }
                    }
                }

                impl.setDiscriminator(null);
                ComposedModel child = new ComposedModel().parent(new RefModel(model.getName())).child(impl);
                context.defineModel(impl.getName(), child);
                ++count;
            }
        }
        return count != 0;
    }
}
