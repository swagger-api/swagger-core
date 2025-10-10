package io.swagger.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleBuilderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBuilderTest.class);

    @Test
    public void testBuilder() throws Exception {
        // basic metadata
        OpenAPI oai =
                new OpenAPI()
                        .info(new Info()
                                .contact(new Contact()
                                        .email("tony@eatbacon.org")
                                        .name("Tony the Tam")
                                        .url("https://foo.bar")))
                        .externalDocs(new ExternalDocumentation()
                                .description("read more here")
                                .url("http://swagger.io"))
                        .addTagsItem(new Tag()
                                .name("funky dunky")
                                .description("all about neat things"))
                        .extensions(new HashMap<String, Object>() {{
                            put("x-fancy-extension", "something");
                        }});

        Map<String, Schema> schemas = new HashMap<>();

        schemas
                .put("StringSchema", new StringSchema()
                        .description("simple string schema")
                        .minLength(3)
                        .maxLength(100)
                        .example("it works")
                );

        schemas.put("IntegerSchema", new IntegerSchema()
                .description("simple integer schema")
                .multipleOf(new BigDecimal(3))
                .minimum(new BigDecimal(6))
        );

        oai.components(new Components()
                .schemas(schemas));

        schemas.put("Address", new Schema()
                .description("address object")
                .addProperties("street", new StringSchema()
                        .description("the street number"))
                .addProperties("city", new StringSchema()
                        .description("city"))
                .addProperties("state", new StringSchema()
                        .description("state")
                        .minLength(2)
                        .maxLength(2))
                .addProperties("zip", new StringSchema()
                        .description("zip code")
                        .pattern("^\\d{5}(?:[-\\s]\\d{4})?$")
                        .minLength(2)
                        .maxLength(2))
                .addProperties("country", new StringSchema()
                        ._enum(new ArrayList<String>() {{
                            this.add("US");
                        }}))
                .description("2-digit country code")
                .minLength(2)
                .maxLength(2)

        );

        oai.paths(new Paths()
                .addPathItem("/foo", new PathItem()
                        .description("the foo path")
                        .get(new Operation()
                                .addParametersItem(new QueryParameter()
                                        .description("Records to skip")
                                        .required(false)
                                        .schema(new IntegerSchema()
                                        ))
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse()
                                                .description("it worked")
                                                .content(new Content()
                                                        .addMediaType("application/json",
                                                                new MediaType().schema(new Schema()
                                                                        .$ref("#/components/schemas/Address")))
                                                )
                                                .addLink("funky", new Link()
                                                        .operationId("getFunky")))
                                )
                        )
                )
        );

        LOGGER.debug(writeJson(oai));
    }

    public static String writeJson(Object value) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper.writer(new DefaultPrettyPrinter()).writeValueAsString(value);
    }
}
