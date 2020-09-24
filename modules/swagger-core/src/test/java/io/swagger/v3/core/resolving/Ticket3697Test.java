package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObject3697;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket3697Test extends SwaggerTestBase {

    @Test
    public void testHiddenJsonCreator() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(TestObject3697.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "TestObject3697:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int64");
    }

}
