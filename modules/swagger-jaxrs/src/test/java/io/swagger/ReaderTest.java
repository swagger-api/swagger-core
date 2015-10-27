package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.*;
import io.swagger.resources.*;
import org.testng.annotations.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class ReaderTest {
    private static final String APPLICATION_XML = "application/xml";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html";

    @Test(description = "scan methods")
    public void scanMethods() {
        Method[] methods = SimpleMethods.class.getMethods();
        Reader reader = new Reader(new Swagger());
        for (Method method : methods) {
            if (isValidRestPath(method)) {
                Operation operation = reader.parseMethod(method);
                assertNotNull(operation);
            }
        }
    }

    @Test(description = "scan consumes and produces values with api class level annotations")
    public void scanConsumesProducesValuesWithApiClassLevelAnnotations() {
        Swagger swagger = getSwagger(ApiConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_XHTML_XML);
        assertEquals(getGet(swagger, "/{id}").getProduces().get(0), MediaType.APPLICATION_ATOM_XML);
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @Test(description = "scan consumes and produces values with rs class level annotations")
    public void scanConsumesProducesValuesWithRsClassLevelAnnotations() {
        Swagger swagger = getSwagger(RsConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes().get(0), "application/yaml");
        assertEquals(getGet(swagger, "/{id}").getProduces().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/split").getProduces(), Arrays.asList("image/jpeg",  "image/gif", "image/png"));
        assertEquals(getPut(swagger, "/split").getConsumes(), Arrays.asList("image/jpeg",  "image/gif", "image/png"));
    }

    @Test(description = "scan consumes and produces values with both class level annotations")
    public void scanConsumesProducesValuesWithBothClassLevelAnnotations() {
        Swagger swagger = getSwagger(BothConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_XHTML_XML);
        assertEquals(getGet(swagger, "/{id}").getProduces().get(0), MediaType.APPLICATION_ATOM_XML);
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getGet(swagger, "/{id}/{name}/value").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getGet(swagger, "/{id}/{name}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getGet(swagger, "/{id}/{type}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/{type}/value").getProduces().get(0), TEXT_HTML);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @Test(description = "scan consumes and produces values with no class level annotations")
    public void scanConsumesProducesValuesWithoutClassLevelAnnotations() {
        Swagger swagger = getSwagger(NoConsumesProducesResource.class);
        assertNull(getGet(swagger, "/{id}").getConsumes());
        assertNull(getGet(swagger, "/{id}").getProduces());
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @Test(description = "scan class level and field level annotations")
    public void scanClassAndFieldLevelAnnotations() {
        Swagger swagger = getSwagger(ResourceWithKnownInjections.class);

        List<Parameter> resourceParameters = getGet(swagger, "/resource/{id}").getParameters();
        assertNotNull(resourceParameters);
        assertEquals(resourceParameters.size(), 3);
        assertEquals(resourceParameters.get(0).getName(), "id");
        assertEquals(resourceParameters.get(1).getName(), "fieldParam");
        assertEquals(resourceParameters.get(2).getName(), "methodParam");

        List<Parameter> subResourceParameters = getGet(swagger, "/resource/{id}/subresource1").getParameters();
        assertNotNull(subResourceParameters);
        assertEquals(subResourceParameters.size(), 3);
        assertEquals(subResourceParameters.get(0).getName(), "id");
        assertEquals(subResourceParameters.get(1).getName(), "fieldParam");
        assertEquals(subResourceParameters.get(2).getName(), "subResourceParam");
    }

    private Boolean isValidRestPath(Method method) {
        for (Class<? extends Annotation> item : Arrays.asList(GET.class, PUT.class, POST.class, DELETE.class,
                OPTIONS.class, HEAD.class)) {
            if (method.getAnnotation(item) != null) {
                return true;
            }
        }
        return false;
    }

    @Test(description = "scan overridden method in descendantResource")
    public void scanOverriddenMethod() {
        Swagger swagger = getSwagger(DescendantResource.class);
        Operation overriddenMethodWithTypedParam = getGet(swagger, "/pet/{petId1}");
        assertNotNull(overriddenMethodWithTypedParam);
        assertEquals(overriddenMethodWithTypedParam.getParameters().get(0).getDescription(), "ID of pet to return child");

        Operation methodWithoutTypedParam = getGet(swagger, "/pet/{petId2}");
        assertNotNull(methodWithoutTypedParam);

        Operation overriddenMethodWithoutTypedParam = getGet(swagger, "/pet/{petId3}");
        assertNotNull(overriddenMethodWithoutTypedParam);

        Operation methodWithoutTypedParamFromDescendant = getGet(swagger, "/pet/{petId4}");
        assertNotNull(methodWithoutTypedParamFromDescendant);

        Operation methodFromInterface = getGet(swagger, "/pet/{petId5}");
        assertNotNull(methodFromInterface);
    }

    @Test(description = "scan annotation from interface, issue#1427")
    public void scanInterfaceTest() {
        final Swagger swagger = new Reader(new Swagger()).read(AnnotatedInterfaceImpl.class);
        assertNotNull(swagger);
        assertNotNull(swagger.getPath("/v1/users/{id}").getGet());
    }

    @Test(description = "scan implicit params")
    public void scanImplicitParam() {
        Swagger swagger = getSwagger(ResourceWithImplicitParams.class);

        List<Parameter> params = swagger.getPath("/testString").getPost().getParameters();
        assertNotNull(params);
        assertEquals(params.size(), 7);
        assertEquals(params.get(0).getName(), "sort");
        assertEquals(params.get(0).getIn(), "query");

        PathParameter pathParam = (PathParameter) params.get(1);
        assertEquals(pathParam.getName(), "type");
        assertEquals(pathParam.getIn(), "path");
        assertEquals(pathParam.getEnum().size(), 3);
        assertEquals(pathParam.getType(), "string");

        HeaderParameter headerParam = (HeaderParameter) params.get(2);
        assertEquals(headerParam.getName(), "size");
        assertEquals(headerParam.getIn(), "header");
        assertEquals(headerParam.getMinimum(), 1.0);

        FormParameter formParam = (FormParameter) params.get(3);
        assertEquals(formParam.getName(), "width");
        assertEquals(formParam.getIn(), "formData");
        assertEquals(formParam.getMaximum(), 1.0);

        assertEquals(params.get(4).getName(), "width");
        assertEquals(params.get(4).getIn(), "formData");

        QueryParameter queryParam = (QueryParameter) params.get(5);
        assertEquals(queryParam.getName(), "height");
        assertEquals(queryParam.getIn(), "query");
        assertEquals(queryParam.getMinimum(), 3.0);
        assertEquals(queryParam.getMaximum(), 4.0);

        BodyParameter bodyParam = (BodyParameter) params.get(6);
        assertEquals(bodyParam.getName(), "body");
        assertEquals(bodyParam.getIn(), "body");
        assertTrue(bodyParam.getRequired());
    }

    @Test(description = "scan implicit params with file objct")
    public void scanImplicitWithFile() {
        Swagger swagger = getSwagger(ResourceWithImplicitFileParam.class);
        Parameter param = swagger.getPath("/testString").getPost().getParameters().get(0);
        assertTrue(param instanceof FormParameter);
        FormParameter fp = (FormParameter) param;
        assertEquals("file", fp.getType());
    }

    @Test(description = "scan Deprecated annotation")
    public void scanDeprecatedAnnotation() {
        Swagger swagger = getSwagger(ResourceWithDeprecatedMethod.class);
        assertTrue(getGet(swagger, "/testDeprecated").isDeprecated());
        assertNull(getGet(swagger, "/testAllowed").isDeprecated());
    }

    @Test(description = "scan empty path annotation")
    public void scanEmptyPathAnnotation() {
        Swagger swagger = getSwagger(ResourceWithEmptyPath.class);
        assertNotNull(getGet(swagger, "/"));
    }

    @Test(description = "it should scan parameters from base resource class")
    public void scanParametersFromBaseResource(){
        Swagger swagger = getSwagger(BookResource.class);
        assertNotNull(swagger);

        List<Parameter> parameters =  getGet(swagger, "/{id}/v1/books/{name}").getParameters();
        assertEquals(parameters.size(), 4);

        Parameter description = parameters.get(0);
        assertTrue(description instanceof PathParameter);
        assertEquals(description.getName(), "description");
        assertEquals(description.getDescription(), "Overriden description");

        Parameter id = parameters.get(1);
        assertTrue(id instanceof PathParameter);
        assertEquals(id.getName(), "id");
        assertEquals(id.getDescription(), "The Identifier of entity");

        Parameter test = parameters.get(2);
        assertTrue(test instanceof QueryParameter);
        assertEquals(test.getName(), "test");
        assertEquals(test.getDescription(), "Test Query Param");

        Parameter name = parameters.get(3);
        assertTrue(name instanceof PathParameter);
        assertEquals(name.getName(), "name");
        assertEquals(name.getDescription(), "The books name");
    }

    private Swagger getSwagger(Class<?> cls) {
        return new Reader(new Swagger()).read(cls);
    }

    private Operation getGet(Swagger swagger, String path) {
        return swagger.getPath(path).getGet();
    }

    private Operation getPut(Swagger swagger, String path) {
        return swagger.getPath(path).getPut();
    }
}
