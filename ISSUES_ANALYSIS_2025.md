# Swagger-Core GitHub Issues Analysis (2025)

**Analysis Date:** 2026-02-03  
**Total Issues Analyzed:** 155  
**Open Issues:** 92  
**Closed Issues:** 63

---

## Executive Summary

This analysis examines 155 issues reported to the swagger-api/swagger-core repository since January 1, 2025. The issues have been categorized using three different approaches to enable meaningful grouping and assignment:

1. **By Issue Type and Status** - Traditional categorization for tracking and resolution
2. **By Functional Area** - Groups by technical domain for specialized team assignment
3. **By Priority/Severity** - Prioritizes work based on impact and urgency

---

## Approach 1: Categorization by Issue Type and Status

This approach groups issues by their type (Bug, Feature, Question, etc.) and tracks their current status. This is useful for:
- Understanding the balance of work types
- Tracking closure rates by category
- Sprint planning and capacity allocation

### Summary Statistics

**Bugs:** 31 total (16 open, 15 closed)
**Features:** 11 total (10 open, 1 closed)
**Questions:** 5 total (5 open, 0 closed)
**Improvements:** 2 total (2 open, 0 closed)
**Other:** 106 total (59 open, 47 closed)

### Detailed Breakdown

#### Bugs (31 issues)

**Open (16):**
- #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
- #5036: [Bug]: Example text is truncated when beginning with numbers
- #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
- #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
- #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
- #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1
- #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
- #5007: [Bug]: kotlin inline classes produce weird field names
- #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
- #4999: [Bug]: Example value starting with a number is parsed incorrectly
- #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas
- #4963: [Bug]: Primitive boxed type schemas used in @ApiResponse content schema implementations are incorrectly cast to string
- #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field
- #4841: 2.2.27 &amp; 2.2.28: org.gradle.api.internal.DefaultMutationGuard$IllegalMutationException: Project#afterEvaluate(Action) on project &#39;:XX&#39; cannot be executed in the current context.
- #4813: Upgrade to v2.2.27: Cannot change dependencies of dependency configuration &#39;&#39; after it has been included in dependency resolution
- #4682: BUG properties of type Object do not have any type information when generating open api 3.1.0

**Closed (15):**
- #5032: [Bug]: @NotEmpty and @NotBlank validation annotations ignored when combined with @Schema(requiredMode = ...)
- #5003: [Bug]: Regression: AnnotatedType equality change drops polymorphic subtype allOf entries
- #4967: [Bug]: RequiredMode.NOT_REQUIRED is not respected by properties annotated with @NotBlank and @NotEmpty
- #4965: [Bug]: [swagger-core-jakarta] ModelConverterContextImpl does not properly cache AnnotatedType
- #4959: [Bug]: Superfluous sibling $ref when using oneOf in Schema annotation - OAS 3.1
- #4952: [Bug]: swagger-jaxrs2-jakarta Exception in Wildfly/JBoss
- #4862: 2.2.29 do not treat @NotBlank as mandatory, breaking change?
- #4850: Extensions no longer enforced to be spec compliant between OpenAPI 3 and OpenAPI 3.1
- #4836: [Bug] Description for array schema is being ignored when generating OpenAPI 3.1
- #4835: [Bug] Enum values missing in OpenAPI 3.1 schema
- ... and 5 more

#### Features (11 issues)

**Open (10):**
- #5045: [Feature]: Need since or reason field in @Hidden annotation
- #5011: [Feature]: Drop commons-lang3 dependency and replace its usages with core Java
- #4991: [Feature] Support Jackson 3.x release line
- #4976: [Feature]: Reduce Boilerplate in swagger-core Models (Optional Lombok Support or Alternatives)
- #4966: [Feature]: Provide a bom module for managing dependencies
- #4951: [Feature]: support for runtime skip properties inside ModelConverter
- #4840: [Feature] Mark type as deprecated if the class is `@Deprecated`
- #4838: Empty default values are ignored
- #3411: Consider the @JsonSubTypes for generating the discriminator mapping
- #3323: Generic Response Types

**Closed (1):**
- #5041: [Feature]: I want to configure the default value of @Schema RequiredMode to other value, than AUTO

