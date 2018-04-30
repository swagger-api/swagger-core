package io.swagger.v3.core.converting.override;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converting.override.resources.GenericModel;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
        final Map<String, Schema> read = converters.read(GenericModel.class);
        assertTrue(read.containsKey(GenericModel.class.getSimpleName()));

        final Schema model = read.get(GenericModel.class.getSimpleName());
        assertTrue(model.getProperties().containsKey(NAME));
        assertEquals(((Schema) model.getProperties().get(NAME)).getType(), "string");
        assertTrue(model.getProperties().containsKey(COUNT));
        assertEquals(((Schema) model.getProperties().get(COUNT)).getType(), "integer");
    }
}
