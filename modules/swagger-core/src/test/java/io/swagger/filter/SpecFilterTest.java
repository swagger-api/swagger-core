package io.swagger.filter;

import com.google.common.collect.Sets;
import io.swagger.core.filter.SpecFilter;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
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

    @Test(description = "it should clone everything")
    public void cloneEverything() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/petstore.json");
        final OpenAPI filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null);

        assertEquals(Json.pretty(swagger), Json.pretty(filtered));
    }

    @Test(description = "it should clone everything concurrently")
    public void cloneEverythingConcurrent() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/petstore.json");

        ThreadGroup tg = new ThreadGroup("SpecFilterTest" + "|" + System.currentTimeMillis());
        final Map<String, OpenAPI> filteredMap = new ConcurrentHashMap<String, OpenAPI>();
        for (int i = 0; i < 10; i++) {
            final int id = i;
            new Thread(tg, "SpecFilterTest"){
                public void run(){
                    try {
                        filteredMap.put("filtered " + id, new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        new Thread(new FailureHandler(tg, filteredMap, swagger)).start();
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
            for (OpenAPI filtered: filteredMap.values()) {
                assertEquals(Json.pretty(swagger), Json.pretty(filtered));
            }
        }
    }

    @Test(description = "it should clone everything from JSON without models")
    public void cloneWithoutModels() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/noModels.json");
        final OpenAPI swagger = Json.mapper().readValue(json, OpenAPI.class);
        final OpenAPI filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null);

        SerializationMatchers.assertEqualsToJson(filtered, json);
    }

    @Test(description = "it should filter away get operations in a resource")
    public void filterAwayGetOperations() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/petstore.json");
        final NoGetOperationsFilter filter = new NoGetOperationsFilter();

        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                assertNull(entry.getValue().getGet());
            }
        } else {
            fail("paths should not be null");
        }

    }

    @Test(description = "it should filter away the store resource")
    public void filterAwayStoreResource() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/petstore.json");
        final NoUserOperationsFilter filter = new NoUserOperationsFilter();

        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : filtered.getPaths().entrySet()) {
                assertNotEquals(entry.getKey(), "/user");
            }
        } else {
            fail("paths should not be null");
        }
    }

    @Test(description = "it should filter away secret parameters")
    public void filterAwaySecretParameters() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/sampleSpec.json");
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

    @Test(description = "it should filter away internal model properties")
    public void filterAwayInternalModelProperties() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/sampleSpec.json");
        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();

        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        for (Map.Entry<String, Schema> entry : filtered.getComponents().getSchemas().entrySet()) {
            for (String propName : entry.getValue().getProperties().keySet()) {
                assertFalse(propName.startsWith("_"));
            }
        }
    }


    @Test(description = "it should filter away broken reference model properties")
    public void filterAwayBrokenReferenceModelProperties() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/paramAndResponseRef.json");

        assertNotNull(swagger.getComponents().getSchemas().get("Order"));
        assertNotNull(swagger.getComponents().getSchemas().get("NoPropertiesModel"));
        assertNotNull(swagger.getComponents().getSchemas().get("OrderTag"));
        assertNotNull(swagger.getComponents().getSchemas().get("Tag"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        OpenAPI filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("Order"));
        assertNotNull(filtered.getComponents().getSchemas().get("NoPropertiesModel"));
        assertNotNull(filtered.getComponents().getSchemas().get("OrderTag"));
        assertNotNull(filtered.getComponents().getSchemas().get("Tag"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNull(filtered.getComponents().getSchemas().get("Order"));
        assertNull(filtered.getComponents().getSchemas().get("NoPropertiesModel"));
        assertNull(filtered.getComponents().getSchemas().get("OrderTag"));
        assertNotNull(filtered.getComponents().getSchemas().get("Tag"));

    }

    @Test(description = "it should retain non-broken reference model properties")
    public void retainNonBrokenReferenceModelProperties() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/paramAndResponseRefArray.json");

        assertNotNull(swagger.getComponents().getSchemas().get("User"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        OpenAPI filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User")); // ArrayProperty
        assertNotNull(filtered.getComponents().getSchemas().get("Pet")); // ArrayModel

    }

    @Test(description = "it should retain non-broken reference model composed properties")
    public void retainNonBrokenReferenceModelComposedProperties() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/paramAndResponseRefComposed.json");

        assertNotNull(swagger.getComponents().getSchemas().get("User"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        OpenAPI filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("User"));
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));

    }

    @Test(description = "recursive models, e.g. A-> A or A-> B and B -> A should not result in stack overflow")
    public void removeUnreferencedDefinitionsOfRecuriveModels() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/recursivemodels.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("SelfReferencingModel"));
        assertNotNull(filtered.getComponents().getSchemas().get("IndirectRecursiveModelA"));
        assertNotNull(filtered.getComponents().getSchemas().get("IndirectRecursiveModelB"));
    }

    @Test(description = "broken references should not result in NPE")
    public void removeUnreferencedModelOverride() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/brokenrefmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("RootModel"));
    }

    @Test(description = "Retain models referenced from additonalProperties")
    public void retainModelsReferencesFromAdditionalProperties() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/additionalpropsmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getComponents().getSchemas().get("A"));
        assertNotNull(filtered.getComponents().getSchemas().get("B"));
    }

    @Test(description = "Clone should retain any 'deperecated' flags present on operations")
    public void cloneRetainDeperecatedFlags() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/deprecatedoperationmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        Operation operation = filtered.getPaths().get("/test").getGet();

        Boolean deprectedFlag = operation.getDeprecated();
        assertNotNull(deprectedFlag);
        assertEquals(deprectedFlag, Boolean.TRUE);
    }

    @Test(description = "it should filter models where some fields have no properties")
    public void filterNoPropertiesModels() throws IOException {
        final String modelName = "Array";
        final Schema model = new Schema().title(modelName);

        final OpenAPI swagger = new OpenAPI();
        swagger.getComponents().addSchemas(modelName, model);

        final Map<String, Schema> filtered = new SpecFilter()
                .filterDefinitions(
                        new NoOpOperationsFilter(), swagger.getComponents().getSchemas(), null, null, null);

        if (filtered.size() != 1) {
            fail("ModelImpl with no properties failed to filter");
        }
    }

    @Test(description = "it should contain all tags in the top level Swagger object")
    public void shouldContainAllTopLevelTags() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/petstore.json");
        final NoOpOperationsFilter filter = new NoOpOperationsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet("pet", "user", "store"));
    }

    @Test(description = "it should not contain user tags in the top level Swagger object")
    public void shouldNotContainTopLevelUserTags() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/petstore.json");
        final NoUserOperationsFilter filter = new NoUserOperationsFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet("pet", "store"));
    }

    @Test(description = "it should filter with null definitions")
    public void filterWithNullDefinitions() throws IOException {
        final OpenAPI swagger = getSwagger("specFiles/petstore.json");
        swagger.getComponents().setSchemas(null);

        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();
        final OpenAPI filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertNotNull(filtered);
    }

    private Set getTagNames(OpenAPI swagger) {
        Set<String> result = new HashSet<String>();
        for (Tag item : swagger.getTags()) {
            result.add(item.getName());
        }
        return result;
    }

    private OpenAPI getSwagger(String path) throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), path);
        return Json.mapper().readValue(json, OpenAPI.class);
    }
}
