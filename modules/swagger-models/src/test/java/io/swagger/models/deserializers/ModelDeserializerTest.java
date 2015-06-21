package io.swagger.models.deserializers;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 21/6/15
 */
public class ModelDeserializerTest {

    private static final String TEST_MODELS_DIR = "/models";

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();

    }

    @Test
    public void modelImplTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_MODELS_DIR + "/model_impl.json");
        Model model = mapper.readValue(inputStream, Model.class);
        Assert.assertNotNull(model);
        Assert.assertTrue(model instanceof ModelImpl);
    }

    @Test
    public void refModelTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_MODELS_DIR + "/model_ref.json");
        Model model = mapper.readValue(inputStream, Model.class);
        Assert.assertNotNull(model);
        Assert.assertTrue(model instanceof RefModel);
    }

    @Test
    public void composedModelTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_MODELS_DIR + "/model_composed.json");
        Model model = mapper.readValue(inputStream, Model.class);
        Assert.assertNotNull(model);
        Assert.assertTrue(model instanceof ComposedModel);
    }
}
