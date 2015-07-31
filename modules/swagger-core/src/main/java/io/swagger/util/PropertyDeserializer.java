package io.swagger.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.RefProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class PropertyDeserializer extends JsonDeserializer<Property> {
    Logger LOGGER = LoggerFactory.getLogger(PropertyDeserializer.class);

    private static String getString(JsonNode node, PropertyBuilder.PropertyId type) {
        final JsonNode detailNode = getDetailNode(node, type);
        return detailNode == null ? null : detailNode.asText();
    }

    private static Integer getInteger(JsonNode node, PropertyBuilder.PropertyId type) {
        final JsonNode detailNode = getDetailNode(node, type);
        return detailNode == null ? null : detailNode.intValue();
    }

    private static Double getDouble(JsonNode node, PropertyBuilder.PropertyId type) {
        final JsonNode detailNode = getDetailNode(node, type);
        return detailNode == null ? null : detailNode.doubleValue();
    }

    private static Boolean getBoolean(JsonNode node, PropertyBuilder.PropertyId type) {
        final JsonNode detailNode = getDetailNode(node, type);
        return detailNode == null ? null : detailNode.booleanValue();
    }

    private static List<String> getEnum(JsonNode node, PropertyBuilder.PropertyId type) {
        final List<String> result = new ArrayList<String>();
        JsonNode detailNode = getDetailNode(node, type);
        if (detailNode != null) {
            ArrayNode an = (ArrayNode) detailNode;
            for (JsonNode child : an) {
                if (child instanceof TextNode) {
                    result.add(child.asText());
                }
            }
        }

        return result.isEmpty() ? null : result;
    }

    private static JsonNode getDetailNode(JsonNode node, PropertyBuilder.PropertyId type) {
        return node.get(type.getPropertyName());
    }

    @Override
    public Property deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return propertyFromNode(node);
    }

    private boolean isReserved(String propertyName){
        PropertyBuilder.PropertyId[] reserved = PropertyBuilder.PropertyId.values();
        boolean isReserved = false;
        for(int i = 0; i < reserved.length; i++){
          if(reserved[i].getPropertyName().equals(propertyName)){
            isReserved = true;
          }
        }
        return isReserved;
    }

    Property propertyFromNode(JsonNode node) {
        final String type = getString(node, PropertyBuilder.PropertyId.TYPE);
        final String format = getString(node, PropertyBuilder.PropertyId.FORMAT);
        final String description = getString(node, PropertyBuilder.PropertyId.DESCRIPTION);

        JsonNode detailNode = node.get("$ref");
        if (detailNode != null) {
            return new RefProperty(detailNode.asText()).description(description);
        }

        if (ObjectProperty.isType(type)) {
            detailNode = node.get("additionalProperties");
            if (detailNode != null) {
                Property items = propertyFromNode(detailNode);
                if (items != null) {
                    return new MapProperty(items).description(description);
                }
            } else {
              detailNode = node.get("properties");
              Map<String, Property> properties = new HashMap<String, Property>();
              if(detailNode != null){
                  for(Iterator<Map.Entry<String,JsonNode>> iter = detailNode.fields(); iter.hasNext();){
                      Map.Entry<String,JsonNode> field = iter.next();
                      if(!isReserved(field.getKey())){
                          Property property = propertyFromNode(field.getValue());
                          properties.put(field.getKey(), property);
                      }
                  }
              }
              return new ObjectProperty(properties);
            }
        }
        if (ArrayProperty.isType(type)) {
            detailNode = node.get("items");
            if (detailNode != null) {
                Property subProperty = propertyFromNode(detailNode);
                return new ArrayProperty().items(subProperty).description(description);
            }
        }

        final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
        args.put(PropertyBuilder.PropertyId.TYPE, type);
        args.put(PropertyBuilder.PropertyId.FORMAT, format);
        args.put(PropertyBuilder.PropertyId.DESCRIPTION, description);
        args.put(PropertyBuilder.PropertyId.EXAMPLE, getString(node, PropertyBuilder.PropertyId.EXAMPLE));
        args.put(PropertyBuilder.PropertyId.ENUM, getEnum(node, PropertyBuilder.PropertyId.ENUM));
        args.put(PropertyBuilder.PropertyId.TITLE, getString(node, PropertyBuilder.PropertyId.TITLE));
        args.put(PropertyBuilder.PropertyId.DEFAULT, getString(node, PropertyBuilder.PropertyId.DEFAULT));
        args.put(PropertyBuilder.PropertyId.PATTERN, getString(node, PropertyBuilder.PropertyId.PATTERN));
        args.put(PropertyBuilder.PropertyId.DESCRIMINATOR, getString(node, PropertyBuilder.PropertyId.DESCRIMINATOR));
        args.put(PropertyBuilder.PropertyId.MIN_ITEMS, getInteger(node, PropertyBuilder.PropertyId.MIN_ITEMS));
        args.put(PropertyBuilder.PropertyId.MAX_ITEMS, getInteger(node, PropertyBuilder.PropertyId.MAX_ITEMS));
        args.put(PropertyBuilder.PropertyId.MIN_PROPERTIES, getInteger(node, PropertyBuilder.PropertyId.MIN_PROPERTIES));
        args.put(PropertyBuilder.PropertyId.MAX_PROPERTIES, getInteger(node, PropertyBuilder.PropertyId.MAX_PROPERTIES));
        args.put(PropertyBuilder.PropertyId.MIN_LENGTH, getInteger(node, PropertyBuilder.PropertyId.MIN_LENGTH));
        args.put(PropertyBuilder.PropertyId.MAX_LENGTH, getInteger(node, PropertyBuilder.PropertyId.MAX_LENGTH));
        args.put(PropertyBuilder.PropertyId.MINIMUM, getDouble(node, PropertyBuilder.PropertyId.MINIMUM));
        args.put(PropertyBuilder.PropertyId.MAXIMUM, getDouble(node, PropertyBuilder.PropertyId.MAXIMUM));
        args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, getBoolean(node, PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM));
        args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, getBoolean(node, PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM));
        args.put(PropertyBuilder.PropertyId.UNIQUE_ITEMS, getBoolean(node, PropertyBuilder.PropertyId.UNIQUE_ITEMS));

        Property output = PropertyBuilder.build(type, format, args);
        if (output == null) {
            LOGGER.warn("no property from " + type + ", " + format + ", " + args);
            return null;
        }
        output.setDescription(description);
        return output;
    }
}
