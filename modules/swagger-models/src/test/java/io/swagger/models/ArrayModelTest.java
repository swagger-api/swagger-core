package io.swagger.models;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.testng.Assert.assertEquals;

public class ArrayModelTest extends PowerMockTestCase {

    private ArrayModel instance;

    @BeforeMethod
    public void setUp() throws Exception {
        instance = new ArrayModel();
    }

    @Test
    public void testClone() {
        // given
        instance.setProperties(new HashMap<String, Property>());
        instance.setType("type");
        instance.setDescription("description");
        instance.setItems(new StringProperty());
        instance.setExample(new Object());

        // when
        ArrayModel cloned = (ArrayModel) instance.clone();

        // then
        assertEquals(instance.getProperties(), cloned.getProperties(),
                "The instance and the clone must have the same properties value");
        assertEquals(instance.getType(), cloned.getType(), "The instance and the clone must have the same type value");
        assertEquals(instance.getDescription(), cloned.getDescription(),
                "The instance and the clone must have the same description value");
        assertEquals(instance.getExample(), cloned.getExample(),
                "The instance and the clone must have the same example value");
    }

    @Test
    public void testDescription() {
        // given
        String description = "description";

        // when
        instance.description(description);

        // then
        assertEquals(description, instance.getDescription(), "The got description must be the same as the set one");
    }

    @Test
    public void testItems() {
        // given
        Property items = new ArrayProperty();

        // when
        instance.items(items);

        // then
        assertEquals(items, instance.getItems(), "The got items must be the same as the set one");
    }
}
