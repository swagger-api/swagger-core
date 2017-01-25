package io.swagger;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.TestEnum;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.SerializableParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.resources.ClassWithExamplePost;
import io.swagger.resources.ClassWithExamplePostClass;
import io.swagger.resources.HiddenResource;
import io.swagger.resources.Issue1979Resource;
import io.swagger.resources.NicknamedOperation;
import io.swagger.resources.NotValidRootResource;
import io.swagger.resources.Resource1041;
import io.swagger.resources.Resource1073;
import io.swagger.resources.Resource1085;
import io.swagger.resources.Resource653;
import io.swagger.resources.Resource841;
import io.swagger.resources.Resource877;
import io.swagger.resources.Resource937;
import io.swagger.resources.ResourceWithApiOperationCode;
import io.swagger.resources.ResourceWithApiResponseResponseContainer;
import io.swagger.resources.ResourceWithBodyParams;
import io.swagger.resources.ResourceWithCustomHTTPMethodAnnotations;
import io.swagger.resources.ResourceWithEmptyModel;
import io.swagger.resources.ResourceWithEnums;
import io.swagger.resources.ResourceWithInnerClass;
import io.swagger.resources.ResourceWithMapReturnValue;
import io.swagger.resources.ResourceWithRanges;
import io.swagger.resources.ResourceWithResponse;
import io.swagger.resources.ResourceWithResponseHeaders;
import io.swagger.resources.ResourceWithTypedResponses;
import io.swagger.resources.ResourceWithVoidReturns;
import io.swagger.resources.SimpleResource;
import io.swagger.resources.SimpleResourceWithoutAnnotations;
import io.swagger.resources.SimpleSelfReferencingSubResource;
import io.swagger.resources.TaggedResource;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class SimpleReaderTest {

    private Swagger getSwagger(Class<?> cls) {
        return new Reader(new Swagger()).read(cls);
    }

    private Map<String, Response> getGetResponses(Swagger swagger, String path) {
        return getGet(swagger, path).getResponses();
    }

    private Map<String, Response> getPutResponses(Swagger swagger, String path) {
        return getPut(swagger, path).getResponses();
    }

    private List<Parameter> getGetParameters(Swagger swagger, String path) {
        return getGet(swagger, path).getParameters();
    }

    private List<Parameter> getPostParameters(Swagger swagger, String path) {
        return getPost(swagger, path).getParameters();
    }

    private List<Parameter> getPutParameters(Swagger swagger, String path) {
        return getPut(swagger, path).getParameters();
    }

    private Operation getGet(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getGet();
    }

    private Operation getPost(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getPost();
    }

    private Operation getPut(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getPut();
    }

    private Operation getPatch(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getPatch();
    }

    private Operation getDelete(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getDelete();
    }

    @Test(description = "scan a simple resource")
    public void scanSimpleResource() {
        Swagger swagger = getSwagger(SimpleResource.class);
        assertEquals(swagger.getPaths().size(), 3);

        Operation get = getGet(swagger, "/{id}");
        assertNotNull(get);
        assertEquals(get.getParameters().size(), 2);

        PathParameter param1 = (PathParameter) get.getParameters().get(0);
        assertEquals(param1.getIn(), "path");
        assertEquals(param1.getName(), "id");
        assertTrue(param1.getRequired());
        assertEquals(param1.getDescription(), "sample param data");
        assertEquals(param1.getDefaultValue(), "5");

        Parameter param2 = get.getParameters().get(1);
        assertEquals(param2.getIn(), "query");
        assertEquals(param2.getName(), "limit");
        assertFalse(param2.getRequired());
        assertNull(param2.getDescription());

        List<Parameter> params = getPutParameters(swagger, "/{bodyparams}");
        BodyParameter bodyParam1 = (BodyParameter) params.get(0);
        assertEquals(bodyParam1.getIn(), "body");
        assertEquals(bodyParam1.getName(), "body");
        assertTrue(bodyParam1.getRequired());

        BodyParameter bodyParam2 = (BodyParameter) params.get(1);
        assertEquals(bodyParam2.getIn(), "body");
        assertEquals(bodyParam2.getName(), "body");
        assertFalse(bodyParam2.getRequired());
    }

    @Test(description = "scan a resource with custom http method annotations")
    public void scanResourceWithCustomHttpMethodAnnotations() {
        Swagger swagger = getSwagger(ResourceWithCustomHTTPMethodAnnotations.class);

        Operation get = getGet(swagger, "/");
        assertNotNull(get);

        Operation post = getPost(swagger, "/");
        assertNotNull(post);

        Operation patch = getPatch(swagger, "/");
        assertNotNull(patch);

        Operation put = getPut(swagger, "/");
        assertNotNull(put);

        Operation delete = getDelete(swagger, "/");
        assertNotNull(delete);
    }

    @Test(description = "scan a resource with void return type")
    public void scanResourceWithVoidReturnType() {
        Swagger swagger = getSwagger(ResourceWithVoidReturns.class);
        assertEquals(swagger.getDefinitions().size(), 1);
        assertNotNull(swagger.getDefinitions().get("NotFoundModel"));
    }

    @Test(description = "scan a resource with map return type")
    public void scanResourceWithMapReturnType() {
        Swagger swagger = getSwagger(ResourceWithMapReturnValue.class);

        Operation get = getGet(swagger, "/{id}");
        assertNotNull(get);
        assertNotNull(get.getResponses());

        Response response = get.getResponses().get("200");
        assertNotNull(response);

        Property schema = response.getSchema();
        assertEquals(schema.getClass(), MapProperty.class);
    }

    @Test(description = "scan a resource with generics per 653")
    public void scanResourceWithGenerics() {
        Swagger swagger = getSwagger(Resource653.class);
        Operation get = getGet(swagger, "/external/info");
        assertNotNull(get);

        Map<String, Response> responses = get.getResponses();
        assertNotNull(responses);

        Response response = responses.get("default");
        assertNotNull(response);
        assertNull(response.getSchema());
    }

    @Test(description = "scan a resource with javax.ws.core.Response ")
    public void scanResourceWithResponse() {
        assertNull(getSwagger(ResourceWithResponse.class).getDefinitions());
    }

    @Test(description = "scan a resource with Response.Status return type per 877")
    public void scanResourceWithResponseStatusReturnType() {
        Swagger swagger = getSwagger(Resource877.class);

        assertNotNull(swagger.getTags());
        assertEquals(swagger.getTags().size(), 1);

        Tag tag = swagger.getTags().get(0);
        assertEquals(tag.getName(), "externalinfo");
        assertNull(tag.getDescription());
        assertNull(tag.getExternalDocs());
    }

    @Test(description = "scan a resource with tags")
    public void scanResourceWithApiTags() {
        assertEquals(getSwagger(TaggedResource.class).getTags().size(), 2);
    }

    @Test(description = "scan a resource with tags in test 841")
    public void scanResourceWithApiOperationTags() {
        Swagger swagger = getSwagger(Resource841.class);
        assertEquals(swagger.getTags().size(), 3);

        List<String> rootTags = getGet(swagger, "/fun").getTags();
        assertEquals(rootTags.size(), 2);
        assertEquals(rootTags, Arrays.asList("tag1", "tag2"));

        List<String> thisTags = getGet(swagger, "/fun/this").getTags();
        assertEquals(thisTags.size(), 1);
        assertEquals(thisTags, Arrays.asList("tag1"));

        List<String> thatTags = getGet(swagger, "/fun/that").getTags();
        assertEquals(thatTags.size(), 1);
        assertEquals(thatTags, Arrays.asList("tag2"));
    }

    @Test(description = "scan a resource with param enums")
    public void scanResourceWithParamEnums() {
        Swagger swagger = getSwagger(ResourceWithEnums.class);
        SerializableParameter param = (SerializableParameter) getGetParameters(swagger, "/{id}").get(2);
        List<String> _enum = param.getEnum();
        assertEquals(_enum, Arrays.asList("a", "b", "c", "d", "e"));

        List<Parameter> checkEnumHandling = getGetParameters(swagger, "/checkEnumHandling/{v0}");
        List<String> allEnumValues = Lists.newArrayList(Collections2.transform(Arrays.asList(TestEnum.values()), Functions.toStringFunction()));
        SerializableParameter v0 = (SerializableParameter) checkEnumHandling.get(0);
        assertEquals(v0.getEnum(), allEnumValues);
        SerializableParameter v1 = (SerializableParameter) checkEnumHandling.get(1);
        assertEquals(((StringProperty) v1.getItems()).getEnum(), allEnumValues);
        SerializableParameter v2 = (SerializableParameter) checkEnumHandling.get(2);
        assertEquals(((StringProperty) v2.getItems()).getEnum(), allEnumValues);
        SerializableParameter v3 = (SerializableParameter) checkEnumHandling.get(3);
        assertEquals(v3.getEnum(), Arrays.asList("A", "B", "C"));
    }

    @Test(description = "scan a resource with param range")
    public void scanResourceWithParamRange() {
        Swagger swagger = getSwagger(ResourceWithRanges.class);
        List<Parameter> params = getGetParameters(swagger, "/{id}");

        PathParameter param0 = (PathParameter) params.get(0);
        assertEquals(param0.getName(), "id");
        assertEquals(param0.getDefaultValue(), 5);
        assertEquals(param0.getMinimum(), new BigDecimal(0.0));
        assertEquals(param0.getMaximum(), new BigDecimal(10.0));

        PathParameter param1 = (PathParameter) params.get(1);
        assertEquals(param1.getName(), "minValue");
        assertEquals(param1.getMinimum(), new BigDecimal(0.0));
        assertNull(param1.getMaximum(), null);

        PathParameter param2 = (PathParameter) params.get(2);
        assertEquals(param2.getName(), "maxValue");
        assertNull(param2.getMinimum());
        assertEquals(param2.getMaximum(), new BigDecimal(100.0));

        PathParameter param3 = (PathParameter) params.get(3);
        assertEquals(param3.getName(), "values");
        IntegerProperty items = (IntegerProperty) param3.getItems();
        assertEquals(items.getMinimum(), new BigDecimal(0.0));
        assertEquals(items.getMaximum(), new BigDecimal(5.0));
        assertEquals(items.getExclusiveMinimum(), Boolean.TRUE);
        assertEquals(items.getExclusiveMaximum(), Boolean.TRUE);
    }

    @Test(description = "scan a resource with response headers")
    public void scanResourceWithResponseHeaders() {
        Swagger swagger = getSwagger(ResourceWithResponseHeaders.class);
        Map<String, Response> responses = getGetResponses(swagger, "/{id}");
        Map<String, Property> headers200 = responses.get("200").getHeaders();
        assertEquals(headers200.size(), 1);
        assertEquals(headers200.get("foo").getDescription(), "description");
        assertEquals(headers200.get("foo").getType(), "string");

        Map<String, Property> headers400 = responses.get("400").getHeaders();
        assertEquals(headers400.size(), 2);
        assertEquals(headers400.get("X-Rack-Cache").getDescription(), "Explains whether or not a cache was used");
        assertEquals(headers400.get("X-Rack-Cache").getType(), "boolean");

        Iterator<String> keyItr = headers400.keySet().iterator();
        assertEquals(keyItr.next(), "X-Rack-Cache");
        assertEquals(keyItr.next(), "X-After-Rack-Cache");
    }

    @Test(description = "not scan a hidden resource")
    public void notScanHiddenResource() {
        assertNull(getSwagger(HiddenResource.class).getPaths());
    }

    @Test(description = "not scan a resource without @Api annotation")
    public void notScanNotValidRootResourcee() {
        assertNull(getSwagger(NotValidRootResource.class).getPaths());
    }

    @Test(description = "correctly model an empty model per 499")
    public void scanResourceWithEmptyModel() {
        Map<String, Model> definitions = getSwagger(ResourceWithEmptyModel.class).getDefinitions();
        assertEquals(definitions.size(), 1);
        ModelImpl empty = (ModelImpl) definitions.get("EmptyModel");
        assertEquals(empty.getType(), "object");
        assertNull(empty.getProperties());
        assertNull(empty.getAdditionalProperties(), null);
    }

    @Test(description = "scan a simple resource without annotations")
    public void scanSimpleResourceWithoutAnnotations() {
        DefaultReaderConfig config = new DefaultReaderConfig();
        config.setScanAllResources(true);
        Swagger swagger = new Reader(new Swagger(), config).read(SimpleResourceWithoutAnnotations.class);
        assertEquals(swagger.getPaths().size(), 2);

        Operation get = getGet(swagger, "/{id}");
        assertNotNull(get);
        assertEquals(get.getParameters().size(), 2);

        PathParameter param1 = (PathParameter) get.getParameters().get(0);
        assertEquals(param1.getIn(), "path");
        assertEquals(param1.getName(), "id");
        assertTrue(param1.getRequired());
        assertNull(param1.getDescription());
        assertEquals(param1.getDefaultValue(), "5");

        Parameter param2 = get.getParameters().get(1);
        assertEquals(param2.getIn(), "query");
        assertEquals(param2.getName(), "limit");
        assertFalse(param2.getRequired());
        assertNull(param2.getDescription());
    }

    @Test(description = "scan a simple self-referencing subresource")
    public void scanSimpleSelfReferencingSubResource() {
        DefaultReaderConfig config = new DefaultReaderConfig();
        config.setScanAllResources(true);
        Swagger swagger = new Reader(new Swagger(), config).read(SimpleSelfReferencingSubResource.class);

        assertEquals(swagger.getPaths().size(), 4);

        // these two paths are directly reachable without passing thru a recursive reference
        Operation retrieve = getGet(swagger, "/sub");
        assertNotNull(retrieve);
        assertEquals(retrieve.getParameters().size(), 0);

        retrieve = getGet(swagger, "/sub/leaf");
        assertNotNull(retrieve);
        assertEquals(retrieve.getParameters().size(), 0);

        retrieve = getGet(swagger, "/sub/recurse2");
        assertNotNull(retrieve);
        assertEquals(retrieve.getParameters().size(), 0);

        retrieve = getGet(swagger, "/sub/recurse2/leaf");
        assertNotNull(retrieve);
        assertEquals(retrieve.getParameters().size(), 0);
    }

    @Test(description = "scan resource with ApiOperation.code() value")
    public void scanResourceWithApiOperationCodeValue() {
        Swagger swagger = getSwagger(ResourceWithApiOperationCode.class);
        Map<String, Response> responses1 = getGetResponses(swagger, "/{id}");
        assertEquals(responses1.size(), 3);
        assertTrue(responses1.containsKey("202"));
        assertFalse(responses1.containsKey("200"));
        assertEquals(responses1.get("202").getDescription(), "successful operation");

        Map<String, Response> responses2 = getPutResponses(swagger, "/{id}");
        assertEquals(responses2.size(), 3);
        assertTrue(responses2.containsKey("200"));
        assertEquals(responses2.get("200").getDescription(), "successful operation");
    }

    @Test(description = "scan resource with ApiResponse.responseContainer() value")
    public void scanResourceWithApiResponseResponseContainerValue() {
        Swagger swagger = getSwagger(ResourceWithApiResponseResponseContainer.class);
        Path paths = swagger.getPaths().get("/{id}");
        Map<String, Response> responses1 = paths.getGet().getResponses();
        assertEquals(responses1.get("200").getSchema().getClass(), MapProperty.class);
        assertEquals(responses1.get("400").getSchema().getClass(), ArrayProperty.class);

        Map<String, Response> responses2 = paths.getPut().getResponses();
        assertEquals(responses2.get("201").getSchema().getClass(), RefProperty.class);
        assertEquals(responses2.get("401").getSchema().getClass(), ArrayProperty.class);

        Map<String, Response> responses3 = paths.getPost().getResponses();
        assertEquals(responses3.get("202").getSchema().getClass(), RefProperty.class);
        assertEquals(responses3.get("402").getSchema().getClass(), RefProperty.class);

        Map<String, Response> responses4 = paths.getDelete().getResponses();
        assertEquals(responses4.get("203").getSchema().getClass(), RefProperty.class);
        assertEquals(responses4.get("403").getSchema().getClass(), RefProperty.class);

        Path paths2 = swagger.getPaths().get("/{id}/name");
        Map<String, Response> responses5 = paths2.getGet().getResponses();
        assertEquals(responses5.get("203").getSchema().getClass(), ArrayProperty.class);
        assertNull(((ArrayProperty) responses5.get("203").getSchema()).getUniqueItems());
        assertNotEquals(responses5.get("203").getHeaders().get("foo").getClass(), MapProperty.class);
        assertEquals(responses5.get("403").getSchema().getClass(), ArrayProperty.class);
        assertEquals(((ArrayProperty) responses5.get("403").getSchema()).getUniqueItems(), Boolean.TRUE);

        Map<String, Response> responses6 = paths2.getPut().getResponses();
        assertEquals(responses6.get("203").getSchema().getClass(), ArrayProperty.class);
        assertEquals(((ArrayProperty) responses6.get("203").getSchema()).getUniqueItems(), Boolean.TRUE);
        assertEquals(responses6.get("203").getHeaders().get("foo").getClass(), ArrayProperty.class);
        assertEquals(((ArrayProperty) responses6.get("203").getHeaders().get("foo")).getUniqueItems(), Boolean.TRUE);
        assertEquals(responses6.get("403").getSchema().getClass(), ArrayProperty.class);
    }

    @Test(description = "scan a resource with inner class")
    public void scanResourceWithInnerClass() {
        Swagger swagger = getSwagger(ResourceWithInnerClass.class);
        assertEquals(((RefProperty) ((ArrayProperty) getGetResponses(swagger, "/description").get("200").getSchema()).
                getItems()).get$ref(), "#/definitions/Description");
        assertTrue(swagger.getDefinitions().containsKey("Description"));
    }

    @Test(description = "scan defaultValue and required per #937")
    public void scanDefaultValueAndRequiredOptions() {
        Swagger swagger = getSwagger(Resource937.class);
        QueryParameter param = (QueryParameter) getGetParameters(swagger, "/external/info").get(0);
        assertFalse(param.getRequired());
        assertEquals(param.getDefaultValue(), "dogs");
    }

    @Test(description = "scan a resource with all hidden values #1073")
    public void scanResourceWithAllHiddenValues() {
        assertNull(getSwagger(Resource1073.class).getPaths());
    }

    @Test(description = "scan a resource with body parameters")
    public void scanResourceWithBodyParameters() {
        Swagger swagger = getSwagger(ResourceWithBodyParams.class);
        BodyParameter param = (BodyParameter) getPostParameters(swagger, "/testShort").get(0);
        assertEquals(param.getDescription(), "a short input");

        ModelImpl schema = (ModelImpl) param.getSchema();
        assertEquals(schema.getType(), "integer");
        assertEquals(schema.getFormat(), "int32");

        assertEquals(swagger.getDefinitions().keySet(), Arrays.asList("Tag"));

        testString(swagger, "/testApiString", "input", "String parameter");
        testString(swagger, "/testString", "body", null);

        testObject(swagger, "/testApiObject", "input", "Object parameter");
        testObject(swagger, "/testObject", "body", null);

        List<Operation> operations = new ArrayList<Operation>();
        for (Path item : swagger.getPaths().values()) {
            Operation op = item.getPost();
            if (op.getOperationId().startsWith("testPrimitive")) {
                operations.add(op);
            }
        }
        assertEquals(operations.size(), 16);
        for (Operation item : operations) {
            assertEquals(item.getParameters().size(), 1);
        }
    }

    private Model testParam(Swagger swagger, String path, String name, String description) {
        BodyParameter param = (BodyParameter) getPostParameters(swagger, path).get(0);
        assertEquals(param.getIn(), "body");
        assertEquals(param.getName(), name);
        assertEquals(param.getDescription(), description);
        return param.getSchema();
    }

    private void testString(Swagger swagger, String path, String name, String description) {
        assertEquals(((ModelImpl) testParam(swagger, path, name, description)).getType(), "string");
    }

    private void testObject(Swagger swagger, String path, String name, String description) {
        assertEquals(((RefModel) testParam(swagger, path, name, description)).getSimpleRef(), "Tag");
    }

    @Test(description = "verify top-level path params per #1085")
    public void verifyTopLevelPathParams() {
        Swagger swagger = getSwagger(Resource1085.class);
        Parameter param = getGetParameters(swagger, "/external/info/{id}").get(0);
        assertEquals(param.getName(), "id");
        assertTrue(param instanceof PathParameter);
    }

    @Test(description = "verify top-level auth #1041")
    public void verifyTopLevelAuthorization() {
        Swagger swagger = getSwagger(Resource1041.class);
        Operation path1 = getGet(swagger, "/external/info/path1");
        List<Map<String, List<String>>> security1 = path1.getSecurity();
        assertEquals(security1.size(), 1);
        assertNotNull(security1.get(0).get("my_auth"));

        Operation path2 = getGet(swagger, "/external/info/path2");
        List<Map<String, List<String>>> security2 = path2.getSecurity();
        assertEquals(security2.size(), 1);
        assertNotNull(security2.get(0).get("your_auth"));
    }

    @Test(description = "check response models processing")
    public void checkResponseModelsProcessing() {
        Swagger swagger = getSwagger(ResourceWithTypedResponses.class);
        assertEquals(swagger.getDefinitions().keySet(), Arrays.asList("Tag"));
        for (Map.Entry<String, Path> entry : swagger.getPaths().entrySet()) {
            String name = entry.getKey().substring(entry.getKey().lastIndexOf("/") + 1);
            if ("testPrimitiveResponses".equals(name)) {
                Map<String, String[]> expected = ImmutableMap.of("400", new String[]{"string", "uri"},
                        "401", new String[]{"string", "url"},
                        "402", new String[]{"string", "uuid"},
                        "403", new String[]{"integer", "int64"},
                        "404", new String[]{"string", null});
                assertEquals(entry.getValue().getGet().getResponses().size(), expected.size());
                for (Map.Entry<String, Response> responseEntry : entry.getValue().getGet().getResponses().entrySet()) {
                    String[] expectedProp = expected.get(responseEntry.getKey());
                    Property property = responseEntry.getValue().getSchema();
                    assertEquals(property.getType(), expectedProp[0]);
                    assertEquals(property.getFormat(), expectedProp[1]);
                }
            } else {
                Operation op = entry.getValue().getGet();
                Property response = op.getResponses().get("200").getSchema();
                Model model = ((BodyParameter) op.getParameters().get(0)).getSchema();
                assertEquals(op.getParameters().size(), 1);

                if ("testObjectResponse".equals(name)) {
                    assertEquals(((RefProperty) response).getSimpleRef(), "Tag");
                    assertEquals(((RefModel) model).getSimpleRef(), "Tag");
                } else if ("testObjectsResponse".equals(name)) {
                    assertEquals(((RefProperty) ((ArrayProperty) response).getItems()).getSimpleRef(), "Tag");
                    assertEquals(((RefProperty) ((ArrayModel) model).getItems()).getSimpleRef(), "Tag");
                } else if ("testStringResponse".equals(name)) {
                    assertEquals(response.getClass(), StringProperty.class);
                    assertEquals(((ModelImpl) model).getType(), "string");
                } else if ("testStringsResponse".equals(name)) {
                    assertEquals(((ArrayProperty) response).getItems().getClass(), StringProperty.class);
                    assertEquals(((ArrayModel) model).getItems().getClass(), StringProperty.class);
                } else if ("testMapResponse".equals(name)) {
                    assertEquals(((RefProperty) ((MapProperty) response).getAdditionalProperties()).getSimpleRef(), "Tag");
                    assertNull(model.getProperties());
                    assertEquals(((RefProperty) ((ModelImpl) model).getAdditionalProperties()).getSimpleRef(), "Tag");
                } else {
                    fail(String.format("Unexpected property: %s", name));
                }
            }
        }
    }

    @Test(description = "scan a resource with custom operation nickname")
    public void scanResourceWithApiOperationNickname() {
        Swagger swagger = getSwagger(NicknamedOperation.class);
        assertEquals(swagger.getPaths().size(), 1);

        assertNotNull(swagger.getPaths().get("/external/info"));

        Operation op = swagger.getPaths().get("/external/info").getGet();
        assertNotNull(op);

        assertEquals(op.getOperationId(), "getMyNicknameTest");
    }

    @Test(description = "scan a resource with operation post example")
    public void scanClassWithExamplePost() {
        Swagger swagger = getSwagger(ClassWithExamplePost.class);
        Parameter param = swagger.getPaths().get("/external/info").getPost().getParameters().get(0);
        BodyParameter bp = (BodyParameter) param;
        assertNotNull(bp.getExamples());
        assertTrue(bp.getExamples().size() == 1);
        String value = bp.getExamples().get("application/json");
        assertEquals("[\"a\",\"b\"]", value);
    }

    @Test(description = "scan a resource with operation implicit post example")
    public void scanClassWithImplicitExamplePost() {
        Swagger swagger = getSwagger(ClassWithExamplePost.class);
        Parameter param = swagger.getPaths().get("/external/info2").getPost().getParameters().get(0);
        BodyParameter bp = (BodyParameter) param;
        assertNotNull(bp.getExamples());
        assertTrue(bp.getExamples().size() == 1);
        String value = bp.getExamples().get("application/json");
        assertEquals("[\"a\",\"b\"]", value);
    }

    @Test(description = "scan a resource with query param example")
    public void scanClassWithExampleQuery() {
        Swagger swagger = getSwagger(ClassWithExamplePost.class);
        Parameter param = swagger.getPaths().get("/external/info").getGet().getParameters().get(0);
        QueryParameter bp = (QueryParameter) param;
        assertNotNull(bp.getExample());
        Object value = bp.getExample();
        assertEquals("a,b,c", value);
    }

    @Test(description = "scan a resource with implicit operation query example")
    public void scanClassWithImplicitExampleQuery() {
        Swagger swagger = getSwagger(ClassWithExamplePost.class);
        Parameter param = swagger.getPaths().get("/external/info2").getGet().getParameters().get(0);
        QueryParameter bp = (QueryParameter) param;
        assertNotNull(bp.getExample());
        Object value = bp.getExample();
        assertEquals("77", value);
    }

    @Test(description = "scan a resource with operation post example (dataTypeClass)")
    public void scanClassWithExamplePostClass() {
        Swagger swagger = getSwagger(ClassWithExamplePostClass.class);
        Parameter param = swagger.getPaths().get("/external/info").getPost().getParameters().get(0);
        BodyParameter bp = (BodyParameter) param;
        assertNotNull(bp.getExamples());
        assertTrue(bp.getExamples().size() == 1);
        String value = bp.getExamples().get("application/json");
        assertEquals("[\"a\",\"b\"]", value);
    }

    @Test(description = "scan a resource with operation implicit post example (dataTypeClass)")
    public void scanClassWithImplicitExamplePostClass() {
        Swagger swagger = getSwagger(ClassWithExamplePostClass.class);
        Parameter param = swagger.getPaths().get("/external/info2").getPost().getParameters().get(0);
        BodyParameter bp = (BodyParameter) param;
        assertNotNull(bp.getExamples());
        assertTrue(bp.getExamples().size() == 1);
        String value = bp.getExamples().get("application/json");
        assertEquals("[\"a\",\"b\"]", value);
    }

    @Test(description = "scan a resource with query param example (dataTypeClass)")
    public void scanClassWithExampleClassQuery() {
        Swagger swagger = getSwagger(ClassWithExamplePostClass.class);
        Parameter param = swagger.getPaths().get("/external/info").getGet().getParameters().get(0);
        QueryParameter bp = (QueryParameter) param;
        assertNotNull(bp.getExample());
        Object value = bp.getExample();
        assertEquals("a,b,c", value);
    }

    @Test(description = "scan a resource with implicit operation query example (dataTypeClass)")
    public void scanClassWithImplicitExampleClassQuery() {
        Swagger swagger = getSwagger(ClassWithExamplePostClass.class);
        Parameter param = swagger.getPaths().get("/external/info2").getGet().getParameters().get(0);
        QueryParameter bp = (QueryParameter) param;
        assertNotNull(bp.getExample());
        Object value = bp.getExample();
        assertEquals("77", value);
    }

    @Test(description = "scan a resource with read-only and empty value parameters")
    public void scanClassWithReadOnlyAndEmptyValueParams() {
        Swagger swagger = getSwagger(Issue1979Resource.class);
        Parameter readOnlyParam = swagger.getPath("/fun/readOnly").getGet().getParameters().get(0);
        assertTrue(readOnlyParam.isReadOnly());

        Parameter allowEmptyParam = swagger.getPath("/fun/allowEmpty").getGet().getParameters().get(0);
        assertTrue(allowEmptyParam.getAllowEmptyValue());
    }
}
