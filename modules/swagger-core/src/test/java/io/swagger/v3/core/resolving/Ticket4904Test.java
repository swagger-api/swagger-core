package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import static io.swagger.v3.core.resolving.SwaggerTestBase.mapper;

public class Ticket4904Test {

    @Test
    public void testComposedSchemaWithDiscriminator() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        context.resolve(new AnnotatedType(ParentClass.class));
    }

    @Schema(
            type = "object",
            discriminatorMapping = {
                    @DiscriminatorMapping(value = "A", schema = ChildClassA.class),
                    @DiscriminatorMapping(value = "B", schema = ChildClassB.class)
            },
            oneOf = {ChildClassA.class, ChildClassB.class},
            discriminatorProperty = "objectType"
    )
    public abstract static class ParentClass {
    }

    public static class ChildClassA extends ParentClass {
    }

    public static class ChildClassB extends ParentClass {
    }

}
