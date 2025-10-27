package io.swagger.v3.jaxrs2;


import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class Ticket4993Test {

    @Schema(description = "User resource")
    static class User {
        private List<Thing> things;

        @ArraySchema(
                arraySchema = @Schema(
                        description = "List of Things assigned to the user",
                        requiredMode = RequiredMode.REQUIRED
                ),
                schema = @Schema(implementation = Thing.ThingAssignment.class)
        )
        public List<Thing> getThings() {
            return things;
        }
    }

    @Schema(description = "Thing resource")
    static class Thing {
        @Schema(description = "Thing to be assigned to user")
        static class ThingAssignment extends Thing {
        }
    }

    @Schema(description = "User resource (no override)")
    static class UserNoOverride {
        private List<Thing> things;

        @ArraySchema(
                arraySchema = @Schema(description = "List of Things assigned to the user",
                        requiredMode = RequiredMode.REQUIRED)
        )
        public List<Thing> getThings() {
            return things;
        }
    }

    @Test
    void arrayItems_useOverrideClass_andOverrideIsPresentInComponents_oas30() {
        ResolvedSchema rs = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(User.class).resolveAsRef(true));

        assertNotNull(rs.schema, "User schema should be resolved");

        @SuppressWarnings("rawtypes")
        Map<String, io.swagger.v3.oas.models.media.Schema> refs = rs.referencedSchemas;
        assertNotNull(refs, "referencedSchemas map must be present");
        assertTrue(refs.containsKey("User"), "User schema should be in referencedSchemas");

        io.swagger.v3.oas.models.media.Schema<?> userSchema = refs.get("User");

        io.swagger.v3.oas.models.media.Schema<?> thingsProp =
            (io.swagger.v3.oas.models.media.Schema<?>) userSchema.getProperties().get("things");
        assertNotNull(thingsProp, "'things' property should exist on User");
        io.swagger.v3.oas.models.media.Schema<?> items = thingsProp.getItems();
        assertNotNull(items, "Array items must be present");
        assertEquals(items.get$ref(), "#/components/schemas/ThingAssignment",
                "Items $ref must point to the override class (ThingAssignment)");

        assertTrue(refs.containsKey("ThingAssignment"),
                "ThingAssignment must be included in components/schemas");
    }

    @Test
    void arrayItems_useOverrideClass_andOverrideIsPresentInComponents_oas31() {
        ResolvedSchema rs = ModelConverters.getInstance(true)
                .resolveAsResolvedSchema(new AnnotatedType(User.class).resolveAsRef(true));

        assertNotNull(rs.schema, "User schema should be resolved");

        @SuppressWarnings("rawtypes")
        Map<String, io.swagger.v3.oas.models.media.Schema> refs = rs.referencedSchemas;
        assertNotNull(refs, "referencedSchemas map must be present");
        assertTrue(refs.containsKey("User"), "User schema should be in referencedSchemas");

        io.swagger.v3.oas.models.media.Schema<?> userSchema = refs.get("User");

        io.swagger.v3.oas.models.media.Schema<?> thingsProp =
            (io.swagger.v3.oas.models.media.Schema<?>) userSchema.getProperties().get("things");
        assertNotNull(thingsProp, "'things' property should exist on User");

        io.swagger.v3.oas.models.media.Schema<?> items = thingsProp.getItems();
        assertNotNull(items, "Array items must be present");
        assertEquals(items.get$ref(), "#/components/schemas/ThingAssignment",
                "Items $ref must point to the override class (ThingAssignment)");

        assertTrue(refs.containsKey("ThingAssignment"),
                "ThingAssignment must be included in components/schemas");
    }

    @Test
    void arrayItems_withoutOverride_fallBackToDeclaredGenericType_oas30() {
        ResolvedSchema rs = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(UserNoOverride.class).resolveAsRef(true));

        // then
        @SuppressWarnings("rawtypes")
        Map<String, io.swagger.v3.oas.models.media.Schema> refs = rs.referencedSchemas;
        assertNotNull(refs, "referencedSchemas map must be present");
        assertTrue(refs.containsKey("UserNoOverride"), "UserNoOverride schema should be in referencedSchemas");

        io.swagger.v3.oas.models.media.Schema<?> userSchema = refs.get("UserNoOverride");

        io.swagger.v3.oas.models.media.Schema<?> thingsProp =
            (io.swagger.v3.oas.models.media.Schema<?>) userSchema.getProperties().get("things");

        io.swagger.v3.oas.models.media.Schema<?> items = thingsProp.getItems();
        assertEquals(items.get$ref(), "#/components/schemas/Thing",
                "Without override, items $ref should resolve to the declared generic type (Thing)");
        assertTrue(rs.referencedSchemas.containsKey("Thing"),
                "'Thing' should be included in components/schemas");
    }

    @Test
    void arrayItems_withoutOverride_fallBackToDeclaredGenericType_oas31() {
        // when
        ResolvedSchema rs = ModelConverters.getInstance(true)
                .resolveAsResolvedSchema(new AnnotatedType(UserNoOverride.class).resolveAsRef(true));

        // then
        @SuppressWarnings("rawtypes")
        Map<String, io.swagger.v3.oas.models.media.Schema> refs = rs.referencedSchemas;
        assertNotNull(refs, "referencedSchemas map must be present");
        assertTrue(refs.containsKey("UserNoOverride"), "UserNoOverride schema should be in referencedSchemas");

        io.swagger.v3.oas.models.media.Schema<?> userSchema = refs.get("UserNoOverride");

        io.swagger.v3.oas.models.media.Schema<?> thingsProp =
            (io.swagger.v3.oas.models.media.Schema<?>) userSchema.getProperties().get("things");

        io.swagger.v3.oas.models.media.Schema<?> items = thingsProp.getItems();
        assertEquals(items.get$ref(), "#/components/schemas/Thing",
                "Without override, items $ref should resolve to the declared generic type (Thing)");
        assertTrue(rs.referencedSchemas.containsKey("Thing"),
                "'Thing' should be included in components/schemas");
    }
}
