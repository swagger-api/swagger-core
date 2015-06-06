package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public void testSimple() throws Exception {
        final ModelConverter mr = modelResolver();
        Model model = mr.resolve(XmlDecoratedBean.class, new ModelConverterContextImpl(mr), null);
        assertTrue(model instanceof ModelImpl);

        ModelImpl impl = (ModelImpl) model;

        Xml xml = impl.getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), "xmlDecoratedBean");

        Property property = impl.getProperties().get("items");
        assertNotNull(property);
        xml = property.getXml();

        assertNotNull(xml);
        assertEquals(xml.getName(), "item");
        assertTrue(xml.getWrapped());

        property = impl.getProperties().get("elementC");
        assertNotNull(property);
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
