package io.swagger.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelDeserializer extends JsonDeserializer<Model> {
    @Override
    public Model deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode sub = node.get("$ref");
        JsonNode allOf = node.get("allOf");

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
        } else {
            sub = node.get("type");
            Model model = null;
            if (sub != null && "array".equals(((TextNode) sub).textValue())) {
                model = Json.mapper().convertValue(node, ArrayModel.class);
            } else {
                model = Json.mapper().convertValue(node, ModelImpl.class);
            }
            return model;
        }
    }
}
