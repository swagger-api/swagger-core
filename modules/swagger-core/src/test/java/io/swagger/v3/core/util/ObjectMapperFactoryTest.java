package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectMapperFactoryTest {
    @Test
    public void modifyJsonMapperBuilder() {
        assertFalse(ObjectMapperFactory.createJson().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        try {
            ObjectMapperFactory.modifyJsonMapperBuilder(builder -> builder.enable(JsonParser.Feature.ALLOW_COMMENTS));
            assertTrue(ObjectMapperFactory.createJson().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        } finally {
            ObjectMapperFactory.resetJsonMapperBuilder();
            assertFalse(ObjectMapperFactory.createJson().isEnabled(JsonParser.Feature.ALLOW_COMMENTS));
        }
    }
}
