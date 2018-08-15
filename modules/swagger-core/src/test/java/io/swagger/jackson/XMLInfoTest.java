package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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

        assertNotNull(impl.getProperties().get("a"));

        assertNull(impl.getProperties().get("b"));

        assertNotNull(impl.getProperties().get("c"));

        assertNotNull(impl.getProperties().get("d"));

        assertNotNull(impl.getProperties().get("e"));

        assertNotNull(impl.getProperties().get("f")); 
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @XmlAccessorType(XmlAccessType.NONE)
    @ApiModel
    static class XmlDecoratedBeanXmlAccessorNone {

        @XmlElement
        public int a;

        public String b;

        @XmlAttribute
        public String c;

        @XmlElementRef
        public XmlDecoratedBean d;

        @XmlElementRefs(value = {@XmlElementRef})
        public List<XmlDecoratedBean> e;

        @JsonProperty
        public int f;
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

        final Property propertyB = impl.getProperties().get("b");
        assertNotNull(propertyB);

        final Property propertyC = impl.getProperties().get("c");
        assertNull(propertyC);

    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @ApiModel
    static class XmlDecoratedBeanXmlAccessorPublic {

        @XmlElement
        public int a;

        public String b;

        @JsonIgnore
        public String c;
    }

}
