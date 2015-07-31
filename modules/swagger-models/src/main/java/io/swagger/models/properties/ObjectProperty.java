package io.swagger.models.properties;

import io.swagger.models.Xml;
import java.util.Map;

public class ObjectProperty extends AbstractProperty implements Property {
    public static final String TYPE = "object";

    Map<String, Property> properties;

    public ObjectProperty() {
        super.type = TYPE;
    }

    public ObjectProperty(Map<String, Property> properties) {
        super.type = TYPE;
        this.properties = properties;
    }

    public static boolean isType(String type) {
        return TYPE.equals(type);
    }

    //TODO: implement additional formats
    public static boolean isType(String type, String format) {
        return isType(type);
    }

    public Map<String, Property> getProperties(){
      return this.properties;
    }

    public void setProperties(Map<String, Property> properties){
      this.properties = properties;
    }

    public ObjectProperty xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public ObjectProperty example(String example) {
        this.setExample(example);
        return this;
    }
}
