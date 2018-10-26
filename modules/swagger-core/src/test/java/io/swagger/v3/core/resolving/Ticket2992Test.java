package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObject2992;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket2992Test extends SwaggerTestBase {

    @Test
    public void testLocalTime() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(TestObject2992.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "LocalTime:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    hour:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    minute:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    second:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    nano:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "TestObject2992:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: string\n" +
                "    a:\n" +
                "      $ref: '#/components/schemas/LocalTime'\n" +
                "    b:\n" +
                "      $ref: '#/components/schemas/LocalTime'\n" +
                "    c:\n" +
                "      $ref: '#/components/schemas/LocalTime'\n" +
                "    d:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    e:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    f:\n" +
                "      type: string\n" +
                "      format: date-time");

        PrimitiveType.enablePartialTime();
        context = new ModelConverterContextImpl(modelResolver);

        context
                .resolve(new AnnotatedType(TestObject2992.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "TestObject2992:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: string\n" +
                "    a:\n" +
                "      type: string\n" +
                "      format: partial-time\n" +
                "    b:\n" +
                "      type: string\n" +
                "      format: partial-time\n" +
                "    c:\n" +
                "      type: string\n" +
                "      format: partial-time\n" +
                "    d:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    e:\n" +
                "      type: string\n" +
                "      format: date-time\n" +
                "    f:\n" +
                "      type: string\n" +
                "      format: date-time");
    }

}
