package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class JavaRecordTest {

    @Test
    public void testJavaRecordWithSchema() {
        String expectedYaml = "JavaRecordClassWithSchema:\n" +
                "  type: object\n" +
                "  description: Java Record with Schema Test\n" +
                "  properties:\n" +
                "    test:\n" +
                "      type: string\n" +
                "      description: Testing of Schema on fields processing in Java Records\n" +
                "    isLatest:\n" +
                "      type: boolean";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordClassWithSchema.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    @Test
    public void testJavaRecordWithBeanValidation() {
        String expectedYaml = "JavaRecordClassWithBeanValidation:\n" +
                "  type: object\n" +
                "  description: Java Record with Bean Validation Test\n" +
                "  properties:\n" +
                "    test:\n" +
                "      maxLength: 100\n" +
                "      minLength: 1\n" +
                "      type: string\n" +
                "    isLatest:\n" +
                "      type: boolean\n" +
                "    randomList:\n" +
                "      maxItems: 101\n" +
                "      minItems: 2\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n" +
                "    myField:\n" +
                "      maximum: 100\n" +
                "      exclusiveMaximum: false\n" +
                "      minimum: 1\n" +
                "      exclusiveMinimum: false\n" +
                "      type: number\n" +
                "    email:\n" +
                "      pattern: (.+?)@(.+?)\n" +
                "      type: string";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordClassWithBeanValidation.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "Java Record with Schema Test")
    public record JavaRecordClassWithSchema(
            @io.swagger.v3.oas.annotations.media.Schema(description = "Testing of Schema on fields processing in Java Records") String test,
            boolean isLatest
    ){
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "Java Record with Bean Validation Test")
    public record JavaRecordClassWithBeanValidation(
            @Size(min = 1, max = 100) String test,
            boolean isLatest,
            @Size(min = 2, max = 101)
            List<String> randomList,
            @DecimalMin("1")
            @DecimalMax("100")
            Number myField,
            @Pattern(regexp = "(.+?)@(.+?)")
            String email
    ){
    }

}
