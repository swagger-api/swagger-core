package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Reproduces GitHub Issue #4341
 * ArraySchema.arraySchema.requiredMode should control array requirement
 * 
 * Problem: After replacing 'required' with 'requiredMode', ArraySchema.arraySchema.requiredMode doesn't work
 * Expected: Field marked as required in arraySchema should appear in required array
 * Actual: Required mode on arraySchema is ignored; must use schema.requiredMode as workaround
 * 
 * @see https://github.com/swagger-api/swagger-core/issues/4341
 */
public class Issue4341Test {
    
    // Test model - using ArraySchema with requiredMode on arraySchema
    static class PersonWithArraySchemaRequired {
        @ArraySchema(
            arraySchema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        )
        private List<String> addresses;

        public List<String> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<String> addresses) {
            this.addresses = addresses;
        }
    }
    
    // Workaround model - using requiredMode on schema instead
    static class PersonWithSchemaRequired {
        @ArraySchema(
            arraySchema = @Schema(description = "The person's addresses"),
            schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        )
        private List<String> addresses;

        public List<String> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<String> addresses) {
            this.addresses = addresses;
        }
    }

    @Test(description = "Reproduces issue #4341: ArraySchema.arraySchema.requiredMode should mark field as required")
    public void testArraySchemaRequiredMode() {
        Map<String, io.swagger.v3.oas.models.media.Schema> schemas = ModelConverters.getInstance()
            .readAll(PersonWithArraySchemaRequired.class);
        
        assertNotNull(schemas);
        io.swagger.v3.oas.models.media.Schema personSchema = schemas.get("PersonWithArraySchemaRequired");
        assertNotNull(personSchema, "Schema should be generated");
        assertTrue(personSchema instanceof ObjectSchema);
        
        ObjectSchema objectSchema = (ObjectSchema) personSchema;
        List<String> required = objectSchema.getRequired();
        
        // This is the bug - required should contain "addresses" but it doesn't
        assertNotNull(required, "Required array should not be null when arraySchema has requiredMode=REQUIRED");
        assertTrue(required != null && required.contains("addresses"), 
            "Field 'addresses' should be in required array when arraySchema has requiredMode=REQUIRED");
    }
    
    @Test(description = "Workaround: Using schema.requiredMode works correctly")
    public void testWorkaround() {
        Map<String, io.swagger.v3.oas.models.media.Schema> schemas = ModelConverters.getInstance()
            .readAll(PersonWithSchemaRequired.class);
        
        assertNotNull(schemas);
        io.swagger.v3.oas.models.media.Schema personSchema = schemas.get("PersonWithSchemaRequired");
        assertNotNull(personSchema, "Schema should be generated");
        assertTrue(personSchema instanceof ObjectSchema);
        
        ObjectSchema objectSchema = (ObjectSchema) personSchema;
        List<String> required = objectSchema.getRequired();
        
        // Workaround works - schema.requiredMode is respected
        assertNotNull(required, "Required array should exist");
        assertTrue(required.contains("addresses"), 
            "Workaround: Field 'addresses' should be in required array when schema has requiredMode=REQUIRED");
    }
}
