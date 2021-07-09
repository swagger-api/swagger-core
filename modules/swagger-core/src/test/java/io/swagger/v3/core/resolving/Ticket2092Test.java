package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.Model2092;
import io.swagger.v3.core.oas.models.Model2092WithJsonValueAnnotation;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class Ticket2092Test {
    @Test(description = "it should extract enum values from fields using @XmlEnumValue")
    public void test2092() {
        ModelConverters modelConverters = ModelConverters.getInstance();
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(Model2092.class);
        assertEquals(schemas.size(), 1);

        final String yaml = "Model2092:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    enumValue:\n" +
                "      type: string\n" +
                "      enum:\n" +
                "      - jsonPrivate\n" +
                "      - xmlPublic\n" +
                "      - jsonSystem\n" +
                "      - invite only\n" +
                "      - A_NOTHING\n";
        SerializationMatchers.assertEqualsToYaml(schemas, yaml);
    }

    @Test(description = "it should extract enum values from fields using @XmlEnumValue")
    public void test2092WithJsonValueAnnotation() {
        ModelConverters modelConverters = ModelConverters.getInstance();
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(Model2092WithJsonValueAnnotation.class);
        assertEquals(schemas.size(), 1);

        final String yaml = "Model2092WithJsonValueAnnotation:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    enumValue:\n" +
                "      type: string\n" +
                "      enum:\n" +
                "      - \"0\"\n" +
                "      - \"1\"\n" +
                "      - \"2\"\n" +
                "      - \"3\"\n" +
                "      - \"4\"\n";
        SerializationMatchers.assertEqualsToYaml(schemas, yaml);
    }

}
