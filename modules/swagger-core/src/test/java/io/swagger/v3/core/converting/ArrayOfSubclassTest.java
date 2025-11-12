package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.oas.models.ModelWithArrayOfSubclasses;
import io.swagger.v3.core.util.Json31;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


public class ArrayOfSubclassTest {

    @Test
    public void extractSubclassArray_oas31() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(ModelWithArrayOfSubclasses.Holder.class);
        assertNotNull(schema);
        String expectedJson = new String(Files.readAllBytes(Paths.get("src/test/java/io/swagger/v3/core/converting/ArrayOfSubclassTest_expected31.json")));
        String actualJson = Json31.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    @Test
    public void extractSubclassArray_oas30() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(false).readAllAsResolvedSchema(ModelWithArrayOfSubclasses.Holder.class);
        assertNotNull(schema);
        String expectedJson = new String(Files.readAllBytes(Paths.get("src/test/java/io/swagger/v3/core/converting/ArrayOfSubclassTest_expected30.json")));
        String actualJson = Json31.pretty(schema);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }
}