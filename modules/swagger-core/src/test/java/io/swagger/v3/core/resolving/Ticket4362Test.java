package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;

public class Ticket4362Test extends SwaggerTestBase {

    @AfterMethod
    public void afterTest() {
        ModelResolver.enumsAsRef = false;
    }

    @Test
    public void testAnyOf() throws Exception {
        ModelResolver.enumsAsRef = true;

        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(DayOfWeek.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "DayOfWeek:\n" +
                "  type: string\n" +
                "  enum:\n" +
                "  - MONDAY\n" +
                "  - TUESDAY\n" +
                "  - WEDNESDAY\n" +
                "  - THURSDAY\n" +
                "  - FRIDAY\n" +
                "  - SATURDAY\n" +
                "  - SUNDAY");
    }

}
