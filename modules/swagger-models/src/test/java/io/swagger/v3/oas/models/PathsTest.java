package io.swagger.v3.oas.models;

import java.util.HashMap;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;

public class PathsTest {

    @Test
    public void testAddPathItem() {
        Paths paths = new Paths();
        assertEquals(paths.addPathItem("foo", null), paths);
    }

    @Test
    public void testEquals() {
        Paths paths = new Paths();
        assertEquals(paths, paths);
        assertEquals(paths, new Paths());

        assertNotEquals(paths, null);
        assertNotEquals(paths, new String());
    }

    @Test
    public void testGetExtensions1() {
        Paths paths = new Paths();
        paths.addExtension("", null);
        paths.addExtension("y-", null);
        paths.addExtension(null, null);

        assertNull(paths.getExtensions());
    }

    @Test
    public void testGetExtensions2() {
        Paths paths = new Paths();
        paths.addExtension("x-", "foo");
        paths.addExtension("x-", "bar");
        paths.addExtension("x-", "baz");

        assertEquals(paths.getExtensions(),
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

        assertEquals(paths.getExtensions(),
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

        assertEquals(paths.extensions(hashMap), paths);
    }

    @Test
    public void testToString() {
        Paths paths = new Paths();
        paths.addPathItem("foo", null);
        assertEquals(paths.toString(),
                "class Paths {\n    {foo=null}\n}");
    }
}
