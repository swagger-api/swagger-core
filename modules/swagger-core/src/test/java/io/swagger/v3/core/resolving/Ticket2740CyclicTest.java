package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.MyThing;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket2740CyclicTest extends SwaggerTestBase {
    @Test
    public void testCyclicBean() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(MyThing.class));

        SerializationMatchers.assertEqualsToYaml(model, "type: object\n" +
                "properties:\n" +
                "  otherThings:\n" +
                "    uniqueItems: true\n" +
                "    type: array\n" +
                "    description: Other related things\n" +
                "    items:\n" +
                "      $ref: '#/components/schemas/MyThing'\n" +
                "description: Thing");
    }

}
