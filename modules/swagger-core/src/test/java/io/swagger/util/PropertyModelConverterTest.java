package io.swagger.util;

import io.swagger.annotations.Example;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyModelConverterTest {

    //TODO Test ArrayModel to Property - Test ModelImpl Full to Property

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

    //TODO test with ArrayPropety to ArrayModel - Test RefProperty To RefModel - Test ObjectProperty to Model

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






}
