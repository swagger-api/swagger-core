package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Sample filter to remove the top level user tag, returning a copy of the OpenAPI object
 **/
public class NoUserTagFilter extends AbstractSpecFilter {

    public static final String USER_TAG = "user";

    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        List<Tag> tags = openAPI.getTags().stream()
                .filter(tag -> !USER_TAG.equals(tag.getName()))
                .collect(Collectors.toList());
        return Optional.of(new OpenAPI()
                .openapi(openAPI.getOpenapi())
                .info(openAPI.getInfo())
                .paths(openAPI.getPaths())
                .components(openAPI.getComponents())
                .tags(tags));
    }
}
