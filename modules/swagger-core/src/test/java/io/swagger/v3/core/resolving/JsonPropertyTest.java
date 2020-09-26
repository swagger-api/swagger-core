package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Test(description = "test ticket 2845")
    public void testTicket2845() {

        SerializationMatchers.assertEqualsToYaml(ModelConverters.getInstance().readAll(Ticket2845Holder.class), "Ticket2845Child:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    bar:\n" +
                "      type: string\n" +
                "    meow:\n" +
                "      type: string\n" +
                "Ticket2845Holder:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    child:\n" +
                "      $ref: '#/components/schemas/Ticket2845Child'");

        /*
            TODO: Test demonstrating annotation not being resolved when class is used/refernces elsewhere with different annotations
            in this case the annotation isn't resolved or not consistently resolved as the same object is also present
            and referenced  (in the same or different class) with no or different @JsonIgnoreProperties annotations.
            The possible solutions are either resolve into different unrelated schemas or resolve inline
            (see https://github.com/swagger-api/swagger-core/issues/3366 and other related tickets)
         */
        SerializationMatchers.assertEqualsToYaml(
                ModelConverters.getInstance().readAll(Ticket2845HolderNoAnnotationNotWorking.class),
                "Ticket2845Child:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    foo:\n" +
                        "      type: string\n" +
                        "    bar:\n" +
                        "      type: string\n" +
                        "    meow:\n" +
                        "      type: string\n" +
                        "Ticket2845HolderNoAnnotationNotWorking:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    child:\n" +
                        "      $ref: '#/components/schemas/Ticket2845Child'\n" +
                        "    childNoAnnotation:\n" +
                        "      $ref: '#/components/schemas/Ticket2845Child'");
    }

    static class Ticket2845Parent {
        public String foo;
        public String bar;
        public String bob;
    }

    @JsonIgnoreProperties({"bob"})
    static class Ticket2845Child extends Ticket2845Parent {
        public String meow;
    }

    static class Ticket2845Holder {
        @JsonIgnoreProperties({"foo"})
        public Ticket2845Child child;

    }

    static class Ticket2845HolderNoAnnotationNotWorking {
        @JsonIgnoreProperties({"foo"})
        public Ticket2845Child child;

        public Ticket2845Child childNoAnnotation;
    }
}
