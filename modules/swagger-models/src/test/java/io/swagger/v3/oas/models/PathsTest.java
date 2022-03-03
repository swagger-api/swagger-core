/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
