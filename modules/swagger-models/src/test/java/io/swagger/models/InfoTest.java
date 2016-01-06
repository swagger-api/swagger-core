package io.swagger.models;

import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class InfoTest {

    private Info instance;

    @BeforeMethod
    public void setUp() throws Exception {
        instance = new Info();
    }

    @Test
    public void testSetVendorExtension() {
        // given
        String name = "x-vendor";
        String value = "value";

        // when
        instance.setVendorExtension(name, value);

        // then
        assertEquals(instance.getVendorExtensions().get(name), value,
                "Must be able to retrieve the same value from the map");
    }

    @Test
    public void testMergeWith() {
        // given
        Info info = new Info();
        info.setDescription("description");
        Whitebox.setInternalState(instance, "vendorExtensions", (Object) null);

        // when
        instance.mergeWith(info);

        // then
        assertEquals(info.getDescription(), instance.getDescription(),
                "Merged instances must have the same description");
    }
}
