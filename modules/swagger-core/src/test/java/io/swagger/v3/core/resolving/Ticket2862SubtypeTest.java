package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.Ticket2862Model;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket2862SubtypeTest extends SwaggerTestBase {
    @Test
    public void testSubType() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(Ticket2862Model.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Ticket2862Model:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    Ticket2862Model:\n" +
                "      type: object\n" +
                "Ticket2862ModelImpl:\n" +
                "  type: string\n" +
                "  allOf:\n" +
                "  - $ref: '#/components/schemas/Ticket2862Model'\n" +
                "  enum:\n" +
                "  - VALUE1\n" +
                "  - VALUE2\n");
    }

}
