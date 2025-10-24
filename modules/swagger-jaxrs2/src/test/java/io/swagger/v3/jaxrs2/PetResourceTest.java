package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.petstore.EmptyPetResource;
import io.swagger.v3.jaxrs2.petstore.WebHookResource;
import io.swagger.v3.jaxrs2.petstore.callback.ComplexCallback31Resource;
import io.swagger.v3.jaxrs2.petstore.callback.ComplexCallbackResource;
import io.swagger.v3.jaxrs2.petstore.callback.MultipleCallbacksTestWithOperationResource;
import io.swagger.v3.jaxrs2.petstore.callback.RepeatableCallbackResource;
import io.swagger.v3.jaxrs2.petstore.callback.SimpleCallbackWithOperationResource;
import io.swagger.v3.jaxrs2.petstore.example.ExamplesResource;
import io.swagger.v3.jaxrs2.petstore.link.LinksAndContent31Resource;
import io.swagger.v3.jaxrs2.petstore.link.LinksResource;
import io.swagger.v3.jaxrs2.petstore.openapidefintion.OpenAPI31DefinitionResource;
import io.swagger.v3.jaxrs2.petstore.openapidefintion.OpenAPIDefinitionResource;
import io.swagger.v3.jaxrs2.petstore.operation.AnnotatedSameNameOperationResource;
import io.swagger.v3.jaxrs2.petstore.operation.ExternalDocumentationResource;
import io.swagger.v3.jaxrs2.petstore.operation.FullyAnnotatedOperationResource;
import io.swagger.v3.jaxrs2.petstore.operation.HiddenOperationResource;
import io.swagger.v3.jaxrs2.petstore.operation.NotAnnotatedSameNameOperationResource;
import io.swagger.v3.jaxrs2.petstore.operation.OperationResource;
import io.swagger.v3.jaxrs2.petstore.operation.OperationWithoutAnnotationResource;
import io.swagger.v3.jaxrs2.petstore.operation.ServerOperationResource;
import io.swagger.v3.jaxrs2.petstore.operation.SubResource;
import io.swagger.v3.jaxrs2.petstore.parameter.ArraySchemaResource;
import io.swagger.v3.jaxrs2.petstore.parameter.ComplexParameterResource;
import io.swagger.v3.jaxrs2.petstore.parameter.ComplexParameterWithOperationResource;
import io.swagger.v3.jaxrs2.petstore.parameter.MultipleNotAnnotatedParameter;
import io.swagger.v3.jaxrs2.petstore.parameter.OpenAPIJaxRSAnnotatedParameter;
import io.swagger.v3.jaxrs2.petstore.parameter.OpenAPIWithContentJaxRSAnnotatedParameter;
import io.swagger.v3.jaxrs2.petstore.parameter.OpenAPIWithImplementationJaxRSAnnotatedParameter;
import io.swagger.v3.jaxrs2.petstore.parameter.Parameters31Resource;
import io.swagger.v3.jaxrs2.petstore.parameter.ParametersResource;
import io.swagger.v3.jaxrs2.petstore.parameter.RepeatableParametersResource;
import io.swagger.v3.jaxrs2.petstore.parameter.SingleJaxRSAnnotatedParameter;
import io.swagger.v3.jaxrs2.petstore.parameter.SingleNotAnnotatedParameter;
import io.swagger.v3.jaxrs2.petstore.requestbody.RequestBody31Resource;
import io.swagger.v3.jaxrs2.petstore.requestbody.RequestBodyMethodPriorityResource;
import io.swagger.v3.jaxrs2.petstore.requestbody.RequestBodyParameterPriorityResource;
import io.swagger.v3.jaxrs2.petstore.requestbody.RequestBodyResource;
import io.swagger.v3.jaxrs2.petstore.responses.ComplexResponseResource;
import io.swagger.v3.jaxrs2.petstore.responses.ImplementationResponseResource;
import io.swagger.v3.jaxrs2.petstore.responses.MethodResponseResource;
import io.swagger.v3.jaxrs2.petstore.responses.NoImplementationResponseResource;
import io.swagger.v3.jaxrs2.petstore.responses.NoResponseResource;
import io.swagger.v3.jaxrs2.petstore.responses.OperationResponseResource;
import io.swagger.v3.jaxrs2.petstore.responses.PriorityResponseResource;
import io.swagger.v3.jaxrs2.petstore.security.SecurityResource;
import io.swagger.v3.jaxrs2.petstore.tags.CompleteTagResource;
import io.swagger.v3.jaxrs2.petstore.tags.TagClassResource;
import io.swagger.v3.jaxrs2.petstore.tags.TagMethodResource;
import io.swagger.v3.jaxrs2.petstore.tags.TagOpenAPIDefinitionResource;
import io.swagger.v3.jaxrs2.petstore.tags.TagOperationResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

