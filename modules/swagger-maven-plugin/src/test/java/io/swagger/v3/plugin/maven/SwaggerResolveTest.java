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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SwaggerResolveTest extends ASwaggerMavenIntegrationTest {

    protected static Logger LOGGER = LoggerFactory.getLogger(SwaggerResolveTest.class);

    public void testResolve() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFile.xml");
        checkOutput(runTest(pom));
    }

    public void testResolveWithFilter() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileWithFilter.xml");
        checkOutput(runTest(pom));
    }

    public void testResolveNoName() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileNoName.xml");
        checkOutput(runTest(pom));
    }

    public void testResolveJsonAndYaml() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileJsonAndYaml.xml");
        checkOutput(runTest(pom));
    }

    public void testResolveWithJsonInput() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileFromJsonInput.xml");
        checkOutput(runTest(pom));
    }

    private void checkOutput(SwaggerMojo mojo) {
        assertNull(mojo.getConfigurationFilePath());
    }
}
