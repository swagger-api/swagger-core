package io.swagger.jackson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;

import io.swagger.models.refs.RefFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.Xml;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.models.properties.UUIDProperty;
import io.swagger.util.AllowableValues;
import io.swagger.util.AllowableValuesUtils;
import io.swagger.util.BaseReaderUtils;
import io.swagger.util.PrimitiveType;
import io.swagger.util.ReflectionUtils;

public class ModelResolver extends AbstractModelConverter implements ModelConverter {
    Logger LOGGER = LoggerFactory.getLogger(ModelResolver.class);

    public ModelResolver(ObjectMapper mapper) {
        super(mapper);
    }
    public ModelResolver(ObjectMapper mapper, TypeNameResolver typeNameResolver) {
        super(mapper, typeNameResolver);
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
                LOGGER.debug("Can't check class {}, {}", type, rt.getRawClass().getName());
                if (rt.getRawClass().equals(Class.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
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
        LOGGER.debug("resolveProperty {}", propType);

        Property property = null;
        if (propType.isContainerType()) {
            JavaType keyType = propType.getKeyType();
            JavaType valueType = propType.getContentType();
            if (keyType != null && valueType != null) {
                property = new MapProperty().additionalProperties(context.resolveProperty(valueType, new Annotation[]{}));
            } else if (valueType != null) {
                Property items = context.resolveProperty(valueType, new Annotation[]{});
                // If property is XmlElement annotated, then use the name provided by annotation | https://github.com/swagger-api/swagger-core/issues/2047
                if(annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        if(annotation instanceof XmlElement) {
                            XmlElement xmlElement =   (XmlElement)annotation;
                            if(xmlElement != null && xmlElement.name() != null && !"".equals(xmlElement.name()) && !"##default".equals(xmlElement.name())) {
                                Xml xml = items.getXml() != null ? items.getXml() : new Xml();
                                xml.setName(xmlElement.name());
                                items.setXml(xml);
                            }
                        }
                    }
                }
                ArrayProperty arrayProperty =
                        new ArrayProperty().items(items);
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
                _addEnumProps(propType.getRawClass(), (StringProperty) property);
            } else if (_isOptionalType(propType)) {
                property = context.resolveProperty(propType.containedType(0), null);
            } else {
                // complex type
                Model innerModel = context.resolve(propType);
                if (innerModel instanceof ComposedModel) {
                    innerModel = ((ComposedModel) innerModel).getChild();
                }
                if (innerModel instanceof ModelImpl) {
                    ModelImpl mi = (ModelImpl) innerModel;
                    if (StringUtils.isNotEmpty(mi.getReference())) {
                        property = new RefProperty(mi.getReference());
                    } else {
                        property = new RefProperty(mi.getName(), RefFormat.INTERNAL);
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
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (this.shouldIgnoreClass(type)) {
            return null;
        }

        return resolve(_mapper.constructType(type), context, next);
    }

    protected void _addEnumProps(Class<?> propClass, StringProperty property) {
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
                n = en.name();
            }
            property._enum(n);
        }
        
        if (!useIndex && !useToString) {
            property._enum(Arrays.asList(_intr.findEnumValues(propClass, enumClass.getEnumConstants(), property.getEnum().toArray(new String[0]))));
        }
    }

    public Model resolve(JavaType type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (type.isEnumType() || PrimitiveType.fromType(type) != null) {
            // We don't build models for primitive types
            return null;
        }

        BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(type);
        // Couple of possibilities for defining
        String name = _typeName(type, beanDesc);

        if ("Object".equals(name)) {
            return new ModelImpl();
        }

        name = decorateModelName(context, name);

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
        Model resolvedModel = context.resolve(type.getRawClass());
        if (resolvedModel != null) {
            if (!(resolvedModel instanceof ModelImpl || resolvedModel instanceof ComposedModel)
                    || (resolvedModel instanceof ModelImpl && ((ModelImpl) resolvedModel).getName().equals(name))) {
                return resolvedModel;
            } else if (resolvedModel instanceof ComposedModel) {
                Model childModel = ((ComposedModel) resolvedModel).getChild();
                if (childModel != null && (!(childModel instanceof ModelImpl)
                        || ((ModelImpl) childModel).getName().equals(name))) {
                    return resolvedModel;
                }
            }
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

        final ApiModel apiModel = beanDesc.getClassAnnotations().get(ApiModel.class);
        if (apiModel != null && StringUtils.isNotEmpty(apiModel.reference())) {
            model.setReference(apiModel.reference());
        }

        // if XmlRootElement annotation, construct an Xml object and attach it to the model
        XmlRootElement rootAnnotation = beanDesc.getClassAnnotations().get(XmlRootElement.class);
        if (rootAnnotation != null && !"".equals(rootAnnotation.name()) && !"##default".equals(rootAnnotation.name())) {
            LOGGER.debug("{}", rootAnnotation);
            Xml xml = new Xml().name(rootAnnotation.name());
            if (rootAnnotation.namespace() != null && !"".equals(rootAnnotation.namespace()) && !"##default".equals(rootAnnotation.namespace())) {
                xml.namespace(rootAnnotation.namespace());
            }
            else {
                // If namespace was not given in the annotation, look for it in package-info
                Package pkg = type.getRawClass().getPackage();
                if(pkg != null) {
                    XmlSchema xmlSchma = pkg.getAnnotation(XmlSchema.class);
                    if(xmlSchma != null) {
                        xml.namespace(xmlSchma.namespace());
                    }
                }
            }
            model.xml(xml);
        }
        final XmlAccessorType xmlAccessorTypeAnnotation = beanDesc.getClassAnnotations().get(XmlAccessorType.class);

        //If JsonSerialize(as=...) is specified then use that bean to figure out all the json-like bits
        JsonSerialize jasonSerialize = beanDesc.getClassAnnotations().get(JsonSerialize.class);
        if (jasonSerialize != null) {
            if (jasonSerialize.as() != null) {
                JavaType asType = _mapper.constructType(jasonSerialize.as());
                beanDesc = _mapper.getSerializationConfig().introspect(asType);
            }
        }

        // see if @JsonIgnoreProperties exist
        Set<String> propertiesToIgnore = new HashSet<String>();
        JsonIgnoreProperties ignoreProperties = beanDesc.getClassAnnotations().get(JsonIgnoreProperties.class);
        if (ignoreProperties != null) {
            propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
        }

        String disc = (apiModel == null) ? "" : apiModel.discriminator();
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
                JsonProperty jsonPropertyAnn = propDef.getPrimaryMember().getAnnotation(JsonProperty.class);
                if (jsonPropertyAnn == null || !jsonPropertyAnn.value().equals(propName)) {
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

            if (member != null && !ignore(member, xmlAccessorTypeAnnotation, propName, propertiesToIgnore)) {
                List<Annotation> annotationList = new ArrayList<Annotation>();
                for (Annotation a : member.annotations()) {
                    annotationList.add(a);
                }

                annotations = annotationList.toArray(new Annotation[annotationList.size()]);
                if(hiddenByJsonView(annotations, context)) {
                    continue;
                }

                ApiModelProperty mp = member.getAnnotation(ApiModelProperty.class);

                if (mp != null && mp.readOnly()) {
                    isReadOnly = mp.readOnly();
                }

                if (mp != null && mp.allowEmptyValue()) {
                    allowEmptyValue = mp.allowEmptyValue();
                }
                else {
                    allowEmptyValue = null;
                }

                JavaType propType = member.getType(beanDesc.bindingsForBeanType());

                // allow override of name from annotation
                if (mp != null && !mp.name().isEmpty()) {
                    propName = mp.name();
                }

                if (mp != null && !mp.dataType().isEmpty()) {
                    String or = mp.dataType();

                    JavaType innerJavaType = null;
                    LOGGER.debug("overriding datatype from {} to {}", propType, or);

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
                        JsonUnwrapped uw = member.getAnnotation(JsonUnwrapped.class);
                        if (uw != null && uw.enabled()) {
                            handleUnwrapped(props, context.resolve(propType), uw.prefix(), uw.suffix());
                        } else {
                            property = context.resolveProperty(propType, annotations);
                        }
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
                    if(allowEmptyValue != null) {
                        property.setAllowEmptyValue(allowEmptyValue);
                    }

                    if (property.getReadOnly() == null) {
                        if (isReadOnly) {
                            property.setReadOnly(isReadOnly);
                        }
                    }

                    // keep read-only handling code unaltered to maintain backward compatibility/behaviour,
                    // but also process new (since 1.5.19) ApiModelProperty.accessMode annotation field.
                    Boolean readOnlyFromAccessMode = _findReadOnlyFromAccessMode(member);
                    if (readOnlyFromAccessMode != null) {
                        property.setReadOnly(readOnlyFromAccessMode);
                    }

                    if (mp != null) {
                        final AllowableValues allowableValues = AllowableValuesUtils.create(mp.allowableValues());
                        if (allowableValues != null) {
                            final Map<PropertyBuilder.PropertyId, Object> args = allowableValues.asPropertyArguments();
                            PropertyBuilder.merge(property, args);
                        }
                    }

                    if (mp != null && mp.extensions() != null) {
                        property.getVendorExtensions().clear();
                        property.getVendorExtensions().putAll(BaseReaderUtils.parseExtensions(mp.extensions()));
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

        if (apiModel != null) {
            /**
             * Check if the @ApiModel annotation has a parent property containing a value that should not be ignored
             */
            Class<?> parentClass = apiModel.parent();
            if (parentClass != null && !parentClass.equals(Void.class) && !this.shouldIgnoreClass(parentClass)) {
                JavaType parentType = _mapper.constructType(parentClass);
                final BeanDescription parentBeanDesc = _mapper.getSerializationConfig().introspect(parentType);

                /**
                 * Retrieve all the sub-types of the parent class and ensure that the current type is one of those types
                 */
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

                /**
                 Retrieve the subTypes from the parent class @ApiModel annotation and ensure that the current type
                 is one of those types.
                 */
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

                /**
                 If the current type is a sub-type of the parent class and is listed in the subTypes property of the
                 parent class @ApiModel annotation, then do the following:
                 1. Resolve the model for the parent class. This will result in the parent model being created, and the
                 current child model being updated to be a ComposedModel referencing the parent.
                 2. Resolve and return the current child type again. This will return the new ComposedModel from the
                 context, which was created in step 1 above. Admittedly, there is a small chance that this may result
                 in a stack overflow, if the context does not correctly cache the model for the current type. However,
                 as context caching is assumed elsewhere to avoid cyclical model creation, this was deemed to be
                 sufficient.
                 */
                if (currentTypeIsParentSubType && currentTypeIsParentApiModelSubType) {
                    context.resolve(parentClass);
                    return context.resolve(currentType);
                }
            }
        }

        return model;
    }

    /**
     *  Decorate the name based on the JsonView
     */
    protected String decorateModelName(ModelConverterContext context, String originalName) {
        String name = originalName;
        if (context.getJsonView() != null && context.getJsonView().value().length > 0) {
            String COMBINER = "-or-";
            StringBuffer sb = new StringBuffer();
            for (Class<?> view : context.getJsonView().value()) {
                sb.append(view.getSimpleName()).append(COMBINER);
            }
            String suffix = sb.toString().substring(0, sb.length() - COMBINER.length());
            name = originalName + "_" + suffix;
        }
        return name;
    }

    private boolean hiddenByJsonView(Annotation[] annotations,
        ModelConverterContext context) {
        JsonView jsonView = context.getJsonView();
        if (jsonView == null)
            return false;

        Class<?>[] filters = jsonView.value();
        boolean containsJsonViewAnnotation = false;
        for (Annotation ant : annotations) {
            if (ant instanceof JsonView) {
                containsJsonViewAnnotation = true;
                Class<?>[] views = ((JsonView) ant).value();
                for (Class<?> f : filters) {
                    for (Class<?> v : views) {
                        if (v == f || v.isAssignableFrom(f)) {
                            return false;
                        }
                    }
                }
            }
        }
        return containsJsonViewAnnotation;
    }

    protected boolean ignore(final Annotated member, final XmlAccessorType xmlAccessorTypeAnnotation, final String propName, final Set<String> propertiesToIgnore) {
        if (propertiesToIgnore.contains(propName)) {
            return true;
        }
        if (member.hasAnnotation(JsonIgnore.class)) {
            return true;
        }
        if (xmlAccessorTypeAnnotation == null) {
            return false;
        }
        if (xmlAccessorTypeAnnotation.value().equals(XmlAccessType.NONE)) {
            if (!member.hasAnnotation(XmlElement.class) &&
                    !member.hasAnnotation(XmlAttribute.class) &&
                    !member.hasAnnotation(XmlElementRef.class) &&
                    !member.hasAnnotation(XmlElementRefs.class) &&
                    !member.hasAnnotation(JsonProperty.class)) {
                return true;
            }
        }
        return false;
    }

    private void handleUnwrapped(List<Property> props, Model innerModel, String prefix, String suffix) {
        if (StringUtils.isBlank(suffix) && StringUtils.isBlank(prefix)) {
            if (innerModel != null) {
                Map<String, Property> innerProps = innerModel.getProperties();
                if (innerProps != null) {
                    props.addAll(innerProps.values());
                }
            }
        } else {
            if (prefix == null) {
                prefix = "";
            }
            if (suffix == null) {
                suffix = "";
            }
            for (Property prop : innerModel.getProperties().values()) {
                props.add(prop.rename(prefix + prop.getName() + suffix));
            }
        }
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
            Model model = context.resolve(type);
            if (model instanceof ComposedModel) {
                model = ((ComposedModel) model).getChild();
            }
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
                ap.setMinimum(new BigDecimal(min.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Max")) {
            if (property instanceof AbstractNumericProperty) {
                Max max = (Max) annos.get("javax.validation.constraints.Max");
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                ap.setMaximum(new BigDecimal(max.value()));
            }
        }
        if (annos.containsKey("javax.validation.constraints.Size")) {
            Size size = (Size) annos.get("javax.validation.constraints.Size");
            if (property instanceof AbstractNumericProperty) {
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                ap.setMinimum(new BigDecimal(size.min()));
                ap.setMaximum(new BigDecimal(size.max()));
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
                ap.setMinimum(new BigDecimal(min.value()));
                ap.setExclusiveMinimum(!min.inclusive());
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMax")) {
            DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
            if (property instanceof AbstractNumericProperty) {
                AbstractNumericProperty ap = (AbstractNumericProperty) property;
                ap.setMaximum(new BigDecimal(max.value()));
                ap.setExclusiveMaximum(!max.inclusive());
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
                ComposedModel child = new ComposedModel().parent(new RefModel(model.getName(), RefFormat.INTERNAL)).child(impl);
                context.defineModel(impl.getName(), child, subtypeType, null);
                ++count;
            }
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
}
