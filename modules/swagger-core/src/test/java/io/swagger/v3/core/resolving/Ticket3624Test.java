package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Optional;

public class Ticket3624Test extends SwaggerTestBase {

    @Test
    public void testSelfReferencingOptional() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(ModelContainer.class));

        Yaml.prettyPrint(context.getDefinedModels());


        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Model:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    model:\n" +
                "      $ref: '#/components/schemas/Model'\n" +
                "ModelContainer:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    model:\n" +
                "      $ref: '#/components/schemas/Model'\n");

    }

    static class ModelContainer {
        public Optional<Model> model;
    }

    static class Model {
        public Optional<Model> model;
    }


}
