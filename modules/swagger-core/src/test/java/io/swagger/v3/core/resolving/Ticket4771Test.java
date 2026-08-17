package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket4771Test {

    @Test
    public void testArraySchemaItemsValidation(){
        ModelConverters.reset();
        System.clearProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY);
        ResolvedSchema schema = ModelConverters.getInstance(false, Schema.SchemaResolution.INLINE).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass[].class));
        String expectedJson = "{\n" +
                "  \"schema\" : {\n" +
                "    \"type\" : \"array\",\n" +
                "    \"items\" : {\n" +
                "      \"type\" : \"object\",\n" +
                "      \"properties\" : {\n" +
                "        \"foo\" : {\n" +
                "          \"type\" : \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"referencedSchemas\" : {\n" +
                "    \"CustomClass\" : {\n" +
                "      \"type\" : \"object\",\n" +
                "      \"properties\" : {\n" +
                "        \"foo\" : {\n" +
                "          \"type\" : \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(schema, expectedJson);
        ModelConverters.reset();
        schema = ModelConverters.getInstance(true, Schema.SchemaResolution.INLINE).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass[].class));
        expectedJson = "{\n" +
                "  \"schema\" : {\n" +
                "    \"type\" : \"array\",\n" +
                "    \"items\" : {\n" +
                "      \"$ref\" : \"#/components/schemas/CustomClass\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"referencedSchemas\" : {\n" +
                "    \"CustomClass\" : {\n" +
                "      \"type\" : \"object\",\n" +
                "      \"properties\" : {\n" +
                "        \"foo\" : {\n" +
                "          \"type\" : \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson31(schema, expectedJson);
        ModelConverters.reset();
        System.setProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY, "true");
        schema = ModelConverters.getInstance(true, Schema.SchemaResolution.INLINE).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass[].class));
        expectedJson = "{\n" +
                "  \"schema\" : {\n" +
                "    \"type\" : \"array\",\n" +
                "    \"items\" : {\n" +
                "      \"type\" : \"object\",\n" +
                "      \"properties\" : {\n" +
                "        \"foo\" : {\n" +
                "          \"type\" : \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"referencedSchemas\" : {\n" +
                "    \"CustomClass\" : {\n" +
                "      \"type\" : \"object\",\n" +
                "      \"properties\" : {\n" +
                "        \"foo\" : {\n" +
                "          \"type\" : \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson31(schema, expectedJson);
        System.clearProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY);
        ModelConverters.reset();


    }

    private static class CustomClass {
        public String foo;
    }
}
