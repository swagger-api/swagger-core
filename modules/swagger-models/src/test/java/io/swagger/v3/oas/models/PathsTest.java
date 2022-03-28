package io.swagger.v3.oas.models;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PathsTest {

    @Test
    public void testAddPathItem() {
        Paths paths = new Paths();
        Assert.assertEquals(paths.addPathItem("foo", null), paths);
    }

    @Test
    public void testEquals() {
        Paths paths = new Paths();
        Assert.assertTrue(paths.equals(paths));
        Assert.assertTrue(paths.equals(new Paths()));

        Assert.assertFalse(paths.equals(null));
        Assert.assertFalse(paths.equals(new String()));
    }

    @Test
    public void testGetExtensions1() {
        Paths paths = new Paths();
        paths.addExtension("", null);
        paths.addExtension("y-", null);
        paths.addExtension(null, null);

        Assert.assertNull(paths.getExtensions());
    }

    @Test
    public void testGetExtensions2() {
        Paths paths = new Paths();
        paths.addExtension("x-", "foo");
        paths.addExtension("x-", "bar");
        paths.addExtension("x-", "baz");

        Assert.assertEquals(paths.getExtensions(),
                new HashMap<String, Object>() {{
                    put("x-", "baz");
                }});
    }

    @Test
    public void testGetExtensions3() {
        Paths paths = new Paths();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");
        paths.setExtensions(hashMap);

        Assert.assertEquals(paths.getExtensions(),
                new HashMap<String, Object>() {{
                    put("x-", "baz");
                }});
    }

    @Test
    public void testExtensions() {
        Paths paths = new Paths();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");

        Assert.assertEquals(paths.extensions(hashMap), paths);
    }

    @Test
    public void testToString() {
        Paths paths = new Paths();
        paths.addPathItem("foo", null);
        Assert.assertEquals(paths.toString(),
                "class Paths {\n    {foo=null}\n}");
    }
}
