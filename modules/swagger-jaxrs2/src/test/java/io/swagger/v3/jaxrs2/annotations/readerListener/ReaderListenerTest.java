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
    public void testReaderListener() throws Exception {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(Collections.singleton(ReaderListenerResource.class));
        assertNotNull(openAPI);
        assertEquals(openAPI.getTags().get(0).getName(), "Tag-added-before-read");
        assertEquals(openAPI.getTags().get(1).getName(), "Tag-added-after-read");
    }

}
