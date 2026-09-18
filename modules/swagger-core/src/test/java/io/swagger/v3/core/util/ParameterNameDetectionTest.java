package io.swagger.v3.core.util;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.dataformat.yaml.YAMLFactory;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.expectThrows;
import static org.testng.Assert.assertTrue;

public class ParameterNameDetectionTest {

    private static final String FIXTURE_PACKAGE = "io.swagger.v3.core.util.fixtures.";

    private URLClassLoader withParametersLoader;
    private URLClassLoader withoutParametersLoader;
    private Class<?> implicitParameterFixture;
    private Class<?> annotatedCreatorFixture;
    private Class<?> noParameterMetadataFixture;

    @BeforeClass
    public void compileFixtures() throws Exception {
        Path projectDirectory = Path.of(System.getProperty("basedir", "."));
        Path fixtureRoot = projectDirectory.resolve("src/test/parameter-name-fixtures");
        Path outputRoot = projectDirectory.resolve("target/parameter-name-fixtures");
        Path withParametersOutput = outputRoot.resolve("with-parameters");
        Path withoutParametersOutput = outputRoot.resolve("without-parameters");

        compile(fixtureRoot.resolve("with-parameters"), withParametersOutput, true);
        compile(fixtureRoot.resolve("without-parameters"), withoutParametersOutput, false);

        withParametersLoader = fixtureLoader(withParametersOutput);
        withoutParametersLoader = fixtureLoader(withoutParametersOutput);
        implicitParameterFixture = withParametersLoader.loadClass(FIXTURE_PACKAGE + "ImplicitParameterFixture");
        annotatedCreatorFixture = withParametersLoader.loadClass(
                FIXTURE_PACKAGE + "AnnotatedCreatorPrecedenceFixture");
        noParameterMetadataFixture = withoutParametersLoader.loadClass(
                FIXTURE_PACKAGE + "NoParameterMetadataFixture");

        assertTrue(implicitParameterFixture.getConstructors()[0].getParameters()[0].isNamePresent(),
                "The positive-control fixture must contain MethodParameters metadata");
        assertFalse(noParameterMetadataFixture.getConstructors()[0].getParameters()[0].isNamePresent(),
                "The negative-control fixture must not contain MethodParameters metadata");
    }

    @AfterClass(alwaysRun = true)
    public void closeFixtureLoaders() throws IOException {
        if (withParametersLoader != null) {
            withParametersLoader.close();
        }
        if (withoutParametersLoader != null) {
            withoutParametersLoader.close();
        }
    }

    @DataProvider(name = "publicMappers")
    public Object[][] publicMappers() {
        return new Object[][]{
                {"Json.mapper", (Supplier<ObjectMapper>) () -> Json.mapper().rebuild().build()},
                {"Json31.mapper", (Supplier<ObjectMapper>) () -> Json31.mapper().rebuild().build()},
                {"Yaml.mapper", (Supplier<ObjectMapper>) () -> Yaml.mapper().rebuild().build()},
                {"Yaml31.mapper", (Supplier<ObjectMapper>) () -> Yaml31.mapper().rebuild().build()},
                {"createJson", (Supplier<ObjectMapper>) ObjectMapperFactory::createJson},
                {"createJson31", (Supplier<ObjectMapper>) ObjectMapperFactory::createJson31},
                {"createYaml", (Supplier<ObjectMapper>) ObjectMapperFactory::createYaml},
                {"createYaml31", (Supplier<ObjectMapper>) ObjectMapperFactory::createYaml31},
                {"createJson(factory)", (Supplier<ObjectMapper>) () ->
                        ObjectMapperFactory.createJson(new JsonFactory())},
                {"createJson31(factory)", (Supplier<ObjectMapper>) () ->
                        ObjectMapperFactory.createJson31(new JsonFactory())},
                {"createYaml(factory)", (Supplier<ObjectMapper>) () ->
                        ObjectMapperFactory.createYaml(new YAMLFactory())},
                {"createYaml31(factory)", (Supplier<ObjectMapper>) () ->
                        ObjectMapperFactory.createYaml31(new YAMLFactory())},
                {"createJsonConverter", (Supplier<ObjectMapper>) ObjectMapperFactory::createJsonConverter},
                {"Json31.converterMapper", (Supplier<ObjectMapper>) () ->
                        Json31.converterMapper().rebuild().build()},
                {"create(JSON, false)", (Supplier<ObjectMapper>) () ->
                        ObjectMapperFactory.create(new JsonFactory(), false)},
                {"create(YAML, true)", (Supplier<ObjectMapper>) () ->
                        ObjectMapperFactory.create(new YAMLFactory(), true)},
                {"createYaml(false)", (Supplier<ObjectMapper>) () -> ObjectMapperFactory.createYaml(false)},
                {"createYaml(true)", (Supplier<ObjectMapper>) () -> ObjectMapperFactory.createYaml(true)}
        };
    }

