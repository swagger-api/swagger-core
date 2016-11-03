package io.swagger.filter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import io.swagger.core.filter.SpecFilter;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;
import io.swagger.util.Json;
import io.swagger.util.ResourceUtils;

import com.google.common.collect.Sets;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SpecFilterTest {

    @Test(description = "it should clone everything")
    public void cloneEverything() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");
        final Swagger filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null);

        assertEquals(Json.pretty(swagger), Json.pretty(filtered));
    }

    @Test(description = "it should clone everything concurrently")
    public void cloneEverythingConcurrent() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");

        ThreadGroup tg = new ThreadGroup("SpecFilterTest" + "|" + System.currentTimeMillis());
        final Map<String, Swagger> filteredMap = new ConcurrentHashMap<String, Swagger>();
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
        Map<String, Swagger> filteredMap;
        private Swagger swagger;

        public FailureHandler(ThreadGroup tg, Map<String, Swagger> filteredMap, Swagger swagger) {
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
            for (Swagger filtered: filteredMap.values()) {
                assertEquals(Json.pretty(swagger), Json.pretty(filtered));
            }
        }
    }

    @Test(description = "it should clone everything from JSON without models")
    public void cloneWithoutModels() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/noModels.json");
        final Swagger swagger = Json.mapper().readValue(json, Swagger.class);
        final Swagger filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null);

        SerializationMatchers.assertEqualsToJson(filtered, json);
    }

    @Test(description = "it should filter away get operations in a resource")
    public void filterAwayGetOperations() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");
        final NoGetOperationsFilter filter = new NoGetOperationsFilter();

        final Swagger filtered = new SpecFilter().filter(swagger, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, Path> entry : filtered.getPaths().entrySet()) {
                assertNull(entry.getValue().getGet());
            }
        } else {
            fail("paths should not be null");
        }

    }

    @Test(description = "it should filter away the store resource")
    public void filterAwayStoreResource() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");
        final NoUserOperationsFilter filter = new NoUserOperationsFilter();

        final Swagger filtered = new SpecFilter().filter(swagger, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, Path> entry : filtered.getPaths().entrySet()) {
                assertNotEquals(entry.getKey(), "/user");
            }
        } else {
            fail("paths should not be null");
        }
    }

    @Test(description = "it should filter away secret parameters")
    public void filterAwaySecretParameters() throws IOException {
        final Swagger swagger = getSwagger("specFiles/sampleSpec.json");
        final RemoveInternalParamsFilter filter = new RemoveInternalParamsFilter();

        final Swagger filtered = new SpecFilter().filter(swagger, filter, null, null, null);

        if (filtered.getPaths() != null) {
            for (Map.Entry<String, Path> entry : filtered.getPaths().entrySet()) {
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
        final Swagger swagger = getSwagger("specFiles/sampleSpec.json");
        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();

        final Swagger filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        for (Map.Entry<String, Model> entry : filtered.getDefinitions().entrySet()) {
            for (String propName : entry.getValue().getProperties().keySet()) {
                assertFalse(propName.startsWith("_"));
            }
        }
    }


    @Test(description = "it should filter away broken reference model properties")
    public void filterAwayBrokenReferenceModelProperties() throws IOException {
        final Swagger swagger = getSwagger("specFiles/paramAndResponseRef.json");

        assertNotNull(swagger.getDefinitions().get("Order"));
        assertNotNull(swagger.getDefinitions().get("NoPropertiesModel"));
        assertNotNull(swagger.getDefinitions().get("OrderTag"));
        assertNotNull(swagger.getDefinitions().get("Tag"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        Swagger filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getDefinitions().get("Order"));
        assertNotNull(filtered.getDefinitions().get("NoPropertiesModel"));
        assertNotNull(filtered.getDefinitions().get("OrderTag"));
        assertNotNull(filtered.getDefinitions().get("Tag"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNull(filtered.getDefinitions().get("Order"));
        assertNull(filtered.getDefinitions().get("NoPropertiesModel"));
        assertNull(filtered.getDefinitions().get("OrderTag"));
        assertNotNull(filtered.getDefinitions().get("Tag"));

    }

    @Test(description = "it should retain non-broken reference model properties")
    public void retainNonBrokenReferenceModelProperties() throws IOException {
        final Swagger swagger = getSwagger("specFiles/paramAndResponseRefArray.json");

        assertNotNull(swagger.getDefinitions().get("User"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        Swagger filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getDefinitions().get("User"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNotNull(filtered.getDefinitions().get("User")); // ArrayProperty
        assertNotNull(filtered.getDefinitions().get("Pet")); // ArrayModel

    }

    @Test(description = "it should retain non-broken reference model composed properties")
    public void retainNonBrokenReferenceModelComposedProperties() throws IOException {
        final Swagger swagger = getSwagger("specFiles/paramAndResponseRefComposed.json");

        assertNotNull(swagger.getDefinitions().get("User"));

        final NoOpOperationsFilter noOpfilter = new NoOpOperationsFilter();
        Swagger filtered = new SpecFilter().filter(swagger, noOpfilter, null, null, null);

        assertNotNull(filtered.getDefinitions().get("User"));

        final RemoveUnreferencedDefinitionsFilter refFilter = new RemoveUnreferencedDefinitionsFilter();
        filtered = new SpecFilter().filter(swagger, refFilter, null, null, null);

        assertNotNull(filtered.getDefinitions().get("User"));
        assertNotNull(filtered.getDefinitions().get("Pet"));

    }

    @Test(description = "recursive models, e.g. A-> A or A-> B and B -> A should not result in stack overflow")
    public void removeUnreferencedDefinitionsOfRecuriveModels() throws IOException {
        final Swagger swagger = getSwagger("specFiles/recursivemodels.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final Swagger filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getDefinitions().get("SelfReferencingModel"));
        assertNotNull(filtered.getDefinitions().get("IndirectRecursiveModelA"));
        assertNotNull(filtered.getDefinitions().get("IndirectRecursiveModelB"));
    }

    @Test(description = "broken references should not result in NPE")
    public void removeUnreferencedModelOverride() throws IOException {
        final Swagger swagger = getSwagger("specFiles/brokenrefmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final Swagger filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getDefinitions().get("RootModel"));
    }

    @Test(description = "Retain models referenced from additonalProperties")
    public void retainModelsReferencesFromAdditionalProperties() throws IOException {
        final Swagger swagger = getSwagger("specFiles/additionalpropsmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final Swagger filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        assertNotNull(filtered.getDefinitions().get("A"));
        assertNotNull(filtered.getDefinitions().get("B"));
    }

    @Test(description = "Clone should retain any 'deperecated' flags present on operations")
    public void cloneRetainDeperecatedFlags() throws IOException {
        final Swagger swagger = getSwagger("specFiles/deprecatedoperationmodel.json");
        final RemoveUnreferencedDefinitionsFilter remover = new RemoveUnreferencedDefinitionsFilter();
        final Swagger filtered = new SpecFilter().filter(swagger, remover, null, null, null);

        Operation operation = filtered.getPath("/test").getOperations().get(0);

        Boolean deprectedFlag = operation.isDeprecated();
        assertNotNull(deprectedFlag);
        assertEquals(deprectedFlag, Boolean.TRUE);
    }

    @Test(description = "it should filter models where some fields have no properties")
    public void filterNoPropertiesModels() throws IOException {
        final String modelName = "Array";
        final ModelImpl model = new ModelImpl().type(ModelImpl.OBJECT).name(modelName);

        final Swagger swagger = new Swagger();
        swagger.addDefinition(modelName, model);

        final Map<String, Model> filtered = new SpecFilter().filterDefinitions(new NoOpOperationsFilter(), swagger.getDefinitions(),
                null, null, null);

        if (filtered.size() != 1) {
            fail("ModelImpl with no properties failed to filter");
        }
    }

    @Test(description = "it should contain all tags in the top level Swagger object")
    public void shouldContainAllTopLevelTags() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");
        final NoOpOperationsFilter filter = new NoOpOperationsFilter();
        final Swagger filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet("pet", "user", "store"));
    }

    @Test(description = "it should not contain user tags in the top level Swagger object")
    public void shouldNotContainTopLevelUserTags() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");
        final NoUserOperationsFilter filter = new NoUserOperationsFilter();
        final Swagger filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertEquals(getTagNames(filtered), Sets.newHashSet("pet", "store"));
    }

    @Test(description = "it should filter with null definitions")
    public void filterWithNullDefinitions() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");
        swagger.setDefinitions(null);

        final InternalModelPropertiesRemoverFilter filter = new InternalModelPropertiesRemoverFilter();
        final Swagger filtered = new SpecFilter().filter(swagger, filter, null, null, null);
        assertNotNull(filtered);
    }

    private Set getTagNames(Swagger swagger) {
        Set<String> result = new HashSet<String>();
        for (Tag item : swagger.getTags()) {
            result.add(item.getName());
        }
        return result;
    }

    private Swagger getSwagger(String path) throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), path);
        return Json.mapper().readValue(json, Swagger.class);
    }
}
