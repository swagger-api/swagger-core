package io.swagger.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Link;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Xml;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlRootElement;

import static org.testng.Assert.*;

public class ModelResolverTest extends SwaggerTestBase {

    @Test(dataProvider = "testXmlNamespaceData")
    public void testXmlNamespace(Class clazz, String name, String namespace) throws Exception {
        final ObjectMapper mapper = mapper();
        final ModelResolver modelResolver = new ModelResolver(mapper);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final JavaType javaType = mapper.constructType(clazz);

        final Model model = modelResolver.resolve(javaType, context, null);

        assertTrue(model instanceof ModelImpl);

        final Xml xml = ((ModelImpl) model).getXml();
        assertNotNull(xml);
        assertEquals(xml.getName(), name);
        assertEquals(xml.getNamespace(), namespace);
    }

    @DataProvider
    private Object[][] testXmlNamespaceData() {
        return new Object[][]{
                {Link.class, "link", null},
                {TypeWithNamespace.class, "TypeWithNamespace", "http://io.swagger/jackson"},
                {TypeWithoutNamespace.class, "TypeWithOutNamespace", "http://io.swagger/jackson/package"}
        };
    }

    @XmlRootElement(name = "TypeWithNamespace", namespace = "http://io.swagger/jackson")
    public static class TypeWithNamespace {
    }

    @XmlRootElement(name = "TypeWithOutNamespace")
    public static class TypeWithoutNamespace {
    }

}
