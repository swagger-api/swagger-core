package io.swagger.filter;

import com.google.common.collect.Sets;
import io.swagger.core.filter.SpecFilter;
import io.swagger.filter.resources.ChangeGetOperationsFilter;
import io.swagger.filter.resources.InternalModelPropertiesRemoverFilter;
import io.swagger.filter.resources.NoGetOperationsFilter;
import io.swagger.filter.resources.NoOpOperationsFilter;
import io.swagger.filter.resources.NoOpenAPIFilter;
import io.swagger.filter.resources.NoParametersWithoutQueryInFilter;
import io.swagger.filter.resources.NoPathItemFilter;
import io.swagger.filter.resources.NoPetOperationsFilter;
import io.swagger.filter.resources.NoPetRefSchemaFilter;
import io.swagger.filter.resources.RemoveInternalParamsFilter;
import io.swagger.filter.resources.RemoveUnreferencedDefinitionsFilter;
import io.swagger.filter.resources.ReplaceGetOperationsFilter;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.tags.Tag;
import io.swagger.util.Json;
import io.swagger.util.ResourceUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

public class SpecFilterTest {

    private static final String RESOURCE_PATH = "specFiles/petstore-3.0-v2.json";
    private static final String CHANGED_OPERATION_ID = "Changed Operation";
    private static final String CHANGED_OPERATION_DESCRIPTION = "Changing some attributes of the operation";
    private static final String NEW_OPERATION_ID = "New Operation";
    private static final String NEW_OPERATION_DESCRIPTION = "Replaced Operation";
    private static final String QUERY = "query";
    private static final String PET_REF = "#/components/schemas/Pet";

