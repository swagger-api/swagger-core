package io.swagger.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Link;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Xml;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlRootElement;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

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

    @Test
    public void testResolvePropertyWithAtomicReference() {
        final ObjectMapper mapper = mapper();
        final ModelResolver modelResolver = new ModelResolver(mapper);

        final JavaType javaType = TypeFactory.defaultInstance().constructType(TypeWithAtomicReferenceMember.class);
        final BeanDescription beanDescription = mapper.getSerializationConfig().introspect(javaType);

        JavaType atomicReferenceBigDecimalType = null;
        for (final BeanPropertyDefinition propDef : beanDescription.findProperties()) {
            if ("member".equals(propDef.getName())) {
                atomicReferenceBigDecimalType = propDef.getPrimaryType();
            }
        }
        assertNotNull(atomicReferenceBigDecimalType, "Failed to read atomic reference field 'member'");

        final Property actualProperty = modelResolver.resolveProperty(atomicReferenceBigDecimalType,
                new ModelConverterContextImpl(modelResolver), new Annotation[0], null);
        assertEquals(actualProperty.getType(), DecimalProperty.TYPE, "Got wrong type for AtomicReference member");
    }

    @SuppressWarnings("unused")
    private static final class TypeWithAtomicReferenceMember {
        AtomicReference<BigDecimal> member;

        public AtomicReference<BigDecimal> getMember() {
            return member;
        }

        public void setMember(AtomicReference<BigDecimal> member) {
            this.member = member;
        }
    }


}
