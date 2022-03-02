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

package io.swagger.v3.jaxrs2.annotations.info;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class InfoTest extends AbstractAnnotationTest {
    @Test
    public void testSimpleInfoGet() {
        String openApiYAML = readIntoYaml(InfoTest.ClassWithInfoAnnotation.class);
        int start = openApiYAML.indexOf("info:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "info:\n" +
                "  title: the title\n" +
                "  description: My API\n" +
                "  contact:\n" +
                "    name: Fred\n" +
                "    url: http://gigantic-server.com\n" +
                "    email: Fred@gigagantic-server.com\n" +
                "  license:\n" +
                "    name: Apache 2.0\n" +
                "    url: http://foo.bar\n" +
                "  version: \"0.0\"";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    @OpenAPIDefinition(info = @Info(
            title = "the title",
            version = "0.0",
            description = "My API",
            license = @License(name = "Apache 2.0", url = "http://foo.bar"),
            contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com"))
    )
    static class ClassWithInfoAnnotation {
        // do something here
    }
}