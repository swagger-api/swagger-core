package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NoPetRefSchemaFilter extends AbstractSpecFilter {
    private static final String MODEL = "Pet";

    @Override
    public Optional<Schema> filterSchema(Schema schema, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (schema == null || StringUtils.isBlank(schema.getXml().getName())) {
            return Optional.of(schema);
        }

        if (schema.getXml().getName().equals(MODEL)) {
            return Optional.empty();
        }
        return Optional.of(schema);
    }
}