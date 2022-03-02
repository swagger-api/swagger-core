/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
