package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class YamlTest {

    @Test(description = "mapper can be set")
    public void setMapper() {
        ObjectMapper mapper = ObjectMapperFactory.createYaml();
        Yaml.setMapper(mapper);
        Assert.assertTrue(mapper == Yaml.mapper());
    }
}
