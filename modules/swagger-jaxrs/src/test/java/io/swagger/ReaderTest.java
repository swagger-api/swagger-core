package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.resources.AnnotatedInterfaceImpl;
import io.swagger.resources.ApiConsumesProducesResource;
import io.swagger.resources.ApiMultipleConsumesProducesResource;
import io.swagger.resources.BookResource;
import io.swagger.resources.BothConsumesProducesResource;
import io.swagger.resources.DescendantResource;
import io.swagger.resources.IndirectImplicitParamsImpl;
import io.swagger.resources.NoConsumesProducesResource;
import io.swagger.resources.Resource1970;
import io.swagger.resources.ResourceWithAnnotationsOnlyInInterfaceImpl;
import io.swagger.resources.ResourceWithClassLevelApiResourceNoMethodLevelApiResources;
import io.swagger.resources.ResourceWithCustomException;
import io.swagger.resources.ResourceWithCustomExceptionAndClassLevelApiResource;
import io.swagger.resources.ResourceWithDeprecatedMethod;
import io.swagger.resources.ResourceWithEmptyPath;
import io.swagger.resources.ResourceWithExternalDocs;
import io.swagger.resources.ResourceWithImplicitFileParam;
import io.swagger.resources.ResourceWithImplicitParams;
import io.swagger.resources.ResourceWithKnownInjections;
import io.swagger.resources.ResourceWithValidation;
import io.swagger.resources.RsConsumesProducesResource;
import io.swagger.resources.RsMultipleConsumesProducesResource;
import io.swagger.resources.SimpleMethods;
import org.testng.annotations.Test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ReaderTest {
    private static final String APPLICATION_XML = "application/xml";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_XML = "text/xml";
    private static final String TEXT_JSON = "text/json";
    private static final String CHARSET8 = ";charset=UTF-8";
    private static final String TEXT_XML_CHARSET = TEXT_XML + CHARSET8;
    private static final String TEXT_HTML_CHARSET = TEXT_HTML + CHARSET8;

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
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(1), TEXT_HTML_CHARSET);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(1), TEXT_XML_CHARSET);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(1), TEXT_HTML_CHARSET);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(1), TEXT_XML_CHARSET);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @Test(description = "scan consumes and produces values with api class level annotations")
    public void scanMultipleConsumesProducesValuesWithApiClassLevelAnnotations() {
        Swagger swagger = getSwagger(ApiMultipleConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes(), Arrays.asList(MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON));
        assertEquals(getGet(swagger, "/{id}").getProduces(), Arrays.asList(MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));

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

    @Test(description = "scan multiple consumes and produces values with rs class level annotations")
    public void scanMultipleConsumesProducesValuesWithRsClassLevelAnnotations() {
        Swagger swagger = getSwagger(RsMultipleConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes(), Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
        assertEquals(getGet(swagger, "/{id}").getProduces(), Arrays.asList(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON));
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

    @Test(description = "scan indirect implicit params from interface")
    public void scanImplicitParamInterfaceTest() {
        final Swagger swagger = new Reader(new Swagger()).read(IndirectImplicitParamsImpl.class);
        assertNotNull(swagger);
        assertEquals(swagger.getPath("/v1/users/{id}").getGet().getParameters().size(), 2);
    }

    @Test(description = "scan indirect implicit params from overridden method")
    public void scanImplicitParamOverriddenMethodTest() {
        final Swagger swagger = new Reader(new Swagger()).read(IndirectImplicitParamsImpl.class);
        assertNotNull(swagger);
        assertEquals(swagger.getPath("/v1/users").getPost().getParameters().size(), 2);
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
        assertEquals(headerParam.getMinimum(), new BigDecimal(1.0));

        FormParameter formParam = (FormParameter) params.get(3);
        assertEquals(formParam.getName(), "width");
        assertEquals(formParam.getIn(), "formData");
        assertEquals(formParam.getMaximum(), new BigDecimal(1.0));

        assertEquals(params.get(4).getName(), "width");
        assertEquals(params.get(4).getIn(), "formData");

        QueryParameter queryParam = (QueryParameter) params.get(5);
        assertEquals(queryParam.getName(), "height");
        assertEquals(queryParam.getIn(), "query");
        assertEquals(queryParam.getMinimum(), new BigDecimal(3.0));
        assertEquals(queryParam.getMaximum(), new BigDecimal(4.0));

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
        assertEquals(description.getDescription(), "Overridden description");

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

    @Test(description = "it should scan parameters with Swagger and JSR-303 bean validation annotations")
    public void scanBeanValidation(){

        Swagger swagger = getSwagger(ResourceWithValidation.class);
        assertNotNull(swagger);

        QueryParameter par = (QueryParameter) swagger.getPaths().get("/303").getOperations().get(0).getParameters().get(0);
        assertTrue(par.getRequired());
        assertEquals(par.getMinimum(), new BigDecimal(10));

        par = (QueryParameter) swagger.getPaths().get("/swagger-and-303").getOperations().get(0).getParameters().get(0);
        assertTrue(par.getRequired());
        assertEquals(par.getMinimum(), new BigDecimal(7));

        par = (QueryParameter) swagger.getPaths().get("/swagger").getOperations().get(0).getParameters().get(0);
        assertTrue(par.getRequired());
        assertEquals(par.getMinimum(), new BigDecimal(7));
    }

    @Test(description = "scan resource with annotated exception")
    public void scanDeclaredExceptions() {
        Swagger swagger = getSwagger(ResourceWithCustomException.class);
        assertNotNull(swagger);

        Operation operation = getGet(swagger, "/{id}");
        assertEquals(operation.getResponses().size(), 3);
        assertTrue(operation.getResponses().containsKey("200"));
        assertTrue(operation.getResponses().containsKey("400"));
        assertTrue(operation.getResponses().containsKey("404"));
    }

    @Test(description = "scan resource with annotated exception")
    public void scanDeclaredExceptionsAndCombineWithMethodResponses() {
        Swagger swagger = getSwagger(ResourceWithCustomException.class);
        assertNotNull(swagger);

        Operation operation = getPut(swagger, "/{id}");
        assertEquals(operation.getResponses().size(), 4);
        assertTrue(operation.getResponses().containsKey("200"));
        assertTrue(operation.getResponses().containsKey("400"));
        assertTrue(operation.getResponses().containsKey("404"));
        assertTrue(operation.getResponses().containsKey("409"));
    }

    @Test(description = "scan resource with annotated exception")
    public void scanDeclaredExceptionsAndCombineWithMethodResponsesClassLevel() {
        Swagger swagger = getSwagger(ResourceWithCustomExceptionAndClassLevelApiResource.class);
        assertNotNull(swagger);

        Operation operation = getPut(swagger, "/{id}");
        assertEquals(operation.getResponses().size(), 5);
        assertTrue(operation.getResponses().containsKey("200"));
        assertTrue(operation.getResponses().containsKey("400"));
        assertTrue(operation.getResponses().containsKey("404"));
        assertTrue(operation.getResponses().containsKey("403"));
        assertTrue(operation.getResponses().containsKey("409"));
        assertEquals(operation.getResponses().get("409").getDescription(), "Conflict");

        swagger = getSwagger(ResourceWithClassLevelApiResourceNoMethodLevelApiResources.class);
        assertNotNull(swagger);
        operation = getPut(swagger, "/{id}");
        assertEquals(operation.getResponses().size(), 2);
        assertTrue(operation.getResponses().containsKey("403"));
        assertTrue(operation.getResponses().containsKey("409"));


    }

    @Test(description = "scan resource (impl) which has the Api annotations only declared in its interface")
    public void scanApiAnnotationWhichAreOnlyPresentInInterfaceAndNotInImplementation() {
        Swagger swagger = getSwagger(ResourceWithAnnotationsOnlyInInterfaceImpl.class);
        assertNotNull(swagger);

        final List<Tag> tags = swagger.getTags();
        assertEquals(tags.size(), 1);
        assertEquals(tags.get(0).getName(), "someTag");
    }

    @Test(description = "scan resource (impl) which has the ApiParam annotations only declared in its interface")
    public void scanApiImplicitParamAnnotationWhichAreOnlyPresentInInterfaceAndNotInImplementation() {
        Swagger swagger = getSwagger(ResourceWithAnnotationsOnlyInInterfaceImpl.class);
        assertNotNull(swagger);

        List<Parameter> parameters = getGet(swagger, "/pet/randomPet").getParameters();
        assertNotNull(parameters);
        assertEquals(parameters.size(), 1);
        assertEquals(parameters.get(0).getName(), "petImplicitIdParam");
    }

    @Test(description = "scan resource per #1970")
    public void scanBigDecimal() {
        Swagger swagger = getSwagger(Resource1970.class);
        assertNotNull(swagger);

        PathParameter parameter = (PathParameter)swagger.getPath("/v1/{param1}").getGet().getParameters().get(0);
        assertEquals(parameter.getType(), "number");
    }

    @Test(description = "scan external docs on method")
    public void scanExternalDocsOnMethod() {
        Swagger swagger = getSwagger(ResourceWithExternalDocs.class);

        ExternalDocs externalDocsForGet = swagger.getPath("/testString").getGet().getExternalDocs();
        assertNull(externalDocsForGet);

        ExternalDocs externalDocsForPost = swagger.getPath("/testString").getPost().getExternalDocs();
        assertNotNull(externalDocsForPost);
        assertEquals("Test Description", externalDocsForPost.getDescription());
        assertEquals("https://swagger.io/", externalDocsForPost.getUrl());
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
