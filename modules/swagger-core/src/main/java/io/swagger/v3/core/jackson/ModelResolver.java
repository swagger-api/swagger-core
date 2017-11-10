package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Constants;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.media.XML;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.core.util.RefUtils.constructRef;

public class ModelResolver extends AbstractModelConverter implements ModelConverter {
    Logger LOGGER = LoggerFactory.getLogger(ModelResolver.class);

    public ModelResolver(ObjectMapper mapper) {
        super(mapper);
    }

    public ObjectMapper objectMapper() {
        return _mapper;
    }

    @Override
    public Schema resolve(Type type,
                          ModelConverterContext context,
                          Annotation[] annotations,
                          Iterator<ModelConverter> next) {
        if (this.shouldIgnoreClass(type)) {
            return null;
        }

        return resolveProperty(_mapper.constructType(type), context, annotations, next);
    }

    @Override
    public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (this.shouldIgnoreClass(type)) {
            return null;
        }

        return resolve(_mapper.constructType(type), context, next);
    }

    @Override
    public Schema resolveAnnotatedType(
            Type type,
            Annotated member,
            String elementName,
            Schema parent,
            BiFunction<JavaType, Annotation[], Schema> jsonUnwrappedHandler,
            ModelConverterContext context,
            Iterator<ModelConverter> next) {

        final JavaType javaType;
        if (type instanceof JavaType) {
            javaType = (JavaType)type;
        } else {
            javaType = _mapper.constructType(type);
        }

        String name = elementName;
        Schema property = null;
        Annotation[] annotations = null;

        List<Annotation> annotationList = new ArrayList<Annotation>();
        for (Annotation a : member.annotations()) {
            annotationList.add(a);
        }

        annotations = annotationList.toArray(new Annotation[annotationList.size()]);

        io.swagger.v3.oas.annotations.media.Schema mp = null;

        io.swagger.v3.oas.annotations.media.ArraySchema as = member.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);
        if (as != null) {
            mp = as.schema();
        } else {
            mp = member.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        }

        // allow override of name from annotation
        if (mp != null && !mp.name().isEmpty()) {
            name = mp.name();
        }

        if (mp != null && !Void.class.equals(mp.implementation())) {
            Class<?> cls = mp.implementation();

            LOGGER.debug("overriding datatype from {} to {}", javaType, cls.getName());

            Schema primitiveProperty = PrimitiveType.createProperty(cls);
            if (primitiveProperty != null) {
                property = primitiveProperty;
            } else {
                property = context.resolve(cls, annotations);
            }
        }

        // no property from override, construct from propType
        if (property == null) {
            if (mp != null && StringUtils.isNotEmpty(mp.ref())) {
                property = new Schema().$ref(mp.ref());
/*
                    } else if (member.getAnnotation(JsonIdentityInfo.class) != null) {
                        // TODO #2312
                        property = GeneratorWrapper.processJsonIdentity(propType, context, _mapper,
                                member.getAnnotation(JsonIdentityInfo.class),
                                member.getAnnotation(JsonIdentityReference.class));
*/
            }
            if (property == null) {
                if (mp != null && StringUtils.isNotEmpty(mp.type())) {
                    PrimitiveType primitiveType = PrimitiveType.fromTypeAndFormat(mp.type(), mp.format());
                    if (primitiveType == null) {
                        primitiveType = PrimitiveType.fromType(javaType);
                    }
                    if (primitiveType != null) {
                        property = primitiveType.createProperty();
                    }
                } else {
                    PrimitiveType primitiveType = PrimitiveType.fromType(javaType);
                    if (primitiveType != null) {
                        property = primitiveType.createProperty();
                    }
                }
                if (property == null) {
                    if (jsonUnwrappedHandler != null) {
                        property = jsonUnwrappedHandler.apply(javaType, annotations);
                    } else {
                        property = context.resolve(javaType, annotations);
                    }
                }
            }
        }

        if (property != null) {
            property.setName(name);
            if (property.get$ref() == null) {
                resolveSchemaMembers(property, member);

                JAXBAnnotationsHelper.apply(member, property);
                applyBeanValidatorAnnotations(property, annotations, parent);
            }
        }
        if (AnnotationsUtils.hasArrayAnnotation(as)) {
            ArraySchema arraySchema = AnnotationsUtils.getArraySchema(as).get();
            arraySchema.setName(name);
            arraySchema.setItems(property);
            return arraySchema;
        }

        return property;
    }

    public Schema resolve(JavaType type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (type.isEnumType()) {
            // We don't build models for enum types
            return null;
        } else if (PrimitiveType.fromType(type) != null) {
            return PrimitiveType.fromType(type).createProperty();
        }

        final io.swagger.v3.oas.annotations.media.Schema declaredSchemaAnnotation = type.getRawClass().getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(type);

        String name = _typeName(type, beanDesc);

        if (declaredSchemaAnnotation != null &&
                StringUtils.isNotEmpty(declaredSchemaAnnotation.type()) &&
                !declaredSchemaAnnotation.type().equals("object")) {
            if (PrimitiveType.fromName(declaredSchemaAnnotation.type()) != null) {
                Schema primitive = PrimitiveType.fromName(declaredSchemaAnnotation.type()).createProperty();
                resolveSchemaMembers(primitive, beanDesc.getClassInfo());
                XML xml = resolveXml(beanDesc.getClassInfo());
                if (xml != null) {
                    primitive.xml(xml);
                }

                return primitive;
            }
        }
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotationReference = null;
        io.swagger.v3.oas.annotations.media.ArraySchema declaredArraySchemaAnnotation = type.getRawClass().getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);
        if (declaredArraySchemaAnnotation != null) {
            schemaAnnotationReference = declaredArraySchemaAnnotation.schema();
        } else {
            schemaAnnotationReference = declaredSchemaAnnotation;
        }

        if (schemaAnnotationReference != null && !Void.class.equals(schemaAnnotationReference.implementation())) {
            Class<?> cls = schemaAnnotationReference.implementation();

            LOGGER.debug("overriding datatype from {} to {}", type, cls.getName());

            if (declaredArraySchemaAnnotation != null) {
                ArraySchema schema = new ArraySchema();
                Schema innerSchema = null;

                Schema primitive = PrimitiveType.createProperty(cls);
                if (primitive != null) {
                    innerSchema = primitive;
                } else {
                    innerSchema = context.resolve(cls);
                }
                schema.setItems(innerSchema);
                return schema;
            } else {
                return context.resolve(cls);
            }
        }

        if ("Object".equals(name)) {
            return new Schema();
        }

        /**
         * --Preventing parent/child hierarchy creation loops - Comment 1--
         * Creating a parent model will result in the creation of child models. Creating a child model will result in
         * the creation of a parent model, as per the second If statement following this comment.
         *
         * By checking whether a model has already been resolved (as implemented below), loops of parents creating
         * children and children creating parents can be short-circuited. This works because currently the
         * ModelConverterContextImpl will return null for a class that already been processed, but has not yet been
         * defined. This logic works in conjunction with the early immediate definition of model in the context
         * implemented later in this method (See "Preventing parent/child hierarchy creation loops - Comment 2") to
         * prevent such
         */
        Schema resolvedModel = context.resolve(type.getRawClass());
        if (resolvedModel != null) {
            if (name.equals(resolvedModel.getName())) {
                return resolvedModel;
            }
        }

        List<Class<?>> composedSchemaReferencedClasses = getComposedSchemaReferencedClasses(type.getRawClass());
        boolean isComposedSchema = composedSchemaReferencedClasses != null;
        final Schema model;
        if (isComposedSchema) {
            model = new ComposedSchema()
                    .type("object")
                    .name(name);
        } else if (type.isContainerType()) {
            JavaType keyType = type.getKeyType();
            JavaType valueType = type.getContentType();
            if (keyType != null && valueType != null) {
                if (ReflectionUtils.isSystemType(type)) {
                    context.resolve(valueType);
                    return null;
                }
                Schema mapModel = new MapSchema().additionalProperties(context.resolve(valueType, new Annotation[]{}));
                mapModel.name(name);
                model = mapModel;
            } else if (valueType != null) {
                if (ReflectionUtils.isSystemType(type)) {
                    context.resolve(valueType);
                    return null;
                }
                Schema items = context.resolve(valueType, new Annotation[]{});
                Schema arrayModel =
                        new ArraySchema().items(items);
                if (_isSetType(type.getRawClass())) {
                    arrayModel.setUniqueItems(true);
                }
                arrayModel.name(name);
                model = arrayModel;
            } else {
                if (ReflectionUtils.isSystemType(type)) {
                    return null;
                }
                model = new Schema()
                        .type("object")
                        .name(name);
            }
        } else {
            model = new Schema()
                    .type("object")
                    .name(name);
        }

        if (!type.isContainerType()) {
            // define the model here to support self/cyclic referencing of models
            context.defineModel(name, model, type, null);
        }

        XML xml = resolveXml(beanDesc.getClassInfo());
        if (xml != null) {
            model.xml(xml);
        }

        resolveSchemaMembers(model, beanDesc.getClassInfo());

        final XmlAccessorType xmlAccessorTypeAnnotation = beanDesc.getClassAnnotations().get(XmlAccessorType.class);

        // see if @JsonIgnoreProperties exist
        Set<String> propertiesToIgnore = new HashSet<String>();
        JsonIgnoreProperties ignoreProperties = beanDesc.getClassAnnotations().get(JsonIgnoreProperties.class);
        if (ignoreProperties != null) {
            propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
        }

        List<Schema> props = new ArrayList<Schema>();
        Map<String, Schema> modelProps = new LinkedHashMap<String, Schema>();

        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        List<String> ignoredProps = getIgnoredProperties(beanDesc);
        properties.removeIf(p -> ignoredProps.contains(p.getName()));
        for (BeanPropertyDefinition propDef : properties) {
            Schema property = null;
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
            try {
                if (propDef.getSetter() == null) {
                    hasSetter = false;
                } else {
                    hasSetter = true;
                }
            } catch (IllegalArgumentException e) {
                //com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder would throw IllegalArgumentException
                // if there are overloaded setters. If we only want to know whether a set method exists, suppress the exception
                // is reasonable.
                // More logs might be added here
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

                JavaType propType = member.getType();
                property = context.resolveAnnotatedType(propType, member, propName, model, (t, a) -> {
                    JsonUnwrapped uw = member.getAnnotation(JsonUnwrapped.class);
                    if (uw != null && uw.enabled()) {
                        handleUnwrapped(props, context.resolve(t), uw.prefix(), uw.suffix());
                        return null;
                    } else {
                        return context.resolve(t, a);
                    }
                }, context, next);

                if (property != null) {
                    if (property.get$ref() == null) {
                        Boolean required = md.getRequired();
                        if (required != null && !Boolean.FALSE.equals(required)) {
                            addRequiredItem(model, propName);
                        }
                        if (property.getReadOnly() == null) {
                            if (isReadOnly) {
                                property.readOnly(isReadOnly);
                            }
                        }
                    }
                    props.add(property);
                    modelProps.put(property.getName(), property);
                }
            }
        }
        for (Schema prop : props) {
            modelProps.put(prop.getName(), prop);
        }
        if (modelProps.size() > 0) {
            model.setProperties(modelProps);
        }

        /**
         * --Preventing parent/child hierarchy creation loops - Comment 2--
         * Creating a parent model will result in the creation of child models, as per the first If statement following
         * this comment. Creating a child model will result in the creation of a parent model, as per the second If
         * statement following this comment.
         *
         * The current model must be defined in the context immediately. This done to help prevent repeated
         * loops where  parents create children and children create parents when a hierarchy is present. This logic
         * works in conjunction with the "early checking" performed earlier in this method
         * (See "Preventing parent/child hierarchy creation loops - Comment 1"), to prevent repeated creation loops.
         *
         *
         * As an aside, defining the current model in the context immediately also ensures that child models are
         * available for modification by resolveSubtypes, when their parents are created.
         */
        Class<?> currentType = type.getRawClass();
        context.defineModel(name, model, currentType, null);

        /**
         * This must be done after model.setProperties so that the model's set
         * of properties is available to filter from any subtypes
         **/
        if (!resolveSubtypes(model, beanDesc, context)) {
            model.setDiscriminator(null);
        }

        Discriminator discriminator = resolveDiscriminator(type, context);
        if (discriminator != null) {
            model.setDiscriminator(discriminator);
        }

        if (schemaAnnotationReference != null) {
            String ref = schemaAnnotationReference.ref();
            // consider ref as is
            if (!StringUtils.isBlank(ref)) {
                model.$ref(ref);
            }
            Class<?> not = schemaAnnotationReference.not();
            if (!Void.class.equals(not)) {
                model.not((new Schema().$ref(context.resolve(not).getName())));
            }
            if (schemaAnnotationReference.requiredProperties() != null &&
                    schemaAnnotationReference.requiredProperties().length > 0 &&
                    StringUtils.isNotBlank(schemaAnnotationReference.requiredProperties()[0])) {
                for (String prop : schemaAnnotationReference.requiredProperties()) {
                    addRequiredItem(model, prop);
                }
            }
        }

        if (isComposedSchema) {

            ComposedSchema composedSchema = (ComposedSchema) model;

            Class<?>[] allOf = schemaAnnotationReference.allOf();
            Class<?>[] anyOf = schemaAnnotationReference.anyOf();
            Class<?>[] oneOf = schemaAnnotationReference.oneOf();

            List<Class<?>> allOfFiltered = Stream.of(allOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            allOfFiltered.forEach(c -> {
                Schema allOfRef = context.resolve(c);
                Schema refSchema = new Schema().$ref(allOfRef.getName());
                // allOf could have already being added during subtype resolving
                if (composedSchema.getAllOf() == null || !composedSchema.getAllOf().contains(refSchema)) {
                    composedSchema.addAllOfItem(refSchema);
                }
                // remove shared properties defined in the parent
                if (isSubtype(beanDesc.getClassInfo(), c)) {
                    removeParentProperties(composedSchema, allOfRef);
                }
            });

            List<Class<?>> anyOfFiltered = Stream.of(anyOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            anyOfFiltered.forEach(c -> {
                Schema anyOfRef = context.resolve(c);
                composedSchema.addAnyOfItem(new Schema().$ref(anyOfRef.getName()));
                // remove shared properties defined in the parent
                if (isSubtype(beanDesc.getClassInfo(), c)) {
                    removeParentProperties(composedSchema, anyOfRef);
                }

            });

            List<Class<?>> oneOfFiltered = Stream.of(oneOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            oneOfFiltered.forEach(c -> {
                Schema oneOfRef = context.resolve(c);
                composedSchema.addOneOfItem(new Schema().$ref(oneOfRef.getName()));
                // remove shared properties defined in the parent
                if (isSubtype(beanDesc.getClassInfo(), c)) {
                    removeParentProperties(composedSchema, oneOfRef);
                }

            });

        }

        return model;
    }

    private boolean isSubtype(AnnotatedClass childClass, Class<?> parentClass) {
        final BeanDescription parentDesc = _mapper.getSerializationConfig().introspectClassAnnotations(parentClass);
        List<NamedType> subTypes =_intr.findSubtypes(parentDesc.getClassInfo());
        if (subTypes == null) {
            return false;
        }
        for (NamedType subtype : subTypes) {
            final Class<?> subtypeType = subtype.getType();
            if (childClass.getRawType().isAssignableFrom(subtypeType)) {
                return true;
            }
        }
        return false;
    }

    public Schema resolveProperty(JavaType propType,
                                  ModelConverterContext context,
                                  Annotation[] annotations,
                                  Iterator<ModelConverter> next) {
        LOGGER.debug("resolveProperty {}", propType);

        Schema property = null;
        if (propType.isContainerType()) {
            JavaType keyType = propType.getKeyType();
            JavaType valueType = propType.getContentType();
            if (keyType != null && valueType != null) {
                property = new MapSchema().additionalProperties(context.resolve(valueType, new Annotation[]{}));
            } else if (valueType != null) {
                Schema items = context.resolve(valueType, new Annotation[]{});
                // If property is XmlElement annotated, then use the name provided by annotation | https://github.com/swagger-api/swagger-core/issues/2047
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof XmlElement) {
                            XmlElement xmlElement = (XmlElement) annotation;
                            if (xmlElement != null && xmlElement.name() != null && !"".equals(xmlElement.name()) && !"##default".equals(xmlElement.name())) {
                                XML xml = items.getXml() != null ? items.getXml() : new XML();
                                xml.setName(xmlElement.name());
                                items.setXml(xml);
                            }
                        }
                    }
                }
                Schema arrayProperty =
                        new ArraySchema().items(items);
                if (_isSetType(propType.getRawClass())) {
                    arrayProperty.setUniqueItems(true);
                }
                property = arrayProperty;
            }
        } else {
            io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = getSchemaAnnotation(annotations);
            if (schemaAnnotation != null) {
                String format = schemaAnnotation.format();
                String type = schemaAnnotation.type();
                // handle strings with format
                if (!StringUtils.isBlank(schemaAnnotation.format()) &&
                        propType.getRawClass().isAssignableFrom(String.class)) {
                    type = "string";
                }
                if (StringUtils.isNotBlank(type)) {
                    PrimitiveType primitiveType = PrimitiveType.fromTypeAndFormat(type, format);
                    if (primitiveType == null) {
                        primitiveType = PrimitiveType.fromType(propType);
                    }
                    if (primitiveType != null) {
                        property = primitiveType.createProperty();
                    }
                } else {
                    PrimitiveType primitiveType = PrimitiveType.fromType(propType);
                    if (primitiveType != null) {
                        property = primitiveType.createProperty();
                    }
                }
            } else {
                property = PrimitiveType.createProperty(propType);
            }
        }

        if (property == null) {
            if (propType.isEnumType()) {
                property = new StringSchema();
                _addEnumProps(propType.getRawClass(), property);
            } else if (_isOptionalType(propType)) {
                property = context.resolve(propType.containedType(0), null);
            } else {
                // complex type
                Schema mi = context.resolve(propType);
                if (mi != null) {
                    if ("object".equals(mi.getType())) {
                        // create a reference for the property
                        final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(propType);
                        String name = _typeName(propType, beanDesc);
                        property = new Schema().$ref(constructRef(name));
                    } else if (mi.get$ref() != null) {
                        property = new Schema().$ref(StringUtils.isNotEmpty(mi.get$ref()) ? mi.get$ref() : mi.getName());
                    } else {
                        property = mi;
                    }
                }
            }
        }
        return property;
    }



    private boolean _isOptionalType(JavaType propType) {
        return Arrays.asList("com.google.common.base.Optional", "java.util.Optional")
                .contains(propType.getRawClass().getCanonicalName());
    }

    protected void _addEnumProps(Class<?> propClass, Schema property) {
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
            if (property instanceof StringSchema) {
                StringSchema sp = (StringSchema) property;
                sp.addEnumItem(n);
            }
        }
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

    private void handleUnwrapped(List<Schema> props, Schema innerModel, String prefix, String suffix) {
        if (StringUtils.isBlank(suffix) && StringUtils.isBlank(prefix)) {
            props.addAll(innerModel.getProperties().values());
        } else {
            if (prefix == null) {
                prefix = "";
            }
            if (suffix == null) {
                suffix = "";
            }
            // TODO #2312 - needs clone implemented in #2227
            for (Schema prop : (Collection<Schema>) innerModel.getProperties().values()) {
                //props.add(prop.rename(prefix + prop.getName() + suffix));
            }
        }
    }

    private enum GeneratorWrapper {
        PROPERTY(ObjectIdGenerators.PropertyGenerator.class) {
            @Override
            protected Schema processAsProperty(String propertyName, JavaType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                /*
                 * When generator = ObjectIdGenerators.PropertyGenerator.class and
                 * @JsonIdentityReference(alwaysAsId = false) then property is serialized
                 * in the same way it is done without @JsonIdentityInfo annotation.
                 */
                return null;
            }

            @Override
            protected Schema processAsId(String propertyName, JavaType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(type);
                for (BeanPropertyDefinition def : beanDesc.findProperties()) {
                    final String name = def.getName();
                    if (name != null && name.equals(propertyName)) {
                        final AnnotatedMember propMember = def.getPrimaryMember();
                        final JavaType propType = propMember.getType();
                        if (PrimitiveType.fromType(propType) != null) {
                            return PrimitiveType.createProperty(propType);
                        } else {
                            List<Annotation> list = new ArrayList<>();
                            for (Annotation a : propMember.annotations()) {
                                list.add(a);
                            }
                            return context.resolve(propType, list.toArray(new Annotation[list.size()]));
                        }
                    }
                }
                return null;
            }
        },
        INT(ObjectIdGenerators.IntSequenceGenerator.class) {
            @Override
            protected Schema processAsProperty(String propertyName, JavaType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                Schema id = new IntegerSchema();
                return process(id, propertyName, type, context);
            }

            @Override
            protected Schema processAsId(String propertyName, JavaType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                return new IntegerSchema();
            }
        },
        UUID(ObjectIdGenerators.UUIDGenerator.class) {
            @Override
            protected Schema processAsProperty(String propertyName, JavaType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                Schema id = new UUIDSchema();
                return process(id, propertyName, type, context);
            }

            @Override
            protected Schema processAsId(String propertyName, JavaType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                return new UUIDSchema();
            }
        },
        NONE(ObjectIdGenerators.None.class) {
            // When generator = ObjectIdGenerators.None.class property should be processed as normal property.
            @Override
            protected Schema processAsProperty(String propertyName, JavaType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }

            @Override
            protected Schema processAsId(String propertyName, JavaType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }
        };

        private final Class<? extends ObjectIdGenerator> generator;

        GeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
            this.generator = generator;
        }

        protected abstract Schema processAsProperty(String propertyName, JavaType type,
                                                    ModelConverterContext context, ObjectMapper mapper);

        protected abstract Schema processAsId(String propertyName, JavaType type,
                                              ModelConverterContext context, ObjectMapper mapper);

        public static Schema processJsonIdentity(JavaType type, ModelConverterContext context,
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

        private static Schema process(Schema id, String propertyName, JavaType type,
                                      ModelConverterContext context) {
            Schema model = context.resolve(type);
            Schema mi = model;
            mi.getProperties().put(propertyName, id);
            return new Schema().$ref(StringUtils.isNotEmpty(mi.get$ref())
                    ? mi.get$ref() : mi.getName());
        }
    }

    protected void applyBeanValidatorAnnotations(Schema property, Annotation[] annotations, Schema parent) {
        Map<String, Annotation> annos = new HashMap<String, Annotation>();
        if (annotations != null) {
            for (Annotation anno : annotations) {
                annos.put(anno.annotationType().getName(), anno);
            }
        }
        if (parent != null && annos.containsKey("javax.validation.constraints.NotNull")) {
            addRequiredItem(parent, property.getName());
        }
        if (annos.containsKey("javax.validation.constraints.Min")) {
            if ("integer".equals(property.getType()) || "number".equals(property.getType())) {
                Min min = (Min) annos.get("javax.validation.constraints.Min");
                property.setMinimum(new BigDecimal(min.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Max")) {
            if ("integer".equals(property.getType()) || "number".equals(property.getType())) {
                Max max = (Max) annos.get("javax.validation.constraints.Max");
                property.setMaximum(new BigDecimal(max.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Size")) {
            Size size = (Size) annos.get("javax.validation.constraints.Size");
            if ("integer".equals(property.getType()) || "number".equals(property.getType())) {
                property.setMinimum(new BigDecimal(size.min()));
                property.setMaximum(new BigDecimal(size.max()));
            } else if (property instanceof StringSchema) {
                StringSchema sp = (StringSchema) property;
                sp.minLength(new Integer(size.min()));
                sp.maxLength(new Integer(size.max()));
            } else if (property instanceof ArraySchema) {
                ArraySchema sp = (ArraySchema) property;
                sp.setMinItems(size.min());
                sp.setMaxItems(size.max());
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMin")) {
            DecimalMin min = (DecimalMin) annos.get("javax.validation.constraints.DecimalMin");
            if (property instanceof NumberSchema) {
                NumberSchema ap = (NumberSchema) property;
                ap.setMinimum(new BigDecimal(min.value()));
                ap.setExclusiveMinimum(!min.inclusive());
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMax")) {
            DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
            if (property instanceof NumberSchema) {
                NumberSchema ap = (NumberSchema) property;
                ap.setMaximum(new BigDecimal(max.value()));
                ap.setExclusiveMaximum(!max.inclusive());
            }
        }
        if (annos.containsKey("javax.validation.constraints.Pattern")) {
            Pattern pattern = (Pattern) annos.get("javax.validation.constraints.Pattern");
            if (property instanceof StringSchema) {
                property.setPattern(pattern.regexp());
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

    private boolean resolveSubtypes(Schema model, BeanDescription bean, ModelConverterContext context) {
        final List<NamedType> types = _intr.findSubtypes(bean.getClassInfo());
        if (types == null) {
            return false;
        }

        /**
         * As the introspector will find @JsonSubTypes for a child class that are present on its super classes, the
         * code segment below will also run the introspector on the parent class, and then remove any sub-types that are
         * found for the parent from the sub-types found for the child. The same logic all applies to implemented
         * interfaces, and is accounted for below.
         */
        removeSuperClassAndInterfaceSubTypes(types, bean);

        int count = 0;
        final Class<?> beanClass = bean.getClassInfo().getAnnotated();
        for (NamedType subtype : types) {
            final Class<?> subtypeType = subtype.getType();
            if (!beanClass.isAssignableFrom(subtypeType)) {
                continue;
            }

            final Schema subtypeModel = context.resolve(subtypeType);

            if (subtypeModel.getName().equals(model.getName())) {
                subtypeModel.setName(_typeNameResolver.nameForType(_mapper.constructType(subtypeType),
                        TypeNameResolver.Options.SKIP_API_MODEL));
            }

            // here schema could be not composed, but we want it to be composed, doing same work as done
            // in resolve method??

            ComposedSchema composedSchema = null;
            if (!(subtypeModel instanceof ComposedSchema)) {
                // create composed schema
                // TODO #2312 - smarter way needs clone implemented in #2227
                composedSchema = (ComposedSchema) new ComposedSchema()
                        .title(subtypeModel.getTitle())
                        .name(subtypeModel.getName())
                        .deprecated(subtypeModel.getDeprecated())
                        .additionalProperties(subtypeModel.getAdditionalProperties())
                        .description(subtypeModel.getDescription())
                        .discriminator(subtypeModel.getDiscriminator())
                        .example(subtypeModel.getExample())
                        .exclusiveMaximum(subtypeModel.getExclusiveMaximum())
                        .exclusiveMinimum(subtypeModel.getExclusiveMinimum())
                        .externalDocs(subtypeModel.getExternalDocs())
                        .format(subtypeModel.getFormat())
                        .maximum(subtypeModel.getMaximum())
                        .maxItems(subtypeModel.getMaxItems())
                        .maxLength(subtypeModel.getMaxLength())
                        .maxProperties(subtypeModel.getMaxProperties())
                        .minimum(subtypeModel.getMinimum())
                        .minItems(subtypeModel.getMinItems())
                        .minLength(subtypeModel.getMinLength())
                        .minProperties(subtypeModel.getMinProperties())
                        .multipleOf(subtypeModel.getMultipleOf())
                        .not(subtypeModel.getNot())
                        .nullable(subtypeModel.getNullable())
                        .pattern(subtypeModel.getPattern())
                        .properties(subtypeModel.getProperties())
                        .readOnly(subtypeModel.getReadOnly())
                        .required(subtypeModel.getRequired())
                        .type(subtypeModel.getType())
                        .uniqueItems(subtypeModel.getUniqueItems())
                        .writeOnly(subtypeModel.getWriteOnly())
                        .xml(subtypeModel.getXml())
                        .extensions(subtypeModel.getExtensions());

            } else {
                composedSchema = (ComposedSchema) subtypeModel;
            }
            Schema refSchema = new Schema().$ref(model.getName());
            // allOf could have already being added during type resolving when @Schema(allOf..) is declared
            if (composedSchema.getAllOf() == null || !composedSchema.getAllOf().contains(refSchema)) {
                composedSchema.addAllOfItem(refSchema);
            }
            removeParentProperties(composedSchema, model);

            // replace previous schema..
            Class<?> currentType = subtype.getType();
            context.defineModel(composedSchema.getName(), composedSchema, currentType, null);

        }
        return count != 0;
    }

    private void removeSuperClassAndInterfaceSubTypes(List<NamedType> types, BeanDescription bean) {
        Class<?> beanClass = bean.getType().getRawClass();
        Class<?> superClass = beanClass.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            removeSuperSubTypes(types, superClass);
        }
        if (!types.isEmpty()) {
            Class<?>[] superInterfaces = beanClass.getInterfaces();
            for (Class<?> superInterface : superInterfaces) {
                removeSuperSubTypes(types, superInterface);
                if (types.isEmpty()) {
                    break;
                }
            }
        }
    }

    private void removeSuperSubTypes(List<NamedType> resultTypes, Class<?> superClass) {
        JavaType superType = _mapper.constructType(superClass);
        BeanDescription superBean = _mapper.getSerializationConfig().introspect(superType);
        final List<NamedType> superTypes = _intr.findSubtypes(superBean.getClassInfo());
        if (superTypes != null) {
            resultTypes.removeAll(superTypes);
        }
    }

    private void removeParentProperties(Schema child, Schema parent) {
        final Map<String, Schema> baseProps = parent.getProperties();
        final Map<String, Schema> subtypeProps = child.getProperties();
        if (baseProps != null && subtypeProps != null) {
            for (Map.Entry<String, Schema> entry : baseProps.entrySet()) {
                if (entry.getValue().equals(subtypeProps.get(entry.getKey()))) {
                    subtypeProps.remove(entry.getKey());
                }
            }
        }
        if (subtypeProps == null || subtypeProps.isEmpty()) {
            child.setProperties(null);
        }
    }

    protected List<Class<?>> getComposedSchemaReferencedClasses(Class<?> clazz) {

        final io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = AnnotationsUtils.getSchemaDeclaredAnnotation(clazz);
        if (schemaAnnotation != null) {
            Class<?>[] allOf = schemaAnnotation.allOf();
            Class<?>[] anyOf = schemaAnnotation.anyOf();
            Class<?>[] oneOf = schemaAnnotation.oneOf();

            // try to read all of them anyway and resolve?
            List<Class<?>> parentClasses = Stream.of(allOf, anyOf, oneOf)
                    .flatMap(Stream::of)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());

            if (!parentClasses.isEmpty()) {
                return parentClasses;
            }
        }
        return null;
    }

    protected String resolveDescription(Annotated ann) {
        // while name suggests it's only for properties, should work for any Annotated thing.
        // also; with Swagger introspector's help, should get it from @Schema
        return _intr.findPropertyDescription(ann);
    }

    protected String resolveTitle(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && StringUtils.isNotBlank(schema.title())) {
            return schema.title();
        }
        return null;
    }

    protected String resolveFormat(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && StringUtils.isNotBlank(schema.format())) {
            return schema.format();
        }
        return null;
    }

    protected String resolveDefaultValue(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null) {
            if (!schema.defaultValue().isEmpty()) {
                return schema.defaultValue();
            }
        }
        XmlElement elem = a.getAnnotation(XmlElement.class);
        if (elem != null) {
            if (!elem.defaultValue().isEmpty() && !"\u0000".equals(elem.defaultValue())) {
                return elem.defaultValue();
            }
        }
        return null;
    }

    protected Object resolveExample(Annotated a) {

        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null) {
            if (!schema.example().isEmpty()) {
                try {
                    return Json.mapper().readTree(schema.example());
                } catch (IOException e) {
                    return schema.example();
                }
            }
        }

        return null;
    }

    protected Boolean resolveReadOnly(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.readOnly()) {
            return schema.readOnly();
        }
        return null;
    }

    protected Boolean resolveNullable(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.nullable()) {
            return schema.nullable();
        }
        return null;
    }

    protected BigDecimal resolveMultipleOf(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.multipleOf() != 0) {
            return new BigDecimal(schema.multipleOf());
        }
        return null;
    }

    protected Integer resolveMaxLength(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.maxLength() != Integer.MAX_VALUE && schema.maxLength() > 0) {
            return schema.maxLength();
        }
        return null;
    }

    protected Integer resolveMinLength(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.minLength() > 0) {
            return schema.minLength();
        }
        return null;
    }

    protected BigDecimal resolveMinimum(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && NumberUtils.isNumber(schema.minimum())) {
            String filteredMinimum = schema.minimum().replaceAll(Constants.COMMA, StringUtils.EMPTY);
            return new BigDecimal(filteredMinimum);
        }
        return null;
    }

    protected BigDecimal resolveMaximum(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && NumberUtils.isNumber(schema.maximum())) {
            String filteredMaximum = schema.maximum().replaceAll(Constants.COMMA, StringUtils.EMPTY);
            return new BigDecimal(filteredMaximum);
        }
        return null;
    }

    protected Boolean resolveExclusiveMinimum(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.exclusiveMinimum()) {
            return schema.exclusiveMinimum();
        }
        return null;
    }

    protected Boolean resolveExclusiveMaximum(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.exclusiveMaximum()) {
            return schema.exclusiveMaximum();
        }
        return null;
    }

    protected String resolvePattern(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && StringUtils.isNotBlank(schema.pattern())) {
            return schema.pattern();
        }
        return null;
    }

    protected Integer resolveMinProperties(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.minProperties() > 0) {
            return schema.minProperties();
        }
        return null;
    }

    protected Integer resolveMaxProperties(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.maxProperties() > 0) {
            return schema.maxProperties();
        }
        return null;
    }

    protected List<String> resolveRequiredProperties(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null &&
                schema.requiredProperties() != null &&
                schema.requiredProperties().length > 0 &&
                StringUtils.isNotBlank(schema.requiredProperties()[0])) {

            return Arrays.asList(schema.requiredProperties());
        }
        return null;
    }

    protected Boolean resolveWriteOnly(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.writeOnly()) {
            return schema.writeOnly();
        }
        return null;
    }

    protected ExternalDocumentation resolveExternalDocumentation(Annotated a) {

        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        io.swagger.v3.oas.annotations.ExternalDocumentation externalDocumentation = a.getAnnotation(io.swagger.v3.oas.annotations.ExternalDocumentation.class);

        ExternalDocumentation external = resolveExternalDocumentation(externalDocumentation);
        if (external == null) {
            if (schema != null) {
                external = resolveExternalDocumentation(schema.externalDocs());
            }
        }
        return external;
    }

    protected ExternalDocumentation resolveExternalDocumentation(io.swagger.v3.oas.annotations.ExternalDocumentation externalDocumentation) {

        if (externalDocumentation == null) {
            return null;
        }
        boolean isEmpty = true;
        ExternalDocumentation external = new ExternalDocumentation();
        if (StringUtils.isNotBlank(externalDocumentation.description())) {
            isEmpty = false;
            external.setDescription(externalDocumentation.description());
        }
        if (StringUtils.isNotBlank(externalDocumentation.url())) {
            isEmpty = false;
            external.setUrl(externalDocumentation.url());
        }
        if (isEmpty) {
            return null;
        }
        return external;
    }

    protected Boolean resolveDeprecated(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null && schema.deprecated()) {
            return schema.deprecated();
        }
        return null;
    }

    protected List<String> resolveAllowableValues(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null &&
                schema.allowableValues() != null &&
                schema.allowableValues().length > 0) {
            return Arrays.asList(schema.allowableValues());
        }
        return null;
    }

    protected Map<String, Object> resolveExtensions(Annotated a) {
        io.swagger.v3.oas.annotations.media.Schema schema = getSchemaAnnotation(a);
        if (schema != null &&
                schema.extensions() != null &&
                schema.extensions().length > 0) {
            return AnnotationsUtils.getExtensions(schema.extensions());
        }
        return null;
    }

    protected Discriminator resolveDiscriminator(JavaType type, ModelConverterContext context) {

        io.swagger.v3.oas.annotations.media.Schema declaredSchemaAnnotation = AnnotationsUtils.getSchemaDeclaredAnnotation(type.getRawClass());

        String disc = (declaredSchemaAnnotation == null) ? "" : declaredSchemaAnnotation.discriminatorProperty();

        if (disc.isEmpty()) {
            // longer method would involve AnnotationIntrospector.findTypeResolver(...) but:
            JsonTypeInfo typeInfo = type.getRawClass().getDeclaredAnnotation(JsonTypeInfo.class);
            if (typeInfo != null) {
                disc = typeInfo.property();
            }
        }
        if (!disc.isEmpty()) {
            Discriminator discriminator = new Discriminator()
                    .propertyName(disc);
            if (declaredSchemaAnnotation != null) {
                DiscriminatorMapping mappings[] = declaredSchemaAnnotation.discriminatorMapping();
                if (mappings != null && mappings.length > 0) {
                    for (DiscriminatorMapping mapping : mappings) {
                        if (!mapping.value().isEmpty() && !mapping.schema().equals(Void.class)) {
                            discriminator.mapping(mapping.value(), constructRef(context.resolve(mapping.schema()).getName()));
                        }
                    }
                }
            }

            return discriminator;
        }
        return null;
    }

    protected XML resolveXml(Annotated a) {
        // if XmlRootElement annotation, construct an Xml object and attach it to the model
        XmlRootElement rootAnnotation = a.getAnnotation(XmlRootElement.class);
        if (rootAnnotation != null && !"".equals(rootAnnotation.name()) && !"##default".equals(rootAnnotation.name())) {
            XML xml = new XML().name(rootAnnotation.name());
            if (rootAnnotation.namespace() != null && !"".equals(rootAnnotation.namespace()) && !"##default".equals(rootAnnotation.namespace())) {
                xml.namespace(rootAnnotation.namespace());
            }
            return xml;
        }
        return null;
    }

    protected void resolveSchemaMembers(Schema schema, Annotated a) {
        String description = resolveDescription(a);
        if (StringUtils.isNotBlank(description)) {
            schema.description(description);
        }
        String title = resolveTitle(a);
        if (StringUtils.isNotBlank(title)) {
            schema.title(title);
        }
        String format = resolveFormat(a);
        if (StringUtils.isNotBlank(format) && StringUtils.isBlank(schema.getFormat())) {
            schema.format(format);
        }
        String defaultValue = resolveDefaultValue(a);
        if (StringUtils.isNotBlank(defaultValue)) {
            schema.setDefault(defaultValue);
        }
        Object example = resolveExample(a);
        if (example != null) {
            schema.example(example);
        }
        Boolean readOnly = resolveReadOnly(a);
        if (readOnly != null) {
            schema.readOnly(readOnly);
        }
        Boolean nullable = resolveNullable(a);
        if (nullable != null) {
            schema.nullable(nullable);
        }
        BigDecimal multipleOf = resolveMultipleOf(a);
        if (multipleOf != null) {
            schema.multipleOf(multipleOf);
        }
        Integer maxLength = resolveMaxLength(a);
        if (maxLength != null) {
            schema.maxLength(maxLength);
        }
        Integer minLength = resolveMinLength(a);
        if (minLength != null) {
            schema.minLength(minLength);
        }
        BigDecimal minimum = resolveMinimum(a);
        if (minimum != null) {
            schema.minimum(minimum);
        }
        BigDecimal maximum = resolveMaximum(a);
        if (maximum != null) {
            schema.maximum(maximum);
        }
        Boolean exclusiveMinimum = resolveExclusiveMinimum(a);
        if (exclusiveMinimum != null) {
            schema.exclusiveMinimum(exclusiveMinimum);
        }
        Boolean exclusiveMaximum = resolveExclusiveMaximum(a);
        if (exclusiveMaximum != null) {
            schema.exclusiveMaximum(exclusiveMaximum);
        }
        String pattern = resolvePattern(a);
        if (StringUtils.isNotBlank(pattern)) {
            schema.pattern(pattern);
        }
        Integer minProperties = resolveMinProperties(a);
        if (minProperties != null) {
            schema.minProperties(minProperties);
        }
        Integer maxProperties = resolveMaxProperties(a);
        if (maxProperties != null) {
            schema.maxProperties(maxProperties);
        }
        List<String> requiredProperties = resolveRequiredProperties(a);
        if (requiredProperties != null) {
            for (String prop : requiredProperties) {
                addRequiredItem(schema, prop);
            }
        }
        Boolean writeOnly = resolveWriteOnly(a);
        if (writeOnly != null) {
            schema.writeOnly(writeOnly);
        }
        ExternalDocumentation externalDocs = resolveExternalDocumentation(a);
        if (externalDocs != null) {
            schema.externalDocs(externalDocs);
        }
        Boolean deprecated = resolveDeprecated(a);
        if (deprecated != null) {
            schema.deprecated(deprecated);
        }
        List<String> allowableValues = resolveAllowableValues(a);
        if (allowableValues != null) {
            for (String prop : allowableValues) {
                schema.addEnumItemObject(prop);
            }
        }

        Map<String, Object> extensions = resolveExtensions(a);
        if (extensions != null) {
            for (String ext : extensions.keySet()) {
                schema.addExtension(ext, extensions.get(ext));
            }
        }
    }

    protected io.swagger.v3.oas.annotations.media.Schema getSchemaAnnotation(Annotated a) {
        boolean useDeclared = false;
        List<NamedType> subTypes =_intr.findSubtypes(a);
        if (subTypes == null) {
            return AnnotationsUtils.getSchemaAnnotation(a);
        }
        // if subtypes are defined, including the current class, we assume they are defined in super class.
        for (NamedType subtype : subTypes) {
            final Class<?> subtypeType = subtype.getType();
            if (a.getRawType().isAssignableFrom(subtypeType)) {
                useDeclared = true;
                break;
            }
        }
        if (useDeclared) {
            return AnnotationsUtils.getSchemaDeclaredAnnotation(a);
        } else {
            return AnnotationsUtils.getSchemaAnnotation(a);
        }

    }

    private void addRequiredItem(Schema model, String propName) {
        if (model == null || propName == null || StringUtils.isBlank(propName)) {
            return;
        }
        if (model.getRequired() == null || model.getRequired().isEmpty()) {
            model.addRequiredItem(propName);
        }
        if (model.getRequired().stream().noneMatch(s -> propName.equals(s))) {
            model.addRequiredItem(propName);
        }
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
                LOGGER.trace("Can't check class {}, {}", type, rt.getRawClass().getName());
                if (rt.getRawClass().equals(Class.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    private io.swagger.v3.oas.annotations.media.Schema getSchemaAnnotation(Annotation... annotations) {
        if (annotations == null) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof io.swagger.v3.oas.annotations.media.Schema) {
                return (io.swagger.v3.oas.annotations.media.Schema) annotation;
            }
        }
        return null;
    }

    private List<String> getIgnoredProperties(BeanDescription beanDescription) {
        AnnotationIntrospector introspector = _mapper.getSerializationConfig().getAnnotationIntrospector();
        String[] ignored = introspector.findPropertiesToIgnore(beanDescription.getClassInfo(), true);
        return ignored == null ? Collections.emptyList() : Arrays.asList(ignored);
    }

}
