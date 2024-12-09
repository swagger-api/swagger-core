package io.swagger.v3.core.resolving;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.Schema.SchemaResolution;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Ticket4804Test {

    @BeforeMethod
    public void beforeTest() {
        ModelConverters.reset();
        System.clearProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY);
    }

    @Test
    public void shouldIncludeOnlyNonGroupedJakartaValidatedFieldsAsMandatoryByDefault() {
        ResolvedSchema schema = ModelConverters.getInstance(false).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass.class));
        String expectedJson = "{\"schema\":{\"required\":[\"nonGroupValidatedField\"],\"type\":\"object\",\"properties\":{\"nonGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField\":{\"type\":\"integer\",\"format\":\"int32\"},\"multipleGroupValidatedField\":{\"type\":\"number\"},\"otherGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField2\":{\"type\":\"string\"}}},\"referencedSchemas\":{\"CustomClass\":{\"required\":[\"nonGroupValidatedField\"],\"type\":\"object\",\"properties\":{\"nonGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField\":{\"type\":\"integer\",\"format\":\"int32\"},\"multipleGroupValidatedField\":{\"type\":\"number\"},\"otherGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField2\":{\"type\":\"string\"}}}}}";
        SerializationMatchers.assertEqualsToJson(schema, expectedJson);
    }

    @Test
    public void shouldIncludeAllJakartaValidatedFieldsAsMandatoryIfFilterAlwaysTrue() {
        ResolvedSchema schema = ModelConverters.getInstance(false, SchemaResolution.DEFAULT, annotationGroups -> true).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass.class));
        String expectedJson = "{\"schema\":{\"required\":[\"nonGroupValidatedField\",\"singleGroupValidatedField2\"],\"type\":\"object\",\"properties\":{\"nonGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField\":{\"minimum\":1,\"type\":\"integer\",\"format\":\"int32\"},\"multipleGroupValidatedField\":{\"minimum\":1,\"exclusiveMinimum\":false,\"type\":\"number\"},\"otherGroupValidatedField\":{\"pattern\":\".*\",\"type\":\"string\"},\"singleGroupValidatedField2\":{\"type\":\"string\"}}},\"referencedSchemas\":{\"CustomClass\":{\"required\":[\"nonGroupValidatedField\",\"singleGroupValidatedField2\"],\"type\":\"object\",\"properties\":{\"nonGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField\":{\"minimum\":1,\"type\":\"integer\",\"format\":\"int32\"},\"multipleGroupValidatedField\":{\"minimum\":1,\"exclusiveMinimum\":false,\"type\":\"number\"},\"otherGroupValidatedField\":{\"pattern\":\".*\",\"type\":\"string\"},\"singleGroupValidatedField2\":{\"type\":\"string\"}}}}}";
        SerializationMatchers.assertEqualsToJson(schema, expectedJson);
    }


    @Test
    public void shouldIncludeOnlyJakartaValidatedFieldsMatchingConditionInFilter() {
        ResolvedSchema schema = ModelConverters.getInstance(false, SchemaResolution.ALL_OF, annotationGroups -> annotationGroups.length == 0 || Arrays.stream(annotationGroups).anyMatch(group -> group == ValidationGroup.class)).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass.class));
        String expectedJson = "{\"schema\":{\"required\":[\"nonGroupValidatedField\",\"singleGroupValidatedField2\"],\"type\":\"object\",\"properties\":{\"nonGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField\":{\"minimum\":1,\"type\":\"integer\",\"format\":\"int32\"},\"multipleGroupValidatedField\":{\"minimum\":1,\"exclusiveMinimum\":false,\"type\":\"number\"},\"otherGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField2\":{\"type\":\"string\"}}},\"referencedSchemas\":{\"CustomClass\":{\"required\":[\"nonGroupValidatedField\",\"singleGroupValidatedField2\"],\"type\":\"object\",\"properties\":{\"nonGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField\":{\"minimum\":1,\"type\":\"integer\",\"format\":\"int32\"},\"multipleGroupValidatedField\":{\"minimum\":1,\"exclusiveMinimum\":false,\"type\":\"number\"},\"otherGroupValidatedField\":{\"type\":\"string\"},\"singleGroupValidatedField2\":{\"type\":\"string\"}}}}}";
        SerializationMatchers.assertEqualsToJson(schema, expectedJson);
    }


    private interface ValidationGroup {

    }

    private interface OtherValidationGroup {

    }


    private static class CustomClass {

        @NotNull
        public String nonGroupValidatedField;
        @Min(value = 1, groups = ValidationGroup.class)
        public Integer singleGroupValidatedField;
        @DecimalMin(value = "1.0", groups = {ValidationGroup.class, OtherValidationGroup.class})
        public BigDecimal multipleGroupValidatedField;
        @Pattern(regexp = ".*", groups = OtherValidationGroup.class)
        public String otherGroupValidatedField;
        @NotEmpty(groups = ValidationGroup.class)
        public String singleGroupValidatedField2;
    }
}
