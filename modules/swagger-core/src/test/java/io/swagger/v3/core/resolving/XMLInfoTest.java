package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class XMLInfoTest extends SwaggerTestBase {

    @Test
    public void testSimple() throws Exception {
        final ModelConverter mr = modelResolver();
        ModelConverterContextImpl ctx = new ModelConverterContextImpl(mr);
        final Schema model = mr.resolve(new AnnotatedType(XmlDecoratedBean.class), ctx, null);

        final XML xml = model.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        // Cast it to an array property
        final ArraySchema property = (ArraySchema) model.getProperties().get("elements");
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
    @io.swagger.v3.oas.annotations.media.Schema(description = "DESC")
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
        final Schema model = mr.resolve(new AnnotatedType(XmlDecoratedBeanXmlAccessorNone.class), new ModelConverterContextImpl(mr), null);

        final XML xml = model.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        final Schema property = (Schema) model.getProperties().get("a");
        assertNotNull(property);

        assertNull(model.getProperties().get("b"));

        assertNotNull(model.getProperties().get("c"));

        assertNotNull(model.getProperties().get("d"));

        assertNotNull(model.getProperties().get("e"));

        assertNotNull(model.getProperties().get("f"));
    }

    @Test
    public void testReadingXmlAccessorTypePublic() throws Exception {
        final ModelConverter mr = modelResolver();
        final Schema model = mr.resolve(new AnnotatedType(XmlDecoratedBeanXmlAccessorPublic.class), new ModelConverterContextImpl(mr), null);

        final XML xml = model.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        final Schema propertyA = (Schema) model.getProperties().get("a");
        assertNotNull(propertyA);

        final Schema propertyB = (Schema) model.getProperties().get("b");
        assertNotNull(propertyB);

        final Schema propertyC = (Schema) model.getProperties().get("c");
        assertNull(propertyC);
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @XmlAccessorType(XmlAccessType.NONE)
    @io.swagger.v3.oas.annotations.media.Schema
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

    @XmlRootElement(name = "xmlDecoratedBean")
    @io.swagger.v3.oas.annotations.media.Schema
    static class XmlDecoratedBeanXmlAccessorPublic {

        @XmlElement
        public int a;

        public String b;
        
        @JsonIgnore
        public String c;

    }

}
