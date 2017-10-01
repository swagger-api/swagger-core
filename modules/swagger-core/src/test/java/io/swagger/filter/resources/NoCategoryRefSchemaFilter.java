package io.swagger.filter.resources;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Does nothing
 **/
public class NoCategoryRefSchemaFilter extends AbstractSpecFilter {
    private static final String MODEL = "Category";

    @Override
    public Optional<Schema> filterSchemaProperty(Schema property, Schema schema, String propName, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (schema == null || StringUtils.isBlank(schema.get$ref())) {
            return Optional.of(schema);
        }

        if (schema.get$ref().contains(MODEL)) {
            return Optional.empty();
        }
        return Optional.of(schema);
    }
}