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
import io.swagger.v3.core.resolving.v31.model.AnnotatedArrayProperty;
import io.swagger.v3.core.resolving.v31.model.AnnotatedArrayPropertyWriteOnly;
import io.swagger.v3.core.resolving.v31.model.AnnotatedArrayPropertyReadWrite;
import io.swagger.v3.core.resolving.v31.model.ModelWithDependentSchema;
import io.swagger.v3.core.resolving.v31.model.ModelWithOAS31Stuff;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public class ModelResolverOAS31Test extends SwaggerTestBase {

    @Test
    public void testAnnotatedArray() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(AnnotatedArray.class));
        SerializationMatchers.assertEqualsToYaml31(model, """
                type: array
                contains:
                  type: string
                description: arraydescription
                items:
                  type: string
                  description: itemdescription
                maxContains: 10
                minContains: 1
                prefixItems:
                - type: string
                unevaluatedItems:
                  type: number
                """);
    }

    @Test
    public void testAnnotatedArrayProperty() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(AnnotatedArrayProperty.class));
        SerializationMatchers.assertEqualsToYaml31(model, """
                type: object
                properties:
                  randomList:
                    type: array
                    contains:
                      type: string
                    deprecated: true
                    description: arraydescription
                    examples:
                    - John
                    items:
                      type: string
                      description: itemdescription
                      title: itemtitle
                    maxContains: 10
                    minContains: 1
                    prefixItems:
                    - type: string
                      description: prefixdescription
                    readOnly: true
                    title: arraytitle
                    unevaluatedItems:
                      type: number""");
    }

    @Test
    public void testAnnotatedArrayPropertyWriteOnly() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(AnnotatedArrayPropertyWriteOnly.class));
        SerializationMatchers.assertEqualsToYaml31(model, """
                type: object
                properties:
                  randomList:
                    type: array
                    description: arraydescription
                    examples:
                    - Jane
                    items:
                      type: string
                      description: itemdescription
                      title: itemtitle
                    maxContains: 5
                    minContains: 1
                    title: arraytitle
                    writeOnly: true""");
    }

    @Test
    public void testAnnotatedArrayPropertyReadWrite() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(AnnotatedArrayPropertyReadWrite.class));
        SerializationMatchers.assertEqualsToYaml31(model, """
                type: object
                properties:
                  randomList:
                    type: array
                    description: arraydescription
                    examples:
                    - Bob
                    items:
                      type: string
                      description: itemdescription
                      title: itemtitle
                    maxContains: 8
                    minContains: 2
                    title: arraytitle""");
    }

    @Test
    public void testOAS31Fields() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        context.resolve(new AnnotatedType(ModelWithOAS31Stuff.class));
        SerializationMatchers.assertEqualsToYaml31(context.getDefinedModels(), """
                Address:
                  type: object
                  if:
                    $ref: "#/components/schemas/AnnotatedCountry"
                  then:
                    $ref: "#/components/schemas/PostalCodeNumberPattern"
                  else:
                    $ref: "#/components/schemas/PostalCodePattern"
                  dependentRequired:
                    street:
                    - country
                  properties:
                    street:
                      type: string
                    country:
                      type: string
                      enum:
                      - UNITED_STATES_OF_AMERICA
                      - CANADA
                  propertyNames:
                    pattern: "^[A-Za-z_][A-Za-z0-9_]*$"
                AnnotatedCountry:
                  type: object
                  properties:
                    country:
                      type: string
                      const: United States
                Client:
                  type: object
                  properties:
                    name:
                      type: string
                    creditCard:
                      type: integer
                      format: int32
                CreditCard:
                  type: object
                  properties:
                    billingAddress:
                      type: string
                    acceptingCountries:
                      type: array
                      items:
                        type: string
                        description: accepting country
                        enum:
                        - UNITED_STATES_OF_AMERICA
                        - CANADA
                      uniqueItems: true
                    availableCurrencies:
                      type: array
                      items:
                        $ref: "#/components/schemas/Currency"
                        description: available currencies
                      uniqueItems: true
                Currency:
                  type: object
                  properties:
                    currencyCode:
                      type: string
                ModelWithOAS31Stuff:
                  type: object
                  $comment: Random comment at schema level
                  $id: http://yourdomain.com/schemas/myschema.json
                  description: this is model for testing OAS 3.1 resolving
                  properties:
                    randomList:
                      type: array
                      contains:
                        type: string
                      description: arraydescription
                      items:
                        type: string
                        description: itemdescription
                        title: itemtitle
                      maxContains: 10
                      minContains: 1
                      prefixItems:
                      - type: string
                      title: arraytitle
                      unevaluatedItems:
                        type: number
                    status:
                      type:
                      - string
                      - number
                    intValue:
                      type: integer
                      format: int32
                      $anchor: intValue
                      $comment: comment at schema property level
                      exclusiveMaximum: 100
                      exclusiveMinimum: 1
                    text:
                      type: string
                      contentEncoding: plan/text
                      contentMediaType: base64
                    encodedString:
                      type: string
                      contentMediaType: application/jwt
                      contentSchema:
                        $ref: "#/components/schemas/MultipleBaseBean"
                    address:
                      $ref: "#/components/schemas/Address"
                    client:
                      $ref: "#/components/schemas/Client"
                      dependentSchemas:
                        creditCard:
                          $ref: "#/components/schemas/CreditCard"
                      patternProperties:
                        creditCard:
                          $ref: "#/components/schemas/CreditCard"
                      properties:
                        extraObject: {}
                MultipleBaseBean:
                  type: object
                  description: MultipleBaseBean
                  properties:
                    beanType:
                      type: string
                    a:
                      type: integer
                      format: int32
                    b:
                      type: string
                MultipleSub1Bean:
                  allOf:
                  - $ref: "#/components/schemas/MultipleBaseBean"
                  - type: object
                    properties:
                      c:
                        type: integer
                        format: int32
                  description: MultipleSub1Bean
                MultipleSub2Bean:
                  allOf:
                  - $ref: "#/components/schemas/MultipleBaseBean"
                  - type: object
                    properties:
                      d:
                        type: integer
                        format: int32
                  description: MultipleSub2Bean
                PostalCodeNumberPattern:
                  type: object
                  properties:
                    postalCode:
                      type: string
                      pattern: "[0-9]{5}(-[0-9]{4})?"
                PostalCodePattern:
                  type: object
                  properties:
                    postalCode:
                      type: string
                      pattern: "[A-Z][0-9][A-Z] [0-9][A-Z][0-9]"
                PropertyNamesPattern:
                  pattern: "^[A-Za-z_][A-Za-z0-9_]*$"
                """);
    }


    @Test
    public void testDependentSchemasAnnotation() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        context.resolve(new AnnotatedType(ModelWithDependentSchema.class));

        SerializationMatchers.assertEqualsToYaml31(context.getDefinedModels(), """
                BooleanFakeClass:
                  type: object
                  properties:
                    type:
                      type: boolean
                ModelWithDependentSchema:
                  type: object
                  dependentSchemas:
                    value:
                      properties:
                        enable:
                          $ref: "#/components/schemas/BooleanFakeClass"
                  properties:
                    name:
                      type: string
                    value:
                      type: integer
                      format: int32
                """);
    }

    @Test(description = "Top level type:object should appear in OAS31")
    public void testObjectTypeSchemaOAS31(){
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(TestObject4715.class));
        SerializationMatchers.assertEqualsToYaml31(model, """
                type: object
                properties:
                  foo:
                    type: string
                  bar:
                    type: string
                  id:
                    type: integer
                    format: int32""");
    }

    @Test
    public void testFieldArraySchemaAnnotation() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(TestArrayType.class));
        SerializationMatchers.assertEqualsToYaml31(model, """
                  type: object
                  properties:
                    id:
                      type: integer
                      format: int32
                    names:
                      type: array
                      items:
                        type: string
                      maxItems: 10""");
    }

    @Test(description = "@Pattern correctly handled in type parameters of properties using collections when using oas 3.1.0")
    public void testModelUsingCollectionTypePropertyHandlesPatternAnnotationForOas31() {
        String expectedYaml = """
                ClassWithUsingPatternOnCollection:
                  type: object
                  properties:
                    myField:
                      type: array
                      items:
                        pattern: myPattern
                        type: string""";

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
        String expectedYaml = """
                ClassWithUsingSizeOnCollection:
                  type: object
                  properties:
                    myField:
                      maxItems: 100
                      minItems: 1
                      type: array
                      items:
                        type: string""";

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
        String expectedYaml = """
                ClassWithUsingSizeOnField:
                  type: object
                  properties:
                    myField:
                      type: string
                      maxLength: 100
                      minLength: 1""";

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
        String expectedYaml = """
                ClassWithUsingDecimalAnnotationsOnField:
                  type: object
                  properties:
                    myField:
                      type: number
                      maximum: 100
                      minimum: 1""";

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

    @Test
    public void testOAS31JavaRecord() {
        String expectedYaml = """
                JavaRecordWithOAS31Fields:
                  type: object
                  $comment: Random comment at schema level
                  $id: http://yourdomain.com/schemas/myschema.json
                  description: this is model for testing OAS 3.1 Java Record resolving
                  properties:
                    test:
                      type: string
                    isLatest:
                      type: boolean
                    randomList:
                      type: array
                      contains:
                        type: string
                      items:
                        type: string
                      maxContains: 10
                      minContains: 1
                      prefixItems:
                      - type: string
                      unevaluatedItems:
                        type: number
                    Status:
                      type:
                      - string
                      - number
                """;

        Map<String, Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(JavaRecordWithOAS31Fields.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    @io.swagger.v3.oas.annotations.media.Schema(
            $id = "http://yourdomain.com/schemas/myschema.json",
            description = "this is model for testing OAS 3.1 Java Record resolving",
            $comment = "Random comment at schema level",
            types = "object"
    )
    private record JavaRecordWithOAS31Fields(
            String test,
            boolean isLatest,
            @ArraySchema(
                    maxContains = 10,
                    minContains = 1,
                    contains = @io.swagger.v3.oas.annotations.media.Schema(
                            types = "string"
                    ),
                    unevaluatedItems = @io.swagger.v3.oas.annotations.media.Schema(
                            types = "number"
                    ),
                    prefixItems = {
                            @io.swagger.v3.oas.annotations.media.Schema(
                                    types = "string"
                            )
                    }
            )
            List<String> randomList,
            @io.swagger.v3.oas.annotations.media.Schema(types = {
                    "string",
                    "number"
            })
            Object Status
    ){
    }
}
