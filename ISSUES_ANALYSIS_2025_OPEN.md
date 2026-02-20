# Swagger-Core Open Issues Analysis (2025)

**Analysis Date:** 2026-02-03  
**Total Open Issues Analyzed:** 92  
**Date Range:** Since January 1, 2025

---

## Executive Summary

This analysis examines **92 OPEN issues** reported to the swagger-api/swagger-core repository since January 1, 2025. The issues have been categorized using three approaches, with a special **Combined Approach** that merges functional areas with priority levels:

1. **By Issue Type** - Traditional categorization (Bug, Feature, Question, etc.)
2. **By Functional Area** - Groups by technical domain for specialized team assignment
3. **By Priority/Severity** - Prioritizes work based on impact and urgency
4. **Combined: Functional Areas by Priority** ⭐ - Shows which functional areas have the highest priority issues

---

## How Priorities Are Determined

Priority is calculated using a **weighted scoring system** that considers multiple factors:

### Priority Scoring Methodology

| Priority Level | Score | Criteria |
|---------------|-------|----------|
| **Critical** | 5 | • Regressions (stopped working, breaking changes)<br>• Issues with P0/P1 labels<br>• Keywords: "regression", "breaking change", "stopped working" |
| **High** | 4 | • Exceptions and crashes<br>• All bugs (Bug label)<br>• Keywords: "exception", "crash", "nullpointer", "fails" |
| **Medium** | 3 | • Standard issues without special markers<br>• General improvements |
| **Low** | 2 | • Feature requests<br>• Enhancements |
| **Low** | 1 | • Questions<br>• Documentation requests |

**Average Priority Score** for each functional area is calculated as the mean of all issue scores in that area. This helps identify which functional areas need the most urgent attention.

---

## Approach 1: By Issue Type

This approach groups issues by their type. **OPEN ISSUES ONLY.**

### Summary Statistics

**Bug:** 16 open
**Feature:** 10 open
**Question:** 5 open
**Other:** 61 open


### Detailed Breakdown

#### Bug (16 open issues)

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

#### Feature (10 open issues)

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

#### Question (5 open issues)

- #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten
- #5014: [Question]: How to specify @Schema.examples where each element is an array?
- #4984: [Question]: Duplicate records if Class and Operation both has @Path param
- #4939: Integer enum Schema parsing can fail based upon locale
- #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;

#### Other (61 open issues)

- #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
- #4928: Lack of Friendly Support for Parameter Validation Grouping
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
- ... and 41 more

---

## Approach 2: By Functional Area

This approach groups issues by technical domain and functionality. **OPEN ISSUES ONLY.**

### Summary Statistics (sorted by issue count)

**Annotation Processing:** 63 open issues
**Examples:** 34 open issues
**Validation:** 25 open issues
**Dependencies:** 21 open issues
**Jakarta Migration:** 20 open issues
**Jackson:** 17 open issues
**Other:** 12 open issues
**Polymorphic Types:** 11 open issues
**Kotlin:** 3 open issues

### Detailed Breakdown

#### Annotation Processing (63 open issues)

*Issues related to @Schema, @ArraySchema, @Hidden, and other annotations*

🟢 #5045: [Feature]: Need since or reason field in @Hidden annotation
🟠 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
🟠 #5036: [Bug]: Example text is truncated when beginning with numbers
🟠 #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
🟠 #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten
🟢 #5014: [Question]: How to specify @Schema.examples where each element is an array?
🟠 #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1
🟠 #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
🟠 #5007: [Bug]: kotlin inline classes produce weird field names
🟠 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🟠 #4999: [Bug]: Example value starting with a number is parsed incorrectly
🔴 #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas
🟢 #4984: [Question]: Duplicate records if Class and Operation both has @Path param
🟠 #4963: [Bug]: Primitive boxed type schemas used in @ApiResponse content schema implementations are incorrectly cast to string
🟠 #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field

... and 48 more issues

#### Examples (34 open issues)

*Issues with example values and documentation generation*

