package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

public class RecursivePropertyMissingTest {

    static class WrapperDTO {
        @JsonProperty("nodes")
        @JsonPropertyDescription("Child nodes")
        public List<TestNodeDTO> nodes;
    }

    static class TestNodeDTO {
        @JsonProperty("name")
        public String name;

        @JsonProperty("nodes")
        @JsonPropertyDescription("Child nodes")
        public List<TestNodeDTO> nodes;
    }

    @Test
    public void testRecursivePropertyNotMissing() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(WrapperDTO.class);
        Schema testNodeDTOSchema = schemas.get("TestNodeDTO");
        assertNotNull(testNodeDTOSchema);
        
        Map<String, Schema> properties = testNodeDTOSchema.getProperties();
        assertNotNull(properties, "Properties should not be null");
        assertNotNull(properties.get("nodes"), "The 'nodes' property is missing from TestNodeDTO schema");
    }
}
