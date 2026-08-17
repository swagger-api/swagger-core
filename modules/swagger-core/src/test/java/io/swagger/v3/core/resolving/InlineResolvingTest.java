package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

public class InlineResolvingTest extends SwaggerTestBase{

    @Test
    public void testInlineResolving() {

        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(false).schemaResolution(io.swagger.v3.oas.models.media.Schema.SchemaResolution.INLINE);
        final ModelConverterContextImpl c = new ModelConverterContextImpl(modelResolver);
        // ModelConverters c = ModelConverters.getInstance(false, io.swagger.v3.oas.models.media.Schema.SchemaResolution.INLINE);
        c.resolve(new AnnotatedType(InlineSchemaFirst.class));

        String expectedYaml = "InlineSchemaFirst:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    property1:\n" +
                "      type: object\n" +
                "      description: InlineSchemaFirst property 1\n" +
                "      nullable: true\n" +
                "      example: example\n" +
                "    property2:\n" +
                "      type: object\n" +
                "      description: ' InlineSchemaFirst property 2'\n" +
                "      example: example 2\n" +
                "InlineSchemaPropertyFirst:\n" +
                "  type: object\n" +
                "  description: property\n" +
                "  example: example\n";

        SerializationMatchers.assertEqualsToYaml(c.getDefinedModels(), expectedYaml);
        // stringSchemaMap = c.readAll(InlineSchemaSecond.class);
        c.resolve(new AnnotatedType(InlineSchemaSecond.class));
        expectedYaml = "InlineSchemaFirst:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    property1:\n" +
                "      type: object\n" +
                "      description: InlineSchemaFirst property 1\n" +
                "      nullable: true\n" +
                "      example: example\n" +
                "    property2:\n" +
                "      type: object\n" +
                "      description: ' InlineSchemaFirst property 2'\n" +
                "      example: example 2\n" +
                "InlineSchemaPropertyFirst:\n" +
                "  type: object\n" +
                "  description: property\n" +
                "  example: example\n" +
                "InlineSchemaPropertySecond:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    bar:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          type: object\n" +
                "          description: property 1\n" +
                "        property2:\n" +
                "          type: object\n" +
                "          description: property 2\n" +
                "          example: example\n" +
                "  description: propertysecond\n" +
                "  nullable: true\n" +
                "  example: examplesecond\n" +
                "InlineSchemaPropertySimple:\n" +
                "  type: object\n" +
                "  description: property\n" +
                "  example: example\n" +
                "InlineSchemaSecond:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    propertySecond1:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            property1:\n" +
                "              type: object\n" +
                "              description: property 1\n" +
                "            property2:\n" +
                "              type: object\n" +
                "              description: property 2\n" +
                "              example: example\n" +
                "      description: InlineSchemaSecond property 1\n" +
                "      nullable: true\n" +
                "      example: examplesecond\n" +
                "    property2:\n" +
                "      type: object\n" +
                "      description: InlineSchemaSecond property 2\n" +
                "      example: InlineSchemaSecond example 2\n" +
                "InlineSchemaSimple:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    property1:\n" +
                "      type: object\n" +
                "      description: property 1\n" +
                "    property2:\n" +
                "      type: object\n" +
                "      description: property 2\n" +
                "      example: example\n";
        SerializationMatchers.assertEqualsToYaml(c.getDefinedModels(), expectedYaml);
    }

    static class InlineSchemaFirst {

        // public String foo;

        @Schema(description = "InlineSchemaFirst property 1", nullable = true)
        public InlineSchemaPropertyFirst property1;


        private InlineSchemaPropertyFirst property2;

        @Schema(description = " InlineSchemaFirst property 2", example = "example 2")
        public InlineSchemaPropertyFirst getProperty2() {
            return null;
        }
    }

    static class InlineSchemaSecond {

        // public String foo;

        @Schema(description = "InlineSchemaSecond property 1", nullable = true)
        public InlineSchemaPropertySecond propertySecond1;


        private InlineSchemaPropertyFirst property2;

        @Schema(description = "InlineSchemaSecond property 2", example = "InlineSchemaSecond example 2")
        public InlineSchemaPropertyFirst getProperty2() {
            return null;
        }
    }

    @Schema(description = "property", example = "example")
    static class InlineSchemaPropertyFirst {
        // public String bar;
    }

    @Schema(description = "propertysecond", example = "examplesecond")
    static class InlineSchemaPropertySecond {
        public InlineSchemaSimple bar;
    }

    static class InlineSchemaSimple {

        @Schema(description = "property 1")
        public InlineSchemaPropertySimple property1;


        private InlineSchemaPropertySimple property2;

        @Schema(description = "property 2", example = "example")
        public InlineSchemaPropertySimple getProperty2() {
            return null;
        }
    }

    @Schema(description = "property")
    static class InlineSchemaPropertySimple {
        // public String bar;
    }
}
