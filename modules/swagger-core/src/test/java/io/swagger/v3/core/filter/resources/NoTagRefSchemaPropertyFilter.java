package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NoTagRefSchemaPropertyFilter extends AbstractSpecFilter {
    private static final String MODEL = "Tag";

    @Override
    public Optional<Schema> filterSchemaProperty(Schema property, Schema schema, String propName, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (property == null || StringUtils.isBlank(property.get$ref())) {
            return Optional.of(property);
        }

        if (property.get$ref().contains(MODEL)) {
            return Optional.empty();
        }
        return Optional.of(property);
    }
}