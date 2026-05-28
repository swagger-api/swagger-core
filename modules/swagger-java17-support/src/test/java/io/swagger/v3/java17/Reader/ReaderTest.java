package io.swagger.v3.java17.Reader;

import io.swagger.v3.java17.matchers.SerializationMatchers;
import io.swagger.v3.java17.resources.JavaRecordWithPathResource;
import io.swagger.v3.java17.resources.OtherJavaRecordWithPathsResource;
import io.swagger.v3.java17.resources.TestControllerWithRecordResource;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

public class ReaderTest {

    @Test
    public void TestJavaRecordRef(){
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(true));

        OpenAPI openAPI = reader.read(TestControllerWithRecordResource.class);
        String yaml = "openapi: 3.1.0\n" +
                "paths:\n" +
                "  /v17:\n" +
                "    post:\n" +
                "      operationId: opsRecordID\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: Successful operation\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/JavaRecordResource'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    JavaRecordResource:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        test:\n" +
                "          type: string\n" +
                "          description: Testing of Java Record Processing\n" +
                "        isLatest:\n" +
                "          type: boolean\n" +
                "        id:\n" +
                "          type: string\n" +
                "        age:\n" +
                "          type: integer\n" +
                "          format: int32";
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }

    @Test
    public void TestSetOfRecords(){
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JavaRecordWithPathResource.class);
        classes.add(OtherJavaRecordWithPathsResource.class);

        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(classes);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /sample/1:\n" +
                "    post:\n" +
                "      description: description 1\n" +
                "      operationId: id 1\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /sample/2:\n" +
                "    post:\n" +
                "      description: description 2\n" +
                "      operationId: id 2\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "  /sample2:\n" +
                "    get:\n" +
                "      description: description\n" +
                "      operationId: Operation Id\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      security:\n" +
                "      - security_key:\n" +
                "        - write:pets\n" +
                "        - read:pets\n" +
                "  /sample2/2:\n" +
                "    get:\n" +
                "      description: description 2\n" +
                "      operationId: Operation Id 2\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "      security:\n" +
                "      - security_key2:\n" +
                "        - write:pets\n" +
                "        - read:pets";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }
}
