package io.swagger.jackson;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EnumTest extends SwaggerTestBase {
    public void testEnum() throws Exception {
        ModelResolver modelResolver = new ModelResolver(mapper());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Model model = context.resolve(Currency.class);
        assertNull(model);
        Property property = context.resolveProperty(Currency.class, new Annotation[]{});
        assertNotNull(property);
        Assert.assertThat(property, CoreMatchers.instanceOf(StringProperty.class));
        final StringProperty strProperty = (StringProperty) property;
        assertNotNull(strProperty.getEnum());
        final Collection<String> values =
                new ArrayList<String>(Collections2.transform(Arrays.asList(Currency.values()), Functions.toStringFunction()));
        assertEquals(values, strProperty.getEnum());
    }

    public enum Currency {
        USA, CANADA
    }
}
