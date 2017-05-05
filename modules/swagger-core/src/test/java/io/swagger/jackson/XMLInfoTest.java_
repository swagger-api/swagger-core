package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Xml;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

public class XMLInfoTest extends SwaggerTestBase {

    @Test
    public void testSimple() throws Exception {
        final ModelConverter mr = modelResolver();
        final Model model = mr.resolve(XmlDecoratedBean.class, new ModelConverterContextImpl(mr), null);
        assertTrue(model instanceof ModelImpl);

        final ModelImpl impl = (ModelImpl) model;

        final Xml xml = impl.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        // Cast it to an array property
        final ArrayProperty property = (ArrayProperty)impl.getProperties().get("elements");
        assertNotNull(property);
        final Xml propertyXml = property.getXml();
        assertNotNull(propertyXml);
        assertNull(propertyXml.getName());
        assertTrue(propertyXml.getWrapped());
        // Get the xml for items for the array property
        final Xml itemsXml = property.getItems().getXml();
        assertNotNull(itemsXml);
        // Check the name of item name
        assertEquals(itemsXml.getName(), "element");
        assertNotNull(impl.getProperties().get("elementC"));
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @ApiModel(description = "DESC")
    static class XmlDecoratedBean {

        @XmlElement(name = "elementB")
        public int b;

        @XmlElementWrapper(name = "elements")
        @XmlElement(name = "element")
        public List<String> elements;

        @JsonProperty("elementC")
        public String c;
    }

    @Test
    public void testReadingXmlAccessorTypeNone() throws Exception {
        final ModelConverter mr = modelResolver();
        final Model model = mr.resolve(XmlDecoratedBeanXmlAccessorNone.class, new ModelConverterContextImpl(mr), null);
        assertTrue(model instanceof ModelImpl);

        final ModelImpl impl = (ModelImpl) model;

        final Xml xml = impl.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        final Property property = impl.getProperties().get("a");
        assertNotNull(property);

        assertNull(impl.getProperties().get("b"));
    }

    @Test
    public void testReadingXmlAccessorTypePublic() throws Exception {
        final ModelConverter mr = modelResolver();
        final Model model = mr.resolve(XmlDecoratedBeanXmlAccessorPublic.class, new ModelConverterContextImpl(mr), null);
        assertTrue(model instanceof ModelImpl);

        final ModelImpl impl = (ModelImpl) model;

        final Xml xml = impl.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        final Property propertyA = impl.getProperties().get("a");
        assertNotNull(propertyA);

        Property propertyB = impl.getProperties().get("b");
        assertNotNull(propertyB);
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @XmlAccessorType(XmlAccessType.NONE)
    @ApiModel
    static class XmlDecoratedBeanXmlAccessorNone {

        @XmlElement
        public int a;

        public String b;
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @ApiModel
    static class XmlDecoratedBeanXmlAccessorPublic {

        @XmlElement
        public int a;

        public String b;
    }

}
