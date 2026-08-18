package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Tests for issue #4928 - Validation Grouping support in @Schema.
 * Verifies that groups() in @Schema annotation are correctly resolved
 * into the Schema model object's groups field.
 */
public class ValidationGroupTest {

    // --- Marker interfaces representing validation groups ---
    interface Create {}
    interface Update {}

    // --- Test DTO class ---
    static class UserDto {

        @Schema(description = "Primary Key ID", groups = { Update.class })
        public Integer id;

        @Schema(description = "Full name", groups = { Create.class, Update.class })
        public String name;

        @Schema(description = "Email address")   // no groups -> should remain null
        public String email;
    }

    @Test(description = "groups() on @Schema with a single group should be resolved into the model")
    public void testSchemaGroupsSingleGroup() {
        final Map<String, io.swagger.v3.oas.models.media.Schema> schemas =
                ModelConverters.getInstance().readAll(UserDto.class);
        final io.swagger.v3.oas.models.media.Schema model = schemas.get("UserDto");
        assertNotNull(model, "UserDto schema must be resolved");

        final Map<String, io.swagger.v3.oas.models.media.Schema> properties = model.getProperties();
        assertNotNull(properties, "properties must not be null");

        final io.swagger.v3.oas.models.media.Schema idSchema = properties.get("id");
        assertNotNull(idSchema, "id property must exist");
        assertNotNull(idSchema.getGroups(), "id groups must not be null");
        assertEquals(idSchema.getGroups().size(), 1);
        assertTrue(idSchema.getGroups().contains("Update"), "id should belong to Update group");
    }

    @Test(description = "groups() on @Schema with multiple groups should all be resolved")
    public void testSchemaGroupsMultipleGroups() {
        final Map<String, io.swagger.v3.oas.models.media.Schema> schemas =
                ModelConverters.getInstance().readAll(UserDto.class);
        final io.swagger.v3.oas.models.media.Schema model = schemas.get("UserDto");
        final io.swagger.v3.oas.models.media.Schema nameSchema = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("name");

        assertNotNull(nameSchema, "name property must exist");
        assertNotNull(nameSchema.getGroups(), "name groups must not be null");

        final List<String> groups = nameSchema.getGroups();
        assertEquals(groups.size(), 2, "name should have exactly 2 groups");
        assertTrue(groups.contains("Create"), "name should belong to Create group");
        assertTrue(groups.contains("Update"), "name should belong to Update group");
    }

    @Test(description = "No groups() on @Schema should leave groups field as null")
    public void testSchemaNoGroupsRemainsNull() {
        final Map<String, io.swagger.v3.oas.models.media.Schema> schemas =
                ModelConverters.getInstance().readAll(UserDto.class);
        final io.swagger.v3.oas.models.media.Schema model = schemas.get("UserDto");
        final io.swagger.v3.oas.models.media.Schema emailSchema = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("email");

        assertNotNull(emailSchema, "email property must exist");
        assertNull(emailSchema.getGroups(),
                "email groups should be null when no groups() is specified on @Schema");
    }
}
