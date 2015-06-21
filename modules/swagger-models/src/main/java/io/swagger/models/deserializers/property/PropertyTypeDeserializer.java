package io.swagger.models.deserializers.property;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import io.swagger.models.deserializers.CustomAsTypeDeserializer;
import io.swagger.models.properties.*;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 26/3/15
 */
public class PropertyTypeDeserializer extends CustomAsTypeDeserializer {

    private static final Map<String, Class<? extends Property>> subTypesMap = new HashMap<String, Class<? extends
            Property>>();

    static {
        subTypesMap.put("boolean", BooleanProperty.class);
        subTypesMap.put("date", DateProperty.class);
        subTypesMap.put("date-time", DateTimeProperty.class);
        subTypesMap.put("number", DecimalProperty.class);
        subTypesMap.put("double", DoubleProperty.class);
        subTypesMap.put("float", FloatProperty.class);
        subTypesMap.put("int32", IntegerProperty.class);
        subTypesMap.put("int64", LongProperty.class);
        subTypesMap.put("file", FileProperty.class);
        subTypesMap.put("map", MapProperty.class);
        subTypesMap.put("object", ObjectProperty.class);
        subTypesMap.put("array", ArrayProperty.class);
        subTypesMap.put("string", StringProperty.class);
    }

    public PropertyTypeDeserializer(final PropertyTypeDeserializer src, final BeanProperty property) {
        super(src, property);
    }

    public PropertyTypeDeserializer(
            final JavaType bt, final TypeIdResolver idRes,
            final String typePropertyName, final boolean typeIdVisible, final Class<?> defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Override
    protected Class<?> findSubType(JsonNode node) {
        Class<? extends Property> subType = null;
        if (node.get("format") != null) {
            String format = node.get("format").asText();
            if (format != null && !format.equals(NULL)) {
                subType = subTypesMap.get(format);
            }
        }
        if (subType == null) {
            if (node.get("type") != null) { // it won't come
                String type = node.get("type").asText();
                if (type != null && !type.equals(NULL)) {
                    if (type.equals("object")) {
                        if (node.get("additionalProperties") != null) {
                            subType = MapProperty.class;
                        } else {
                            subType = ObjectProperty.class;
                        }
                    } else {
                        subType = subTypesMap.get(type);
                    }
                }
            }
        }
        if (subType == null) {
            if (node.get("$ref") != null) {
                String ref = node.get("$ref").asText();
                if (ref != null && !ref.equals(NULL)) {
                    subType = RefProperty.class;
                }
            }
        }
        if (subType == null) {
            subType = ObjectProperty.class; // default one
        }

        return subType;
    }

    @Override
    protected TypeDeserializer newInstance(final BeanProperty property) {
        return new PropertyTypeDeserializer(this, property);
    }
}
