package io.swagger.v3.jaxrs2;

import aQute.bnd.build.model.EE;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.resource.CapReqBuilder;
import aQute.bnd.osgi.resource.ResourceBuilder;
import biz.aQute.resolve.AbstractResolveContext;
import biz.aQute.resolve.BndResolver;
import biz.aQute.resolve.GenericResolveContext;
import biz.aQute.resolve.ResolverLogger;
import org.osgi.framework.Version;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.namespace.PackageNamespace;
import org.osgi.resource.Capability;
import org.osgi.resource.Resource;
import org.osgi.resource.Wire;
import org.osgi.service.repository.ContentNamespace;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class OsgiJakartaClassLoadingTest {

    private static final String CONTRACT_NAMESPACE = "osgi.contract";
    private static final String CONTRACT_ATTRIBUTE = "osgi.contract";

    @Test
    public void resolvesAndLoadsJakartaEe10ApisWithOsgiResolver() throws Exception {
        Path restClasses = swaggerRestClasses();
        assertTrue(Files.isRegularFile(restClasses.resolve("META-INF/MANIFEST.MF")),
                "Missing generated swagger-rest bundle manifest");

        Path temporaryDirectory = Files.createTempDirectory("swagger-rest-osgi-resolver-");
        try {
            Resource rest = resourceForClasspathEntry(restClasses, temporaryDirectory);
            assertNotNull(rest, "swagger-rest classes did not produce an OSGi resource");

            try (ResolverLogger logger = new ResolverLogger()) {
                Map<Resource, List<Wire>> resolution = resolve(rest,
                        repositoryResources(restClasses, temporaryDirectory), logger);

                assertRestWiring(resolution.get(rest));
                assertCoreWiring(resolution);
                assertNoLegacyPackageProviders(resolution);
                assertClassLoadingFromResolvedBundles(rest, resolution);
            }
        } finally {
            deleteRecursively(temporaryDirectory);
        }
    }

    private static Path swaggerRestClasses() {
        return Path.of("target/classes").toAbsolutePath().normalize();
    }

    private static List<Resource> repositoryResources(Path restClasses, Path temporaryDirectory) throws Exception {
        List<Resource> resources = new ArrayList<>();
        for (Path entry : testClasspath()) {
            if (entry.toAbsolutePath().normalize().equals(restClasses)) {
                continue;
            }
            Resource resource = resourceForClasspathEntry(entry, temporaryDirectory);
            if (resource != null) {
                resources.add(resource);
            }
        }
        return resources;
    }

    private static Map<Resource, List<Wire>> resolve(Resource rest, List<Resource> repositoryResources,
                                                      ResolverLogger logger) throws Exception {
        GenericResolveContext context = new GenericResolveContext(logger);
        context.addEE(EE.JavaSE_17);
        // Model portable Java contracts and runtime-supplied service-loader
        // extenders as system capabilities rather than application bundles.
        context.getSystem().addResource(jakartaFrameworkCapabilities());
        context.getInput().addResource(rest);
        context.addRepository(AbstractResolveContext.createRepository(repositoryResources));
        context.done();
        return new BndResolver(logger).resolve(context);
    }

    private static void assertRestWiring(List<Wire> wires) {
        assertNotNull(wires, "Resolver did not return wiring for swagger-rest");
        assertPackageWire(wires, "jakarta.ws.rs", "3.1.0");
        assertPackageWire(wires, "jakarta.servlet", "6.0.0");
        assertContractWire(wires, "JakartaRESTfulWebServices", "3.1.0");
        assertContractWire(wires, "JakartaServlet", "6.0.0");
    }

    private static void assertCoreWiring(Map<Resource, List<Wire>> resolution) {
        Resource core = resolution.keySet().stream()
                .filter(resource -> "io.swagger.core.v3.swagger-core".equals(identity(resource)))
                .findFirst()
                .orElseThrow(() -> new AssertionError("swagger-core was not part of the resolved graph"));
        assertPackageWire(resolution.get(core), "jakarta.validation.constraints", "3.0.2");
        assertPackageWire(resolution.get(core), "jakarta.xml.bind.annotation", "4.0.2");
    }

    private static void assertNoLegacyPackageProviders(Map<Resource, List<Wire>> resolution) {
        boolean containsLegacyProvider = resolution.keySet().stream()
                .flatMap(resource -> resource.getCapabilities(PackageNamespace.PACKAGE_NAMESPACE).stream())
                .map(capability -> capability.getAttributes().get(PackageNamespace.PACKAGE_NAMESPACE))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .anyMatch(name -> name.startsWith("javax.ws.rs")
                        || name.startsWith("javax.servlet")
                        || name.startsWith("javax.validation")
                        || name.startsWith("javax.xml.bind"));
        assertFalse(containsLegacyProvider, "Resolved graph contains a legacy Jakarta API package provider");
    }

    private static void assertClassLoadingFromResolvedBundles(Resource rest,
                                                               Map<Resource, List<Wire>> resolution) throws Exception {
        Set<Resource> resources = new LinkedHashSet<>();
        resources.add(rest);
        resources.addAll(resolution.keySet());
        resolution.values().stream().flatMap(List::stream).map(Wire::getProvider).forEach(resources::add);

        URL[] urls = contentUrls(resources);
        assertTrue(urls.length > 0, "Resolved graph has no bundle content URLs for class loading");

        try (URLClassLoader loader = new URLClassLoader(urls, ClassLoader.getPlatformClassLoader())) {
            Class<?> reader = Class.forName("io.swagger.v3.jaxrs2.Reader", true, loader);
            assertNotNull(reader.getConstructor().newInstance());

            Class<?> json = Class.forName("io.swagger.v3.core.util.Json", true, loader);
            Object mapper = json.getMethod("mapper").invoke(null);
            Class<?> objectMapper = Class.forName("tools.jackson.databind.ObjectMapper", true, loader);
            Class<?> modelResolver = Class.forName("io.swagger.v3.core.jackson.ModelResolver", true, loader);
            Constructor<?> constructor = modelResolver.getConstructor(objectMapper);
            assertNotNull(constructor.newInstance(mapper));

            assertApiClass(loader, "jakarta.ws.rs.core.Response", "jakarta.ws.rs-api-3.1.0.jar");
            assertApiClass(loader, "jakarta.servlet.ServletContainerInitializer",
                    "jakarta.servlet-api-6.0.0.jar");
            assertApiClass(loader, "jakarta.validation.constraints.NotNull",
                    "jakarta.validation-api-3.0.2.jar");
            assertApiClass(loader, "jakarta.xml.bind.annotation.XmlElement",
                    "jakarta.xml.bind-api-4.0.2.jar");
        }
    }

    private static URL[] contentUrls(Set<Resource> resources) throws Exception {
        Set<URL> urls = new LinkedHashSet<>();
        for (Resource resource : resources) {
            for (Capability content : resource.getCapabilities(ContentNamespace.CONTENT_NAMESPACE)) {
                Object value = content.getAttributes().get(ContentNamespace.CAPABILITY_URL_ATTRIBUTE);
                if (value != null) {
                    URI uri = value instanceof URI ? (URI) value : URI.create(value.toString());
                    urls.add(uri.toURL());
                }
            }
        }
        return urls.toArray(URL[]::new);
    }

    private static void assertApiClass(ClassLoader loader, String className, String sourceJar) throws Exception {
        Class<?> apiClass = Class.forName(className, true, loader);
        assertTrue(apiClass.getProtectionDomain().getCodeSource().getLocation().toString().contains(sourceJar),
                className + " was not loaded from " + sourceJar);
    }

    private static void deleteRecursively(Path directory) throws Exception {
        if (!Files.exists(directory)) {
            return;
        }
        try (java.util.stream.Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).collect(Collectors.toList())) {
                Files.deleteIfExists(path);
            }
        }
    }

    private static List<Path> testClasspath() {
        String classpath = System.getProperty("surefire.test.class.path", System.getProperty("java.class.path"));
        return Arrays.stream(classpath.split(File.pathSeparator))
                .map(Path::of)
                .filter(Files::exists)
                .collect(Collectors.toList());
    }

    private static Resource resourceForClasspathEntry(Path entry, Path temporaryDirectory) throws Exception {
        File bundleFile;
        if (Files.isDirectory(entry)) {
            if (!Files.isRegularFile(entry.resolve("META-INF/MANIFEST.MF"))) {
                return null;
            }
            bundleFile = Files.createTempFile(temporaryDirectory, "bundle-", ".jar").toFile();
            try (Jar jar = new Jar(entry.getFileName().toString(), entry.toFile())) {
                jar.write(bundleFile);
            }
        } else {
            bundleFile = entry.toFile();
        }

        ResourceBuilder builder = new ResourceBuilder();
        return builder.addFile(bundleFile, bundleFile.toURI()) ? builder.build() : null;
    }

    private static Resource jakartaFrameworkCapabilities() {
        ResourceBuilder builder = new ResourceBuilder();
        builder.addCapability(new CapReqBuilder(IdentityNamespace.IDENTITY_NAMESPACE)
                .addAttribute(IdentityNamespace.IDENTITY_NAMESPACE, "swagger-test-jakarta-contracts")
                .addAttribute(IdentityNamespace.CAPABILITY_VERSION_ATTRIBUTE, Version.emptyVersion)
                .addAttribute(IdentityNamespace.CAPABILITY_TYPE_ATTRIBUTE, IdentityNamespace.TYPE_BUNDLE));
        builder.addCapability(contract("JakartaRESTfulWebServices", "3.1.0",
                "jakarta.ws.rs,jakarta.ws.rs.client,jakarta.ws.rs.container,jakarta.ws.rs.core,jakarta.ws.rs.ext,jakarta.ws.rs.sse"));
        builder.addCapability(contract("JakartaServlet", "6.0.0",
                "jakarta.servlet,jakarta.servlet.annotation,jakarta.servlet.descriptor,jakarta.servlet.http"));
        // A runtime component such as Aries SPI Fly supplies these extenders.
        // SLF4J 2 requires them before its exports can join the resolved graph.
        builder.addCapability(new CapReqBuilder("osgi.extender")
                .addAttribute("osgi.extender", "osgi.serviceloader.processor")
                .addAttribute("version", Version.parseVersion("1.0.0")));
        builder.addCapability(new CapReqBuilder("osgi.extender")
                .addAttribute("osgi.extender", "osgi.serviceloader.registrar")
                .addAttribute("version", Version.parseVersion("1.0.0")));
        return builder.build();
    }

    private static CapReqBuilder contract(String name, String version, String uses) {
        return new CapReqBuilder(CONTRACT_NAMESPACE)
                .addAttribute(CONTRACT_ATTRIBUTE, name)
                .addAttribute("version", List.of(Version.parseVersion(version)))
                .addDirective("uses", uses);
    }

    private static void assertPackageWire(List<Wire> wires, String packageName, String version) {
        Wire wire = findWire(wires, PackageNamespace.PACKAGE_NAMESPACE, PackageNamespace.PACKAGE_NAMESPACE, packageName);
        assertEquals(wire.getCapability().getAttributes().get(PackageNamespace.CAPABILITY_VERSION_ATTRIBUTE),
                Version.parseVersion(version), "Unexpected provider version for " + packageName);
    }

    private static void assertContractWire(List<Wire> wires, String contractName, String version) {
        Wire wire = findWire(wires, CONTRACT_NAMESPACE, CONTRACT_ATTRIBUTE, contractName);
        Object versions = wire.getCapability().getAttributes().get("version");
        assertTrue(versions instanceof List, "Contract version is not a typed OSGi version list: " + versions);
        assertTrue(((List<?>) versions).contains(Version.parseVersion(version)),
                "Contract does not provide version " + version + ": " + versions);
    }

    private static Wire findWire(List<Wire> wires, String namespace, String attribute, String value) {
        assertNotNull(wires, "Resolver returned no wires while looking for " + value);
        return wires.stream()
                .filter(wire -> namespace.equals(wire.getRequirement().getNamespace()))
                .filter(wire -> value.equals(String.valueOf(wire.getCapability().getAttributes().get(attribute))))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No resolved " + namespace + " wire for " + value));
    }

    private static String identity(Resource resource) {
        List<Capability> identities = resource.getCapabilities(IdentityNamespace.IDENTITY_NAMESPACE);
        if (identities.isEmpty()) {
            return null;
        }
        return String.valueOf(identities.get(0).getAttributes().get(IdentityNamespace.IDENTITY_NAMESPACE));
    }
}
