package io.swagger.v3.core.filter;

import io.swagger.v3.core.util.OpenAPI30To31;
import io.swagger.v3.core.util.OpenAPISchema2JsonSchema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OpenAPI31SpecFilter extends AbstractSpecFilter {

    private OpenAPI30To31 openAPI30To31 = new OpenAPI30To31();
    private OpenAPISchema2JsonSchema schema2JsonSchema = new OpenAPISchema2JsonSchema();

    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        openAPI30To31.process(openAPI);
        return Optional.of(openAPI);
    }

    @Override
    public Optional<Schema> filterSchema(Schema schema, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        schema2JsonSchema.process(schema);
        return Optional.of(schema);
    }

    @Override
    public boolean isOpenAPI31Filter() {
        return true;
    }
}
