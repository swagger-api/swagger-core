package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRecord;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRequiredClass;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRequiredProperty;
import org.testng.annotations.Test;

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
}
