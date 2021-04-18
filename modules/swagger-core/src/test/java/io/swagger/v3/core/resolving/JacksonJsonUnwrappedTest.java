package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
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
}
