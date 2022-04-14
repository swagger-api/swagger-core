package io.swagger.v3.plugins.gradle.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.oas.models.OpenAPI;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class OpenAPI31Filter extends AbstractSpecFilter {
    
    @Override
        public Optional<OpenAPI> filterOpenAPI(
                OpenAPI openAPI,
                Map<String, List<String>> params,
                Map<String, String> cookies,
                Map<String, List<String>> headers) {
            openAPI.setOpenapi("3.1.0");
            openAPI.getInfo().setTitle("UPDATEDBYOAS31FILTER");
            return Optional.of(openAPI);
        }
    
}
