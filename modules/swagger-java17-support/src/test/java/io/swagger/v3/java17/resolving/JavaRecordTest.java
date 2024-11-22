package io.swagger.v3.java17.resolving;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.java17.matchers.SerializationMatchers;
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

    @Test
    public void testJavaRecordWithBeanValidationSizeTypeUse() {
        String expectedYaml = "JavaRecordWithAnnotationsOnGenericType:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    randomList:\n" +
                "      maxItems: 10000\n" +
                "      minItems: 100\n" +
                "      type: array\n" +
                "      items:\n" +
                "        maxLength: 10\n" +
                "        minLength: 1\n" +
                "        type: string\n" +
                "    secondList:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        pattern: (.+?)@(.+?)\n" +
                "        type: string\n" +
                "    id:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        maximum: 10000\n" +
                "        minimum: 1\n" +
                "        type: integer\n" +
                "        format: int32";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordWithAnnotationsOnGenericType.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
        }

    public record JavaRecordWithAnnotationsOnGenericType(
            @Size(min = 100, max = 10000)
            List<@Size(min = 1, max = 10) String> randomList,
            List<@Pattern(regexp = "(.+?)@(.+?)") String> secondList,
            List<@Min(1)@Max(10000) Integer> id
    ){
    }

    @Test
    public void testJavaRecordWithJsonPropertyAnnotationNotMatchingFieldName() {
        String expectedYaml = "JavaRecordWithJsonPropertyAnnotationNotMatchingFieldName:\n" +
            "  type: object\n" +
            "  properties:\n" +
            "    listOfStrings:\n" +
            "      type: array\n" +
            "      items:\n" +
            "        maxLength: 5\n" +
            "        minLength: 1\n" +
            "        type: string";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordWithJsonPropertyAnnotationNotMatchingFieldName.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    public record JavaRecordWithJsonPropertyAnnotationNotMatchingFieldName(
        @JsonProperty("listOfStrings") List<@Size(min = 1, max = 5)String> stringList
    ) { }

}
