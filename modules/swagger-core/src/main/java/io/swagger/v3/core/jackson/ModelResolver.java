package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
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
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.util.Annotations;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Constants;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.core.util.ReferenceTypeUtils;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.core.util.ValidatorProcessor;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.DependentRequired;
import io.swagger.v3.oas.annotations.media.DependentSchema;
import io.swagger.v3.oas.annotations.media.DependentSchemas;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.PatternProperties;
import io.swagger.v3.oas.annotations.media.PatternProperty;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.media.XML;
import javax.validation.constraints.Email;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.core.jackson.JAXBAnnotationsHelper.JAXB_DEFAULT;
import static io.swagger.v3.core.util.RefUtils.constructRef;

public class ModelResolver extends AbstractModelConverter implements ModelConverter {

    Logger LOGGER = LoggerFactory.getLogger(ModelResolver.class);
    public static List<String> NOT_NULL_ANNOTATIONS = Arrays.asList("NotNull", "NonNull", "NotBlank", "NotEmpty");

    public static final String SET_PROPERTY_OF_COMPOSED_MODEL_AS_SIBLING = "composed-model-properties-as-sibiling";
    public static final String SET_PROPERTY_OF_ENUMS_AS_REF = "enums-as-ref";

    public static boolean composedModelPropertiesAsSibling = System.getProperty(SET_PROPERTY_OF_COMPOSED_MODEL_AS_SIBLING) != null;

    private static final int SCHEMA_COMPONENT_PREFIX = "#/components/schemas/".length();

    /**
     * Allows all enums to be resolved as a reference to a scheme added to the components section.
     */
    public static boolean enumsAsRef = System.getProperty(SET_PROPERTY_OF_ENUMS_AS_REF) != null;

    private boolean openapi31;

    private Schema.SchemaResolution schemaResolution = Schema.SchemaResolution.DEFAULT;

    protected Configuration configuration = new Configuration();

