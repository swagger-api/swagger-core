package io.swagger.filter.resources;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.oas.models.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Does nothing
 **/
public class NoStringSchemasFilter extends AbstractSpecFilter {
    private static final String TYPE = "string";

    @Override
    public Optional<Schema> filterProperty(Schema schema, Schema property, String propertyName, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (TYPE.equals(schema.getType())) {
            return Optional.empty();
        }
        return Optional.of(schema);
    }
}