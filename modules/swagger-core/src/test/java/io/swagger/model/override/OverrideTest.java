package io.swagger.model.override;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;

import org.testng.annotations.Test;

import java.util.Map;

public class OverrideTest {
    private static final String NAME = "name";
    private static final String COUNT = "count";

    @Test
    public void test() {
        GenericModel.declareProperty(NAME, String.class);
        GenericModel.declareProperty(COUNT, int.class);

        // create new instead of use singleton
        final ModelConverters converters = new ModelConverters();
        converters.addConverter(new GericModelConverter());
        final Map<String, Model> read = converters.read(GenericModel.class);
        assertTrue(read.containsKey(GenericModel.class.getSimpleName()));

        final Model model = read.get(GenericModel.class.getSimpleName());
        assertTrue(model.getProperties().containsKey(NAME));
        assertEquals(model.getProperties().get(NAME).getType(), "string");
        assertTrue(model.getProperties().containsKey(COUNT));
        assertEquals(model.getProperties().get(COUNT).getType(), "integer");
    }
}
