package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticket4474Test extends SwaggerTestBase {

    @AfterMethod
    public void afterTest() {
        ModelResolver.enumsAsRef = false;
    }

    @Test
    public void testAnyOf() throws Exception {
        ModelResolver.enumsAsRef = true;

        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(Document.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Document:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    data:\n" +
                "      type: object\n" +
                "      additionalProperties:\n" +
                "        type: object\n" +
                "        anyOf:\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: boolean\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: integer\n" +
                "            format: int64\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: number\n" +
                "            format: double\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: string\n" +
                "        - type: boolean\n" +
                "        - type: integer\n" +
                "          format: int32\n" +
                "        - type: integer\n" +
                "          format: int64\n" +
                "    listData:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: object\n" +
                "        anyOf:\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: boolean\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: integer\n" +
                "            format: int64\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: number\n" +
                "            format: double\n" +
                "        - type: array\n" +
                "          items:\n" +
                "            type: string\n" +
                "        - type: boolean\n" +
                "        - type: integer\n" +
                "          format: int32\n" +
                "        - type: integer\n" +
                "          format: int64\n" +
                "    itemData:\n" +
                "      type: object\n" +
                "      anyOf:\n" +
                "      - type: array\n" +
                "        items:\n" +
                "          type: boolean\n" +
                "      - type: array\n" +
                "        items:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      - type: array\n" +
                "        items:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "      - type: array\n" +
                "        items:\n" +
                "          type: number\n" +
                "          format: double\n" +
                "      - type: array\n" +
                "        items:\n" +
                "          type: string\n" +
                "      - type: boolean\n" +
                "      - type: integer\n" +
                "        format: int32\n" +
                "      - type: integer\n" +
                "        format: int64");
    }

    static class Document {
        @io.swagger.v3.oas.annotations.media.Schema(
                anyOf = {
                        Boolean[].class,
                        Integer[].class,
                        Long[].class,
                        Double[].class,
                        String[].class,
                        Boolean.class,
                        Integer.class,
                        Long.class,
                        Map.class,
                })
        public Map<String, Object> data = new HashMap<>();

        @io.swagger.v3.oas.annotations.media.Schema(
                anyOf = {
                        Boolean[].class,
                        Integer[].class,
                        Long[].class,
                        Double[].class,
                        String[].class,
                        Boolean.class,
                        Integer.class,
                        Long.class,
                        Map.class,
                })
        public List<Object> listData = new ArrayList<>();

        @io.swagger.v3.oas.annotations.media.Schema(
                anyOf = {
                        Boolean[].class,
                        Integer[].class,
                        Long[].class,
                        Double[].class,
                        String[].class,
                        Boolean.class,
                        Integer.class,
                        Long.class,
                        Map.class,
                })
        public Object itemData = new Object();
    }
}
