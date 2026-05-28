package io.swagger.v3.jaxrs2.util;

import org.testng.annotations.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

public class ServletUtilsTest {

    @Test(description = "convert query parameters to multivaluedmap")
    public void convertWithRightOutputSize() throws Exception {

        Map<String, String[]> params = new HashMap<>();
        params.put("key1", new String[]{"value1", "value2"});
        params.put("key2", new String[]{"value2", "value3", "value4", "value4"});

        MultivaluedHashMap<String, String> multivaluedMap = ServletUtils.getQueryParams(params);
        assertEquals(multivaluedMap.size(), 2);
        assertEquals(multivaluedMap.get("key1").size(), 2);
        assertEquals(multivaluedMap.get("key2").size(), 4);
        assertTrue(multivaluedMap.containsKey("key1"));
        assertTrue(multivaluedMap.containsKey("key2"));
    }

    @Test(description = "convert query parameters to multivaluedmap with decoded values")
    public void convertWithDecodedValues() throws Exception {

        Map<String, String[]> params = new HashMap<>();
        params.put(URLEncoder.encode("key1&", UTF_8.name()), new String[]{"value1", URLEncoder.encode("value2?", UTF_8.name())});
        params.put("key2", new String[]{URLEncoder.encode("value2", UTF_8.name()), "value3", "value4", "value4"});
        assertEquals(params.get("key1%26"), new String[]{"value1", "value2%3F"});

        MultivaluedHashMap<String, String> multivaluedHashMap = ServletUtils.getQueryParams(params);
        assertEquals(multivaluedHashMap.size(), 2);
        assertNotNull(multivaluedHashMap.get("key1&"));
        assertEquals(multivaluedHashMap.get("key1&").size(), 2);
        assertEquals(multivaluedHashMap.get("key1&"), Arrays.asList("value1", "value2?"));
        assertEquals(multivaluedHashMap.get("key2"), Arrays.asList("value2", "value3", "value4", "value4"));
    }

}
