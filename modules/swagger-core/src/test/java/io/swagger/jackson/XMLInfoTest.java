package io.swagger.jackson;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.testng.annotations.Test;

import io.swagger.annotations.ApiModel;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Xml;
import io.swagger.models.properties.Property;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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

        final Property property = impl.getProperties().get("items");
        assertNotNull(property);
        final Xml propertyXml = property.getXml();

        assertNotNull(propertyXml);
        assertEquals(propertyXml.getName(), "item");
        assertTrue(propertyXml.getWrapped());

        assertNotNull(impl.getProperties().get("elementC"));
    }

    @XmlRootElement(name = "xmlDecoratedBean")
    @ApiModel(description = "DESC")
    static class XmlDecoratedBean {
        @XmlElement(name = "elementB")
        public int b;

        @XmlElementWrapper(name = "items")
        @XmlElement(name = "item")
        public List<String> items;

        @JsonProperty("elementC")
        public String c;
    }
}
