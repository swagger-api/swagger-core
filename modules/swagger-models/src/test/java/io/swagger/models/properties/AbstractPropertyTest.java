package io.swagger.models.properties;

import io.swagger.models.Xml;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class AbstractPropertyTest {

    /*
     * Tests getters and setters methods on {@link AbstractProperty} It was not
     * possible to cove it with {@link io.swagger.PojosTest} so a manual
     * implementation is provided for now TODO improve PojosTest to test getters
     * and setters for abstracts classes
     */
    @Test
    public void testGettersAndSetters() {

        // given
        AbstractProperty instance = new ArrayProperty();
        String name = "name";

        // when
        instance.setName(name);

        // then
        assertEquals(instance.getName(), name, "The get name must be the same as the set one");

        // given
        String type = "type";

        // when
        instance.setType(type);

        // then
        assertEquals(instance.getType(), type, "The get type must be the same as the set one");

        // given
        String format = "format";

        // when
        instance.setFormat(format);

        // then
        assertEquals(instance.getFormat(), format, "The get format must be the same as the set one");

        // given
        String example = "example";

        // when
        instance.setExample(example);

        // then
        assertEquals(instance.getExample(), example, "The get example must be the same as the set one");

        // given
        Xml xml = new Xml();

        // when
        instance.setXml(xml);

        // then
        assertEquals(instance.getXml(), xml, "The get xml must be the same as the set one");

        // given
        boolean required = true;

        // when
        instance.setRequired(required);

        // then
        assertEquals(instance.getRequired(), required, "The get required must be the same as the set one");

        // given
        Integer position = 3;

        // when
        instance.setPosition(position);

        // then
        assertEquals(instance.getPosition(), position, "The get position must be the same as the set one");

        // given
        String description = "description";

        // when
        instance.setDescription(description);

        // then
        assertEquals(instance.getDescription(), description, "The get description must be the same as the set one");

        // given
        String title = "title";

        // when
        instance.setTitle(title);

        // then
        assertEquals(instance.getTitle(), title, "The get title must be the same as the set one");

        // given
        Boolean readOnly = true;

        // when
        instance.readOnly();

        // then
        assertEquals(instance.getReadOnly(), readOnly, "The get readOnly must be the same as the set one");

        // given
        String access = "String access";

        // when
        instance.setAccess(access);

        // then
        assertEquals(instance.getAccess(), access, "The get access must be the same as the set one");

        // when
        instance.setReadOnly(false);

        // then
        assertNull(instance.getReadOnly(), "Read only must be null when set to false");

        // given
        instance.setDefault("default");
        String vendorName = "x-vendor";
        String value = "value";

        // when
        instance.setVendorExtension(vendorName, value);

        // then
        assertEquals(instance.getVendorExtensions().get(vendorName), value,
                "The retrieved value must be the same as the set one");

        // when
        instance.setVendorExtensionMap(new HashMap<String, Object>());

        // then
        assertEquals(instance.getVendorExtensions().get(vendorName), value,
                "The retrieved value must be the same as the set one");
    }
}
