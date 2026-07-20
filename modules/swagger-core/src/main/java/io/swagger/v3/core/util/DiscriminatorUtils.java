package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.apache.commons.lang3.StringUtils;

import static io.swagger.v3.core.util.RefUtils.constructRef;

public class DiscriminatorUtils {

    private static final int SCHEMA_COMPONENT_PREFIX = "#/components/schemas/".length();
    private static final String TYPE_STRING = "string";

    public static void resolveDiscriminatorProperty(JavaType type,
                                                    ModelConverterContext context,
                                                    Schema model,
                                                    boolean openapi31) {
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
                    Schema discriminatorSchema = openapi31 ? new JsonSchema().typesItem(TYPE_STRING).name(typeInfoProp) : new StringSchema().name(typeInfoProp);
                    modelToUpdate.addProperties(typeInfoProp, discriminatorSchema);
                    if (modelToUpdate.getRequired() == null || !modelToUpdate.getRequired().contains(typeInfoProp)) {
                        modelToUpdate.addRequiredItem(typeInfoProp);
                    }
                }
            }
        }
    }

    public static Discriminator resolveDiscriminator(JavaType type, ModelConverterContext context) {
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
}