🟠 #5036: [Bug]: Example text is truncated when beginning with numbers
🟡 #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
🟠 #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
🟢 #5014: [Question]: How to specify @Schema.examples where each element is an array?
🟠 #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
🟠 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🟠 #4999: [Bug]: Example value starting with a number is parsed incorrectly
🟢 #4984: [Question]: Duplicate records if Class and Operation both has @Path param
🟢 #4966: [Feature]: Provide a bom module for managing dependencies
🟢 #4939: Integer enum Schema parsing can fail based upon locale
🟠 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
🟡 #4928: Lack of Friendly Support for Parameter Validation Grouping
🟠 #4923: Custom UUID wrapper class can&#39;t be used anymore after 2.2.28
🔴 #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
🟡 #4908: defaultValue attribute at enum object generate arbitrary json file

... and 19 more issues

#### Validation (25 open issues)

*Issues with validation annotations, required fields, and RequiredMode*

🟠 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
🟠 #5036: [Bug]: Example text is truncated when beginning with numbers
🟠 #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
🟡 #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
🟠 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🟠 #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field
🟡 #4928: Lack of Friendly Support for Parameter Validation Grouping
🔴 #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
🔴 #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
🟡 #4887: Order of `required` properties
🟡 #4886: Validation Meta annotations not working
🟡 #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
🟠 #4842: OpenApi doc not beeing generated properly
🔴 #4841: 2.2.27 &amp; 2.2.28: org.gradle.api.internal.DefaultMutationGuard$IllegalMutationException: Project#afterEvaluate(Action) on project &#39;:XX&#39; cannot be executed in the current context.
🟢 #4838: Empty default values are ignored

... and 10 more issues

#### Dependencies (21 open issues)

*Issues with dependencies, Maven/Gradle plugins, and build processes*

🔴 #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
🟠 #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1
🟠 #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
🟢 #5011: [Feature]: Drop commons-lang3 dependency and replace its usages with core Java
🟠 #5007: [Bug]: kotlin inline classes produce weird field names
🟠 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🔴 #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas
🟢 #4984: [Question]: Duplicate records if Class and Operation both has @Path param
🟢 #4966: [Feature]: Provide a bom module for managing dependencies
🟠 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
🟡 #4908: defaultValue attribute at enum object generate arbitrary json file
🟡 #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
🟡 #4864: generate OpenApi yml file with swagger-maven-plugin and with Java 17 #903
🟡 #4846: Swagger adds extra line breaks in yaml generated from annotated code
🟠 #4842: OpenApi doc not beeing generated properly

... and 6 more issues

#### Jakarta Migration (20 open issues)

*Issues related to Jakarta namespace migration and Jakarta annotations*

🟠 #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
🟠 #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
🟠 #5007: [Bug]: kotlin inline classes produce weird field names
🟠 #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
🟠 #4999: [Bug]: Example value starting with a number is parsed incorrectly
🟢 #4984: [Question]: Duplicate records if Class and Operation both has @Path param
🟠 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
🟡 #4928: Lack of Friendly Support for Parameter Validation Grouping
🔴 #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
🟡 #4908: defaultValue attribute at enum object generate arbitrary json file
🟠 #4903: jackson module not working in 2.2.30
🟡 #4886: Validation Meta annotations not working
🟡 #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
🟡 #4846: Swagger adds extra line breaks in yaml generated from annotated code
🟠 #4842: OpenApi doc not beeing generated properly

... and 5 more issues

#### Jackson (17 open issues)

*Issues with Jackson annotations and serialization/deserialization*

🔴 #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
🟠 #5007: [Bug]: kotlin inline classes produce weird field names
🔴 #4991: [Feature] Support Jackson 3.x release line
🟠 #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
🟡 #4905: CustomResolver&#39;s Results get overwritten by default ModelResolver
🟠 #4904: java.lang.ClassCastException: class io.swagger.v3.oas.models.media.JsonSchema cannot be cast to class io.swagger.v3.oas.models.media.ComposedSchema
🟠 #4903: jackson module not working in 2.2.30
🟡 #4848: ModelResolver#resolve ignores TYPE_USE annotations on property types
🟡 #4847: @Schema annotation on custom string type is ignored when using @JsonValue
🟠 #4842: OpenApi doc not beeing generated properly
🟠 #4819: Definition for parent interface not generated
🟡 #4696: Using @JsonUnwrapped with @Schema(implementation = Something) does not unwrap
🟠 #4682: BUG properties of type Object do not have any type information when generating open api 3.1.0
🟡 #4506: Jackson @JsonFormat doesn&#39;t support Enum type
🟡 #4156: Sealed kotlin class with oneOf annotation generates a type: object

