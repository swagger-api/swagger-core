package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Iterables;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.oas.annotations.media.DiscriminatorMapping;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.ComposedSchema;
import io.swagger.oas.models.media.Discriminator;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.MapSchema;
import io.swagger.oas.models.media.NumberSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.oas.models.media.UUIDSchema;
import io.swagger.util.PrimitiveType;
import io.swagger.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelResolver extends AbstractModelConverter implements ModelConverter {
    Logger LOGGER = LoggerFactory.getLogger(ModelResolver.class);

    public ModelResolver(ObjectMapper mapper) {
        super(mapper);
    }

    public ObjectMapper objectMapper() {
        return _mapper;
    }

    private String constructRef(String simpleRef) {
        return "#/components/schemas/" + simpleRef;
    }

    private String extractSimpleName(String ref) {
        int idx = ref.lastIndexOf("/");
        if(idx > 0) {
            return ref.substring(idx);
        }
        return ref;
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
                LOGGER.debug("Can't check class {}, {}", type, rt.getRawClass().getName());
                if (rt.getRawClass().equals(Class.class)) {
                    return true;
                }
            }
        }
        return false;
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
                if(annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        if(annotation instanceof XmlElement) {
                            XmlElement xmlElement =   (XmlElement)annotation;
                            if(xmlElement != null && xmlElement.name() != null && !"".equals(xmlElement.name()) && !"##default".equals(xmlElement.name())) {
//                                Xml xml = items.getXml() != null ? items.getXml() : new Xml();
//                                xml.setName(xmlElement.name());
//                                items.setXml(xml);
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
            property = PrimitiveType.createProperty(propType);
        }

        if (property == null) {
            if (propType.isEnumType()) {
                property = new StringSchema();
                _addEnumProps(propType.getRawClass(), property);
            } else if (_isOptionalType(propType)) {
                property = context.resolve(propType.containedType(0), null);
            } else {
                // complex type
                Schema innerModel = context.resolve(propType);
                if (innerModel instanceof Schema) {
                    Schema mi = (Schema) innerModel;

                    if("object".equals(mi.getType())) {
                        // create a reference for the property
                        final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(propType);
                        String name = _typeName(propType, beanDesc);
                        property = new Schema().$ref(constructRef(name));
                    }
                    else if(mi.get$ref() != null) {
                        property = new Schema().$ref(StringUtils.isNotEmpty(mi.get$ref()) ? mi.get$ref() : mi.getTitle());
                    }
                    else {
                        property = innerModel;
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

    @Override
    public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (this.shouldIgnoreClass(type)) {
            return null;
        }

        return resolve(_mapper.constructType(type), context, next);
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

    public Schema resolve(JavaType type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (type.isEnumType()) {
            // We don't build models for primitive types
            return null;
        }
        else if(PrimitiveType.fromType(type) != null) {
            return PrimitiveType.fromType(type).createProperty();
        }

        final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(type);
        // Couple of possibilities for defining
        String name = _typeName(type, beanDesc);
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
            return resolvedModel;
        }
        // TODO modified not to consider super class (as @JsonTypeInfo is not marked inherited) to handle composed model stuff; ok?
        //List<Class<?>> composedSchemaReferencedClasses = getComposedSchemaReferencedClasses(beanDesc);
        List<Class<?>> composedSchemaReferencedClasses = getComposedSchemaReferencedClasses(type.getRawClass());
        boolean isComposedSchema = composedSchemaReferencedClasses != null;
        final Schema model;
        if (isComposedSchema) {
            model = new ComposedSchema()
                    .type("object")
                    .name(name)
                    .description(_description(beanDesc.getClassInfo()));
        } else {
            model = new Schema()
                    .type("object")
                    .name(name)
                    .description(_description(beanDesc.getClassInfo()));
        }

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
            LOGGER.debug("{}", rootAnnotation);
            /*
            Xml xml = new Xml().name(rootAnnotation.name());
            if (rootAnnotation.namespace() != null && !"".equals(rootAnnotation.namespace()) && !"##default".equals(rootAnnotation.namespace())) {
                xml.namespace(rootAnnotation.namespace());
            }
            model.xml(xml);
            */
        }
        final XmlAccessorType xmlAccessorTypeAnnotation = beanDesc.getClassAnnotations().get(XmlAccessorType.class);

        // see if @JsonIgnoreProperties exist
        Set<String> propertiesToIgnore = new HashSet<String>();
        JsonIgnoreProperties ignoreProperties = beanDesc.getClassAnnotations().get(JsonIgnoreProperties.class);
        if (ignoreProperties != null) {
            propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
        }

        List<Schema> props = new ArrayList<Schema>();
        Map<String, Schema> modelProps = new LinkedHashMap<String, Schema>();

        for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
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
            try{
                if (propDef.getSetter() == null) {
                    hasSetter = false;
                } else {
                    hasSetter = true;
                }
            } catch (IllegalArgumentException e){
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
            Boolean allowEmptyValue = null;
            String minimum = null, maximum = null;
            boolean exclusiveMinimum = false, exclusiveMaximum = false;

            if (member != null && !ignore(member, xmlAccessorTypeAnnotation, propName, propertiesToIgnore)) {
                List<Annotation> annotationList = new ArrayList<Annotation>();
                for (Annotation a : member.annotations()) {
                    annotationList.add(a);
                }

                annotations = annotationList.toArray(new Annotation[annotationList.size()]);

                io.swagger.oas.annotations.media.Schema mp = null;

                io.swagger.oas.annotations.media.ArraySchema as = null;
                as = member.getAnnotation(io.swagger.oas.annotations.media.ArraySchema.class);
                if (as != null) {
                    mp = as.schema();
                } else {
                    mp = member.getAnnotation(io.swagger.oas.annotations.media.Schema.class);
                }

                if(mp != null) {
                    if (mp.readOnly()) {
                        isReadOnly = mp.readOnly();
                    }
                    if (mp.nullable()) {
                        allowEmptyValue = mp.nullable();
                    } else {
                        allowEmptyValue = null;
                    }
                    if (StringUtils.isNotBlank(mp.minimum()) && !String.valueOf(Integer.MAX_VALUE).equals(mp.minimum())) {
                        minimum = mp.minimum();
                    }
                    if (StringUtils.isNotBlank(mp.maximum()) && !String.valueOf(Integer.MIN_VALUE).equals(mp.maximum())) {
                        maximum = mp.maximum();
                    }
                    if(mp.exclusiveMinimum()) {
                        exclusiveMinimum = true;
                    }
                    if(mp.exclusiveMaximum()) {
                        exclusiveMaximum = true;
                    }
                }

                JavaType propType = member.getType(beanDesc.bindingsForBeanType());

                // allow override of name from annotation
                if (mp != null && !mp.name().isEmpty()) {
                    propName = mp.name();
                }

                if (mp != null && !Void.class.equals(mp.implementation())) {
                    Class<?> cls = mp.implementation();

                    LOGGER.debug("overriding datatype from {} to {}", propType, cls.getName());

                    if (as != null) {
                        ArraySchema propertySchema = new ArraySchema();
                        Schema innerSchema = null;

                        Schema primitiveProperty = PrimitiveType.createProperty(cls);
                        if (primitiveProperty != null) {
                            innerSchema = primitiveProperty;
                        } else {
                            innerSchema = context.resolve(cls, annotations);
                        }
                        propertySchema.setItems(innerSchema);
                        property = propertySchema;
                    }
                    else {
                        property = context.resolve(cls, annotations);
                    }
                    /*else if (or.toLowerCase().startsWith("map[")) {
                        // TODO
//                        int pos = or.indexOf(",");
//                        if (pos > 0) {
//                            String innerType = or.substring(pos + 1, or.length() - 1);
//                            MapProperty p = new MapProperty();
//                            Property primitiveProperty = PrimitiveType.createProperty(innerType);
//                            if (primitiveProperty != null) {
//                                p.setAdditionalProperties(primitiveProperty);
//                            } else {
//                                innerJavaType = getInnerType(innerType);
//                                p.setAdditionalProperties(context.resolveProperty(innerJavaType, annotations));
//                            }
//                            property = p;
//                        }
                    } else {
                        Schema primitiveProperty = PrimitiveType.createProperty(or);
                        if (primitiveProperty != null) {
                            property = primitiveProperty;
                        } else {
                            innerJavaType = getInnerType(or);
                            property = context.resolve(innerJavaType, annotations);
                        }
                    }
                    if (innerJavaType != null) {
                        context.resolve(innerJavaType);
                    }*/
                }

                // no property from override, construct from propType
                if (property == null) {
                    if (mp != null && StringUtils.isNotEmpty(mp.ref())) {
                        property = new Schema().$ref(mp.ref());
                    } else if (member.getAnnotation(JsonIdentityInfo.class) != null) {
                        // TODO
//                        property = GeneratorWrapper.processJsonIdentity(propType, context, _mapper,
//                                member.getAnnotation(JsonIdentityInfo.class),
//                                member.getAnnotation(JsonIdentityReference.class));
                    }
                    if (property == null) {
                        JsonUnwrapped uw = member.getAnnotation(JsonUnwrapped.class);
                        if (uw != null && uw.enabled()) {
                            handleUnwrapped(props, context.resolve(propType), uw.prefix(), uw.suffix());
                        } else {
                            property = context.resolve(propType, annotations);
                        }
                    }
                }

                if (property != null) {
                    if (property.get$ref() == null) {
                        Boolean required = md.getRequired();
                        if (required != null && !Boolean.FALSE.equals(required)) {
                            model.addRequiredItem(propName);
                        }

                        String description = _intr.findPropertyDescription(member);
                        if (description != null && !"".equals(description)) {
                            property.setDescription(description);
                        }

                        Integer index = _intr.findPropertyIndex(member);
                        if (index != null) {
//                        property.setPosition(index);
                        }
                        String _defaultValue = _findDefaultValue(member);
                        property.setDefault(_defaultValue);
                        if(minimum != null) {
                            property.minimum(new BigDecimal(minimum));
                        }
                        if(maximum != null) {
                            property.maximum(new BigDecimal(maximum));
                        }
                        if(exclusiveMaximum) {
                            property.exclusiveMaximum(true);
                        }
                        if(exclusiveMinimum) {
                            property.exclusiveMinimum(true);
                        }

                        property.setExample(_findExampleValue(member));
                        property.setReadOnly(_findReadOnly(member));
                        if (allowEmptyValue != null) {
                            property.setNullable(allowEmptyValue);
                        }

                        if (property.getReadOnly() == null) {
                            if (isReadOnly) {
                                property.setReadOnly(isReadOnly);
                            }
                        }
                        if (mp != null) {
                            if(mp._enum().length > 0) {
                                for(String _enum : mp._enum()) {
                                    if(StringUtils.isNotBlank(_enum)) {
                                        property.addEnumItemObject(_enum);
                                    }
                                }
                            }
                        }
                        JAXBAnnotationsHelper.apply(member, property);
                        applyBeanValidatorAnnotations(property, annotations);
                    }
                }

                if(property != null) {
                    props.add(property);
                    modelProps.put(propName, property);
                }
            }
        }

//        Collections.sort(props, getPropertyComparator());

        if(modelProps.size() > 0) {
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
        // TODO modified not to consider super class (as @Schema is not marked inherited) to handle composed model stuff; ok?
        //final io.swagger.oas.annotations.media.Schema schemaAnnotation = beanDesc.getClassAnnotations().get(io.swagger.oas.annotations.media.Schema.class);
        final io.swagger.oas.annotations.media.Schema schemaAnnotation = type.getRawClass().getAnnotation(io.swagger.oas.annotations.media.Schema.class);

        String disc = (schemaAnnotation == null) ? "" : schemaAnnotation.discriminatorProperty();


        if (disc.isEmpty()) {
            // longer method would involve AnnotationIntrospector.findTypeResolver(...) but:
            // TODO modified not to consider super class (as @JsonTypeInfo is not marked inherited) to handle composed model stuff; ok?
            //JsonTypeInfo typeInfo = beanDesc.getClassAnnotations().get(JsonTypeInfo.class);
            JsonTypeInfo typeInfo = type.getRawClass().getAnnotation(JsonTypeInfo.class);
            if (typeInfo != null) {
                disc = typeInfo.property();
            }
        }
        if (!disc.isEmpty()) {
            Discriminator discriminator = new Discriminator()
                    .propertyName(disc);
            DiscriminatorMapping mappings[] = schemaAnnotation.discriminatorMapping();
            if (mappings != null && mappings.length > 0) {
                for (DiscriminatorMapping mapping: mappings) {
                    if (!mapping.value().isEmpty() && !mapping.schema().equals(Void.class)) {
                        discriminator.mapping(mapping.value(), constructRef(context.resolve(mapping.schema()).getName()));
                    }
                }

            }

            model.setDiscriminator(discriminator);
        }



        if (schemaAnnotation != null) {
            Class<?> not = schemaAnnotation.not();
            if (!Void.class.equals(not)) {
                model.not((new Schema().$ref(context.resolve(not.getClass()).getName())));
            }
        }

        if (isComposedSchema) {

            ComposedSchema composedSchema = (ComposedSchema)model;


            Class<?>[] allOf = schemaAnnotation.allOf();
            Class<?>[] anyOf = schemaAnnotation.anyOf();
            Class<?>[] oneOf = schemaAnnotation.oneOf();


            List<Class<?>> allOfFiltered = Stream.of(allOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            allOfFiltered.forEach(c -> {
                Schema allOfRef = context.resolve(c);
                composedSchema.addAllOfItem(new Schema().$ref(allOfRef.getName()));
                removeParentProperties(composedSchema, allOfRef);
            });

            List<Class<?>> anyOfFiltered = Stream.of(anyOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            anyOfFiltered.forEach(c -> {
                Schema anyOfRef = context.resolve(c);
                // TODO do we want to expand this? or use ref? and remove interface schema??
                // or do we want to only "resolve" interfaces, and not classes in anyOf?
                // TODO do we want to implement the same for allOf and oneOf?
                //composedSchema.addAnyOfItem(new Schema().$ref(anyOfRef.getName()));
                composedSchema.addAnyOfItem(anyOfRef);
                // remove shared properties defined in the parent
                removeParentProperties(composedSchema, anyOfRef);
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
                removeParentProperties(composedSchema, oneOfRef);
            });


            /* TODO do we need logic below (from 2.0)? do we need the "child" to be referenced by the "parent" to be resolved?
            // why is/was that needed?
            if (parentClass != null && !parentClass.equals(Void.class) && !this.shouldIgnoreClass(parentClass)) {
                JavaType parentType = _mapper.constructType(parentClass);
                final BeanDescription parentBeanDesc = _mapper.getSerializationConfig().introspect(parentType);

//                 * Retrieve all the sub-types of the parent class and ensure that the current type is one of those types
                // TODO this is not anymore true? do we need to only resolve when mentioned in parent? why?
                boolean currentTypeIsParentSubType = false;
                List<NamedType> subTypes = _intr.findSubtypes(parentBeanDesc.getClassInfo());
                if (subTypes != null) {
                    for (NamedType subType : subTypes) {
                        if (subType.getType().equals(currentType)) {
                            currentTypeIsParentSubType = true;
                            break;
                        }
                    }
                }

//                 Retrieve the subTypes from the parent class @ApiModel annotation and ensure that the current type
//                 is one of those types.
                boolean currentTypeIsParentApiModelSubType = false;
                final ApiModel parentApiModel = parentBeanDesc.getClassAnnotations().get(ApiModel.class);
                if (parentApiModel != null) {
                    Class<?>[] apiModelSubTypes = parentApiModel.subTypes();
                    if (apiModelSubTypes != null) {
                        for (Class<?> subType : apiModelSubTypes) {
                            if (subType.equals(currentType)) {
                                currentTypeIsParentApiModelSubType = true;
                                break;
                            }
                        }
                    }
                }


//                 If the current type is a sub-type of the parent class and is listed in the subTypes property of the
//                 parent class @ApiModel annotation, then do the following:
//                 1. Resolve the model for the parent class. This will result in the parent model being created, and the
//                 current child model being updated to be a ComposedModel referencing the parent.
//                 2. Resolve and return the current child type again. This will return the new ComposedModel from the
//                 context, which was created in step 1 above. Admittedly, there is a small chance that this may result
//                 in a stack overflow, if the context does not correctly cache the model for the current type. However,
//                 as context caching is assumed elsewhere to avoid cyclical model creation, this was deemed to be
//                 sufficient.

                if (currentTypeIsParentSubType && currentTypeIsParentApiModelSubType) {
                    context.resolve(parentClass);
                    return context.resolve(currentType);
                }
            }
            END TODO */
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
            for (Schema prop : (Collection<Schema>)innerModel.getProperties().values()) {
                // TODO
//                props.add(prop.rename(prefix + prop.getName() + suffix));
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
                        final JavaType propType = propMember.getType(beanDesc.bindingsForBeanType());
                        if (PrimitiveType.fromType(propType) != null) {
                            return PrimitiveType.createProperty(propType);
                        } else {
                            return context.resolve(propType,
                                    Iterables.toArray(propMember.annotations(), Annotation.class));
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
//            id.setTitle(propertyName);
            Schema model = context.resolve(type);
                Schema mi = (Schema) model;
                mi.getProperties().put(propertyName, id);
                return new Schema().$ref(StringUtils.isNotEmpty(mi.get$ref())
                        ? mi.get$ref() : mi.getTitle());
        }
    }

    protected void applyBeanValidatorAnnotations(Schema property, Annotation[] annotations) {
        Map<String, Annotation> annos = new HashMap<String, Annotation>();
        if (annotations != null) {
            for (Annotation anno : annotations) {
                annos.put(anno.annotationType().getName(), anno);
            }
        }
        if (annos.containsKey("javax.validation.constraints.NotNull")) {
//            property.setRequired(true);
        }
        if (annos.containsKey("javax.validation.constraints.Min")) {
            if ("integer".equals(property.getType()) || "number". equals(property.getType())) {
                Min min = (Min) annos.get("javax.validation.constraints.Min");
//                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                property.setMinimum(new BigDecimal(min.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Max")) {
            if ("integer".equals(property.getType()) || "number". equals(property.getType())) {
                Max max = (Max) annos.get("javax.validation.constraints.Max");
                property.setMaximum(new BigDecimal(max.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Size")) {
            Size size = (Size) annos.get("javax.validation.constraints.Size");
            if ("integer".equals(property.getType()) || "number". equals(property.getType())) {
                property.setMinimum(new BigDecimal(size.min()));
                property.setMaximum(new BigDecimal(size.max()));
            } else if (property instanceof StringSchema) {
                StringSchema sp = (StringSchema) property;
                sp.minLength(new Integer(size.min()));
                sp.maxLength(new Integer(size.max()));
            } /*else if (property instanceof ArrayProperty) {
                ArrayProperty sp = (ArrayProperty) property;
                sp.setMinItems(size.min());
                sp.setMaxItems(size.max());
            }*/
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

            // TODO why do we need stuff below? if child defines the same it's his problem..
/*
            // remove shared properties defined in the parent
            final Map<String, Schema> baseProps = model.getProperties();
            final Map<String, Schema> subtypeProps = subtypeModel.getProperties();
            if (baseProps != null && subtypeProps != null) {
                for (Map.Entry<String, Schema> entry : baseProps.entrySet()) {
                    if (entry.getValue().equals(subtypeProps.get(entry.getKey()))) {
                        subtypeProps.remove(entry.getKey());
                    }
                }
            }
*/
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

    private void removeParentProperties (Schema child, Schema parent) {
        final Map<String, Schema> baseProps = parent.getProperties();
        final Map<String, Schema> subtypeProps = child.getProperties();
        if (baseProps != null && subtypeProps != null) {
            for (Map.Entry<String, Schema> entry : baseProps.entrySet()) {
                if (entry.getValue().equals(subtypeProps.get(entry.getKey()))) {
                    subtypeProps.remove(entry.getKey());
                }
            }
        }
        if (subtypeProps.isEmpty()) {
            child.setProperties(null);
        }
    }

    // TODO modified not to consider super class (as @JsonTypeInfo is not marked inherited) to handle composed model stuff; ok?
    //private List<Class<?>> getComposedSchemaReferencedClasses(BeanDescription beanDesc) {
    private List<Class<?>> getComposedSchemaReferencedClasses(Class<?> clazz) {
        //final io.swagger.oas.annotations.media.Schema schemaAnnotation = beanDesc.getClassAnnotations().get(io.swagger.oas.annotations.media.Schema.class);
        final io.swagger.oas.annotations.media.Schema schemaAnnotation = clazz.getAnnotation(io.swagger.oas.annotations.media.Schema.class);
        if (schemaAnnotation != null) {
            // TODO not??
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
}
