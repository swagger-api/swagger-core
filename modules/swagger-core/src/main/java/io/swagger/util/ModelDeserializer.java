package io.swagger.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.BooleanSchema;
import io.swagger.oas.models.media.ComposedSchema;
import io.swagger.oas.models.media.DateSchema;
import io.swagger.oas.models.media.DateTimeSchema;
import io.swagger.oas.models.media.EmailSchema;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.MapSchema;
import io.swagger.oas.models.media.NumberSchema;
import io.swagger.oas.models.media.ObjectSchema;
import io.swagger.oas.models.media.PasswordSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.oas.models.media.UUIDSchema;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModelDeserializer extends JsonDeserializer<Schema> {
    @Override
    public Schema deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode allOf = node.get("allOf");
        JsonNode anyOf = node.get("anyOf");
        JsonNode oneOf = node.get("oneOf");

        Schema schema = null;

        if (allOf != null || anyOf != null || oneOf != null) {

            ComposedSchema composedSchema = Json.mapper().convertValue(node, ComposedSchema.class);

            // TODO do we support stuff similar to below?
/*
            // we only support one parent, no multiple inheritance or composition
            model = Json.mapper().convertValue(node, ComposedModel.class);
            List<Model> allComponents = model.getAllOf();
            if (allComponents.size() >= 1) {
                model.setParent(allComponents.get(0));
                if (allComponents.size() >= 2) {
                    model.setChild(allComponents.get(allComponents.size() - 1));
                    List<RefModel> interfaces = new ArrayList<RefModel>();
                    int size = allComponents.size();
                    for (Model m : allComponents.subList(1, size - 1)) {
                        if (m instanceof RefModel) {
                            RefModel ref = (RefModel) m;
                            interfaces.add(ref);
                        }
                    }
                    model.setInterfaces(interfaces);
                } else {
                    model.setChild(new ModelImpl());
                }
            }
*/
            return composedSchema;

        } else {

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
                    JsonNode additionalProperties = node.get("additionalProperties");
                    if (additionalProperties != null) {
                        Schema innerSchema = Json.mapper().convertValue(additionalProperties, Schema.class);
                        MapSchema ms = Json.mapper().convertValue(node, MapSchema.class);
                        ms.setAdditionalProperties(innerSchema);
                        schema = ms;
                    } else {
                        schema = Json.mapper().convertValue(node, ObjectSchema.class);
                    }
                }
            } else if (node.get("$ref") != null) {
                schema = new Schema().$ref(node.get("$ref").asText());
            } else { // assume object
                schema = Json.mapper().convertValue(node, ObjectSchema.class);
            }
        }

        // check extensions

        Iterator<String> it = node.fieldNames();
        while(it.hasNext()) {
            String key = it.next();
            Object value = node.get(key);
            if(key.startsWith("x-") && value != null && StringUtils.isNotBlank(value.toString())) {
                if(value instanceof NullNode) {
                    value =  null;
                }
                if(value instanceof TextNode) {
                    schema.addExtension(key, ((TextNode)value).asText());
                }
                else {
                    if(value instanceof ObjectNode) {
                        value = Json.mapper().convertValue(value, Map.class);
                    }
                    else if (value instanceof ArrayNode) {
                        value = Json.mapper().convertValue(value, List.class);
                    }
                    else if (value instanceof IntNode) {
                        value = ((IntNode)value).intValue();
                    }
                    else if (value instanceof LongNode) {
                        value = ((LongNode)value).longValue();
                    }
                    else if (value instanceof BooleanNode) {
                        value = ((BooleanNode)value).booleanValue();
                    }
                    else if (value instanceof FloatNode) {
                        value = ((FloatNode)value).floatValue();
                    }
                    else if (value instanceof DoubleNode) {
                        value = ((DoubleNode)value).doubleValue();
                    }
                    schema.addExtension(key, value);
                }
            }
        }

        return schema;
    }
}
