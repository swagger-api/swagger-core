package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.core.resolving.resources.TestArrayType;
import io.swagger.v3.core.resolving.resources.TestObject4715;
import io.swagger.v3.core.resolving.v31.model.AnnotatedArray;
import io.swagger.v3.core.resolving.v31.model.ModelWithDependentSchema;
import io.swagger.v3.core.resolving.v31.model.ModelWithOAS31Stuff;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public class ModelResolverOAS31Test extends SwaggerTestBase {

    @Test
    public void testAnnotatedArray() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(AnnotatedArray.class));
        SerializationMatchers.assertEqualsToYaml31(model, "type: array\n" +
                "contains:\n" +
                "  type: string\n" +
                "description: arraydescription\n" +
                "items:\n" +
                "  type: string\n" +
                "  description: itemdescription\n"+
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
                "  type: object\n" +
                "  if:\n" +
                "    $ref: \"#/components/schemas/AnnotatedCountry\"\n" +
                "  then:\n" +
                "    $ref: \"#/components/schemas/PostalCodeNumberPattern\"\n" +
                "  else:\n" +
                "    $ref: \"#/components/schemas/PostalCodePattern\"\n" +
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
                "    pattern: \"^[A-Za-z_][A-Za-z0-9_]*$\"\n" +
                "AnnotatedCountry:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    country:\n" +
                "      type: string\n" +
                "      const: United States\n" +
                "Client:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: string\n" +
                "    creditCard:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "CreditCard:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    billingAddress:\n" +
                "      type: string\n" +
                "    acceptingCountries:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n" +
                "        description: accepting country\n" +
                "        enum:\n" +
                "        - UNITED_STATES_OF_AMERICA\n" +
                "        - CANADA\n" +
                "      uniqueItems: true\n" +
                "    availableCurrencies:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        $ref: \"#/components/schemas/Currency\"\n" +
                "        description: available currencies\n" +
                "      uniqueItems: true\n" +
                "Currency:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    currencyCode:\n" +
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
                "      description: arraydescription\n" +
                "      items:\n" +
                "        type: string\n" +
                "        description: itemdescription\n" +
                "        title: itemtitle\n" +
                "      maxContains: 10\n" +
                "      minContains: 1\n" +
                "      prefixItems:\n" +
                "      - type: string\n" +
                "      title: arraytitle\n" +
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
                "        $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "    address:\n" +
                "      $ref: \"#/components/schemas/Address\"\n" +
                "    client:\n" +
                "      $ref: \"#/components/schemas/Client\"\n" +
                "      dependentSchemas:\n" +
                "        creditCard:\n" +
                "          $ref: \"#/components/schemas/CreditCard\"\n" +
                "      patternProperties:\n" +
                "        creditCard:\n" +
                "          $ref: \"#/components/schemas/CreditCard\"\n" +
                "      properties:\n" +
                "        extraObject: {}\n" +
                "MultipleBaseBean:\n" +
                "  type: object\n" +
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
                "  - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "  - type: object\n" +
                "    properties:\n" +
                "      c:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "  description: MultipleSub1Bean\n" +
                "MultipleSub2Bean:\n" +
                "  allOf:\n" +
                "  - $ref: \"#/components/schemas/MultipleBaseBean\"\n" +
                "  - type: object\n" +
                "    properties:\n" +
                "      d:\n" +
                "        type: integer\n" +
                "        format: int32\n" +
                "  description: MultipleSub2Bean\n" +
                "PostalCodeNumberPattern:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    postalCode:\n" +
                "      type: string\n" +
                "      pattern: \"[0-9]{5}(-[0-9]{4})?\"\n" +
                "PostalCodePattern:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    postalCode:\n" +
                "      type: string\n" +
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
                "  type: object\n" +
                "  properties:\n" +
                "    type:\n" +
                "      type: boolean\n" +
                "ModelWithDependentSchema:\n" +
                "  type: object\n" +
                "  dependentSchemas:\n" +
                "    value:\n" +
                "      properties:\n" +
                "        enable:\n" +
                "          $ref: \"#/components/schemas/BooleanFakeClass\"\n" +
                "  properties:\n" +
                "    name:\n" +
                "      type: string\n" +
                "    value:\n" +
                "      type: integer\n" +
                "      format: int32\n");
    }

    @Test(description = "Top level type:object should appear in OAS31")
    public void testObjectTypeSchemaOAS31(){
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(TestObject4715.class));
        SerializationMatchers.assertEqualsToYaml31(model, "type: object\n" +
                "properties:\n" +
                "  foo:\n" +
                "    type: string\n" +
                "  bar:\n" +
                "    type: string\n" +
                "  id:\n" +
                "    type: integer\n" +
                "    format: int32");
    }

    @Test
    public void testFieldArraySchemaAnnotation() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(TestArrayType.class));
        SerializationMatchers.assertEqualsToYaml31(model, "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    names:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n" +
                "      maxItems: 10");
    }

    @Test(description = "@Pattern correctly handled in type parameters of properties using collections when using oas 3.1.0")
    public void testModelUsingCollectionTypePropertyHandlesPatternAnnotationForOas31() {
        String expectedYaml = "ClassWithUsingPatternOnCollection:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    myField:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        pattern: myPattern\n" +
                "        type: string";

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(ClassWithUsingPatternOnCollection.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    private static class ClassWithUsingPatternOnCollection {
        private List<@Pattern(regexp = "myPattern") String> myField;

        public List<String> getMyField() {
            return myField;
        }

        public void setMyField(List<String> myField) {
            this.myField = myField;
        }
    }

    @Test(description = "@Size correctly handled in properties using collections when using oas 3.1.0")
    public void testModelUsingCollectionTypePropertyHandleSizeAnnotationForOas31() {
        String expectedYaml = "ClassWithUsingSizeOnCollection:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    myField:\n" +
                "      maxItems: 100\n" +
                "      minItems: 1\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string";

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(ClassWithUsingSizeOnCollection.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    private static class ClassWithUsingSizeOnCollection {
        @Size(min = 1, max = 100)
        private List<String> myField;

        public List<String> getMyField() {
            return myField;
        }

        public void setMyField(List<String> myField) {
            this.myField = myField;
        }
    }

    @Test(description = "@Size correctly handled for field type String using OAS 3.1.0")
    public void testSizeAnnotationOnFieldForOAS31() {
        String expectedYaml = "ClassWithUsingSizeOnField:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    myField:\n" +
                "      type: string\n" +
                "      maxLength: 100\n" +
                "      minLength: 1";

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(ClassWithUsingSizeOnField.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    private static class ClassWithUsingSizeOnField {
        @Size(min = 1, max = 100)
        private String myField;

        public String getMyField() {
            return myField;
        }

        public void setMyField(String myField) {
            this.myField = myField;
        }
    }

    @Test(description = "@DecimalMax/Min annotations correctly handled for field type Number using OAS 3.1.0")
    public void testDecimalAnnotationsOnField() {
        String expectedYaml = "ClassWithUsingDecimalAnnotationsOnField:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    myField:\n" +
                "      type: number\n" +
                "      maximum: 100\n" +
                "      minimum: 1";

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(ClassWithUsingDecimalAnnotationsOnField.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    private static class ClassWithUsingDecimalAnnotationsOnField {
        @DecimalMin("1")
        @DecimalMax("100")
        private Number myField;

        public Number getMyField() {
            return myField;
        }

        public void setMyField(Number myField) {
            this.myField = myField;
        }
    }
}
