package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.User2169;
import org.testng.annotations.Test;

public class JsonPropertyTest {

    @Test(description = "test ticket 2169")
    public void testTicket2169() {

        SerializationMatchers.assertEqualsToYaml(ModelConverters.getInstance().read(User2169.class), "User2169:\n" +
                "  required:\n" +
                "  - Age\n" +
                "  - GetterJsonPropertyOnField\n" +
                "  - GetterJsonPropertyOnFieldReadOnly\n" +
                "  - GetterJsonPropertyOnFieldReadWrite\n" +
                "  - GetterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse\n" +
                "  - GetterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse\n" +
                "  - GetterJsonPropertyOnFieldSchemaReadOnlyTrue\n" +
                "  - Name\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    Name:\n" +
                "      type: string\n" +
                "    Age:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    GetterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse:\n" +
                "      type: string\n" +
                "    publi:\n" +
                "      type: string\n" +
                "    getter:\n" +
                "      type: string\n" +
                "    setter:\n" +
                "      type: string\n" +
                "      writeOnly: true\n" +
                "    getterSetter:\n" +
                "      type: string\n" +
                "    jsonProp:\n" +
                "      type: string\n" +
                "    jsonPropReadOnly:\n" +
                "      type: string\n" +
                "      readOnly: true\n" +
                "    jsonPropWriteOnly:\n" +
                "      type: string\n" +
                "      writeOnly: true\n" +
                "    jsonPropReadWrite:\n" +
                "      type: string\n" +
                "    getter_jsonProp:\n" +
                "      type: string\n" +
                "    getter_jsonPropReadOnly:\n" +
                "      type: string\n" +
                "      readOnly: true\n" +
                "    getter_jsonPropWriteOnly:\n" +
                "      type: string\n" +
                "    getter_jsonPropReadWrite:\n" +
                "      type: string\n" +
                "    setter_jsonProp:\n" +
                "      type: string\n" +
                "      writeOnly: true\n" +
                "    setter_jsonPropReadOnly:\n" +
                "      type: string\n" +
                "    setter_jsonPropWriteOnly:\n" +
                "      type: string\n" +
                "      writeOnly: true\n" +
                "    setter_jsonPropReadWrite:\n" +
                "      type: string\n" +
                "    gettersetter_jsonPropGet:\n" +
                "      type: string\n" +
                "    gettersetter_jsonPropReadOnlyGet:\n" +
                "      type: string\n" +
                "      readOnly: true\n" +
                "    gettersetter_jsonPropWriteOnlyGet:\n" +
                "      type: string\n" +
                "    gettersetter_jsonPropReadWriteGet:\n" +
                "      type: string\n" +
                "    gettersetter_jsonPropSet:\n" +
                "      type: string\n" +
                "    gettersetter_jsonPropReadOnlySet:\n" +
                "      type: string\n" +
                "    gettersetter_jsonPropWriteOnlySet:\n" +
                "      type: string\n" +
                "      writeOnly: true\n" +
                "    gettersetter_jsonPropReadWriteSet:\n" +
                "      type: string\n" +
                "    getterIgnore_jsonPropSet:\n" +
                "      type: string\n" +
                "      writeOnly: true\n" +
                "    getterIgnore_jsonPropReadOnlySet:\n" +
                "      type: string\n" +
                "    getterIgnore_jsonPropWriteOnlySet:\n" +
                "      type: string\n" +
                "      writeOnly: true\n" +
                "    getterIgnore_jsonPropReadWriteSet:\n" +
                "      type: string\n" +
                "    setterIgnore_jsonPropGet:\n" +
                "      type: string\n" +
                "    setterIgnore_jsonPropReadOnlyGet:\n" +
                "      type: string\n" +
                "      readOnly: true\n" +
                "    setterIgnore_jsonPropWriteOnlyGet:\n" +
                "      type: string\n" +
                "    setterIgnore_jsonPropReadWriteGet:\n" +
                "      type: string\n" +
                "    getterSchemaReadOnlyTrue:\n" +
                "      type: string\n" +
                "      readOnly: true\n" +
                "    data:\n" +
                "      $ref: '#/components/schemas/Data'\n" +
                "    GetterJsonPropertyOnField:\n" +
                "      type: string\n" +
                "    GetterJsonPropertyOnFieldReadWrite:\n" +
                "      type: string\n" +
                "    GetterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse:\n" +
                "      type: string\n" +
                "    GetterJsonPropertyOnFieldReadOnly:\n" +
                "      type: string\n" +
                "      readOnly: true\n" +
                "    GetterJsonPropertyOnFieldSchemaReadOnlyTrue:\n" +
                "      type: string\n" +
                "      readOnly: true\n" +
                "    approvePairing:\n" +
                "      type: boolean\n" +
                "      writeOnly: true\n");
    }
}
