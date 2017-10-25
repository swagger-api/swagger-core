package io.swagger.filter.resources;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.v3.oas.models.PathItem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Does nothing
 **/
public class NoPathItemFilter extends AbstractSpecFilter {
    @Override
    public Optional<PathItem> filterPathItem(PathItem pathItem, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (StringUtils.isBlank(pathItem.get$ref())) {
            return Optional.empty();
        }
        return Optional.of(pathItem);
    }
}