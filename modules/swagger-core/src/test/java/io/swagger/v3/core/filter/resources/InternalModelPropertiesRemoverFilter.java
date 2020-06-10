package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sample filter to model properties starting with "_" unless a header
 * "super-user" is passed
 */
public class InternalModelPropertiesRemoverFilter extends AbstractSpecFilter {
    @Override
    public Optional<Schema> filterSchemaProperty(Schema property, Schema schema, String propName, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (StringUtils.isNotBlank(property.getName()) && property.getName().startsWith("_")) {
            if (headers != null && headers.containsKey("super-user")) {
                return Optional.of(property);
            }
            return Optional.empty();
        } else {
            return Optional.of(property);
        }
    }
}
