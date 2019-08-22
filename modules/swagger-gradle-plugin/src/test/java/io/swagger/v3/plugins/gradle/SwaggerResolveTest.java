package io.swagger.v3.plugins.gradle;

import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.Assert.assertEquals;

public class SwaggerResolveTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File buildFile;
    private File settingsFile;
    private File openapiInputFile;
    private String outputFile;
    private String outputDir;

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
        settingsFile = testProjectDir.newFile("settings.gradle");
        openapiInputFile = testProjectDir.newFile("openapiinput.yaml");
        writeFile(openapiInputFile, "openapi: 3.0.1\n" +
                "servers:\n" +
                "- url: http://foo\n" +
                "  description: server 1\n" +
                "  variables:\n" +
                "    var1:\n" +
                "      description: var 1\n" +
                "      enum:\n" +
                "      - \"1\"\n" +
                "      - \"2\"\n" +
                "      default: \"2\"\n" +
                "    var2:\n" +
                "      description: var 2\n" +
                "      enum:\n" +
                "      - \"1\"\n" +
                "      - \"2\"\n" +
                "      default: \"2\"");
    }

    @Test
    public void testSwaggerResolveTask() throws IOException {
        outputDir = testProjectDir.getRoot().toString() + "/target";
        outputFile = testProjectDir.getRoot().toString() + "/testAPI.json";
        //outputDir = "/tmp/a/target";
        String resolveTask = "resolve";

        String buildFileContent =
                "buildscript {\n" +
                        "        dependencies {\n" +
                        "            classpath files(\"" + testProjectDir.getRoot().toString() + "/classes/java/test\")\n" +
                        "        }\n" +
                        "    }\n" +
                "plugins {\n" +
                "    id 'groovy'\n" +
                "    id 'java'\n" +
                "    id 'io.swagger.core.v3.swagger-gradle-plugin'\n" +
                "}\n" +
                "sourceSets {\n" +
                "    test {\n" +
                "        java {\n" +
                "            srcDirs = ['" + new File("src/test/javatest").getAbsolutePath() + "']\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "repositories {\n" +
                "    jcenter()\n" +
                "    mavenLocal()\n" +
                "    mavenCentral()\n" +
                "}\n" +
                "dependencies {  \n" +
                //"    compile configurations.runtime\n" +
                "    compile group: 'org.apache.commons', name: 'commons-lang3', version:'3.7'\n" +
                "    compile group: 'io.swagger.core.v3', name: 'swagger-jaxrs2', version:'2.0.9'\n" +
                "    compile group: 'javax.ws.rs', name: 'javax.ws.rs-api', version:'2.1'\n" +
                "    compile group: 'javax.servlet', name: 'javax.servlet-api', version:'3.1.0'\n" +
                "    testCompile group: 'com.github.tomakehurst', name: 'wiremock', version:'2.14.0'\n" +
                "    testCompile 'junit:junit:4+'\n" +
                "\n" +
                "\n" +
                "}\n" +
                resolveTask + " {\n" +
                "    outputFileName = 'PetStoreAPI'\n" +
                "    outputFormat = 'JSON'\n" +
                "    prettyPrint = 'TRUE'\n" +
                //"    classpath = compileTestJava.outputs.files\n" +
                "    classpath = sourceSets.test.runtimeClasspath\n" +
                "    resourcePackages = ['io.swagger.v3.plugins.gradle.petstore']\n" +
                "    outputPath = \'" + outputDir + "\'\n" +
                "    filterClass = \'io.swagger.v3.plugins.gradle.resources.MyFilter\'\n" +
                "    openApiFile = file(\'" + openapiInputFile.getAbsolutePath() + "\')\n" +
                "}";


        String settingsFileContent = "pluginManagement {\n" +
                "    repositories {\n" +
                "        maven {\n" +
                "            url mavenLocal().url\n" +
                "        }\n" +
                "        gradlePluginPortal()\n" +
                "    }\n" +
                "}\n" +
                "rootProject.name = 'gradle-test'\n" +
                "\n";
        writeFile(buildFile, buildFileContent);
        writeFile(settingsFile, settingsFileContent);

        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir.getRoot())
                .withDebug(true)
                //.withArguments("build", "--stacktrace", "--info")
                .withArguments(resolveTask, "--stacktrace")
                .build();

        assertEquals(SUCCESS, result.task(":" + resolveTask).getOutcome());
        assertTrue(new File(outputDir + "/PetStoreAPI.json").exists());
        assertTrue(FileUtils.readFileToString(new File(outputDir + "/PetStoreAPI.json")).contains("UPDATEDBYFILTER"));
    }

    private void writeFile(File destination, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(destination));
            output.write(content);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
}
