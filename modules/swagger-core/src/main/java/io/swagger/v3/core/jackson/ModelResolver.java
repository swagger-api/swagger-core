package io.swagger.v3.core.jackson;

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
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Constants;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ObjectMapperFactory;
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
import io.swagger.v3.oas.models.media.ObjectSchema;
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.core.util.RefUtils.constructRef;

public class ModelResolver extends AbstractModelConverter implements ModelConverter {
    Logger LOGGER = LoggerFactory.getLogger(ModelResolver.class);

    public static final String SET_PROPERTY_OF_COMPOSED_MODEL_AS_SIBLING = "composed-model-properties-as-sibiling";
    public static final String SET_PROPERTY_OF_ENUMS_AS_REF = "enums-as-ref";

    public static boolean composedModelPropertiesAsSibling = System.getProperty(SET_PROPERTY_OF_COMPOSED_MODEL_AS_SIBLING) != null ? true : false;

    /**
     * Allows all enums to be resolved as a reference to a scheme added to the components section.
     */
    public static boolean enumsAsRef = System.getProperty(SET_PROPERTY_OF_ENUMS_AS_REF) != null ? true : false;

    public ModelResolver(ObjectMapper mapper) {
        super(mapper);
    }
    public ModelResolver(ObjectMapper mapper, TypeNameResolver typeNameResolver) {
        super(mapper, typeNameResolver);
    }

