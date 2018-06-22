package io.swagger.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.*;
import io.swagger.models.utils.PropertyModelConverter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyModelConverterTest {

    @Test
    public void convertToUUIDProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: uuid\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof UUIDProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"uuid");
    }

    @Test
    public void convertToEmailProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: email\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof EmailProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"email");
    }

    @Test
    public void convertToBooleanProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: boolean\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof BooleanProperty);
        Assert.assertEquals(property.getType(),"boolean");
    }

    @Test
    public void convertToDateProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: date\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof DateProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"date");
    }

    @Test
    public void convertToDateTimeProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: date-time\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof DateTimeProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"date-time");
    }

    @Test
    public void convertToStringProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: password\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof StringProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"password");
    }

    @Test
    public void convertToStringWithEnumProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            enum:\n" +
                "              - a\n" +
                "              - b\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof StringProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(((StringProperty)property).getEnum().size(),2);
    }

    @Test
    public void convertToStringNewProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: password\n"+
                "            pattern: Pattern\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof StringProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"password");
        Assert.assertEquals(((StringProperty)property).getPattern(),"Pattern");
    }

    @Test
    public void convertToBinaryProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: binary\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof BinaryProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"binary");
    }

    @Test
    public void convertToDoubleProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: number\n"+
                "            format: double\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof DoubleProperty);
        Assert.assertEquals(property.getType(),"number");
        Assert.assertEquals(property.getFormat(),"double");
    }

    @Test
    public void convertToNumericNewProperties()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: number\n"+
                "            minimum: 1\n"+
                "            maximum: 100\n"+
                "            format: double\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof DoubleProperty);
        Assert.assertEquals(property.getType(),"number");
        Assert.assertEquals(property.getFormat(),"double");
        Assert.assertEquals(((DoubleProperty)property).getMinimum(), new BigDecimal(1));
        Assert.assertEquals(((DoubleProperty)property).getMaximum(), new BigDecimal(100));
    }

    @Test
    public void convertToByteArrayProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: string\n"+
                "            format: byte\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof ByteArrayProperty);
        Assert.assertEquals(property.getType(),"string");
        Assert.assertEquals(property.getFormat(),"byte");
    }

    @Test
    public void convertToLongProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: integer\n"+
                "            format: int64\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof LongProperty);
        Assert.assertEquals(property.getType(),"integer");
        Assert.assertEquals(property.getFormat(),"int64");
    }

    @Test
    public void convertToIntegerProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: integer\n"+
                "            format: int32\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof IntegerProperty);
        Assert.assertEquals(property.getType(),"integer");
        Assert.assertEquals(property.getFormat(),"int32");
    }

    @Test
    public void convertToArrayProperty()throws Exception{
        String yaml = "      produces:\n" +
                "        - application/json\n" +
                "      parameters:\n" +
                "        []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: array\n" +
                "            items:\n" +
                "              type: string\n" +
                "              format: date-time\n" +
                "              example: 1985-04-12T23:20:50.52Z";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponsesObject().get("200");
        Assert.assertNotNull(response);
        Property property = response.getSchema();

        Assert.assertTrue(property instanceof ArrayProperty);
        ArrayProperty arrayProperty = (ArrayProperty) property;
        Assert.assertEquals(property.getType(),"array");
        Assert.assertEquals(arrayProperty.getItems().getType(),"string");
        Assert.assertEquals(arrayProperty.getItems().getFormat(),"date-time");
        Assert.assertEquals(arrayProperty.getItems().getExample(),"1985-04-12T23:20:50.52Z");
    }

    @Test
    public void convertArrayModel()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/models.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Model arrayModel  = swagger.getDefinitions().get("InstructionSequence");

        PropertyModelConverter converter = new PropertyModelConverter();
        Property convertedProperty = converter.modelToProperty(arrayModel);

        Assert.assertTrue(convertedProperty instanceof ArrayProperty);
        ArrayProperty property = (ArrayProperty) convertedProperty;

        Assert.assertEquals(property.getTitle(),"InstructionSequence");
        Assert.assertEquals(property.getDescription(),"The sequence of steps that make up the Instructions");
        Assert.assertEquals(property.getType(),"array");
        Assert.assertTrue(property.getItems() instanceof StringProperty);
        Assert.assertEquals(property.getItems().getType(),"string");
    }

    @Test
    public void convertArrayWithNewPropertiesModel()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/models.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Model arrayModel  = swagger.getDefinitions().get("NewSequence");

        PropertyModelConverter converter = new PropertyModelConverter();
        Property convertedProperty = converter.modelToProperty(arrayModel);

        Assert.assertTrue(convertedProperty instanceof ArrayProperty);
        ArrayProperty property = (ArrayProperty) convertedProperty;

        Assert.assertEquals(property.getTitle(),"NewSequence");
        Assert.assertEquals(property.getDescription(),"A new sequence of steps that make up the Instructions");
        Assert.assertEquals(property.getType(),"array");
        Assert.assertTrue(property.getItems() instanceof StringProperty);
        final StringProperty stringProperty = (StringProperty) property.getItems();
        Assert.assertEquals(property.getItems().getType(),"string");
        /*Assert.assertEquals(stringProperty.getPattern(),"Pattern");
        Assert.assertEquals(property.getExclusiveMaximum(), Boolean.TRUE);
        Assert.assertEquals(property.getExclusiveMinimum(), Boolean.TRUE);*/

    }

    @Test
    public void convertAddressModel()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/models.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Model arrayModel  = swagger.getDefinitions().get("Address");

        PropertyModelConverter converter = new PropertyModelConverter();
        Property convertedProperty = converter.modelToProperty(arrayModel);

        Assert.assertTrue(convertedProperty instanceof ObjectProperty);
        ObjectProperty property = (ObjectProperty) convertedProperty;

        Assert.assertEquals(property.getType(),"object");
        Assert.assertEquals(property.getRequiredProperties().get(0),"street");
        Assert.assertEquals(property.getProperties().get("city").getType(),"string");
    }

    @Test
    public void composedExtendedModelToPropertyTest()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/models.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Model composedModel  = swagger.getDefinitions().get("ExtendedAddress");

        PropertyModelConverter converter = new PropertyModelConverter();
        Property convertedProperty = converter.modelToProperty(composedModel);

        Assert.assertTrue(convertedProperty instanceof ObjectProperty);
        ObjectProperty objectProperty = (ObjectProperty) convertedProperty;
        Assert.assertEquals(objectProperty.getType(),"object");

        Assert.assertEquals(objectProperty.getProperties().get("Address").getType(),"ref");
        Assert.assertEquals(objectProperty.getProperties().get("gps").getType(),"string");
        Assert.assertEquals(objectProperty.getRequiredProperties().get(0),"gps");
    }

    @Test
    public void stringNewPropertiesModelToPropertyTest()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/models.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Model composedModel  = swagger.getDefinitions().get("NewExtendedStringAddress");

        PropertyModelConverter converter = new PropertyModelConverter();
        Property convertedProperty = converter.modelToProperty(composedModel);

        Assert.assertTrue(convertedProperty instanceof StringProperty);
        StringProperty objectProperty = (StringProperty) convertedProperty;
        Assert.assertEquals(objectProperty.getType(),"string");

        Assert.assertEquals(objectProperty.getPattern(),"Pattern");
        Assert.assertEquals(objectProperty.getMinLength(), new Integer(10));
        Assert.assertEquals(objectProperty.getMaxLength(), new Integer(50));
    }

    @Test
    public void numericNewPropertiesModelToPropertyTest()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/models.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Model composedModel  = swagger.getDefinitions().get("NewExtendedNumericAddress");

        PropertyModelConverter converter = new PropertyModelConverter();
        Property convertedProperty = converter.modelToProperty(composedModel);

        Assert.assertTrue(convertedProperty instanceof AbstractNumericProperty);
        AbstractNumericProperty objectProperty = (AbstractNumericProperty) convertedProperty;
        Assert.assertEquals(objectProperty.getType(),"number");
        Assert.assertEquals(objectProperty.getExclusiveMaximum(), Boolean.TRUE);
        Assert.assertEquals(objectProperty.getExclusiveMinimum(), Boolean.TRUE);
        Assert.assertEquals(objectProperty.getMultipleOf(),new BigDecimal(5));
        Assert.assertEquals(objectProperty.getMinimum(),new BigDecimal(1));
        Assert.assertEquals(objectProperty.getMaximum(),new BigDecimal(100));
    }


    @Test
    public void composedModelToPropertyTest(){
        List<Model> list = new ArrayList();
        List<String> requiredList = new ArrayList();
        requiredList.add("url");

        ModelImpl model = new ModelImpl();
        model.setType("object");
        model.setRequired(requiredList);

        Property property = new StringProperty();
        property.setDescription("Url with information or picture of the product");
        Map<String, Property> propertyMap = new HashMap();
        propertyMap.put("url", property);
        model.setProperties(propertyMap);
        list.add(model);

        RefModel refModel1 = new RefModel("#/definitions/ReferencedObject1");
        list.add(refModel1);

        RefModel refModel2 = new RefModel("#/definitions/ReferencedObject2");
        list.add(refModel2);

        ComposedModel composedModel = new ComposedModel();
        composedModel.setDescription("AllOf test");
        composedModel.setExample("Example");
        composedModel.setAllOf(list);

        PropertyModelConverter converter = new PropertyModelConverter();
        Property convertedProperty = converter.modelToProperty(composedModel);

        Assert.assertTrue(convertedProperty instanceof ObjectProperty);
        ObjectProperty objectProperty = (ObjectProperty) convertedProperty;
        Assert.assertEquals(objectProperty.getType(),"object");
        Assert.assertEquals(objectProperty.getExample(),"Example");
        Assert.assertEquals(objectProperty.getDescription(),"AllOf test");
        Assert.assertEquals(objectProperty.getProperties().get("url").getType(),"string");
        Assert.assertEquals(objectProperty.getProperties().get("ReferencedObject1").getType(),"ref");
        Assert.assertEquals(objectProperty.getRequiredProperties().get(0),"url");
    }

    @Test
    public void convertPropertyToModel(){
        IntegerProperty integerProperty = new IntegerProperty();
        MapProperty mapProperty = new MapProperty();
        mapProperty.setAdditionalProperties(integerProperty);

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(mapProperty);

        Assert.assertTrue(convertedModel instanceof ModelImpl);
        ModelImpl model = (ModelImpl) convertedModel;
        Assert.assertEquals(model.getType(),"object");
        Assert.assertEquals(model.getAdditionalProperties().getType(),"integer");
    }

    @Test
    public void convertStringProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/string");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof ModelImpl);
        ModelImpl model = (ModelImpl) convertedModel;
        Assert.assertEquals(model.getType(),"string");
        Assert.assertEquals(model.getExample(),"Hello");
    }

    @Test
    public void convertStringPropertyWithEnum()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/stringenum");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        Assert.assertEquals(((StringProperty)property).getEnum().size(),2);

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof ModelImpl);
        ModelImpl model = (ModelImpl) convertedModel;
        Assert.assertEquals(model.getType(),"string");
        Assert.assertEquals(model.getExample(),"Hello");
        Assert.assertEquals(model.getEnum().size(),2);
    }

    @Test
    public void convertStringRefProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/stringRef");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof RefModel);
        RefModel model = (RefModel) convertedModel;
        Assert.assertEquals(model.get$ref(),"#/definitions/DayOfWeekAsString");
    }

    @Test
    public void convertBooleanProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/boolean");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof ModelImpl);
        ModelImpl model = (ModelImpl) convertedModel;
        Assert.assertEquals(model.getType(),"boolean");
        Assert.assertEquals(model.getExample(),true);
    }

    @Test
    public void convertNumberProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/number");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof ModelImpl);
        ModelImpl model = (ModelImpl) convertedModel;
        Assert.assertEquals(model.getType(),"number");
        Assert.assertEquals(model.getExample(),1.5);
    }

    @Test
    public void convertArrayProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/arrayOfInt");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof ArrayModel);
        ArrayModel model = (ArrayModel) convertedModel;
        Assert.assertEquals(model.getType(),"array");
        Assert.assertEquals(model.getItems().getType(),"integer");
    }

    @Test
    public void convertArrayOfRefProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/arrayOfRef");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof ArrayModel);
        ArrayModel model = (ArrayModel) convertedModel;
        Assert.assertEquals(model.getType(),"array");
        Assert.assertEquals(model.getItems().getType(),"ref");
        RefProperty items = (RefProperty) model.getItems();
        Assert.assertEquals(items.get$ref(),"#/definitions/User");
    }

    @Test
    public void convertArrayRefProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/arrayRef");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof RefModel);
        RefModel model = (RefModel) convertedModel;
        Assert.assertEquals(model.get$ref(),"#/definitions/ArrayOfint");
    }

    @Test
    public void convertObjectProperty()throws Exception{
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final JsonNode rootNode = mapper.readTree(Files.readAllBytes(java.nio.file.Paths.get(getClass().getResource("/specFiles/responses.yaml").toURI())));

        String specAsYaml = rootNode.toString();

        Swagger swagger = Yaml.mapper().readValue(specAsYaml, Swagger.class);

        Path string  = swagger.getPaths().get("/object");
        Operation operation = string.getOperations().get(0);
        Response response = operation.getResponsesObject().get("200");
        Property property = response.getSchema();

        PropertyModelConverter converter = new PropertyModelConverter();
        Model convertedModel = converter.propertyToModel(property);

        Assert.assertTrue(convertedModel instanceof ModelImpl);
        ModelImpl model = (ModelImpl) convertedModel;
        Assert.assertEquals(model.getType(),"object");
        Assert.assertEquals(model.getProperties().get("id").getType(), "integer");
        Assert.assertEquals(model.getProperties().get("id").getFormat(), "int64");
        Assert.assertEquals(model.getProperties().get("name").getType(), "string");
        Assert.assertEquals(model.getRequired().get(0), "id");
        Assert.assertEquals(model.getRequired().get(1), "name");
    }
}