#### Questions (5 issues)

**Open (5):**
- #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten
- #5014: [Question]: How to specify @Schema.examples where each element is an array?
- #4984: [Question]: Duplicate records if Class and Operation both has @Path param
- #4939: Integer enum Schema parsing can fail based upon locale
- #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;

#### Improvements (2 issues)

**Open (2):**
- #4928: Lack of Friendly Support for Parameter Validation Grouping
- #4404: Add support for more java.time objects

#### Other (106 issues)

**Open (59):**
- #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
- #4924: Ability to remove unused definitions from generated YML
- #4923: Custom UUID wrapper class can&#39;t be used anymore after 2.2.28
- #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
- #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
- #4908: defaultValue attribute at enum object generate arbitrary json file
- #4906: java.lang.Object converted to json object (map) type
- #4905: CustomResolver&#39;s Results get overwritten by default ModelResolver
- #4904: java.lang.ClassCastException: class io.swagger.v3.oas.models.media.JsonSchema cannot be cast to class io.swagger.v3.oas.models.media.ComposedSchema
- #4903: jackson module not working in 2.2.30
- #4887: Order of `required` properties
- #4886: Validation Meta annotations not working
- #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
- #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
- #4864: generate OpenApi yml file with swagger-maven-plugin and with Java 17 #903
- #4863: &#34;Override&#34; example value when extending a class
- #4854: Bootstrapped servlet returns empty JSON as of 2.2.24
- #4851: The example value of the request body in swagger has a &#34;string&#34; value
- #4848: ModelResolver#resolve ignores TYPE_USE annotations on property types
- #4847: @Schema annotation on custom string type is ignored when using @JsonValue
- ... and 39 more

**Closed (47):**
- #4964: @Schema does&#39;nt work on non-primitive
- #4941: Autoswagger
- #4936: [VULNERABILITY] `org.apache.commons:commons-lang3:3.17.0` holds uncontrolled recursion vulnerability
- #4933: 🔥 @RestControllerAdvice Causes /v3/api-docs 500 Error in SpringDoc OpenAPI UI (Fixed with @Hidden on All Methods)
- #4932: Using open api 3.1  - Putting  annotation - `@field:Schema(enumAsRef = true)` - on a property does not result in `$ref`
- #4927: @Path annotation is unrecognizable on OpenApiResource class
- #4926: Ordering of various JSONSchema validation constraints changed in generated OpenAPI spec
- #4925: swagger-jaxrs2-jakarta not generating spec when wars exploded on Wildfly/JBoss
- #4907: Maven plugin generating wrong openapi version on  plugin version 2.2.30 and 2.2.32
- #4878: [BUG] Model resolution for global @PathParam ignores Open API 3.1 setting
- ... and 37 more

---

## Approach 2: Categorization by Functional Area

This approach groups issues by technical domain and functionality. This is ideal for:
- Assigning issues to specialized team members or teams
- Identifying areas requiring attention or refactoring
- Understanding which components have the most issues

### Summary Statistics

**Annotation Processing:** 103 total (63 open, 40 closed)
**Dependencies & Build Tools:** 80 total (44 open, 36 closed)
**Examples & Documentation:** 57 total (34 open, 23 closed)
**Validation & Required Fields:** 44 total (25 open, 19 closed)
**Jakarta Migration:** 35 total (20 open, 15 closed)
**Jackson Integration:** 32 total (20 open, 12 closed)
**Polymorphic Types & Inheritance:** 20 total (11 open, 9 closed)
**Schema Generation & OAS Spec:** 12 total (10 open, 2 closed)
**Kotlin Support:** 5 total (3 open, 2 closed)
**Other/Uncategorized:** 3 total (1 open, 2 closed)

### Detailed Breakdown

#### Annotation Processing (103 issues, 63 open)

*Issues related to @Schema, @ArraySchema, @Hidden, and other annotations*

