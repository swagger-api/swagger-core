package io.swagger.v3.jaxrs2.annotations.readerListener;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.resources.ReaderListenerResource;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ReaderListenerTest {

    @Test(description = "test a readerListener resource")
    public void testReaderListener() throws Exception{
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(Collections.singleton(ReaderListenerResource.class));
        assertNotNull(openAPI);
        assertEquals(openAPI.getTags().get(0).getName(), "Tag-added-before-read");
        assertEquals(openAPI.getTags().get(1).getName(), "Tag-added-after-read");
    }

}
