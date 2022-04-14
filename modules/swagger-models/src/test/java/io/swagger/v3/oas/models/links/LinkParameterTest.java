package io.swagger.v3.oas.models.links;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LinkParameterTest {

    @Test
    public void testValue() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.setValue("foo");
        linkParameter.setValue("bar");
        linkParameter.setValue("baz");

        Assert.assertEquals(linkParameter.value("bar"), linkParameter);
        Assert.assertEquals(linkParameter.getValue(), "bar");
    }

    @Test
    public void testEquals() {
        LinkParameter linkParameter = new LinkParameter();
        Assert.assertFalse(linkParameter.equals(null));
        Assert.assertFalse(linkParameter.equals(new String()));

        Assert.assertTrue(linkParameter.equals(linkParameter));
        Assert.assertTrue(linkParameter.equals(new LinkParameter()));
    }

    @Test
    public void testGetExtensions1() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.addExtension("", null);
        linkParameter.addExtension("y-", null);
        linkParameter.addExtension(null, null);

        Assert.assertNull(linkParameter.getExtensions());
    }

    @Test
    public void testGetExtensions2() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.addExtension("x-", "foo");
        linkParameter.addExtension("x-", "bar");
        linkParameter.addExtension("x-", "baz");

        Assert.assertEquals(linkParameter.getExtensions(),
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

        Assert.assertEquals(linkParameter.getExtensions(),
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

        Assert.assertEquals(linkParameter.extensions(hashMap), linkParameter);
    }

    @Test
    public void testToString() {
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.setValue("foo");
        Assert.assertEquals(linkParameter.toString(),
                "class LinkParameter {\n}");
    }
}
