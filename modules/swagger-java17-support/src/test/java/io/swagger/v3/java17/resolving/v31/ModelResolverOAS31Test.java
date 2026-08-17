package io.swagger.v3.java17.resolving.v31;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.java17.resolving.SwaggerTestBase;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;
import io.swagger.v3.java17.matchers.SerializationMatchers;
import java.util.List;
import java.util.Map;

public class ModelResolverOAS31Test extends SwaggerTestBase {

    @Test
    public void testOAS31JavaRecord() {
        String expectedYaml = "JavaRecordWithOAS31Fields:\n" +
                "  type: object\n" +
                "  $comment: Random comment at schema level\n" +
                "  $id: http://yourdomain.com/schemas/myschema.json\n" +
                "  description: this is model for testing OAS 3.1 Java Record resolving\n" +
                "  properties:\n" +
                "    test:\n" +
                "      type: string\n" +
                "    isLatest:\n" +
                "      type: boolean\n" +
                "    randomList:\n" +
                "      type: array\n" +
                "      contains:\n" +
                "        type: string\n" +
                "      items:\n" +
                "        type: string\n" +
                "      maxContains: 10\n" +
                "      minContains: 1\n" +
                "      prefixItems:\n" +
                "      - type: string\n" +
                "      unevaluatedItems:\n" +
                "        type: number\n" +
                "    Status:\n" +
                "      type:\n" +
                "      - string\n" +
                "      - number\n";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(JavaRecordWithOAS31Fields.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    @io.swagger.v3.oas.annotations.media.Schema(
            $id = "http://yourdomain.com/schemas/myschema.json",
            description = "this is model for testing OAS 3.1 Java Record resolving",
            $comment = "Random comment at schema level",
            types = "object"
    )
    private record JavaRecordWithOAS31Fields(
            String test,
            boolean isLatest,
            @ArraySchema(
                    maxContains = 10,
                    minContains = 1,
                    contains = @io.swagger.v3.oas.annotations.media.Schema(
                            types = "string"
                    ),
                    unevaluatedItems = @io.swagger.v3.oas.annotations.media.Schema(
                            types = "number"
                    ),
                    prefixItems = {
                            @io.swagger.v3.oas.annotations.media.Schema(
                                    types = "string"
                            )
                    }
            )
            List<String> randomList,
            @io.swagger.v3.oas.annotations.media.Schema(types = {
                    "string",
                    "number"
            })
            Object Status
    ){
    }
}
