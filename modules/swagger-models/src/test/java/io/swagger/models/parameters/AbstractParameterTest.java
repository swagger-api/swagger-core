package io.swagger.models.parameters;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class AbstractParameterTest {

    /*
     * Tests getters and setters methods on {@link AbstractParameter}
     * It was not possible to cove it with {@link io.swagger.PojosTest} so a manual implementation is provided for now
     * TODO improve PojosTest to test getters and setters for abstracts classes
     */
    @Test
    public void testGettersAndSetters() {
        // given
        AbstractParameter abstractParameter = new BodyParameter();
        String in = "in";
        String name = "name";

        // when
        abstractParameter.setIn(in);
        abstractParameter.setName(name);

        // then
        assertEquals(abstractParameter.getIn(), in, "The get in must be the same as the set one");
        assertEquals(abstractParameter.getName(), name, "The get in must be the same as the set one");

        // given
        String description = "description";

        // when
        abstractParameter.setDescription(description);

        // then
        assertEquals(abstractParameter.getDescription(), description,
                "The get description must be the same as the set one");

        // given
        Boolean required = true;

        // when
        abstractParameter.setRequired(required);

        // then
        assertTrue(abstractParameter.getRequired(), "The get required must be the same as the set one");

        // given
        String access = "access";

        // when
        abstractParameter.setAccess(access);

        // then
        assertEquals(abstractParameter.getAccess(), access, "The get access must be the same as the set one");

        // given
        String pattern = "pattern";

        // when
        abstractParameter.setPattern(pattern);

        // then
        assertEquals(abstractParameter.getPattern(), pattern, "The get pattern must be the same as the set one");

        // given
        String vendorName = "x-vendor";
        String value = "value";

        // when
        abstractParameter.setVendorExtension(vendorName, value);

        // then
        assertEquals(abstractParameter.getVendorExtensions().get(vendorName), value,
                "The get value must be the same as the set one");
    }
}