... and 2 more issues

#### Other (12 open issues)

🟠 #4976: [Feature]: Reduce Boilerplate in swagger-core Models (Optional Lombok Support or Alternatives)
🟢 #4951: [Feature]: support for runtime skip properties inside ModelConverter
🟡 #4924: Ability to remove unused definitions from generated YML
🟠 #4906: java.lang.Object converted to json object (map) type
🟡 #4843: OpenApi not generated when resourceClasses used
🟡 #4837: Is it possible to convert from OAS 3.0 to OAS 3.1 using this lib?
🟡 #4789: Classes / enums with same name but different package replace each other
🟡 #4404: Add support for more java.time objects
🟡 #3577: [bug] Integer enum support
🟠 #3025: openapi.json fails to take into account servlet context path, produces bad spec file
🟡 #2961: Field with first letter lower case not recognized
🟡 #2192: Field name is getting generated wrongly if field type is &#34;Boolean&#34; (wrapper class for boolean) instead of primitive &#34;boolean&#34; type

#### Polymorphic Types (11 open issues)

*Issues with oneOf, allOf, discriminators, and class hierarchies*

🔴 #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
🟡 #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
🟠 #4819: Definition for parent interface not generated
🟠 #4358: Incorrect mapping of sub-classes when nested as member of another sub-class
🟡 #4352: Inconsistent behaviour for oneOf
🟡 #4330: Missing oneOf sections for polymorphic schema
🟡 #4156: Sealed kotlin class with oneOf annotation generates a type: object
🟡 #3689: @ArraySchema is ignored in model property (array of abstract class)
🟡 #3677: How to prevent oneOf() on supertypes leading to recursion in subtypes?
🟡 #3567: Question: Is there a way to specify allowableValues dynamically?
🟢 #3411: Consider the @JsonSubTypes for generating the discriminator mapping

#### Kotlin (3 open issues)

*Kotlin-specific issues including inline classes and sealed classes*

🟠 #5007: [Bug]: kotlin inline classes produce weird field names
🟡 #4506: Jackson @JsonFormat doesn&#39;t support Enum type
🟡 #4156: Sealed kotlin class with oneOf annotation generates a type: object

---

## Approach 3: By Priority/Severity

This approach prioritizes issues based on their impact and urgency. **OPEN ISSUES ONLY.**

### Priority Levels

**Critical:** 9 open issues
**High:** 27 open issues
**Medium:** 46 open issues
**Low:** 10 open issues

### Detailed Breakdown

#### Critical Priority (9 open issues)

- #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference [Bug]
- #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas [Bug]
- #4991: [Feature] Support Jackson 3.x release line [Feature]
- #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
- #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
- #4841: 2.2.27 &amp; 2.2.28: org.gradle.api.internal.DefaultMutationGuard$IllegalMutationException: Project#afterEvaluate(Action) on project &#39;:XX&#39; cannot be executed in the current context. [Bug, P1]
- #4813: Upgrade to v2.2.27: Cannot change dependencies of dependency configuration &#39;&#39; after it has been included in dependency resolution [Bug, P1]
- #4649: Generated but not references ResponseEntity*** type in schema if rest-api-class is using spring&#39;s ResponseEntity
- #4555: Could `@Schema(nullable = true)` work when outputting OpenAPI 3.1.0?

#### High Priority (27 open issues)

- #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl [Bug]
- #5036: [Bug]: Example text is truncated when beginning with numbers [Bug]
- #5035: [Bug]: Jakarta @Positive validation is not present in Swagger [Bug]
- #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas [Bug]
- #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten [Question]
- #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1 [Bug]
- #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema [Bug]
- #5007: [Bug]: kotlin inline classes produce weird field names [Bug]
- #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types [Bug]
- #4999: [Bug]: Example value starting with a number is parsed incorrectly [Bug]
- #4976: [Feature]: Reduce Boilerplate in swagger-core Models (Optional Lombok Support or Alternatives) [Feature]
- #4963: [Bug]: Primitive boxed type schemas used in @ApiResponse content schema implementations are incorrectly cast to string [Bug]
- #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field [Bug]
- #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34; [Question]
- #4923: Custom UUID wrapper class can&#39;t be used anymore after 2.2.28
- #4906: java.lang.Object converted to json object (map) type
- #4904: java.lang.ClassCastException: class io.swagger.v3.oas.models.media.JsonSchema cannot be cast to class io.swagger.v3.oas.models.media.ComposedSchema
- #4903: jackson module not working in 2.2.30
- #4854: Bootstrapped servlet returns empty JSON as of 2.2.24
- #4842: OpenApi doc not beeing generated properly

