package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import io.swagger.converter.ModelConverters;
import io.swagger.models.Address;
import io.swagger.models.Issue534;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.ModelWithJAXBAnnotations;
import io.swagger.models.Xml;
import io.swagger.models.properties.Property;

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
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(Monster.class);
        final Model model = schemas.get("Monster");

        assertNotNull(model);
        assertTrue(model instanceof ModelImpl);
        Xml xml = ((ModelImpl) model).getXml();

        assertNotNull(xml);
        assertEquals(xml.getName(), "monster");
        final Property property = model.getProperties().get("children");
        assertNotNull(property);
        xml = property.getXml();
        assertTrue(xml.getWrapped());
        assertNull(xml.getName());
    }

    @Test(description = "it should not create an xml object")
    public void itShouldNotCreateXmlObject() {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(Address.class);
        final Model model = schemas.get("Address");

        assertNotNull(model);
        assertTrue(model instanceof ModelImpl);

        final Property property = model.getProperties().get("streetNumber");
        final Xml xml = property.getXml();

        assertNull(xml);
    }

    @Test(description = "it should stay hidden per 534")
    public void stayHidden() {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(Issue534.class);
        assertEquals(schemas.get("Issue534").getProperties().size(), 1);
    }

    @Test(description = "it should process a model with JAXB annotations")
    public void processModelWithJAXBAnnotations() {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(ModelWithJAXBAnnotations.class);
        assertEquals(schemas.size(), 1);

        final Model model = schemas.get("ModelWithJAXBAnnotations");
        assertNotNull(model);
        assertTrue(model instanceof ModelImpl);

        final Xml rootXml = ((ModelImpl) model).getXml();
        assertNotNull(rootXml);
        assertEquals(rootXml.getName(), "rootName");

        for (Map.Entry<String, Property> entry : model.getProperties().entrySet()) {
            final String name = entry.getKey();
            final Property property = entry.getValue();
            if ("id".equals(name)) {
                final Xml xml = property.getXml();
                assertNotNull(xml);
                assertNull(xml.getName());
                assertTrue(xml.getAttribute());
                assertNull(xml.getWrapped());
            } else if ("name".equals(name)) {
                final Xml xml = property.getXml();
                assertNotNull(xml);
                assertEquals(xml.getName(), "renamed");
                assertNull(xml.getAttribute());
                assertNull(xml.getWrapped());
            } else if (Arrays.asList("list", "forcedElement").contains(name)) {
                assertNull(property.getXml());
            } else if ("wrappedList".equals(name)) {
                final Xml xml = property.getXml();
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
        final ModelImpl model = io.swagger.util.Yaml.mapper().readValue(yaml, ModelImpl.class);

        final Property wrappedList = model.getProperties().get("wrappedList");
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
