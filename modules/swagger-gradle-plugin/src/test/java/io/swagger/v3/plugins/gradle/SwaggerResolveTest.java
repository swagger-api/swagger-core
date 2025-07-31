package io.swagger.v3.plugins.gradle;

import static java.lang.String.format;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.testng.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class SwaggerResolveTest {

    private Path testProjectDir;
    private Path buildFile;
    private Path settingsFile;
    private Path openapiInputFile;
    private String outputFile;
    private String outputDir;

    @BeforeMethod
    public void setup() throws IOException {
        testProjectDir = Files.createTempDirectory("test");
        buildFile = Files.createFile(testProjectDir.resolve("build.gradle"));
        settingsFile = Files.createFile(testProjectDir.resolve("settings.gradle"));
        openapiInputFile = Files.createFile(testProjectDir.resolve("openapiinput.yaml"));
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
        outputDir = testProjectDir.toString() + "/target";
        outputFile = testProjectDir.toString() + "/testAPI.json";
        //outputDir = "/tmp/a/target";
        String resolveTask = "resolve";

        String buildFileContent =
                "plugins {\n" +
                "    id 'java'\n" +
                "    id 'io.swagger.core.v3.swagger-gradle-plugin'\n" +
                "}\n" +
                "sourceSets {\n" +
                "    test {\n" +
                "        java {\n" +
                "            srcDirs('" + toNormalizedPath(new File("src/test/java").getAbsolutePath()) + "')\n" +
                "            exclude('**/*Test.java')\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "repositories {\n" +
                "    mavenLocal()\n" +
                "    mavenCentral()\n" +
                "}\n" +
                "dependencies {  \n" +
                "    implementation 'io.swagger.core.v3:swagger-jaxrs2:2.2.36-SNAPSHOT'\n" +
                "    implementation 'javax.ws.rs:javax.ws.rs-api:2.1'\n" +
                "    implementation 'javax.servlet:javax.servlet-api:3.1.0'\n" +
                "    testImplementation 'com.github.tomakehurst:wiremock:2.27.2'\n" +
                "    testImplementation 'org.testng:testng:7.10.2'\n" +
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
                "    outputPath = \'" + toNormalizedPath(outputDir) + "\'\n" +
                "    filterClass = \'io.swagger.v3.plugins.gradle.resources.MyFilter\'\n" +
                "    openApiFile = file(\'" + toNormalizedPath(openapiInputFile.toAbsolutePath().toString()) + "\')\n" +
                "}";


        String settingsFileContent = "pluginManagement {\n" +
                "    repositories {\n" +
                "        maven {\n" +
                "            url mavenLocal().url\n" +
                "        }\n" +
                "        mavenCentral()\n" +
                "        gradlePluginPortal()\n" +
                "    }\n" +
                "}\n" +
                "rootProject.name = 'gradle-test'\n" +
                "\n";
        writeFile(buildFile, buildFileContent);
        writeFile(settingsFile, settingsFileContent);

        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir.toFile())
                .withDebug(true)
                .withArguments(resolveTask, "--stacktrace", "--info")
                .forwardOutput()
                .build();

        assertTrue(result.taskPaths(SUCCESS).contains(format(":%s", resolveTask)));
        assertTrue(new File(outputDir + "/PetStoreAPI.json").exists());
        assertTrue(Files.readString(Paths.get(outputDir, "PetStoreAPI.json")).contains("UPDATEDBYFILTER"));
    }

    @Test
    public void testSwaggerResolveWithOAS31OptionTask() throws IOException {
        outputDir = testProjectDir.toString() + "/target";
        outputFile = testProjectDir.toString() + "/testAPI31.json";
        //outputDir = "/tmp/a/target";
        String resolveTask = "resolve";

        String buildFileContent =
                "plugins {\n" +
                "    id 'java'\n" +
                "    id 'io.swagger.core.v3.swagger-gradle-plugin'\n" +
                "}\n" +
                "sourceSets {\n" +
                "    test {\n" +
                "        java {\n" +
                "            srcDirs('" + toNormalizedPath(new File("src/test/java").getAbsolutePath()) + "')\n" +
                "            exclude('**/*Test.java')\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "repositories {\n" +
                "    mavenLocal()\n" +
                "    mavenCentral()\n" +
                "}\n" +
                "dependencies {  \n" +
                "    implementation 'io.swagger.core.v3:swagger-jaxrs2:2.2.36-SNAPSHOT'\n" +
                "    implementation 'javax.ws.rs:javax.ws.rs-api:2.1'\n" +
                "    implementation 'javax.servlet:javax.servlet-api:3.1.0'\n" +
                "    testImplementation 'com.github.tomakehurst:wiremock:2.27.2'\n" +
                "    testImplementation 'org.testng:testng:7.10.2'\n" +
                "\n" +
                "\n" +
                "}\n" +
                resolveTask + " {\n" +
                "    outputFileName = 'PetStoreAPI31'\n" +
                "    outputFormat = 'JSON'\n" +
                "    prettyPrint = 'TRUE'\n" +
                //"    classpath = compileTestJava.outputs.files\n" +
                "    classpath = sourceSets.test.runtimeClasspath\n" +
                "    resourcePackages = ['io.swagger.v3.plugins.gradle.petstore']\n" +
                "    outputPath = \'" + toNormalizedPath(outputDir) + "\'\n" +
                "    openAPI31 = \'TRUE\'\n" +
                "    convertToOpenAPI31 = \'TRUE\'\n" +
                "    openApiFile = file(\'" + toNormalizedPath(openapiInputFile.toAbsolutePath().toString()) + "\')\n" +
                "}";


        String settingsFileContent = "pluginManagement {\n" +
                "    repositories {\n" +
                "        maven {\n" +
                "            url mavenLocal().url\n" +
                "        }\n" +
                "        mavenCentral()\n" +
                "        gradlePluginPortal()\n" +
                "    }\n" +
                "}\n" +
                "rootProject.name = 'gradle-test'\n" +
                "\n";
        writeFile(buildFile, buildFileContent);
        writeFile(settingsFile, settingsFileContent);

        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir.toFile())
                .withDebug(true)
                .withArguments(resolveTask, "--stacktrace", "--info")
                .forwardOutput()
                .build();

        assertTrue(result.taskPaths(SUCCESS).contains(format(":%s", resolveTask)));
        assertTrue(new File(outputDir + "/PetStoreAPI31.json").exists());

        byte[] content = Files.readAllBytes(Paths.get(outputDir, "PetStoreAPI31.json"));

        String strContent = new String(content, StandardCharsets.UTF_8);
        assertTrue(strContent.contains("\"openapi\" : \"3.0.1\""));
    }

    private static void writeFile(Path destination, String content) throws IOException {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(destination.toFile()))) {
            output.write(content);
        }
    }

    private static String toNormalizedPath(String path) {
        return path.replace("\\", "/"); // necessary on windows
    }
}