... and 7 more issues

#### Medium Priority (46 open issues)

- #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
- #4928: Lack of Friendly Support for Parameter Validation Grouping
- #4924: Ability to remove unused definitions from generated YML
- #4908: defaultValue attribute at enum object generate arbitrary json file
- #4905: CustomResolver&#39;s Results get overwritten by default ModelResolver
- #4887: Order of `required` properties
- #4886: Validation Meta annotations not working
- #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
- #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
- #4864: generate OpenApi yml file with swagger-maven-plugin and with Java 17 #903
- #4863: &#34;Override&#34; example value when extending a class
- #4851: The example value of the request body in swagger has a &#34;string&#34; value
- #4848: ModelResolver#resolve ignores TYPE_USE annotations on property types
- #4847: @Schema annotation on custom string type is ignored when using @JsonValue
- #4846: Swagger adds extra line breaks in yaml generated from annotated code
- #4844: swagger-annotations: Add annotations to support x-enum-descriptions and x-enum-varnames
- #4843: OpenApi not generated when resourceClasses used
- #4840: [Feature] Mark type as deprecated if the class is `@Deprecated`
- #4839: [Feature/Bug] Forbid mutliple `@ApiResponse` annotations with the same response code
- #4837: Is it possible to convert from OAS 3.0 to OAS 3.1 using this lib?

... and 26 more issues

#### Low Priority (10 open issues)

- #5045: [Feature]: Need since or reason field in @Hidden annotation [Feature]
- #5014: [Question]: How to specify @Schema.examples where each element is an array? [Question]
- #5011: [Feature]: Drop commons-lang3 dependency and replace its usages with core Java [Feature]
- #4984: [Question]: Duplicate records if Class and Operation both has @Path param [Question]
- #4966: [Feature]: Provide a bom module for managing dependencies [Feature]
- #4951: [Feature]: support for runtime skip properties inside ModelConverter [Feature]
- #4939: Integer enum Schema parsing can fail based upon locale [Question]
- #4838: Empty default values are ignored [Feature]
- #3411: Consider the @JsonSubTypes for generating the discriminator mapping [Feature, Workaround available, P2, :thumbsup: Top Issue!]
- #3323: Generic Response Types [Feature]

---

## ⭐ Combined Approach: Functional Areas by Priority

This approach **combines Approach 2 and Approach 3** to show which functional areas have the highest priority issues. This helps answer: **"Which functional area should we focus on first?"**

### Functional Areas Ranked by Average Priority Score

The table below shows each functional area ranked by its average priority score, along with the distribution of issue priorities.

| Rank | Functional Area | Total | Avg Score | Critical | High | Medium | Low | Priority Level |
|------|----------------|-------|-----------|----------|------|--------|-----|----------------|
| 1 | **Validation** | 25 | **3.64** | 5 | 9 | 8 | 3 | 🟠 HIGH |
| 2 | **Jackson** | 17 | **3.59** | 2 | 7 | 7 | 1 | 🟠 HIGH |
| 3 | **Dependencies** | 21 | **3.57** | 5 | 7 | 5 | 4 | 🟠 HIGH |
| 4 | **Jakarta Migration** | 20 | **3.55** | 2 | 9 | 8 | 1 | 🟠 HIGH |
| 5 | **Annotation Processing** | 63 | **3.33** | 5 | 19 | 33 | 6 | 🟡 MEDIUM |
| 6 | **Kotlin** | 3 | **3.33** | 0 | 1 | 2 | 0 | 🟡 MEDIUM |
| 7 | **Polymorphic Types** | 11 | **3.27** | 1 | 2 | 7 | 1 | 🟡 MEDIUM |
| 8 | **Examples** | 34 | **3.21** | 2 | 11 | 16 | 5 | 🟡 MEDIUM |
| 9 | **Other** | 12 | **3.17** | 0 | 3 | 8 | 1 | 🟡 MEDIUM |


