package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.media.OASSchema;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.XML;
import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class XMLInfoTest extends SwaggerTestBase {

    @Test(enabled = false)
    public void testSimple() throws Exception {
        final ModelConverter mr = modelResolver();
        final Schema model = mr.resolve(XmlDecoratedBean.class, new ModelConverterContextImpl(mr), null);

        final XML xml = model.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        // Cast it to an array property
        final ArraySchema property = (ArraySchema)model.getProperties().get("elements");
        assertNotNull(property);
        final XML propertyXml = property.getXml();
        assertNotNull(propertyXml);
        assertNull(propertyXml.getName());
        assertTrue(propertyXml.getWrapped());
        // Get the xml for items for the array property
        final XML itemsXml = property.getItems().getXml();
        assertNotNull(itemsXml);
        // Check the name of item name
        assertEquals(itemsXml.getName(), "element");
        assertNotNull(model.getProperties().get("elementC"));
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @OASSchema(description = "DESC")
    static class XmlDecoratedBean {

        @XmlElement(name = "elementB")
        public int b;

        @XmlElementWrapper(name = "elements")
        @XmlElement(name = "element")
        public List<String> elements;

        @JsonProperty("elementC")
        public String c;
    }

    @Test(enabled = false)
    public void testReadingXmlAccessorTypeNone() throws Exception {
        final ModelConverter mr = modelResolver();
        final Schema model = mr.resolve(XmlDecoratedBeanXmlAccessorNone.class, new ModelConverterContextImpl(mr), null);

        final XML xml = model.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        final Schema property = model.getProperties().get("a");
        assertNotNull(property);

        assertNull(model.getProperties().get("b"));
    }

    @Test(enabled = false)
    public void testReadingXmlAccessorTypePublic() throws Exception {
        final ModelConverter mr = modelResolver();
        final Schema model = mr.resolve(XmlDecoratedBeanXmlAccessorPublic.class, new ModelConverterContextImpl(mr), null);

        final XML xml = model.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        final Schema propertyA = model.getProperties().get("a");
        assertNotNull(propertyA);

        Schema propertyB = model.getProperties().get("b");
        assertNotNull(propertyB);
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @XmlAccessorType(XmlAccessType.NONE)
    @OASSchema
    static class XmlDecoratedBeanXmlAccessorNone {

        @XmlElement
        public int a;

        public String b;
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @OASSchema
    static class XmlDecoratedBeanXmlAccessorPublic {

        @XmlElement
        public int a;

        public String b;
    }

}
