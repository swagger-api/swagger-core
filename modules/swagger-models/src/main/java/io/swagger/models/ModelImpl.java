package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.models.properties.Property;

import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@XmlType(propOrder = {"type", "required", "discriminator", "properties"})
@JsonPropertyOrder({"type", "required", "discriminator", "properties"})
public class ModelImpl extends AbstractModel {
    public static final String OBJECT = "object";
    private String type;
    private String format;
    private String name;
    private List<String> required;
    private Map<String, Property> properties;
    private Boolean allowEmptyValue;
    private Boolean uniqueItems;
    private boolean isSimple = false;
    private String description;
    private Object example;
    private Property additionalProperties;
    private String discriminator;
    private Xml xml;
    @JsonProperty("default")
    private String defaultValue;
    private List<String> _enum;
    private BigDecimal minimum;
    private BigDecimal maximum;

    public ModelImpl _enum(List<String> value) {
        this._enum = value;
        return this;
    }

    public ModelImpl _enum(String value) {
        if(this._enum == null) {
            this._enum = new ArrayList<String>();
        }
        this._enum.add(value);
        return this;
    }

    public List<String> getEnum() {
        return _enum;
    }

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }


    public ModelImpl discriminator(String discriminator) {
        this.setDiscriminator(discriminator);
        return this;
    }

    public ModelImpl type(String type) {
        this.setType(type);
        return this;
    }

    public ModelImpl format(String format) {
        this.setFormat(format);
        return this;
    }

    public ModelImpl name(String name) {
        this.setName(name);
        return this;
    }


    public ModelImpl uniqueItems(Boolean uniqueItems) {
        this.setUniqueItems(uniqueItems);
        return this;
    }

    public ModelImpl allowEmptyValue(Boolean allowEmptyValue) {
        this.setAllowEmptyValue(allowEmptyValue);
        return this;
    }

    public ModelImpl description(String description) {
        this.setDescription(description);
        return this;
    }

    public ModelImpl property(String key, Property property) {
        this.addProperty(key, property);
        return this;
    }

    public ModelImpl example(Object example) {
        this.setExample(example);
        return this;
    }

    public ModelImpl additionalProperties(Property additionalProperties) {
        this.setAdditionalProperties(additionalProperties);
        return this;
    }

    public ModelImpl required(String name) {
        this.addRequired(name);
        return this;
    }

    public ModelImpl xml(Xml xml) {
        this.setXml(xml);
        return this;
    }

    public ModelImpl minimum(BigDecimal minimum) {
        this.minimum = minimum;
        return this;
    }

    public ModelImpl maximum(BigDecimal maximum) {
        this.maximum = maximum;
        return this;
    }

    public String getDiscriminator() {
        return this.discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @JsonIgnore
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public boolean isSimple() {
        return isSimple;
    }

    public void setSimple(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public Property getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Property additionalProperties) {
        type(OBJECT);
        this.additionalProperties = additionalProperties;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        if(allowEmptyValue != null) {
            this.allowEmptyValue = allowEmptyValue;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void addRequired(String name) {
        if (required == null) {
            required = new ArrayList<String>();
        }
        required.add(name);
        Property p = properties.get(name);
        if (p != null) {
            p.setRequired(true);
        }
    }

    public List<String> getRequired() {
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

    public void setRequired(List<String> required) {
        this.required = required;
        if (properties != null){
            for (String s : required) {
                Property p = properties.get(s);
                if (p != null) {
                    p.setRequired(true);
                }
            }
        }
    }

    public void addProperty(String key, Property property) {
        if (property == null) {
            return;
        }
        if (properties == null) {
            properties = new LinkedHashMap<String, Property>();
        }
        if (required != null) {
            for (String ek : required) {
                if (key.equals(ek)) {
                    property.setRequired(true);
                }
            }
        }
        properties.put(key, property);
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        if (properties != null) {
            for (String key : properties.keySet()) {
                this.addProperty(key, properties.get(key));
            }
        }
    }

    public Object getExample() {
        if (example == null) {
            // TODO: will add logic to construct examples based on payload here
        }

        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public Xml getXml() {
        return xml;
    }

    public void setXml(Xml xml) {
        this.xml = xml;
    }

    public Object getDefaultValue() {
        if(defaultValue == null) {
            return null;
        }

        // don't return a default value if types fail to convert
        try {
            if ("integer".equals(this.type)) {
                return new Integer(defaultValue);
            }
            if ("number".equals(this.type)) {
                return new BigDecimal(defaultValue);
            }
        }
        catch (Exception e) {
            return null;
        }

        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelImpl)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ModelImpl model = (ModelImpl) o;

        if (isSimple != model.isSimple) {
            return false;
        }
        if (type != null ? !type.equals(model.type) : model.type != null) {
            return false;
        }
        if (format != null ? !format.equals(model.format) : model.format != null) {
            return false;
        }
        if (name != null ? !name.equals(model.name) : model.name != null) {
            return false;
        }
        if (required != null ? !required.equals(model.required) : model.required != null) {
            return false;
        }
        if (properties != null ? !properties.equals(model.properties) : model.properties != null) {
            return false;
        }
        if (allowEmptyValue != null ? !allowEmptyValue.equals(model.allowEmptyValue) : model.allowEmptyValue != null) {
            return false;
        }
        if (uniqueItems != null ? !uniqueItems.equals(model.uniqueItems) : model.uniqueItems != null) {
            return false;
        }
        if (description != null ? !description.equals(model.description) : model.description != null) {
            return false;
        }
        if (example != null ? !example.equals(model.example) : model.example != null) {
            return false;
        }
        if (additionalProperties != null ? !additionalProperties.equals(model.additionalProperties) : model.additionalProperties != null) {
            return false;
        }
        if (discriminator != null ? !discriminator.equals(model.discriminator) : model.discriminator != null) {
            return false;
        }
        if (xml != null ? !xml.equals(model.xml) : model.xml != null) {
            return false;
        }
        if (defaultValue != null ? !defaultValue.equals(model.defaultValue) : model.defaultValue != null) {
            return false;
        }
        if (_enum != null ? !_enum.equals(model._enum) : model._enum != null) {
            return false;
        }
        if (minimum != null ? !minimum.equals(model.minimum) : model.minimum != null) {
            return false;
        }
        return maximum != null ? maximum.equals(model.maximum) : model.maximum == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (required != null ? required.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (allowEmptyValue != null ? allowEmptyValue.hashCode() : 0);
        result = 31 * result + (uniqueItems != null ? uniqueItems.hashCode() : 0);
        result = 31 * result + (isSimple ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (example != null ? example.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        result = 31 * result + (discriminator != null ? discriminator.hashCode() : 0);
        result = 31 * result + (xml != null ? xml.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 31 * result + (_enum != null ? _enum.hashCode() : 0);
        result = 31 * result + (minimum != null ? minimum.hashCode() : 0);
        result = 31 * result + (maximum != null ? maximum.hashCode() : 0);
        return result;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;

    }

    public Object clone() {
        ModelImpl cloned = new ModelImpl();
        super.cloneTo(cloned);
        cloned.type = this.type;
        cloned.name = this.name;
        cloned.required = this.required;
        if (this.properties != null) cloned.properties =  new LinkedHashMap<String, Property>(this.properties);
        cloned.isSimple = this.isSimple;
        cloned.description = this.description;
        cloned.example = this.example;
        cloned.additionalProperties = this.additionalProperties;
        cloned.discriminator = this.discriminator;
        cloned.xml = this.xml;
        cloned.defaultValue = this.defaultValue;
        cloned.minimum = this.minimum;
        cloned.maximum = this.maximum;

        return cloned;
    }

}