### Interpretation Guide

- **Avg Score:** Higher scores (closer to 5.0) indicate more urgent issues
- **Priority Level:** 
  - 🔴 CRITICAL (4.0+): Immediate attention required
  - 🟠 HIGH (3.5-3.9): Should be prioritized soon
  - 🟡 MEDIUM (2.5-3.4): Standard priority
  - 🟢 LOW (<2.5): Can be deferred

### Key Insights from Combined Analysis

**Top 3 Priority Areas:**

1. **Validation** (Avg: 3.64)
   - 25 open issues
   - 5 critical, 9 high priority
   - Critical issues: #4910, #4909, #4841, +2 more

2. **Jackson** (Avg: 3.59)
   - 17 open issues
   - 2 critical, 7 high priority
   - Critical issues: #5028, #4991

3. **Dependencies** (Avg: 3.57)
   - 21 open issues
   - 5 critical, 7 high priority
   - Critical issues: #5028, #4993, #4841, +2 more


**Recommendation:** Focus development effort on the top-ranked functional areas, starting with their Critical and High priority issues.

---

## Detailed Priority Breakdown by Functional Area

This section provides detailed listings of issues in each functional area, grouped by priority.

### Validation
**Total:** 25 | **Avg Score:** 3.64 | Critical: 5 | High: 9 | Medium: 8 | Low: 3

**🔴 Critical Priority (5 issues):**
- #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
- #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
- #4841: 2.2.27 &amp; 2.2.28: org.gradle.api.internal.DefaultMutationGuard$IllegalMutationException: Project#afterEvaluate(Action) on project &#39;:XX&#39; cannot be executed in the current context.
- #4649: Generated but not references ResponseEntity*** type in schema if rest-api-class is using spring&#39;s ResponseEntity
- #4555: Could `@Schema(nullable = true)` work when outputting OpenAPI 3.1.0?

**🟠 High Priority (9 issues):**
- #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
- #5036: [Bug]: Example text is truncated when beginning with numbers
- #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
- #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
- #4960: [Bug]: SpecFilter skips the ApiResponse#$ref field
- #4842: OpenApi doc not beeing generated properly
- #4819: Definition for parent interface not generated
- #4763: Explicit schema type specified on `@Parameter` annotation is always ignored
- #4689: BUG Unable to deserialize OpenAPI Parameter

**🟡 Medium Priority (8 issues):**
- #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
- #4928: Lack of Friendly Support for Parameter Validation Grouping
- #4887: Order of `required` properties
- #4886: Validation Meta annotations not working
- #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
- #4753: @Schema field-level description ignored when using custom type with $ref #2723
- #4711: @Schema is processed without enum when implementation and allowableValues exist at the same time
- #3818: Maps that use enums as keys don&#39;t explicitly declare them in the resulting swagger def.

**🟢 Low Priority (3 issues):**
- #4838: Empty default values are ignored
- #3411: Consider the @JsonSubTypes for generating the discriminator mapping
- #3323: Generic Response Types

---

### Jackson
**Total:** 17 | **Avg Score:** 3.59 | Critical: 2 | High: 7 | Medium: 7 | Low: 1

**🔴 Critical Priority (2 issues):**
- #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
- #4991: [Feature] Support Jackson 3.x release line

**🟠 High Priority (7 issues):**
- #5007: [Bug]: kotlin inline classes produce weird field names
- #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
- #4904: java.lang.ClassCastException: class io.swagger.v3.oas.models.media.JsonSchema cannot be cast to class io.swagger.v3.oas.models.media.ComposedSchema
- #4903: jackson module not working in 2.2.30
- #4842: OpenApi doc not beeing generated properly
- #4819: Definition for parent interface not generated
- #4682: BUG properties of type Object do not have any type information when generating open api 3.1.0

**🟡 Medium Priority (7 issues):**
- #4905: CustomResolver&#39;s Results get overwritten by default ModelResolver
- #4848: ModelResolver#resolve ignores TYPE_USE annotations on property types
- #4847: @Schema annotation on custom string type is ignored when using @JsonValue
- #4696: Using @JsonUnwrapped with @Schema(implementation = Something) does not unwrap
- #4506: Jackson @JsonFormat doesn&#39;t support Enum type
- #4156: Sealed kotlin class with oneOf annotation generates a type: object
- #3677: How to prevent oneOf() on supertypes leading to recursion in subtypes?

