package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class PojoTest {

    private Map<String, io.swagger.v3.oas.models.media.Schema> read(Type type) {
        return ModelConverters.getInstance().read(type);
    }

    private Map<String, io.swagger.v3.oas.models.media.Schema> readAll(Type type) {
        return ModelConverters.getInstance().readAll(type);
    }

    private void assertEqualsToJson(Object objectToSerialize, String fileName) throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), fileName);
        SerializationMatchers.assertEqualsToJson(objectToSerialize, json);
    }

    @Test
    public void testModelWithTitle() {

        String yaml = "ClassWithTitle:\n" +
                "  type: object\n" +
                "  title: 'My Pojo'\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string";
        SerializationMatchers.assertEqualsToYaml(read(ClassWithTitle.class), yaml);

    }

    @Schema(title = "My Pojo")
    static class ClassWithTitle {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test(description = "The @Schema annotation will only be adding additional sugar on the property")
    public void testModelWithAnnotatedPrivateMember() {
        String yaml = "ClassWithAnnotatedProperty:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      description: 'a long description for this property'\n" +
                "      type: string";
        SerializationMatchers.assertEqualsToYaml(read(ClassWithAnnotatedProperty.class), yaml);

    }

    static class ClassWithAnnotatedProperty {
        @Schema(description = "a long description for this property")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test(description = "The @Schema annotation will only be adding additional sugar on the property")
    public void testModelWithAnnotatedPublicMethod() {
        String yaml = "ClassWithAnnotatedMethod:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      description: 'a long description for this property'\n" +
                "      type: string";
        SerializationMatchers.assertEqualsToYaml(read(ClassWithAnnotatedMethod.class), yaml);
    }

    static class ClassWithAnnotatedMethod {
        private String id;

        @Schema(description = "a long description for this property")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test(description = "The @Schema annotation will override the type of the actual parameter")
    public void testModelWithOverriddenMemberType() {

        String yaml = "ClassWithOverriddenMemberType:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      description: 'we are declaring a string implementation must be a valid long integer, even though " +
                "the model backs it with a String implementation'\n" +
                "      type: integer\n" +
                "      format: int64";
        SerializationMatchers.assertEqualsToYaml(read(ClassWithOverriddenMemberType.class), yaml);
    }

    static class ClassWithOverriddenMemberType {
        private String id;

        @Schema(type = "integer", format = "int64", description = "we are declaring a string implementation must be " +
                "a valid long integer, even though the model backs it with a String implementation")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test(description = "@Schema is completely overriding the type for this model")
    public void testModelWithAlternateRepresentation() {
        String yaml = "ClassWithAnnotatedMethod:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      description: 'a long description for this property'\n" +
                "      type: string";
        SerializationMatchers.assertEqualsToYaml(read(ClassWithAlternateRepresentation.class), yaml);
    }

    @Schema(implementation = ClassWithAnnotatedMethod.class)
    static class ClassWithAlternateRepresentation {
        private Date dateField;

        public Date getDateField() {
            return dateField;
        }

        public void setDateField(Date dateField) {
            this.dateField = dateField;
        }
    }

    @Test(description = "@Schema is allowing multiple definition interfaces to represent this model")
    public void testModelWithMultipleRepresentations() {

        String yaml = "anyOf:\n" +
                "- $ref: \"#/components/schemas/UserObject\"\n" +
                "- $ref: \"#/components/schemas/EmployeeObject\"\n" +
                "type: object\n" +
                "properties:\n" +
                "  id:\n" +
                "    type: string\n" +
                "    format: uuid\n" +
                "  name:\n" +
                "    type: string\n" +
                "  department:\n" +
                "    type: string\n" +
                "required:\n" +
                "  - id";

        String yamlUser = "type: object\n" +
                "description: 'A User Object'\n" +
                "required:\n" +
                "  - id\n" +
                "properties:\n" +
                "  id:\n" +
                "    type: string\n" +
                "    format: uuid\n" +
                "  name:\n" +
                "    type: string\n";

        String yamlEmployee = "type: object\n" +
                "description: An Employee Object\n" +
                "required:\n" +
                "  - department\n" +
                "properties:\n" +
                "  id:\n" +
                "    type: string\n" +
                "    format: email\n" +
                "  department:\n" +
                "    type: string";

        final Map<String, io.swagger.v3.oas.models.media.Schema> schemas = readAll(UberObject.class);
        assertEquals(schemas.size(), 3);
        SerializationMatchers.assertEqualsToYaml(schemas.get("UberObject"), yaml);
        SerializationMatchers.assertEqualsToYaml(schemas.get("UserObject"), yamlUser);
        SerializationMatchers.assertEqualsToYaml(schemas.get("EmployeeObject"), yamlEmployee);

    }

    @Schema(anyOf = {UserObject.class, EmployeeObject.class})
    static class UberObject implements UserObject, EmployeeObject {
        private String id;
        private String name;
        private String department;

        @Override
        public String getDepartment() {
            return department;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Schema(description = "A User Object")
    interface UserObject {
        @Schema(format = "uuid", required = true)
        String getId();

        String getName();
    }

    @Schema(description = "An Employee Object", requiredProperties = {"department"})
    interface EmployeeObject {
        @Schema(format = "email")
        String getId();

        String getDepartment();
    }

    @Test(description = "Shows how @Schema can be used to allow only certain data formats")
    public void testModelWithSpecificFormat() {

        String yaml = "AuthorizedUser:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "      description: 'A valid user social security'\n" +
                "      pattern: '^\\d{3}-?\\d{2}-?\\d{4}$'";
        SerializationMatchers.assertEqualsToYaml(read(ClassWithIdConstraints.class), yaml);
    }

    @Schema(name = "AuthorizedUser")
    static class ClassWithIdConstraints {
        @Schema(pattern = "^\\d{3}-?\\d{2}-?\\d{4}$", description = "A valid user social security")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test(description = "Shows how to restrict a particular schema")
    public void testExcludeSchema() {

        String yaml = "type: object\n" +
                "description: We don't store social security numbers here!\n" +
                "not:\n" +
                "  $ref: \"#/components/schemas/AuthorizedUser\"";

        String yamlUser = "type: object\n" +
                "properties:\n" +
                "  id:\n" +
                "    type: string\n" +
                "    description: 'A valid user social security'\n" +
                "    pattern: '^\\d{3}-?\\d{2}-?\\d{4}$'";
        Map<String, io.swagger.v3.oas.models.media.Schema> map = readAll(ArbitraryDataReceiver.class);
        Yaml.prettyPrint(map);
        SerializationMatchers.assertEqualsToYaml(map.get("ArbitraryDataReceiver"), yaml);
        SerializationMatchers.assertEqualsToYaml(map.get("AuthorizedUser"), yamlUser);

    }

    @Schema(not = ClassWithIdConstraints.class, description = "We don't store social security numbers here!")
    static class ArbitraryDataReceiver {
    }

    @Test(description = "Shows how to override a definition with a schema reference")
    public void testSchemaReference() {

        String yaml = "NotAPet:\n" +
                "  $ref: http://petstore.swagger.io/v2/swagger.json#/definitions/Tag";
        SerializationMatchers.assertEqualsToYaml(read(NotAPet.class), yaml);

    }

    @Schema(ref = "http://petstore.swagger.io/v2/swagger.json#/definitions/Tag")
    static class NotAPet {
    }

    @Test(description = "Shows how to add a reference on a property")
    public void testPropertySchemaReference() {

        String yaml = "ModelWithSchemaPropertyReference:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    notATag:\n" +
                "      $ref: 'http://petstore.swagger.io/v2/swagger.json#/definitions/Tag'";
        SerializationMatchers.assertEqualsToYaml(read(ModelWithSchemaPropertyReference.class), yaml);

    }

    static class ModelWithSchemaPropertyReference {

        @Schema(ref = "http://petstore.swagger.io/v2/swagger.json#/definitions/Tag")
        private String notATag;

        public String getNotATag() {
            return notATag;
        }

        public void setNotATag(String notATag) {
            this.notATag = notATag;
        }

    }

    @Test(description = "Shows how to override a property name")
    public void testPropertyNameOverride() {

        String yaml = "ModelWithPropertyNameOverride:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    username:\n" +
                "      type: string";
        SerializationMatchers.assertEqualsToYaml(read(ModelWithPropertyNameOverride.class), yaml);

    }

    static class ModelWithPropertyNameOverride {
        public String getDefinitelyNotCalledUsername() {
            return definitelyNotCalledUsername;
        }

        public void setDefinitelyNotCalledUsername(String definitelyNotCalledUsername) {
            this.definitelyNotCalledUsername = definitelyNotCalledUsername;
        }

        @Schema(name = "username")
        private String definitelyNotCalledUsername;
    }

    @Test(description = "Shows how to override a model name")
    public void testModelNameOverride() {

        String yaml = "Employee:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string";
        SerializationMatchers.assertEqualsToYaml(read(ModelWithNameOverride.class), yaml);

    }

    @Schema(name = "Employee")
    static class ModelWithNameOverride {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Test(description = "Shows how to provide model examples")
    public void testModelPropertyExampleOverride() {

        String yaml = "modelWithPropertyExampleOverride:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "      example: abc-123";
        SerializationMatchers.assertEqualsToYaml(read(modelWithPropertyExampleOverride.class), yaml);
    }

    static class modelWithPropertyExampleOverride {
        @Schema(example = "abc-123")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @Test(description = "Shows how to provide model examples as json")
    public void testModelPropertyExampleJson() {

        String yaml = "ExampleJson:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "  minimum: 2\n" +
                "  example:\n" +
                "    id: 19877734\n" +
                "modelWithPropertyExampleOverrideJson:\n" +
                "  minimum: 2\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    exampleJson:\n" +
                "      $ref: '#/components/schemas/ExampleJson'\n" +
                "  example:\n" +
                "    id: 19877734";
        SerializationMatchers.assertEqualsToYaml(readAll(modelWithPropertyExampleOverrideJson.class), yaml);
    }

    @Schema(example = "{\"id\": 19877734}", minimum = "2")
    static class modelWithPropertyExampleOverrideJson {

        @Schema(example = "{\"id\": 19877734}", minimum = "2")
        private ExampleJson exampleJson;

        public ExampleJson getExampleJson() {
            return exampleJson;
        }

        public void setExampleJson(ExampleJson exampleJson) {
            this.exampleJson = exampleJson;
        }

    }

    @Test(description = "Shows how to provide model examples as json")
    public void testModelPropertyImplExampleJson() {

        String yaml = "ExampleJson:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "  example:\n" +
                "    id: 19877734\n" +
                "modelWithPropertyImplExampleOverrideJson:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    exampleJson:\n" +
                "      $ref: '#/components/schemas/ExampleJson'\n";
        SerializationMatchers.assertEqualsToYaml(readAll(modelWithPropertyImplExampleOverrideJson.class), yaml);
    }

    static class modelWithPropertyImplExampleOverrideJson {

        @Schema(implementation = ExampleJson.class, example = "{\"id\": 19877734}")
        private String exampleJson;

        public String getExampleJson() {
            return exampleJson;
        }

        public void setExampleJson(String exampleJson) {
            this.exampleJson = exampleJson;
        }

    }

    @Test(description = "Shows how to provide model examples as json")
    public void testModelPropertyStringExampleJson() {

        String yaml = "modelWithPropertyStringExampleOverrideJson:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      minimum: 2\n" +
                "      type: string\n" +
                "      description: testdesc\n" +
                "      example: '{\"id\":19877734}'";
        SerializationMatchers.assertEqualsToYaml(readAll(modelWithPropertyStringExampleOverrideJson.class), yaml);
    }

    static class modelWithPropertyStringExampleOverrideJson {

        @Schema(type = "string", name = "id", example = "{\"id\": 19877734}", minimum = "2", description = "testdesc")
        private String exampleJson;

        public String getExampleJson() {
            return exampleJson;
        }

        public void setExampleJson(String exampleJson) {
            this.exampleJson = exampleJson;
        }

    }

    static class ExampleJson {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @Test(description = "Shows how to provide an example array")
    public void testExampleArray() {

        String yaml =
                "modelExampleArray:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    ids:\n" +
                        "      type: array\n" +
                        "      example:\n" +
                        "      - abc-123\n" +
                        "      - zz-aa-bb\n" +
                        "      items:\n" +
                        "        type: string";
        Map<String, io.swagger.v3.oas.models.media.Schema> schemaMap = readAll(modelExampleArray.class);
        SerializationMatchers.assertEqualsToYaml(schemaMap, yaml);
    }

    static class modelExampleArray {
        @ArraySchema(schema = @Schema(example = "[\"abc-123\", \"zz-aa-bb\"]"))
        private String[] ids;

        public String[] getIds() {
            return ids;
        }

        public void setIds(String[] ids) {
            this.ids = ids;
        }
    }

    @Test(description = "Show how to completely override an object example")
    public void testModelExampleOverride() {

        String yaml =
                "modelWithExampleOverride:\n" +
                        "  type: object\n" +
                        "  properties:\n" +
                        "    id:\n" +
                        "      type: string\n" +
                        "  example:\n" +
                        "    foo: bar\n" +
                        "    baz: true";

        SerializationMatchers.assertEqualsToYaml(readAll(modelWithExampleOverride.class), yaml);
    }

    @Schema(example = "{\"foo\": \"bar\",\"baz\": true}")
    static class modelWithExampleOverride {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
