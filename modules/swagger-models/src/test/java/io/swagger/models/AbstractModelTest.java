package io.swagger.models;

import io.swagger.models.properties.Property;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class AbstractModelTest {

    private static class MyAbstractModel extends AbstractModel {

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public void setDescription(String description) {
        }

        @Override
        public Map<String, Property> getProperties() {
            return null;
        }

        @Override
        public void setProperties(Map<String, Property> properties) {
        }

        @Override
        public Object getExample() {
            return null;
        }

        @Override
        public void setExample(Object example) {
        }

    }


    private AbstractModel instance;

    private AbstractModel other;

    @BeforeMethod
    public void setUp() throws Exception {
        instance = new MyAbstractModel();
        ExternalDocs externalDocs = new ExternalDocs();
        instance.setExternalDocs(externalDocs);
        Map<String, Object> vendorExtensions = new HashMap<String, Object>();
        Whitebox.setInternalState(instance, "vendorExtensions", vendorExtensions);

        other = new MyAbstractModel();
        other.setExternalDocs(externalDocs);
        Whitebox.setInternalState(other, "vendorExtensions", vendorExtensions);
    }

    @Test
    public void testEqualsObject() {
        assertTrue(instance.equals(instance), "An instance must be equals to itself");
        assertFalse(instance.equals(null), "An instance must not be equals to null");
        assertFalse(instance.equals(new Object()), "An instance must not be equals to an object of another class");
        assertTrue(instance.equals(other), "Instances having the same field values must be equals");
    }

    @Test
    public void testEqualsWithDifferentExternalDocs() {
        // when
        instance.setExternalDocs(null);

        // then
        assertFalse(instance.equals(other),
                "Instance with null externaldocs can not equals other with non null external docs");
        assertFalse(other.equals(instance),
                "Instance with non null externaldocs can not equals other with  null external docs");
    }

    @Test
    public void testEqualsWithDifferentVendorExtensions() {
        // when
        Whitebox.setInternalState(instance, "vendorExtensions", (Map<String, Object>) null);

        // then
        assertFalse(instance.equals(other),
                "Instance with null vendorExtensions can not equals other with non null vendorExtensions");
        assertFalse(other.equals(instance),
                "Instance with non null vendorExtensions can not equals other with  null vendorExtensions");
    }

    @Test
    public void testEqualsAndHashCode() {
        // when
        ExternalDocs externalDocs = new ExternalDocs();
        instance.setExternalDocs(externalDocs);
        other.setExternalDocs(externalDocs);

        // then
        assertEquals(instance, other, "The two instances must be equal since they have the same externalDocs");
        assertEquals(instance.hashCode(), other.hashCode(), "Hash code value must be the same since the two instances have the same externalDocs");

    }

    @Test
    public void testGetExternalDocs() {
        // given
        ExternalDocs value = new ExternalDocs();

        // when
        instance.setExternalDocs(value);

        // then
        assertEquals(value, instance.getExternalDocs(), "The instance externalDocs must be the one that have been set");
    }

    @Test
    public void testGetTitle() {
        // given
        String title = "title";

        // when
        instance.setTitle(title);

        // then
        assertEquals(title, instance.getTitle(), "The instance title must be the one that have been set");
    }

    @Test
    public void testGetVendorExtensions() {
        // given
        String name = "x-name";
        Object value = "value";

        // when
        instance.setVendorExtension(name, value);

        // then
        assertEquals(value, instance.getVendorExtensions().get(name),
                "Must be able to retrieve the value from the map");
    }

    @Test
    public void testCloneTo() {
        // given
        AbstractModel clone = new ModelImpl();
        ExternalDocs externalDocs = new ExternalDocs();
        instance.setExternalDocs(externalDocs);

        // when
        instance.cloneTo(clone);

        // then
        assertEquals(externalDocs, clone.getExternalDocs(), "The instance and the clone must have the same value");
    }

    @Test
    public void testClone() {
        assertNull(instance.clone(), "The default clone method from abstract model must return null");
    }

    @Test
    public void testGetReference() {
        // given
        String reference = "reference";

        // when
        instance.setReference(reference);

        // then
        assertEquals(reference, instance.getReference(),
                "The obtained reference value must be the same that we have set");
    }
}
