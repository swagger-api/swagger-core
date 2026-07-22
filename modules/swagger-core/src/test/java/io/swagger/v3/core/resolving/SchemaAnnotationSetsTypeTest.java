package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

public class SchemaAnnotationSetsTypeTest {

    @Test(description = "Setting type in @Schema is applied for OAS 3.0")
    public void testTypeInSchemaAnnotationIsAppliedForOAS30() {
        String expectedYaml = "ClassWithFieldsUsingOAS30Type:\n" +
                              "  type: object\n" +
                              "  properties:\n" +
                              "    inferred:\n" +
                              "      type: number\n" +
                              "    amount:\n" +
                              "      type: boolean\n" +
                              "    count:\n" +
                              "      type: boolean\n" +
                              "    flag:\n" +
                              "      type: integer\n" +
                              "      format: int32\n" +
                              "    unit:\n" +
                              "      type: string\n" +
                              "      enum:\n" +
                              "      - DAY\n" +
                              "      - WEEK\n" +
                              "      - MONTH";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance().readAll(ClassWithFieldsUsingOAS30Type.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    @Test(description = "Setting types in @Schema is ignored for OAS 3.0")
    public void testTypesInSchemaAnnotationIsIgnoredForOAS30() {
        String expectedYaml = "ClassWithFieldsUsingOAS31Types:\n" +
                              "  type: object\n" +
                              "  properties:\n" +
                              "    inferred:\n" +
                              "      type: number\n" +
                              "    amount:\n" +
                              "      type: number\n" +
                              "    count:\n" +
                              "      type: integer\n" +
                              "      format: int32\n" +
                              "    flag:\n" +
                              "      type: boolean\n" +
                              "    unit:\n" +
                              "      type: string\n" +
                              "      enum:\n" +
                              "      - DAY\n" +
                              "      - WEEK\n" +
                              "      - MONTH";

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance().readAll(ClassWithFieldsUsingOAS31Types.class);
        SerializationMatchers.assertEqualsToYaml(stringSchemaMap, expectedYaml);
    }

    @Test(description = "Setting type in @Schema is ignored for OAS 3.1")
    public void testTypeInSchemaAnnotationIsIgnoredForOAS31() {
        String expectedYaml = "ClassWithFieldsUsingOAS30Type:\n" +
                              "  type: object\n" +
                              "  properties:\n" +
                              "    inferred:\n" +
                              "      type: number\n" +
                              "    amount:\n" +
                              "      type: number\n" +
                              "    count:\n" +
                              "      type: integer\n" +
                              "      format: int32\n" +
                              "    flag:\n" +
                              "      type: boolean\n" +
                              "    unit:\n" +
                              "      type: string\n" +
                              "      enum:\n" +
                              "      - DAY\n" +
                              "      - WEEK\n" +
                              "      - MONTH";

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance(true)
                .readAll(ClassWithFieldsUsingOAS30Type.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    @Test(description = "Setting types in @Schema is applied for OAS 3.1")
    public void testTypesInSchemaAnnotationIsAppliedForOAS31() {
        String expectedYaml = "ClassWithFieldsUsingOAS31Types:\n" +
                              "  type: object\n" +
                              "  properties:\n" +
                              "    inferred:\n" +
                              "      type: number\n" +
                              "    amount:\n" +
                              "      type: boolean\n" +
                              "    count:\n" +
                              "      type: boolean\n" +
                              "      format: int32\n" +
                              "    flag:\n" +
                              "      type: integer\n" +
                              "    unit:\n" +
                              "      type: string\n" +
                              "      enum:\n" +
                              "      - DAY\n" +
                              "      - WEEK\n" +
                              "      - MONTH";

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance(true)
                .readAll(ClassWithFieldsUsingOAS31Types.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    @Test(description = "Setting several types in @Schema is applied for OAS 3.1")
    public void testSeveralTypesInSchemaAnnotationIsAppliedForOAS31() {
        String expectedYaml = "ClassWithFieldsUsingOAS31TypesDefiningSeveralTypes:\n" +
                              "  type: object\n" +
                              "  properties:\n" +
                              "    inferred:\n" +
                              "      type: number\n" +
                              "    amount:\n" +
                              "      type:\n" +
                              "      - boolean\n" +
                              "      - number\n" +
                              "    count:\n" +
                              "      type:\n" +
                              "      - boolean\n" +
                              "      - number\n" +
                              "      format: int32\n" +
                              "    flag:\n" +
                              "      type:\n" +
                              "      - integer\n" +
                              "      - number\n" +
                              "    unit:\n" +
                              "      type: string\n" +
                              "      enum:\n" +
                              "      - DAY\n" +
                              "      - WEEK\n" +
                              "      - MONTH";

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance(true)
                .readAll(ClassWithFieldsUsingOAS31TypesDefiningSeveralTypes.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    private static class ClassWithFieldsUsingOAS30Type {
        @io.swagger.v3.oas.annotations.media.Schema
        public BigDecimal inferred;
        @io.swagger.v3.oas.annotations.media.Schema(type = "boolean")
        public BigDecimal amount;
        @io.swagger.v3.oas.annotations.media.Schema(type = "boolean")
        public Integer count;
        @io.swagger.v3.oas.annotations.media.Schema(type = "integer")
        public Boolean flag;
        @io.swagger.v3.oas.annotations.media.Schema
        public Frequency unit;

        enum Frequency { DAY, WEEK, MONTH }
    }

    private static class ClassWithFieldsUsingOAS31Types {
        @io.swagger.v3.oas.annotations.media.Schema
        public BigDecimal inferred;
        @io.swagger.v3.oas.annotations.media.Schema(types = {"boolean"})
        public BigDecimal amount;
        @io.swagger.v3.oas.annotations.media.Schema(types = {"boolean"})
        public Integer count;
        @io.swagger.v3.oas.annotations.media.Schema(types = {"integer"})
        public Boolean flag;
        @io.swagger.v3.oas.annotations.media.Schema
        public Frequency unit;

        enum Frequency { DAY, WEEK, MONTH }
    }

    private static class ClassWithFieldsUsingOAS31TypesDefiningSeveralTypes {
        @io.swagger.v3.oas.annotations.media.Schema
        public BigDecimal inferred;
        @io.swagger.v3.oas.annotations.media.Schema(types = {"boolean", "number"})
        public BigDecimal amount;
        @io.swagger.v3.oas.annotations.media.Schema(types = {"boolean", "number"})
        public Integer count;
        @io.swagger.v3.oas.annotations.media.Schema(types = {"integer", "number"})
        public Boolean flag;
        @io.swagger.v3.oas.annotations.media.Schema
        public Frequency unit;

        enum Frequency { DAY, WEEK, MONTH }
    }

}
