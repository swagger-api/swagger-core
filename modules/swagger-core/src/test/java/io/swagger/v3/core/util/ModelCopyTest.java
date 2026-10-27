package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

public class ModelCopyTest {

    @Test
    public void copiesWhatJsonRoundTripLoses() {
        JsonSchema person = new JsonSchema();
        person.specVersion(SpecVersion.V31);
        person.name("Person");
        person.types(Set.of("object", "null"));
        person.$id("urn:person");
        StringSchema name = new StringSchema();
        name.example(null);
        person.addProperty("name", name);
        person.addProperty("tags", new ArraySchema().items(new StringSchema()));
        Schema<Object> amount = new Schema<>();
        amount.example(new BigDecimal("1.50"));
        amount.setDefault(LocalDate.of(2026, 10, 24));
        person.addProperty("amount", amount);
        ObjectNode node = JsonNodeFactory.instance.objectNode().put("k", "v");
        Map<String, Object> extension = new LinkedHashMap<>();
        extension.put("nullValue", null);
        extension.put("node", node);
        person.addExtension("x-ext", extension);
        OpenAPI source = new OpenAPI(SpecVersion.V31).paths(new Paths())
                .components(new Components().addSchemas("Person", person));

        OpenAPI copy = ModelCopy.deepCopy(source);

        assertEquals(copy, source);
        Schema<?> copied = copy.getComponents().getSchemas().get("Person");
        assertNotSame(copied, person);
        assertSame(copied.getClass(), JsonSchema.class);
        assertSame(copied.getProperties().get("name").getClass(), StringSchema.class);
        assertSame(copied.getProperties().get("tags").getClass(), ArraySchema.class);
        assertEquals(copied.getName(), "Person");
        assertEquals(copied.getSpecVersion(), SpecVersion.V31);
        assertEquals(copied.getTypes(), Set.of("object", "null"));
        assertTrue(((Schema<?>) copied.getProperties().get("name")).getExampleSetFlag());
        assertEquals(((Schema<?>) copied.getProperties().get("amount")).getExample(), new BigDecimal("1.50"));
        assertEquals(((Schema<?>) copied.getProperties().get("amount")).getDefault(), LocalDate.of(2026, 10, 24));
        Map<?, ?> copiedExtension = (Map<?, ?>) copied.getExtensions().get("x-ext");
        assertTrue(copiedExtension.containsKey("nullValue"));
        assertEquals(copiedExtension.get("node"), node);
        assertNotSame(copiedExtension.get("node"), node);
        assertSame(copy.getPaths().getClass(), Paths.class);

        copied.getProperties().put("added", new Schema<>());
        ((ObjectNode) copiedExtension.get("node")).put("k", "changed");
        assertFalse(person.getProperties().containsKey("added"));
        assertEquals(node.get("k").asString(), "v");
    }

    @Test
    public void preservesSharedInstances() {
        StringSchema shared = new StringSchema();
        Schema<?> schema = new Schema<>().addProperty("a", shared).addProperty("b", shared);

        Schema<?> copy = ModelCopy.deepCopy(schema);

        assertSame(copy.getProperties().get("a"), copy.getProperties().get("b"));
        assertNotSame(copy.getProperties().get("a"), shared);
        assertNull(ModelCopy.deepCopy(null));
        assertEquals(ModelCopy.deepCopy(List.of(shared)), List.of(shared));
    }
}
