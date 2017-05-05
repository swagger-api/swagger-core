package io.swagger.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.models.media.ArraySchema;
import io.swagger.models.media.DateSchema;
import io.swagger.models.media.DateTimeSchema;
import io.swagger.models.media.EmailSchema;
import io.swagger.models.media.IntegerSchema;
import io.swagger.models.media.NumberSchema;
import io.swagger.models.media.PasswordSchema;
import io.swagger.models.media.Schema;
import io.swagger.models.media.StringSchema;
import io.swagger.models.media.UUIDSchema;

import java.io.IOException;

public class ModelDeserializer extends JsonDeserializer<Schema> {
    @Override
    public Schema deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");
        JsonNode allOf = node.get("allOf");

        // TODO

        /*
        if (sub != null) {
            return Json.mapper().convertValue(sub, RefModel.class);
        } else if (allOf != null) {
            ComposedModel model = null;
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
            return model;
        } else
        {*/
        sub = node.get("type");
        String format = node.get("format") == null ? "" : node.get("format").textValue();

        Schema model = null;

        if (sub != null && "array".equals(((TextNode) sub).textValue())) {
            model = Json.mapper().convertValue(node, ArraySchema.class);
        } else if(sub != null) {
            if (sub.textValue().equals("integer")) {
                model = Json.mapper().convertValue(node, IntegerSchema.class);
            }
            else if (sub.textValue().equals("number")) {
                model = Json.mapper().convertValue(node, NumberSchema.class);
            }
            else if (sub.textValue().equals("string")) {
                if("date".equals(format)) {
                    model = Json.mapper().convertValue(node, DateSchema.class);
                }
                else if("date-time".equals(format)) {
                    model = Json.mapper().convertValue(node, DateTimeSchema.class);
                }
                else if("email".equals(format)) {
                    model = Json.mapper().convertValue(node, EmailSchema.class);
                }
                else if("password".equals(format)) {
                    model = Json.mapper().convertValue(node, PasswordSchema.class);
                }
                else if("uuid".equals(format)) {
                    model = Json.mapper().convertValue(node, UUIDSchema.class);
                }
                else {
                    model = Json.mapper().convertValue(node, StringSchema.class);
                }
            }
        } else if(node.get("$ref") != null) {
            model = new Schema().ref(node.get("$ref").asText());
        }

        return model;
    }
}
