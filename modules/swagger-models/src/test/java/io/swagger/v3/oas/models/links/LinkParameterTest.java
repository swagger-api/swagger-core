/**
 * Copyright 2019 SmartBear Software
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
