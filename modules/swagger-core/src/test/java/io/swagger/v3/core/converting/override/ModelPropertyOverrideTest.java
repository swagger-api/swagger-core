package io.swagger.v3.core.converting.override;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converting.override.resources.MyCustomClass;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.BidimensionalArray;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class ModelPropertyOverrideTest {
    @Test
    public void overrideTest() throws Exception {
        ModelConverters.getInstance().addConverter(new SamplePropertyConverter());
        final Map<String, Schema> model = ModelConverters.getInstance().read(MyPojo.class);
        final String expected = "{" +
                "  \"MyPojo\" : {" +
                "    \"type\" : \"object\"," +
                "    \"properties\" : {" +
                "      \"id\" : {" +
                "        \"type\" : \"string\"" +
                "      }," +
                "      \"myCustomClass\" : {" +
                "        \"type\" : \"string\"," +
                "        \"format\" : \"date-time\"" +
                "      }" +
                "    }" +
                "  }" +
                "}";
        SerializationMatchers.assertEqualsToJson(model, expected);
    }

    @Test
    public void extendedOverrideTest() throws Exception {
        ModelConverters.getInstance().addConverter(new SamplePropertyExtendedConverter(Json.mapper()));
        final Map<String, Schema> model = ModelConverters.getInstance().read(MyPojo.class);
        final String expected = "{" +
                "  \"MyPojo\" : {" +
                "    \"type\" : \"object\"," +
                "    \"properties\" : {" +
                "      \"id\" : {" +
                "        \"type\" : \"string\"" +
                "      }," +
                "      \"myCustomClass\" : {" +
                "        \"type\" : \"string\"," +
                "        \"format\" : \"date-time\"," +
                "        \"description\" : \"instead of modeling this class in the documentation, we will model a string\"" +
                "      }" +
                "    }" +
                "  }" +
                "}";
        SerializationMatchers.assertEqualsToJson(model, expected);
    }

    public static class MyPojo {
        public String getId() {
            return "";
        }

        public void setId(String id) {
        }

        @io.swagger.v3.oas.annotations.media.Schema(required = false, description = "instead of modeling this class in the documentation, we will model a string")
        public MyCustomClass getMyCustomClass() {
            return null;
        }

        public void setMyCustomClass(MyCustomClass myCustomClass) {
        }
    }

    @Test
    public void customAnnotationTest() throws Exception {
        ModelConverters.getInstance().addConverter(new CustomAnnotationConverter(Json.mapper()));
        final Map<String, Schema> model = ModelConverters.getInstance().read(BidimensionalArray.class);
        final String expected = "BidimensionalArray:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    withCustomAnnotation:\n" +
                "      maxItems: 3\n" +
                "      type: array\n" +
                "      items:\n" +
                "        maxItems: 2\n" +
                "        type: array\n" +
                "        items:\n" +
                "          type: string\n" +
                "    withHelperClass:\n" +
                "      maxItems: 3\n" +
                "      type: array\n" +
                "      items:\n" +
                "        maxItems: 2\n" +
                "        type: array\n" +
                "        items:\n" +
                "          type: string\n" +
                "    sized:\n" +
                "      maxItems: 2\n" +
                "      minItems: 0\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n";
        SerializationMatchers.assertEqualsToYaml(model, expected);
    }
}
