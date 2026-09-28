package io.swagger.v3.core.util;

/**
 * Identifies which of the {@link tools.jackson.databind.ObjectMapper} instances built by
 * {@link ObjectMapperFactory} a {@link MapperCustomizer} is being applied to.
 *
 * @since 3.0.0
 */
public enum MapperTarget {
    /** OpenAPI 3.0 JSON mapper, see {@link Json#mapper()}. */
    JSON(false, false),
    /** OpenAPI 3.0 YAML mapper, see {@link Yaml#mapper()}. */
    YAML(true, false),
    /** OpenAPI 3.1 JSON mapper, see {@link Json31#mapper()}. */
    JSON31(false, true),
    /** OpenAPI 3.1 YAML mapper, see {@link Yaml31#mapper()}. */
    YAML31(true, true),
    /** JSON mapper used to convert between OpenAPI 3.0 and 3.1 schemas, see {@link Json31#converterMapper()}. */
    JSON_CONVERTER(false, false);

    private final boolean yaml;
    private final boolean openapi31;

    MapperTarget(boolean yaml, boolean openapi31) {
        this.yaml = yaml;
        this.openapi31 = openapi31;
    }

    public boolean isYaml() {
        return yaml;
    }

    public boolean isJson() {
        return !yaml;
    }

    public boolean isOpenapi31() {
        return openapi31;
    }
}