🔴 #5045: [Feature]: Need since or reason field in @Hidden annotation
🔴 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
🔴 #5036: [Bug]: Example text is truncated when beginning with numbers
🔴 #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
🔴 #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten
🔴 #5014: [Question]: How to specify @Schema.examples where each element is an array?
🔴 #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1
🔴 #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
🔴 #5007: [Bug]: kotlin inline classes produce weird field names
🔴 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🔴 #4999: [Bug]: Example value starting with a number is parsed incorrectly
🔴 #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas
🔴 #4984: [Question]: Duplicate records if Class and Operation both has @Path param
🔴 #4963: [Bug]: Primitive boxed type schemas used in @ApiResponse content schema implementations are incorrectly cast to string
🔴 #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field

... and 88 more issues

#### Dependencies & Build Tools (80 issues, 44 open)

*Issues with dependencies, Maven/Gradle plugins, and build processes*

🔴 #5045: [Feature]: Need since or reason field in @Hidden annotation
🔴 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
🔴 #5036: [Bug]: Example text is truncated when beginning with numbers
🔴 #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
🔴 #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
🔴 #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
🔴 #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten
🔴 #5014: [Question]: How to specify @Schema.examples where each element is an array?
🔴 #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1
🔴 #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
🔴 #5011: [Feature]: Drop commons-lang3 dependency and replace its usages with core Java
🔴 #5007: [Bug]: kotlin inline classes produce weird field names
🔴 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🔴 #4999: [Bug]: Example value starting with a number is parsed incorrectly
🔴 #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas

... and 65 more issues

#### Examples & Documentation (57 issues, 34 open)

*Issues with example values and documentation generation*

🔴 #5036: [Bug]: Example text is truncated when beginning with numbers
🔴 #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
🔴 #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
🔴 #5014: [Question]: How to specify @Schema.examples where each element is an array?
🔴 #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
🔴 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🔴 #4999: [Bug]: Example value starting with a number is parsed incorrectly
🔴 #4984: [Question]: Duplicate records if Class and Operation both has @Path param
🔴 #4966: [Feature]: Provide a bom module for managing dependencies
🔴 #4939: Integer enum Schema parsing can fail based upon locale
🔴 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
🔴 #4928: Lack of Friendly Support for Parameter Validation Grouping
🔴 #4923: Custom UUID wrapper class can&#39;t be used anymore after 2.2.28
🔴 #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
🔴 #4908: defaultValue attribute at enum object generate arbitrary json file

... and 42 more issues

#### Validation & Required Fields (44 issues, 25 open)

*Issues with validation annotations, required fields, and RequiredMode*

🔴 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
🔴 #5036: [Bug]: Example text is truncated when beginning with numbers
🔴 #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
🔴 #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
🔴 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🔴 #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field
🔴 #4928: Lack of Friendly Support for Parameter Validation Grouping
🔴 #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
🔴 #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
🔴 #4887: Order of `required` properties
🔴 #4886: Validation Meta annotations not working
🔴 #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
🔴 #4842: OpenApi doc not beeing generated properly
🔴 #4841: 2.2.27 &amp; 2.2.28: org.gradle.api.internal.DefaultMutationGuard$IllegalMutationException: Project#afterEvaluate(Action) on project &#39;:XX&#39; cannot be executed in the current context.
🔴 #4838: Empty default values are ignored

... and 29 more issues

#### Jakarta Migration (35 issues, 20 open)

*Issues related to Jakarta namespace migration and Jakarta annotations*

🔴 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
🔴 #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
🔴 #5007: [Bug]: kotlin inline classes produce weird field names
🔴 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🔴 #4999: [Bug]: Example value starting with a number is parsed incorrectly
🔴 #4984: [Question]: Duplicate records if Class and Operation both has @Path param
🔴 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
🔴 #4928: Lack of Friendly Support for Parameter Validation Grouping
🔴 #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
🔴 #4908: defaultValue attribute at enum object generate arbitrary json file
🔴 #4903: jackson module not working in 2.2.30
🔴 #4886: Validation Meta annotations not working
🔴 #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
🔴 #4846: Swagger adds extra line breaks in yaml generated from annotated code
🔴 #4842: OpenApi doc not beeing generated properly

... and 20 more issues

#### Jackson Integration (32 issues, 20 open)

*Issues with Jackson annotations and serialization/deserialization*

