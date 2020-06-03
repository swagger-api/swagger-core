package io.swagger.v3.core.filter;

import com.google.common.collect.Sets;
import io.swagger.v3.core.filter.resources.ChangeGetOperationsFilter;
import io.swagger.v3.core.filter.resources.InternalModelPropertiesRemoverFilter;
import io.swagger.v3.core.filter.resources.NoGetOperationsFilter;
import io.swagger.v3.core.filter.resources.NoOpOperationsFilter;
import io.swagger.v3.core.filter.resources.NoOpenAPIFilter;
import io.swagger.v3.core.filter.resources.NoParametersWithoutQueryInFilter;
import io.swagger.v3.core.filter.resources.NoPathItemFilter;
import io.swagger.v3.core.filter.resources.NoPetOperationsFilter;
import io.swagger.v3.core.filter.resources.NoPetRefSchemaFilter;
import io.swagger.v3.core.filter.resources.RemoveInternalParamsFilter;
import io.swagger.v3.core.filter.resources.RemoveUnreferencedDefinitionsFilter;
import io.swagger.v3.core.filter.resources.ReplaceGetOperationsFilter;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.StringUtils;
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

    private static final String RESOURCE_RECURSIVE_MODELS = "specFiles/recursivemodels.json";
    private static final String RESOURCE_PATH = "specFiles/petstore-3.0-v2.json";
    private static final String RESOURCE_PATH_3303 = "specFiles/petstore-3.0-v2-ticket-3303.json";
    private static final String RESOURCE_REFERRED_SCHEMAS = "specFiles/petstore-3.0-referred-schemas.json";
    private static final String RESOURCE_PATH_WITHOUT_MODELS = "specFiles/petstore-3.0-v2_withoutModels.json";
    private static final String RESOURCE_DEPRECATED_OPERATIONS = "specFiles/deprecatedoperationmodel.json";
    private static final String CHANGED_OPERATION_ID = "Changed Operation";
    private static final String CHANGED_OPERATION_DESCRIPTION = "Changing some attributes of the operation";
    private static final String NEW_OPERATION_ID = "New Operation";
    private static final String NEW_OPERATION_DESCRIPTION = "Replaced Operation";
    private static final String QUERY = "query";
    private static final String PET_MODEL = "Pet";
    private static final String TAG_MODEL = "/Tag";
    private static final String PET_TAG = "pet";
    private static final String STORE_TAG = "store";
    private static final String USER_TAG = "user";

    @Test(description = "it should clone everything")
    public void cloneEverything() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoOpOperationsFilter(), null, null, null);

        assertEquals(Json.pretty(filtered), Json.pretty(openAPI));
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
                    assertEquals(get.getOperationId(), operationId);
                    assertEquals(get.getDescription(), description);
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
        if (operation != null && operation.getParameters() != null) {
            for (Parameter parameter : operation.getParameters()) {
                assertNotEquals(QUERY, parameter.getIn());
            }
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
        private OpenAPI openAPI;

        private FailureHandler(ThreadGroup tg, Map<String, OpenAPI> filteredMap, OpenAPI openAPI) {
            this.tg = tg;
            this.filteredMap = filteredMap;
            this.openAPI = openAPI;
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
                assertEquals(Json.pretty(openAPI), Json.pretty(filtered));
            }
        }
    }

    @Test(description = "it should clone everything from JSON without models")
    public void cloneWithoutModels() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), RESOURCE_PATH_WITHOUT_MODELS);
        final OpenAPI openAPI = Json.mapper().readValue(json, OpenAPI.class);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoOpOperationsFilter(), null, null, null);

        SerializationMatchers.assertEqualsToJson(filtered, json);
    }

    @Test
    public void shouldRemoveBrokenRefs() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        openAPI.getPaths().get("/pet/{petId}").getGet().getResponses().getDefault().getHeaders().remove("X-Rate-Limit-Limit");
        assertNotNull(openAPI.getComponents().getSchemas().get("PetHeader"));
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, remover, null, null, null);

        assertNull(filtered.getComponents().getSchemas().get("PetHeader"));
        assertNotNull(filtered.getComponents().getSchemas().get("Category"));
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));
    }

    @Test
    public void shouldRemoveBrokenNestedRefs() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH_3303);
        openAPI.getPaths().get("/pet/{petId}").getGet().getResponses().getDefault().getHeaders().remove("X-Rate-Limit-Limit");
        assertNotNull(openAPI.getComponents().getSchemas().get("PetHeader"));
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, remover, null, null, null);

        assertNull(filtered.getComponents().getSchemas().get("PetHeader"));
        assertNull(filtered.getComponents().getSchemas().get("Bar"));
        assertNotNull(filtered.getComponents().getSchemas().get("Category"));
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));
        assertNotNull(filtered.getComponents().getSchemas().get("Foo"));
        assertNotNull(filtered.getComponents().getSchemas().get("allOfChild"));
        assertNotNull(filtered.getComponents().getSchemas().get("anyOfChild"));
        assertNotNull(filtered.getComponents().getSchemas().get("oneOfChild"));
        assertNotNull(filtered.getComponents().getSchemas().get("allOfparentA"));
        assertNotNull(filtered.getComponents().getSchemas().get("allOfparentB"));
        assertNotNull(filtered.getComponents().getSchemas().get("anyOfparentA"));
        assertNotNull(filtered.getComponents().getSchemas().get("anyOfparentB"));
        assertNotNull(filtered.getComponents().getSchemas().get("oneOfparentA"));
        assertNotNull(filtered.getComponents().getSchemas().get("oneOfparentB"));
        assertNotNull(filtered.getComponents().getSchemas().get("oneOfNestedParentA"));
        assertNotNull(filtered.getComponents().getSchemas().get("oneOfNestedParentB"));
        assertNotNull(filtered.getComponents().getSchemas().get("discriminatorParent"));
        assertNotNull(filtered.getComponents().getSchemas().get("discriminatorMatchedChildA"));
        assertNotNull(filtered.getComponents().getSchemas().get("discriminatorRefProperty"));
        assertNotNull(filtered.getComponents().getSchemas().get("discriminatorParentRefProperty"));
        assertNotNull(filtered.getComponents().getSchemas().get("discriminatorMatchedChildB"));
    }

    @Test
    public void shouldNotRemoveGoodRefs() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        assertNotNull(openAPI.getComponents().getSchemas().get("PetHeader"));
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("PetHeader"));
        assertNotNull(filtered.getComponents().getSchemas().get("Category"));
    }

    @Test(description = "it should filter any Pet Ref in Schemas")
    public void filterAwayPetRefInSchemas() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final OpenAPI filtered = new SpecFilter().filter(openAPI, new NoPetRefSchemaFilter(), null, null, null);
        validateSchemasInComponents(filtered.getComponents(), PET_MODEL);
    }

    private void validateSchemasInComponents(Components components, String model) {
        if (components != null) {
            if (components.getSchemas() != null) {
                components.getSchemas().forEach((k, v) -> assertNotEquals(model, k));
            }
        }
    }

    @Test(description = "it should filter away secret parameters")
    public void filterAwaySecretParameters() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final RemoveInternalParamsFilter filter = new RemoveInternalParamsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                final Operation get = entry.getValue().getGet();
                if (get != null) {
                    for (Parameter param : get.getParameters()) {
                        final String description = param.getDescription();
                        if (StringUtils.isNotBlank(description)) {
                            assertFalse(description.startsWith("secret"));
                        }
                    }
                }
            }
        } else {
            fail("paths should not be null");
        }
    }

    @Test(description = "it should filter away internal model properties")
    public void filterAwayInternalModelProperties() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();

        final OpenAPI filtered = new SpecFilter().filter(openAPI, filter, null, null, null);
        for (Map.Entry<String, Schema> entry : filtered.getComponents().getSchemas().entrySet()) {
            for (String propName : (Set<String>) entry.getValue().getProperties().keySet()) {
                assertFalse(propName.startsWith("_"));
            }
        }
    }

    @Test(description = "it should retain non-broken reference model composed properties")
    public void retainNonBrokenReferenceModelComposedProperties() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_REFERRED_SCHEMAS);

        assertNotNull(openAPI.getComponents().getSchemas().get("User"));

        final NoOpOperationsFilter noOperationsFilter = new NoOpOperationsFilter();
        OpenAPI filtered = new SpecFilter().filter(openAPI, noOperationsFilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(openAPI, refFilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));

    }

    @Test(description = "recursive models, e.g. A-> A or A-> B and B -> A should not result in stack overflow")
    public void removeUnreferencedDefinitionsOfRecuriveModels() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_RECURSIVE_MODELS);
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("SelfReferencingModel"));
        assertNotNull(filtered.getComponents().getSchemas().get("IndirectRecursiveModelA"));
        assertNotNull(filtered.getComponents().getSchemas().get("IndirectRecursiveModelB"));
    }

    @Test(description = "broken references should not result in NPE")
    public void removeUnreferencedModelOverride() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_REFERRED_SCHEMAS);
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("Order"));
    }

    @Test(description = "Retain models referenced from additonalProperties")
    public void retainModelsReferencesFromAdditionalProperties() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_REFERRED_SCHEMAS);
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("Order"));
        assertNotNull(filtered.getComponents().getSchemas().get("ReferredOrder"));
    }

    @Test(description = "Clone should retain any 'deperecated' flags present on operations")
    public void cloneRetainDeperecatedFlags() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_DEPRECATED_OPERATIONS);
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, remover, null, null, null);

        Operation operation = filtered.getPaths().get("/test").getGet();

        Boolean deprectedFlag = operation.getDeprecated();
        assertNotNull(deprectedFlag);
        assertEquals(deprectedFlag, Boolean.TRUE);
    }

    @Test(description = "it should contain all tags in the top level OpenAPI object")
    public void shouldContainAllTopLevelTags() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_REFERRED_SCHEMAS);
        final NoOpOperationsFilter filter = new NoOpOperationsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet(PET_TAG, USER_TAG, STORE_TAG));
    }

    @Test(description = "it should not contain user tags in the top level OpenAPI object")
    public void shouldNotContainTopLevelUserTags() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_REFERRED_SCHEMAS);
        final NoPetOperationsFilter filter = new NoPetOperationsFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet(USER_TAG, STORE_TAG));

    }

    @Test(description = "it should filter with null definitions")
    public void filterWithNullDefinitions() throws IOException {
        final OpenAPI openAPI = getOpenAPI(RESOURCE_PATH);
        openAPI.getComponents().setSchemas(null);

        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();
        final OpenAPI filtered = new SpecFilter().filter(openAPI, filter, null, null, null);
        assertNotNull(filtered);
    }

    private Set getTagNames(OpenAPI openAPI) {
        Set<String> result = new HashSet<>();
        if (openAPI.getTags() != null) {
            for (Tag item : openAPI.getTags()) {
                result.add(item.getName());
            }
        }
        return result;
    }

    private OpenAPI getOpenAPI(String path) throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), path);
        return Json.mapper().readValue(json, OpenAPI.class);
    }
}
