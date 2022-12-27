package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.core.resolving.v31.model.AnnotatedArray;
import io.swagger.v3.core.resolving.v31.model.ModelWithDependentSchema;
import io.swagger.v3.core.resolving.v31.model.ModelWithOAS31Stuff;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class ModelResolverOAS31Test extends SwaggerTestBase {

    @Test
    public void testAnnotatedArray() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(AnnotatedArray.class));
        SerializationMatchers.assertEqualsToYaml31(model, "type:\n" +
                "- array\n" +
                "- string\n" +
                "- number\n" +
                "contains:\n" +
                "  type: string\n" +
                "items:\n" +
                "  type: string\n" +
                "maxContains: 10\n" +
                "minContains: 1\n" +
                "prefixItems:\n" +
                "- type: string\n" +
                "unevaluatedItems:\n" +
                "  type: number");
    }

    @Test
    public void testOAS31Fields() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithOAS31Stuff.class));
        SerializationMatchers.assertEqualsToYaml31(model, "type: object\n" +
                "$comment: Random comment at schema level\n" +
                "$id: http://yourdomain.com/schemas/myschema.json\n" +
                "description: this is model for testing OAS 3.1 resolving\n" +
                "properties:\n" +
                "  randomList:\n" +
                "    type:\n" +
                "    - array\n" +
                "    - string\n" +
                "    - number\n" +
                "    contains:\n" +
                "      type: string\n" +
                "    items:\n" +
                "      type: string\n" +
                "    maxContains: 10\n" +
                "    minContains: 1\n" +
                "    prefixItems:\n" +
                "    - type: string\n" +
                "    unevaluatedItems:\n" +
                "      type: number\n" +
                "  status:\n" +
                "    type:\n" +
                "    - string\n" +
                "    - number\n" +
                "  intValue:\n" +
                "    type: integer\n" +
                "    format: int32\n" +
                "    $anchor: intValue\n" +
                "    $comment: comment at schema property level\n" +
                "    exclusiveMaximum: 100\n" +
                "    exclusiveMinimum: 1\n" +
                "  text:\n" +
                "    type: string\n" +
                "    contentEncoding: plan/text\n" +
                "    contentMediaType: base64\n" +
                "  encodedString:\n" +
                "    type: string\n" +
                "    contentMediaType: application/jwt\n" +
                "    contentSchema:\n" +
                "      description: MultipleBaseBean\n" +
                "      properties:\n" +
                "        beanType:\n" +
                "          type: string\n" +
                "        a:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        b:\n" +
                "          type: string\n" +
                "  address:\n" +
                "    if:\n" +
                "      properties:\n" +
                "        country:\n" +
                "          const: United States\n" +
                "    then:\n" +
                "      properties:\n" +
                "        postalCode:\n" +
                "          pattern: \"[0-9]{5}(-[0-9]{4})?\"\n" +
                "    else:\n" +
                "      properties:\n" +
                "        postalCode:\n" +
                "          pattern: \"[A-Z][0-9][A-Z] [0-9][A-Z][0-9]\"\n" +
                "    dependentRequired:\n" +
                "      street:\n" +
                "      - country\n" +
                "    properties:\n" +
                "      street:\n" +
                "        type: string\n" +
                "      country:\n" +
                "        type: string\n" +
                "        enum:\n" +
                "        - UNITED_STATES_OF_AMERICA\n" +
                "        - CANADA\n" +
                "    propertyNames:\n" +
                "      pattern: \"^[A-Za-z_][A-Za-z0-9_]*$\"\n" +
                "  client:\n" +
                "    dependentSchemas:\n" +
                "      creditCard:\n" +
                "        properties:\n" +
                "          billingAddress:\n" +
                "            type: string\n" +
                "    patternProperties:\n" +
                "      creditCard:\n" +
                "        properties:\n" +
                "          billingAddress:\n" +
                "            type: string\n" +
                "    properties:\n" +
                "      extraObject: {}\n" +
                "      name:\n" +
                "        type: string\n" +
                "      creditCard:\n" +
                "        type: integer\n" +
                "        format: int32");
    }

    @Test
    public void testDependentSchemasAnnotation() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(ModelWithDependentSchema.class));

        SerializationMatchers.assertEqualsToYaml31(model, "dependentSchemas:\n" +
                "  value:\n" +
                "    properties:\n" +
                "      enable:\n" +
                "        properties:\n" +
                "          type:\n" +
                "            type: boolean\n" +
                "properties:\n" +
                "  name:\n" +
                "    type: string\n" +
                "  value:\n" +
                "    type: integer\n" +
                "    format: int32");
    }
}
