package io.swagger.v3.core.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.testng.annotations.Test;

import java.util.Arrays;

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

    @Test
    public void testNoBackSlashesAddedInYaml() throws Exception {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.0.0")
                .info(new Info()
                        .title("Test long description")
                        .version("1.0.0")
                        .description("Lorem ipsum dolor sit amet consectetur adipiscing elit, ligula integer tempus proin mattis fusce. Proin vivamus phasellus tincidunt sapien inceptos viverra turpis vehicula neque sociosqu, nostra rhoncus sagittis cursus dapibus dignissim ornare consequat curabitur, imperdiet lacus facilisis ridiculus ante molestie convallis accumsan donec. Laoreet pulvinar feugiat velit proin rhoncus dis ullamcorper, curae nullam at penatibus tempor quis."))
                .servers(Arrays.asList(new Server().url("/")));

        String serializedYaml = Yaml.pretty(openAPI);

        assertEquals(serializedYaml, "openapi: 3.0.0\n" +
                "info:\n" +
                "  title: Test long description\n" +
                "  description: Lorem ipsum dolor sit amet consectetur adipiscing elit, ligula integer\n" +
                "    tempus proin mattis fusce. Proin vivamus phasellus tincidunt sapien inceptos viverra\n" +
                "    turpis vehicula neque sociosqu, nostra rhoncus sagittis cursus dapibus dignissim\n" +
                "    ornare consequat curabitur, imperdiet lacus facilisis ridiculus ante molestie\n" +
                "    convallis accumsan donec. Laoreet pulvinar feugiat velit proin rhoncus dis ullamcorper,\n" +
                "    curae nullam at penatibus tempor quis.\n" +
                "  version: 1.0.0\n" +
                "servers:\n" +
                "- url: /\n");
    }
}