    @Test(dataProvider = "publicMappers")
    public void detectsConstructorParameterNames(
            String mapperName, Supplier<ObjectMapper> mapperSupplier) throws Exception {
        ObjectMapper mapper = mapperSupplier.get();

        assertTrue(mapper.isEnabled(MapperFeature.DETECT_PARAMETER_NAMES), mapperName);
        Object result = mapper.readValue("{\"name\":\"Ada\"}", implicitParameterFixture);

        assertEquals(invokeGetter(result, "getName"), "Ada", mapperName);
    }

    @Test(dataProvider = "publicMappers")
    public void givesAnnotatedCreatorPrecedence(
            String mapperName, Supplier<ObjectMapper> mapperSupplier) throws Exception {
        Object result = mapperSupplier.get().readValue(
                "{\"explicit\":\"Ada\"}", annotatedCreatorFixture);

        assertEquals(invokeGetter(result, "getValue"), "explicit:Ada", mapperName);
    }

    @Test
    public void doesNotInferAPropertyNameWithoutParameterMetadata() {
        ObjectMapper mapper = ObjectMapperFactory.createJson();

        assertTrue(mapper.isEnabled(MapperFeature.DETECT_PARAMETER_NAMES));
        MismatchedInputException exception = expectThrows(MismatchedInputException.class,
                () -> mapper.readValue("{\"name\":\"Ada\"}", noParameterMetadataFixture));
        assertTrue(exception.getMessage().contains("Cannot construct instance"), exception.getMessage());
        assertTrue(exception.getMessage().contains("NoParameterMetadataFixture"), exception.getMessage());
        assertTrue(exception.getMessage().contains("no delegate- or property-based Creator"),
                exception.getMessage());
    }

    private static Object invokeGetter(Object target, String methodName) throws Exception {
        Method getter = target.getClass().getMethod(methodName);
        return getter.invoke(target);
    }

    private static URLClassLoader fixtureLoader(Path classesDirectory) throws IOException {
        return new URLClassLoader(
                new URL[]{classesDirectory.toUri().toURL()}, ParameterNameDetectionTest.class.getClassLoader());
    }

    private static void compile(Path sourceDirectory, Path outputDirectory, boolean parameters) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "The tests must run with a JDK so fixture sources can be compiled");
        Files.createDirectories(outputDirectory);
        List<Path> sources;
        try (var paths = Files.walk(sourceDirectory)) {
            sources = paths.filter(path -> path.toString().endsWith(".java")).toList();
        }
        assertFalse(sources.isEmpty(), "No fixture sources found in " + sourceDirectory);

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            var compilationUnits = fileManager.getJavaFileObjectsFromPaths(sources);
            var options = new java.util.ArrayList<>(List.of(
                    "--release", "17",
                    "-classpath", System.getProperty("java.class.path"),
                    "-d", outputDirectory.toString()));
            if (parameters) {
                options.add("-parameters");
            }
            Boolean success = compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
            assertTrue(Boolean.TRUE.equals(success), "Could not compile parameter-name fixtures");
        }
    }
}
