package io.swagger.util;

import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>BaseReaderUtils</code> class is utility class which helps read annotations to the Swagger.
 */
public final class BaseReaderUtils {

    private BaseReaderUtils() {

    }

    /**
     * Collects extensions.
     *
     * @param extensions is an array of extensions
     * @return the map with extensions
     */
    public static Map<String, Object> parseExtensions(Extension[] extensions) {
        final Map<String, Object> map = new HashMap<String, Object>();
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
}

