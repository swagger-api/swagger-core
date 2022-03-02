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

package io.swagger.v3.plugin.maven;

import java.io.File;

public class SwaggerConfigFileTest extends ASwaggerMavenIntegrationTest {


    public void testResolveFromConfigIncludingOpenApi() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileFromConfigAndOpenApi.xml");

        SwaggerMojo mojo = runTest(pom, openAPI -> {
            assertEquals(1, openAPI.getServers().get(0).getVariables().size());
            assertNotNull(openAPI.getInfo());
        });
        assertTrue(mojo.getInternalConfiguration().isPrettyPrint());
        assertEquals(1, mojo.getInternalConfiguration().getResourcePackages().size());
        assertEquals("io.swagger.v3.plugin.maven.petstore.petstore", mojo.getInternalConfiguration().getResourcePackages().iterator().next());
        assertEquals(1, mojo.getInternalConfiguration().getOpenAPI().getServers().get(0).getVariables().size());
    }

    public void testResolveFromConfig() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileFromConfig.xml");

        SwaggerMojo mojo = runTest(pom);
        assertTrue(mojo.getInternalConfiguration().isPrettyPrint());
        assertEquals(1, mojo.getInternalConfiguration().getResourcePackages().size());
        assertEquals("io.swagger.v3.plugin.maven.petstore.petstore", mojo.getInternalConfiguration().getResourcePackages().iterator().next());
        assertEquals(2, mojo.getInternalConfiguration().getOpenAPI().getServers().get(0).getVariables().size());
    }
}