    @Test(description = "it should clone everything")
    public void cloneEverything() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoOpOperationsFilter(), null, null, null);

        assertEquals(Json.pretty(openAPI), Json.pretty(filtered));
    }

    @Test(description = "it should filter away get operations in a resource")
    public void filterAwayGetOperations() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final NoGetOperationsFilter filter = new NoGetOperationsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                assertNull(entry.getValue().getGet());
            }
        } else {
            fail("paths should not be null");
        }
    }

    @Test(description = "it should filter away the pet resource")
    public void filterAwayPetResource() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final NoPetOperationsFilter filter = new NoPetOperationsFilter();

        final OpenAPI filtered = new SpecFilter().filter(openAPI, filter, null, null, null);
        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                assertNull(entry.getValue().getDelete());
                assertNull(entry.getValue().getPost());
                assertNull(entry.getValue().getPut());
                assertNull(entry.getValue().getGet());
                assertNull(entry.getValue().getHead());
                assertNull(entry.getValue().getOptions());
            }
        } else {
            fail("paths should not be null");
        }
    }

    @Test(description = "it should replace away with a new operation")
    public void replaceGetResources() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        OpenAPI filter = new SpecFilter().filter(openAPI, new ReplaceGetOperationsFilter(), null, null, null);
        assertOperations(filter, NEW_OPERATION_ID, NEW_OPERATION_DESCRIPTION);
    }

    @Test(description = "it should change away with a new operation")
    public void changeGetResources() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        OpenAPI filter = new SpecFilter().filter(openAPI, new ChangeGetOperationsFilter(), null, null, null);
        assertOperations(filter, CHANGED_OPERATION_ID, CHANGED_OPERATION_DESCRIPTION);
    }

    private void assertOperations(OpenAPI filtered, String operationId, String description) {
        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                Operation get = entry.getValue().getGet();
                if (get != null) {
                    assertEquals(operationId, get.getOperationId());
                    assertEquals(description, get.getDescription());
                }
            }
        } else {
            fail("paths should not be null");
        }
    }

    @Test(description = "it should filter an openAPI object")
    public void filterAwayOpenAPI() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoOpenAPIFilter(), null, null, null);
        assertNull(filtered);
    }

    @Test(description = "it should filter any PathItem objects without Ref")
    public void filterAwayPathItemWithoutRef() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoPathItemFilter(), null, null, null);
        assertEquals(0, filtered.getPaths().size());
    }

    @Test(description = "it should filter any query parameter")
    public void filterAwayQueryParameters() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoParametersWithoutQueryInFilter(), null, null, null);
        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                validateParameters(entry.getValue().getGet());
                validateParameters(entry.getValue().getPost());
                validateParameters(entry.getValue().getPut());
                validateParameters(entry.getValue().getPatch());
                validateParameters(entry.getValue().getHead());
                validateParameters(entry.getValue().getDelete());
                validateParameters(entry.getValue().getOptions());
            }
        }
    }

    private void validateParameters(Operation operation) {
        if (operation != null) {
            for (Parameter parameter : operation.getParameters()) {
                assertNotEquals(QUERY, parameter.getIn());
            }
        }
    }

    @Test(description = "it should filter any Pet Ref in Schemas")
    public void filterAwayPetRefInSchemas() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoPetRefSchemaFilter(), null, null, null);
        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                validateSchemasInOperations(entry.getValue().getGet());
                validateSchemasInOperations(entry.getValue().getPost());
                validateSchemasInOperations(entry.getValue().getPut());
                validateSchemasInOperations(entry.getValue().getPatch());
                validateSchemasInOperations(entry.getValue().getHead());
                validateSchemasInOperations(entry.getValue().getDelete());
                validateSchemasInOperations(entry.getValue().getOptions());
            }
        }
    }

    private void validateSchemasInOperations(Operation operation) {
        if (operation != null) {
            for (Parameter parameter : operation.getParameters()) {
                Schema schema = parameter.getSchema();
                if (schema != null) {
                    assertNotEquals(PET_REF, schema.get$ref());
                }
            }

            RequestBody requestBody = operation.getRequestBody();
            if (requestBody != null) {
                requestBody.getContent().forEach((key, content) -> {
                    Schema schema = content.getSchema();
                    if (schema != null) {
                        assertNotEquals(PET_REF, schema.get$ref());
                    }
                });
            }

            operation.getResponses().forEach((key, response) -> {
                if (response != null && response.getContent() != null) {
                    response.getContent().forEach((contentKey, content) -> {
                        Schema schema = content.getSchema();
                        if (schema != null) {
                            assertNotEquals(PET_REF, schema.get$ref());
                        }
                    });
                }
            });
        }
    }

    @Test(description = "it should clone everything concurrently")
    public void cloneEverythingConcurrent() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);

        ThreadGroup tg = new ThreadGroup("SpecFilterTest" + "|" + System.currentTimeMillis());
        final Map<String, OpenAPI> filteredMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 10; i++) {
            final int id = i;
            new Thread(tg, "SpecFilterTest") {
                public void run() {
                    try {
                        filteredMap.put("filtered " + id, new SpecFilter().filter(openAPI, new NoOpOperationsFilter(), null, null, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        new Thread(new FailureHandler(tg, filteredMap, openAPI)).start();
    }

    class FailureHandler implements Runnable {
        ThreadGroup tg;
        Map<String, OpenAPI> filteredMap;
        private OpenAPI swagger;

        public FailureHandler(ThreadGroup tg, Map<String, OpenAPI> filteredMap, OpenAPI swagger) {
            this.tg = tg;
            this.filteredMap = filteredMap;
            this.swagger = swagger;
        }

        @Override
        public void run() {
            try {
                Thread[] thds = new Thread[tg.activeCount()];
                tg.enumerate(thds);
                for (Thread t : thds) {
                    if (t != null) {
                        t.join(10000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (OpenAPI filtered : filteredMap.values()) {
                assertEquals(Json.pretty(swagger), Json.pretty(filtered));
            }
        }
    }

    @Test(enabled = false, description = "it should clone everything from JSON without models")
    public void cloneWithoutModels() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/noModels.json");
        final OpenAPI swagger = Json.mapper().readValue(json, OpenAPI.class);
        final OpenAPI filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null);

        SerializationMatchers.assertEqualsToJson(filtered, json);
    }

    @Test(description = "it should filter away broken reference model properties")
    public void filterAwayBrokenReferenceModelProperties() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);

        assertNotNull(openAPI.getComponents().getSchemas().get("Pet"));

        OpenAPI filtered = new SpecFilter().filter(openAPI, new NoPetRefSchemaFilter(), null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(openAPI, refFilter, null, null, null);

        assertNull(filtered.getComponents().getSchemas().get("Pet"));
    }

    @Test(enabled = false, description = "it should filter away secret parameters")
    public void filterAwaySecretParameters() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/sampleSpec.json");
        final RemoveInternalParamsFilter filter = new RemoveInternalParamsFilter();

        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                final Operation get = entry.getValue().getGet();
                for (Parameter param : get.getParameters()) {
                    final String description = param.getDescription();
                    assertNotNull(description);
                    assertFalse(description.startsWith("secret"));
                }
            }
        } else {
            fail("paths should not be null");
        }
    }

    @Test(enabled = false, description = "it should filter away internal model properties")
    public void filterAwayInternalModelProperties() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/sampleSpec.json");
        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();

        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        for (Map.Entry<String, Schema> entry : filtered.getComponents().getSchemas().entrySet()) {
            for (String propName : (Set<String>) entry.getValue().getProperties().keySet()) {
                assertFalse(propName.startsWith("_"));
            }
        }
    }

    @Test(enabled = false, description = "it should retain non-broken reference model properties")
    public void retainNonBrokenReferenceModelProperties() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/paramAndResponseRefArray.json");

        assertNotNull(swagger.getComponents().getSchemas().get("User"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        OpenAPI filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User")); // ArrayProperty
        assertNotNull(filtered.getComponents().getSchemas().get("Pet")); // ArrayModel

    }

    @Test(enabled = false, description = "it should retain non-broken reference model composed properties")
    public void retainNonBrokenReferenceModelComposedProperties() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/paramAndResponseRefComposed.json");

        assertNotNull(swagger.getComponents().getSchemas().get("User"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        OpenAPI filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));

    }

    @Test(enabled = false, description = "recursive models, e.g. A-> A or A-> B and B -> A should not result in stack overflow")
    public void removeUnreferencedDefinitionsOfRecuriveModels() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/recursivemodels.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("SelfReferencingModel"));
        assertNotNull(filtered.getComponents().getSchemas().get("IndirectRecursiveModelA"));
        assertNotNull(filtered.getComponents().getSchemas().get("IndirectRecursiveModelB"));
    }

    @Test(enabled = false, description = "broken references should not result in NPE")
    public void removeUnreferencedModelOverride() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/brokenrefmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("RootModel"));
    }

    @Test(enabled = false, description = "Retain models referenced from additonalProperties")
    public void retainModelsReferencesFromAdditionalProperties() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/additionalpropsmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("A"));
        assertNotNull(filtered.getComponents().getSchemas().get("B"));
    }

    @Test(enabled = false, description = "Clone should retain any 'deperecated' flags present on operations")
    public void cloneRetainDeperecatedFlags() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/deprecatedoperationmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        Operation operation = filtered.getPaths().get("/test").getGet();

        Boolean deprectedFlag = operation.getDeprecated();
        assertNotNull(deprectedFlag);
        assertEquals(deprectedFlag, Boolean.TRUE);
    }

    @Test(enabled = false, description = "it should filter models where some fields have no properties")
    public void filterNoPropertiesModels() throws IOException {
        final String modelName = "Array";
        final Schema model = new Schema().title(modelName);

        final OpenAPI swagger = new OpenAPI();
        swagger.getComponents().addSchemas(modelName, model);

        /*final Map<String, Schema> filtered = new SpecFilter()
                .filterDefinitions(
                        new NoOpOperationsFilter(), swagger.getComponents().getSchemas(), null, null, null);

        if (filtered.size() != 1) {
            fail("ModelImpl with no properties failed to filter");
        }*/
    }

    @Test(enabled = false, description = "it should contain all tags in the top level Swagger object")
    public void shouldContainAllTopLevelTags() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/petstore.json");
        final NoOpOperationsFilter filter = new NoOpOperationsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet("pet", "user", "store"));
    }

    @Test(enabled = false, description = "it should not contain user tags in the top level Swagger object")
    public void shouldNotContainTopLevelUserTags() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/petstore.json");
        final NoPetOperationsFilter filter = new NoPetOperationsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet("pet", "store"));
    }

    @Test(enabled = false, description = "it should filter with null definitions")
    public void filterWithNullDefinitions() throws IOException {
        final OpenAPI swagger = getOpenAPI("specFiles/petstore.json");
        swagger.getComponents().setSchemas(null);

        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertNotNull(filtered);
    }

    private Set getTagNames(OpenAPI swagger) {
        Set<String> result = new HashSet<>();
        for (Tag item : swagger.getTags()) {
            result.add(item.getName());
        }
        return result;
    }

    private OpenAPI getOpenAPI(String path) throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), path);
        return Json.mapper().readValue(json, OpenAPI.class);
    }
}
