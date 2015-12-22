package io.swagger.models.parameters;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BaseIntegerProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

public class AbstractSerializableParameterTest {
  @Test
  public void testGettersAndSetters() {
	  String type="type";
	  String format="format";
	  String collectionFormat="collectionFormat";
	  Property items=new BooleanProperty();
	  List<String> _enum=Arrays.asList("_enum");
	  Boolean exclusiveMaximum=true;
	  Double maximum=1.0;
	  Boolean exclusiveMinimum=true;
	  Double minimum=0.1;
	  String example="example";
	  Integer maxItems=100;
	  Integer minItems=10;
	  Integer maxLength=500;
	  Integer minLength=25;
	  String pattern="String pattern";
	  Boolean uniqueItems=true;
	  Number multipleOf=5;
	  String defaultValue="defaultValue";
	  
	  AbstractSerializableParameter instance=new AbstractSerializableParameterTestImpl();
	  instance.setType(type);
	  instance.setFormat(format);
	  instance.setCollectionFormat(collectionFormat);
	  instance.setItems(items);
	  instance._enum(_enum);
	  instance.setEnum(_enum);
	  instance.setExclusiveMaximum(exclusiveMaximum);
	  instance.setMaximum(maximum);
	  instance.setExclusiveMinimum(exclusiveMinimum);
	  instance.setMinimum(minimum);
	  instance.setExample(example);
	  instance.setMaxItems(maxItems);
	  instance.setMinItems(minItems);
	  instance.setMaxLength(maxLength);
	  instance.setMinLength(minLength);
	  instance.setPattern(pattern);
	  instance.setUniqueItems(uniqueItems);
	  instance.setMultipleOf(multipleOf);
	  instance.setDefaultValue(defaultValue);
	  
	  assertEquals(instance.getType(), type);
	  assertEquals(instance.getFormat(), format);
	  assertEquals(instance.getCollectionFormat(), collectionFormat);
	  assertEquals(instance.getItems(), items);
	  assertEquals(instance.getEnum(), _enum);
	  assertEquals(instance.isExclusiveMaximum(), exclusiveMaximum);
	  assertEquals(instance.getMaximum(), maximum);
	  assertEquals(instance.isExclusiveMinimum(), exclusiveMinimum);
	  assertEquals(instance.getMinimum(), minimum);
	  assertEquals(instance.getExample(), example);
	  assertEquals(instance.getMaxItems(), maxItems);
	  assertEquals(instance.getMinItems(), minItems);
	  assertEquals(instance.getMaxLength(), maxLength);
	  assertEquals(instance.getMinLength(), minLength);
	  assertEquals(instance.getPattern(), pattern);
	  assertEquals(instance.isUniqueItems(), uniqueItems);
	  assertEquals(instance.getMultipleOf(), multipleOf);
	  assertEquals(instance.getDefaultValue(), defaultValue);
	  
	  instance.required(true);
	  assertTrue(instance.getRequired());
	  
	  StringProperty property=new StringProperty();
	  property._enum(_enum);
	  instance.property(property);
	  assertEquals(instance.getEnum(), _enum);
	  assertEquals(instance.getType(), property.getType());
	  
	  ArrayProperty arrayProperty=new ArrayProperty();
	  arrayProperty.items(items);
	  instance.property(arrayProperty);
	  assertEquals(instance.getItems(), items);
	  assertEquals(instance.getType(), arrayProperty.getType());
	  
	  assertEquals(instance.getDefaultCollectionFormat(), "csv");
	  
	  
  }
  
  @Test
  public void testGetDefault(){
	  AbstractSerializableParameter instance=new AbstractSerializableParameterTestImpl();
	  assertNull(instance.getDefault());
	  
	  instance.setProperty(new BaseIntegerProperty());
	  Object defaul=14;
	  instance.setDefault(defaul);
	  assertEquals(instance.getDefault(), 14L);
	  
	  instance.setProperty(new DecimalProperty());
	  defaul=14.1;
	  instance.setDefault(defaul);
	  assertEquals(instance.getDefault(), 14.1);
	  
	  defaul="worng format";
	  instance.setDefault(defaul);
	  assertEquals(instance.getDefault(), defaul);
	  
	  instance.setProperty(new ArrayProperty());
	  assertEquals(instance.getDefault(), defaul);
	  
	  instance.setProperty(new BooleanProperty());
	  defaul=true;
	  instance.setDefault(defaul);
	  assertEquals(instance.getDefault(), true);	  
	  
  }
  
  @Test
  public void testGetExample(){
	  AbstractSerializableParameter instance=new AbstractSerializableParameterTestImpl();
	  assertNull(instance.getExample());
	  
	  instance.setProperty(new BaseIntegerProperty());
	  String example="14";
	  instance.setExample(example);
	  assertEquals(instance.getExample(), 14L);
	  
	  instance.setProperty(new DecimalProperty());
	  example="14.1";
	  instance.setExample(example);
	  assertEquals(instance.getExample(), 14.1);
	  
	  example="worng format";
	  instance.setExample(example);
	  assertEquals(instance.getExample(), example);
	  
	  instance.setProperty(new ArrayProperty());
	  assertEquals(instance.getExample(), example);
	  
	  instance.setProperty(new BooleanProperty());
	  example="true";
	  instance.setExample(example);
	  assertEquals(instance.getExample(), true);	  
	  
  }
}
