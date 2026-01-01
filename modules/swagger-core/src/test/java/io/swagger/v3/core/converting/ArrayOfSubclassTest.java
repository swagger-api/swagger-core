package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.oas.models.ModelWithArrayOfSubclasses;
import io.swagger.v3.core.util.Json31;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;


public class ArrayOfSubclassTest {

    @Test
    public void extractSubclassArray_oas31() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(ModelWithArrayOfSubclasses.Holder.class);
        assertNotNull(schema);
        String expectedJson = new String(Files.readAllBytes(Path.of("src/test/java/io/swagger/v3/core/converting/ArrayOfSubclassTest_expected31.json")));
        String actualJson = Json31.pretty(schema);
        ObjectMapper mapper = JsonMapper.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }

    @Test
    public void extractSubclassArray_oas30() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(false).readAllAsResolvedSchema(ModelWithArrayOfSubclasses.Holder.class);
        assertNotNull(schema);
        String expectedJson = new String(Files.readAllBytes(Path.of("src/test/java/io/swagger/v3/core/converting/ArrayOfSubclassTest_expected30.json")));
        String actualJson = Json31.pretty(schema);
        ObjectMapper mapper = JsonMapper.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(actualNode, expectedNode);
    }
}