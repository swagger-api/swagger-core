package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.testng.Assert;
import tools.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SwaggerAnnotationIntrospectorTest {

    private ModelConverterContextImpl context;

    @BeforeMethod
    public void setup() {
        ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @AfterMethod
    public void tearDown() {
        context = null;
    }

    @Test
    public void testFindSubtypesWithJsonSubTypesOnly() {
        // This test verifies that @JsonSubTypes annotation is properly handled
        // when no @Schema(subTypes) is present (the Jackson 3 regression case)
        final Schema<?> type = context.resolve(new AnnotatedType(Animal.class));

        Assert.assertNotNull(type);
        
        // With @JsonSubTypes support, findSubtypes should return subtypes
        // and they should be available in the resolved schema's oneOf or discriminator
        Assert.assertNotNull(context.getDefinedModels().get("Dog"));
        Assert.assertNotNull(context.getDefinedModels().get("Cat"));
    }

    @Test
    public void testFindSubtypesWithSchemaSubTypesOnly() {
        final Schema<?> type = context.resolve(new AnnotatedType(Vehicle.class));

        Assert.assertNotNull(type);
        
        // Verify @Schema(subTypes) is handled
        Assert.assertNotNull(context.getDefinedModels().get("Car"));
        Assert.assertNotNull(context.getDefinedModels().get("Truck"));
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(name = "Dog", value = Dog.class),
        @JsonSubTypes.Type(name = "Cat", value = Cat.class)
    })
    static class Animal {
        public String type;
    }

    static class Dog extends Animal {
        public String breed;
    }

    static class Cat extends Animal {
        public String color;
    }

    @io.swagger.v3.oas.annotations.media.Schema(
        subTypes = {Car.class, Truck.class}
    )
    static class Vehicle {
    }

    static class Car extends Vehicle {
    }

    static class Truck extends Vehicle {
    }
}
