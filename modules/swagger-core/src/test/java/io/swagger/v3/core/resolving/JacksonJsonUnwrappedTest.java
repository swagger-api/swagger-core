package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedDisabledClass;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedPrefixSuffixClass;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRecord;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRequiredClass;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRequiredProperty;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class JacksonJsonUnwrappedTest {

  @Test(description = "test the @JsonUnwrapped behaviour when required Properties")
  public void jacksonJsonUnwrappedTest() {

    SerializationMatchers
        .assertEqualsToYaml(ModelConverters.getInstance().read(
            JacksonUnwrappedRequiredProperty.class), "InnerTypeRequired:\n" +
            "  required:\n" +
            "  - name\n" +
            "  type: object\n" +
            "  properties:\n" +
            "    foo:\n" +
            "      type: integer\n" +
            "      format: int32\n" +
            "    name:\n" +
            "      type: string\n" +
            "JacksonUnwrappedRequiredProperty:\n" +
            "  required:\n" +
            "  - name\n" +
            "  type: object\n" +
            "  properties:\n" +
                "    foo:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    name:\n" +
                "      type: string\n");
    }

    @Test(description = "OAS 3.1: a required @JsonUnwrapped record component must not leave an empty property, nor Jackson 3's synthetic creator property")
    public void jacksonJsonUnwrappedRecordTest31() {
        SerializationMatchers
                .assertEqualsToYaml31(ModelConverters.getInstance(true).read(
                        JacksonUnwrappedRecord.class), "JacksonUnwrappedRecord:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    value:\n" +
                        "      type: string\n" +
                        "    number:\n" +
                        "      type: integer\n" +
                        "      format: int32\n" +
                        "  required:\n" +
                        "  - number\n" +
                        "  - value\n");
    }

    @Test(description = "OAS 3.1: a required @JsonUnwrapped field must not leave an empty property")
    public void jacksonJsonUnwrappedRequiredFieldTest31() {
        SerializationMatchers
                .assertEqualsToYaml31(ModelConverters.getInstance(true).read(
                        JacksonUnwrappedRequiredClass.class), "JacksonUnwrappedRequiredClass:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    value:\n" +
                        "      type: string\n" +
                        "  required:\n" +
                        "  - value\n" +
                        "Inner:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    value:\n" +
                        "      type: string\n" +
                        "  required:\n" +
                        "  - value\n");
    }

    @Test(description = "OAS 3.0: a required @JsonUnwrapped field must not leave an empty property")
    public void jacksonJsonUnwrappedRequiredFieldTest30() {
        SerializationMatchers
                .assertEqualsToYaml(ModelConverters.getInstance().read(
                        JacksonUnwrappedRequiredClass.class), "JacksonUnwrappedRequiredClass:\n" +
                        "  required:\n" +
                        "  - value\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    value:\n" +
                        "      type: string\n" +
                        "Inner:\n" +
                        "  required:\n" +
                        "  - value\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    value:\n" +
                        "      type: string\n");
    }

    @Test(description = "OAS 3.0: a required @JsonUnwrapped record component must not leave an empty property")
    public void jacksonJsonUnwrappedRecordTest30() {
        SerializationMatchers
                .assertEqualsToYaml(ModelConverters.getInstance().read(
                        JacksonUnwrappedRecord.class), "JacksonUnwrappedRecord:\n" +
                        "  required:\n" +
                        "  - number\n" +
                        "  - value\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    value:\n" +
                        "      type: string\n" +
                        "    number:\n" +
                        "      type: integer\n" +
                        "      format: int32\n");
    }

    @Test(description = "OAS 3.1: required @JsonUnwrapped with prefix and suffix must not leave an empty property")
    public void jacksonJsonUnwrappedPrefixSuffixTest31() {
        assertNoEmptyUnwrappedProperty(ModelConverters.getInstance(true).read(JacksonUnwrappedPrefixSuffixClass.class)
                .get("JacksonUnwrappedPrefixSuffixClass"));
    }

    @Test(description = "OAS 3.0: required @JsonUnwrapped with prefix and suffix must not leave an empty property")
    public void jacksonJsonUnwrappedPrefixSuffixTest30() {
        assertNoEmptyUnwrappedProperty(ModelConverters.getInstance().read(JacksonUnwrappedPrefixSuffixClass.class)
                .get("JacksonUnwrappedPrefixSuffixClass"));
    }

    @Test(description = "OAS 3.1: @JsonUnwrapped(enabled = false) keeps the member as a regular property")
    public void jacksonJsonUnwrappedDisabledTest31() {
        SerializationMatchers
                .assertEqualsToYaml31(ModelConverters.getInstance(true).read(
                        JacksonUnwrappedDisabledClass.class), "JacksonUnwrappedDisabledClass:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    inner:\n" +
                        "      $ref: \"#/components/schemas/Inner\"\n" +
                        "  required:\n" +
                        "  - inner\n");
    }

    @Test(description = "OAS 3.0: @JsonUnwrapped(enabled = false) keeps the member as a regular property")
    public void jacksonJsonUnwrappedDisabledTest30() {
        SerializationMatchers
                .assertEqualsToYaml(ModelConverters.getInstance().read(
                        JacksonUnwrappedDisabledClass.class), "JacksonUnwrappedDisabledClass:\n" +
                        "  required:\n" +
                        "  - inner\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    inner:\n" +
                        "      $ref: \"#/components/schemas/Inner\"\n");
    }

    private static void assertNoEmptyUnwrappedProperty(Schema<?> model) {
        assertNotNull(model);
        assertTrue(model.getProperties().containsKey("in_value_x"));
        assertTrue(model.getProperties().containsKey("number"));
        assertFalse(model.getProperties().containsKey("inner"));
        assertFalse(model.getRequired().contains("inner"));
    }
}
