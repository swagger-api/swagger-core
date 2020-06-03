package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AnnotationsUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(AnnotationsUtils.class);
    public static final String COMPONENTS_REF = "#/components/schemas/";

    public static boolean hasSchemaAnnotation(io.swagger.v3.oas.annotations.media.Schema schema) {
        if (schema == null) {
            return false;
        }
        if (StringUtils.isBlank(schema.type())
                && StringUtils.isBlank(schema.format())
                && StringUtils.isBlank(schema.title())
                && StringUtils.isBlank(schema.description())
                && StringUtils.isBlank(schema.ref())
                && StringUtils.isBlank(schema.name())
                && schema.multipleOf() == 0
                && StringUtils.isBlank(schema.maximum())
                && StringUtils.isBlank(schema.minimum())
                && !schema.exclusiveMinimum()
                && !schema.exclusiveMaximum()
                && schema.maxLength() == Integer.MAX_VALUE
                && schema.minLength() == 0
                && schema.minProperties() == 0
                && schema.maxProperties() == 0
                && schema.requiredProperties().length == 0
                && !schema.required()
                && !schema.nullable()
                && !schema.readOnly()
                && !schema.writeOnly()
                && schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO)
                && !schema.deprecated()
                && schema.allowableValues().length == 0
                && StringUtils.isBlank(schema.defaultValue())
                && schema.implementation().equals(Void.class)
                && StringUtils.isBlank(schema.example())
                && StringUtils.isBlank(schema.pattern())
                && schema.not().equals(Void.class)
                && schema.allOf().length == 0
                && schema.oneOf().length == 0
                && schema.anyOf().length == 0
                && schema.subTypes().length == 0
                && !getExternalDocumentation(schema.externalDocs()).isPresent()
                && StringUtils.isBlank(schema.discriminatorProperty())
                && schema.discriminatorMapping().length == 0
                && schema.extensions().length == 0
                && !schema.hidden()
                && !schema.enumAsRef()
                ) {
            return false;
        }
        return true;
    }

    public static boolean equals(Annotation thisAnnotation, Annotation thatAnnotation) {
        if (thisAnnotation == null && thatAnnotation == null) {
            return true;
        }
        else if (thisAnnotation == null || thatAnnotation == null) {
            return false;
        }
        if (!thisAnnotation.annotationType().equals(thatAnnotation.annotationType())) {
            return false;
        }
        if (thisAnnotation instanceof io.swagger.v3.oas.annotations.media.Schema) {
            return equals((io.swagger.v3.oas.annotations.media.Schema) thisAnnotation, (io.swagger.v3.oas.annotations.media.Schema) thatAnnotation);
        } else if (thisAnnotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema) {
            return equals((io.swagger.v3.oas.annotations.media.ArraySchema)thisAnnotation, (io.swagger.v3.oas.annotations.media.ArraySchema)thatAnnotation);
        }
        return true;
    }

    public static boolean equals(io.swagger.v3.oas.annotations.media.ArraySchema thisArraySchema, io.swagger.v3.oas.annotations.media.ArraySchema thatArraySchema) {
        if (thisArraySchema == null && thatArraySchema == null) {
            return true;
        }
        else if (thisArraySchema == null || thatArraySchema == null) {
            return false;
        }

        if (thisArraySchema.maxItems() != thatArraySchema.maxItems()) {
            return false;
        }
        if (thisArraySchema.minItems() != thatArraySchema.minItems()) {
            return false;
        }
        if (thisArraySchema.uniqueItems() != thatArraySchema.uniqueItems()) {
            return false;
        }

        if (!Arrays.equals(thisArraySchema.extensions(), thatArraySchema.extensions())) {
            return false;
        }

        if (!equals(thisArraySchema.schema(), thatArraySchema.schema())) {
            return false;
        }
        return true;
    }

    public static boolean equals(io.swagger.v3.oas.annotations.media.Schema thisSchema, io.swagger.v3.oas.annotations.media.Schema thatSchema) {
        if (thisSchema == null && thatSchema == null) {
            return true;
        }
        else if (thisSchema == null || thatSchema == null) {
            return false;
        }

        if (!StringUtils.equals(thisSchema.type(), thatSchema.type())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.format(), thatSchema.format())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.title(), thatSchema.title())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.description(), thatSchema.description())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.ref(), thatSchema.ref())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.name(), thatSchema.name())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.defaultValue(), thatSchema.defaultValue())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.maximum(), thatSchema.maximum())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.minimum(), thatSchema.minimum())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.example(), thatSchema.example())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.pattern(), thatSchema.pattern())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.discriminatorProperty(), thatSchema.discriminatorProperty())) {
            return false;
        }

        if (thisSchema.multipleOf() != thatSchema.multipleOf()) {
            return false;
        }
        if (thisSchema.minLength() != thatSchema.minLength()) {
            return false;
        }
        if (thisSchema.minProperties() != thatSchema.minProperties()) {
            return false;
        }
        if (thisSchema.maxProperties() != thatSchema.maxProperties()) {
            return false;
        }
        if (thisSchema.maxLength() != thatSchema.maxLength()) {
            return false;
        }

        if (!Arrays.equals(thisSchema.allOf(), thatSchema.allOf())) {
            return false;
        }
        if (!Arrays.equals(thisSchema.oneOf(), thatSchema.oneOf())) {
            return false;
        }
        if (!Arrays.equals(thisSchema.anyOf(), thatSchema.anyOf())) {
            return false;
        }
        if (!Arrays.equals(thisSchema.subTypes(), thatSchema.subTypes())) {
            return false;
        }
        if (!Arrays.equals(thisSchema.discriminatorMapping(), thatSchema.discriminatorMapping())) {
            return false;
        }
        if (!Arrays.equals(thisSchema.extensions(), thatSchema.extensions())) {
            return false;
        }
        if (!Arrays.equals(thisSchema.allowableValues(), thatSchema.allowableValues())) {
            return false;
        }
        if (!Arrays.equals(thisSchema.requiredProperties(), thatSchema.requiredProperties())) {
            return false;
        }

        if (thisSchema.exclusiveMinimum() != thatSchema.exclusiveMinimum()) {
            return false;
        }
        if (thisSchema.exclusiveMaximum() != thatSchema.exclusiveMaximum()) {
            return false;
        }
        if (thisSchema.required() != thatSchema.required()) {
            return false;
        }
        if (thisSchema.nullable() != thatSchema.nullable()) {
            return false;
        }
        if (thisSchema.readOnly() != thatSchema.readOnly()) {
            return false;
        }
        if (thisSchema.writeOnly() != thatSchema.writeOnly()) {
            return false;
        }
        if (!thisSchema.accessMode().equals(thatSchema.accessMode())) {
            return false;
        }
        if (thisSchema.deprecated() != thatSchema.deprecated()) {
            return false;
        }
        if (thisSchema.hidden() != thatSchema.hidden()) {
            return false;
        }
        if (thisSchema.enumAsRef() != thatSchema.enumAsRef()) {
            return false;
        }
        if (!thisSchema.implementation().equals(thatSchema.implementation())) {
            return false;
        }
        if (!thisSchema.not().equals(thatSchema.not())) {
            return false;
        }

        if (!StringUtils.equals(thisSchema.externalDocs().description(), thatSchema.externalDocs().description())) {
            return false;
        }
        if (!StringUtils.equals(thisSchema.externalDocs().url(), thatSchema.externalDocs().url())) {
            return false;
        }
        if (thisSchema.externalDocs().extensions().length !=  thatSchema.externalDocs().extensions().length) {
            return false;
        }
        if (!Arrays.equals(thisSchema.extensions(), thatSchema.extensions())) {
            return false;
        }
        return true;
    }


    public static boolean hasArrayAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema array) {
        if (array == null) {
            return false;
        }
        if (array.uniqueItems() == false
                && array.maxItems() == Integer.MIN_VALUE
                && array.minItems() == Integer.MAX_VALUE
                && !hasSchemaAnnotation(array.schema())
                && !hasSchemaAnnotation(array.arraySchema())
                ) {
            return false;
        }
        return true;
    }

    public static Optional<Example> getExample(ExampleObject example) {
        return getExample(example, false);
    }

    public static Optional<Example> getExample(ExampleObject example, boolean ignoreName) {
        if (example == null) {
            return Optional.empty();
        }
        Example exampleObject = new Example();
        if (!ignoreName && StringUtils.isNotBlank(example.name())) {

            if (StringUtils.isNotBlank(example.name())) {
                exampleObject.setDescription(example.name());
            }
            resolveExample(exampleObject, example);

            return Optional.of(exampleObject);
        } else if (ignoreName){
            if (resolveExample(exampleObject, example)) {
                return Optional.of(exampleObject);
            }
        }
        return Optional.empty();
    }

    private static boolean resolveExample(Example exampleObject, ExampleObject example) {

        boolean isEmpty = true;
        if (StringUtils.isNotBlank(example.summary())) {
            isEmpty = false;
            exampleObject.setSummary(example.summary());
        }

        if (StringUtils.isNotBlank(example.description())) {
            isEmpty = false;
            exampleObject.setDescription(example.description());
        }

        if (StringUtils.isNotBlank(example.externalValue())) {
            isEmpty = false;
            exampleObject.setExternalValue(example.externalValue());
        }
        if (StringUtils.isNotBlank(example.value())) {
            isEmpty = false;
            try {
                ObjectMapper mapper = ObjectMapperFactory.buildStrictGenericObjectMapper();
                exampleObject.setValue(mapper.readTree(example.value()));
            } catch (IOException e) {
                exampleObject.setValue(example.value());
            }
        }
        if (StringUtils.isNotBlank(example.ref())) {
            isEmpty = false;
            exampleObject.set$ref(example.ref());
        }
        if (example.extensions().length > 0) {
            isEmpty = false;
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(example.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    exampleObject.addExtension(ext, extensions.get(ext));
                }
            }
        }
        return !isEmpty;
    }

    public static Optional<ArraySchema> getArraySchema(io.swagger.v3.oas.annotations.media.ArraySchema arraySchema, JsonView jsonViewAnnotation) {
        return getArraySchema(arraySchema, null, jsonViewAnnotation);
    }
    public static Optional<ArraySchema> getArraySchema(io.swagger.v3.oas.annotations.media.ArraySchema arraySchema, Components components, JsonView jsonViewAnnotation) {
        if (arraySchema == null || !hasArrayAnnotation(arraySchema)) {
            return Optional.empty();
        }
        ArraySchema arraySchemaObject = new ArraySchema();
        if (arraySchema.uniqueItems()) {
            arraySchemaObject.setUniqueItems(arraySchema.uniqueItems());
        }
        if (arraySchema.maxItems() > 0) {
            arraySchemaObject.setMaxItems(arraySchema.maxItems());
        }

        if (arraySchema.minItems() < Integer.MAX_VALUE) {
            arraySchemaObject.setMinItems(arraySchema.minItems());
        }
        if (arraySchema.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(arraySchema.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    arraySchemaObject.addExtension(ext, extensions.get(ext));
                }
            }
        }

        if (arraySchema.schema() != null) {
            if (arraySchema.schema().implementation().equals(Void.class)) {
                getSchemaFromAnnotation(arraySchema.schema(), components, jsonViewAnnotation).ifPresent(schema -> {
                    arraySchemaObject.setItems(schema);
                });
            } // if present, schema implementation handled upstream
        }

        return Optional.of(arraySchemaObject);
    }

    public static Optional<Schema> getSchemaFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schema, JsonView jsonViewAnnotation) {
        return getSchemaFromAnnotation(schema, null, jsonViewAnnotation);
    }
    public static Optional<Schema> getSchemaFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schema, Components components, JsonView jsonViewAnnotation) {
        if (schema == null || !hasSchemaAnnotation(schema)) {
            return Optional.empty();
        }
        Schema schemaObject = null;
        if (schema.oneOf().length > 0 ||
            schema.allOf().length > 0 ||
            schema.anyOf().length > 0) {
            schemaObject = new ComposedSchema();
        } else {
            schemaObject = new Schema();
        }
        if (StringUtils.isNotBlank(schema.description())) {
            schemaObject.setDescription(schema.description());
        }
        if (StringUtils.isNotBlank(schema.ref())) {
            schemaObject.set$ref(schema.ref());
        }
        if (StringUtils.isNotBlank(schema.type())) {
            schemaObject.setType(schema.type());
        }
        if (StringUtils.isNotBlank(schema.defaultValue())) {
            schemaObject.setDefault(schema.defaultValue());
        }
        if (StringUtils.isNotBlank(schema.example())) {
            try {
                schemaObject.setExample(Json.mapper().readTree(schema.example()));
            } catch (IOException e) {
                schemaObject.setExample(schema.example());
            }
        }
        if (StringUtils.isNotBlank(schema.format())) {
            schemaObject.setFormat(schema.format());
        }
        if (StringUtils.isNotBlank(schema.pattern())) {
            schemaObject.setPattern(schema.pattern());
        }
        if (schema.readOnly()) {
            schemaObject.setReadOnly(schema.readOnly());
        }
        if (schema.deprecated()) {
            schemaObject.setDeprecated(schema.deprecated());
        }
        if (schema.exclusiveMaximum()) {
            schemaObject.setExclusiveMaximum(schema.exclusiveMaximum());
        }
        if (schema.exclusiveMinimum()) {
            schemaObject.setExclusiveMinimum(schema.exclusiveMinimum());
        }
        if (schema.maxProperties() > 0) {
            schemaObject.setMaxProperties(schema.maxProperties());
        }
        if (schema.maxLength() != Integer.MAX_VALUE && schema.maxLength() > 0) {
            schemaObject.setMaxLength(schema.maxLength());
        }
        if (schema.minProperties() > 0) {
            schemaObject.setMinProperties(schema.minProperties());
        }
        if (schema.minLength() > 0) {
            schemaObject.setMinLength(schema.minLength());
        }
        if (schema.multipleOf() != 0) {
            schemaObject.setMultipleOf(new BigDecimal(schema.multipleOf()));
        }
        if (NumberUtils.isNumber(schema.maximum())) {
            String filteredMaximum = schema.maximum().replaceAll(Constants.COMMA, StringUtils.EMPTY);
            schemaObject.setMaximum(new BigDecimal(filteredMaximum));
        }
        if (NumberUtils.isNumber(schema.minimum())) {
            String filteredMinimum = schema.minimum().replaceAll(Constants.COMMA, StringUtils.EMPTY);
            schemaObject.setMinimum(new BigDecimal(filteredMinimum));
        }
        if (schema.nullable()) {
            schemaObject.setNullable(schema.nullable());
        }
        if (StringUtils.isNotBlank(schema.title())) {
            schemaObject.setTitle(schema.title());
        }
        if (schema.writeOnly()) {
            schemaObject.setWriteOnly(schema.writeOnly());
        }
        // process after readOnly and writeOnly
        if (schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY)) {
            schemaObject.setReadOnly(true);
            schemaObject.setWriteOnly(null);
        } else if (schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY)) {
            schemaObject.setReadOnly(false);
            schemaObject.setWriteOnly(null);
        } else if (schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE)) {
            schemaObject.setReadOnly(null);
            schemaObject.setWriteOnly(null);
        }
        if (schema.requiredProperties().length > 0) {
            schemaObject.setRequired(Arrays.asList(schema.requiredProperties()));
        }
        if (schema.allowableValues().length > 0) {
            schemaObject.setEnum(Arrays.asList(schema.allowableValues()));
        }

        if (schema.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(schema.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    schemaObject.addExtension(ext, extensions.get(ext));
                }
            }
        }

        getExternalDocumentation(schema.externalDocs()).ifPresent(schemaObject::setExternalDocs);

        if (!schema.not().equals(Void.class)) {
            Class<?> schemaImplementation = schema.not();
            Schema notSchemaObject = resolveSchemaFromType(schemaImplementation, components, jsonViewAnnotation);
            schemaObject.setNot(notSchemaObject);
        }
        if (schema.oneOf().length > 0) {
            Class<?>[] schemaImplementations = schema.oneOf();
            for (Class<?> schemaImplementation : schemaImplementations) {
                Schema oneOfSchemaObject = resolveSchemaFromType(schemaImplementation, components, jsonViewAnnotation);
                ((ComposedSchema) schemaObject).addOneOfItem(oneOfSchemaObject);
            }
        }
        if (schema.anyOf().length > 0) {
            Class<?>[] schemaImplementations = schema.anyOf();
            for (Class<?> schemaImplementation : schemaImplementations) {
                Schema anyOfSchemaObject = resolveSchemaFromType(schemaImplementation, components, jsonViewAnnotation);
                ((ComposedSchema) schemaObject).addAnyOfItem(anyOfSchemaObject);
            }
        }
        if (schema.allOf().length > 0) {
            Class<?>[] schemaImplementations = schema.allOf();
            for (Class<?> schemaImplementation : schemaImplementations) {
                Schema allOfSchemaObject = resolveSchemaFromType(schemaImplementation, components, jsonViewAnnotation);
                ((ComposedSchema) schemaObject).addAllOfItem(allOfSchemaObject);
            }
        }


        return Optional.of(schemaObject);
    }

    public static Schema resolveSchemaFromType(Class<?> schemaImplementation, Components components, JsonView jsonViewAnnotation) {
        Schema schemaObject;
        PrimitiveType primitiveType = PrimitiveType.fromType(schemaImplementation);
        if (primitiveType != null) {
            schemaObject = primitiveType.createProperty();
        } else {
            schemaObject = new Schema();
            ResolvedSchema resolvedSchema = ModelConverters.getInstance().readAllAsResolvedSchema(new AnnotatedType().type(schemaImplementation).jsonViewAnnotation(jsonViewAnnotation));
            Map<String, Schema> schemaMap;
            if (resolvedSchema != null) {
                schemaMap = resolvedSchema.referencedSchemas;
                schemaMap.forEach((key, referencedSchema) -> {
                    if (components != null) {
                        components.addSchemas(key, referencedSchema);
                    }
                });
                if (StringUtils.isNotBlank(resolvedSchema.schema.getName())) {
                    schemaObject.set$ref(COMPONENTS_REF + resolvedSchema.schema.getName());
                } else {
                    schemaObject = resolvedSchema.schema;
                }
            }
        }
        if (StringUtils.isBlank(schemaObject.get$ref()) && StringUtils.isBlank(schemaObject.getType())) {
            // default to string
            schemaObject.setType("string");
        }
        return schemaObject;
    }

    public static Optional<Set<Tag>> getTags(io.swagger.v3.oas.annotations.tags.Tag[] tags, boolean skipOnlyName) {
        if (tags == null) {
            return Optional.empty();
        }
        Set<Tag> tagsList = new LinkedHashSet<>();
        for (io.swagger.v3.oas.annotations.tags.Tag tag : tags) {
            if (StringUtils.isBlank(tag.name())) {
                continue;
            }
            if (skipOnlyName &&
                    StringUtils.isBlank(tag.description()) &&
                    StringUtils.isBlank(tag.externalDocs().description()) &&
                    StringUtils.isBlank(tag.externalDocs().url())) {
                continue;
            }
            Tag tagObject = new Tag();
            if (StringUtils.isNotBlank(tag.description())) {
                tagObject.setDescription(tag.description());
            }
            tagObject.setName(tag.name());
            getExternalDocumentation(tag.externalDocs()).ifPresent(tagObject::setExternalDocs);
            if (tag.extensions().length > 0) {
                Map<String, Object> extensions = AnnotationsUtils.getExtensions(tag.extensions());
                if (extensions != null) {
                    for (String ext : extensions.keySet()) {
                        tagObject.addExtension(ext, extensions.get(ext));
                    }
                }
            }
            tagsList.add(tagObject);
        }
        if (tagsList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tagsList);
    }

    public static Optional<List<Server>> getServers(io.swagger.v3.oas.annotations.servers.Server[] servers) {
        if (servers == null) {
            return Optional.empty();
        }
        List<Server> serverObjects = new ArrayList<>();
        for (io.swagger.v3.oas.annotations.servers.Server server : servers) {
            getServer(server).ifPresent(serverObjects::add);
        }
        if (serverObjects.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(serverObjects);
    }

    public static Optional<Server> getServer(io.swagger.v3.oas.annotations.servers.Server server) {
        if (server == null) {
            return Optional.empty();
        }

        Server serverObject = new Server();
        boolean isEmpty = true;
        if (StringUtils.isNotBlank(server.url())) {
            serverObject.setUrl(server.url());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(server.description())) {
            serverObject.setDescription(server.description());
            isEmpty = false;
        }
        if (server.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(server.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    serverObject.addExtension(ext, extensions.get(ext));
                }
            }
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }
        io.swagger.v3.oas.annotations.servers.ServerVariable[] serverVariables = server.variables();
        ServerVariables serverVariablesObject = new ServerVariables();
        for (io.swagger.v3.oas.annotations.servers.ServerVariable serverVariable : serverVariables) {
            ServerVariable serverVariableObject = new ServerVariable();
            if (StringUtils.isNotBlank(serverVariable.description())) {
                serverVariableObject.setDescription(serverVariable.description());
            }
            if (StringUtils.isNotBlank(serverVariable.defaultValue())) {
                serverVariableObject.setDefault(serverVariable.defaultValue());
            }
            if (serverVariable.allowableValues() != null && serverVariable.allowableValues().length > 0) {
                if (StringUtils.isNotBlank(serverVariable.allowableValues()[0])) {
                    serverVariableObject.setEnum(Arrays.asList(serverVariable.allowableValues()));
                }
            }
            if (serverVariable.extensions() != null && serverVariable.extensions().length > 0) {
                Map<String, Object> extensions = AnnotationsUtils.getExtensions(serverVariable.extensions());
                if (extensions != null) {
                    for (String ext : extensions.keySet()) {
                        serverVariableObject.addExtension(ext, extensions.get(ext));
                    }
                }
            }
            serverVariablesObject.addServerVariable(serverVariable.name(), serverVariableObject);
        }
        serverObject.setVariables(serverVariablesObject);

        return Optional.of(serverObject);
    }

    public static Optional<ExternalDocumentation> getExternalDocumentation(io.swagger.v3.oas.annotations.ExternalDocumentation externalDocumentation) {
        if (externalDocumentation == null) {
            return Optional.empty();
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
        if (externalDocumentation.extensions() != null && externalDocumentation.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(externalDocumentation.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    external.addExtension(ext, extensions.get(ext));
                }
                isEmpty = false;
            }
        }

        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(external);
    }

    public static Optional<Info> getInfo(io.swagger.v3.oas.annotations.info.Info info) {
        if (info == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        Info infoObject = new Info();
        if (StringUtils.isNotBlank(info.description())) {
            infoObject.setDescription(info.description());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(info.termsOfService())) {
            infoObject.setTermsOfService(info.termsOfService());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(info.title())) {
            infoObject.setTitle(info.title());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(info.version())) {
            infoObject.setVersion(info.version());
            isEmpty = false;
        }
        if (info.extensions() != null && info.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(info.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    infoObject.addExtension(ext, extensions.get(ext));
                }
                isEmpty = false;
            }
        }
        if (isEmpty) {
            return Optional.empty();
        }
        getContact(info.contact()).ifPresent(infoObject::setContact);
        getLicense(info.license()).ifPresent(infoObject::setLicense);

        return Optional.of(infoObject);
    }

    public static Optional<Contact> getContact(io.swagger.v3.oas.annotations.info.Contact contact) {
        if (contact == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        Contact contactObject = new Contact();
        if (StringUtils.isNotBlank(contact.email())) {
            contactObject.setEmail(contact.email());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(contact.name())) {
            contactObject.setName(contact.name());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(contact.url())) {
            contactObject.setUrl(contact.url());
            isEmpty = false;
        }
        if (contact.extensions() != null && contact.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(contact.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    contactObject.addExtension(ext, extensions.get(ext));
                }
                isEmpty = false;
            }
        }

        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(contactObject);
    }

    public static Optional<License> getLicense(io.swagger.v3.oas.annotations.info.License license) {
        if (license == null) {
            return Optional.empty();
        }
        License licenseObject = new License();
        boolean isEmpty = true;
        if (StringUtils.isNotBlank(license.name())) {
            licenseObject.setName(license.name());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(license.url())) {
            licenseObject.setUrl(license.url());
            isEmpty = false;
        }
        if (license.extensions() != null && license.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(license.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    licenseObject.addExtension(ext, extensions.get(ext));
                }
                isEmpty = false;
            }
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(licenseObject);
    }

    public static Map<String, Link> getLinks(io.swagger.v3.oas.annotations.links.Link[] links) {
        Map<String, Link> linkMap = new HashMap<>();
        if (links == null) {
            return linkMap;
        }
        for (io.swagger.v3.oas.annotations.links.Link link : links) {
            getLink(link).ifPresent(linkResult -> linkMap.put(link.name(), linkResult));
        }
        return linkMap;
    }

    public static Optional<Link> getLink(io.swagger.v3.oas.annotations.links.Link link) {
        if (link == null) {
            return Optional.empty();
        }
        boolean isEmpty = true;
        Link linkObject = new Link();
        if (StringUtils.isNotBlank(link.description())) {
            linkObject.setDescription(link.description());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(link.operationId())) {
            linkObject.setOperationId(link.operationId());
            isEmpty = false;
            if (StringUtils.isNotBlank(link.operationRef())) {
                LOGGER.debug("OperationId and OperatonRef are mutually exclusive, there must be only one setted");
            }
        } else {
            if (StringUtils.isNotBlank(link.operationRef())) {
                linkObject.setOperationRef(link.operationRef());
                isEmpty = false;
            }
        }
        if (StringUtils.isNotBlank(link.ref())) {
            linkObject.set$ref(link.ref());
            isEmpty = false;
        }
        if (link.extensions() != null && link.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(link.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    linkObject.addExtension(ext, extensions.get(ext));
                }
                isEmpty = false;
            }
        }
        if (isEmpty) {
            return Optional.empty();
        }
        Map<String, String> linkParameters = getLinkParameters(link.parameters());
        if (linkParameters.size() > 0) {
            linkObject.setParameters(linkParameters);
        }

        if (StringUtils.isNotBlank(link.requestBody())) {
            JsonNode processedValue = null;
            try {
                processedValue = Json.mapper().readTree(link.requestBody());
            } catch (Exception e) {
                // not a json string
            }
            if (processedValue == null) {
                linkObject.requestBody(link.requestBody());
            } else {
                linkObject.requestBody(processedValue);
            }
        }
        return Optional.of(linkObject);
    }

    public static Map<String, String> getLinkParameters(LinkParameter[]
                                                                linkParameter) {
        Map<String, String> linkParametersMap = new HashMap<>();
        if (linkParameter == null) {
            return linkParametersMap;
        }
        for (LinkParameter parameter : linkParameter) {
            if (StringUtils.isNotBlank(parameter.name())) {
                linkParametersMap.put(parameter.name(), parameter.expression());
            }
        }

        return linkParametersMap;
    }

    public static Optional<Map<String, Header>> getHeaders(io.swagger.v3.oas.annotations.headers.Header[] annotationHeaders, JsonView jsonViewAnnotation) {
        if (annotationHeaders == null) {
            return Optional.empty();
        }

        Map<String, Header> headers = new HashMap<>();
        for (io.swagger.v3.oas.annotations.headers.Header header : annotationHeaders) {
            getHeader(header, jsonViewAnnotation).ifPresent(headerResult -> headers.put(header.name(), headerResult));
        }

        if (headers.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(headers);
    }

    public static Optional<Header> getHeader(io.swagger.v3.oas.annotations.headers.Header header, JsonView jsonViewAnnotation) {

        if (header == null) {
            return Optional.empty();
        }

        Header headerObject = new Header();
        boolean isEmpty = !StringUtils.isNotBlank(header.name());
        if (StringUtils.isNotBlank(header.description())) {
            headerObject.setDescription(header.description());
            isEmpty = false;
        }
        if (StringUtils.isNotBlank(header.ref())) {
            headerObject.set$ref(header.ref());
            isEmpty = false;
        }
        if (header.deprecated()) {
            headerObject.setDeprecated(header.deprecated());
        }
        if (header.required()) {
            headerObject.setRequired(header.required());
            isEmpty = false;
        }
        headerObject.setStyle(Header.StyleEnum.SIMPLE);

        if (header.schema() != null) {
            if (header.schema().implementation().equals(Void.class)) {
                AnnotationsUtils.getSchemaFromAnnotation(header.schema(), jsonViewAnnotation).ifPresent(schema -> {
                    headerObject.setSchema(schema);
                    //schema inline no need to add to components
                    //components.addSchemas(schema.getType(), schema);
                });
            }
        }

        if (isEmpty) {
            return Optional.empty();
        }

        return Optional.of(headerObject);
    }

    public static void addEncodingToMediaType(MediaType mediaType, io.swagger.v3.oas.annotations.media.Encoding encoding, JsonView jsonViewAnnotation) {
        if (encoding == null) {
            return;
        }
        if (StringUtils.isNotBlank(encoding.name())) {

            Encoding encodingObject = new Encoding();

            if (StringUtils.isNotBlank(encoding.contentType())) {
                encodingObject.setContentType(encoding.contentType());
            }
            if (StringUtils.isNotBlank(encoding.style())) {
                encodingObject.setStyle(Encoding.StyleEnum.valueOf(encoding.style()));
            }
            if (encoding.explode()) {
                encodingObject.setExplode(encoding.explode());
            }
            if (encoding.allowReserved()) {
                encodingObject.setAllowReserved(encoding.allowReserved());
            }

            if (encoding.headers() != null) {
                getHeaders(encoding.headers(), jsonViewAnnotation).ifPresent(encodingObject::headers);
            }
            if (encoding.extensions() != null && encoding.extensions().length > 0) {
                Map<String, Object> extensions = AnnotationsUtils.getExtensions(encoding.extensions());
                if (extensions != null) {
                    for (String ext : extensions.keySet()) {
                        encodingObject.addExtension(ext, extensions.get(ext));
                    }
                }
            }

            mediaType.addEncoding(encoding.name(), encodingObject);
        }

    }

    public static Type getSchemaType(io.swagger.v3.oas.annotations.media.Schema schema) {
        return getSchemaType(schema, false);
    }

    public static Type getSchemaType(io.swagger.v3.oas.annotations.media.Schema schema, boolean nullIfNotFound) {
        if (schema == null) {
            if (nullIfNotFound) {
                return null;
            }
            return String.class;
        }
        String schemaType = schema.type();
        String schemaFormat = schema.format();
        Class schemaImplementation = schema.implementation();

        if (!schemaImplementation.equals(Void.class)) {
            return schemaImplementation;
        } else if (StringUtils.isBlank(schemaType)) {
            if (nullIfNotFound) {
                return null;
            }
            return String.class;
        }
        switch (schemaType) {
            case "number":
                if ("float".equals(schemaFormat)) {
                    return Float.class;
                } else if ("double".equals(schemaFormat)) {
                    return Double.class;
                } else {
                    return BigDecimal.class;
                }
            case "integer":
                if ("int32".equals(schemaFormat)) {
                    return Integer.class;
                } else {
                    return Long.class;
                }
            case "boolean":
                return Boolean.class;
            case "string":
                return String.class;
            default:
                if (nullIfNotFound) {
                    return null;
                }
                return String.class;
        }
    }

    public static Optional<Content> getContent(io.swagger.v3.oas.annotations.media.Content[] annotationContents, String[] classTypes, String[] methodTypes, Schema schema, Components components, JsonView jsonViewAnnotation) {
        if (annotationContents == null || annotationContents.length == 0) {
            return Optional.empty();
        }

        //Encapsulating Content model
        Content content = new Content();

        for (io.swagger.v3.oas.annotations.media.Content annotationContent : annotationContents) {
            MediaType mediaType = new MediaType();
            if (components != null) {
                getSchema(annotationContent, components, jsonViewAnnotation).ifPresent(mediaType::setSchema);
            } else {
                mediaType.setSchema(schema);
            }

            ExampleObject[] examples = annotationContent.examples();
            if (examples.length == 1 && StringUtils.isBlank(examples[0].name())) {
                getExample(examples[0], true).ifPresent(exampleObject -> mediaType.example(exampleObject.getValue()));
            } else {
                for (ExampleObject example : examples) {
                    getExample(example).ifPresent(exampleObject -> mediaType.addExamples(example.name(), exampleObject));
                }
            }
            if (annotationContent.extensions() != null && annotationContent.extensions().length > 0) {
                Map<String, Object> extensions = AnnotationsUtils.getExtensions(annotationContent.extensions());
                if (extensions != null) {
                    for (String ext : extensions.keySet()) {
                        mediaType.addExtension(ext, extensions.get(ext));
                    }
                }
            }

            io.swagger.v3.oas.annotations.media.Encoding[] encodings = annotationContent.encoding();
            for (io.swagger.v3.oas.annotations.media.Encoding encoding : encodings) {
                addEncodingToMediaType(mediaType, encoding, jsonViewAnnotation);
            }
            if (StringUtils.isNotBlank(annotationContent.mediaType())) {
                content.addMediaType(annotationContent.mediaType(), mediaType);
            } else {
                applyTypes(classTypes, methodTypes, content, mediaType);
            }
        }

        if (content.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(content);
    }

    public static Optional<? extends Schema> getSchema(io.swagger.v3.oas.annotations.media.Content annotationContent, Components components, JsonView jsonViewAnnotation) {
        Class<?> schemaImplementation = annotationContent.schema().implementation();
        boolean isArray = false;
        if (schemaImplementation == Void.class) {
            schemaImplementation = annotationContent.array().schema().implementation();
            if (schemaImplementation != Void.class) {
                isArray = true;
            }
        }
        return getSchema(annotationContent.schema(), annotationContent.array(), isArray, schemaImplementation, components, jsonViewAnnotation);
    }

    public static Optional<? extends Schema> getSchema(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation,
                                                       io.swagger.v3.oas.annotations.media.ArraySchema arrayAnnotation,
                                                       boolean isArray,
                                                       Class<?> schemaImplementation,
                                                       Components components,
                                                       JsonView jsonViewAnnotation) {
        if (schemaImplementation != Void.class) {
            Schema schemaObject = resolveSchemaFromType(schemaImplementation, components, jsonViewAnnotation);
            if (StringUtils.isNotBlank(schemaAnnotation.format())) {
               schemaObject.setFormat(schemaAnnotation.format());
            }
            if (isArray) {
                Optional<ArraySchema> arraySchema = AnnotationsUtils.getArraySchema(arrayAnnotation, components, jsonViewAnnotation);
                if (arraySchema.isPresent()) {
                    arraySchema.get().setItems(schemaObject);
                    return arraySchema;
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.of(schemaObject);
            }

        } else {
            Optional<Schema> schemaFromAnnotation = AnnotationsUtils.getSchemaFromAnnotation(schemaAnnotation, components, jsonViewAnnotation);
            if (schemaFromAnnotation.isPresent()) {
                if (StringUtils.isBlank(schemaFromAnnotation.get().get$ref()) && StringUtils.isBlank(schemaFromAnnotation.get().getType()) && !(schemaFromAnnotation.get() instanceof ComposedSchema)) {
                    // default to string
                    schemaFromAnnotation.get().setType("string");
                }
                return Optional.of(schemaFromAnnotation.get());
            } else {
                Optional<ArraySchema> arraySchemaFromAnnotation = AnnotationsUtils.getArraySchema(arrayAnnotation, components, jsonViewAnnotation);
                if (arraySchemaFromAnnotation.isPresent()) {
                    if (arraySchemaFromAnnotation.get().getItems() != null && StringUtils.isBlank(arraySchemaFromAnnotation.get().getItems().get$ref()) && StringUtils.isBlank(arraySchemaFromAnnotation.get().getItems().getType())) {
                        // default to string
                        arraySchemaFromAnnotation.get().getItems().setType("string");
                    }
                    return Optional.of(arraySchemaFromAnnotation.get());
                }
            }
        }
        return Optional.empty();
    }


    public static void applyTypes(String[] classTypes, String[] methodTypes, Content content, MediaType mediaType) {
        if (methodTypes != null && methodTypes.length > 0) {
            for (String value : methodTypes) {
                content.addMediaType(value, mediaType);
            }
        } else if (classTypes != null && classTypes.length > 0) {
            for (String value : classTypes) {
                content.addMediaType(value, mediaType);
            }
        } else {
            content.addMediaType(ParameterProcessor.MEDIA_TYPE, mediaType);
        }

    }

    public static io.swagger.v3.oas.annotations.media.Schema getSchemaAnnotation(Annotated a) {
        if (a == null) {
            return null;
        }
        io.swagger.v3.oas.annotations.media.ArraySchema arraySchema = a.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);
        if (arraySchema != null) {
            return arraySchema.schema();
        } else {
            return a.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        }
    }

    public static io.swagger.v3.oas.annotations.media.Schema getSchemaDeclaredAnnotation(Annotated a) {
        if (a == null) {
            return null;
        }
        io.swagger.v3.oas.annotations.media.ArraySchema arraySchema = a.getRawType().getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);
        if (arraySchema != null) {
            return arraySchema.schema();
        } else {
            return a.getRawType().getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        }
    }

    public static io.swagger.v3.oas.annotations.media.Schema getSchemaAnnotation(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        io.swagger.v3.oas.annotations.media.Schema mp = null;
        io.swagger.v3.oas.annotations.media.ArraySchema as = cls.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);
        if (as != null) {
            mp = as.schema();
        } else {
            mp = cls.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        }
        return mp;
    }

    public static io.swagger.v3.oas.annotations.media.Schema getSchemaDeclaredAnnotation(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        io.swagger.v3.oas.annotations.media.Schema mp = null;
        io.swagger.v3.oas.annotations.media.ArraySchema as = cls.getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);
        if (as != null) {
            mp = as.schema();
        } else {
            mp = cls.getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        }
        return mp;
    }

    public static Map<String, Object> getExtensions(Extension... extensions) {
        final Map<String, Object> map = new HashMap<>();
        for (Extension extension : extensions) {
            final String name = extension.name();
            final String key = name.length() > 0 ? StringUtils.prependIfMissing(name, "x-") : name;

            for (ExtensionProperty property : extension.properties()) {
                final String propertyName = property.name();
                final String propertyValue = property.value();
                JsonNode processedValue = null;
                final boolean propertyAsJson = property.parseValue();
                if (StringUtils.isNotBlank(propertyName) && StringUtils.isNotBlank(propertyValue)) {
                    if (key.isEmpty()) {
                        if (propertyAsJson) {
                            try {
                                processedValue = Json.mapper().readTree(propertyValue);
                                map.put(StringUtils.prependIfMissing(propertyName, "x-"), processedValue);
                            } catch (Exception e) {
                                map.put(StringUtils.prependIfMissing(propertyName, "x-"), propertyValue);
                            }
                        } else {
                            map.put(StringUtils.prependIfMissing(propertyName, "x-"), propertyValue);
                        }
                    } else {
                        Object value = map.get(key);
                        if (value == null || !(value instanceof Map)) {
                            value = new HashMap<String, Object>();
                            map.put(key, value);
                        }
                        @SuppressWarnings("unchecked") final Map<String, Object> mapValue = (Map<String, Object>) value;
                        if (propertyAsJson) {
                            try {
                                processedValue = Json.mapper().readTree(propertyValue);
                                mapValue.put(propertyName, processedValue);
                            } catch (Exception e) {
                                mapValue.put(propertyName, propertyValue);
                            }
                        } else {
                            mapValue.put(propertyName, propertyValue);
                        }
                    }
                }
            }
        }

        return map;
    }

    public static io.swagger.v3.oas.annotations.media.Schema getSchemaAnnotation(Annotation... annotations) {
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

    public static io.swagger.v3.oas.annotations.media.ArraySchema getArraySchemaAnnotation(Annotation... annotations) {
        if (annotations == null) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof io.swagger.v3.oas.annotations.media.ArraySchema) {
                return (io.swagger.v3.oas.annotations.media.ArraySchema) annotation;
            }
        }
        return null;
    }

    public static <T> T getAnnotation(Class<T> cls, Annotation... annotations) {
        if (annotations == null) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (cls.isAssignableFrom(annotation.getClass())) {
                return (T)annotation;
            }
        }
        return null;
    }

    public static Annotation[] removeAnnotations(Annotation[] annotations, Class ... classes) {
        if (annotations == null) {
            return null;
        }
        List<Annotation> result = new ArrayList<>();
        for (Annotation annotation : annotations) {
            boolean found = false;
            for (Class cls : classes) {
                if (cls.isAssignableFrom(annotation.getClass())) {
                    found = true;
                }
            }
            if (!found) {
                result.add(annotation);
            }
        }
        return result.toArray(new Annotation[result.size()]);
    }


    public static void updateAnnotation(Class<?> clazz, io.swagger.v3.oas.annotations.media.Schema newAnnotation) {
        try {
            Field field = Class.class.getDeclaredField("annotations");
            field.setAccessible(true);
            Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) field.get(clazz);
            annotations.put(io.swagger.v3.oas.annotations.media.Schema.class, newAnnotation);
        } catch (NoSuchFieldException e) {
            //
        } catch (IllegalAccessException e) {
            //
        }

    }

    /*
     * returns null if no annotations, otherwise either ArraySchema or Schema
     */
    public static Annotation mergeSchemaAnnotations(
            Annotation[] ctxAnnotations, JavaType type) {
        // get type array and schema
        io.swagger.v3.oas.annotations.media.Schema tS = type.getRawClass().getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        if (!hasSchemaAnnotation(tS)) {
            tS = null;
        }
        io.swagger.v3.oas.annotations.media.ArraySchema tA = type.getRawClass().getDeclaredAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);
        if (!hasArrayAnnotation(tA)) {
            tA = null;
        }

        io.swagger.v3.oas.annotations.media.Schema tAs = tA == null ? null : tA.schema();
        if (!hasSchemaAnnotation(tAs)) {
            tAs = null;
        }
        // get ctx array and schema
        io.swagger.v3.oas.annotations.media.Schema cS = getSchemaAnnotation(ctxAnnotations);
        if (!hasSchemaAnnotation(cS)) {
            cS = null;
        }
        io.swagger.v3.oas.annotations.media.ArraySchema cA = getArraySchemaAnnotation(ctxAnnotations);
        if (!hasArrayAnnotation(cA)) {
            cA = null;
        }
        io.swagger.v3.oas.annotations.media.Schema cAs = cA == null ? null : cA.schema();
        if (!hasSchemaAnnotation(cAs)) {
            cAs = null;
        }

        if (tS == null && tA == null && cS == null && cA == null) {
            return null;
        }

        else if (tS == null && tA == null && cS != null) {
            return cS;
        }

        else if (tS == null && tA == null && cS == null && cA != null) {
            return cA;
        }

        else if (tS == null && tA != null && cS == null && cA == null) {
            return tA;
        }

        else if (tS == null && tA != null && cS != null && cA == null) {
            if (tAs != null) {
                return tA;
            }
            return mergeArrayWithSchemaAnnotation(tA, cS);
        }

        else if (tS != null && tA == null && cS == null && cA != null) {
            if (cAs != null) {
                return cA;
            }
            return mergeArrayWithSchemaAnnotation(cA, tS);
        }

        else if (tA != null && cA != null) {
            return mergeArraySchemaAnnotations(cA, tA);
        }

        else if (tS != null && cS == null && cA == null) {
            return tS;
        }

        else if (tS != null && cS != null) {
            //return mergeSchemaAnnotations(cS, tS);
            return mergeSchemaAnnotations(tS, cS);
        }

        else {
            return tS;
        }


    }
    public static io.swagger.v3.oas.annotations.media.Schema mergeSchemaAnnotations(
            io.swagger.v3.oas.annotations.media.Schema master,
            io.swagger.v3.oas.annotations.media.Schema patch) {
        if (master == null) {
            return patch;
        } else if (patch == null) {
            return master;
        } else if (!hasSchemaAnnotation(patch)) {
            return master;
        }
        Annotation schema = new io.swagger.v3.oas.annotations.media.Schema() {
            @Override
            public Class<?> implementation() {
                if (!master.implementation().equals(Void.class) || patch.implementation().equals(Void.class)) {
                    return master.implementation();
                }
                return patch.implementation();
            }

            @Override
            public Class<?> not() {
                if (!master.not().equals(Void.class) || patch.not().equals(Void.class)) {
                    return master.not();
                }
                return patch.not();
            }

            @Override
            public Class<?>[] oneOf() {
                if (master.oneOf().length > 0 || patch.oneOf().length == 0) {
                    return master.oneOf();
                }
                return patch.oneOf();
            }

            @Override
            public Class<?>[] anyOf() {
                if (master.anyOf().length > 0 || patch.anyOf().length == 0) {
                    return master.anyOf();
                }
                return patch.anyOf();
            }

            @Override
            public Class<?>[] allOf() {
                if (master.allOf().length > 0 || patch.allOf().length == 0) {
                    return master.allOf();
                }
                return patch.allOf();
            }

            @Override
            public String name() {
                if (StringUtils.isNotBlank(master.name()) || StringUtils.isBlank(patch.name())) {
                    return master.name();
                }
                return patch.name();
            }

            @Override
            public String title() {
                if (StringUtils.isNotBlank(master.title()) || StringUtils.isBlank(patch.title())) {
                    return master.title();
                }
                return patch.title();
            }

            @Override
            public double multipleOf() {
                if (master.multipleOf() != 0 || patch.multipleOf() == 0) {
                    return master.multipleOf();
                }
                return patch.multipleOf();

            }

            @Override
            public String maximum() {
                if (StringUtils.isNotBlank(master.maximum()) || StringUtils.isBlank(patch.maximum())) {
                    return master.maximum();
                }
                return patch.maximum();
            }

            @Override
            public boolean exclusiveMaximum() {
                if (master.exclusiveMaximum() || !patch.exclusiveMaximum()) {
                    return master.exclusiveMaximum();
                }
                return patch.exclusiveMaximum();
            }

            @Override
            public String minimum() {
                if (StringUtils.isNotBlank(master.minimum()) || StringUtils.isBlank(patch.minimum())) {
                    return master.minimum();
                }
                return patch.minimum();
            }

            @Override
            public boolean exclusiveMinimum() {
                if (master.exclusiveMinimum() || !patch.exclusiveMinimum()) {
                    return master.exclusiveMinimum();
                }
                return patch.exclusiveMinimum();
            }

            @Override
            public int maxLength() {
                if ((   master.maxLength() != Integer.MAX_VALUE && master.maxLength() > 0) ||
                        (patch.maxLength() == Integer.MAX_VALUE || patch.maxLength() == 0)   ) {
                    return master.maxLength();
                }
                return patch.maxLength();
            }

            @Override
            public int minLength() {
                if (master.minLength() != 0 || patch.minLength() == 0) {
                    return master.minLength();
                }
                return patch.minLength();
            }

            @Override
            public String pattern() {
                if (StringUtils.isNotBlank(master.pattern()) || StringUtils.isBlank(patch.pattern())) {
                    return master.pattern();
                }
                return patch.pattern();
            }

            @Override
            public int maxProperties() {
                if (master.maxProperties() != 0 || patch.maxProperties() == 0) {
                    return master.maxProperties();
                }
                return patch.maxProperties();
            }

            @Override
            public int minProperties() {
                if (master.minProperties() != 0 || patch.minProperties() == 0) {
                    return master.minProperties();
                }
                return patch.minProperties();
            }

            @Override
            public String[] requiredProperties() {
                if (master.requiredProperties().length > 0 || patch.requiredProperties().length == 0) {
                    return master.requiredProperties();
                }
                return patch.requiredProperties();
            }

            @Override
            public boolean required() {
                if (master.required() || !patch.required()) {
                    return master.required();
                }
                return patch.required();
            }

            @Override
            public String description() {
                if (StringUtils.isNotBlank(master.description()) || StringUtils.isBlank(patch.description())) {
                    return master.description();
                }
                return patch.description();
            }

            @Override
            public String format() {
                if (StringUtils.isNotBlank(master.format()) || StringUtils.isBlank(patch.format())) {
                    return master.format();
                }
                return patch.format();
            }

            @Override
            public String ref() {
                if (StringUtils.isNotBlank(master.ref()) || StringUtils.isBlank(patch.ref())) {
                    return master.ref();
                }
                return patch.ref();
            }

            @Override
            public boolean nullable() {
                if (master.nullable() || !patch.nullable()) {
                    return master.nullable();
                }
                return patch.nullable();
            }

            @Override
            public boolean readOnly() {
                if (master.readOnly() || !patch.readOnly()) {
                    return master.readOnly();
                }
                return patch.readOnly();
            }

            @Override
            public boolean writeOnly() {
                if (master.writeOnly() || !patch.writeOnly()) {
                    return master.writeOnly();
                }
                return patch.writeOnly();
            }

            @Override
            public AccessMode accessMode() {
                if (!master.accessMode().equals(AccessMode.AUTO) || patch.accessMode().equals(AccessMode.AUTO)) {
                    return master.accessMode();
                }
                return patch.accessMode();
            }

            @Override
            public String example() {
                if (StringUtils.isNotBlank(master.example()) || StringUtils.isBlank(patch.example())) {
                    return master.example();
                }
                return patch.example();
            }

            @Override
            public io.swagger.v3.oas.annotations.ExternalDocumentation externalDocs() {
                if (getExternalDocumentation(master.externalDocs()).isPresent() || !getExternalDocumentation(patch.externalDocs()).isPresent()) {
                    return master.externalDocs();
                }
                return patch.externalDocs();
            }

            @Override
            public boolean deprecated() {
                if (master.deprecated() || !patch.deprecated()) {
                    return master.deprecated();
                }
                return patch.deprecated();
            }

            @Override
            public String type() {
                if (StringUtils.isNotBlank(master.type()) || StringUtils.isBlank(patch.type())) {
                    return master.type();
                }
                return patch.type();
            }

            @Override
            public String[] allowableValues() {
                if (master.allowableValues().length > 0 || patch.allowableValues().length == 0) {
                    return master.allowableValues();
                }
                return patch.allowableValues();
            }

            @Override
            public String defaultValue() {
                if (StringUtils.isNotBlank(master.defaultValue()) || StringUtils.isBlank(patch.defaultValue())) {
                    return master.defaultValue();
                }
                return patch.defaultValue();
            }

            @Override
            public String discriminatorProperty() {
                if (StringUtils.isNotBlank(master.discriminatorProperty()) || StringUtils.isBlank(patch.discriminatorProperty())) {
                    return master.discriminatorProperty();
                }
                return patch.discriminatorProperty();
            }

            @Override
            public DiscriminatorMapping[] discriminatorMapping() {
                if (master.discriminatorMapping().length > 0 || patch.discriminatorMapping().length == 0) {
                    return master.discriminatorMapping();
                }
                return patch.discriminatorMapping();
            }

            @Override
            public boolean hidden() {
                if (master.hidden() || !patch.hidden()) {
                    return master.hidden();
                }
                return patch.hidden();
            }

            @Override
            public boolean enumAsRef() {
                if (master.enumAsRef() || !patch.enumAsRef()) {
                    return master.enumAsRef();
                }
                return patch.enumAsRef();
            }

            @Override
            public Class<?>[] subTypes() {
                if (master.subTypes().length > 0 || patch.subTypes().length == 0) {
                    return master.subTypes();
                }
                return patch.subTypes();
            }

            @Override
            public Extension[] extensions() {
                if (master.extensions().length > 0 || patch.extensions().length == 0) {
                    return master.extensions();
                }
                return patch.extensions();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return io.swagger.v3.oas.annotations.media.Schema.class;
            }
        };

        return (io.swagger.v3.oas.annotations.media.Schema)schema;
    }

    public static io.swagger.v3.oas.annotations.media.ArraySchema mergeArraySchemaAnnotations(
            io.swagger.v3.oas.annotations.media.ArraySchema master,
            io.swagger.v3.oas.annotations.media.ArraySchema patch) {
        if (master == null) {
            return patch;
        } else if (patch == null) {
            return master;
        } else if (!hasArrayAnnotation(patch)) {
            return master;
        }
        Annotation newArraySchema = new io.swagger.v3.oas.annotations.media.ArraySchema() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return io.swagger.v3.oas.annotations.media.ArraySchema.class;
            }

            @Override
            public io.swagger.v3.oas.annotations.media.Schema schema() {
                io.swagger.v3.oas.annotations.media.Schema patchSchema = patch.schema();
                if (!hasSchemaAnnotation(patchSchema)) {
                    patchSchema = null;
                }

                if (patchSchema == null) {
                    return master.schema();
                } else {
                    return mergeSchemaAnnotations(master.schema(), patch.schema());
                }
            }

            @Override
            public io.swagger.v3.oas.annotations.media.Schema arraySchema() {
                io.swagger.v3.oas.annotations.media.Schema patchSchema = patch.arraySchema();
                if (!hasSchemaAnnotation(patchSchema)) {
                    patchSchema = null;
                }

                if (patchSchema == null) {
                    return master.arraySchema();
                } else {
                    return mergeSchemaAnnotations(master.arraySchema(), patch.arraySchema());
                }
            }

            @Override
            public int maxItems() {
                if (master.maxItems() != 0 || patch.maxItems() == 0) {
                    return master.maxItems();
                }
                return patch.maxItems();
            }

            @Override
            public int minItems() {
                if (master.maxItems() != 0 || patch.maxItems() == 0) {
                    return master.maxItems();
                }
                return patch.maxItems();
            }

            @Override
            public boolean uniqueItems() {
                if (master.uniqueItems() || !patch.uniqueItems()) {
                    return master.uniqueItems();
                }
                return patch.uniqueItems();
            }

            @Override
            public Extension[] extensions() {
                if (master.extensions().length > 0 || patch.extensions().length == 0) {
                    return master.extensions();
                }
                return patch.extensions();
            }
        };

        return (io.swagger.v3.oas.annotations.media.ArraySchema)newArraySchema;
    }

    public static io.swagger.v3.oas.annotations.media.ArraySchema mergeArrayWithSchemaAnnotation(
            io.swagger.v3.oas.annotations.media.ArraySchema arraySchema,
            io.swagger.v3.oas.annotations.media.Schema schema) {
        if (arraySchema == null || schema == null) {
            return arraySchema;
        }
        if (!hasSchemaAnnotation(schema)) {
            return arraySchema;
        }
        Annotation newArraySchema = new io.swagger.v3.oas.annotations.media.ArraySchema() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return io.swagger.v3.oas.annotations.media.ArraySchema.class;
            }

            @Override
            public io.swagger.v3.oas.annotations.media.Schema schema() {
                return schema;
            }

            @Override
            public io.swagger.v3.oas.annotations.media.Schema arraySchema() {
                return arraySchema.arraySchema();
            }

            @Override
            public int maxItems() {
                return arraySchema.maxItems();
            }

            @Override
            public int minItems() {
                return arraySchema.minItems();
            }

            @Override
            public boolean uniqueItems() {
                return arraySchema.uniqueItems();
            }

            @Override
            public Extension[] extensions() {
                return arraySchema.extensions();
            }
        };

        return (io.swagger.v3.oas.annotations.media.ArraySchema)newArraySchema;
    }

}
