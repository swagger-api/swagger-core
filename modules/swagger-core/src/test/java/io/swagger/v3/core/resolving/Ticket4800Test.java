package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class Ticket4800Test extends SwaggerTestBase{

    @Test(description = "Custom schema implementation in property and enum as ref type value")
    public void testCustomSchemaImplementation() {

        String expectedYaml = "ModelWithCustomSchemaImplementationInProperty:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    enumExampleFieldWithImplementationProp:\n" +
                "      $ref: \"#/components/schemas/MyEnum\"\n" +
                "      default: \"yes\"\n" +
                "      description: Prop description\n" +
                "    secondExampleFieldWithTypeProp:\n" +
                "      type: string\n" +
                "MyEnum:\n" +
                "  type: string\n" +
                "  enum:\n" +
                "  - \"yes\"\n" +
                "  - \"no\"";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(Ticket4800Test.ModelWithCustomSchemaImplementationInProperty.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    static class ModelWithCustomSchemaImplementationInProperty {

        @io.swagger.v3.oas.annotations.media.Schema(implementation = MyEnum.class, description = "Prop description", defaultValue = "yes", enumAsRef = true)
        private MyEnum enumExampleFieldWithImplementationProp;

        @io.swagger.v3.oas.annotations.media.Schema(type = "string", enumAsRef = true)
        private MyEnum2 secondExampleFieldWithTypeProp;

        public MyEnum getEnumExampleFieldWithImplementationProp() {
            return enumExampleFieldWithImplementationProp;
        }

        public void setEnumExampleFieldWithImplementationProp(MyEnum enumExampleFieldWithImplementationProp) {
            this.enumExampleFieldWithImplementationProp = enumExampleFieldWithImplementationProp;
        }

        public MyEnum2 getSecondExampleFieldWithTypeProp() {
            return secondExampleFieldWithTypeProp;
        }

        public void setSecondExampleFieldWithTypeProp(MyEnum2 secondExampleFieldWithTypeProp) {
            this.secondExampleFieldWithTypeProp = secondExampleFieldWithTypeProp;
        }

        enum MyEnum {
            yes, no
        }

        //Should not be included in the model definitions
        enum MyEnum2 {
            si, no
        }
    }
}
