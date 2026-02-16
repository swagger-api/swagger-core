package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.core.resolving.v31.model.ModelWithArrayExamples;
import io.swagger.v3.core.resolving.v31.model.ModelWithInvalidJsonExamples;
import io.swagger.v3.core.resolving.v31.model.ModelWithExampleWorkarounds;
import io.swagger.v3.core.resolving.v31.model.ModelWithArraySchemaExamples;
import io.swagger.v3.core.resolving.v31.model.ApiResponseWithExamples;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.core.converter.ModelConverters;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Tests to illustrate issue #5014: Support for structured examples (arrays, objects) in OAS 3.1
 * 
 * Issue: When using @Schema.examples with JSON array or object strings in OAS 3.1,
 * swagger-core currently treats them as plain strings rather than parsing them as JSON.
 * 
 * Current behavior: examples = {"[\"one\",\"two\"]", "[\"three\",\"four\"]"} produces:
 *   examples:
 *     - "[\"one\",\"two\"]"
 *     - "[\"three\",\"four\"]"
 * 
 * Expected behavior for OAS 3.1: Should parse JSON and produce:
 *   examples:
 *     - ["one", "two"]
 *     - ["three", "four"]
 */
public class Core5014ExamplesTest extends SwaggerTestBase {

    /**
     * Tests the CURRENT behavior (issue): Array examples are emitted as strings.
     * This test documents what currently happens - examples remain as string literals.
     */
    @Test
    public void testArrayExamplesCurrentBehavior() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithArrayExamples.class));
        
        // Current behavior: JSON array strings are NOT parsed, they remain as strings
        SerializationMatchers.assertEqualsToYaml31(model,
                "type: object\n" +
                "properties:\n" +
                "  arrayWithMultipleExamples:\n" +
                "    type: array\n" +
                "    description: Array of strings with multiple array examples\n" +
                "    examples:\n" +
                "    - \"[\\\"one\\\",\\\"two\\\",\\\"three\\\"]\"\n" +  // Currently remains as string
                "    - \"[\\\"four\\\",\\\"five\\\",\\\"six\\\"]\"\n" +   // Currently remains as string
                "    items:\n" +
                "      type: string\n" +
                "  integerArrayWithExamples:\n" +
                "    type: array\n" +
                "    description: Array of integers with array examples\n" +
                "    examples:\n" +
                "    - \"[1,2,3]\"\n" +                        // Currently remains as string
                "    - \"[4,5,6]\"\n" +                        // Currently remains as string
                "    items:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "  simpleStringWithExamples:\n" +
                "    type: string\n" +
                "    description: Simple string field with scalar examples\n" +
                "    examples:\n" +
                "    - foo\n" +                              // Scalars work correctly
                "    - bar\n" +
                "  objectWithExamples:\n" +
                "    description: Object field with structured examples\n" +
                "    examples:\n" +
                "    - \"{\\\"name\\\":\\\"John\\\",\\\"age\\\":30}\"\n" + // Currently remains as string
                "    - \"{\\\"name\\\":\\\"Jane\\\",\\\"age\\\":25}\"\n" + // Currently remains as string
                "  nestedArrayWithExamples:\n" +
                "    type: array\n" +
                "    description: Array field with nested array examples (array-of-arrays)\n" +
                "    examples:\n" +
                "    - \"[[\\\"a\\\",\\\"b\\\"],[\\\"c\\\",\\\"d\\\"]]\"\n" +  // Currently remains as string
                "    - \"[[\\\"x\\\",\\\"y\\\"],[\\\"z\\\"]]\"\n" +        // Currently remains as string
                "    items:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string");
    }

    /**
     * Tests EXPECTED behavior for OAS 3.1 (currently FAILING - illustrates the issue).
     * When this test starts passing, issue #5014 is resolved.
     * 
     * This test is marked with expectedExceptions to show it currently fails,
     * demonstrating the need for JSON parsing in OAS 3.1 mode.
     */
    @Test(enabled = false, description = "Expected behavior for #5014 - currently not supported")
    public void testArrayExamplesExpectedBehavior() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithArrayExamples.class));
        
        // EXPECTED behavior for OAS 3.1: JSON strings should be parsed into structured values
        SerializationMatchers.assertEqualsToYaml31(model,
                "type: object\n" +
                "properties:\n" +
                "  arrayWithMultipleExamples:\n" +
                "    type: array\n" +
                "    description: Array of strings with multiple array examples\n" +
                "    examples:\n" +
                "    - [\"one\", \"two\", \"three\"]\n" +    // EXPECTED: parsed JSON array
                "    - [\"four\", \"five\", \"six\"]\n" +     // EXPECTED: parsed JSON array
                "    items:\n" +
                "      type: string\n" +
                "  integerArrayWithExamples:\n" +
                "    type: array\n" +
                "    description: Array of integers with array examples\n" +
                "    examples:\n" +
                "    - [1, 2, 3]\n" +                         // EXPECTED: parsed JSON array
                "    - [4, 5, 6]\n" +                         // EXPECTED: parsed JSON array
                "    items:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "  nestedArrayWithExamples:\n" +
                "    type: array\n" +
                "    description: Array field with nested array examples (array-of-arrays)\n" +
                "    examples:\n" +
                "    - [[\"a\", \"b\"], [\"c\", \"d\"]]\n" +  // EXPECTED: parsed nested array
                "    - [[\"x\", \"y\"], [\"z\"]]\n" +        // EXPECTED: parsed nested array
                "    items:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n" +
                "  objectWithExamples:\n" +
                "    description: Object field with structured examples\n" +
                "    examples:\n" +
                "    - {\"name\": \"John\", \"age\": 30}\n" + // EXPECTED: parsed JSON object
                "    - {\"name\": \"Jane\", \"age\": 25}\n" + // EXPECTED: parsed JSON object
                "  simpleStringWithExamples:\n" +
                "    type: string\n" +
                "    description: Simple string field with scalar examples\n" +
                "    examples:\n" +
                "    - foo\n" +                               // Scalars should remain unchanged
                "    - bar");                                  // Scalars should remain unchanged
    }

    /**
     * Tests that invalid JSON should gracefully fall back to string representation.
     * Even when JSON parsing is implemented, invalid JSON should not cause errors.
     */
    @Test
    public void testInvalidJsonExamplesCurrentBehavior() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithInvalidJsonExamples.class));
        
        // Invalid JSON should remain as strings (both current and expected behavior)
        SerializationMatchers.assertEqualsToYaml31(model,
                "type: object\n" +
                "properties:\n" +
                "  invalidJsonExamples:\n" +
                "    type: array\n" +
                "    description: Field with invalid JSON examples (should remain as strings)\n" +
                "    examples:\n" +
                "    - \"[invalid json\"\n" +                   // Invalid JSON stays as string
                "    - \"not a json array]\"\n" +               // Invalid JSON stays as string
                "    items:\n" +
                "      type: string\n" +
                "  mixedExamples:\n" +
                "    type: array\n" +
                "    description: Mixed valid and invalid JSON\n" +
                "    examples:\n" +
                "    - \"[\\\"valid\\\",\\\"json\\\"]\"\n" +            // Currently string, should parse when valid
                "    - \"invalid}\"\n" +                        // Invalid JSON stays as string
                "    items:\n" +
                "      type: string");
    }

    /**
     * Tests scalar examples - these should work correctly in both current and expected behavior.
     * Scalars don't need JSON parsing and should remain unchanged.
     */
    @Test
    public void testScalarExamplesWorkCorrectly() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithArrayExamples.class));
        
        // Scalar examples should work fine (no JSON parsing needed)
        // This partial check ensures scalar examples are handled correctly
        Schema stringProperty = (Schema) model.getProperties().get("simpleStringWithExamples");
        assert stringProperty != null;
        assert stringProperty.getExamples() != null;
        assert stringProperty.getExamples().size() == 2;
        assert stringProperty.getExamples().contains("foo");
        assert stringProperty.getExamples().contains("bar");
    }

    /**
     * WORKAROUND 1: Use single 'example' (not 'examples') with nested JSON string.
     * 
     * This is a WORKING workaround TODAY! The 'example' field automatically parses JSON:
     * - When you provide a JSON string in @Schema.example, it gets parsed
     * - Swagger UI and the spec show it as structured data, not a string
     * - Works for both OAS 3.0 and 3.1
     * 
     * Trade-off: You only get ONE example instead of multiple distinct examples.
     * 
     * This test PROVES the workaround works!
     */
    @Test
    public void testWorkaround1_SingleExampleWithNestedJson() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithExampleWorkarounds.class));
        
        // The workaround WORKS! 'example' field parses JSON automatically
        SerializationMatchers.assertEqualsToYaml31(model,
                "type: object\n" +
                "properties:\n" +
                "  singleExampleWorkaround:\n" +
                "    type: array\n" +
                "    description: \"Workaround 1: Single example with array-of-arrays\"\n" +
                "    example:\n" +
                "    - - one\n" +              // ✅ PARSED! Not a string!
                "      - two\n" +
                "      - three\n" +
                "    - - four\n" +
                "      - five\n" +
                "      - six\n" +
                "    items:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n" +
                "  singleArrayExample:\n" +
                "    type: array\n" +
                "    description: \"Workaround 1b: Single example as JSON array\"\n" +
                "    example:\n" +
                "    - red\n" +                // ✅ PARSED! Not a string!
                "    - green\n" +
                "    - blue\n" +
                "    items:\n" +
                "      type: string\n" +
                "  multipleExamplesNotWorking:\n" +
                "    type: array\n" +
                "    description: \"What we want (doesn't work yet): Multiple array examples\"\n" +
                "    examples:\n" +
                "    - \"[\\\"one\\\",\\\"two\\\",\\\"three\\\"]\"\n" +  // ❌ NOT parsed (the issue!)
                "    - \"[\\\"four\\\",\\\"five\\\",\\\"six\\\"]\"\n" +
                "    items:\n" +
                "      type: string");
        
        // Verify the workaround: 'example' field contains parsed structure, not a string
        Schema workaroundProperty = (Schema) model.getProperties().get("singleExampleWorkaround");
        assertNotNull(workaroundProperty.getExample(), "Workaround uses 'example' field");
        // The example is parsed as a Jackson ArrayNode, not left as a string!
        assertTrue(workaroundProperty.getExample() instanceof com.fasterxml.jackson.databind.node.ArrayNode, 
                "Example is parsed as ArrayNode (structured JSON), not a string!");
    }

    /**
     * WORKAROUND 1 - Comparison test to show the KEY DIFFERENCE.
     * 
     * This test directly compares and PROVES:
     * - example (singular) - Automatically parses JSON! ✅ WORKING WORKAROUND
     * - examples (plural) - Does NOT parse JSON ❌ THE ISSUE
     */
    @Test
    public void testWorkaround1_CompareExampleVsExamples() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithExampleWorkarounds.class));
        
        // ✅ WORKAROUND SUCCESS: Single 'example' field PARSES the JSON string!
        Schema singleExample = (Schema) model.getProperties().get("singleArrayExample");
        assertNotNull(singleExample.getExample());
        // This is the KEY: example is parsed as Jackson ArrayNode, not kept as a string!
        assertTrue(singleExample.getExample() instanceof com.fasterxml.jackson.databind.node.ArrayNode, 
                "WORKAROUND WORKS: 'example' is parsed as ArrayNode (structured JSON)!");
        com.fasterxml.jackson.databind.node.ArrayNode exampleArray = 
                (com.fasterxml.jackson.databind.node.ArrayNode) singleExample.getExample();
        assertEquals(exampleArray.size(), 3);
        assertEquals(exampleArray.get(0).asText(), "red");
        assertEquals(exampleArray.get(1).asText(), "green");
        assertEquals(exampleArray.get(2).asText(), "blue");
        assertNull(singleExample.getExamples(), "Single example doesn't use 'examples' array");
        
        // ❌ THE ISSUE: Multiple 'examples' are NOT parsed, remain as strings
        Schema multipleExamples = (Schema) model.getProperties().get("multipleExamplesNotWorking");
        assertNull(multipleExamples.getExample(), "Multiple examples don't use 'example' field");
        assertNotNull(multipleExamples.getExamples());
        assertEquals(multipleExamples.getExamples().size(), 2);
        // The examples are STRINGS, not parsed arrays (this is the bug!):
        assertTrue(multipleExamples.getExamples().get(0) instanceof String,
                "THE ISSUE: 'examples' array contains strings, not parsed JSON!");
        assertEquals(multipleExamples.getExamples().get(0), "[\"one\",\"two\",\"three\"]");
        assertEquals(multipleExamples.getExamples().get(1), "[\"four\",\"five\",\"six\"]");
    }

    /**
     * Documentation test: Explains when to use each workaround.
     * This test always passes - it's for documentation purposes.
     */
    @Test
    public void testWorkaroundGuidance() {
        // This test documents the workarounds and when to use them
        
        // WORKAROUND 1: Use single 'example' with nested JSON
        // ✅ WHEN: You can live with one example showing the pattern
        // ✅ WHEN: You need it to work today without code changes
        // ❌ WHEN: You need multiple distinct examples at schema level
        
        String workaround1 = 
                "@Schema(\n" +
                "    example = \"[\\\"one\\\",\\\"two\\\",\\\"three\\\"]\"\n" +
                ")\n" +
                "private List<String> myArray;";
        
        // WORKAROUND 2: Use @Content.examples with @ExampleObject at operation level
        // ✅ WHEN: Examples are in a request/response body
        // ✅ WHEN: You want multiple named examples in Swagger UI
        // ✅ WHEN: You want full JSON structure with proper formatting
        // ❌ WHEN: You need examples at the schema/property level itself
        
        String workaround2 =
                "@ApiResponse(\n" +
                "    content = @Content(\n" +
                "        examples = {\n" +
                "            @ExampleObject(name=\"ex1\", value=\"{\\\"data\\\": [\\\"a\\\",\\\"b\\\"]}\"),\n" +
                "            @ExampleObject(name=\"ex2\", value=\"{\\\"data\\\": [\\\"x\\\",\\\"y\\\"]}\")\n" +
                "        }\n" +
                "    )\n" +
                ")";
        
        // THE DESIRED FEATURE (not yet working):
        String desired =
                "@Schema(\n" +
                "    examples = {\"[\\\"one\\\",\\\"two\\\"]\", \"[\\\"three\\\",\\\"four\\\"]\"}\n" +
                ")\n" +
                "// Should generate: examples: [['one','two'], ['three','four']]";
        
        // Test passes - this is documentation
        assertNotNull(workaround1);
        assertNotNull(workaround2);
        assertNotNull(desired);
    }

    /**
     * WORKAROUND 2 - Actual test demonstrating media-type examples with @ExampleObject.
     * 
     * This shows how to provide multiple examples at the operation/response level,
     * which is a valid alternative to schema-level examples.
     * 
     * This workaround provides:
     * - Multiple named examples (appear in Swagger UI dropdown)
     * - Full JSON structure with proper array formatting
     * - Each example can have name, summary, description, value
     */
    @Test
    public void testWorkaround2_MediaTypeExamples() {
        // Create an ApiResponse with multiple examples at content/media-type level
        io.swagger.v3.oas.models.responses.ApiResponse response = 
                new io.swagger.v3.oas.models.responses.ApiResponse()
                .description("Successful response with multiple array examples")
                .content(new io.swagger.v3.oas.models.media.Content()
                    .addMediaType("application/json", new io.swagger.v3.oas.models.media.MediaType()
                        .schema(new io.swagger.v3.oas.models.media.Schema<>()
                            .type("object")
                            .addProperty("triple", new io.swagger.v3.oas.models.media.Schema<>()
                                .type("array")
                                .items(new io.swagger.v3.oas.models.media.Schema<>().type("string"))))
                        .addExamples("example1", new io.swagger.v3.oas.models.examples.Example()
                            .summary("Primary colors")
                            .description("An example with three primary colors")
                            .value("{\"triple\": [\"red\", \"blue\", \"yellow\"]}"))
                        .addExamples("example2", new io.swagger.v3.oas.models.examples.Example()
                            .summary("Secondary colors")
                            .description("An example with three secondary colors")
                            .value("{\"triple\": [\"green\", \"orange\", \"purple\"]}"))
                        .addExamples("example3", new io.swagger.v3.oas.models.examples.Example()
                            .summary("Number words")
                            .description("An example with number words")
                            .value("{\"triple\": [\"one\", \"two\", \"three\"]}"))
                    )
                );
        
        // Verify the structure
        assertNotNull(response.getContent());
        MediaType mediaType = response.getContent().get("application/json");
        assertNotNull(mediaType);
        assertNotNull(mediaType.getExamples());
        assertEquals(mediaType.getExamples().size(), 3, "Should have 3 examples");
        
        // Verify each example has the expected structure
        assertTrue(mediaType.getExamples().containsKey("example1"));
        assertTrue(mediaType.getExamples().containsKey("example2"));
        assertTrue(mediaType.getExamples().containsKey("example3"));
        
        io.swagger.v3.oas.models.examples.Example ex1 = mediaType.getExamples().get("example1");
        assertEquals(ex1.getSummary(), "Primary colors");
        assertEquals(ex1.getValue(), "{\"triple\": [\"red\", \"blue\", \"yellow\"]}");
        
        // The key difference: These examples are at OPERATION level, not SCHEMA level
        // But they provide multiple examples with full JSON structure
        // This is what workaround #2 gives you
        
        // Verify YAML serialization
        SerializationMatchers.assertEqualsToYaml31(response,
                "description: Successful response with multiple array examples\n" +
                "content:\n" +
                "  application/json:\n" +
                "    schema:\n" +
                "      properties:\n" +
                "        triple:\n" +
                "          items: {}\n" +
                "    examples:\n" +
                "      example1:\n" +
                "        summary: Primary colors\n" +
                "        description: An example with three primary colors\n" +
                "        value: \"{\\\"triple\\\": [\\\"red\\\", \\\"blue\\\", \\\"yellow\\\"]}\"\n" +
                "      example2:\n" +
                "        summary: Secondary colors\n" +
                "        description: An example with three secondary colors\n" +
                "        value: \"{\\\"triple\\\": [\\\"green\\\", \\\"orange\\\", \\\"purple\\\"]}\"\n" +
                "      example3:\n" +
                "        summary: Number words\n" +
                "        description: An example with number words\n" +
                "        value: \"{\\\"triple\\\": [\\\"one\\\", \\\"two\\\", \\\"three\\\"]}\"");
    }

    /**
     * Tests @ArraySchema with arraySchema containing examples.
     * 
     * This demonstrates the combination of:
     * - Issue #5012: arraySchema examples should appear (fixed in PR #5049)
     * - Issue #5014: examples should be parsed as JSON (NOT YET FIXED)
     * 
     * After PR #5049, examples in arraySchema DO appear in the output.
     * But they're still emitted as strings, not parsed arrays (issue #5014).
     */
    @Test
    public void testArraySchemaWithExamples() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        Schema model = context.resolve(new AnnotatedType(ModelWithArraySchemaExamples.class));
        
        // After PR #5049: examples from arraySchema DO appear!
        // But they're still strings, not parsed JSON (issue #5014)
        SerializationMatchers.assertEqualsToYaml31(model,
                "type: object\n" +
                "properties:\n" +
                "  colorsWithSingleExample:\n" +
                "    type: array\n" +
                "    description: Array of color names\n" +
                // Note: 'example' (singular) from arraySchema is NOT appearing
                // This seems to be a separate limitation
                "    items:\n" +
                "      type: string\n" +
                "      description: Individual color name\n" +
                "  tripleWithMultipleExamples:\n" +
                "    type: array\n" +
                "    description: Triple of words\n" +
                "    examples:\n" +
                "    - \"[\\\"one\\\", \\\"two\\\", \\\"three\\\"]\"\n" +   // ❌ String (issue #5014)
                "    - \"[\\\"four\\\", \\\"five\\\", \\\"six\\\"]\"\n" +    // ❌ String (issue #5014)
                "    - \"[\\\"red\\\", \\\"blue\\\", \\\"yellow\\\"]\"\n" +  // ❌ String (issue #5014)
                "    items:\n" +
                "      type: string\n" +
                "      description: Individual word\n" +
                "  directSchemaWithExamples:\n" +
                "    type: array\n" +
                "    description: Direct schema on field\n" +
                "    examples:\n" +
                "    - \"[\\\"alpha\\\", \\\"beta\\\", \\\"gamma\\\"]\"\n" +  // ❌ String (issue #5014)
                "    - \"[\\\"delta\\\", \\\"epsilon\\\", \\\"zeta\\\"]\"\n" +  // ❌ String (issue #5014)
                "    items:\n" +
                "      type: string");
        
        // Verify that examples ARE present (thanks to PR #5049)
        Schema tripleField = (Schema) model.getProperties().get("tripleWithMultipleExamples");
        assertNotNull(tripleField.getExamples(), "Examples from arraySchema.examples should be present");
        assertEquals(tripleField.getExamples().size(), 3);
        
        // But they're strings, not parsed (the issue #5014)
        assertTrue(tripleField.getExamples().get(0) instanceof String,
                "Examples are still strings (issue #5014), not parsed JSON");
        assertEquals(tripleField.getExamples().get(0), "[\"one\", \"two\", \"three\"]");
        
        // Compare with direct @Schema - same issue
        Schema directField = (Schema) model.getProperties().get("directSchemaWithExamples");
        assertNotNull(directField.getExamples());
        assertTrue(directField.getExamples().get(0) instanceof String,
                "@Schema.examples also not parsed (same issue)");
    }
}
