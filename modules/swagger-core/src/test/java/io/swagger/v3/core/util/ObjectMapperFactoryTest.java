package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ObjectMapperFactoryTest {
    @Test
    public void modifyJsonMapperBuilder() {
        assertFalse(ObjectMapperFactory.createJson().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        try {
            ObjectMapperFactory.modifyJsonMapperBuilder(builder -> builder.enable(JsonParser.Feature.ALLOW_COMMENTS));
            //assertTrue(ObjectMapperFactory.createJson().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        } finally {
            ObjectMapperFactory.resetJsonMapperBuilder();
            assertFalse(ObjectMapperFactory.createJson().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        }
    }

    @Test
    public void modifyYamlMapperBuilder() {
        assertFalse(ObjectMapperFactory.createYaml().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        try {
            ObjectMapperFactory.modifyYamlMapperBuilder(builder -> builder.enable(JsonParser.Feature.ALLOW_COMMENTS));
            //assertTrue(ObjectMapperFactory.createYaml().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        } finally {
            ObjectMapperFactory.resetYamlMapperBuilder();
            assertFalse(ObjectMapperFactory.createYaml().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        }
    }
}