    protected ValidatorProcessor validatorProcessor;

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
        boolean implicitObject = false;
        boolean isPrimitive = false;
        Schema model = null;
        List<String> requiredProps = new ArrayList<>();

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
            if (StringUtils.isBlank(name) && (type.isEnumType() || !ReflectionUtils.isSystemType(type))) {
                name = _typeName(type, beanDesc);
            }
        }

        name = decorateModelName(annotatedType, name);

        // if we have a ref, for OAS 3.0 we don't consider anything else, while for OAS 3.1 we store the ref and add it later
        String schemaRefFromAnnotation = null;
        if (resolvedSchemaAnnotation != null &&
                StringUtils.isNotEmpty(resolvedSchemaAnnotation.ref())) {
            if (resolvedArrayAnnotation == null) {
                schemaRefFromAnnotation = resolvedSchemaAnnotation.ref();
                if (!openapi31) {
                    return new Schema().$ref(resolvedSchemaAnnotation.ref()).name(name);
                }
            } else {
                ArraySchema schema = new ArraySchema();
                if (openapi31) {
                    schema.specVersion(SpecVersion.V31);
                }
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                Schema itemsSchema = openapi31 ? new JsonSchema() : new Schema();
                return schema.items(itemsSchema.$ref(resolvedSchemaAnnotation.ref()).name(name));
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
                    .components(annotatedType.getComponents())
                    .skipOverride(true);
            if (resolvedArrayAnnotation != null) {
                ArraySchema schema = new ArraySchema();
                if (openapi31) {
                    schema.specVersion(SpecVersion.V31);
                }
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                Schema innerSchema = null;

                Schema primitive = PrimitiveType.createProperty(cls, openapi31);
                if (primitive != null) {
                    innerSchema = primitive;
                } else {
                    innerSchema = context.resolve(aType);
                    if (innerSchema != null && isObjectSchema(innerSchema) && StringUtils.isNotBlank(innerSchema.getName())) {
                        // create a reference for the items
                        if (context.getDefinedModels().containsKey(innerSchema.getName())) {
                            String ref = constructRef(innerSchema.getName());
                            innerSchema = openapi31 ? new JsonSchema() : new Schema();
                            innerSchema.$ref(ref);
                        }
                    } else if (innerSchema != null && innerSchema.get$ref() != null) {
                        String ref = StringUtils.isNotEmpty(innerSchema.get$ref()) ? innerSchema.get$ref() : innerSchema.getName();
                        innerSchema = openapi31 ? new JsonSchema() : new Schema();
                        innerSchema.$ref(ref);
                    }
                }
                schema.setItems(innerSchema);
                return schema;
            } else {
                Schema implSchema = context.resolve(aType);
                if (implSchema != null && aType.isResolveAsRef() && isObjectSchema(implSchema) && StringUtils.isNotBlank(implSchema.getName())) {
                    // create a reference for the items
                    if (context.getDefinedModels().containsKey(implSchema.getName())) {
                        String ref = constructRef(implSchema.getName());
                        implSchema = openapi31 ? new JsonSchema() : new Schema();
                        implSchema.$ref(ref);
                    }
                } else if (implSchema != null && implSchema.get$ref() != null) {
                    String ref = StringUtils.isNotEmpty(implSchema.get$ref()) ? implSchema.get$ref() : implSchema.getName();
                    implSchema = openapi31 ? new JsonSchema() : new Schema();
                    implSchema.$ref(ref);
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
                Schema primitive = openapi31 ? primitiveType.createProperty31() : primitiveType.createProperty();
                model = primitive;
                isPrimitive = true;

            }
        }

        if (model == null && type.isEnumType()) {
            @SuppressWarnings("unchecked")
            Class<Enum<?>> rawEnumClass = (Class<Enum<?>>) type.getRawClass();
            model = _createSchemaForEnum(rawEnumClass);
            isPrimitive = true;
        }
        if (model == null) {
            if (resolvedSchemaAnnotation != null && StringUtils.isEmpty(resolvedSchemaAnnotation.type())) {
                PrimitiveType primitiveType = PrimitiveType.fromTypeAndFormat(type, resolvedSchemaAnnotation.format());
                if (primitiveType != null) {
                    model = openapi31 ? primitiveType.createProperty31() : primitiveType.createProperty();
                    isPrimitive = true;
                }
            } 

            if (model == null) {
                PrimitiveType primitiveType = PrimitiveType.fromType(type);
                if (primitiveType != null) {
                    model = openapi31 ? primitiveType.createProperty31() : primitiveType.createProperty();
                    isPrimitive = true;
                }
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
                model = new GeneratorWrapper().processJsonIdentity(annotatedType, context, _mapper, jsonIdentityInfo, jsonIdentityReference);
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
            Schema schema = openapi31 ? new JsonSchema() : new Schema();
            if (schemaRefFromAnnotation != null) {
                schema.raw$ref(schemaRefFromAnnotation);
            }
            return schema;
        }

        List<Class<?>> composedSchemaReferencedClasses = getComposedSchemaReferencedClasses(type.getRawClass(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
        boolean hasCompositionKeywords = composedSchemaReferencedClasses != null;

        if (isPrimitive) {
            XML xml = resolveXml(beanDesc.getClassInfo(), annotatedType.getCtxAnnotations(), resolvedSchemaAnnotation);
            if (xml != null) {
                model.xml(xml);
            }
            if (!type.isEnumType()){
                applyBeanValidatorAnnotations(model, annotatedType.getCtxAnnotations(), null, false);
            }
            resolveSchemaMembers(model, annotatedType, context, next);
            if (resolvedArrayAnnotation != null) {
                ArraySchema schema = new ArraySchema();
                if (openapi31) {
                    schema.specVersion(SpecVersion.V31);
                }
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                schema.setItems(model);
                return schema;
            }
            if (type.isEnumType() && shouldResolveEnumAsRef(resolvedSchemaAnnotation, annotatedType.isResolveEnumAsRef())) {
                // Store off the ref and add the enum as a top-level model
                context.defineModel(name, model, annotatedType, null);
                // Return the model as a ref only property
                model = openapi31 ? new JsonSchema() : new Schema();
                model.$ref(Components.COMPONENTS_SCHEMAS_REF + name);
            }
            if (!hasCompositionKeywords) {
                if (schemaRefFromAnnotation != null && model != null) {
                    model.raw$ref(schemaRefFromAnnotation);
                }
                return model;
            }
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

        Type jsonValueType = findJsonValueType(beanDesc);

        if(jsonValueType != null) {
            AnnotatedType aType = new AnnotatedType()
                    .type(jsonValueType)
                    .parent(annotatedType.getParent())
                    .name(annotatedType.getName())
                    .schemaProperty(annotatedType.isSchemaProperty())
                    .resolveAsRef(annotatedType.isResolveAsRef())
                    .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                    .propertyName(annotatedType.getPropertyName())
                    .ctxAnnotations(annotatedType.getCtxAnnotations())
                    .components(annotatedType.getComponents())
                    .skipOverride(true);
            return context.resolve(aType);
        }

        if (type.isContainerType()) {
            // TODO currently a MapSchema or ArraySchema don't also support composed schema props (oneOf,..)
            hasCompositionKeywords = false;
            JavaType keyType = type.getKeyType();
            JavaType valueType = type.getContentType();
            String pName = null;
            if (valueType != null) {
                BeanDescription valueTypeBeanDesc = _mapper.getSerializationConfig().introspect(valueType);
                pName = _typeName(valueType, valueTypeBeanDesc);
            }
            List<Annotation> strippedCtxAnnotations = new ArrayList<>();
            if (resolvedSchemaAnnotation != null) {
                strippedCtxAnnotations.add(0, resolvedSchemaAnnotation);
            }
            if (annotatedType.getCtxAnnotations() != null) {
                strippedCtxAnnotations.addAll(Arrays.stream(
                        annotatedType.getCtxAnnotations()).filter(
                        ass -> !ass.annotationType().getName().startsWith("io.swagger") && !ass.annotationType().getName().startsWith("javax.validation.constraints")
                ).collect(Collectors.toList()));
            }

            Schema.SchemaResolution containerResolvedSchemaResolution = AnnotationsUtils.resolveSchemaResolution(this.schemaResolution, resolvedSchemaAnnotation);
            if (keyType != null && valueType != null) {
                if (ReflectionUtils.isSystemTypeNotArray(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    context.resolve(new AnnotatedType().components(annotatedType.getComponents()).type(valueType).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                    return null;
                }
                Schema addPropertiesSchema = context.resolve(
                        new AnnotatedType()
                                .type(valueType)
                                .schemaProperty(annotatedType.isSchemaProperty())
                                .ctxAnnotations(strippedCtxAnnotations.toArray(new Annotation[0]))
                                .skipSchemaName(true)
                                .resolveAsRef(annotatedType.isResolveAsRef())
                                .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                                .propertyName(annotatedType.getPropertyName())
                                .components(annotatedType.getComponents())
                                .parent(annotatedType.getParent()));
                if (addPropertiesSchema != null) {
                    if (StringUtils.isNotBlank(addPropertiesSchema.getName())) {
                        pName = addPropertiesSchema.getName();
                    }
                    if (isObjectSchema(addPropertiesSchema) && pName != null) {
                        if (context.getDefinedModels().containsKey(pName)) {
                            if (Schema.SchemaResolution.INLINE.equals(containerResolvedSchemaResolution) && applySchemaResolution()) {
                                addPropertiesSchema = context.getDefinedModels().get(pName);
                            } else {
                                // create a reference for the items
                                addPropertiesSchema = openapi31 ? new JsonSchema() : new Schema();
                                addPropertiesSchema.$ref(constructRef(pName));
                            }
                        }
                    } else if (addPropertiesSchema.get$ref() != null) {
                        String ref = StringUtils.isNotEmpty(addPropertiesSchema.get$ref()) ? addPropertiesSchema.get$ref() : addPropertiesSchema.getName();
                        addPropertiesSchema = openapi31 ? new JsonSchema() : new Schema();
                        addPropertiesSchema.$ref(ref);
                    }
                }
                Schema mapModel = new MapSchema().additionalProperties(addPropertiesSchema);
                if (openapi31) {
                    mapModel.specVersion(SpecVersion.V31);
                }
                mapModel.name(name);
                model = mapModel;
            } else if (valueType != null) {
                if (ReflectionUtils.isSystemTypeNotArray(type) && !annotatedType.isSchemaProperty() && !annotatedType.isResolveAsRef()) {
                    context.resolve(new AnnotatedType().components(annotatedType.getComponents()).type(valueType).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                    return null;
                }
                Schema items = context.resolve(new AnnotatedType()
                        .type(valueType)
                        .schemaProperty(annotatedType.isSchemaProperty())
                        .ctxAnnotations(strippedCtxAnnotations.toArray(new Annotation[0]))
                        .skipSchemaName(true)
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .propertyName(annotatedType.getPropertyName())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .components(annotatedType.getComponents())
                        .resolveEnumAsRef(annotatedType.isResolveEnumAsRef())
                        .parent(annotatedType.getParent()));

                if (items == null) {
                    return null;
                }
                if (annotatedType.isSchemaProperty() && annotatedType.getCtxAnnotations() != null && annotatedType.getCtxAnnotations().length > 0) {
                    if (!"object".equals(items.getType())) {
                        for (Annotation annotation : annotatedType.getCtxAnnotations()) {
                            if (annotation instanceof XmlElement) {
                                XmlElement xmlElement = (XmlElement) annotation;
                                if (xmlElement != null && xmlElement.name() != null && !"".equals(xmlElement.name()) && !JAXB_DEFAULT.equals(xmlElement.name())) {
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
                if (isObjectSchema(items) && pName != null) {
                    if (context.getDefinedModels().containsKey(pName)) {
                        if (Schema.SchemaResolution.INLINE.equals(containerResolvedSchemaResolution) && applySchemaResolution()) {
                            items = context.getDefinedModels().get(pName);
                        } else {
                            // create a reference for the items
                            items = openapi31 ? new JsonSchema() : new Schema();
                            items.$ref(constructRef(pName));
                        }
                    }
                } else if (items.get$ref() != null) {
                    String ref = StringUtils.isNotEmpty(items.get$ref()) ? items.get$ref() : items.getName();
                    items = openapi31 ? new JsonSchema() : new Schema();
                    items.$ref(ref);
                }

                Schema arrayModel =
                        new ArraySchema().items(items);
                if (openapi31) {
                    arrayModel.specVersion(SpecVersion.V31);
                }
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
        } else if (hasCompositionKeywords) {
            model = openapi31 ? new JsonSchema() : new ComposedSchema();
            model.name(name);
            if (
                    (openapi31 && Boolean.TRUE.equals(PrimitiveType.explicitObjectType)) ||
                    (!openapi31 && (!Boolean.FALSE.equals(PrimitiveType.explicitObjectType)))) {
                if (openapi31 && resolvedArrayAnnotation == null) {
                    model.addType("object");
                } else {
                    model.type("object");
                }
            } else {
                implicitObject = true;
            }
        } else {
            AnnotatedType aType = ReferenceTypeUtils.unwrapReference(annotatedType);
            if (aType != null) {
                model = context.resolve(aType);
                return model;
            } else {
                model = openapi31 ? new JsonSchema().name(name) : new Schema().name(name);
                if ((openapi31 && Boolean.TRUE.equals(PrimitiveType.explicitObjectType)) ||
                                (!openapi31 && (!Boolean.FALSE.equals(PrimitiveType.explicitObjectType)))) {
                    if (openapi31 && resolvedArrayAnnotation == null) {
                        model.addType("object");
                    } else {
                        model.type("object");
                    }
                } else {
                    implicitObject = true;
                }
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
            resolveSchemaMembers(model, annotatedType, context, next);
        }

        final XmlAccessorType xmlAccessorTypeAnnotation = beanDesc.getClassAnnotations().get(XmlAccessorType.class);

        // see if @JsonIgnoreProperties exist
        Set<String> propertiesToIgnore = resolveIgnoredProperties(beanDesc.getClassAnnotations(), annotatedType.getCtxAnnotations());

        List<Schema> props = new ArrayList<>();
        Map<String, Schema> modelProps = new LinkedHashMap<>();

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
            if (propDef.getPrimaryMember() != null) {
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

            if (member != null && !ignore(member, xmlAccessorTypeAnnotation, propName, propertiesToIgnore, propDef)) {

                List<Annotation> annotationList = new ArrayList<>();
                for (Annotation a : member.annotations()) {
                    annotationList.add(a);
                }

                annotations = annotationList.toArray(new Annotation[annotationList.size()]);

                if (hiddenByJsonView(annotations, annotatedType)) {
                    continue;
                }

                JavaType propType = member.getType();
                if (propType != null && "void".equals(propType.getRawClass().getName())) {
                    if (member instanceof AnnotatedMethod) {
                        propType = ((AnnotatedMethod) member).getParameterType(0);
                    }

                }

                String propSchemaName = null;
                io.swagger.v3.oas.annotations.media.Schema ctxSchema = AnnotationsUtils.getSchemaAnnotation(annotations);
                if (AnnotationsUtils.hasSchemaAnnotation(ctxSchema)) {
                    if (!StringUtils.isBlank(ctxSchema.name())) {
                        propSchemaName = ctxSchema.name();
                    }
                }
                io.swagger.v3.oas.annotations.media.ArraySchema ctxArraySchema = AnnotationsUtils.getArraySchemaAnnotation(annotations);
                if (propSchemaName == null) {
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
                io.swagger.v3.oas.annotations.media.Schema.RequiredMode requiredMode = resolveRequiredMode(propResolvedSchemaAnnotation);

                Annotation[] ctxAnnotation31 = null;
                Schema.SchemaResolution resolvedSchemaResolution = AnnotationsUtils.resolveSchemaResolution(this.schemaResolution, ctxSchema);
                if (AnnotationsUtils.areSiblingsAllowed(resolvedSchemaResolution, openapi31)) {
                    List<Annotation> ctxAnnotations31List = new ArrayList<>();
                    if (annotations != null) {
                        for (Annotation a : annotations) {
                            if (
                                    !(a instanceof io.swagger.v3.oas.annotations.media.Schema) &&
                                            !(a instanceof io.swagger.v3.oas.annotations.media.ArraySchema)) {
                                ctxAnnotations31List.add(a);
                            }
                        }
                        ctxAnnotation31 = ctxAnnotations31List.toArray(new Annotation[ctxAnnotations31List.size()]);
                    }
                }
                Set<Annotation> validationInvocationAnnotations = null;
                if (validatorProcessor != null) {
                    validationInvocationAnnotations = validatorProcessor.resolveInvocationAnnotations(annotations);
                    if (validationInvocationAnnotations == null) {
                        validationInvocationAnnotations = validatorProcessor.resolveInvocationAnnotations(annotatedType.getCtxAnnotations());
                    } else {
                        validationInvocationAnnotations.addAll(validatorProcessor.resolveInvocationAnnotations(annotatedType.getCtxAnnotations()));
                    }
                }
                if (validationInvocationAnnotations == null) {
                    validationInvocationAnnotations = resolveValidationInvocationAnnotations(annotations);
                    validationInvocationAnnotations.addAll(resolveValidationInvocationAnnotations(annotatedType.getCtxAnnotations()));
                }
                annotations = Stream.concat(Arrays.stream(annotations), Arrays.stream(validationInvocationAnnotations.toArray(new Annotation[0]))).toArray(Annotation[]::new);
                if (ctxAnnotation31 != null) {
                    ctxAnnotation31 = Stream.concat(Arrays.stream(ctxAnnotation31), Arrays.stream(validationInvocationAnnotations.toArray(new Annotation[0]))).toArray(Annotation[]::new);
                }
                AnnotatedType aType = new AnnotatedType()
                        .type(propType)
                        .parent(model)
                        .resolveAsRef(annotatedType.isResolveAsRef())
                        .jsonViewAnnotation(annotatedType.getJsonViewAnnotation())
                        .skipSchemaName(true)
                        .subtype(annotatedType.isSubtype())
                        .schemaProperty(true)
                        .components(annotatedType.getComponents())
                        .propertyName(propName)
                        .resolveEnumAsRef(AnnotationsUtils.computeEnumAsRef(ctxSchema, ctxArraySchema));
                if (
                        Schema.SchemaResolution.ALL_OF.equals(resolvedSchemaResolution) ||
                                Schema.SchemaResolution.ALL_OF_REF.equals(resolvedSchemaResolution) ||
                                openapi31) {
                    aType.ctxAnnotations(ctxAnnotation31);
                } else {
                    aType.ctxAnnotations(annotations);
                }
                final AnnotatedMember propMember = member;
                aType.jsonUnwrappedHandler(t -> {
                    JsonUnwrapped uw = propMember.getAnnotation(JsonUnwrapped.class);
                    if (uw != null && uw.enabled()) {
                        t
                                .ctxAnnotations(null)
                                .jsonUnwrappedHandler(null)
                                .resolveAsRef(false);
                        Schema innerModel = context.resolve(t);
                        if (StringUtils.isNotBlank(innerModel.get$ref())) {
                            innerModel = context.getDefinedModels().get(innerModel.get$ref().substring(SCHEMA_COMPONENT_PREFIX));
                        }
                        handleUnwrapped(props, innerModel, uw.prefix(), uw.suffix(), requiredProps);
                        return null;
                    } else {
                        return openapi31 ? new JsonSchema() : new Schema();
                    }
                });

                boolean areSiblingsAllowed = AnnotationsUtils.areSiblingsAllowed(resolvedSchemaResolution, openapi31);
                aType = AnnotationsUtils.addTypeWhenSiblingsAllowed(aType, ctxSchema, areSiblingsAllowed);
                property = context.resolve(aType);
                property = clone(property);
                Schema ctxProperty = null;
                if (!applySchemaResolution()) {
                    Optional<Schema> reResolvedProperty = AnnotationsUtils.getSchemaFromAnnotation(ctxSchema, annotatedType.getComponents(), null, openapi31, property, schemaResolution, context);
                    if (reResolvedProperty.isPresent()) {
                        property = reResolvedProperty.get();
                    }
                    reResolvedProperty = AnnotationsUtils.getArraySchema(ctxArraySchema, annotatedType.getComponents(), null, openapi31, property, true);
                    if (reResolvedProperty.isPresent()) {
                        property = reResolvedProperty.get();
                    }

                } else if (Schema.SchemaResolution.ALL_OF.equals(resolvedSchemaResolution) || Schema.SchemaResolution.ALL_OF_REF.equals(resolvedSchemaResolution)) {
                    Optional<Schema> reResolvedProperty = AnnotationsUtils.getSchemaFromAnnotation(ctxSchema, annotatedType.getComponents(), null, openapi31, null, schemaResolution, context);
                    if (reResolvedProperty.isPresent()) {
                        ctxProperty = reResolvedProperty.get();
                    }
                    reResolvedProperty = AnnotationsUtils.getArraySchema(ctxArraySchema, annotatedType.getComponents(), null, openapi31, ctxProperty);
                    if (reResolvedProperty.isPresent()) {
                        ctxProperty = reResolvedProperty.get();
                    }

                }
                if (property != null) {
                    Boolean required = md.getRequired();
                    if (!io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED.equals(requiredMode)) {
                        if (required != null && !Boolean.FALSE.equals(required)) {
                            addRequiredItem(model, propName);
                        } else {
                            if (propDef.isRequired()) {
                                addRequiredItem(model, propName);
                            }
                        }
                    }
                    if (property.get$ref() == null || openapi31) {
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
                        if (isObjectSchema(property)) {
                            // create a reference for the property
                            String pName = _typeName(propType, propBeanDesc);
                            if (StringUtils.isNotBlank(property.getName())) {
                                pName = property.getName();
                            }

                            if (context.getDefinedModels().containsKey(pName)) {
                                if (Schema.SchemaResolution.INLINE.equals(resolvedSchemaResolution)) {
                                    property = context.getDefinedModels().get(pName);
                                } else if (Schema.SchemaResolution.ALL_OF.equals(resolvedSchemaResolution) && ctxProperty != null) {
                                    property = openapi31 ? new JsonSchema() : new Schema();
                                    property
                                            .addAllOfItem(ctxProperty)
                                            .addAllOfItem(openapi31 ? new JsonSchema().$ref(constructRef(pName)) : new Schema().$ref(constructRef(pName)));
                                } else if (Schema.SchemaResolution.ALL_OF_REF.equals(resolvedSchemaResolution) && ctxProperty != null) {
                                    property = ctxProperty.addAllOfItem(openapi31 ? new JsonSchema().$ref(constructRef(pName)) : new Schema().$ref(constructRef(pName)));
                                } else {
                                    property = openapi31 ? new JsonSchema() : new Schema();
                                    property.$ref(constructRef(pName));
                                }
                                property = clone(property);
                                // TODO: why is this needed? is it not handled before?
                                if (openapi31 || Schema.SchemaResolution.INLINE.equals(resolvedSchemaResolution)) {
                                    Optional<Schema> reResolvedProperty = AnnotationsUtils.getSchemaFromAnnotation(ctxSchema, annotatedType.getComponents(), null, openapi31, property, this.schemaResolution, context);
                                    if (reResolvedProperty.isPresent()) {
                                        property = reResolvedProperty.get();
                                    }
                                    reResolvedProperty = AnnotationsUtils.getArraySchema(ctxArraySchema, annotatedType.getComponents(), null, openapi31, property);
                                    if (reResolvedProperty.isPresent()) {
                                        property = reResolvedProperty.get();
                                    }
                                }
                            }
                        } else if (property.get$ref() != null) {
                            if (applySchemaResolution()) {
                                if (Schema.SchemaResolution.ALL_OF.equals(resolvedSchemaResolution) && ctxProperty != null) {
                                    property = new Schema()
                                            .addAllOfItem(ctxProperty)
                                            .addAllOfItem(new Schema().$ref(StringUtils.isNotEmpty(property.get$ref()) ? property.get$ref() : property.getName()));
                                } else if (Schema.SchemaResolution.ALL_OF_REF.equals(resolvedSchemaResolution) && ctxProperty != null) {
                                    property = ctxProperty
                                            .addAllOfItem(new Schema().$ref(StringUtils.isNotEmpty(property.get$ref()) ? property.get$ref() : property.getName()));
                                } else {
                                    property = new Schema().$ref(StringUtils.isNotEmpty(property.get$ref()) ? property.get$ref() : property.getName());
                                }
                            } else {
                                if (StringUtils.isEmpty(property.get$ref())) {
                                    property.$ref(property.getName());
                                }
                            }
                        }
                    }
                    property.setName(propName);
                    JAXBAnnotationsHelper.apply(propBeanDesc.getClassInfo(), annotations, property);
                    if (property != null && io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED.equals(requiredMode)) {
                        addRequiredItem(model, property.getName());
                    }
                    if (ctxProperty == null) {
                        ctxProperty = property;
                    }
                    final boolean applyNotNullAnnotations = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.AUTO.equals(requiredMode);
                    annotations = addGenericTypeArgumentAnnotationsForOptionalField(propDef, annotations);
                    applyBeanValidatorAnnotations(propDef, ctxProperty, annotations, model, applyNotNullAnnotations);

                    props.add(property);
                }
            }
        }
        for (Schema prop : props) {
            modelProps.put(prop.getName(), prop);
        }
        if (modelProps.size() > 0) {
            if (model.getProperties() == null) {
                model.setProperties(modelProps);
            } else {
                for (String key : modelProps.keySet()) {
                    model.addProperty(key, modelProps.get(key));
                }
            }

            for (String propName : requiredProps) {
                addRequiredItem(model, propName);
            }
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
        if (!resolveSubtypes(model, beanDesc, context, annotatedType.getJsonViewAnnotation())) {
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
                model.not((new Schema().$ref(context.resolve(new AnnotatedType().components(annotatedType.getComponents()).type(not).jsonViewAnnotation(annotatedType.getJsonViewAnnotation())).getName())));
            }
            if (resolvedSchemaAnnotation.requiredProperties() != null &&
                    resolvedSchemaAnnotation.requiredProperties().length > 0 &&
                    StringUtils.isNotBlank(resolvedSchemaAnnotation.requiredProperties()[0])) {
                for (String prop : resolvedSchemaAnnotation.requiredProperties()) {
                    addRequiredItem(model, prop);
                }
            }
        }

        Map<String, Schema> patternProperties = resolvePatternProperties(type, annotatedType.getCtxAnnotations(), context);
        if (model != null && patternProperties != null && !patternProperties.isEmpty()) {
            if (model.getPatternProperties() == null) {
                model.patternProperties(patternProperties);
            } else {
                model.getPatternProperties().putAll(patternProperties);
            }
        }

        Map<String, Schema> schemaProperties = resolveSchemaProperties(type, annotatedType.getCtxAnnotations(), context);
        if (model != null && schemaProperties != null && !schemaProperties.isEmpty()) {
            if (model.getProperties() == null) {
                model.properties(schemaProperties);
            } else {
                model.getProperties().putAll(schemaProperties);
            }
        }

        if (openapi31) {
            Map<String, Schema> dependentSchemas = resolveDependentSchemas(type, annotatedType.getCtxAnnotations(), context, annotatedType.getComponents(), annotatedType.getJsonViewAnnotation(), openapi31);
            if (model != null && dependentSchemas != null && !dependentSchemas.isEmpty()) {
                if (model.getDependentSchemas() == null) {
                    model.dependentSchemas(dependentSchemas);
                } else {
                    model.getDependentSchemas().putAll(dependentSchemas);
                }
            }
        }

        if (hasCompositionKeywords) {

            Schema schemaWithCompositionKeys = model;

            Class<?>[] allOf = resolvedSchemaAnnotation.allOf();
            Class<?>[] anyOf = resolvedSchemaAnnotation.anyOf();
            Class<?>[] oneOf = resolvedSchemaAnnotation.oneOf();

            List<Class<?>> allOfFiltered = Stream.of(allOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            allOfFiltered.forEach(c -> {
                Schema allOfRef = context.resolve(new AnnotatedType().components(annotatedType.getComponents()).type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                Schema refSchema = new Schema().$ref(Components.COMPONENTS_SCHEMAS_REF + allOfRef.getName());
                if (StringUtils.isBlank(allOfRef.getName())) {
                    refSchema = allOfRef;
                }
                // allOf could have already being added during subtype resolving
                if (schemaWithCompositionKeys.getAllOf() == null || !schemaWithCompositionKeys.getAllOf().contains(refSchema)) {
                    schemaWithCompositionKeys.addAllOfItem(refSchema);
                }
                // remove shared properties defined in the parent
                if (isSubtype(beanDesc.getClassInfo(), c)) {
                    removeParentProperties(schemaWithCompositionKeys, allOfRef);
                }
            });

            List<Class<?>> anyOfFiltered = Stream.of(anyOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            anyOfFiltered.forEach(c -> {
                Schema anyOfRef = context.resolve(new AnnotatedType().components(annotatedType.getComponents()).type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                if (anyOfRef != null) {
                    if (StringUtils.isNotBlank(anyOfRef.getName())) {
                        schemaWithCompositionKeys.addAnyOfItem(new Schema().$ref(Components.COMPONENTS_SCHEMAS_REF + anyOfRef.getName()));
                    } else {
                        schemaWithCompositionKeys.addAnyOfItem(anyOfRef);
                    }
                }
                // remove shared properties defined in the parent
                if (isSubtype(beanDesc.getClassInfo(), c)) {
                    removeParentProperties(schemaWithCompositionKeys, anyOfRef);
                }

            });

            List<Class<?>> oneOfFiltered = Stream.of(oneOf)
                    .distinct()
                    .filter(c -> !this.shouldIgnoreClass(c))
                    .filter(c -> !(c.equals(Void.class)))
                    .collect(Collectors.toList());
            oneOfFiltered.forEach(c -> {
                Schema oneOfRef = context.resolve(new AnnotatedType().components(annotatedType.getComponents()).type(c).jsonViewAnnotation(annotatedType.getJsonViewAnnotation()));
                if (oneOfRef != null) {
                    if (StringUtils.isBlank(oneOfRef.getName())) {
                        schemaWithCompositionKeys.addOneOfItem(oneOfRef);
                    } else {
                        schemaWithCompositionKeys.addOneOfItem(new Schema().$ref(Components.COMPONENTS_SCHEMAS_REF + oneOfRef.getName()));
                    }
                    // remove shared properties defined in the parent
                    if (isSubtype(beanDesc.getClassInfo(), c)) {
                        removeParentProperties(schemaWithCompositionKeys, oneOfRef);
                    }
                }

                dropRootRefIfComposed(schemaWithCompositionKeys);
            });

            if (!composedModelPropertiesAsSibling) {
                if (schemaWithCompositionKeys.getAllOf() != null && !schemaWithCompositionKeys.getAllOf().isEmpty()) {
                    if (schemaWithCompositionKeys.getProperties() != null && !schemaWithCompositionKeys.getProperties().isEmpty()) {
                        Schema propSchema = openapi31 ? new JsonSchema().typesItem("object") : new ObjectSchema();
                        propSchema.properties(schemaWithCompositionKeys.getProperties());
                        schemaWithCompositionKeys.setProperties(null);
                        schemaWithCompositionKeys.addAllOfItem(propSchema);
                    }
                }
            }
        }

        if (!type.isContainerType() && StringUtils.isNotBlank(name)) {
            // define the model here to support self/cyclic referencing of models
            context.defineModel(name, model, annotatedType, null);
        }
        // check if it has "object" related keywords
        if (isInferredObjectSchema(model) && model.get$ref() == null) {
          if (openapi31 && model.getTypes() == null) {
            model.addType("object");
          } else if (!openapi31 && model.getType() == null){
            model.type("object");
          }
        }
        Schema.SchemaResolution resolvedSchemaResolution = AnnotationsUtils.resolveSchemaResolution(this.schemaResolution, resolvedSchemaAnnotation);

        if (model != null && annotatedType.isResolveAsRef() &&
                (hasCompositionKeywords || isObjectSchema(model) || implicitObject) &&
                StringUtils.isNotBlank(model.getName())) {
            if (context.getDefinedModels().containsKey(model.getName())) {
                if (!Schema.SchemaResolution.INLINE.equals(resolvedSchemaResolution)) {
                    model = new Schema().$ref(constructRef(model.getName()));
                }
            }
        } else if (model != null && model.get$ref() != null) {
            model = new Schema().$ref(StringUtils.isNotEmpty(model.get$ref()) ? model.get$ref() : model.getName());
        }

        if (model != null && resolvedArrayAnnotation != null) {
            if (!"array".equals(model.getType())) {
                ArraySchema schema = new ArraySchema();
                if (openapi31) {
                    schema.specVersion(SpecVersion.V31);
                }
                schema.setItems(model);
                resolveArraySchema(annotatedType, schema, resolvedArrayAnnotation);
                return schema;
            } else {
                if (isArraySchema(model)) {
                    resolveArraySchema(annotatedType, (ArraySchema) model, resolvedArrayAnnotation);
                }
            }
        }

        resolveDiscriminatorProperty(type, context, model);
        model = resolveWrapping(type, context, model);

        return model;
    }

    private Annotation[] addGenericTypeArgumentAnnotationsForOptionalField(BeanPropertyDefinition propDef, Annotation[] annotations) {

        boolean isNotOptionalType = Optional.ofNullable(propDef)
                .map(BeanPropertyDefinition::getField)
                .map(AnnotatedField::getAnnotated)
                .map(field -> !(field.getType().equals(Optional.class)))
                .orElse(false);

        if (isNotOptionalType || isRecordType(propDef)) {
            return annotations;
        }

        Stream<Annotation> genericTypeArgumentAnnotations = extractGenericTypeArgumentAnnotations(propDef);
        return Stream.concat(Stream.of(annotations), genericTypeArgumentAnnotations).toArray(Annotation[]::new);
    }

    private Stream<Annotation> extractGenericTypeArgumentAnnotations(BeanPropertyDefinition propDef) {
        if (isRecordType(propDef)) {
            return getRecordComponentAnnotations(propDef);
        }
        return Optional.ofNullable(propDef)
                .map(BeanPropertyDefinition::getField)
                .map(AnnotatedField::getAnnotated)
                .map(this::getGenericTypeArgumentAnnotations)
                .orElseGet(Stream::of);
    }

    private Stream<Annotation> getRecordComponentAnnotations(BeanPropertyDefinition propDef) {
        try {
            Method accessor = propDef.getPrimaryMember().getDeclaringClass().getDeclaredMethod(propDef.getPrimaryMember().getName());
            return getGenericTypeArgumentAnnotations(accessor.getAnnotatedReturnType());
        } catch (NoSuchMethodException e) {
            LOGGER.error("Accessor for record component not found");
            return Stream.empty();
        }
    }

    private void dropRootRefIfComposed(Schema<?> s) {
        if (s == null || s.get$ref() == null) {
            return;
        }

        if (!isComposedSchema(s)) {
            return;
        }

        String ref = s.get$ref();
        if (refMatchesAnyComposedItem(s, ref)) {
            s.set$ref(null);
        }
    }

    private boolean isComposedSchema(Schema<?> s) {
        return (s.getOneOf() != null && !s.getOneOf().isEmpty())
                || (s.getAnyOf() != null && !s.getAnyOf().isEmpty())
                || (s.getAllOf() != null && !s.getAllOf().isEmpty());
    }

    private boolean refMatchesAnyComposedItem(Schema<?> s, String ref) {
        return refMatchesInList(s.getOneOf(), ref)
                || refMatchesInList(s.getAllOf(), ref)
                || refMatchesInList(s.getAnyOf(), ref);
    }

    private boolean refMatchesInList(List<Schema> schemas, String ref) {
        return schemas != null && schemas.stream()
                .anyMatch(schema -> ref.equals(schema.get$ref()));
    }

    private Boolean isRecordType(BeanPropertyDefinition propDef) {
        try {
            if (propDef.getPrimaryMember() != null) {
                Class<?> clazz = propDef.getPrimaryMember().getDeclaringClass();
                Method isRecordMethod = Class.class.getMethod("isRecord");
                return (Boolean) isRecordMethod.invoke(clazz);
            } else {
                return false;
            }
        } catch (NoSuchMethodException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private Stream<Annotation> getGenericTypeArgumentAnnotations(Field field) {
        return getGenericTypeArgumentAnnotations(field.getAnnotatedType());
    }

    private Stream<Annotation> getGenericTypeArgumentAnnotations(java.lang.reflect.AnnotatedType annotatedType) {
        return Optional.of(annotatedType)
                .filter(type -> type instanceof AnnotatedParameterizedType)
                .map(type -> (AnnotatedParameterizedType) type)
                .map(AnnotatedParameterizedType::getAnnotatedActualTypeArguments)
                .map(types -> Stream.of(types)
                        .flatMap(type -> Stream.of(type.getAnnotations())))
                .orElseGet(Stream::of);
    }

    private boolean shouldResolveEnumAsRef(io.swagger.v3.oas.annotations.media.Schema resolvedSchemaAnnotation, boolean isResolveEnumAsRef) {
        return (resolvedSchemaAnnotation != null && resolvedSchemaAnnotation.enumAsRef()) || ModelResolver.enumsAsRef || isResolveEnumAsRef;
    }

    protected Type findJsonValueType(final BeanDescription beanDesc) {

        // use recursion to check for method findJsonValueAccessor existence (Jackson 2.9+)
        // if not found use previous deprecated method which could lead to inaccurate result
        try {
            AnnotatedMember jsonValueMember = invokeMethod(beanDesc, "findJsonValueAccessor");
            if (jsonValueMember != null) {
                return jsonValueMember.getType();
            }
            return null;
        } catch (Exception e) {
            LOGGER.warn("jackson BeanDescription.findJsonValueAccessor not found, this could lead to inaccurate result, please update jackson to 2.9+");
        }

        try {
            AnnotatedMember jsonValueMember = invokeMethod(beanDesc, "findJsonValueMethod");
            if (jsonValueMember != null) {
                return jsonValueMember.getType();
            }
        } catch (Exception e) {
            LOGGER.error("Neither 'findJsonValueMethod' nor 'findJsonValueAccessor' found in jackson BeanDescription. Please verify your Jackson version.");
        }
        return null;
    }

    private Schema clone(Schema property) {
        return AnnotationsUtils.clone(property, openapi31);
    }

    private boolean isSubtype(AnnotatedClass childClass, Class<?> parentClass) {
        final BeanDescription parentDesc = _mapper.getSerializationConfig().introspectClassAnnotations(parentClass);
        List<NamedType> subTypes = _intr().findSubtypes(parentDesc.getClassInfo());
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

    /**
     * @deprecated use '_createSchemaForEnum'
     */
    protected void _addEnumProps(Class<?> propClass, Schema property) {
        if (propClass.isEnum()) {
            Class<Enum<?>> rawEnumClass = (Class<Enum<?>>) propClass;
            Schema enumSchema = _createSchemaForEnum(rawEnumClass);
            if (enumSchema != null) {
                property.setEnum(enumSchema.getEnum());
                property.setType(enumSchema.getType());
                property.setFormat(enumSchema.getFormat());
                property.setName(enumSchema.getName());
                property.setDescription(enumSchema.getDescription());
            }
        }
    }

    /**
     * Adds each enum property value to the model schema
     *
     * @param enumClass the enum class for which to add properties
     */
    protected Schema _createSchemaForEnum(Class<Enum<?>> enumClass) {
        boolean useIndex = _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        boolean useToString = _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        Optional<Method> jsonValueMethod = Arrays.stream(enumClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(JsonValue.class))
                .filter(m -> m.getAnnotation(JsonValue.class).value())
                .findFirst();

        Optional<Field> jsonValueField = Arrays.stream(enumClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(JsonValue.class))
                .filter(f -> f.getAnnotation(JsonValue.class).value())
                .findFirst();

        Schema schema = null;
        if (jsonValueField.isPresent()) {
            jsonValueField.get().setAccessible(true);
            PrimitiveType primitiveType = PrimitiveType.fromType(jsonValueField.get().getType());
            if (primitiveType != null) {
                schema = openapi31 ? primitiveType.createProperty31() : primitiveType.createProperty();
            }
        } else if (jsonValueMethod.isPresent()) {
            jsonValueMethod.get().setAccessible(true);
            PrimitiveType primitiveType = PrimitiveType.fromType(jsonValueMethod.get().getReturnType());
            if (primitiveType != null) {
                schema = openapi31 ? primitiveType.createProperty31() : primitiveType.createProperty();
            }
        }
        if (schema == null) {
            schema = openapi31 ? new JsonSchema().typesItem("string") : new StringSchema();
        }

        Enum<?>[] enumConstants = enumClass.getEnumConstants();

        if (enumConstants != null) {
            String[] enumValues = _intr().findEnumValues(enumClass, enumConstants,
                    new String[enumConstants.length]);

            for (Enum<?> en : enumConstants) {
                Field enumField = ReflectionUtils.findField(en.name(), enumClass);
                if (null != enumField && enumField.isAnnotationPresent(Hidden.class)) {
                    continue;
                }

                String enumValue = enumValues[en.ordinal()];
                Object methodValue = jsonValueMethod.flatMap(m -> ReflectionUtils.safeInvoke(m, en)).orElse(null);
                Object fieldValue = jsonValueField.flatMap(f -> ReflectionUtils.safeGet(f, en)).orElse(null);

                Object n;
                if (methodValue != null) {
                    n = methodValue;
                } else if (fieldValue != null) {
                    n = fieldValue;
                } else if (enumValue != null) {
                    n = enumValue;
                } else if (useIndex) {
                    n = String.valueOf(en.ordinal());
                } else if (useToString) {
                    n = en.toString();
                } else {
                    n = _intr().findEnumValue(en);
                }
                schema.addEnumItemObject(n);
            }
        }
        return schema;
    }

    protected boolean ignore(final Annotated member, final XmlAccessorType xmlAccessorTypeAnnotation, final String propName, final Set<String> propertiesToIgnore) {
        return ignore(member, xmlAccessorTypeAnnotation, propName, propertiesToIgnore, null);
    }

    protected boolean hasHiddenAnnotation(Annotated annotated) {
        return annotated.hasAnnotation(Hidden.class) || (
                annotated.hasAnnotation(io.swagger.v3.oas.annotations.media.Schema.class) &&
                        annotated.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class).hidden()
        );
    }

    protected boolean ignore(final Annotated member, final XmlAccessorType xmlAccessorTypeAnnotation, final String propName, final Set<String> propertiesToIgnore, BeanPropertyDefinition propDef) {
        if (propertiesToIgnore.contains(propName)) {
            return true;
        }
        if (member.hasAnnotation(JsonIgnore.class) && member.getAnnotation(JsonIgnore.class).value()) {
            return true;
        }
        if (hasHiddenAnnotation(member)) {
            return true;
        }

        if (propDef != null) {
            if (propDef.hasGetter() && hasHiddenAnnotation(propDef.getGetter())) {
                return true;
            }
            if (propDef.hasSetter() && hasHiddenAnnotation(propDef.getSetter())) {
                return true;
            }
            if (propDef.hasConstructorParameter() && hasHiddenAnnotation(propDef.getConstructorParameter())) {
                return true;
            }
            if (propDef.hasField() && hasHiddenAnnotation(propDef.getField())) {
                return true;
            }
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

    private void handleUnwrapped(List<Schema> props, Schema innerModel, String prefix, String suffix, List<String> requiredProps) {
        if (StringUtils.isBlank(suffix) && StringUtils.isBlank(prefix)) {
            if (innerModel.getProperties() != null) {
                props.addAll(innerModel.getProperties().values());
                if (innerModel.getRequired() != null) {
                    requiredProps.addAll(innerModel.getRequired());
                }

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

    public Schema.SchemaResolution getSchemaResolution() {
        return schemaResolution;
    }

    public void setSchemaResolution(Schema.SchemaResolution schemaResolution) {
        this.schemaResolution = schemaResolution;
    }

    public ModelResolver schemaResolution(Schema.SchemaResolution schemaResolution) {
        setSchemaResolution(schemaResolution);
        return this;
    }

    private class GeneratorWrapper {

        private final List<Base> wrappers = new ArrayList();

        private final class PropertyGeneratorWrapper extends GeneratorWrapper.Base<ObjectIdGenerators.PropertyGenerator> {

            public PropertyGeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
                super(generator);
            }

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
                    javaType = (JavaType) type.getType();
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
                            return PrimitiveType.createProperty(propType, openapi31);
                        } else {
                            List<Annotation> list = new ArrayList<>();
                            for (Annotation a : propMember.annotations()) {
                                list.add(a);
                            }
                            Annotation[] annotations = list.toArray(new Annotation[list.size()]);
                            AnnotatedType aType = new AnnotatedType()
                                    .type(propType)
                                    .ctxAnnotations(annotations)
                                    .jsonViewAnnotation(type.getJsonViewAnnotation())
                                    .schemaProperty(true)
                                    .components(type.getComponents())
                                    .subtype(type.isSubtype())
                                    .propertyName(type.getPropertyName());

                            return context.resolve(aType);
                        }
                    }
                }
                return null;
            }
        }

        private final class IntGeneratorWrapper extends GeneratorWrapper.Base<ObjectIdGenerators.IntSequenceGenerator> {

            public IntGeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
                super(generator);
            }

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

        }

        private final class UUIDGeneratorWrapper extends GeneratorWrapper.Base<ObjectIdGenerators.UUIDGenerator> {

            public UUIDGeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
                super(generator);
            }

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

        }

        private final class NoneGeneratorWrapper extends GeneratorWrapper.Base<ObjectIdGenerators.None> {

            public NoneGeneratorWrapper(Class<? extends ObjectIdGenerator> generator) {
                super(generator);
            }

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
        }

        private abstract class Base<T> {

            private final Class<? extends ObjectIdGenerator> generator;

            Base(Class<? extends ObjectIdGenerator> generator) {
                this.generator = generator;
            }

            protected abstract Schema processAsProperty(String propertyName, AnnotatedType type,
                                                        ModelConverterContext context, ObjectMapper mapper);

            protected abstract Schema processAsId(String propertyName, AnnotatedType type,
                                                  ModelConverterContext context, ObjectMapper mapper);
        }

        public Schema processJsonIdentity(AnnotatedType type, ModelConverterContext context,
                                          ObjectMapper mapper, JsonIdentityInfo identityInfo,
                                          JsonIdentityReference identityReference) {
            final GeneratorWrapper.Base wrapper = identityInfo != null ? getWrapper(identityInfo.generator()) : null;
            if (wrapper == null) {
                return null;
            }
            if (identityReference != null && identityReference.alwaysAsId()) {
                return wrapper.processAsId(identityInfo.property(), type, context, mapper);
            } else {
                return wrapper.processAsProperty(identityInfo.property(), type, context, mapper);
            }
        }

        private GeneratorWrapper.Base getWrapper(Class<? extends ObjectIdGenerator> generator) {
            if (ObjectIdGenerators.PropertyGenerator.class.isAssignableFrom(generator)) {
                return new PropertyGeneratorWrapper(generator);
            } else if (ObjectIdGenerators.IntSequenceGenerator.class.isAssignableFrom(generator)) {
                return new IntGeneratorWrapper(generator);
            } else if (ObjectIdGenerators.UUIDGenerator.class.isAssignableFrom(generator)) {
                return new UUIDGeneratorWrapper(generator);
            } else if (ObjectIdGenerators.None.class.isAssignableFrom(generator)) {
                return new NoneGeneratorWrapper(generator);
            }
            return null;
        }

        protected Schema process(Schema id, String propertyName, AnnotatedType type,
                                 ModelConverterContext context) {

            type = removeJsonIdentityAnnotations(type);
            Schema model = context.resolve(type);
            if (model == null) {
                model = resolve(type, context, null);
            }
            model.addProperties(propertyName, id);
            Schema retSchema = openapi31 ? new JsonSchema() : new Schema();
            return retSchema.$ref(StringUtils.isNotEmpty(model.get$ref())
                    ? model.get$ref() : model.getName());
        }

        private AnnotatedType removeJsonIdentityAnnotations(AnnotatedType type) {
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
                    .components(type.getComponents())
                    .ctxAnnotations(AnnotationsUtils.removeAnnotations(type.getCtxAnnotations(), JsonIdentityInfo.class, JsonIdentityReference.class));
        }
    }

    protected boolean applyBeanValidatorAnnotations(BeanPropertyDefinition propDef, Schema property, Annotation[] annotations, Schema parent, boolean applyNotNullAnnotations) {
        boolean updated = false;
        updated = applyBeanValidatorAnnotations(property, annotations, parent, applyNotNullAnnotations);

        if (Objects.nonNull(property.getItems())) {
            Annotation[] genericTypeArgumentAnnotations = extractGenericTypeArgumentAnnotations(propDef).toArray(Annotation[]::new);
            updated = applyBeanValidatorAnnotations(property.getItems(), genericTypeArgumentAnnotations, property, applyNotNullAnnotations) || updated;
        }
        return updated;
    }

    protected Configuration.GroupsValidationStrategy resolveGroupsValidationStrategy() {
        return configuration == null ? Configuration.GroupsValidationStrategy.DEFAULT : configuration.getGroupsValidationStrategy();
    }

    protected Set<Class> resolveValidationInvocationGroups(Map<String, Annotation> annos) {
        Set<Class> invocationGroups = new LinkedHashSet<>();
        if (annos.containsKey("org.springframework.validation.annotation.Validated")) {
            Annotation validated = annos.get("org.springframework.validation.annotation.Validated");
            Method method = null;
            try {
                method = validated.getClass().getDeclaredMethod("value");
                Class[] groups = (Class[]) method.invoke(validated, null);
                invocationGroups.addAll(Arrays.asList(groups));
            } catch (Exception e) {
                //
            }
        }
        if (annos.containsKey("io.swagger.v3.oas.annotations.parameters.ValidatedParameter")) {
            Annotation validated = annos.get("io.swagger.v3.oas.annotations.parameters.ValidatedParameter");
            Method method = null;
            try {
                method = validated.getClass().getDeclaredMethod("value");
                Class[] groups = (Class[]) method.invoke(validated, null);
                invocationGroups.addAll(Arrays.asList(groups));
            } catch (Exception e) {
                //
            }
        }
        if (annos.containsKey("io.swagger.v3.oas.annotations.Parameter")) {
            Parameter parameter = (Parameter) annos.get("io.swagger.v3.oas.annotations.Parameter");
            invocationGroups.addAll(Arrays.asList(parameter.validationGroups()));
        }
        return invocationGroups;
    }

    protected Set<Annotation> resolveValidationInvocationAnnotations(Annotation[] annotations) {
        Set<Annotation> validationInvocationAnnotations = new LinkedHashSet<>();
        if (annotations == null) {
            return validationInvocationAnnotations;
        }
        for (Annotation anno : annotations) {
            if (anno.annotationType().getName().equals("org.springframework.validation.annotation.Validated")) {
                validationInvocationAnnotations.add(anno);
            }
            if (anno.annotationType().getName().equals("io.swagger.v3.oas.annotations.parameters.ValidatedParameter")) {
                validationInvocationAnnotations.add(anno);
            }
            if (anno.annotationType().getName().equals("io.swagger.v3.oas.annotations.Parameter")) {
                validationInvocationAnnotations.add(anno);
            }
        }
        return validationInvocationAnnotations;
    }

    protected boolean applyBeanValidatorAnnotations(Schema property, Annotation[] annotations, Schema parent, boolean applyNotNullAnnotations) {
        boolean modified = false;
        Configuration.GroupsValidationStrategy strategy = resolveGroupsValidationStrategy();
        if (strategy.equals(Configuration.GroupsValidationStrategy.ALWAYS)) {
            return applyBeanValidatorAnnotationsNoGroups(property, annotations, parent, applyNotNullAnnotations);
        } else if (strategy.equals(Configuration.GroupsValidationStrategy.NEVER)) {
            return modified;
        }
        if (validatorProcessor != null &&
                (validatorProcessor.getMode().equals(ValidatorProcessor.MODE.BEFORE) ||
                        validatorProcessor.getMode().equals(ValidatorProcessor.MODE.REPLACE))) {
            modified = validatorProcessor.applyBeanValidatorAnnotations(property, annotations, parent, applyNotNullAnnotations);
            if (validatorProcessor.getMode().equals(ValidatorProcessor.MODE.REPLACE)) {
                return modified;
            }
        }
        Map<String, Annotation> annos = new HashMap<>();
        if (annotations != null) {
            for (Annotation anno : annotations) {
                annos.put(anno.annotationType().getName(), anno);
            }
        }

        if (parent != null &&
                Arrays.stream(annotations).anyMatch(
                        annotation -> annotation.annotationType().getSimpleName().equalsIgnoreCase("NonNull"))) {
            modified = updateRequiredItem(parent, property.getName()) || modified;

        }

        // see if we have Validated or other group-aware annotations
        Set<Class> invocationGroups = null;
        if (validatorProcessor != null) {
            invocationGroups = validatorProcessor.resolveInvocationGroups(annos);
        }
        if (invocationGroups == null) {
            invocationGroups = resolveValidationInvocationGroups(annos);
        }
        if (invocationGroups.isEmpty()) {
            if (strategy.equals(Configuration.GroupsValidationStrategy.NEVER_IF_NO_CONTEXT)) {
                return modified;
            }
        } else {
            if (!(strategy.equals(Configuration.GroupsValidationStrategy.DEFAULT) || strategy.equals(Configuration.GroupsValidationStrategy.NEVER_IF_NO_CONTEXT))) {
                return modified;
            }
        }
        boolean acceptNoGroups = !strategy.equals(Configuration.GroupsValidationStrategy.NEVER_IF_NO_CONTEXT);
        // if we get here, validate only if groups match.
        if (parent != null && annos.containsKey("javax.validation.constraints.NotNull") && applyNotNullAnnotations) {
            NotNull anno = (NotNull) annos.get("javax.validation.constraints.NotNull");
            if (anno.groups().length == 0 && acceptNoGroups) {
                ;
                // no groups, so apply
                modified = updateRequiredItem(parent, property.getName()) || modified;
            } else {
                // check if the groups match
                for (Class group : anno.groups()) {
                    if (invocationGroups.contains(group)) {
                        modified = updateRequiredItem(parent, property.getName()) || modified;
                    }
                }
            }
        }

        if (annos.containsKey("javax.validation.constraints.NotEmpty")) {
            NotEmpty anno = (NotEmpty) annos.get("javax.validation.constraints.NotEmpty");
            boolean apply = checkGroupValidation(anno.groups(), invocationGroups, acceptNoGroups);
            if (apply) {
                io.swagger.v3.oas.annotations.media.Schema ctxSchema = AnnotationsUtils.getSchemaAnnotation(annotations);
                io.swagger.v3.oas.annotations.media.ArraySchema ctxArraySchema = AnnotationsUtils.getArraySchemaAnnotation(annotations);
                if (isArraySchema(property)) {
                    if (ctxArraySchema == null || ctxArraySchema.minItems() == Integer.MAX_VALUE){
                        property.setMinItems(1);
                        modified = true;
                    }
                } else if (isStringSchema(property)) {
                    if (ctxSchema == null || ctxSchema.minLength() == 0) {
                        property.setMinLength(1);
                        modified = true;
                    }
                } else if (isObjectSchema(property)) {
                    if (ctxSchema == null || ctxSchema.minProperties() == 0) {
                        property.setMinProperties(1);
                        modified = true;
                    }
                }
                modified = updateRequiredItem(parent, property.getName()) || modified;
            }
        }

        if (annos.containsKey("javax.validation.constraints.NotBlank")) {
            NotBlank anno = (NotBlank) annos.get("javax.validation.constraints.NotBlank");
            boolean apply = checkGroupValidation(anno.groups(), invocationGroups, acceptNoGroups);
            if (apply) {
                if (isStringSchema(property)) {
                    io.swagger.v3.oas.annotations.media.Schema ctxSchema = AnnotationsUtils.getSchemaAnnotation(annotations);
                    if (ctxSchema == null || ctxSchema.minLength() == 0) {
                        property.setMinLength(1);
                        modified = true;
                    }
                }
                modified = updateRequiredItem(parent, property.getName()) || modified;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Min")) {
            Min anno = (Min) annos.get("javax.validation.constraints.Min");
            boolean apply = checkGroupValidation(anno.groups(), invocationGroups, acceptNoGroups);
            if (apply && isNumberSchema(property)) {
                property.setMinimum(new BigDecimal(anno.value()));
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Max")) {
            Max anno = (Max) annos.get("javax.validation.constraints.Max");
            boolean apply = checkGroupValidation(anno.groups(), invocationGroups, acceptNoGroups);
            if (apply && isNumberSchema(property)) {
                property.setMaximum(new BigDecimal(anno.value()));
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Size")) {
            Size anno = (Size) annos.get("javax.validation.constraints.Size");
            boolean apply = checkGroupValidation(anno.groups(), invocationGroups, acceptNoGroups);
            if (apply) {
                if (isNumberSchema(property)) {
                    property.setMinimum(new BigDecimal(anno.min()));
                    property.setMaximum(new BigDecimal(anno.max()));
                    modified = true;
                }
                if (isStringSchema(property)) {
                    property.setMinLength(Integer.valueOf(anno.min()));
                    property.setMaxLength(Integer.valueOf(anno.max()));
                    modified = true;
                }
                if (isArraySchema(property)) {
                    property.setMinItems(anno.min());
                    property.setMaxItems(anno.max());
                    modified = true;
                }
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMin")) {
            DecimalMin min = (DecimalMin) annos.get("javax.validation.constraints.DecimalMin");
            boolean apply = checkGroupValidation(min.groups(), invocationGroups, acceptNoGroups);
            if (apply && isNumberSchema(property)) {
                property.setMinimum(new BigDecimal(min.value()));
                property.setExclusiveMinimum(!min.inclusive());
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMax")) {
            DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
            boolean apply = checkGroupValidation(max.groups(), invocationGroups, acceptNoGroups);
            if (apply && isNumberSchema(property)) {
                property.setMaximum(new BigDecimal(max.value()));
                property.setExclusiveMaximum(!max.inclusive());
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Pattern")) {
            Pattern pattern = (Pattern) annos.get("javax.validation.constraints.Pattern");
            boolean apply = checkGroupValidation(pattern.groups(), invocationGroups, acceptNoGroups);
            if (apply) {
                if (isStringSchema(property)) {
                    property.setPattern(pattern.regexp());
                    modified = true;
                }
                if (property.getItems() != null && isStringSchema(property.getItems())) {
                    property.getItems().setPattern(pattern.regexp());
                    modified = true;
                }
            }
        }
        if (annos.containsKey("javax.validation.constraints.Email")) {
            Email email = (Email) annos.get("javax.validation.constraints.Email");
            boolean apply = checkGroupValidation(email.groups(), invocationGroups, acceptNoGroups);
            if (apply) {
                if (isStringSchema(property)) {
                    property.setFormat("email");
                    modified = true;
                }
                if (property.getItems() != null && isStringSchema(property.getItems())) {
                    property.getItems().setFormat("email");
                    modified = true;
                }
            }
        }
        if (validatorProcessor != null && validatorProcessor.getMode().equals(ValidatorProcessor.MODE.AFTER)) {
            modified = validatorProcessor.applyBeanValidatorAnnotations(property, annotations, parent, applyNotNullAnnotations) || modified;
        }
        return modified;
    }

    protected boolean checkGroupValidation(Class[] groups, Set<Class> invocationGroups, boolean acceptNoGroups) {
        if (groups.length == 0) {
            return acceptNoGroups;
        } else {
            for (Class group : groups) {
                if (invocationGroups.contains(group)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean applyBeanValidatorAnnotationsNoGroups(Schema property, Annotation[] annotations, Schema parent, boolean applyNotNullAnnotations) {
        Map<String, Annotation> annos = new HashMap<>();
        boolean modified = false;
        if (annotations != null) {
            for (Annotation anno : annotations) {
                annos.put(anno.annotationType().getName(), anno);
            }
        }
        if (parent != null && annotations != null && applyNotNullAnnotations) {
            boolean requiredItem = Arrays.stream(annotations).anyMatch(annotation ->
                    NOT_NULL_ANNOTATIONS.contains(annotation.annotationType().getSimpleName())
            );
            if (requiredItem) {
                modified = updateRequiredItem(parent, property.getName());
            }
        }
        if (annos.containsKey("javax.validation.constraints.Min")) {
            if (isNumberSchema(property)) {
                Min min = (Min) annos.get("javax.validation.constraints.Min");
                property.setMinimum(new BigDecimal(min.value()));
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Max")) {
            if (isNumberSchema(property)) {
                Max max = (Max) annos.get("javax.validation.constraints.Max");
                property.setMaximum(new BigDecimal(max.value()));
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Size")) {
            Size size = (Size) annos.get("javax.validation.constraints.Size");
            if (isNumberSchema(property)) {
                if (size.min() != 0) {
                    property.setMinimum(new BigDecimal(size.min()));
                    modified = true;
                }
                if (size.max() != Integer.MAX_VALUE) {
                    property.setMaximum(new BigDecimal(size.max()));
                    modified = true;
                }

            }
            if (isStringSchema(property)) {
                if (size.min() != 0) {
                    property.setMinLength(Integer.valueOf(size.min()));
                    modified = true;
                }
                if (size.max() != Integer.MAX_VALUE) {
                    property.setMaxLength(Integer.valueOf(size.max()));
                    modified = true;
                }
            }
            if (isArraySchema(property)) {
                if (size.min() != 0) {
                    property.setMinItems(size.min());
                    modified = true;
                }
                if (size.max() != Integer.MAX_VALUE) {
                    property.setMaxItems(size.max());
                    modified = true;
                }
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMin")) {
            DecimalMin min = (DecimalMin) annos.get("javax.validation.constraints.DecimalMin");
            if (isNumberSchema(property)) {
                property.setMinimum(new BigDecimal(min.value()));
                property.setExclusiveMinimum(!min.inclusive());
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.DecimalMax")) {
            DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
            if (isNumberSchema(property)) {
                property.setMaximum(new BigDecimal(max.value()));
                property.setExclusiveMaximum(!max.inclusive());
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Pattern")) {
            Pattern pattern = (Pattern) annos.get("javax.validation.constraints.Pattern");
            if (isStringSchema(property)) {
                property.setPattern(pattern.regexp());
                modified = true;
            }
            if (property.getItems() != null && isStringSchema(property.getItems())) {
                property.getItems().setPattern(pattern.regexp());
                modified = true;
            }
        }
        if (annos.containsKey("javax.validation.constraints.Email")) {
            if (isStringSchema(property)) {
                property.setFormat("email");
                modified = true;
            }
            if (property.getItems() != null && isStringSchema(property.getItems())) {
                property.getItems().setFormat("email");
                modified = true;
            }
        }
        return modified;
    }

    private boolean resolveSubtypes(Schema model, BeanDescription bean, ModelConverterContext context, JsonView jsonViewAnnotation) {
        final List<NamedType> types = _intr().findSubtypes(bean.getClassInfo());
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

            final Schema subtypeModel = context.resolve(new AnnotatedType()
                    .type(subtypeType)
                    .jsonViewAnnotation(jsonViewAnnotation)
                    .subtype(true));

            if (StringUtils.isBlank(subtypeModel.getName()) ||
                    subtypeModel.getName().equals(model.getName())) {
                subtypeModel.setName(_typeNameResolver.nameForType(_mapper.constructType(subtypeType),
                        TypeNameResolver.Options.SKIP_API_MODEL));
            }

            // here schema could be not composed, but we want it to be composed, doing same work as done
            // in resolve method??

            ComposedSchema composedSchema = null;
            if (!(subtypeModel instanceof ComposedSchema)) {
                // create composed schema
                composedSchema = ComposedSchema.from(subtypeModel);
            } else {
                composedSchema = (ComposedSchema) subtypeModel;
            }
            Schema refSchema = openapi31 ? new JsonSchema() : new Schema();
            refSchema.$ref(Components.COMPONENTS_SCHEMAS_REF + model.getName());
            // allOf could have already being added during type resolving when @Schema(allOf..) is declared
            if (composedSchema.getAllOf() == null || !composedSchema.getAllOf().contains(refSchema)) {
                composedSchema.addAllOfItem(refSchema);
            }
            removeParentProperties(composedSchema, model);
            if (!composedModelPropertiesAsSibling) {
                if (composedSchema.getAllOf() != null && !composedSchema.getAllOf().isEmpty()) {
                    if (composedSchema.getProperties() != null && !composedSchema.getProperties().isEmpty()) {
                        Schema propSchema = openapi31 ? new JsonSchema().typesItem("object") : new ObjectSchema();
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
        Class<?> beanClass = bean.getType().getRawClass();
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
        final List<NamedType> superTypes = _intr().findSubtypes(superBean.getClassInfo());
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

    protected Map<String, Schema> resolvePatternProperties(JavaType a, Annotation[] annotations, ModelConverterContext context) {

        final Map<String, PatternProperty> propList = new LinkedHashMap<>();

        PatternProperties props = a.getRawClass().getAnnotation(PatternProperties.class);
        if (props != null && props.value().length > 0) {
            for (PatternProperty prop : props.value()) {
                propList.put(prop.regex(), prop);
            }
        }
        PatternProperty singleProp = a.getRawClass().getAnnotation(PatternProperty.class);
        if (singleProp != null) {
            propList.put(singleProp.regex(), singleProp);
        }
        props = AnnotationsUtils.getAnnotation(PatternProperties.class, annotations);
        if (props != null && props.value().length > 0) {
            for (PatternProperty prop : props.value()) {
                propList.put(prop.regex(), prop);
            }
        }
        singleProp = AnnotationsUtils.getAnnotation(PatternProperty.class, annotations);
        if (singleProp != null) {
            propList.put(singleProp.regex(), singleProp);
        }

        if (propList.isEmpty()) {
            return null;
        }

        Map<String, Schema> patternProperties = new LinkedHashMap<>();

        for (PatternProperty prop : propList.values()) {
            String key = prop.regex();
            if (StringUtils.isBlank(key)) {
                continue;
            }
            Annotation[] propAnnotations = new Annotation[]{prop.schema(), prop.array()};
            AnnotatedType propType = new AnnotatedType()
                    .type(String.class)
                    .ctxAnnotations(propAnnotations)
                    .resolveAsRef(true);
            Schema resolvedPropSchema = context.resolve(propType);
            if (resolvedPropSchema != null) {
                patternProperties.put(key, resolvedPropSchema);
            }
        }
        return patternProperties;
    }

    protected Map<String, Schema> resolveSchemaProperties(JavaType a, Annotation[] annotations, ModelConverterContext context) {

        final Map<String, SchemaProperty> propList = new LinkedHashMap<>();

        SchemaProperties props = a.getRawClass().getAnnotation(SchemaProperties.class);
        if (props != null && props.value().length > 0) {
            for (SchemaProperty prop : props.value()) {
                propList.put(prop.name(), prop);
            }
        }
        SchemaProperty singleProp = a.getRawClass().getAnnotation(SchemaProperty.class);
        if (singleProp != null) {
            propList.put(singleProp.name(), singleProp);
        }
        props = AnnotationsUtils.getAnnotation(SchemaProperties.class, annotations);
        if (props != null && props.value().length > 0) {
            for (SchemaProperty prop : props.value()) {
                propList.put(prop.name(), prop);
            }
        }
        singleProp = AnnotationsUtils.getAnnotation(SchemaProperty.class, annotations);
        if (singleProp != null) {
            propList.put(singleProp.name(), singleProp);
        }

        if (propList.isEmpty()) {
            return null;
        }

        Map<String, Schema> schemaProperties = new LinkedHashMap<>();

        for (SchemaProperty prop : propList.values()) {
            String key = prop.name();
            if (StringUtils.isBlank(key)) {
                continue;
            }
            Annotation[] propAnnotations = new Annotation[]{prop.schema(), prop.array()};
            AnnotatedType propType = new AnnotatedType()
                    .type(String.class)
                    .ctxAnnotations(propAnnotations)
                    .resolveAsRef(true);
            Schema resolvedPropSchema = context.resolve(propType);
            if (resolvedPropSchema != null) {
                schemaProperties.put(key, resolvedPropSchema);
            }
        }
        return schemaProperties;
    }

    protected Map<String, Schema> resolveDependentSchemas(JavaType a, Annotation[] annotations, ModelConverterContext context, Components components, JsonView jsonViewAnnotation, boolean openapi31) {
        final Map<String, DependentSchema> dependentSchemaMap = new LinkedHashMap<>();

        DependentSchemas dependentSchemasAnnotation = a.getRawClass().getAnnotation(DependentSchemas.class);
        if (dependentSchemasAnnotation != null && dependentSchemasAnnotation.value().length > 0) {
            for (DependentSchema dependentSchemaAnnotation : dependentSchemasAnnotation.value()) {
                dependentSchemaMap.put(dependentSchemaAnnotation.name(), dependentSchemaAnnotation);
            }
        }

        DependentSchema singleDependentSchema = a.getRawClass().getAnnotation(DependentSchema.class);
        if (singleDependentSchema != null) {
            dependentSchemaMap.put(singleDependentSchema.name(), singleDependentSchema);
        }

        dependentSchemasAnnotation = AnnotationsUtils.getAnnotation(DependentSchemas.class, annotations);
        if (dependentSchemasAnnotation != null && dependentSchemasAnnotation.value().length > 0) {
            for (DependentSchema dependentSchemaAnnotation : dependentSchemasAnnotation.value()) {
                dependentSchemaMap.put(dependentSchemaAnnotation.name(), dependentSchemaAnnotation);
            }
        }

        singleDependentSchema = AnnotationsUtils.getAnnotation(DependentSchema.class, annotations);
        if (singleDependentSchema != null) {
            dependentSchemaMap.put(singleDependentSchema.name(), singleDependentSchema);
        }

        if (dependentSchemaMap.isEmpty()) {
            return null;
        }

        Map<String, Schema> dependentSchemas = new LinkedHashMap<>();

        for (DependentSchema dependentSchemaAnnotation : dependentSchemaMap.values()) {
            String name = dependentSchemaAnnotation.name();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            Annotation[] propAnnotations = new Annotation[]{dependentSchemaAnnotation.schema(), dependentSchemaAnnotation.array()};
            Schema existingSchema = null;
            Optional<Schema> resolvedPropSchema = AnnotationsUtils.getSchemaFromAnnotation(dependentSchemaAnnotation.schema(), components, jsonViewAnnotation, openapi31, null, Schema.SchemaResolution.DEFAULT, context);
            if (resolvedPropSchema.isPresent()) {
                existingSchema = resolvedPropSchema.get();
                dependentSchemas.put(name, existingSchema);
            }
            resolvedPropSchema = AnnotationsUtils.getArraySchema(dependentSchemaAnnotation.array(), components, jsonViewAnnotation, openapi31, existingSchema);
            if (resolvedPropSchema.isPresent()) {
                dependentSchemas.put(name, resolvedPropSchema.get());
            }
        }

        return dependentSchemas;
    }

    protected Object resolveDefaultValue(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null) {
            if (!schema.defaultValue().isEmpty()) {
                try {
                    ObjectMapper mapper = ObjectMapperFactory.buildStrictGenericObjectMapper();
                    return mapper.readTree(schema.defaultValue());
                } catch (IOException e) {
                    return schema.defaultValue();
                }
            }
        }
        if (a == null) {
            return null;
        }
        XmlElement elem = a.getAnnotation(XmlElement.class);
        if (elem == null) {
            if (annotations != null) {
                for (Annotation ann : annotations) {
                    if (ann instanceof XmlElement) {
                        elem = (XmlElement) ann;
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

    protected io.swagger.v3.oas.annotations.media.Schema.RequiredMode resolveRequiredMode(io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && !schema.requiredMode().equals(io.swagger.v3.oas.annotations.media.Schema.RequiredMode.AUTO)) {
            return schema.requiredMode();
        } else if (schema != null && schema.required()) {
            return io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
        }
        return io.swagger.v3.oas.annotations.media.Schema.RequiredMode.AUTO;
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
        if (schema != null && NumberUtils.isCreatable(schema.minimum())) {
            String filteredMinimum = schema.minimum().replace(Constants.COMMA, StringUtils.EMPTY);
            return new BigDecimal(filteredMinimum);
        }
        return null;
    }

    protected BigDecimal resolveMaximum(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && NumberUtils.isCreatable(schema.maximum())) {
            String filteredMaximum = schema.maximum().replace(Constants.COMMA, StringUtils.EMPTY);
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
            boolean usePrefix = !openapi31;
            return AnnotationsUtils.getExtensions(openapi31, usePrefix, schema.extensions());
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
                    modelToUpdate = context.getDefinedModels().get(model.get$ref().substring(SCHEMA_COMPONENT_PREFIX));
                }
                if (modelToUpdate.getProperties() == null || !modelToUpdate.getProperties().keySet().contains(typeInfoProp)) {
                    Schema discriminatorSchema = openapi31 ? new JsonSchema().typesItem("string").name(typeInfoProp) : new StringSchema().name(typeInfoProp);
                    modelToUpdate.addProperties(typeInfoProp, discriminatorSchema);
                    if (modelToUpdate.getRequired() == null || !modelToUpdate.getRequired().contains(typeInfoProp)) {
                        modelToUpdate.addRequiredItem(typeInfoProp);
                    }
                }
            }
        }
    }

    /*
     TODO partial implementation supporting WRAPPER_OBJECT with JsonTypeInfo.Id.CLASS and JsonTypeInfo.Id.NAME

     Also note that JsonTypeInfo on interfaces are not considered as multiple interfaces might have conflicting
     annotations, although Jackson seems to apply them if present on an interface
     */
    protected Schema resolveWrapping(JavaType type, ModelConverterContext context, Schema model) {
        // add JsonTypeInfo.property if not member of bean
        JsonTypeInfo typeInfo = type.getRawClass().getDeclaredAnnotation(JsonTypeInfo.class);
        if (typeInfo != null) {
            JsonTypeInfo.Id id = typeInfo.use();
            JsonTypeInfo.As as = typeInfo.include();
            if (JsonTypeInfo.As.WRAPPER_OBJECT.equals(as)) {
                String name = model.getName();
                if (JsonTypeInfo.Id.CLASS.equals(id)) {
                    name = type.getRawClass().getName();
                }
                JsonTypeName typeName = type.getRawClass().getDeclaredAnnotation((JsonTypeName.class));
                if (JsonTypeInfo.Id.NAME.equals(id) && typeName != null) {
                    name = typeName.value();
                }
                if (JsonTypeInfo.Id.NAME.equals(id) && name == null) {
                    name = type.getRawClass().getSimpleName();
                }
                Schema wrapperSchema = openapi31 ? new JsonSchema().typesItem("object") : new ObjectSchema();
                wrapperSchema.name(model.getName());
                wrapperSchema.addProperties(name, model);
                return wrapperSchema;
            }
        }
        return model;
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
                DiscriminatorMapping[] mappings = declaredSchemaAnnotation.discriminatorMapping();
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
        // if XmlRootElement annotation, construct a Xml object and attach it to the model
        XmlRootElement rootAnnotation = null;
        XmlSchema xmlSchema = null;
        if (a != null) {
            rootAnnotation = a.getAnnotation(XmlRootElement.class);
            Class<?> rawType = a.getRawType();
            if (rawType != null) {
                Package aPackage = rawType.getPackage();
                if (aPackage != null) {
                    xmlSchema = aPackage.getAnnotation(XmlSchema.class);
                }
            }
        }
        if (rootAnnotation == null) {
            if (annotations != null) {
                for (Annotation ann : annotations) {
                    if (ann instanceof XmlRootElement) {
                        rootAnnotation = (XmlRootElement) ann;
                        break;
                    }
                }
            }
        }

        if (rootAnnotation != null && !"".equals(rootAnnotation.name()) && !JAXB_DEFAULT.equals(rootAnnotation.name())) {
            XML xml = new XML().name(rootAnnotation.name());
            if (xmlSchema != null && isNonTrivialXmlNamespace(xmlSchema.namespace())) {
                xml.namespace(xmlSchema.namespace());
            }
            // Let XmlRootElement overwrite global XmlSchema namespace if present
            if (isNonTrivialXmlNamespace(rootAnnotation.namespace())) {
                xml.namespace(rootAnnotation.namespace());
            }
            return xml;
        }
        return null;
    }

    private boolean isNonTrivialXmlNamespace(String namespace) {
        return namespace != null && !"".equals(namespace) && !JAXB_DEFAULT.equals(namespace);
    }

    protected Set<String> resolveIgnoredProperties(Annotations a, Annotation[] annotations) {

        Set<String> propertiesToIgnore = new HashSet<>();
        JsonIgnoreProperties ignoreProperties = a.get(JsonIgnoreProperties.class);
        if (ignoreProperties != null) {
            if (!ignoreProperties.allowGetters()) {
                propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
            }
        }
        propertiesToIgnore.addAll(resolveIgnoredProperties(annotations));
        return propertiesToIgnore;
    }

    protected Set<String> resolveIgnoredProperties(Annotation[] annotations) {

        Set<String> propertiesToIgnore = new HashSet<>();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof JsonIgnoreProperties) {
                    if (!((JsonIgnoreProperties) annotation).allowGetters()) {
                        propertiesToIgnore.addAll(Arrays.asList(((JsonIgnoreProperties) annotation).value()));
                        break;
                    }
                }
            }
        }
        return propertiesToIgnore;
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
            boolean usePrefix = !openapi31;
            return AnnotationsUtils.getExtensions(openapi31, usePrefix, arraySchema.extensions());
        }
        return null;
    }

    protected Integer resolveMaxContains(AnnotatedType a, io.swagger.v3.oas.annotations.media.ArraySchema arraySchema) {
        if (arraySchema != null && arraySchema.maxContains() > 0) {
            return arraySchema.maxContains();
        }
        return null;
    }

    protected Integer resolveMinContains(AnnotatedType a, io.swagger.v3.oas.annotations.media.ArraySchema arraySchema) {
        if (arraySchema != null && arraySchema.minContains() > 0) {
            return arraySchema.minContains();
        }
        return null;
    }

    protected BigDecimal resolveExclusiveMaximumValue(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.exclusiveMaximumValue() > 0) {
            return new BigDecimal(schema.exclusiveMaximumValue());
        }
        return null;
    }

    protected BigDecimal resolveExclusiveMinimumValue(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && schema.exclusiveMinimumValue() > 0) {
            return new BigDecimal(schema.exclusiveMinimumValue());
        }
        return null;
    }

    protected String resolveId(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.$id())) {
            return schema.$id();
        }
        return null;
    }

    protected String resolve$schema(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.$schema())) {
            return schema.$schema();
        }
        return null;
    }

    protected String resolve$anchor(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.$anchor())) {
            return schema.$anchor();
        }
        return null;
    }

    protected String resolve$comment(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.$comment())) {
            return schema.$comment();
        }
        return null;
    }

    protected String resolve$vocabulary(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.$vocabulary())) {
            return schema.$vocabulary();
        }
        return null;
    }

    protected String resolve$dynamicAnchor(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.$dynamicAnchor())) {
            return schema.$dynamicAnchor();
        }
        return null;
    }

    protected String resolve$dynamicRef(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.$dynamicRef())) {
            return schema.$dynamicRef();
        }
        return null;
    }

    protected String resolveContentEncoding(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.contentEncoding())) {
            return schema.contentEncoding();
        }
        return null;
    }

    protected String resolveContentMediaType(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema.contentMediaType())) {
            return schema.contentMediaType();
        }
        return null;
    }

    protected void resolveContains(AnnotatedType annotatedType, ArraySchema arraySchema, io.swagger.v3.oas.annotations.media.ArraySchema arraySchemaAnnotation) {
        final io.swagger.v3.oas.annotations.media.Schema containsAnnotation = arraySchemaAnnotation.contains();
        final Schema contains = openapi31 ? new JsonSchema() : new Schema();
        if (containsAnnotation.types().length > 0) {
            for (String type : containsAnnotation.types()) {
                contains.addType(type);
            }
        }
        arraySchema.setContains(contains);
        resolveSchemaMembers(contains, null, null, containsAnnotation);

        Integer maxContains = resolveMaxContains(annotatedType, arraySchemaAnnotation);
        if (maxContains != null) {
            arraySchema.setMaxContains(maxContains);
        }
        Integer minContains = resolveMinContains(annotatedType, arraySchemaAnnotation);
        if (minContains != null) {
            arraySchema.setMinContains(minContains);
        }
    }

    protected void resolveUnevaluatedItems(AnnotatedType annotatedType, ArraySchema arraySchema, io.swagger.v3.oas.annotations.media.ArraySchema arraySchemaAnnotation) {
        final io.swagger.v3.oas.annotations.media.Schema unevaluatedItemsAnnotation = arraySchemaAnnotation.unevaluatedItems();
        final Schema unevaluatedItems = openapi31 ? new JsonSchema() : new Schema();
        if (StringUtils.isNotBlank(unevaluatedItemsAnnotation.type())) {
            unevaluatedItems.addType(unevaluatedItemsAnnotation.type());
        }
        if (unevaluatedItemsAnnotation.types().length > 0) {
            for (String type : unevaluatedItemsAnnotation.types()) {
                unevaluatedItems.addType(type);
            }
        }
        arraySchema.setUnevaluatedItems(unevaluatedItems);
        resolveSchemaMembers(unevaluatedItems, null, null, unevaluatedItemsAnnotation);
    }

    protected String resolveConst(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema != null && StringUtils.isNotBlank(schema._const())) {
            return schema._const();
        }
        return null;
    }

    protected Map<String, List<String>> resolveDependentRequired(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema.dependentRequiredMap().length == 0) {
            return null;
        }
        final Map<String, List<String>> dependentRequiredMap = new HashMap<>();
        for (DependentRequired dependentRequired : schema.dependentRequiredMap()) {
            final String name = dependentRequired.name();
            if (dependentRequired.value().length == 0) {
                continue;
            }
            final List<String> values = Arrays.asList(dependentRequired.value());
            dependentRequiredMap.put(name, values);
        }
        return dependentRequiredMap;
    }

    protected Map<String, Schema> resolveDependentSchemas(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (schemaAnnotation.dependentSchemas().length == 0) {
            return null;
        }
        final Map<String, Schema> dependentSchemas = new HashMap<>();
        for (StringToClassMapItem mapItem : schemaAnnotation.dependentSchemas()) {
            final String key = mapItem.key();
            if (mapItem.value() == null || Void.class.equals(mapItem.value())) {
                continue;
            }
            final Schema schema = resolve(new AnnotatedType(mapItem.value()), context, next);
            if (schema == null) {
                continue;
            }
            dependentSchemas.put(key, schema);
        }
        return dependentSchemas;
    }

    protected Map<String, Schema> resolvePatternProperties(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (schemaAnnotation.patternProperties().length == 0) {
            return null;
        }
        final Map<String, Schema> patternPropertyMap = new HashMap<>();
        for (StringToClassMapItem patternPropertyItem : schemaAnnotation.patternProperties()) {
            final String key = patternPropertyItem.key();
            if (Void.class.equals(patternPropertyItem.value())) {
                continue;
            }
            final Schema schema = resolve(new AnnotatedType(patternPropertyItem.value()), context, next);
            if (schema == null) {
                continue;
            }
            patternPropertyMap.put(key, schema);
        }
        return patternPropertyMap;
    }

    protected Map<String, Schema> resolveProperties(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (schemaAnnotation.properties().length == 0) {
            return null;
        }
        final Map<String, Schema> propertyMap = new HashMap<>();
        for (StringToClassMapItem propertyItem : schemaAnnotation.properties()) {
            final String key = propertyItem.key();
            if (Void.class.equals(propertyItem.value())) {
                continue;
            }
            final Schema schema = resolve(new AnnotatedType(propertyItem.value()), context, next);
            if (schema == null) {
                continue;
            }
            propertyMap.put(key, schema);
        }
        return propertyMap;
    }

    protected void resolveSchemaMembers(Schema schema, AnnotatedType annotatedType) {
        resolveSchemaMembers(schema, annotatedType, null, null);
    }

    protected void resolveSchemaMembers(Schema schema, AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {
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
        if (schemaAnnotation != null) {
            if (schemaAnnotation.additionalProperties().equals(io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue.TRUE)) {
                schema.additionalProperties(true);
            } else if (schemaAnnotation.additionalProperties().equals(io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue.FALSE)) {
                schema.additionalProperties(false);
            } else {
                if (!schemaAnnotation.additionalPropertiesSchema().equals(Void.class)) {
                    Schema additionalPropertiesSchema = resolve(new AnnotatedType(schemaAnnotation.additionalPropertiesSchema()), context, next);
                    additionalPropertiesSchema = buildRefSchemaIfObject(additionalPropertiesSchema, context);
                    schema.additionalProperties(additionalPropertiesSchema);
                }
            }
        }

        if (openapi31 && schema != null && schemaAnnotation != null) {
            if (!Void.class.equals(schemaAnnotation.contentSchema())) {
                Schema contentSchema = resolve(new AnnotatedType(schemaAnnotation.contentSchema()), context, next);
                contentSchema = buildRefSchemaIfObject(contentSchema, context);
                schema.setContentSchema(contentSchema);
            }
            if (!Void.class.equals(schemaAnnotation.propertyNames())) {
                Schema propertyNames = resolve(new AnnotatedType(schemaAnnotation.propertyNames()), context, next);
                propertyNames = buildRefSchemaIfObject(propertyNames, context);
                schema.setPropertyNames(propertyNames);
            }
            if (!Void.class.equals(schemaAnnotation._if())) {
                Schema ifSchema = resolve(new AnnotatedType(schemaAnnotation._if()), context, next);
                ifSchema = buildRefSchemaIfObject(ifSchema, context);
                schema.setIf(ifSchema);
            }
            if (!Void.class.equals(schemaAnnotation._else())) {
                Schema elseSchema = resolve(new AnnotatedType(schemaAnnotation._else()), context, next);
                elseSchema = buildRefSchemaIfObject(elseSchema, context);
                schema.setElse(elseSchema);
            }
            if (!Void.class.equals(schemaAnnotation.then())) {
                Schema thenSchema = resolve(new AnnotatedType(schemaAnnotation.then()), context, next);
                thenSchema = buildRefSchemaIfObject(thenSchema, context);
                schema.setThen(thenSchema);
            }
            if (!Void.class.equals(schemaAnnotation.unevaluatedProperties())) {
                Schema unevaluatedProperties = resolve(new AnnotatedType(schemaAnnotation.unevaluatedProperties()), context, next);
                unevaluatedProperties = buildRefSchemaIfObject(unevaluatedProperties, context);
                schema.setUnevaluatedProperties(unevaluatedProperties);
            }

            final Map<String, List<String>> dependentRequired = resolveDependentRequired(a, annotations, schemaAnnotation);
            if (dependentRequired != null && !dependentRequired.isEmpty()) {
                schema.setDependentRequired(dependentRequired);
            }
            final Map<String, Schema> dependentSchemas = resolveDependentSchemas(a, annotations, schemaAnnotation, annotatedType, context, next);
            if (dependentSchemas != null) {
                final Map<String, Schema> processedDependentSchemas = new LinkedHashMap<>();
                for (String key : dependentSchemas.keySet()) {
                    Schema val = dependentSchemas.get(key);
                    processedDependentSchemas.put(key, buildRefSchemaIfObject(val, context));
                }
                if (processedDependentSchemas != null && !processedDependentSchemas.isEmpty()) {
                    if (schema.getDependentSchemas() == null) {
                        schema.setDependentSchemas(processedDependentSchemas);
                    } else {
                        schema.getDependentSchemas().putAll(processedDependentSchemas);
                    }
                }
            }
            final Map<String, Schema> patternProperties = resolvePatternProperties(a, annotations, schemaAnnotation, annotatedType, context, next);
            if (patternProperties != null && !patternProperties.isEmpty()) {
                for (String key : patternProperties.keySet()) {
                    schema.addPatternProperty(key, buildRefSchemaIfObject(patternProperties.get(key), context));
                }
            }
            final Map<String, Schema> properties = resolveProperties(a, annotations, schemaAnnotation, annotatedType, context, next);
            if (properties != null && !properties.isEmpty()) {
                for (String key : properties.keySet()) {
                    schema.addProperty(key, buildRefSchemaIfObject(properties.get(key), context));
                }
            }
        }
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
        Object defaultValue = resolveDefaultValue(a, annotations, schemaAnnotation);
        if (defaultValue != null) {
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
            extensions.forEach(schema::addExtension);
        }

        if (openapi31 && schemaAnnotation != null) {
            for (String type : schemaAnnotation.types()) {
                schema.addType(type);
            }
            BigDecimal exclusiveMaximumValue = resolveExclusiveMaximumValue(a, annotations, schemaAnnotation);
            if (exclusiveMaximumValue != null) {
                schema.setExclusiveMaximumValue(exclusiveMaximumValue);
            }
            BigDecimal exclusiveMinimumValue = resolveExclusiveMinimumValue(a, annotations, schemaAnnotation);
            if (exclusiveMinimumValue != null) {
                schema.setExclusiveMinimumValue(exclusiveMinimumValue);
            }
            String $id = resolveId(a, annotations, schemaAnnotation);
            if ($id != null) {
                schema.set$id($id);
            }
            String $schema = resolve$schema(a, annotations, schemaAnnotation);
            if ($schema != null) {
                schema.set$schema($schema);
            }
            String $anchor = resolve$anchor(a, annotations, schemaAnnotation);
            if ($anchor != null) {
                schema.set$anchor($anchor);
            }
            String $comment = resolve$comment(a, annotations, schemaAnnotation);
            if ($comment != null) {
                schema.set$comment($comment);
            }
            String $vocabulary = resolve$vocabulary(a, annotations, schemaAnnotation);
            if ($vocabulary != null) {
                schema.set$vocabulary($vocabulary);
            }
            String $dynamicAnchor = resolve$dynamicAnchor(a, annotations, schemaAnnotation);
            if ($dynamicAnchor != null) {
                schema.$dynamicAnchor($dynamicAnchor);
            }
            String $dynamicRef = resolve$dynamicRef(a, annotations, schemaAnnotation);
            if ($dynamicRef != null) {
                schema.$dynamicRef($dynamicRef);
            }
            String contentEncoding = resolveContentEncoding(a, annotations, schemaAnnotation);
            if (contentEncoding != null) {
                schema.setContentEncoding(contentEncoding);
            }
            String contentMediaType = resolveContentMediaType(a, annotations, schemaAnnotation);
            if (contentMediaType != null) {
                schema.setContentMediaType(contentMediaType);
            }
            if (schemaAnnotation.examples().length > 0) {
                if (schema.getExamples() == null || schema.getExamples().isEmpty()) {
                    schema.setExamples(Arrays.asList(schemaAnnotation.examples()));
                } else {
                    schema.getExamples().addAll(Arrays.asList(schemaAnnotation.examples()));
                }
            }
            String _const = resolveConst(a, annotations, schemaAnnotation);
            if (_const != null) {
                schema.setConst(_const);
            }
        }
    }

    protected void addRequiredItem(Schema model, String propName) {
        updateRequiredItem(model, propName);
    }

    protected boolean updateRequiredItem(Schema model, String propName) {
        if (model == null || propName == null || StringUtils.isBlank(propName)) {
            return false;
        }
        if (model.getRequired() == null || model.getRequired().isEmpty()) {
            model.addRequiredItem(propName);
            return true;
        }
        if (model.getRequired().stream().noneMatch(propName::equals)) {
            model.addRequiredItem(propName);
            return true;
        }
        return false;
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
        JsonIgnoreProperties.Value v = introspector.findPropertyIgnorals(beanDescription.getClassInfo());
        Set<String> ignored = null;
        if (v != null) {
            ignored = v.findIgnoredForSerialization();
        }
        return ignored == null ? Collections.emptyList() : new ArrayList<>(ignored);
    }

    /**
     * Decorate the name based on the JsonView
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

    protected boolean hiddenByJsonView(Annotation[] annotations,
                                       AnnotatedType type) {
        JsonView jsonView = type.getJsonViewAnnotation();
        if (jsonView == null) {
            return false;
        }

        Class<?>[] filters = jsonView.value();
        boolean containsJsonViewAnnotation = !type.isIncludePropertiesWithoutJSONView();
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
            if (openapi31) {
                if (AnnotationsUtils.hasSchemaAnnotation(resolvedArrayAnnotation.contains())) {
                    resolveContains(annotatedType, schema, resolvedArrayAnnotation);
                }
                if (AnnotationsUtils.hasSchemaAnnotation(resolvedArrayAnnotation.unevaluatedItems())) {
                    resolveUnevaluatedItems(annotatedType, schema, resolvedArrayAnnotation);
                }
                if (resolvedArrayAnnotation.prefixItems().length > 0) {
                    for (io.swagger.v3.oas.annotations.media.Schema prefixItemAnnotation : resolvedArrayAnnotation.prefixItems()) {
                        final Schema prefixItem = new JsonSchema();
                        if (StringUtils.isNotBlank(prefixItemAnnotation.type())) {
                            prefixItem.addType(prefixItemAnnotation.type());
                        }
                        resolveSchemaMembers(prefixItem, null, null, prefixItemAnnotation);
                        schema.addPrefixItem(prefixItem);
                    }
                }
                // TODO `ArraySchema.items` is deprecated, when removed, remove this block
                if (schema.getItems() != null && AnnotationsUtils.hasSchemaAnnotation(resolvedArrayAnnotation.items())) {
                    for (String type : resolvedArrayAnnotation.items().types()) {
                        schema.getItems().addType(type);
                    }
                }
                if (schema.getItems() != null && AnnotationsUtils.hasSchemaAnnotation(resolvedArrayAnnotation.schema())) {
                    for (String type : resolvedArrayAnnotation.schema().types()) {
                        schema.getItems().addType(type);
                    }
                }
            }
        }
    }

    public ModelResolver openapi31(boolean openapi31) {
        this.openapi31 = openapi31;
        return this;
    }

    public boolean isOpenapi31() {
        return openapi31;
    }

    public void setOpenapi31(boolean openapi31) {
        this.openapi31 = openapi31;
    }

    public ModelResolver configuration(Configuration configuration) {
        this.setConfiguration(configuration);
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        if (configuration != null) {
            if (configuration.isOpenAPI31() != null) {
                this.openapi31(configuration.isOpenAPI31());
            }
            if (configuration.getSchemaResolution() != null) {
                this.schemaResolution(configuration.getSchemaResolution());
            }
            // see if we have a processor
            if (StringUtils.isNotBlank(configuration.getValidatorProcessorClass())) {
                try {
                    Class<?> processorClass = getClass().getClassLoader().loadClass(configuration.getValidatorProcessorClass());
                    if (ValidatorProcessor.class.isAssignableFrom(processorClass)) {
                        validatorProcessor = (ValidatorProcessor) processorClass.newInstance();
                    }
                } catch (Exception e) {
                    LOGGER.error("Unable to load validator processor class: " + configuration.getValidatorProcessorClass(), e);
                }
            }
        }
    }

    protected boolean isObjectSchema(Schema schema) {
        return (schema.getTypes() != null && schema.getTypes().contains("object")) || "object".equals(schema.getType()) || (schema.getType() == null && ((schema.getProperties() != null && !schema.getProperties().isEmpty()) || (schema.getPatternProperties() != null && !schema.getPatternProperties().isEmpty())));
    }

    protected boolean isInferredObjectSchema(Schema schema) {
        return ((schema.getProperties() != null && !schema.getProperties().isEmpty())
            || (schema.getPatternProperties() != null && !schema.getPatternProperties().isEmpty())
            || (schema.getAdditionalProperties() != null)
            || (schema.getUnevaluatedProperties() != null)
            || (schema.getRequired() != null && !schema.getRequired().isEmpty())
            || (schema.getPropertyNames() != null)
            || (schema.getDependentRequired() != null && !schema.getDependentRequired().isEmpty())
            || (schema.getDependentSchemas() != null && !schema.getDependentSchemas().isEmpty())
            || (schema.getMinProperties() != null && schema.getMinProperties() > 0)
            || (schema.getMaxProperties() != null && schema.getMaxProperties() > 0));
    }

    protected boolean isArraySchema(Schema schema) {
        return "array".equals(schema.getType()) || (schema.getTypes() != null && schema.getTypes().contains("array"));
    }

    protected boolean isStringSchema(Schema schema) {
        return "string".equals(schema.getType()) || (schema.getTypes() != null && schema.getTypes().contains("string"));
    }

    protected boolean isNumberSchema(Schema schema) {
        return "number".equals(schema.getType()) || (schema.getTypes() != null && schema.getTypes().contains("number")) || "integer".equals(schema.getType()) || (schema.getTypes() != null && schema.getTypes().contains("integer"));
    }

    private AnnotatedMember invokeMethod(final BeanDescription beanDesc, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = BeanDescription.class.getMethod(methodName);
        return (AnnotatedMember) m.invoke(beanDesc);
    }

    protected Schema buildRefSchemaIfObject(Schema schema, ModelConverterContext context) {
        if (schema == null) {
            return null;
        }
        Schema result = schema;
        if (isObjectSchema(schema) && StringUtils.isNotBlank(schema.getName())) {
            if (context.getDefinedModels().containsKey(schema.getName())) {
                result = openapi31 ? new JsonSchema() : new Schema();
                result.$ref(constructRef(schema.getName()));
            }
        }
        return result;
    }

    protected boolean applySchemaResolution() {
        return !openapi31 ||
                (Boolean.parseBoolean(System.getProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY, "false")) ||
                        Boolean.parseBoolean(System.getenv(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY)));
    }
}