**🟢 Low Priority (1 issues):**
- #3411: Consider the @JsonSubTypes for generating the discriminator mapping

---

### Dependencies
**Total:** 21 | **Avg Score:** 3.57 | Critical: 5 | High: 7 | Medium: 5 | Low: 4

**🔴 Critical Priority (5 issues):**
- #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference
- #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas
- #4841: 2.2.27 &amp; 2.2.28: org.gradle.api.internal.DefaultMutationGuard$IllegalMutationException: Project#afterEvaluate(Action) on project &#39;:XX&#39; cannot be executed in the current context.
- #4813: Upgrade to v2.2.27: Cannot change dependencies of dependency configuration &#39;&#39; after it has been included in dependency resolution
- #4649: Generated but not references ResponseEntity*** type in schema if rest-api-class is using spring&#39;s ResponseEntity

**🟠 High Priority (7 issues):**
- #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1
- #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
- #5007: [Bug]: kotlin inline classes produce weird field names
- #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
- #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
- #4842: OpenApi doc not beeing generated properly
- #4763: Explicit schema type specified on `@Parameter` annotation is always ignored

**🟡 Medium Priority (5 issues):**
- #4908: defaultValue attribute at enum object generate arbitrary json file
- #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
- #4864: generate OpenApi yml file with swagger-maven-plugin and with Java 17 #903
- #4846: Swagger adds extra line breaks in yaml generated from annotated code
- #4824: Bill of material

**🟢 Low Priority (4 issues):**
- #5011: [Feature]: Drop commons-lang3 dependency and replace its usages with core Java
- #4984: [Question]: Duplicate records if Class and Operation both has @Path param
- #4966: [Feature]: Provide a bom module for managing dependencies
- #3411: Consider the @JsonSubTypes for generating the discriminator mapping

---

### Jakarta Migration
**Total:** 20 | **Avg Score:** 3.55 | Critical: 2 | High: 9 | Medium: 8 | Low: 1

**🔴 Critical Priority (2 issues):**
- #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
- #4649: Generated but not references ResponseEntity*** type in schema if rest-api-class is using spring&#39;s ResponseEntity

**🟠 High Priority (9 issues):**
- #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
- #5035: [Bug]: Jakarta @Positive validation is not present in Swagger
- #5007: [Bug]: kotlin inline classes produce weird field names
- #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
- #4999: [Bug]: Example value starting with a number is parsed incorrectly
- #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
- #4903: jackson module not working in 2.2.30
- #4842: OpenApi doc not beeing generated properly
- #4763: Explicit schema type specified on `@Parameter` annotation is always ignored

**🟡 Medium Priority (8 issues):**
- #4928: Lack of Friendly Support for Parameter Validation Grouping
- #4908: defaultValue attribute at enum object generate arbitrary json file
- #4886: Validation Meta annotations not working
- #4877: 2.2.30 - @NotNull(jakarta.validation.constraints.NotNull) assigned to a group should treat field as mandatory
- #4846: Swagger adds extra line breaks in yaml generated from annotated code
- #4824: Bill of material
- #4753: @Schema field-level description ignored when using custom type with $ref #2723
- #4711: @Schema is processed without enum when implementation and allowableValues exist at the same time

**🟢 Low Priority (1 issues):**
- #4984: [Question]: Duplicate records if Class and Operation both has @Path param

---

### Annotation Processing
**Total:** 63 | **Avg Score:** 3.33 | Critical: 5 | High: 19 | Medium: 33 | Low: 6

**🔴 Critical Priority (5 issues):**
- #4993: [Bug]: Regression: class specified in @ArraySchema.schema.implementation not rendered in schemas
- #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
- #4909: Custom NotNull annotations stopped working between 2.2.28 and 2.2.29
- #4649: Generated but not references ResponseEntity*** type in schema if rest-api-class is using spring&#39;s ResponseEntity
- #4555: Could `@Schema(nullable = true)` work when outputting OpenAPI 3.1.0?

