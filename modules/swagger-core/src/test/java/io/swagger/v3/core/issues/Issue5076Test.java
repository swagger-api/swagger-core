package io.swagger.v3.core.issues;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.swagger.v3.oas.annotations.media.Schema.DEFAULT_SENTINEL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Test for issue #5076 - ensuring DEFAULT_SENTINEL doesn't leak into generated schemas
 */
public class Issue5076Test {

    @Test
    public void testIssue5076ExactScenario() throws Exception {
        final ModelConverterContextImpl context = getModelConverterContext(Json.mapper(), false);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(BoundingBoxLikeIssue5076.class));

        assertNotNull(model);
        
        assertNull(model.getDefault(), "Model should not have default when @Schema doesn't specify one");
        
        String yaml = Yaml.mapper().writeValueAsString(model);

        assertFalse(yaml.contains("##default"),
            "DEFAULT_SENTINEL '##default' must not appear in generated OpenAPI spec");
        assertFalse(yaml.contains("default:"), 
            "No 'default' field should be present when not specified in annotation");
    }

    private static ModelConverterContextImpl getModelConverterContext(ObjectMapper mapper, boolean openAPI31) {
        final ModelResolver modelResolver = new ModelResolver(mapper);
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(openAPI31);
        modelResolver.setConfiguration(configuration);
        return new ModelConverterContextImpl(modelResolver);
    }

    @Schema(description = "Bounding box like issue 5076")
    public static class BoundingBoxLikeIssue5076 {
        @Schema(description = "Bottom coordinate",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "1.0")
        private BigDecimal bottom;
        
        @Schema(description = "Top coordinate",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2.0")
        private BigDecimal top;

        public BigDecimal getBottom() {
            return bottom;
        }

        public void setBottom(BigDecimal bottom) {
            this.bottom = bottom;
        }

        public BigDecimal getTop() {
            return top;
        }

        public void setTop(BigDecimal top) {
            this.top = top;
        }
    }
}
