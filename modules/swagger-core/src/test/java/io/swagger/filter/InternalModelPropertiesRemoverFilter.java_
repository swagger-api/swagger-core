package io.swagger.filter;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

import java.util.List;
import java.util.Map;

/**
 * Sample filter to model properties starting with "_" unless a header
 * "super-user" is passed
 */
public class InternalModelPropertiesRemoverFilter extends AbstractSpecFilter {
    @Override
    public boolean isPropertyAllowed(
            Model model,
            Property property,
            String propertyName,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers) {
        if (propertyName.startsWith("_")) {
            if (headers != null && headers.containsKey("super-user")) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }
}
