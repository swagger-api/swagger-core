package io.swagger.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import org.immutables.value.Value;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ImmutablesGenerationTest  extends SwaggerTestBase {
    private final ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
    private final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

    @Test
    public void testImmutables() {
        final Model model = context.resolve(TestWidget.class);
        final Map<String, Property> props = model.getProperties();
        assertEquals(props.size(), 2);

        Iterator<Property> it = props.values().iterator();
        Property prop;

        prop = it.next();
        assertEquals(prop.getName(), "wibble");
        prop = it.next();
        assertEquals(prop.getName(), "wobble");
    }

    @Value.Immutable
    @JsonSerialize(as=ImmutableTestWidget.class)
    public interface TestWidget {
        public String wibble();
        public String wobble();
    }
}
