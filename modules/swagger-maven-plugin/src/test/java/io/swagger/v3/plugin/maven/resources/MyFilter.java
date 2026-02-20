package io.swagger.v3.plugin.maven.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MyFilter extends AbstractSpecFilter {
        private static Logger LOGGER = LoggerFactory.getLogger(MyFilter.class);

        @Override
        public Optional<OpenAPI> filterOpenAPI(
                OpenAPI openAPI,
                Map<String, List<String>> params,
                Map<String, String> cookies,
                Map<String, List<String>> headers) {
            openAPI.getInfo().setTitle("UPDATEDBYFILTER");
            return Optional.of(openAPI);
        }

        @Override
        public Optional<Operation> filterOperation(
                Operation operation,
                ApiDescription api,
                Map<String, List<String>> params,
                Map<String, String> cookies,
                Map<String, List<String>> headers) {
            // some processing
            return Optional.of(operation);
        }

    }