🔴 #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
🔴 #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
🔴 #5007: [Bug]: kotlin inline classes produce weird field names
🔴 #4991: [Feature] Support Jackson 3.x release line
🔴 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
🔴 #4905: CustomResolver&#39;s Results get overwritten by default ModelResolver
🔴 #4904: java.lang.ClassCastException: class io.swagger.v3.oas.models.media.JsonSchema cannot be cast to class io.swagger.v3.oas.models.media.ComposedSchema
🔴 #4903: jackson module not working in 2.2.30
🔴 #4848: ModelResolver#resolve ignores TYPE_USE annotations on property types
🔴 #4847: @Schema annotation on custom string type is ignored when using @JsonValue
🔴 #4842: OpenApi doc not beeing generated properly
🔴 #4819: Definition for parent interface not generated
🔴 #4696: Using @JsonUnwrapped with @Schema(implementation = Something) does not unwrap
🔴 #4689: BUG Unable to deserialize OpenAPI Parameter
🔴 #4682: BUG properties of type Object do not have any type information when generating open api 3.1.0

... and 17 more issues

#### Polymorphic Types & Inheritance (20 issues, 11 open)

*Issues with oneOf, allOf, discriminators, and class hierarchies*

🔴 #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
🔴 #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
🔴 #4819: Definition for parent interface not generated
🔴 #4358: Incorrect mapping of sub-classes when nested as member of another sub-class
🔴 #4352: Inconsistent behaviour for oneOf
🔴 #4330: Missing oneOf sections for polymorphic schema
🔴 #4156: Sealed kotlin class with oneOf annotation generates a type: object
🔴 #3689: @ArraySchema is ignored in model property (array of abstract class)
🔴 #3677: How to prevent oneOf() on supertypes leading to recursion in subtypes?
🔴 #3567: Question: Is there a way to specify allowableValues dynamically?
🔴 #3411: Consider the @JsonSubTypes for generating the discriminator mapping
🟢 #5003: [Bug]: Regression: AnnotatedType equality change drops polymorphic subtype allOf entries
🟢 #4959: [Bug]: Superfluous sibling $ref when using oneOf in Schema annotation - OAS 3.1
🟢 #4852: StackOverflowError with recursive data model introduced with version 2.2.28
🟢 #4822: Duplicate Schema References in oneOf - OAS 3.1

... and 5 more issues

#### Schema Generation & OAS Spec (12 issues, 10 open)

*General schema generation, OpenAPI spec creation, and spec compliance*

🔴 #4951: [Feature]: support for runtime skip properties inside ModelConverter
🔴 #4924: Ability to remove unused definitions from generated YML
🔴 #4843: OpenApi not generated when resourceClasses used
🔴 #4837: Is it possible to convert from OAS 3.0 to OAS 3.1 using this lib?
🔴 #4789: Classes / enums with same name but different package replace each other
🔴 #4404: Add support for more java.time objects
🔴 #3577: [bug] Integer enum support
🔴 #3025: openapi.json fails to take into account servlet context path, produces bad spec file
🔴 #2961: Field with first letter lower case not recognized
🔴 #2192: Field name is getting generated wrongly if field type is &#34;Boolean&#34; (wrapper class for boolean) instead of primitive &#34;boolean&#34; type
🟢 #4845: Missing property $dynamicRef in Schema model for OpenAPI v3.1
🟢 #4817: RemovingUnreferencedDefinitions functionality ignores schema refs from patternProperties

#### Kotlin Support (5 issues, 3 open)

*Kotlin-specific issues including inline classes and sealed classes*

🔴 #5007: [Bug]: kotlin inline classes produce weird field names
🔴 #4506: Jackson @JsonFormat doesn&#39;t support Enum type
🔴 #4156: Sealed kotlin class with oneOf annotation generates a type: object
🟢 #4932: Using open api 3.1  - Putting  annotation - `@field:Schema(enumAsRef = true)` - on a property does not result in `$ref`
🟢 #4812: isRemovingUnreferencedDefinitions removes $ref used in oneOf.

#### Other/Uncategorized (3 issues, 1 open)

🔴 #4976: [Feature]: Reduce Boilerplate in swagger-core Models (Optional Lombok Support or Alternatives)
🟢 #4941: Autoswagger
🟢 #2224: [Deleted]

---

## Approach 3: Categorization by Priority/Severity

