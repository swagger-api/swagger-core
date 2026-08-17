package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * An explicit {@code @Schema(type = "number" | "integer" | "boolean")} must be reflected in the
 * OpenAPI 3.1 "types" set, not only in the legacy scalar "type" field. Previously the explicit type
 * was applied via {@code setType} (scalar) while the "types" set kept the default {@code ["string"]},
 * so the serialized 3.1 schema rendered every explicitly-typed field as {@code string}.
 */
public class Ticket5233Test extends SwaggerTestBase {

    @Test
    public void testExplicitSchemaTypeIsAppliedToTypesSetUnderOpenApi31() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        final Schema model = context.resolve(new AnnotatedType(Model.class));

        final Map<String, Schema> properties = model.getProperties();
        assertEquals(properties.get("amount").getTypes(), Collections.singleton("number"));
        assertEquals(properties.get("count").getTypes(), Collections.singleton("integer"));
        assertEquals(properties.get("flag").getTypes(), Collections.singleton("boolean"));

        // controls: inferred type and enum are unaffected
        assertEquals(properties.get("inferred").getTypes(), Collections.singleton("number"));
        assertEquals(properties.get("unit").getTypes(), Collections.singleton("string"));
        assertEquals(properties.get("unit").getEnum(), Arrays.asList("DAY", "WEEK", "MONTH"));
    }

    enum Freq {DAY, WEEK, MONTH}

    private static class Model {
        @io.swagger.v3.oas.annotations.media.Schema(title = "Inferred")
        public BigDecimal inferred;

        @io.swagger.v3.oas.annotations.media.Schema(title = "Amount", type = "number")
        public BigDecimal amount;

        @io.swagger.v3.oas.annotations.media.Schema(title = "Count", type = "integer")
        public Integer count;

        @io.swagger.v3.oas.annotations.media.Schema(title = "Flag", type = "boolean")
        public Boolean flag;

        @io.swagger.v3.oas.annotations.media.Schema(title = "Unit")
        public Freq unit;
    }
}
