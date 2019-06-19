package io.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.Reader;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Pet;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.refs.GenericRef;
import io.swagger.resources.ResourceWithMoreReferences;
import io.swagger.resources.ResourceWithReferences;
import io.swagger.util.Json;
import io.swagger.util.ReferenceSerializationConfigurer;
import io.swagger.util.ResourceUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ReferenceTest {

    @Test(description = "Scan a model with common reference and reference with ApiModel")
    public void scanModel() {
        final Map<String, Property> props = ModelConverters.getInstance().readAll(Pet.class).get("Pet").getProperties();
        final RefProperty category = (RefProperty) props.get("category");
        assertEquals(category.getType(), "ref");
        assertEquals(category.get$ref(), "#/definitions/Category");

        final RefProperty categoryWithApiModel = (RefProperty) props.get("categoryWithApiModel");
        assertEquals(categoryWithApiModel.getType(), "ref");
        assertEquals(categoryWithApiModel.get$ref(), "#/definitions/MyCategory");
    }

    @Test(description = "Scan API with operation and response references")
    public void scanAPI() throws IOException {

        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithReferences.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "ResourceWithReferences.json");
        SerializationMatchers.assertEqualsToJson(swagger, json);
    }

    @Test(description = "Scan API with references")
    public void scanRef() throws IOException {

        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithMoreReferences.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "ResourceWithMoreReferences.json");
        SerializationMatchers.assertEqualsToJson(swagger, json);
    }

    @Test(description = "Serialize API with references and OriginalRefMixin activated")
    public void serializeRefWithOriginalRef() throws Exception {

        // workaround for https://github.com/FasterXML/jackson-databind/issues/1998
        ObjectMapper mapper = Json.mapper().copy();
        ReferenceSerializationConfigurer.serializeAsOriginalRef(mapper);

        final String json = ResourceUtils.loadClassResource(getClass(), "ResourceWithMoreReferencesAsOriginalRef.json");
        Swagger swagger = Json.mapper().readValue(json, Swagger.class);
        SerializationMatchers.assertEqualsToString(swagger, json, mapper);
    }

    @Test(description = "Serialize API with references and internal ref also with dots activated")
    public void serializeRefWithInternalRef() throws Exception {
        try {
            GenericRef.internalRefWithAnyDot();
            final String json = ResourceUtils.loadClassResource(getClass(), "ResourceWithMoreReferencesWithInternalRef.json");
            Swagger swagger = Json.mapper().readValue(json, Swagger.class);
            SerializationMatchers.assertEqualsToJson(swagger, json);
        } finally {
            GenericRef.relativeRefWithAnyDot();
        }
    }

    @Test(description = "Scan API with references and OriginalRefMixin activated")
    public void scanRefWithOriginalRef() throws IOException {

        // workaround for https://github.com/FasterXML/jackson-databind/issues/1998
        ObjectMapper mapper = Json.mapper().copy();
        ReferenceSerializationConfigurer.serializeAsOriginalRef(mapper);

        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithMoreReferences.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "ResourceWithMoreReferencesAsOriginalRef.json");
        SerializationMatchers.assertEqualsToString(swagger, json, mapper);
    }

}
