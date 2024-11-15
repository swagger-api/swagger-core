package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public class Ticket4760Test {

    @Test
    public void testArraySchemaItemsValidation(){
        final Map<String, Schema> stringSchemaMap = ModelConverters.getInstance().readAll(ClassWithArraySchemaItemsValidation.class);
        final String expectedJson = "{\n" +
                "  \"ClassWithArraySchemaItemsValidation\" : {\n" +
                "    \"type\" : \"object\",\n" +
                "    \"properties\" : {\n" +
                "      \"setOfEnums\" : {\n" +
                "        \"maxItems\" : 3,\n" +
                "        \"minItems\" : 1,\n" +
                "        \"type\" : \"array\",\n" +
                "        \"items\" : {\n" +
                "          \"type\" : \"string\",\n" +
                "          \"enum\" : [ \"green\", \"blue\" ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(stringSchemaMap, expectedJson);
    }

    private static class ClassWithArraySchemaItemsValidation{

        public enum MyEnum {
            red,
            green
        }

        public enum MyOtherEnum {
            green,
            blue
        }

        @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = MyOtherEnum.class))
        @Size(min = 1, max = 3)
        private List<MyEnum> setOfEnums;

        public List<MyEnum> getSetOfEnums() {
            return setOfEnums;
        }

        public void setSetOfEnums(List<MyEnum> setOfEnums) {
            this.setOfEnums = setOfEnums;
        }
    }
}
