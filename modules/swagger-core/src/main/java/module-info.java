/** JPMS descriptor; preserves the existing Automatic-Module-Name. */
module io.swagger.v3.core {
    requires transitive io.swagger.v3.oas.annotations;
    requires transitive io.swagger.v3.oas.models;
    requires transitive tools.jackson.databind;
    requires transitive tools.jackson.dataformat.yaml;
    requires transitive jakarta.xml.bind;
    requires transitive jakarta.validation;
    requires org.slf4j;

    exports io.swagger.v3.core.converter;
    exports io.swagger.v3.core.filter;
    exports io.swagger.v3.core.jackson;
    exports io.swagger.v3.core.jackson.mixin;
    exports io.swagger.v3.core.model;
    exports io.swagger.v3.core.util;

    // Jackson introspects model and configuration members on the module path.
    opens io.swagger.v3.core.converter to tools.jackson.databind;
    opens io.swagger.v3.core.filter to tools.jackson.databind;
    opens io.swagger.v3.core.jackson to tools.jackson.databind;
    opens io.swagger.v3.core.jackson.mixin to tools.jackson.databind;
    opens io.swagger.v3.core.model to tools.jackson.databind;
    opens io.swagger.v3.core.util to tools.jackson.databind;

    uses io.swagger.v3.core.converter.ModelConverter;
}
