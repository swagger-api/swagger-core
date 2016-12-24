package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.models.Xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public ObjectProperty vendorExtension(String key, Object obj) {
        this.setVendorExtension(key, obj);
        return this;
    }

    public static boolean isType(String type) {
        return TYPE.equals(type);
    }

    //TODO: implement additional formats
    public static boolean isType(String type, String format) {
        return isType(type);
    }

    public ObjectProperty properties(Map<String, Property> properties) {
        this.setProperties(properties);
        return this;
    }

    public ObjectProperty property(String name, Property property) {
        if(this.properties == null) {
            this.properties = new TreeMap<String, Property>();
        }
        this.properties.put(name, property);
        return this;
    }

    public ObjectProperty access(String access) {
        this.setAccess(access);
        return this;
    }

    public ObjectProperty description(String description) {
        this.setDescription(description);
        return this;
    }

    public ObjectProperty name(String name) {
        this.setName(name);
        return this;
    }

    public ObjectProperty title(String title) {
        this.setTitle(title);
        return this;
    }

    public ObjectProperty _default(String _default) {
        this.setDefault(_default);
        return this;
    }

    public ObjectProperty readOnly(boolean readOnly) {
        this.setReadOnly(readOnly);
        return this;
    }

    public ObjectProperty required(boolean required) {
        this.setRequired(required);
        return this;
    }

    public ObjectProperty readOnly() {
        this.setReadOnly(Boolean.TRUE);
        return this;
    }

    public Map<String, Property> getProperties(){
      return this.properties;
    }

    @JsonGetter("required")
    public List<String> getRequiredProperties() {
        List<String> output = new ArrayList<String>();
        if (properties != null) {
            for (String key : properties.keySet()) {
                Property prop = properties.get(key);
                if (prop != null && prop.getRequired()) {
                    output.add(key);
                }
            }
        }
        Collections.sort(output);
        if (output.size() > 0) {
            return output;
        } else {
            return null;
        }
    }

    @JsonSetter("required")
    public void setRequiredProperties(List<String> required) {
        if (properties != null) {
            for (String s : required) {
                Property p = properties.get(s);
                if (p != null) {
                    p.setRequired(true);
                }
            }
        }
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
