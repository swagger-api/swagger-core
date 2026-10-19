package io.swagger.v3.oas.integration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Webhooks;
import org.testng.annotations.Test;

import javax.tools.ToolProvider;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GenericOpenApiScannerTest {
    @OpenAPIDefinition
    protected static class Definition {
        static {
            if (true) {
                throw new AssertionError("Scanning must not initialize classes");
            }
        }
    }

    protected static class InheritedDefinition extends Definition {
    }

    @Webhooks
    protected static class Hooks {
    }

    @Test
    public void shouldDiscoverNonPublicAndInheritedAnnotations() {
        GenericOpenApiScanner scanner = new GenericOpenApiScanner();
        scanner.setConfiguration(new SwaggerConfiguration()
                .resourcePackages(Collections.singleton(getClass().getPackage().getName())));

        assertEquals(scanner.classes(), expectedClasses());
    }

    @Test
    public void shouldDiscoverClassesWithoutPackageFilter() {
        GenericOpenApiScanner scanner = new GenericOpenApiScanner();
        scanner.setConfiguration(new SwaggerConfiguration());

        assertTrue(scanner.classes().containsAll(expectedClasses()));
    }

    @Test
    public void shouldUseScannerLoaderWhenContextLoaderIsNull() {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(null);
            shouldDiscoverNonPublicAndInheritedAnnotations();
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    @Test
    public void shouldDiscoverAndLoadClassesVisibleOnlyToContextLoader() throws Exception {
        Path directory = Files.createTempDirectory("swagger-scanner-");
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Path source = directory.resolve("ContextResource.java");
            Files.write(source, ("package scanner.context;"
                    + "@io.swagger.v3.oas.annotations.OpenAPIDefinition public class ContextResource {"
                    + "static { if (true) throw new AssertionError(\"Must not initialize\"); }}")
                    .getBytes(StandardCharsets.UTF_8));
            assertEquals(ToolProvider.getSystemJavaCompiler().run(null, null, null,
                    "-classpath", System.getProperty("java.class.path"), "-d", directory.toString(), source.toString()), 0);
            try (URLClassLoader loader = new URLClassLoader(new URL[]{directory.toUri().toURL()}, original)) {
                Thread.currentThread().setContextClassLoader(loader);
                GenericOpenApiScanner scanner = new GenericOpenApiScanner();
                scanner.setConfiguration(new SwaggerConfiguration()
                        .resourcePackages(Collections.singleton("scanner.context")));

                assertEquals(scanner.classes(), Collections.singleton(
                        Class.forName("scanner.context.ContextResource", false, loader)));
            }
        } finally {
            Thread.currentThread().setContextClassLoader(original);
            try (Stream<Path> paths = Files.walk(directory)) {
                for (Path path : (Iterable<Path>) paths.sorted(Comparator.reverseOrder())::iterator) {
                    Files.delete(path);
                }
            }
        }
    }

    private Set<Class<?>> expectedClasses() {
        return new HashSet<>(Arrays.asList(Definition.class, InheritedDefinition.class, Hooks.class));
    }
}
