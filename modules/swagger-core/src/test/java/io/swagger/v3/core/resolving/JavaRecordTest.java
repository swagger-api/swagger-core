package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class JavaRecordTest {

    @Test
    public void testJavaRecordWithSchema() {
        String expectedYaml = """
                JavaRecordClassWithSchema:
                  type: object
                  description: Java Record with Schema Test
                  properties:
                    test:
                      type: string
                      description: Testing of Schema on fields processing in Java Records
                    isLatest:
                      type: boolean""";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordClassWithSchema.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    @Test
    public void testJavaRecordWithBeanValidation() {
        String expectedYaml = """
                JavaRecordClassWithBeanValidation:
                  type: object
                  description: Java Record with Bean Validation Test
                  properties:
                    test:
                      maxLength: 100
                      minLength: 1
                      type: string
                    isLatest:
                      type: boolean
                    randomList:
                      maxItems: 101
                      minItems: 2
                      type: array
                      items:
                        type: string
                    myField:
                      maximum: 100
                      exclusiveMaximum: false
                      minimum: 1
                      exclusiveMinimum: false
                      type: number
                    email:
                      pattern: (.+?)@(.+?)
                      type: string""";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordClassWithBeanValidation.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "Java Record with Schema Test")
    public record JavaRecordClassWithSchema(
            @io.swagger.v3.oas.annotations.media.Schema(description = "Testing of Schema on fields processing in Java Records") String test,
            boolean isLatest
    ) {
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
    ) {
    }

    @Test
    public void testJavaRecordWithBeanValidationSizeTypeUse() {
        String expectedYaml = """
                JavaRecordWithAnnotationsOnGenericType:
                  type: object
                  properties:
                    randomList:
                      maxItems: 10000
                      minItems: 100
                      type: array
                      items:
                        maxLength: 10
                        minLength: 1
                        type: string
                    secondList:
                      type: array
                      items:
                        pattern: (.+?)@(.+?)
                        type: string
                    id:
                      type: array
                      items:
                        maximum: 10000
                        minimum: 1
                        type: integer
                        format: int32""";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordWithAnnotationsOnGenericType.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    public record JavaRecordWithAnnotationsOnGenericType(
            @Size(min = 100, max = 10000)
            List<@Size(min = 1, max = 10) String> randomList,
            List<@Pattern(regexp = "(.+?)@(.+?)") String> secondList,
            List<@Min(1) @Max(10000) Integer> id
    ) {
    }

    @Test
    public void testJavaRecordWithJsonPropertyAnnotationNotMatchingFieldName() {
        String expectedYaml = """
                JavaRecordWithJsonPropertyAnnotationNotMatchingFieldName:
                  type: object
                  properties:
                    listOfStrings:
                      type: array
                      items:
                        maxLength: 5
                        minLength: 1
                        type: string""";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(false).readAll(JavaRecordWithJsonPropertyAnnotationNotMatchingFieldName.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    public record JavaRecordWithJsonPropertyAnnotationNotMatchingFieldName(
            @JsonProperty("listOfStrings") List<@Size(min = 1, max = 5) String> stringList
    ) {
    }

}
