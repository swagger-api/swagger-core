package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.v3.oas.models.media.ArbitrarySchema;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ModelDeserializer extends JsonDeserializer<Schema> {

    static Boolean useArbitrarySchema = false;
    static {
        if (System.getenv(Schema.USE_ARBITRARY_SCHEMA_PROPERTY) != null) {
            useArbitrarySchema = Boolean.parseBoolean(System.getenv(Schema.USE_ARBITRARY_SCHEMA_PROPERTY));
        } else if (System.getProperty(Schema.USE_ARBITRARY_SCHEMA_PROPERTY) != null) {
            useArbitrarySchema = Boolean.parseBoolean(System.getProperty(Schema.USE_ARBITRARY_SCHEMA_PROPERTY));
        }
    }

    protected boolean openapi31 = false;
    @Override
    public Schema deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        Schema schema = null;

        if (openapi31) {
            schema = deserializeJsonSchema(node);
            return schema;
        }
        if (node.isBoolean()) {
            return new Schema().booleanSchemaValue(node.booleanValue());
        }

        List<String> composed = Arrays.asList("allOf", "anyOf", "oneOf");
        for (String field: composed) {
            if (node.get(field) != null) {
                return Json.mapper().convertValue(node, ComposedSchema.class);
            }
        }

        JsonNode type = node.get("type");
        String format = node.get("format") == null ? "" : node.get("format").textValue();

        if (type != null && "array".equals(((TextNode) type).textValue())) {
            schema = Json.mapper().convertValue(node, ArraySchema.class);
        } else if (type != null) {
            if (type.textValue().equals("integer")) {
                schema = Json.mapper().convertValue(node, IntegerSchema.class);
                if (StringUtils.isBlank(format)) {
                    schema.setFormat(null);
                }
            } else if (type.textValue().equals("number")) {
                schema = Json.mapper().convertValue(node, NumberSchema.class);
            } else if (type.textValue().equals("boolean")) {
                schema = Json.mapper().convertValue(node, BooleanSchema.class);
            } else if (type.textValue().equals("string")) {
                if ("date".equals(format)) {
                    schema = Json.mapper().convertValue(node, DateSchema.class);
                } else if ("date-time".equals(format)) {
                    schema = Json.mapper().convertValue(node, DateTimeSchema.class);
                } else if ("email".equals(format)) {
                    schema = Json.mapper().convertValue(node, EmailSchema.class);
                } else if ("password".equals(format)) {
                    schema = Json.mapper().convertValue(node, PasswordSchema.class);
                } else if ("uuid".equals(format)) {
                    schema = Json.mapper().convertValue(node, UUIDSchema.class);
                } else {
                    schema = Json.mapper().convertValue(node, StringSchema.class);
                }
            } else if (type.textValue().equals("object")) {
                schema = deserializeArbitraryOrObjectSchema(node, true);
            }
        } else if (node.get("$ref") != null) {
            schema = new Schema().$ref(node.get("$ref").asText());
        } else {
            schema = deserializeArbitraryOrObjectSchema(node, false);
        }

        return schema;
    }

    private Schema deserializeArbitraryOrObjectSchema(JsonNode node, boolean alwaysObject) {
        JsonNode additionalProperties = node.get("additionalProperties");
        Schema schema = null;
        if (additionalProperties != null) {
            if (additionalProperties.isBoolean()) {
                Boolean additionalPropsBoolean = Json.mapper().convertValue(additionalProperties, Boolean.class);
                ((ObjectNode)node).remove("additionalProperties");
                if (additionalPropsBoolean) {
                    schema = Json.mapper().convertValue(node, MapSchema.class);
                } else {
                    schema = Json.mapper().convertValue(node, ObjectSchema.class);
                }
                schema.setAdditionalProperties(additionalPropsBoolean);
            } else {
                Schema innerSchema = Json.mapper().convertValue(additionalProperties, Schema.class);
                ((ObjectNode)node).remove("additionalProperties");
                MapSchema ms = Json.mapper().convertValue(node, MapSchema.class);
                ms.setAdditionalProperties(innerSchema);
                schema = ms;
            }
        } else {
            if (!Boolean.TRUE.equals(useArbitrarySchema) || alwaysObject) {
                schema = Json.mapper().convertValue(node, ObjectSchema.class);
            } else {
                schema = Json.mapper().convertValue(node, ArbitrarySchema.class);
            }
        }
        if (schema != null) {
            schema.jsonSchema(Json31.jsonSchemaAsMap(node));
        }
        return schema;
    }

    private Schema deserializeJsonSchema(JsonNode node) {
        if (node.isBoolean()) {
            return new Schema().booleanSchemaValue(node.booleanValue());
        }
        JsonNode additionalProperties = node.get("additionalProperties");
        JsonNode type = node.get("type");
        Schema schema = null;

        if (type != null || additionalProperties != null) {
            if (type != null) {
                ((ObjectNode)node).remove("type");
            }
            if (additionalProperties != null) {
                ((ObjectNode)node).remove("additionalProperties");
            }
            schema = Json31.mapper().convertValue(node, JsonSchema.class);
            if (type instanceof TextNode) {
                schema.types(new LinkedHashSet<>(Arrays.asList(type.textValue())));
            } else if (type instanceof ArrayNode){
                Set<String> types = new LinkedHashSet<>();
                ((ArrayNode)type).elements().forEachRemaining( n -> {
                    types.add(n.textValue());
                });
                schema.types(types);
            }
            if (additionalProperties != null) {
                try {
                    if (additionalProperties.isBoolean()) {
                        schema.setAdditionalProperties(additionalProperties.booleanValue());
                    } else {
                        Schema innerSchema = deserializeJsonSchema(additionalProperties);
                        schema.setAdditionalProperties(innerSchema);
                    }
                } catch (Exception e) {
                    Boolean additionalPropsBoolean = Json31.mapper().convertValue(additionalProperties, Boolean.class);
                    schema.setAdditionalProperties(additionalPropsBoolean);
                }
            }

        } else {
            schema = Json31.mapper().convertValue(node, JsonSchema.class);
        }
        return schema;
    }
}
