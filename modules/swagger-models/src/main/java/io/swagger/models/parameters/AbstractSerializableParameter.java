package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BaseIntegerProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import java.util.List;

@JsonPropertyOrder({"name", "in", "description", "required", "type", "items", "collectionFormat", "default",
        "maximum", "exclusiveMaximum", "minimum", "exclusiveMinimum", "maxItems", "minItems"})
public abstract class AbstractSerializableParameter<T extends AbstractSerializableParameter<T>> extends AbstractParameter implements SerializableParameter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSerializableParameter.class);
    protected String type;
    protected String format;
    protected String collectionFormat;
    protected Property items;
    protected List<String> _enum;
    protected Boolean exclusiveMaximum;
    protected Double maximum;
    protected Boolean exclusiveMinimum;
    protected Double minimum;
    protected String example;
    private Integer maxItems;
    private Integer minItems;

    @JsonIgnore
    protected String defaultValue;

    public T property(Property property) {
        this.setProperty(property);
        return castThis();
    }

    public T type(String type) {
        this.setType(type);
        return castThis();
    }

    public T format(String format) {
        this.setFormat(format);
        return castThis();
    }

    public T description(String description) {
        this.setDescription(description);
        return castThis();
    }

    public T name(String name) {
        this.setName(name);
        return castThis();
    }

    public T required(boolean required) {
        this.setRequired(required);
        return castThis();
    }

    public T collectionFormat(String collectionFormat) {
        this.setCollectionFormat(collectionFormat);
        return castThis();
    }

    public T example(String example) {
        this.setExample(example);
        return castThis();
    }

    @JsonIgnore
    protected String getDefaultCollectionFormat() {
        return "csv";
    }

    public T items(Property items) {
        this.items = items;
        return castThis();
    }

    public T _enum(List<String> value) {
        this._enum = value;
        return castThis();
    }

    public List<String> getEnum() {
        return _enum;
    }

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    public Property getItems() {
        return items;
    }

    public void setItems(Property items) {
        this.items = items;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        setCollectionFormat(ArrayProperty.isType(type) ? getDefaultCollectionFormat() : null);
    }

    public String getCollectionFormat() {
        return collectionFormat;
    }

    public void setCollectionFormat(String collectionFormat) {
        this.collectionFormat = collectionFormat;
    }

    public void setProperty(Property property) {
        setType(property.getType());
        this.format = property.getFormat();
        if (property instanceof StringProperty) {
            final StringProperty string = (StringProperty) property;
            setEnum(string.getEnum());
        } else if (property instanceof ArrayProperty) {
            final ArrayProperty array = (ArrayProperty) property;
            setItems(array.getItems());
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object getDefault() {
        if (defaultValue == null) {
            return null;
        }
        try {
            if (BaseIntegerProperty.TYPE.equals(type)) {
                return Long.valueOf(defaultValue);
            } else if (DecimalProperty.TYPE.equals(type)) {
                return Double.valueOf(defaultValue);
            } else if (BooleanProperty.TYPE.equals(type)) {
                if ("true".equalsIgnoreCase(defaultValue) || "false".equalsIgnoreCase(defaultValue)) {
                    return Boolean.valueOf(defaultValue);
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.warn(String.format("Illegal DefaultValue %s for parameter type %s", defaultValue, type), e);
        }
        return defaultValue;
    }

    public void setDefault(Object defaultValue) {
        this.defaultValue = defaultValue == null ? null : defaultValue.toString();
    }

    public Boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    @JsonProperty("x-example")
    public Object getExample() {
        if (example == null) {
            return null;
        }
        try {
            if (BaseIntegerProperty.TYPE.equals(type)) {
                return Long.valueOf(example);
            } else if (DecimalProperty.TYPE.equals(type)) {
                return Double.valueOf(example);
            } else if (BooleanProperty.TYPE.equals(type)) {
                if ("true".equalsIgnoreCase(example) || "false".equalsIgnoreCase(defaultValue)) {
                    return Boolean.valueOf(example);
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.warn(String.format("Illegal DefaultValue %s for parameter type %s", defaultValue, type), e);
        }
        return example;
    }
    public void setExample(String example) {
        this.example = example;
    }

    @JsonIgnore
    private T castThis() {
        @SuppressWarnings("unchecked")
        final T result = (T) this;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractSerializableParameter)) return false;
        if (!super.equals(o)) return false;

        AbstractSerializableParameter<?> that = (AbstractSerializableParameter<?>) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getFormat() != null ? !getFormat().equals(that.getFormat()) : that.getFormat() != null) return false;
        if (getCollectionFormat() != null ? !getCollectionFormat().equals(that.getCollectionFormat()) : that.getCollectionFormat() != null)
            return false;
        if (getItems() != null ? !getItems().equals(that.getItems()) : that.getItems() != null) return false;
        if (_enum != null ? !_enum.equals(that._enum) : that._enum != null) return false;
        if (exclusiveMaximum != null ? !exclusiveMaximum.equals(that.exclusiveMaximum) : that.exclusiveMaximum != null)
            return false;
        if (getMaximum() != null ? !getMaximum().equals(that.getMaximum()) : that.getMaximum() != null) return false;
        if (exclusiveMinimum != null ? !exclusiveMinimum.equals(that.exclusiveMinimum) : that.exclusiveMinimum != null)
            return false;
        if (getMinimum() != null ? !getMinimum().equals(that.getMinimum()) : that.getMinimum() != null) return false;
        if (getExample() != null ? !getExample().equals(that.getExample()) : that.getExample() != null) return false;
        if (getMaxItems() != null ? !getMaxItems().equals(that.getMaxItems()) : that.getMaxItems() != null)
            return false;
        if (getMinItems() != null ? !getMinItems().equals(that.getMinItems()) : that.getMinItems() != null)
            return false;
        return !(getDefaultValue() != null ? !getDefaultValue().equals(that.getDefaultValue()) : that.getDefaultValue() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getFormat() != null ? getFormat().hashCode() : 0);
        result = 31 * result + (getCollectionFormat() != null ? getCollectionFormat().hashCode() : 0);
        result = 31 * result + (getItems() != null ? getItems().hashCode() : 0);
        result = 31 * result + (_enum != null ? _enum.hashCode() : 0);
        result = 31 * result + (exclusiveMaximum != null ? exclusiveMaximum.hashCode() : 0);
        result = 31 * result + (getMaximum() != null ? getMaximum().hashCode() : 0);
        result = 31 * result + (exclusiveMinimum != null ? exclusiveMinimum.hashCode() : 0);
        result = 31 * result + (getMinimum() != null ? getMinimum().hashCode() : 0);
        result = 31 * result + (getExample() != null ? getExample().hashCode() : 0);
        result = 31 * result + (getMaxItems() != null ? getMaxItems().hashCode() : 0);
        result = 31 * result + (getMinItems() != null ? getMinItems().hashCode() : 0);
        result = 31 * result + (getDefaultValue() != null ? getDefaultValue().hashCode() : 0);
        return result;
    }
}
