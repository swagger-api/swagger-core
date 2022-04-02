package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.core.resolving.v31.model.AnnotatedPet;
import io.swagger.v3.core.resolving.v31.model.AnnotatedPetSinglePatternProperty;
import io.swagger.v3.core.util.OpenAPISchema2JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;


import static org.testng.Assert.assertEquals;

public class PatternAndSchemaPropertiesTest extends SwaggerTestBase {

    @Test
    public void testPatternAndSchemaProperties() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(AnnotatedPet.class));

        assertEquals(((Schema)model.getPatternProperties().get("what.*ever")).getFormat(), "int32");
        assertEquals(((Schema)model.getPatternProperties().get("it.*takes")).get$ref(), "#/components/schemas/Category");

        assertEquals(((Schema)model.getProperties().get("anotherCategory")).get$ref(), "#/components/schemas/Category");
        assertEquals(((Schema)model.getProperties().get("anotherInteger")).getFormat(), "int32");

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "AnnotatedPet:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int64\n" +
                "    category:\n" +
                "      $ref: '#/components/schemas/Category'\n" +
                "    name:\n" +
                "      type: string\n" +
                "    photoUrls:\n" +
                "      type: array\n" +
                "      xml:\n" +
                "        wrapped: true\n" +
                "      items:\n" +
                "        type: string\n" +
                "        xml:\n" +
                "          name: photoUrl\n" +
                "    tags:\n" +
                "      type: array\n" +
                "      xml:\n" +
                "        wrapped: true\n" +
                "      items:\n" +
                "        $ref: '#/components/schemas/Tag'\n" +
                "    status:\n" +
                "      type: string\n" +
                "      description: pet status in the store\n" +
                "      enum:\n" +
                "      - available\n" +
                "      - pending\n" +
                "      - sold\n" +
                "    anotherCategory:\n" +
                "      $ref: '#/components/schemas/Category'\n" +
                "    anotherInteger:\n" +
                "      maximum: 10\n" +
                "      type: integer\n" +
                "      description: prop schema 1\n" +
                "      format: int32\n" +
                "  description: Annotated Pet\n" +
                "  nullable: true\n" +
                "Category:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int64\n" +
                "    name:\n" +
                "      type: string\n" +
                "  description: prop schema 2\n" +
                "  xml:\n" +
                "    name: Category\n" +
                "Tag:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int64\n" +
                "    name:\n" +
                "      type: string\n" +
                "  xml:\n" +
                "    name: Tag");
        context.getDefinedModels().values().forEach(s -> new OpenAPISchema2JsonSchema().process(s));

        SerializationMatchers.assertEqualsToYaml31(context.getDefinedModels(), "AnnotatedPet:\n" +
                "  type:\n" +
                "  - object\n" +
                "  - \"null\"\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int64\n" +
                "    category:\n" +
                "      $ref: '#/components/schemas/Category'\n" +
                "    name:\n" +
                "      type: string\n" +
                "    photoUrls:\n" +
                "      type: array\n" +
                "      xml:\n" +
                "        wrapped: true\n" +
                "      items:\n" +
                "        type: string\n" +
                "        xml:\n" +
                "          name: photoUrl\n" +
                "    tags:\n" +
                "      type: array\n" +
                "      xml:\n" +
                "        wrapped: true\n" +
                "      items:\n" +
                "        $ref: '#/components/schemas/Tag'\n" +
                "    status:\n" +
                "      type: string\n" +
                "      description: pet status in the store\n" +
                "      enum:\n" +
                "      - available\n" +
                "      - pending\n" +
                "      - sold\n" +
                "    anotherCategory:\n" +
                "      $ref: '#/components/schemas/Category'\n" +
                "    anotherInteger:\n" +
                "      maximum: 10\n" +
                "      type: integer\n" +
                "      description: prop schema 1\n" +
                "      format: int32\n" +
                "  patternProperties:\n" +
                "    what.*ever:\n" +
                "      maximum: 10\n" +
                "      type: integer\n" +
                "      description: prop schema 1\n" +
                "      format: int32\n" +
                "    it.*takes:\n" +
                "      $ref: '#/components/schemas/Category'\n" +
                "  description: Annotated Pet\n" +
                "Category:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int64\n" +
                "    name:\n" +
                "      type: string\n" +
                "  description: prop schema 2\n" +
                "  xml:\n" +
                "    name: Category\n" +
                "Tag:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: integer\n" +
                "      format: int64\n" +
                "    name:\n" +
                "      type: string\n" +
                "  xml:\n" +
                "    name: Tag\n");
    }

    @Test
    public void testSinglePatternAndSchemaProperties() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(AnnotatedPetSinglePatternProperty.class));

        assertEquals(((Schema)model.getPatternProperties().get("what.*ever")).getFormat(), "int32");
        assertEquals(((Schema)model.getProperties().get("anotherCategory")).get$ref(), "#/components/schemas/Category");
    }

}