**🟠 High Priority (19 issues):**
- #5043: [Bug]: AnnotatedType equality in ModelConverterContextImpl
- #5036: [Bug]: Example text is truncated when beginning with numbers
- #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
- #5016: [Question]: Duplicate schema names don&#39;t throw but are overwritten
- #5013: [Bug]: @ArraysSchema applied on a stream property results in object type in OAS 3.1
- #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
- #5007: [Bug]: kotlin inline classes produce weird field names
- #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
- #4999: [Bug]: Example value starting with a number is parsed incorrectly
- #4963: [Bug]: Primitive boxed type schemas used in @ApiResponse content schema implementations are incorrectly cast to string
- ... and 9 more

**🟡 Medium Priority (33 issues):**
- #4928: Lack of Friendly Support for Parameter Validation Grouping
- #4908: defaultValue attribute at enum object generate arbitrary json file
- #4905: CustomResolver&#39;s Results get overwritten by default ModelResolver
- #4886: Validation Meta annotations not working
- #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
- #4864: generate OpenApi yml file with swagger-maven-plugin and with Java 17 #903
- #4863: &#34;Override&#34; example value when extending a class
- #4851: The example value of the request body in swagger has a &#34;string&#34; value
- #4848: ModelResolver#resolve ignores TYPE_USE annotations on property types
- #4847: @Schema annotation on custom string type is ignored when using @JsonValue
- ... and 23 more

**🟢 Low Priority (6 issues):**
- #5045: [Feature]: Need since or reason field in @Hidden annotation
- #5014: [Question]: How to specify @Schema.examples where each element is an array?
- #4984: [Question]: Duplicate records if Class and Operation both has @Path param
- #4838: Empty default values are ignored
- #3411: Consider the @JsonSubTypes for generating the discriminator mapping
- #3323: Generic Response Types

---

### Kotlin
**Total:** 3 | **Avg Score:** 3.33 | Critical: 0 | High: 1 | Medium: 2 | Low: 0

**🟠 High Priority (1 issues):**
- #5007: [Bug]: kotlin inline classes produce weird field names

**🟡 Medium Priority (2 issues):**
- #4506: Jackson @JsonFormat doesn&#39;t support Enum type
- #4156: Sealed kotlin class with oneOf annotation generates a type: object

---

### Polymorphic Types
**Total:** 11 | **Avg Score:** 3.27 | Critical: 1 | High: 2 | Medium: 7 | Low: 1

**🔴 Critical Priority (1 issues):**
- #5028: [Bug]: Polymorphic types don&#39;t include composite schema reference

**🟠 High Priority (2 issues):**
- #4819: Definition for parent interface not generated
- #4358: Incorrect mapping of sub-classes when nested as member of another sub-class

**🟡 Medium Priority (7 issues):**
- #4882: swagger-maven-plugin: OPERATION annotation with multiple TAGS does not correctly generate openapi.json using inheritance in Java
- #4352: Inconsistent behaviour for oneOf
- #4330: Missing oneOf sections for polymorphic schema
- #4156: Sealed kotlin class with oneOf annotation generates a type: object
- #3689: @ArraySchema is ignored in model property (array of abstract class)
- #3677: How to prevent oneOf() on supertypes leading to recursion in subtypes?
- #3567: Question: Is there a way to specify allowableValues dynamically?

**🟢 Low Priority (1 issues):**
- #3411: Consider the @JsonSubTypes for generating the discriminator mapping

---

### Examples
**Total:** 34 | **Avg Score:** 3.21 | Critical: 2 | High: 11 | Medium: 16 | Low: 5

**🔴 Critical Priority (2 issues):**
- #4910: Create an annotation the add at controller level to summarize information regarding failed http responses.
- #4649: Generated but not references ResponseEntity*** type in schema if rest-api-class is using spring&#39;s ResponseEntity

**🟠 High Priority (11 issues):**
- #5036: [Bug]: Example text is truncated when beginning with numbers
- #5017: [Bug]: OpenAPI 3.1: Enum used in @Schema#propertyNames removed when filtering unreferenced schemas
- #5012: [Bug]: The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
- #5001: [Bug]: Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
- #4999: [Bug]: Example value starting with a number is parsed incorrectly
- #4935: Jackson dependency update results in &#34;java 8 date/time type java.time.LocalDateTime not supported by default&#34;
- #4923: Custom UUID wrapper class can&#39;t be used anymore after 2.2.28
- #4854: Bootstrapped servlet returns empty JSON as of 2.2.24
- #4842: OpenApi doc not beeing generated properly
- #4682: BUG properties of type Object do not have any type information when generating open api 3.1.0
- ... and 1 more

