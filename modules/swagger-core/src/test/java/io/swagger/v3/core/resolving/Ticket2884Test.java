package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.Ticket2884Model;
import io.swagger.v3.core.resolving.resources.Ticket2884ModelClass;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket2884Test extends SwaggerTestBase {
    @Test
    public void test2884() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Ticket2884ModelClass a = new Ticket2884ModelClass();

        Schema model = context
                .resolve(new AnnotatedType(Ticket2884Model.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Ticket2884Model:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    Ticket2884Model:\n" +
                "      type: object");

        context = new ModelConverterContextImpl(modelResolver);
        model = context
                .resolve(new AnnotatedType(Ticket2884ModelClass.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Ticket2884ModelClass:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    Ticket2884ModelClass:\n" +
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