    public ObjectMapper objectMapper() {
        return _mapper;
    }

    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {

        boolean isPrimitive = false;
        Schema model = null;

        if (annotatedType == null) {
            return null;
        }
        if (this.shouldIgnoreClass(annotatedType.getType())) {
            return null;
        }

        final JavaType type;
        if (annotatedType.getType() instanceof JavaType) {
            type = (JavaType) annotatedType.getType();
        } else {
            type = _mapper.constructType(annotatedType.getType());
        }

        final Annotation resolvedSchemaOrArrayAnnotation = AnnotationsUtils.mergeSchemaAnnotations(annotatedType.getCtxAnnotations(), type);
        final io.swagger.v3.oas.annotations.media.Schema resolvedSchemaAnnotation =
                resolvedSchemaOrArrayAnnotation == null ?
                        null :
                        resolvedSchemaOrArrayAnnotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema ?
                                ((io.swagger.v3.oas.annotations.media.ArraySchema) resolvedSchemaOrArrayAnnotation).schema() :
                                (io.swagger.v3.oas.annotations.media.Schema) resolvedSchemaOrArrayAnnotation;

        final io.swagger.v3.oas.annotations.media.ArraySchema resolvedArrayAnnotation =
                resolvedSchemaOrArrayAnnotation == null ?
                        null :
                        resolvedSchemaOrArrayAnnotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema ?
                                (io.swagger.v3.oas.annotations.media.ArraySchema) resolvedSchemaOrArrayAnnotation :
                                null;

        final BeanDescription beanDesc;
        {
            BeanDescription recurBeanDesc = _mapper.getSerializationConfig().introspect(type);

            HashSet<String> visited = new HashSet<>();
            JsonSerialize jsonSerialize = recurBeanDesc.getClassAnnotations().get(JsonSerialize.class);
            while (jsonSerialize != null && !Void.class.equals(jsonSerialize.as())) {
                String asName = jsonSerialize.as().getName();
                if (visited.contains(asName)) break;
                visited.add(asName);

                recurBeanDesc = _mapper.getSerializationConfig().introspect(
                        _mapper.constructType(jsonSerialize.as())
                );
                jsonSerialize = recurBeanDesc.getClassAnnotations().get(JsonSerialize.class);
            }
            beanDesc = recurBeanDesc;
        }


        String name = annotatedType.getName();
        if (StringUtils.isBlank(name)) {
            // allow override of name from annotation
            if (!annotatedType.isSkipSchemaName() && resolvedSchemaAnnotation != null && !resolvedSchemaAnnotation.name().isEmpty()) {
                name = resolvedSchemaAnnotation.name();
            }
            if (StringUtils.isBlank(name) && !ReflectionUtils.isSystemType(type)) {
                name = _typeName(type, beanDesc);
            }
        }

        name = decorateModelName(annotatedType, name);

        // if we have a ref we don't consider anything else
        if (resolvedSchemaAnnotation != null &&
                StringUtils.isNotEmpty(resolvedSchemaAnnotation.ref())) {
            if (resolvedArrayAnnotation == null) {
                return new Schema().$ref(resolvedSchemaAnnotation.ref()).name(name);
            } else {
                ArraySchema schema = new ArraySchema();
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                return schema.items(new Schema().$ref(resolvedSchemaAnnotation.ref()).name(name));
            }
        }

        if (!annotatedType.isSkipOverride() && resolvedSchemaAnnotation != null && !Void.class.equals(resolvedSchemaAnnotation.implementation())) {
            Class<?> cls = resolvedSchemaAnnotation.implementation();

            LOGGER.debug("overriding datatype from {} to {}", type, cls.getName());

            Annotation[] ctxAnnotation = null;
            if (resolvedArrayAnnotation != null && annotatedType.getCtxAnnotations() != null) {
                List<Annotation> annList = new ArrayList<>();
                for (Annotation a: annotatedType.getCtxAnnotations()) {
                    if (!(a instanceof ArraySchema)) {
                        annList.add(a);
                    }
                }
                annList.add(resolvedSchemaAnnotation);
                ctxAnnotation = annList.toArray(new Annotation[annList.size()]);
            } else {
                ctxAnnotation = annotatedType.getCtxAnnotations();
            }

            AnnotatedType aType = new AnnotatedType()
                    .type(cls)
                    .ctxAnnotations(ctxAnnotation)
                    .parent(annotatedType.getParent())
                    .name(annotatedType.getName())
                    .resolveAsRef(annotatedType.isResolveAsRef())
                    .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                    .propertyName(annotatedType.getPropertyName())
                    .skipOverride(true);
            if (resolvedArrayAnnotation != null) {
                ArraySchema schema = new ArraySchema();
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                Schema innerSchema = null;

                Schema primitive = PrimitiveType.createProperty(cls);
                if (primitive != null) {
                    innerSchema = primitive;
                } else {
                    innerSchema = context.resolve(aType);
                    if (innerSchema != null && "object".equals(innerSchema.getType()) && StringUtils.isNotBlank(innerSchema.getName())) {
                        // create a reference for the items
                        if (context.getDefinedModels().containsKey(innerSchema.getName())) {
                            innerSchema = new Schema().$ref(constructRef(innerSchema.getName()));
                        }
                    } else if (innerSchema != null && innerSchema.get$ref() != null) {
                        innerSchema = new Schema().$ref(StringUtils.isNotEmpty(innerSchema.get$ref()) ? innerSchema.get$ref() : innerSchema.getName());
                    }
                }
                schema.setItems(innerSchema);
                return schema;
            } else {
                Schema implSchema = context.resolve(aType);
                if (implSchema != null && aType.isResolveAsRef() && "object".equals(implSchema.getType()) && StringUtils.isNotBlank(implSchema.getName())) {
                    // create a reference for the items
                    if (context.getDefinedModels().containsKey(implSchema.getName())) {
                        implSchema = new Schema().$ref(constructRef(implSchema.getName()));
                    }
                } else if (implSchema != null && implSchema.get$ref() != null) {
                    implSchema = new Schema().$ref(StringUtils.isNotEmpty(implSchema.get$ref()) ? implSchema.get$ref() : implSchema.getName());
                }
                return implSchema;
            }
        }

        if (model == null && !annotatedType.isSkipOverride() && resolvedSchemaAnnotation != null &&
                StringUtils.isNotEmpty(resolvedSchemaAnnotation.type()) &&
                !resolvedSchemaAnnotation.type().equals("object")) {
            PrimitiveType primitiveType = PrimitiveType.fromTypeAndFormat(resolvedSchemaAnnotation.type(), resolvedSchemaAnnotation.format());
            if (primitiveType == null) {
                primitiveType = PrimitiveType.fromType(type);
            }
            if (primitiveType == null) {
                primitiveType = PrimitiveType.fromName(resolvedSchemaAnnotation.type());
            }
            if (primitiveType != null) {
                Schema primitive = primitiveType.createProperty();
                model = primitive;
                isPrimitive = true;

            }
        }

        if (model == null && type.isEnumType()) {
            model = new StringSchema();
            _addEnumProps(type.getRawClass(), model);
            isPrimitive = true;
        }
        if (model == null) {
            PrimitiveType primitiveType = PrimitiveType.fromType(type);
            if (primitiveType != null) {
                model = PrimitiveType.fromType(type).createProperty();
                isPrimitive = true;
            }
        }

        if (!annotatedType.isSkipJsonIdentity()) {
            JsonIdentityInfo jsonIdentityInfo = AnnotationsUtils.getAnnotation(JsonIdentityInfo.class, annotatedType.getCtxAnnotations());
            if (jsonIdentityInfo == null) {
                jsonIdentityInfo = type.getRawClass().getAnnotation(JsonIdentityInfo.class);
            }
            if (model == null && jsonIdentityInfo != null) {
                JsonIdentityReference jsonIdentityReference = AnnotationsUtils.getAnnotation(JsonIdentityReference.class, annotatedType.getCtxAnnotations());
                if (jsonIdentityReference == null) {
                    jsonIdentityReference = type.getRawClass().getAnnotation(JsonIdentityReference.class);
                }
                model = GeneratorWrapper.processJsonIdentity(annotatedType, context, _mapper, jsonIdentityInfo, jsonIdentityReference);
                if (model != null) {
                    return model;
                }
            }
        }

        if (model == null && annotatedType.getJsonUnwrappedHandler() != null) {
            model = annotatedType.getJsonUnwrappedHandler().apply(annotatedType);
            if (model == null) {
                return null;
            }
        }

        if ("Object".equals(name)) {
            return new Schema();
        }

        if (isPrimitive) {
            if (annotatedType.isSchemaProperty()) {
                //model.name(name);
            }
            XML xml = resolveXml(beanDesc.getClassInfo(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
            if (xml != null) {
                model.xml(xml);
            }
            resolveSchemaMembers(model, annotatedType);

            if (resolvedArrayAnnotation != null) {
                ArraySchema schema = new ArraySchema();
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                schema.setItems(model);
                return schema;
            }
            if (type.isEnumType() && shouldResolveEnumAsRef(resolvedSchemaAnnotation)) {
                // Store off the ref and add the enum as a top-level model
                context.defineModel(name, model, annotatedType, null);
                // Return the model as a ref only property
                model = new Schema().$ref(name);
            }
            return model;
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
        Schema resolvedModel = context.resolve(annotatedType);
        if (resolvedModel != null) {
            if (name != null && name.equals(resolvedModel.getName())) {
                return resolvedModel;
            }
        }

        // using deprecated method to maintain compatibility with jackson version < 2.9
        //alternatively use AnnotatedMember jsonValueMember = beanDesc.findJsonValueAccessor();
        final AnnotatedMethod jsonValueMethod  = beanDesc.findJsonValueMethod();
        if(jsonValueMethod != null) {
            AnnotatedType aType = new AnnotatedType()
                    .type(jsonValueMethod.getType())
                    .parent(annotatedType.getParent())
                    .name(annotatedType.getName())
                    .schemaProperty(annotatedType.isSchemaProperty())
                    .resolveAsRef(annotatedType.isResolveAsRef())
                    .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                    .propertyName(annotatedType.getPropertyName())
                    .skipOverride(true);
            return context.resolve(aType);
        }

        List<Class<?>> composedSchemaReferencedClasses = getComposedSchemaReferencedClasses(type.getRawClass(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
        boolean isComposedSchema = composedSchemaReferencedClasses != null;

        if (type.isContainerType()) {
            // TODO currently a MapSchema or ArraySchema don't also support composed schema props (oneOf,..)
            isComposedSchema = false;
            JavaType keyType = type.getKeyType();
            JavaType valueType = type.getContentType();
            String pName = null;
            if (valueType != null) {
                BeanDescription valueTypeBeanDesc = _mapper.getSerializationConfig().introspect(valueType);
                pName = _typeName(valueType, valueTypeBeanDesc);
            }
            Annotation[] schemaAnnotations = null;
            if (resolvedSchemaAnnotation != null) {
                schemaAnnotations = new Annotation[]{resolvedSchemaAnnotation};
            }
            if (keyType != null && valueType != null) {
                if (ReflectionUtils.isSystemType(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    context.resolve(new AnnotatedType().type(valueType).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                    return null;
                }
                Schema addPropertiesSchema = context.resolve(
                        new AnnotatedType()
                                .type(valueType)
                                .schemaProperty(annotatedType.isSchemaProperty())
                                .ctxAnnotations(schemaAnnotations)
                                .skipSchemaName(true)
                                .resolveAsRef(annotatedType.isResolveAsRef())
                                .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                                .propertyName(annotatedType.getPropertyName())
                                .parent(annotatedType.getParent()));
                if (addPropertiesSchema != null) {
                    if (StringUtils.isNotBlank(addPropertiesSchema.getName())) {
                        pName = addPropertiesSchema.getName();
                    }
                    if ("object".equals(addPropertiesSchema.getType()) && pName != null) {
                        // create a reference for the items
                        if (context.getDefinedModels().containsKey(pName)) {
                            addPropertiesSchema = new Schema().$ref(constructRef(pName));
                        }
                    } else if (addPropertiesSchema.get$ref() != null) {
                        addPropertiesSchema = new Schema().$ref(StringUtils.isNotEmpty(addPropertiesSchema.get$ref()) ? addPropertiesSchema.get$ref() : addPropertiesSchema.getName());
                    }
                }
                Schema mapModel = new MapSchema().additionalProperties(addPropertiesSchema);
                mapModel.name(name);
                model = mapModel;
                //return model;
            } else if (valueType != null) {
                if (ReflectionUtils.isSystemType(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    context.resolve(new AnnotatedType().type(valueType).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                    return null;
                }
                Schema items = context.resolve(new AnnotatedType()
                        .type(valueType)
                        .schemaProperty(annotatedType.isSchemaProperty())
                        .ctxAnnotations(schemaAnnotations)
                        .skipSchemaName(true)
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .propertyName(annotatedType.getPropertyName())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .parent(annotatedType.getParent()));

                if (items == null) {
                    return null;
                }
                if (annotatedType.isSchemaProperty() && annotatedType.getCtxAnnotations() != null && annotatedType.getCtxAnnotations().length > 0) {
                    if (!"object".equals(items.getType())) {
                        for (Annotation annotation : annotatedType.getCtxAnnotations()) {
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
                }
                if (StringUtils.isNotBlank(items.getName())) {
                    pName = items.getName();
                }
                if ("object".equals(items.getType()) && pName != null) {
                    // create a reference for the items
                    if (context.getDefinedModels().containsKey(pName)) {
                        items = new Schema().$ref(constructRef(pName));
                    }
                } else if (items.get$ref() != null) {
                    items = new Schema().$ref(StringUtils.isNotEmpty(items.get$ref()) ? items.get$ref() : items.getName());
                }

                Schema arrayModel =
                        new ArraySchema().items(items);
                if (_isSetType(type.getRawClass())) {
                    arrayModel.setUniqueItems(true);
                }
                arrayModel.name(name);
                model = arrayModel;
            } else {
                if (ReflectionUtils.isSystemType(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    return null;
                }
            }
        } else if (isComposedSchema) {
            model = new ComposedSchema()
                    .type("object")
                    .name(name);
        } else {
            if (_isOptionalType(type)) {
                AnnotatedType aType = new AnnotatedType()
                        .type(type.containedType(0))
                        .ctxAnnotations(annotatedType.getCtxAnnotations())
                        .parent(annotatedType.getParent())
                        .schemaProperty(annotatedType.isSchemaProperty())
                        .name(annotatedType.getName())
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .propertyName(annotatedType.getPropertyName())
                        .skipOverride(true);
                model = context.resolve(aType);
                return model;
            } else {
                model = new Schema()
                        .type("object")
                        .name(name);
            }
        }

        if (!type.isContainerType() && StringUtils.isNotBlank(name)) {
            // define the model here to support self/cyclic referencing of models
            context.defineModel(name, model, annotatedType, null);
        }

        XML xml = resolveXml(beanDesc.getClassInfo(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
        if (xml != null) {
            model.xml(xml);
        }

        if (!(model instanceof ArraySchema) || (model instanceof ArraySchema && resolvedArrayAnnotation == null)) {
            resolveSchemaMembers(model, annotatedType);
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

        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        List<String> ignoredProps = getIgnoredProperties(beanDesc);
        properties.removeIf(p -> ignoredProps.contains(p.getName()));
        for (BeanPropertyDefinition propDef : properties) {
            Schema property = null;
            String propName = propDef.getName();
            Annotation[] annotations = null;

            AnnotatedMember member = propDef.getPrimaryMember();
            if (member == null) {
                final BeanDescription deserBeanDesc = _mapper.getDeserializationConfig().introspect(type);
                List<BeanPropertyDefinition> deserProperties = deserBeanDesc.findProperties();
                for (BeanPropertyDefinition prop : deserProperties) {
                    if (StringUtils.isNotBlank(prop.getInternalName()) && prop.getInternalName().equals(propDef.getInternalName())) {
                        member = prop.getPrimaryMember();
                        break;
                    }
                }
            }

            // hack to avoid clobbering properties with get/is names
            // it's ugly but gets around https://github.com/swagger-api/swagger-core/issues/415
            if(propDef.getPrimaryMember() != null) {
                final JsonProperty jsonPropertyAnn = propDef.getPrimaryMember().getAnnotation(JsonProperty.class);
                if (jsonPropertyAnn == null || !jsonPropertyAnn.value().equals(propName)) {
                    if (member != null) {
                        java.lang.reflect.Member innerMember = member.getMember();
                        if (innerMember != null) {
                            String altName = innerMember.getName();
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
            }

            PropertyMetadata md = propDef.getMetadata();

            if (member != null && !ignore(member, xmlAccessorTypeAnnotation, propName, propertiesToIgnore)) {

                List<Annotation> annotationList = new ArrayList<Annotation>();
                for (Annotation a : member.annotations()) {
                    annotationList.add(a);
                }

                annotations = annotationList.toArray(new Annotation[annotationList.size()]);

                if(hiddenByJsonView(annotations, annotatedType)) {
                    continue;
                }

                JavaType propType = member.getType();
                if(propType != null && "void".equals(propType.getRawClass().getName())) {
                    if (member instanceof AnnotatedMethod) {
                        propType = ((AnnotatedMethod)member).getParameterType(0);
                    }

                }
                String propSchemaName = null;
                io.swagger.v3.oas.annotations.media.Schema ctxSchema = AnnotationsUtils.getSchemaAnnotation(annotations);
                if (AnnotationsUtils.hasSchemaAnnotation(ctxSchema)) {
                    if (!StringUtils.isBlank(ctxSchema.name())) {
                        propSchemaName = ctxSchema.name();
                    }
                }
                if (propSchemaName == null) {
                    io.swagger.v3.oas.annotations.media.ArraySchema ctxArraySchema = AnnotationsUtils.getArraySchemaAnnotation(annotations);
                    if (AnnotationsUtils.hasArrayAnnotation(ctxArraySchema)) {
                        if (AnnotationsUtils.hasSchemaAnnotation(ctxArraySchema.schema())) {
                            if (!StringUtils.isBlank(ctxArraySchema.schema().name())) {
                                propSchemaName = ctxArraySchema.schema().name();
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(propSchemaName)) {
                    propName = propSchemaName;
                }
                Annotation propSchemaOrArray = AnnotationsUtils.mergeSchemaAnnotations(annotations, propType);
                final io.swagger.v3.oas.annotations.media.Schema propResolvedSchemaAnnotation =
                        propSchemaOrArray == null ?
                                null :
                                propSchemaOrArray instanceof io.swagger.v3.oas.annotations.media.ArraySchema ?
                                        ((io.swagger.v3.oas.annotations.media.ArraySchema) propSchemaOrArray).schema() :
                                        (io.swagger.v3.oas.annotations.media.Schema) propSchemaOrArray;

                io.swagger.v3.oas.annotations.media.Schema.AccessMode accessMode = resolveAccessMode(propDef, type, propResolvedSchemaAnnotation);


                AnnotatedType aType = new AnnotatedType()
                        .type(propType)
                        .ctxAnnotations(annotations)
                        //.name(propName)
                        .parent(model)
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .skipSchemaName(true)
                        .schemaProperty(true)
                        .propertyName(propName);

                final AnnotatedMember propMember = member;
                aType.jsonUnwrappedHandler((t) -> {
                    JsonUnwrapped uw = propMember.getAnnotation(JsonUnwrapped.class);
                    if (uw != null && uw.enabled()) {
                        t
                            .ctxAnnotations(null)
                            .jsonUnwrappedHandler(null)
                            .resolveAsRef(false);
                        handleUnwrapped(props, context.resolve(t), uw.prefix(), uw.suffix());
                        return null;
                    } else {
                        return new Schema();
                        //t.jsonUnwrappedHandler(null);
                        //return context.resolve(t);
                    }
                });
                property = clone(context.resolve(aType));

                if (property != null) {
                    if (property.get$ref() == null) {
                        Boolean required = md.getRequired();
                        if (required != null && !Boolean.FALSE.equals(required)) {
                            addRequiredItem(model, propName);
                        } else {
                            if (propDef.isRequired()) {
                                addRequiredItem(model, propName);
                            }
                        }
                        if (accessMode != null) {
                            switch (accessMode) {
                                case AUTO:
                                    break;
                                case READ_ONLY:
                                    property.readOnly(true);
                                    break;
                                case READ_WRITE:
                                    break;
                                case WRITE_ONLY:
                                    property.writeOnly(true);
                                    break;
                                default:
                            }
                        }
                    }
                    final BeanDescription propBeanDesc = _mapper.getSerializationConfig().introspect(propType);
                    if (property != null && !propType.isContainerType()) {
                        if ("object".equals(property.getType())) {
                            // create a reference for the property
                            String pName = _typeName(propType, propBeanDesc);
                            if (StringUtils.isNotBlank(property.getName())) {
                                pName = property.getName();
                            }

                            if (context.getDefinedModels().containsKey(pName)) {
                                property = new Schema().$ref(constructRef(pName));
                            }
                        } else if (property.get$ref() != null) {
                            property = new Schema().$ref(StringUtils.isNotEmpty(property.get$ref()) ? property.get$ref() : property.getName());
                        }
                    }
                    property.setName(propName);
                    JAXBAnnotationsHelper.apply(propBeanDesc.getClassInfo(), annotations, property);
                    applyBeanValidatorAnnotations(property, annotations, model);

                    props.add(property);
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
        if (!type.isContainerType() && StringUtils.isNotBlank(name)) {
            context.defineModel(name, model, annotatedType, null);
        }

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

        if (resolvedSchemaAnnotation != null) {
            String ref = resolvedSchemaAnnotation.ref();
            // consider ref as is
            if (!StringUtils.isBlank(ref)) {
                model.$ref(ref);
            }
            Class<?> not = resolvedSchemaAnnotation.not();
            if (!Void.class.equals(not)) {
                model.not((new Schema().$ref(context.resolve(new AnnotatedType().type(not).jsonViewAnnotation(annotatedType.getJsonViewAnnotation())).getName())));
            }
            if (resolvedSchemaAnnotation.requiredProperties() != null &&
                    resolvedSchemaAnnotation.requiredProperties().length > 0 &&
                    StringUtils.isNotBlank(resolvedSchemaAnnotation.requiredProperties()[0])) {
                for (String prop : resolvedSchemaAnnotation.requiredProperties()) {
                    addRequiredItem(model, prop);
                }
            }
        }

        if (isComposedSchema) {

            ComposedSchema composedSchema = (ComposedSchema) model;

            Class<?>[] allOf = resolvedSchemaAnnotation.allOf();
            Class<?>[] anyOf = resolvedSchemaAnnotation.anyOf();
            Class<?>[] oneOf = resolvedSchemaAnnotation.oneOf();

            List<Class<?>> allOfFiltered = Stream.of(allOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            allOfFiltered.forEach(c -> {
                Schema allOfRef = context.resolve(new AnnotatedType().type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
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
                Schema anyOfRef = context.resolve(new AnnotatedType().type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
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
                Schema oneOfRef = context.resolve(new AnnotatedType().type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                if (oneOfRef != null) {
                    if (StringUtils.isBlank(oneOfRef.getName())) {
                        composedSchema.addOneOfItem(oneOfRef);
                    } else {
                        composedSchema.addOneOfItem(new Schema().$ref(oneOfRef.getName()));
                    }
                    // remove shared properties defined in the parent
                    if (isSubtype(beanDesc.getClassInfo(), c)) {
                        removeParentProperties(composedSchema, oneOfRef);
                    }
                }

            });

            if (!composedModelPropertiesAsSibling) {
                if (composedSchema.getAllOf() != null && !composedSchema.getAllOf().isEmpty()) {
                    if (composedSchema.getProperties() != null && !composedSchema.getProperties().isEmpty()) {
                        ObjectSchema propSchema = new ObjectSchema();
                        propSchema.properties(composedSchema.getProperties());
                        composedSchema.setProperties(null);
                        composedSchema.addAllOfItem(propSchema);
                    }
                }
            }
        }

        if (!type.isContainerType() && StringUtils.isNotBlank(name)) {
            // define the model here to support self/cyclic referencing of models
            context.defineModel(name, model, annotatedType, null);
        }

        if (model != null && annotatedType.isResolveAsRef() &&
            (isComposedSchema || "object".equals(model.getType())) &&
            StringUtils.isNotBlank(model.getName()))
        {
            if (context.getDefinedModels().containsKey(model.getName())) {
                model = new Schema().$ref(constructRef(model.getName()));
            }
        } else if (model != null && model.get$ref() != null) {
            model = new Schema().$ref(StringUtils.isNotEmpty(model.get$ref()) ? model.get$ref() : model.getName());
        }

        if (model != null && resolvedArrayAnnotation != null) {
            if (!"array".equals(model.getType())) {
                ArraySchema schema = new ArraySchema();
                schema.setItems(model);
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                return schema;
            } else {
                if (model instanceof ArraySchema) {
                    resolveArraySchema(annotatedType, (ArraySchema) model, resolvedArrayAnnotation);
                }
            }
        }

        resolveDiscriminatorProperty(type, context, model);

        return model;
    }

    private boolean shouldResolveEnumAsRef(io.swagger.v3.oas.annotations.media.Schema resolvedSchemaAnnotation) {
        return (resolvedSchemaAnnotation != null && resolvedSchemaAnnotation.enumAsRef()) || ModelResolver.enumsAsRef;
    }

    private Schema clone(Schema property) {
        if(property == null)
            return property;
        try {
            String cloneName = property.getName();
            property = Json.mapper().readValue(Json.pretty(property), Schema.class);
            property.setName(cloneName);
        } catch (IOException e) {
            LOGGER.error("Could not clone property, e");
        }
        return property;
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


    protected boolean _isOptionalType(JavaType propType) {
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

    private void handleUnwrapped(List<Schema> props, Schema innerModel, String prefix, String suffix) {
        if (StringUtils.isBlank(suffix) && StringUtils.isBlank(prefix)) {
            if (innerModel.getProperties() != null) {
                props.addAll(innerModel.getProperties().values());
            }
        } else {
            if (prefix == null) {
                prefix = "";
            }
            if (suffix == null) {
                suffix = "";
            }
            if (innerModel.getProperties() != null) {
                for (Schema prop : (Collection<Schema>) innerModel.getProperties().values()) {
                    try {
                        Schema clonedProp = Json.mapper().readValue(Json.pretty(prop), Schema.class);
                        clonedProp.setName(prefix + prop.getName() + suffix);
                        props.add(clonedProp);
                    } catch (IOException e) {
                        LOGGER.error("Exception cloning property", e);
                        return;
                    }
                }
            }
        }
    }

    private enum GeneratorWrapper {
        PROPERTY(ObjectIdGenerators.PropertyGenerator.class) {
            @Override
            protected Schema processAsProperty(String propertyName, AnnotatedType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                /*
                 * When generator = ObjectIdGenerators.PropertyGenerator.class and
                 * @JsonIdentityReference(alwaysAsId = false) then property is serialized
                 * in the same way it is done without @JsonIdentityInfo annotation.
                 */
                return null;
            }

            @Override
            protected Schema processAsId(String propertyName, AnnotatedType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                final JavaType javaType;
                if (type.getType() instanceof JavaType) {
                    javaType = (JavaType)type.getType();
                } else {
                    javaType = mapper.constructType(type.getType());
                }
                final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(javaType);
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
                            Annotation[] annotations = list.toArray(new Annotation[list.size()]);
                            Annotation propSchemaOrArray = AnnotationsUtils.mergeSchemaAnnotations(annotations, propType);
                            AnnotatedType aType = new AnnotatedType()
                                    .type(propType)
                                    .ctxAnnotations(annotations)
                                    .jsonViewAnnotation(type.getJsonViewAnnotation())
                                    .schemaProperty(true)
                                    .propertyName(type.getPropertyName());

                            return context.resolve(aType);
                        }
                    }
                }
                return null;
            }
        },
        INT(ObjectIdGenerators.IntSequenceGenerator.class) {
            @Override
            protected Schema processAsProperty(String propertyName, AnnotatedType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                Schema id = new IntegerSchema();
                return process(id, propertyName, type, context);
            }

            @Override
            protected Schema processAsId(String propertyName, AnnotatedType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                return new IntegerSchema();
            }
        },
        UUID(ObjectIdGenerators.UUIDGenerator.class) {
            @Override
            protected Schema processAsProperty(String propertyName, AnnotatedType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                Schema id = new UUIDSchema();
                return process(id, propertyName, type, context);
            }

            @Override
            protected Schema processAsId(String propertyName, AnnotatedType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                return new UUIDSchema();
            }
        },
        NONE(ObjectIdGenerators.None.class) {
            // When generator = ObjectIdGenerators.None.class property should be processed as normal property.
            @Override
            protected Schema processAsProperty(String propertyName, AnnotatedType type,
                                               ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }

            @Override
            protected Schema processAsId(String propertyName, AnnotatedType type,
                                         ModelConverterContext context, ObjectMapper mapper) {
                return null;
            }
        };

        private final Class<? extends ObjectIdGenerator> generator;

        GeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
            this.generator = generator;
        }

        protected abstract Schema processAsProperty(String propertyName, AnnotatedType type,
                                                    ModelConverterContext context, ObjectMapper mapper);

        protected abstract Schema processAsId(String propertyName, AnnotatedType type,
                                              ModelConverterContext context, ObjectMapper mapper);

        public static Schema processJsonIdentity(AnnotatedType type, ModelConverterContext context,
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

        private static Schema process(Schema id, String propertyName, AnnotatedType type,
                                      ModelConverterContext context) {

            Schema model = context.resolve(removeJsonIdentityAnnotations(type));
            Schema mi = model;
            mi.addProperties(propertyName, id);
            return new Schema().$ref(StringUtils.isNotEmpty(mi.get$ref())
                    ? mi.get$ref() : mi.getName());
        }
        private static AnnotatedType removeJsonIdentityAnnotations(AnnotatedType type) {
            return new AnnotatedType()
                    .jsonUnwrappedHandler(type.getJsonUnwrappedHandler())
                    .jsonViewAnnotation(type.getJsonViewAnnotation())
                    .name(type.getName())
                    .parent(type.getParent())
                    .resolveAsRef(false)
                    .schemaProperty(type.isSchemaProperty())
                    .skipOverride(type.isSkipOverride())
                    .skipSchemaName(type.isSkipSchemaName())
                    .type(type.getType())
                    .skipJsonIdentity(true)
                    .propertyName(type.getPropertyName())
                    .ctxAnnotations(AnnotationsUtils.removeAnnotations(type.getCtxAnnotations(), JsonIdentityInfo.class, JsonIdentityReference.class));
        }
    }

    protected void applyBeanValidatorAnnotations(Schema property, Annotation[] annotations, Schema parent) {
        Map<String, Annotation> annos = new HashMap<String, Annotation>();
        if (annotations != null) {
            for (Annotation anno : annotations) {
                annos.put(anno.annotationType().getName(), anno);
            }
        }
        if (parent != null &&
                (
                        annos.containsKey("javax.validation.constraints.NotNull") ||
                        annos.containsKey("javax.validation.constraints.NotBlank") ||
                        annos.containsKey("javax.validation.constraints.NotEmpty")
                )) {
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

    private boolean resolveSubtypes(Schema model, BeanDescription bean, ModelConverterContext context) {
        final List<NamedType> types = _intr.findSubtypes(bean.getClassInfo());
        if (types == null) {
            return false;
        }

        /**
         * Remove the current class from the child classes. This happens if @JsonSubTypes references
         * the annotated class as a subtype.
         */
        removeSelfFromSubTypes(types, bean);

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

            final Schema subtypeModel = context.resolve(new AnnotatedType().type(subtypeType));

            if (    StringUtils.isBlank(subtypeModel.getName()) ||
                    subtypeModel.getName().equals(model.getName())) {
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

                composedSchema.setEnum(subtypeModel.getEnum());
            } else {
                composedSchema = (ComposedSchema) subtypeModel;
            }
            Schema refSchema = new Schema().$ref(model.getName());
            // allOf could have already being added during type resolving when @Schema(allOf..) is declared
            if (composedSchema.getAllOf() == null || !composedSchema.getAllOf().contains(refSchema)) {
                composedSchema.addAllOfItem(refSchema);
            }
            removeParentProperties(composedSchema, model);
            if (!composedModelPropertiesAsSibling) {
                if (composedSchema.getAllOf() != null && !composedSchema.getAllOf().isEmpty()) {
                    if (composedSchema.getProperties() != null && !composedSchema.getProperties().isEmpty()) {
                        ObjectSchema propSchema = new ObjectSchema();
                        propSchema.properties(composedSchema.getProperties());
                        composedSchema.setProperties(null);
                        composedSchema.addAllOfItem(propSchema);
                    }
                }
            }


            // replace previous schema..
            Class<?> currentType = subtype.getType();
            if (StringUtils.isNotBlank(composedSchema.getName())) {
                context.defineModel(composedSchema.getName(), composedSchema, new AnnotatedType().type(currentType), null);
            }


        }
        return count != 0;
    }

    private void removeSelfFromSubTypes(List<NamedType> types, BeanDescription bean) {
        Class<?> beanClass= bean.getType().getRawClass();
        types.removeIf(type -> beanClass.equals(type.getType()));
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

    protected List<Class<?>> getComposedSchemaReferencedClasses(Class<?> clazz, Annotation[] ctxAnnotations, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation) {

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

    protected String resolveDescription(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && !"".equals(schema.description())) {
            return schema.description();
        }
        return null;
    }

    protected String resolveTitle(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.title())) {
            return schema.title();
        }
        return null;
    }

    protected String resolveFormat(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.format())) {
            return schema.format();
        }
        return null;
    }

    protected String resolveDefaultValue(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null) {
            if (!schema.defaultValue().isEmpty()) {
                return schema.defaultValue();
            }
        }
        if (a == null) {
            return null;
        }
        XmlElement elem = a.getAnnotation(XmlElement.class);
        if (elem == null) {
            if (annotations != null) {
                for (Annotation ann: annotations) {
                    if (ann instanceof XmlElement) {
                        elem = (XmlElement)ann;
                        break;
                    }
                }
            }
        }
        if (elem != null) {
            if (!elem.defaultValue().isEmpty() && !"\u0000".equals(elem.defaultValue())) {
                return elem.defaultValue();
            }
        }
        return null;
    }

    protected Object resolveExample(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {

        if (schema != null) {
            if (!schema.example().isEmpty()) {
                try {
                    ObjectMapper mapper = ObjectMapperFactory.buildStrictGenericObjectMapper();
                    return mapper.readTree(schema.example());
                } catch (IOException e) {
                    return schema.example();
                }
            }
        }

        return null;
    }

    protected io.swagger.v3.oas.annotations.media.Schema.AccessMode resolveAccessMode(BeanPropertyDefinition propDef, JavaType type, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && !schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO)) {
            return schema.accessMode();
        } else if (schema != null && schema.readOnly()) {
            return io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
        } else if (schema != null && schema.writeOnly()) {
            return io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY;
        }

        if (propDef == null) {
            return null;
        }
        JsonProperty.Access access = null;
        if (propDef instanceof POJOPropertyBuilder) {
            access = ((POJOPropertyBuilder) propDef).findAccess();
        }
        boolean hasGetter = propDef.hasGetter();
        boolean hasSetter = propDef.hasSetter();
        boolean hasConstructorParameter = propDef.hasConstructorParameter();
        boolean hasField = propDef.hasField();


        if (access == null) {
            final BeanDescription beanDesc = _mapper.getDeserializationConfig().introspect(type);
            List<BeanPropertyDefinition> properties = beanDesc.findProperties();
            for (BeanPropertyDefinition prop : properties) {
                if (StringUtils.isNotBlank(prop.getInternalName()) && prop.getInternalName().equals(propDef.getInternalName())) {
                    if (prop instanceof POJOPropertyBuilder) {
                        access = ((POJOPropertyBuilder) prop).findAccess();
                    }
                    hasGetter = hasGetter || prop.hasGetter();
                    hasSetter = hasSetter || prop.hasSetter();
                    hasConstructorParameter = hasConstructorParameter || prop.hasConstructorParameter();
                    hasField = hasField || prop.hasField();
                    break;
                }
            }
        }
        if (access == null) {
            if (!hasGetter && !hasField && (hasConstructorParameter || hasSetter)) {
                return io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY;
            }
            return null;
        } else {
            switch (access) {
                case AUTO:
                    return io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO;
                case READ_ONLY:
                    return io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
                case READ_WRITE:
                    return io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;
                case WRITE_ONLY:
                    return io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY;
                default:
                    return io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO;
            }
        }
    }

    protected Boolean resolveReadOnly(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY)) {
            return true;
        } else if (schema != null && schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY)) {
            return null;
        } else if (schema != null && schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE)) {
            return null;
        } else if (schema != null && schema.readOnly()) {
            return schema.readOnly();
        }
        return null;
    }


    protected Boolean resolveNullable(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.nullable()) {
            return schema.nullable();
        }
        return null;
    }

    protected BigDecimal resolveMultipleOf(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.multipleOf() != 0) {
            return new BigDecimal(schema.multipleOf());
        }
        return null;
    }

    protected Integer resolveMaxLength(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.maxLength() != Integer.MAX_VALUE && schema.maxLength() > 0) {
            return schema.maxLength();
        }
        return null;
    }

    protected Integer resolveMinLength(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.minLength() > 0) {
            return schema.minLength();
        }
        return null;
    }

    protected BigDecimal resolveMinimum(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && NumberUtils.isNumber(schema.minimum())) {
            String filteredMinimum = schema.minimum().replaceAll(Constants.COMMA, StringUtils.EMPTY);
            return new BigDecimal(filteredMinimum);
        }
        return null;
    }

    protected BigDecimal resolveMaximum(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && NumberUtils.isNumber(schema.maximum())) {
            String filteredMaximum = schema.maximum().replaceAll(Constants.COMMA, StringUtils.EMPTY);
            return new BigDecimal(filteredMaximum);
        }
        return null;
    }

    protected Boolean resolveExclusiveMinimum(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.exclusiveMinimum()) {
            return schema.exclusiveMinimum();
        }
        return null;
    }

    protected Boolean resolveExclusiveMaximum(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.exclusiveMaximum()) {
            return schema.exclusiveMaximum();
        }
        return null;
    }

    protected String resolvePattern(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.pattern())) {
            return schema.pattern();
        }
        return null;
    }

    protected Integer resolveMinProperties(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.minProperties() > 0) {
            return schema.minProperties();
        }
        return null;
    }

    protected Integer resolveMaxProperties(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.maxProperties() > 0) {
            return schema.maxProperties();
        }
        return null;
    }

    protected List<String> resolveRequiredProperties(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null &&
                schema.requiredProperties() != null &&
                schema.requiredProperties().length > 0 &&
                StringUtils.isNotBlank(schema.requiredProperties()[0])) {

            return Arrays.asList(schema.requiredProperties());
        }
        return null;
    }

    protected Boolean resolveWriteOnly(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY)) {
            return null;
        } else if (schema != null && schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY)) {
            return true;
        } else if (schema != null && schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE)) {
            return null;
        } else if (schema != null && schema.writeOnly()) {
            return schema.writeOnly();
        }
        return null;
    }

    protected ExternalDocumentation resolveExternalDocumentation(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {

        ExternalDocumentation external = null;
        if (a != null) {
            io.swagger.v3.oas.annotations.ExternalDocumentation externalDocumentation = a.getAnnotation(io.swagger.v3.oas.annotations.ExternalDocumentation.class);
            external = resolveExternalDocumentation(externalDocumentation);
        }

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

    protected Boolean resolveDeprecated(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.deprecated()) {
            return schema.deprecated();
        }
        return null;
    }

    protected List<String> resolveAllowableValues(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null &&
                schema.allowableValues() != null &&
                schema.allowableValues().length > 0) {
            return Arrays.asList(schema.allowableValues());
        }
        return null;
    }

    protected Map<String, Object> resolveExtensions(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null &&
                schema.extensions() != null &&
                schema.extensions().length > 0) {
            return AnnotationsUtils.getExtensions(schema.extensions());
        }
        return null;
    }

    protected void resolveDiscriminatorProperty(JavaType type, ModelConverterContext context, Schema model) {
        // add JsonTypeInfo.property if not member of bean
        JsonTypeInfo typeInfo = type.getRawClass().getDeclaredAnnotation(JsonTypeInfo.class);
        if (typeInfo != null) {
            String typeInfoProp = typeInfo.property();
            if (StringUtils.isNotBlank(typeInfoProp)) {
                Schema modelToUpdate = model;
                if (StringUtils.isNotBlank(model.get$ref())) {
                    modelToUpdate = context.getDefinedModels().get(model.get$ref().substring(21));
                }
                if (modelToUpdate.getProperties() == null || !modelToUpdate.getProperties().keySet().contains(typeInfoProp)) {
                    Schema discriminatorSchema = new StringSchema().name(typeInfoProp);
                    modelToUpdate.addProperties(typeInfoProp, discriminatorSchema);
                    if (modelToUpdate.getRequired() == null || !modelToUpdate.getRequired().contains(typeInfoProp)) {
                        modelToUpdate.addRequiredItem(typeInfoProp);
                    }
                }
            }
        }
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
                            discriminator.mapping(mapping.value(), constructRef(context.resolve(new AnnotatedType().type(mapping.schema())).getName()));
                        }
                    }
                }
            }

            return discriminator;
        }
        return null;
    }

    protected XML resolveXml(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        // if XmlRootElement annotation, construct an Xml object and attach it to the model
        XmlRootElement rootAnnotation = null;
        if (a != null) {
            rootAnnotation = a.getAnnotation(XmlRootElement.class);
        }
        if (rootAnnotation == null) {
            if (annotations != null) {
                for (Annotation ann: annotations) {
                    if (ann instanceof XmlRootElement) {
                        rootAnnotation = (XmlRootElement)ann;
                        break;
                    }
                }
            }
        }
        if (rootAnnotation != null && !"".equals(rootAnnotation.name()) && !"##default".equals(rootAnnotation.name())) {
            XML xml = new XML().name(rootAnnotation.name());
            if (rootAnnotation.namespace() != null && !"".equals(rootAnnotation.namespace()) && !"##default".equals(rootAnnotation.namespace())) {
                xml.namespace(rootAnnotation.namespace());
            }
            return xml;
        }
        return null;
    }

    protected Integer resolveMinItems(AnnotatedType a, io.swagger.v3.oas.annotations.media.ArraySchema arraySchema) {
        if (arraySchema != null) {
            if (arraySchema.minItems() < Integer.MAX_VALUE) {
                return arraySchema.minItems();
            }
        }
        return null;
    }

    protected Integer resolveMaxItems(AnnotatedType a, io.swagger.v3.oas.annotations.media.ArraySchema arraySchema) {
        if (arraySchema != null) {
            if (arraySchema.maxItems() > 0) {
                return arraySchema.maxItems();
            }
        }
        return null;
    }

    protected Boolean resolveUniqueItems(AnnotatedType a, io.swagger.v3.oas.annotations.media.ArraySchema arraySchema) {
        if (arraySchema != null) {
            if (arraySchema.uniqueItems()) {
                return arraySchema.uniqueItems();
            }
        }
        return null;
    }

    protected Map<String, Object> resolveExtensions(AnnotatedType a, io.swagger.v3.oas.annotations.media.ArraySchema arraySchema) {
        if (arraySchema != null &&
                arraySchema.extensions() != null &&
                arraySchema.extensions().length > 0) {
            return AnnotationsUtils.getExtensions(arraySchema.extensions());
        }
        return null;
    }


    protected void resolveSchemaMembers(Schema schema, AnnotatedType annotatedType) {
        final JavaType type;
        if (annotatedType.getType() instanceof JavaType) {
            type = (JavaType) annotatedType.getType();
        } else {
            type = _mapper.constructType(annotatedType.getType());
        }

        final Annotation resolvedSchemaOrArrayAnnotation = AnnotationsUtils.mergeSchemaAnnotations(annotatedType.getCtxAnnotations(), type);
        final io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                resolvedSchemaOrArrayAnnotation == null ?
                        null :
                        resolvedSchemaOrArrayAnnotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema ?
                                ((io.swagger.v3.oas.annotations.media.ArraySchema) resolvedSchemaOrArrayAnnotation).schema() :
                                (io.swagger.v3.oas.annotations.media.Schema) resolvedSchemaOrArrayAnnotation;

        final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(type);
        Annotated a = beanDesc.getClassInfo();
        Annotation[] annotations = annotatedType.getCtxAnnotations();
        resolveSchemaMembers(schema, a, annotations, schemaAnnotation);
    }

    protected void resolveSchemaMembers(Schema schema, Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation) {

        String description = resolveDescription(a, annotations, schemaAnnotation);
        if (StringUtils.isNotBlank(description)) {
            schema.description(description);
        }
        String title = resolveTitle(a, annotations, schemaAnnotation);
        if (StringUtils.isNotBlank(title)) {
            schema.title(title);
        }
        String format = resolveFormat(a, annotations, schemaAnnotation);
        if (StringUtils.isNotBlank(format) && StringUtils.isBlank(schema.getFormat())) {
            schema.format(format);
        }
        String defaultValue = resolveDefaultValue(a, annotations, schemaAnnotation);
        if (StringUtils.isNotBlank(defaultValue)) {
            schema.setDefault(defaultValue);
        }
        Object example = resolveExample(a, annotations, schemaAnnotation);
        if (example != null) {
            schema.example(example);
        }
        Boolean readOnly = resolveReadOnly(a, annotations, schemaAnnotation);
        if (readOnly != null) {
            schema.readOnly(readOnly);
        }
        Boolean nullable = resolveNullable(a, annotations, schemaAnnotation);
        if (nullable != null) {
            schema.nullable(nullable);
        }
        BigDecimal multipleOf = resolveMultipleOf(a, annotations, schemaAnnotation);
        if (multipleOf != null) {
            schema.multipleOf(multipleOf);
        }
        Integer maxLength = resolveMaxLength(a, annotations, schemaAnnotation);
        if (maxLength != null) {
            schema.maxLength(maxLength);
        }
        Integer minLength = resolveMinLength(a, annotations, schemaAnnotation);
        if (minLength != null) {
            schema.minLength(minLength);
        }
        BigDecimal minimum = resolveMinimum(a, annotations, schemaAnnotation);
        if (minimum != null) {
            schema.minimum(minimum);
        }
        BigDecimal maximum = resolveMaximum(a, annotations, schemaAnnotation);
        if (maximum != null) {
            schema.maximum(maximum);
        }
        Boolean exclusiveMinimum = resolveExclusiveMinimum(a, annotations, schemaAnnotation);
        if (exclusiveMinimum != null) {
            schema.exclusiveMinimum(exclusiveMinimum);
        }
        Boolean exclusiveMaximum = resolveExclusiveMaximum(a, annotations, schemaAnnotation);
        if (exclusiveMaximum != null) {
            schema.exclusiveMaximum(exclusiveMaximum);
        }
        String pattern = resolvePattern(a, annotations, schemaAnnotation);
        if (StringUtils.isNotBlank(pattern)) {
            schema.pattern(pattern);
        }
        Integer minProperties = resolveMinProperties(a, annotations, schemaAnnotation);
        if (minProperties != null) {
            schema.minProperties(minProperties);
        }
        Integer maxProperties = resolveMaxProperties(a, annotations, schemaAnnotation);
        if (maxProperties != null) {
            schema.maxProperties(maxProperties);
        }
        List<String> requiredProperties = resolveRequiredProperties(a, annotations, schemaAnnotation);
        if (requiredProperties != null) {
            for (String prop : requiredProperties) {
                addRequiredItem(schema, prop);
            }
        }
        Boolean writeOnly = resolveWriteOnly(a, annotations, schemaAnnotation);
        if (writeOnly != null) {
            schema.writeOnly(writeOnly);
        }
        ExternalDocumentation externalDocs = resolveExternalDocumentation(a, annotations, schemaAnnotation);
        if (externalDocs != null) {
            schema.externalDocs(externalDocs);
        }
        Boolean deprecated = resolveDeprecated(a, annotations, schemaAnnotation);
        if (deprecated != null) {
            schema.deprecated(deprecated);
        }
        List<String> allowableValues = resolveAllowableValues(a, annotations, schemaAnnotation);
        if (allowableValues != null) {
            for (String prop : allowableValues) {
                schema.addEnumItemObject(prop);
            }
        }

        Map<String, Object> extensions = resolveExtensions(a, annotations, schemaAnnotation);
        if (extensions != null) {
            for (String ext : extensions.keySet()) {
                schema.addExtension(ext, extensions.get(ext));
            }
        }
    }

    protected void addRequiredItem(Schema model, String propName) {
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

    private List<String> getIgnoredProperties(BeanDescription beanDescription) {
        AnnotationIntrospector introspector = _mapper.getSerializationConfig().getAnnotationIntrospector();
        String[] ignored = introspector.findPropertiesToIgnore(beanDescription.getClassInfo(), true);
        return ignored == null ? Collections.emptyList() : Arrays.asList(ignored);
    }

    /**
     *  Decorate the name based on the JsonView
     */
    protected String decorateModelName(AnnotatedType type, String originalName) {
        if (StringUtils.isBlank(originalName)) {
            return originalName;
        }
        String name = originalName;
        if (type.getJsonViewAnnotation() != null && type.getJsonViewAnnotation().value().length > 0) {
            String COMBINER = "-or-";
            StringBuilder sb = new StringBuilder();
            for (Class<?> view : type.getJsonViewAnnotation().value()) {
                sb.append(view.getSimpleName()).append(COMBINER);
            }
            String suffix = sb.substring(0, sb.length() - COMBINER.length());
            name = originalName + "_" + suffix;
        }
        return name;
    }

    private boolean hiddenByJsonView(Annotation[] annotations,
                                     AnnotatedType type) {
        JsonView jsonView = type.getJsonViewAnnotation();
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

    private void resolveArraySchema(AnnotatedType annotatedType, ArraySchema schema, io.swagger.v3.oas.annotations.media.ArraySchema resolvedArrayAnnotation) {
        Integer minItems = resolveMinItems(annotatedType, resolvedArrayAnnotation);
        if (minItems != null) {
            schema.minItems(minItems);
        }
        Integer maxItems = resolveMaxItems(annotatedType, resolvedArrayAnnotation);
        if (maxItems != null) {
            schema.maxItems(maxItems);
        }
        Boolean uniqueItems = resolveUniqueItems(annotatedType, resolvedArrayAnnotation);
        if (uniqueItems != null) {
            schema.uniqueItems(uniqueItems);
        }
        Map<String, Object> extensions = resolveExtensions(annotatedType, resolvedArrayAnnotation);
        if (extensions != null) {
            schema.extensions(extensions);
        }
        if (resolvedArrayAnnotation != null) {
            if (AnnotationsUtils.hasSchemaAnnotation(resolvedArrayAnnotation.arraySchema())) {
                resolveSchemaMembers(schema, null, null, resolvedArrayAnnotation.arraySchema());
            }
        }
    }
}
