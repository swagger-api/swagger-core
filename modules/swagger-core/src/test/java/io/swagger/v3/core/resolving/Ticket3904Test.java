package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket3904Test extends SwaggerTestBase {

    @Test
    public void testJsonValueSchemaAnnotation() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(Request.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Request:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "      description: Description of ID.");

    }

    static class Request {
        @io.swagger.v3.oas.annotations.media.Schema(description = "Description of ID.")
        private Id id;

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }
    }
    static class Id {
        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
