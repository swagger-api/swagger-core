package io.swagger.v3.oas.models.links;

import java.util.HashMap;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;

public class LinkParameterTest {

    @Test
    public void testValue() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.setValue("foo");
        linkParameter.setValue("bar");
        linkParameter.setValue("baz");

        assertEquals(linkParameter.value("bar"), linkParameter);
        assertEquals(linkParameter.getValue(), "bar");
    }

    @Test
    public void testEquals() {
        LinkParameter linkParameter = new LinkParameter();
        assertNotEquals(linkParameter, null);
        assertNotEquals(linkParameter, new String());

        assertEquals(linkParameter, linkParameter);
        assertEquals(linkParameter, new LinkParameter());
    }

    @Test
    public void testGetExtensions1() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.addExtension("", null);
        linkParameter.addExtension("y-", null);
        linkParameter.addExtension(null, null);

        assertNull(linkParameter.getExtensions());
    }

    @Test
    public void testGetExtensions2() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.addExtension("x-", "foo");
        linkParameter.addExtension("x-", "bar");
        linkParameter.addExtension("x-", "baz");

        assertEquals(linkParameter.getExtensions(),
                new HashMap<String, Object>() {{
                    put("x-", "baz");
                }});
    }

    @Test
    public void testGetExtensions3() {
        LinkParameter linkParameter = new LinkParameter();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");
        linkParameter.setExtensions(hashMap);

        assertEquals(linkParameter.getExtensions(),
                new HashMap<String, Object>() {{
                    put("x-", "baz");
                }});
    }

    @Test
    public void testExtensions() {
        LinkParameter linkParameter = new LinkParameter();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");

        assertEquals(linkParameter.extensions(hashMap), linkParameter);
    }

    @Test
    public void testToString() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.setValue("foo");
        assertEquals(linkParameter.toString(),
                "class LinkParameter {\n}");
    }
}
