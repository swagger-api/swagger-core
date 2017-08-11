package io.swagger.resolving;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import io.swagger.converter.ModelConverters;

import io.swagger.oas.models.Address;
import io.swagger.oas.models.Issue534;
import io.swagger.oas.models.ModelWithJAXBAnnotations;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.XML;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

public class XmlModelTest {

    @Test(description = "it should process an XML model attribute")
    public void processXMLModelAttribute() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(Monster.class);
        final Schema model = schemas.get("Monster");

        assertNotNull(model);
        assertTrue(model instanceof Schema);
        XML xml = model.getXml();

        assertNotNull(xml);
        assertEquals(xml.getName(), "monster");
        final Schema property = (Schema)model.getProperties().get("children");
        assertNotNull(property);
        xml = property.getXml();
        assertTrue(xml.getWrapped());
        assertNull(xml.getName());
    }


    @Test(description = "it should not create an xml object")
    public void itShouldNotCreateXmlObject() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(Address.class);
        final Schema model = schemas.get("Address");

        assertNotNull(model);
        assertTrue(model instanceof Schema);

        final Schema property = (Schema)model.getProperties().get("streetNumber");
        final XML xml = property.getXml();

        assertNull(xml);
    }
    @Test(description = "it should stay hidden per 534")
    public void stayHidden() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(Issue534.class);
        assertEquals(schemas.get("Issue534").getProperties().size(), 1);
    }

    @Test(description = "it should process a model with JAXB annotations")
    public void processModelWithJAXBAnnotations() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(ModelWithJAXBAnnotations.class);
        assertEquals(schemas.size(), 1);

        final Schema model = schemas.get("ModelWithJAXBAnnotations");
        assertNotNull(model);
        assertTrue(model instanceof Schema);

        final XML rootXml = model.getXml();
        assertNotNull(rootXml);
        assertEquals(rootXml.getName(), "rootName");

        Map<String, Schema> props = model.getProperties();
        for (Map.Entry<String, Schema> entry : props.entrySet()) {
            final String name = entry.getKey();
            final Schema property = entry.getValue();
            if ("id".equals(name)) {
                final XML xml = property.getXml();
                assertNotNull(xml);
                assertNull(xml.getName());
                assertTrue(xml.getAttribute());
                assertNull(xml.getWrapped());
            } else if ("name".equals(name)) {
                final XML xml = property.getXml();
                assertNotNull(xml);
                assertEquals(xml.getName(), "renamed");
                assertNull(xml.getAttribute());
                assertNull(xml.getWrapped());
            } else if (Arrays.asList("list", "forcedElement").contains(name)) {
                assertNull(property.getXml());
            } else if ("wrappedList".equals(name)) {
                final XML xml = property.getXml();
                assertNotNull(xml);
                assertEquals(xml.getName(), "wrappedListItems");
                assertNull(xml.getAttribute());
                assertTrue(xml.getWrapped());
            } else {
                fail(String.format("Unexpected property: %s", name));
            }
        }
    }

    @Test(description = "it should deserialize a model")
    public void deserializeModel() throws IOException {
        final String yaml = "---\n" +
                "type: \"object\"\n" +
                "properties:\n" +
                "  id:\n" +
                "    type: \"string\"\n" +
                "    xml:\n" +
                "      attribute: true\n" +
                "  name:\n" +
                "    type: \"string\"\n" +
                "    xml:\n" +
                "      name: \"renamed\"\n" +
                "  list:\n" +
                "    type: \"array\"\n" +
                "    items:\n" +
                "      type: \"string\"\n" +
                "  wrappedList:\n" +
                "    type: \"array\"\n" +
                "    xml:\n" +
                "      name: \"wrappedListItems\"\n" +
                "      wrapped: true\n" +
                "    items:\n" +
                "      type: \"string\"\n" +
                "  forcedElement:\n" +
                "    type: \"array\"\n" +
                "    items:\n" +
                "      type: \"string\"\n" +
                "xml:\n" +
                "  name: \"rootName\"";
        final Schema model = io.swagger.util.Yaml.mapper().readValue(yaml, Schema.class);

        final Schema wrappedList = (Schema)model.getProperties().get("wrappedList");
        assertNotNull(wrappedList);
        assertNotNull(wrappedList.getXml());
        assertEquals(wrappedList.getXml().getName(), "wrappedListItems");
    }

    @XmlRootElement(name = "monster")
    class Monster {
        public String name = "";

        @XmlElementWrapper()
        @XmlElement(name = "children")
        public java.util.List<String> children = new ArrayList<String>();
    }
}
