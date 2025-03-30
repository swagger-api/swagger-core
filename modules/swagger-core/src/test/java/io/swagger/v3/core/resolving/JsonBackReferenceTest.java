package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class JsonBackReferenceTest {

    @Test(description = "it should ignore a reference field")
    public void testReferenceField() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(Parent.class);

        final Schema parent = models.get("Parent");
        assertNotNull(parent);
        assertEquals(parent.getProperties().size(), 1);

        final Schema child = models.get("Child");
        assertNotNull(child);
        assertNull(child.getProperties());
    }

    static class Parent {

        public List<Child> children = null;

    }

    static class Child {

        @JsonBackReference
        public Parent parent = null;

    }

}
