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

public class SpecFilterTest {

    @Test(description = "it should clone everything")
    public void cloneEverything() throws IOException {
        final Swagger swagger = getSwagger("specFiles/petstore.json");
        final Swagger filtered = new SpecFilter().filter(swagger, new NoOpOperationsFilter(), null, null, null);

        assertEquals(Json.pretty(swagger), Json.pretty(filtered));
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
