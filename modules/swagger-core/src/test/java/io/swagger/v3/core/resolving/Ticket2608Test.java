package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.Ticket2608Model;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket2608Test extends SwaggerTestBase {

    @Test
    public void test2608() {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(Ticket2608Model.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Ticket2608Model:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      description: long field\n" +
                "      format: int64\n" +
                "      example: 2395149362243048630\n" +
                "  description: example\n");

    }
}
