package io.swagger.v3.core.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Yaml;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class YamlSerializerTest {
    @Test
    public void testMultiLineYaml() throws Exception {
        final String yaml =
                "multiLineString: |\n" +
                        "  \"hello multiple lines\n" +
                        "  in my content without\n" +
                        "  any problem\"\n";

        JsonNode node = Yaml.mapper().readValue(yaml, JsonNode.class);

        String serializedYaml = Yaml.pretty(node);

        assertEquals(serializedYaml, yaml);
    }

    @Test
    public void testQuotedStrings() throws Exception {
        final String yaml =
                "singleLineUnquotedString: Look ma no quotes\n";

        JsonNode node = Yaml.mapper().readValue(yaml, JsonNode.class);

        String serializedYaml = Yaml.pretty(node);

        assertEquals(serializedYaml, yaml);
    }

    @Test
    public void testPreserveQuotesOnNumbers() throws Exception {
        final String yaml =
                "quotedNumber: \"3.0\"\n";

        JsonNode node = Yaml.mapper().readValue(yaml, JsonNode.class);

        String serializedYaml = Yaml.pretty(node);

        assertEquals(serializedYaml, yaml);
    }

    @Test
    public void testPreserveNoQuotesOnNumbers() throws Exception {
        final String yaml =
                "unquotedNumber: 4.0\n";

        JsonNode node = Yaml.mapper().readValue(yaml, JsonNode.class);

        String serializedYaml = Yaml.pretty(node);

        assertEquals(serializedYaml, yaml);
    }

    @Test
    public void testPreserveNoQuotesOnNonNumbers() throws Exception {
        final String yaml =
                "unquotedNumber: 4.0.0\n";

        JsonNode node = Yaml.mapper().readValue(yaml, JsonNode.class);

        String serializedYaml = Yaml.pretty(node);

        assertEquals(serializedYaml, yaml);
    }
}