This approach prioritizes issues based on their impact and urgency. This is useful for:
- Determining what to work on next
- Managing technical debt vs. new features
- Emergency response and hotfix planning

### Priority Levels Defined

- **Critical/Regressions**: Breaking changes, regressions from previous versions, blockers
- **High Priority**: Bugs causing exceptions, data loss, or incorrect behavior
- **Medium Priority**: Standard bugs and improvements with moderate impact
- **Low Priority/Enhancement**: Feature requests and minor improvements
- **Questions/Documentation**: User questions and documentation requests

### Summary Statistics

**Critical/Regressions:** 15 total (9 open, 6 closed)
**High Priority:** 51 total (28 open, 23 closed)
**Medium Priority:** 77 total (44 open, 33 closed)
**Low Priority/Enhancement:** 10 total (9 open, 1 closed)
**Questions/Documentation:** 2 total (2 open, 0 closed)

### Detailed Breakdown

#### Critical/Regressions (15 issues, 9 open)

🔴 #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference [Bug]
🔴 #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas [Bug]
🔴 #4991: [Feature] Support Jackson 3.x release line [Feature]
🔴 #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
🔴 #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
🔴 #4841: 2.2.27 &amp; 2.2.28: org.gradle.api.internal.DefaultMutationGuard$IllegalMutationException: Project#afterEvaluate(Action) on project &#39;:XX&#39; cannot be executed in the current context. [Bug, P1]
🔴 #4813: Upgrade to v2.2.27: Cannot change dependencies of dependency configuration &#39;&#39; after it has been included in dependency resolution [Bug, P1]
🔴 #4649: Generated but not references ResponseEntity*** type in schema if rest-api-class is using spring&#39;s ResponseEntity
🔴 #4555: Could `@Schema(nullable = true)` work when outputting OpenAPI 3.1.0?
🟢 #5003: [Bug]: Regression: AnnotatedType equality change drops polymorphic subtype allOf entries [Bug]
🟢 #4865: Regression in 2.2.29 for Pageable (from spring)
🟢 #4862: 2.2.29 do not treat @NotBlank as mandatory, breaking change? [Bug, P2]
🟢 #4860: breaking change in ModelResolver (2.2.29) - protected method addRequiredItem
🟢 #4850: Extensions no longer enforced to be spec compliant between OpenAPI 3 and OpenAPI 3.1 [Bug, P2]
🟢 #4316: Annotating class with @Schema(additionalProperties = AdditionalPropertiesValue.FALSE) does not produce JSON/YAML with additionalProperties: false [Bug, P1]

#### High Priority (51 issues, 28 open)

🔴 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl [Bug]
🔴 #5036: [Bug]: Example text is truncated when beginning with numbers [Bug]
🔴 #5035: [Bug]: Jakarta @Positive validation is not present in Swagger [Bug]
🔴 #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas [Bug]
🔴 #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten [Question]
🔴 #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1 [Bug]
🔴 #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema [Bug]
🔴 #5007: [Bug]: kotlin inline classes produce weird field names [Bug]
🔴 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types [Bug]
🔴 #4999: [Bug]: Example value starting with a number is parsed incorrectly [Bug]
🔴 #4976: [Feature]: Reduce Boilerplate in swagger-core Models (Optional Lombok Support or Alternatives) [Feature]
🔴 #4963: [Bug]: Primitive boxed type schemas used in @ApiResponse content schema implementations are incorrectly cast to string [Bug]
🔴 #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field [Bug]
🔴 #4939: Integer enum Schema parsing can fail based upon locale [Question]
🔴 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34; [Question]
🔴 #4923: Custom UUID wrapper class can&#39;t be used anymore after 2.2.28
🔴 #4906: java.lang.Object converted to json object (map) type
🔴 #4904: java.lang.ClassCastException: class io.swagger.v3.oas.models.media.JsonSchema cannot be cast to class io.swagger.v3.oas.models.media.ComposedSchema
🔴 #4903: jackson module not working in 2.2.30
🔴 #4854: Bootstrapped servlet returns empty JSON as of 2.2.24

... and 31 more issues

#### Medium Priority (77 issues, 44 open)

