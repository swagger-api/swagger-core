package io.swagger.v3.plugins.gradle;

import static java.lang.String.format;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
                "    implementation 'io.swagger.core.v3:swagger-jaxrs2:2.2.11'\n" +
                "    implementation 'javax.ws.rs:javax.ws.rs-api:2.1'\n" +
                "    implementation 'javax.servlet:javax.servlet-api:3.1.0'\n" +
                "    testImplementation 'com.github.tomakehurst:wiremock:2.27.2'\n" +
                "    testImplementation 'junit:junit:4+'\n" +
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
                "    openApiFile = file(\'" + toNormalizedPath(openapiInputFile.getAbsolutePath()) + "\')\n" +
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
                .withProjectDir(testProjectDir.getRoot())
                .withDebug(true)
                .withArguments(resolveTask, "--stacktrace", "--info")
                .forwardOutput()
                .build();

        assertThat(result.taskPaths(SUCCESS), hasItem(format(":%s", resolveTask)));
        assertThat(new File(outputDir + "/PetStoreAPI.json").exists(), is(true));
        assertThat(new String(Files.readAllBytes(Paths.get(outputDir, "PetStoreAPI.json")), StandardCharsets.UTF_8), containsString("UPDATEDBYFILTER"));
    }

     @Test
    public void testSwaggerResolveWithOAS31OptionTask() throws IOException {
        outputDir = testProjectDir.getRoot().toString() + "/target";
        outputFile = testProjectDir.getRoot().toString() + "/testAPI31.json";
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
                "    implementation 'io.swagger.core.v3:swagger-jaxrs2:2.2.11'\n" +
                "    implementation 'javax.ws.rs:javax.ws.rs-api:2.1'\n" +
                "    implementation 'javax.servlet:javax.servlet-api:3.1.0'\n" +
                "    testImplementation 'com.github.tomakehurst:wiremock:2.27.2'\n" +
                "    testImplementation 'junit:junit:4+'\n" +
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
                "    openApiFile = file(\'" + toNormalizedPath(openapiInputFile.getAbsolutePath()) + "\')\n" +
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
                .withProjectDir(testProjectDir.getRoot())
                .withDebug(true)
                .withArguments(resolveTask, "--stacktrace", "--info")
                .forwardOutput()
                .build();

        assertThat(result.taskPaths(SUCCESS), hasItem(format(":%s", resolveTask)));
        assertThat(new File(outputDir + "/PetStoreAPI31.json").exists(), is(true));

        byte[] content = Files.readAllBytes(Paths.get(outputDir, "PetStoreAPI31.json"));

        String strContent = new String(content, StandardCharsets.UTF_8);
        assertThat(strContent, containsString("\"openapi\" : \"3.1.0\""));
        //
    }

    private static void writeFile(File destination, String content) throws IOException {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(destination))) {
            output.write(content);
        }
    }

    private static String toNormalizedPath(String path) {
        return path.replace("\\", "/"); // necessary on windows
    }
}
