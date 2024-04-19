package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.core.resolving.v31.model.AnnotatedArray;
import io.swagger.v3.core.resolving.v31.model.ModelWithDependentSchema;
import io.swagger.v3.core.resolving.v31.model.ModelWithOAS31Stuff;
import io.swagger.v3.core.resolving.v31.model.ModelWithOAS31StuffMinimal;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class ModelResolverOAS31Test extends SwaggerTestBase {

    @Test
    public void testAnnotatedArray() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(AnnotatedArray.class));
        SerializationMatchers.assertEqualsToYaml31(model, "type: array\n" +
                "contains:\n" +
                "  type: string\n" +
                "items:\n" +
                "  type: string\n" +
                "maxContains: 10\n" +
                "minContains: 1\n" +
                "prefixItems:\n" +
                "- type: string\n" +
                "unevaluatedItems:\n" +
                "  type: number\n");
    }

    @Test
    public void testOAS31Fields() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithOAS31Stuff.class));
        SerializationMatchers.assertEqualsToYaml31(context.getDefinedModels(), "Address:\n" +
                "  if:\n" +
                "    $ref: '#/components/schemas/AnnotatedCountry'\n" +
                "  then:\n" +
                "    $ref: '#/components/schemas/PostalCodeNumberPattern'\n" +
                "  else:\n" +
                "    $ref: '#/components/schemas/PostalCodePattern'\n" +
                "  dependentRequired:\n" +
                "    street:\n" +
                "    - country\n" +
                "  properties:\n" +
                "    street:\n" +
                "      type: string\n" +
                "    country:\n" +
                "      type: string\n" +
                "      enum:\n" +
                "      - UNITED_STATES_OF_AMERICA\n" +
                "      - CANADA\n" +
                "  propertyNames:\n" +
                "    $ref: '#/components/schemas/PropertyNamesPattern'\n" +
                "AnnotatedCountry:\n" +
                "  properties:\n" +
                "    country:\n" +
                "      const: United States\n" +
                "Client:\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: string\n" +
                "    creditCard:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "CreditCard:\n" +
                "  properties:\n" +
                "    billingAddress:\n" +
                "      type: string\n" +
                "ModelWithOAS31Stuff:\n" +
                "  type: object\n" +
                "  $comment: Random comment at schema level\n" +
                "  $id: http://yourdomain.com/schemas/myschema.json\n" +
                "  description: this is model for testing OAS 3.1 resolving\n" +
                "  properties:\n" +
                "    randomList:\n" +
                "      type: array\n" +
                "      contains:\n" +
                "        type: string\n" +
                "      items:\n" +
                "        type: string\n" +
                "      maxContains: 10\n" +
                "      minContains: 1\n" +
                "      prefixItems:\n" +
                "      - type: string\n" +
                "      unevaluatedItems:\n" +
                "        type: number\n" +
                "    status:\n" +
                "      type:\n" +
                "      - string\n" +
                "      - number\n" +
                "    intValue:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "      $anchor: intValue\n" +
                "      $comment: comment at schema property level\n" +
                "      exclusiveMaximum: 100\n" +
                "      exclusiveMinimum: 1\n" +
                "    text:\n" +
                "      type: string\n" +
                "      contentEncoding: plan/text\n" +
                "      contentMediaType: base64\n" +
                "    encodedString:\n" +
                "      type: string\n" +
                "      contentMediaType: application/jwt\n" +
                "      contentSchema:\n" +
                "        $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "    address:\n" +
                "      $ref: '#/components/schemas/Address'\n" +
                "    client:\n" +
                "      $ref: '#/components/schemas/Client'\n" +
                "      dependentSchemas:\n" +
                "        creditCard:\n" +
                "          $ref: '#/components/schemas/CreditCard'\n" +
                "      patternProperties:\n" +
                "        creditCard:\n" +
                "          $ref: '#/components/schemas/CreditCard'\n" +
                "      properties:\n" +
                "        extraObject: {}\n" +
                "MultipleBaseBean:\n" +
                "  description: MultipleBaseBean\n" +
                "  properties:\n" +
                "    beanType:\n" +
                "      type: string\n" +
                "    a:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    b:\n" +
                "      type: string\n" +
                "MultipleSub1Bean:\n" +
                "  allOf:\n" +
                "  - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "  - type: object\n" +
                "    properties:\n" +
                "      c:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "  description: MultipleSub1Bean\n" +
                "MultipleSub2Bean:\n" +
                "  allOf:\n" +
                "  - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "  - type: object\n" +
                "    properties:\n" +
                "      d:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "  description: MultipleSub2Bean\n" +
                "PostalCodeNumberPattern:\n" +
                "  properties:\n" +
                "    postalCode:\n" +
                "      pattern: \"[0-9]{5}(-[0-9]{4})?\"\n" +
                "PostalCodePattern:\n" +
                "  properties:\n" +
                "    postalCode:\n" +
                "      pattern: \"[A-Z][0-9][A-Z] [0-9][A-Z][0-9]\"\n" +
                "PropertyNamesPattern:\n" +
                "  pattern: \"^[A-Za-z_][A-Za-z0-9_]*$\"\n");
    }

    @Test
    public void testDependentSchemasAnnotation() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(ModelWithDependentSchema.class));

        SerializationMatchers.assertEqualsToYaml31(context.getDefinedModels(), "BooleanFakeClass:\n" +
                "  properties:\n" +
                "    type:\n" +
                "      type: boolean\n" +
                "ModelWithDependentSchema:\n" +
                "  dependentSchemas:\n" +
                "    value:\n" +
                "      properties:\n" +
                "        enable:\n" +
                "          $ref: '#/components/schemas/BooleanFakeClass'\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: string\n" +
                "    value:\n" +
                "      type: integer\n" +
                "      format: int32\n");
    }

}
