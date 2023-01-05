package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObjectTicket4339;
import org.testng.annotations.Test;

public class Ticket4339Test {
    @Test
    public void testNullableWithNull() {
        SerializationMatchers.assertEqualsToYaml(ModelConverters.getInstance().readAll(TestObjectTicket4339.class), "Address:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    streetNumber:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "  nullable: true\n" +
                "  example: null\n" +
                "  default: null\n" +
                "TestObjectTicket4339:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    string:\n" +
                "      type: string\n" +
                "      nullable: true\n" +
                "      example: null\n" +
                "      default: null\n" +
                "    integer:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "      nullable: true\n" +
                "      example: null\n" +
                "      default: null\n" +
                "    number:\n" +
                "      type: number\n" +
                "      nullable: true\n" +
                "      example: null\n" +
                "      default: null\n" +
                "    aBoolean:\n" +
                "      type: boolean\n" +
                "      nullable: true\n" +
                "      example: null\n" +
                "      default: null\n" +
                "    address:\n" +
                "      $ref: '#/components/schemas/Address'\n" +
                "    testObjectTicket4339List:\n" +
                "      type: array\n" +
                "      nullable: true\n" +
                "      items:\n" +
                "        $ref: '#/components/schemas/TestObjectTicket4339'\n" +
                "      example: null\n" +
                "      default: null\n");
    }
}
