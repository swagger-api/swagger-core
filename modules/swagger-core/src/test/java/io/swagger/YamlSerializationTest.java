package io.swagger;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Contact;
import io.swagger.models.Error;
import io.swagger.models.Info;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Person;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Yaml;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

/**
 * Currently, this act as a proof of the shortcoming of the snakeyaml, which cannot persist key longer than 127 well.
 * Once that was enhanced, the test should pass.
 *
 * https://bitbucket.org/asomov/snakeyaml/issues/399/is-there-any-reason-why-checksimplekey-has
 */
public class YamlSerializationTest {

  @Test(description = "it should fail to save the key longer than 127, so please do NOT go too deep into the packages!", enabled = false)
  public void saveLongKey() throws JsonProcessingException {
    String clazzName = "io.swagger.YamlSerializationTest.Abcdefg.Hijklmn.Opqrst.Uvwxyz.Abcdefg1.Hijklmn1.Opqrst1.Uvwxyz1.Abcdefg2.Hijklmn2.Opqrst2.Uvwxz";
    final Model personModel = ModelConverters.getInstance().read(Person.class).get(
        Person.class.getName());
    final Model errorModel = ModelConverters.getInstance().read(Error.class).get(Error.class.getName());
    final Info info = new Info()
        .version("1.0.0")
        .title("Swagger Petstore");

    final Contact contact = new Contact()
        .name("Swagger API Team")
        .email("foo@bar.baz")
        .url("http://swagger.io");

    info.setContact(contact);

    final Map<String, Object> map = new HashMap<String, Object>();
    map.put("name", "value");
    info.setVendorExtension("x-test2", map);
    info.setVendorExtension("x-test", "value");

    final Swagger swagger = new Swagger()
        .info(info)
        .host("petstore.swagger.io")
        .securityDefinition("api-key", new ApiKeyAuthDefinition("key", In.HEADER))
        .scheme(Scheme.HTTP)
        .consumes("application/json")
        .produces("application/json")
        .model(clazzName, personModel)
        .model(Error.class.getName(), errorModel);

    final Operation get = new Operation()
        .produces("application/json")
        .summary("finds pets in the system")
        .description("a longer description")
        .tag("Pet Operations")
        .operationId("get pet by id")
        .deprecated(true);

    get.parameter(new QueryParameter()
        .name("tags")
        .description("tags to filter by")
        .required(false)
        .property(new StringProperty())
    );

    get.parameter(new PathParameter()
        .name("petId")
        .description("pet to fetch")
        .property(new LongProperty())
    );

    final Response response = new Response()
        .description("pets returned")
        .schema(new RefProperty().asDefault(clazzName))
        .example("application/json", "fun!");

    final Response errorResponse = new Response()
        .description("error response")
        .schema(new RefProperty().asDefault(Error.class.getName()));

    get.response(200, response)
        .defaultResponse(errorResponse);

    final Operation post = new Operation()
        .summary("adds a new pet")
        .description("you can add a new pet this way")
        .tag("Pet Operations")
        .operationId("add pet")
        .defaultResponse(errorResponse)
        .parameter(new BodyParameter()
            .description("the pet to add")
            .schema(new RefModel().asDefault(clazzName)));

    swagger.path("/pets/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz", new Path().get(get).post(post));

    ObjectMapper mapper =  Yaml.mapper();
    byte[] str =mapper.writeValueAsBytes(swagger);
    assertTrue(new String(str).contains("/pets/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz/abcdefghijklmnopqrstuvwxyz:"));
    assertTrue(new String(str).contains("io.swagger.YamlSerializationTest.Abcdefg.Hijklmn.Opqrst.Uvwxyz.Abcdefg1.Hijklmn1.Opqrst1.Uvwxyz1.Abcdefg2.Hijklmn2.Opqrst2.Uvwxz:"));
    assertFalse(new String(str).contains(": type: \"object\""));
  }
}
