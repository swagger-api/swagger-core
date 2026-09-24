package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class SealedClassOneOfTest {

    @Test
    public void sealedParentWithSchemaOneOfDoesNotAddAllOfToSubtypes() {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema<?> parent =
                context.resolve(new AnnotatedType(SealedParent.class));
        assertNotNull(parent);

        // The @Schema(oneOf = ...) is honored on the parent.
        assertTrue(parent.getOneOf() != null && parent.getOneOf().size() == 2,
                "sealed parent should expose the configured oneOf");

        // Jackson 3 auto-detects sealed subtypes; without the fix resolveSubtypes()
        // adds an unwanted allOf->parent ref to each child. The fix must prevent that.
        io.swagger.v3.oas.models.media.Schema<?> child1 =
                context.getDefinedModels().get("SealedChild1");
        io.swagger.v3.oas.models.media.Schema<?> child2 =
                context.getDefinedModels().get("SealedChild2");
        assertNotNull(child1, "SealedChild1 model should exist");
        assertNotNull(child2, "SealedChild2 model should exist");

        assertNull(child1.getAllOf(), "SealedChild1 must not carry an unwanted allOf ref to the parent");
        assertNull(child2.getAllOf(), "SealedChild2 must not carry an unwanted allOf ref to the parent");
    }

    @Schema(oneOf = {SealedChild1.class, SealedChild2.class})
    static sealed class SealedParent permits SealedChild1, SealedChild2 {
        public String type;
    }

    static final class SealedChild1 extends SealedParent {
    }

    static final class SealedChild2 extends SealedParent {
    }
}