**🟡 Medium Priority (16 issues):**
- #5027: &#34;required&#34; property in Schema.java should be a Set (SortedSet for example), not a List
- #4928: Lack of Friendly Support for Parameter Validation Grouping
- #4908: defaultValue attribute at enum object generate arbitrary json file
- #4886: Validation Meta annotations not working
- #4863: &#34;Override&#34; example value when extending a class
- #4851: The example value of the request body in swagger has a &#34;string&#34; value
- #4846: Swagger adds extra line breaks in yaml generated from annotated code
- #4844: swagger-annotations: Add annotations to support x-enum-descriptions and x-enum-varnames
- #4840: [Feature] Mark type as deprecated if the class is `@Deprecated`
- #4820: @Schema(example=&#34;1234&#34;, type=&#34;string&#34;) interpreted as integer
- ... and 6 more

**🟢 Low Priority (5 issues):**
- #5014: [Question]: How to specify @Schema.examples where each element is an array?
- #4984: [Question]: Duplicate records if Class and Operation both has @Path param
- #4966: [Feature]: Provide a bom module for managing dependencies
- #4939: Integer enum Schema parsing can fail based upon locale
- #3323: Generic Response Types

---

### Other
**Total:** 12 | **Avg Score:** 3.17 | Critical: 0 | High: 3 | Medium: 8 | Low: 1

**🟠 High Priority (3 issues):**
- #4976: [Feature]: Reduce Boilerplate in swagger-core Models (Optional Lombok Support or Alternatives)
- #4906: java.lang.Object converted to json object (map) type
- #3025: openapi.json fails to take into account servlet context path, produces bad spec file

**🟡 Medium Priority (8 issues):**
- #4924: Ability to remove unused definitions from generated YML
- #4843: OpenApi not generated when resourceClasses used
- #4837: Is it possible to convert from OAS 3.0 to OAS 3.1 using this lib?
- #4789: Classes / enums with same name but different package replace each other
- #4404: Add support for more java.time objects
- #3577: [bug] Integer enum support
- #2961: Field with first letter lower case not recognized
- #2192: Field name is getting generated wrongly if field type is &#34;Boolean&#34; (wrapper class for boolean) instead of primitive &#34;boolean&#34; type

**🟢 Low Priority (1 issues):**
- #4951: [Feature]: support for runtime skip properties inside ModelConverter

---


## Recommendations

### Based on Combined Priority Analysis:

1. **Immediate Focus (High Avg Score Areas):**
   - Address Critical issues in Validation, Jackson, and Dependencies areas
   - These areas have both high issue counts and high severity

2. **Strategic Priorities:**
   - **Validation:** 25 issues with 5 critical - needs architectural review
   - **Jakarta Migration:** 20 issues, many high priority - complete migration effort
   - **Annotation Processing:** 63 issues - highest volume, needs systematic cleanup

3. **Approach:**
   - Use the Combined Approach to select functional areas
   - Within each area, tackle Critical issues first, then High priority
   - Monitor average priority scores to track improvement

### How to Use This Analysis:

1. **For Planning:** Use the Combined Approach table to prioritize functional areas
2. **For Assignment:** Assign team members to top-priority functional areas
3. **For Sprints:** Pick top 2-3 areas, focus on Critical/High issues
4. **For Tracking:** Re-run analysis monthly to see if priorities shift

---

## Conclusion

The **Combined Approach** (Functional Areas by Priority) provides the best view for decision-making:

- **What to work on:** Functional areas with highest average priority scores
- **Within each area:** Start with Critical and High priority issues
- **Track progress:** Monitor both issue count and average priority score

**Current Focus Areas (based on combined analysis):**
1. Validation (3.64 avg) - 5 critical issues
2. Jackson (3.59 avg) - 2 critical issues  
3. Dependencies (3.57 avg) - 5 critical issues

All data is available in **ISSUES_DATA_2025_OPEN.csv** for further analysis.

