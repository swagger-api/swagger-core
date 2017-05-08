package io.swagger.model.override;

import io.swagger.annotations.media.OASSchema;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class ModelPropertyOverrideTest {
    @Test
    public void overrideTest() throws Exception {
        ModelConverters.getInstance().addConverter(new SamplePropertyConverter());
        final Map<String, Schema> model = ModelConverters.getInstance().read(MyPojo.class);
        final String expected = "{" +
                "  \"MyPojo\" : {" +
                "    \"title\" : \"MyPojo\"," +
                "    \"type\" : \"object\"," +
                "    \"properties\" : {" +
                "      \"id\" : {" +
                "        \"title\" : \"id\"" +
                "        \"type\" : \"string\"" +
                "      }," +
                "      \"myCustomClass\" : {" +
                "        \"title\" : \"myCustomClass\"," +
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

        @OASSchema(description = "instead of modeling this class in the documentation, we will model a string")
        public MyCustomClass getMyCustomClass() {
            return null;
        }

        public void setMyCustomClass(MyCustomClass myCustomClass) {
        }
    }
}