🔴 #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
🔴 #4924: Ability to remove unused definitions from generated YML
🔴 #4908: defaultValue attribute at enum object generate arbitrary json file
🔴 #4905: CustomResolver&#39;s Results get overwritten by default ModelResolver
🔴 #4887: Order of `required` properties
🔴 #4886: Validation Meta annotations not working
🔴 #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
🔴 #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
🔴 #4864: generate OpenApi yml file with swagger-maven-plugin and with Java 17 #903
🔴 #4863: &#34;Override&#34; example value when extending a class
🔴 #4851: The example value of the request body in swagger has a &#34;string&#34; value
🔴 #4848: ModelResolver#resolve ignores TYPE_USE annotations on property types
🔴 #4847: @Schema annotation on custom string type is ignored when using @JsonValue
🔴 #4846: Swagger adds extra line breaks in yaml generated from annotated code
🔴 #4844: swagger-annotations: Add annotations to support x-enum-descriptions and x-enum-varnames
🔴 #4843: OpenApi not generated when resourceClasses used
🔴 #4839: [Feature/Bug] Forbid mutliple `@ApiResponse` annotations with the same response code
🔴 #4837: Is it possible to convert from OAS 3.0 to OAS 3.1 using this lib?
🔴 #4824: Bill of material
🔴 #4820: @Schema(example=&#34;1234&#34;, type=&#34;string&#34;) interpreted as integer

... and 57 more issues

#### Low Priority/Enhancement (10 issues, 9 open)

🔴 #5045: [Feature]: Need since or reason field in @Hidden annotation [Feature]
🔴 #5011: [Feature]: Drop commons-lang3 dependency and replace its usages with core Java [Feature]
🔴 #4966: [Feature]: Provide a bom module for managing dependencies [Feature]
🔴 #4951: [Feature]: support for runtime skip properties inside ModelConverter [Feature]
🔴 #4928: Lack of Friendly Support for Parameter Validation Grouping
🔴 #4840: [Feature] Mark type as deprecated if the class is `@Deprecated`
🔴 #4838: Empty default values are ignored [Feature]
🔴 #3411: Consider the @JsonSubTypes for generating the discriminator mapping [Feature, Workaround available, P2, :thumbsup: Top Issue!]
🔴 #3323: Generic Response Types [Feature]
🟢 #5041: [Feature]: I want to configure the default value of @Schema RequiredMode to other value, than AUTO [Feature]

#### Questions/Documentation (2 issues, 2 open)

🔴 #5014: [Question]: How to specify @Schema.examples where each element is an array? [Question]
🔴 #4984: [Question]: Duplicate records if Class and Operation both has @Path param [Question]

---

## Recommendations

Based on this analysis, here are recommendations for each approach:

### Approach 1: By Issue Type and Status
**Best for:** Traditional project management, sprint planning, tracking work types
**Use when:** You need to balance different types of work (bugs vs. features) or track resolution rates

**Key Insights:**
- Bugs represent the majority of issues, indicating stability concerns
- Feature requests show active community engagement
- Questions suggest areas where documentation could be improved

### Approach 2: By Functional Area
**Best for:** Team assignment, identifying problem areas, specialized expertise allocation
**Use when:** You have team members with different areas of expertise or want to identify which components need attention

**Key Insights:**
- Annotation Processing has the most issues - may need architectural review
- Validation & Required Fields is a hot area - suggests API clarity issues
- Jakarta Migration is ongoing - dedicated effort may be needed

### Approach 3: By Priority/Severity
**Best for:** Triage, roadmap planning, deciding what to work on next
**Use when:** You need to prioritize limited resources or plan releases

**Key Insights:**
- Several regressions need immediate attention
- High-priority bugs should be addressed before new features
- Low-priority enhancements can be deferred for community contributions

---

## Conclusion

All three approaches provide value depending on your goals:

1. **Use Approach 1** if you need traditional issue tracking and want to understand the types of work
2. **Use Approach 2** if you're assigning work to specialized teams or identifying problem areas
3. **Use Approach 3** if you're doing triage, release planning, or need to prioritize work

You can also use them in combination - for example, using Approach 3 to decide what to work on, then Approach 2 to decide who should work on it.