/**
 * Pet Resource Test Class
 * Adding a lot of tests of different pet resource examples
 */
public class PetResourceTest extends AbstractAnnotationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PetResourceTest.class);
    private static final String PETSTORE_SOURCE = "petstore/";
    private static final String TAGS_SOURCE = "petstore/tags/";
    private static final String OPERATIONS_SOURCE = "petstore/operation/";
    private static final String CALLBACKS_SOURCE = "petstore/callbacks/";
    private static final String RESPONSES_SOURCE = "petstore/responses/";
    private static final String PARAMETERS_SOURCE = "petstore/parameters/";
    private static final String LINKS_SOURCE = "petstore/links/";
    private static final String EXAMPLES_SOURCE = "petstore/example/";
    private static final String REQUEST_BODIES_SOURCE = "petstore/requestbody/";
    private static final String YAML_EXTENSION = ".yaml";
    private static final String PETSTORE_PACKAGE = "io.swagger.v3.jaxrs2.petstore";
    private static final char DOT = '.';
    private static final char SLASH = '/';

    @Test(description = "Test an empty resource class (Without operations or annotations)")
    public void testEmptyPetResource() {
        compare(EmptyPetResource.class, PETSTORE_SOURCE);
    }

    @Test(description = "Test a resource with examples)")
    public void testExamplesResource() {
        compare(ExamplesResource.class, EXAMPLES_SOURCE);
    }

    @Test(description = "Test a resource with Links)")
    public void testLinksResource() {
        compare(LinksResource.class, LINKS_SOURCE);
    }

    @Test(description = "Test some resources with Callbacks)")
    public void testCallBacksResources() {
        compare(SimpleCallbackWithOperationResource.class, CALLBACKS_SOURCE);
        compare(MultipleCallbacksTestWithOperationResource.class, CALLBACKS_SOURCE);
        compare(RepeatableCallbackResource.class, CALLBACKS_SOURCE);
        compare(ComplexCallbackResource.class, CALLBACKS_SOURCE);
    }

    @Test(description = "Test some resources with different Operations scenarios)")
    public void testOperationsResources() {
        compare(HiddenOperationResource.class, OPERATIONS_SOURCE);
        compare(OperationWithoutAnnotationResource.class, OPERATIONS_SOURCE);
        compare(FullyAnnotatedOperationResource.class, OPERATIONS_SOURCE);
        compare(AnnotatedSameNameOperationResource.class, OPERATIONS_SOURCE);
        compare(NotAnnotatedSameNameOperationResource.class, OPERATIONS_SOURCE);
        compare(ExternalDocumentationResource.class, OPERATIONS_SOURCE);
        compare(ServerOperationResource.class, OPERATIONS_SOURCE);
        compare(SubResource.class, OPERATIONS_SOURCE);
        compare(OperationResource.class, OPERATIONS_SOURCE);
    }

    @Test(description = "Test OpenAPIDefinition resource)")
    public void testOpenAPIDefinitionResource() {
        compare(OpenAPIDefinitionResource.class, PETSTORE_SOURCE);
    }

    @Test(description = "Test RequestBody resource)")
    public void tetRequestBodyResource() {
        compare(RequestBodyResource.class, REQUEST_BODIES_SOURCE);
        compare(RequestBodyParameterPriorityResource.class, REQUEST_BODIES_SOURCE);
        compare(RequestBodyMethodPriorityResource.class, REQUEST_BODIES_SOURCE);
    }

    @Test(description = "Test Parameters resources)")
    public void testParametersResource() {
        compare(ParametersResource.class, PARAMETERS_SOURCE);
        compare(RepeatableParametersResource.class, PARAMETERS_SOURCE);
        compare(ArraySchemaResource.class, PARAMETERS_SOURCE);
        compare(SingleNotAnnotatedParameter.class, PARAMETERS_SOURCE);
        compare(MultipleNotAnnotatedParameter.class, PARAMETERS_SOURCE);
        compare(SingleJaxRSAnnotatedParameter.class, PARAMETERS_SOURCE);
        compare(OpenAPIJaxRSAnnotatedParameter.class, PARAMETERS_SOURCE);
        compare(OpenAPIWithContentJaxRSAnnotatedParameter.class, PARAMETERS_SOURCE);
        compare(OpenAPIWithImplementationJaxRSAnnotatedParameter.class, PARAMETERS_SOURCE);
        compare(ComplexParameterResource.class, PARAMETERS_SOURCE);
        compare(ComplexParameterWithOperationResource.class, PARAMETERS_SOURCE);
    }

    @Test(description = "Test ApiResponses resource)")
    public void testResponsesResource() {
        compare(MethodResponseResource.class, RESPONSES_SOURCE);
        compare(OperationResponseResource.class, RESPONSES_SOURCE);
        compare(NoResponseResource.class, RESPONSES_SOURCE);
        compare(ImplementationResponseResource.class, RESPONSES_SOURCE);
        compare(NoImplementationResponseResource.class, RESPONSES_SOURCE);
        compare(PriorityResponseResource.class, RESPONSES_SOURCE);
        compare(ComplexResponseResource.class, RESPONSES_SOURCE);
    }

    @Test(description = "Test Security resource)")
    public void testSecurityResource() {
        compare(SecurityResource.class, PETSTORE_SOURCE);
    }

    @Test(description = "Test Tags resource)")
    public void testTagsResource() {
        compare(CompleteTagResource.class, TAGS_SOURCE);
        compare(TagOpenAPIDefinitionResource.class, TAGS_SOURCE);
        compare(TagClassResource.class, TAGS_SOURCE);
        compare(TagMethodResource.class, TAGS_SOURCE);
        compare(TagOperationResource.class, TAGS_SOURCE);
    }

    @Test(description = "Test a full set of classes")
    public void testSetOfClasses() {
        final Reader reader = new Reader(new OpenAPI());
        final OpenAPI openAPI = reader.read(getSetOfClassesFromPackage(PETSTORE_PACKAGE));
        assertNotNull(openAPI);
        try {
            SerializationMatchers.assertEqualsToYaml(openAPI, getOpenAPIAsString(PETSTORE_SOURCE + "FullPetResource.yaml"));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Extract a set of classes from a package name
     *
     * @param packageName target to scan the classes
     * @return Set<Class>
     */
    private Set<Class<?>> getSetOfClassesFromPackage(final String packageName) {
        final Set<Class<?>> classSet = new HashSet<>();
        try {
            final Class[] classes = getClasses(packageName);
            for (final Class aClass : classes) {
                classSet.add(aClass);
            }
        } catch (final ClassNotFoundException | IOException e) {
            fail();
        }
        return classSet;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */
    private static Class[] getClasses(final String packageName)
            throws ClassNotFoundException, IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        final String path = packageName.replace(DOT, SLASH);
        final Enumeration<URL> resources = classLoader.getResources(path);
        final List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            final URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        final ArrayList<Class> classes = new ArrayList<>();
        for (final File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirectories.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class> findClasses(final File directory, final String packageName) throws ClassNotFoundException {
        final List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        final File[] files = directory.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    @Test(description = "Test an empty resource class (Without operations or annotations)")
    public void testEmptyPet31Resource() {
        Reader reader = new Reader(new SwaggerConfiguration()
                .openAPI(new OpenAPI())
                .openAPI31(true));
        OpenAPI openAPI = reader.read(Object.class);
        SerializationMatchers.assertEqualsToYaml31(openAPI, "openapi: 3.1.0");
    }

    @Test(description = "Test a resource with Links and Content)")
    public void testLinksAndContent31Resource() {
        compare(LinksAndContent31Resource.class, LINKS_SOURCE, true);
    }

    @Test(description = "Test OpenAPIDefinition resource)")
    public void testOpenAPI31DefinitionResource() {
        compare(OpenAPI31DefinitionResource.class, PETSTORE_SOURCE, true);
    }

    @Test(description = "Test Parameters resources)")
    public void testParameters31Resource() {
        compare(Parameters31Resource.class, PARAMETERS_SOURCE, true);
    }

    @Test(description = "Test some resources with Callbacks)")
    public void testCallBacks31Resources() {
        compare(ComplexCallback31Resource.class, CALLBACKS_SOURCE, true);
    }

    @Test(description = "Test some resources with Request Body)")
    public void testRequestBody31Resources() {
        compare(RequestBody31Resource.class, REQUEST_BODIES_SOURCE, true);
    }

    @Test(description = "Test webhook resources")
    public void testWebhooksResource() {
        compare(WebHookResource.class, PETSTORE_SOURCE, true);
    }

    /**
     * Compare a class that were read and parsed to a yaml against a yaml file.
     *
     * @param clazz  to read.
     * @param source where is the yaml.
     */
    private void compare(final Class clazz, final String source) {
        compare(clazz, source, false);
    }

    private void compare(final Class clazz, final String source, boolean openapi31) {
        final String file = source + clazz.getSimpleName() + YAML_EXTENSION;
        try {
            if (openapi31) {
                compareAsYamlOAS31(clazz, getOpenAPIAsString(file));
            } else {
                compareAsYaml(clazz, getOpenAPIAsString(file));
            }
        } catch (IOException e) {
            LOGGER.error("Failed to compare class {} with YAML resource {}", clazz.getName(), file, e);
            fail();
        }
    }
}
