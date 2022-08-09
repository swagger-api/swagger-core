package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObject3699;
import org.testng.annotations.Test;

public class Ticket3699Test extends SwaggerTestBase {

    @Test
    public void test3699() {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        context.resolve(new AnnotatedType(TestObject3699.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "TestObject3699:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    CustomName:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "        foo:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: string\n");
    }
}
