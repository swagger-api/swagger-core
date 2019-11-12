package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.models.properties.Property;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractModel implements Model {
    private ExternalDocs externalDocs;
    private String reference;
    private String title;
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();
    private Xml xml;
    private BigDecimal minimum;
    private BigDecimal maximum;
    private BigDecimal multipleOf;
    private Boolean exclusiveMinimum;
    private Boolean exclusiveMaximum;
    private Integer minLength;
    private Integer maxLength;
    private String pattern;
    protected Map<String, Property> properties;
    protected List<String> required;

    @Override
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        externalDocs = value;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            vendorExtensions.put(name, value);
        }
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

    public BigDecimal getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
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
        if (required != null && properties != null){
            for (String s : required) {
                Property p = properties.get(s);
                if (p != null) {
                    p.setRequired(true);
                }
            }
        }
    }

    public void cloneTo(Object clone) {
        AbstractModel cloned = (AbstractModel) clone;
        cloned.externalDocs = this.externalDocs;
        cloned.reference = reference;
        cloned.title = title;
        cloned.minimum = this.minimum;
        cloned.maximum = this.maximum;
        cloned.minLength = this.minLength;
        cloned.maxLength = this.maxLength;
        cloned.exclusiveMinimum = this.exclusiveMinimum;
        cloned.exclusiveMaximum = this.exclusiveMaximum;
        cloned.pattern = this.pattern;
        cloned.multipleOf = this.multipleOf;
        cloned.pattern = this.pattern;
        if (this.properties != null) cloned.properties =  new LinkedHashMap<String, Property>(this.properties);
        cloned.required = this.required;
        if (vendorExtensions == null) {
            cloned.vendorExtensions = vendorExtensions;
        } else {
            for (String key: vendorExtensions.keySet()) {
                cloned.setVendorExtension(key, vendorExtensions.get(key));
            }
        }
        if (xml == null) {
            cloned.xml = xml;
        } else {
            cloned.xml = (Xml) xml.clone();
        }
    }

    public Object clone() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((externalDocs == null) ? 0 : externalDocs.hashCode());
        result = prime * result + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
        result = prime * result + ((reference == null) ? 0 : reference.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((xml == null) ? 0 : xml.hashCode());
        result = prime * result + (minimum != null ? minimum.hashCode() : 0);
        result = prime * result + (maximum != null ? maximum.hashCode() : 0);
        result = prime * result + (minLength != null ? minLength.hashCode() : 0);
        result = prime * result + (maxLength != null ? maxLength.hashCode() : 0);
        result = prime * result + (exclusiveMinimum != null ? exclusiveMinimum.hashCode() : 0);
        result = prime * result + (exclusiveMaximum != null ? exclusiveMaximum.hashCode() : 0);
        result = prime * result + (pattern != null ? pattern.hashCode() : 0);
        result = prime * result + (multipleOf != null ? multipleOf.hashCode() : 0);
        result = prime * result + (pattern != null ? pattern.hashCode() : 0);
        result = prime * result + (properties != null ? properties.hashCode() : 0);
        result = prime * result + (required != null ? required.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractModel other = (AbstractModel) obj;
        if (externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (reference == null) {
            if (other.reference != null) {
                return false;
            }
        } else if (!reference.equals(other.reference)) {
            return false;
        }
        if (xml == null) {
            if (other.xml != null) {
                return false;
            }
        } else if (!xml.equals(other.xml)) {
            return false;
        }
        if (exclusiveMaximum != null ? !exclusiveMaximum.equals(other.exclusiveMaximum) : other.exclusiveMaximum != null) {
            return false;
        }
        if (exclusiveMinimum != null ? !exclusiveMinimum.equals(other.exclusiveMinimum) : other.exclusiveMinimum != null) {
            return false;
        }
        if (minimum != null ? !minimum.equals(other.minimum) : other.minimum != null) {
            return false;
        }
        if (minLength != null ? !minLength.equals(other.minLength) : other.minLength != null) {
            return false;
        }
        if (maxLength != null ? !maxLength.equals(other.maxLength) : other.maxLength != null) {
            return false;
        }
        if (pattern != null ? !pattern.equals(other.pattern) : other.pattern != null) {
            return false;
        }
        if (multipleOf != null ? !multipleOf.equals(other.multipleOf) : other.multipleOf != null) {
            return false;
        }
        if (pattern != null ? !pattern.equals(other.pattern) : other.pattern != null) {
            return false;
        }        
        if (required != null ? !required.equals(other.required) : other.required != null) {
            return false;
        }
        if (properties != null ? !properties.equals(other.properties) : other.properties != null) {
            return false;
        }

        return maximum != null ? maximum.equals(other.maximum) : other.maximum == null;
    }

    @JsonIgnore
    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public void setReference(String reference) {
        this.reference = reference;
    }

    public Xml getXml() {
        return xml;
    }

    public void setXml(Xml xml) {
        this.xml = xml;
    }

}
