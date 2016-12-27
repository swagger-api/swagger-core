package io.swagger.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.models.Xml;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.RefProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private static BigDecimal getBigDecimal(JsonNode node, PropertyBuilder.PropertyId type) {
        final JsonNode detailNode = getDetailNode(node, type);
        return detailNode == null ? null : new BigDecimal(detailNode.toString());
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
                if (child instanceof TextNode ||
                    child instanceof NumericNode ||
                    child instanceof IntNode ||
                    child instanceof LongNode ||
                    child instanceof DoubleNode ||
                    child instanceof FloatNode) {
                    result.add(child.asText());
                }
            }
        }

        return result.isEmpty() ? null : result;
    }

    //because of the complexity of deserializing properties we must handle vendor extensions by hand
    private static Map<String, Object> getVendorExtensions(JsonNode node) {
        Map<String, Object> result = new HashMap<String, Object>();

        Iterator<String> fieldNameIter = node.fieldNames();
        while (fieldNameIter.hasNext()) {
            String fieldName = fieldNameIter.next();

            if(fieldName.startsWith("x-")) {
                JsonNode extensionField = node.get(fieldName);

                Object extensionObject = Json.mapper().convertValue(extensionField, Object.class);
                result.put(fieldName, extensionObject);
            }
        }
        return result;
    }

    private static List<String> getRequired(JsonNode node, PropertyBuilder.PropertyId type) {
        List<String> result = new ArrayList<String>();

        final JsonNode detailNode = getDetailNode(node, type);

        if (detailNode == null) {
            return result;
        }

        if (detailNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) detailNode;
            Iterator<JsonNode> fieldNameIter = arrayNode.iterator();

            while (fieldNameIter.hasNext()) {
                JsonNode item = fieldNameIter.next();
                result.add(item.asText());
            }
            return result;
        } else {
            throw new RuntimeException("Required property should be a list");
        }

    }

    private static JsonNode getDetailNode(JsonNode node, PropertyBuilder.PropertyId type) {
        return node.get(type.getPropertyName());
    }

    @Override
    public Property deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Property property = propertyFromNode(node);
        if(property != null) {
            property.setXml(getXml(node));
        }
        return property;
    }

    public Xml getXml(JsonNode node) {
        Xml xml = null;

        if (node instanceof ObjectNode) {
            ObjectNode obj = (ObjectNode) ((ObjectNode) node).get("xml");
            if (obj != null) {
                xml = new Xml();
                JsonNode n = obj.get("name");
                if (n != null) {
                    xml.name(n.asText());
                }
                n = obj.get("namespace");
                if (n != null) {
                    xml.namespace(n.asText());
                }
                n = obj.get("prefix");
                if (n != null) {
                    xml.prefix(n.asText());
                }
                n = obj.get("attribute");
                if (n != null) {
                    xml.attribute(n.asBoolean());
                }
                n = obj.get("wrapped");
                if (n != null) {
                    xml.wrapped(n.asBoolean());
                }
            }
        }
        return xml;
    }

    private Map<PropertyBuilder.PropertyId, Object> argsFromNode(JsonNode node) {
        if (node == null) return Collections.emptyMap();
        final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
        args.put(PropertyBuilder.PropertyId.TYPE, getString(node, PropertyBuilder.PropertyId.TYPE));
        args.put(PropertyBuilder.PropertyId.FORMAT, getString(node, PropertyBuilder.PropertyId.FORMAT));
        args.put(PropertyBuilder.PropertyId.DESCRIPTION, getString(node, PropertyBuilder.PropertyId.DESCRIPTION));
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
        args.put(PropertyBuilder.PropertyId.MINIMUM, getBigDecimal(node, PropertyBuilder.PropertyId.MINIMUM));
        args.put(PropertyBuilder.PropertyId.MAXIMUM, getBigDecimal(node, PropertyBuilder.PropertyId.MAXIMUM));
        args.put(PropertyBuilder.PropertyId.MULTIPLE_OF, getBigDecimal(node, PropertyBuilder.PropertyId.MULTIPLE_OF));
        args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, getBoolean(node, PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM));
        args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, getBoolean(node, PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM));
        args.put(PropertyBuilder.PropertyId.UNIQUE_ITEMS, getBoolean(node, PropertyBuilder.PropertyId.UNIQUE_ITEMS));
        args.put(PropertyBuilder.PropertyId.READ_ONLY, getBoolean(node, PropertyBuilder.PropertyId.READ_ONLY));
        args.put(PropertyBuilder.PropertyId.VENDOR_EXTENSIONS, getVendorExtensions(node));
        return args;
    }

    Property propertyFromNode(JsonNode node) {
        final String type = getString(node, PropertyBuilder.PropertyId.TYPE);
        final String title = getString(node, PropertyBuilder.PropertyId.TITLE);
        final String format = getString(node, PropertyBuilder.PropertyId.FORMAT);

        String description = getString(node, PropertyBuilder.PropertyId.DESCRIPTION);

        JsonNode detailNode = node.get("$ref");
        if (detailNode != null) {
            return new RefProperty(detailNode.asText())
                    .description(description)
                    .title(title);
        }

        if (ObjectProperty.isType(type) || node.get("properties") != null) {
            JsonNode example = getDetailNode(node, PropertyBuilder.PropertyId.EXAMPLE);
            detailNode = node.get("additionalProperties");
            if (detailNode != null && detailNode.getNodeType().equals(JsonNodeType.OBJECT)) {
                Property items = propertyFromNode(detailNode);
                if (items != null) {
                    MapProperty mapProperty = new MapProperty(items)
                            .description(description)
                            .title(title);
                    mapProperty.setExample(example);
                    mapProperty.setMinProperties(getInteger(node, PropertyBuilder.PropertyId.MIN_PROPERTIES));
                    mapProperty.setMaxProperties(getInteger(node, PropertyBuilder.PropertyId.MAX_PROPERTIES));
                    mapProperty.setVendorExtensionMap(getVendorExtensions(node));
                    return mapProperty;
                }
            } else {
                detailNode = node.get("properties");
                String detailNodeType = null;
                Map<String, Property> properties = new LinkedHashMap<String, Property>();
                if(detailNode != null){
                    for(Iterator<Map.Entry<String,JsonNode>> iter = detailNode.fields(); iter.hasNext();){
                        Map.Entry<String,JsonNode> field = iter.next();
                        Property property = propertyFromNode(field.getValue());
                        if(property != null) {
                            properties.put(field.getKey(), property);
                        }
                        else {
                            if("type".equals(field.getKey()) && field.getValue() != null && "array".equals(field.getValue().asText())) {
                                detailNodeType = "array";
                            }
                            if(("description").equals(field.getKey()) && field.getValue().getNodeType().equals(JsonNodeType.STRING)) {
                                description = field.getValue().asText();
                            }
                        }
                    }
                }

                if("array".equals(detailNodeType)) {
                    ArrayProperty ap = new ArrayProperty()
                            .description(description)
                            .title(title);
                    ap.setExample(example);
                    PropertyBuilder.merge(ap, argsFromNode(detailNode));
                    ap.setDescription(description);

                    if(properties.keySet().size() == 1) {
                        String key = properties.keySet().iterator().next();
                        ap.setItems(properties.get(key));
                    }
                    ap.setVendorExtensionMap(getVendorExtensions(node));
                    return ap;
                }
                ObjectProperty objectProperty = new ObjectProperty(properties)
                        .description(description)
                        .title(title);
                objectProperty.setExample(example);
                objectProperty.setVendorExtensionMap(getVendorExtensions(node));

                List<String> required = getRequired(node, PropertyBuilder.PropertyId.REQUIRED);
                objectProperty.setRequiredProperties(required);

                return objectProperty;
            }
        }
        if (ArrayProperty.isType(type)) {
            detailNode = node.get("items");
            if (detailNode != null) {
                Property subProperty = propertyFromNode(detailNode);
                ArrayProperty arrayProperty = new ArrayProperty()
                        .items(subProperty)
                        .description(description)
                        .title(title);
                arrayProperty.setMinItems(getInteger(node, PropertyBuilder.PropertyId.MIN_ITEMS));
                arrayProperty.setMaxItems(getInteger(node, PropertyBuilder.PropertyId.MAX_ITEMS));
                arrayProperty.setUniqueItems(getBoolean(node, PropertyBuilder.PropertyId.UNIQUE_ITEMS));

                JsonNode example = getDetailNode( node, PropertyBuilder.PropertyId.EXAMPLE);
                arrayProperty.setExample(example);

                arrayProperty.setVendorExtensionMap(getVendorExtensions(node));
                return arrayProperty;
            }
        }


        Map<PropertyBuilder.PropertyId, Object> args = argsFromNode(node);
        Property output = PropertyBuilder.build(type, format, args);
        if (output == null) {
            LOGGER.warn("no property from " + type + ", " + format + ", " + args);
            return null;
        }
        output.setDescription(description);

        return output;
    }
}
