package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BaseIntegerProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat", "default", "maximum",
        "exclusiveMaximum", "minimum", "exclusiveMinimum", "maxLength", "minLength", "pattern", "maxItems", "minItems",
        "uniqueItems", "multipleOf" })
public abstract class AbstractSerializableParameter<T extends AbstractSerializableParameter<T>> extends AbstractParameter implements SerializableParameter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSerializableParameter.class);
    protected String format;
    protected Property items;
    protected Boolean exclusiveMaximum;
    protected BigDecimal maximum;
    protected Boolean exclusiveMinimum;
    protected BigDecimal minimum;
    protected String example;
    private Integer maxItems;
    private Integer minItems;

    @JsonIgnore
    protected List<String> _enum;

    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor26
     */
    public Integer maxLength;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor29
     */
    public Integer minLength;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor33
     */
    public String pattern;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor49
     */
    public Boolean uniqueItems;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor14
     */
    public Number multipleOf;

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

    public T allowEmptyValue(Boolean allowEmpty) {
        this.setAllowEmptyValue(allowEmpty);
        return castThis();
    }

    public T readOnly(Boolean readOnly) {
        this.setReadOnly(readOnly);
        return castThis();
    }

    public T items(Property items) {
        this.items = items;
        return castThis();
    }

    public T _enum(List<String> value) {
        this._enum = value;
        return castThis();
    }

    @JsonIgnore
    @Override
    public List<String> getEnum() {
        return _enum;
    }

    @Override
    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    @JsonProperty("enum")
    @Override
    public List<Object> getEnumValue() {
        if (_enum == null) return null;
        if (_enum.isEmpty()) return Collections.emptyList();
        List<Object> oList = new ArrayList<Object>(_enum.size());

        if (BaseIntegerProperty.TYPE.equals(type) || DecimalProperty.TYPE.equals(type)) {
            for (String s : _enum) {
                try {
                    if ("int32".equals(format)) {
                        oList.add(Integer.valueOf(s));
                    } else if ("int64".equals(format)) {
                        oList.add(Long.valueOf(s));
                    } else if ("double".equals(format)) {
                        oList.add(Double.valueOf(s));
                    } else if ("float".equals(format)) {
                        oList.add(Float.valueOf(s));
                    } else if (BaseIntegerProperty.TYPE.equals(type)) {
                        oList.add(Integer.valueOf(s));
                    } else if (DecimalProperty.TYPE.equals(type)) {
                        oList.add(Double.valueOf(s));
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warn(String.format("Illegal enum value %s for parameter type %s", s, type), e);
                    oList.add(s);
                }
            }
        } else if ((type == null || "array".equals(type)) && items != null) {
            for (String s : _enum) {
                try {
                    if (items instanceof StringProperty) {
                        oList.add(s);
                    } else if (items instanceof IntegerProperty) {
                        oList.add(Integer.valueOf(s));
                    } else if (items instanceof LongProperty) {
                        oList.add(Long.valueOf(s));
                    } else if (items instanceof FloatProperty) {
                        oList.add(Float.valueOf(s));
                    } else if (items instanceof DoubleProperty) {
                        oList.add(Double.valueOf(s));
                    } else if (items instanceof BaseIntegerProperty) {
                        oList.add(Integer.valueOf(s));
                    } else if (items instanceof DecimalProperty) {
                        oList.add(Double.valueOf(s));
                    } else {
                        oList.add(s);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warn(String.format("Illegal enum value %s for parameter type %s", s, type), e);
                    oList.add(s);
                }
            }

        } else {
            for (String s: _enum) {
                oList.add(s);
            }
        }
        return oList;
    }

    @Override
    public void setEnumValue(List<?> enumValue) {
        if (enumValue == null) {
            this._enum = null;
            return;
        }
        if (enumValue.isEmpty()) {
            this._enum = Collections.emptyList();
            return;
        }
        List<String> sList = new ArrayList<String>(enumValue.size());
        for (Object item: enumValue) {
            sList.add(item.toString());
        }
        this._enum = sList;
    }

    @Override
    public Property getItems() {
        return items;
    }

    @Override
    public void setItems(Property items) {
        this.items = items;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    public void setProperty(Property property) {
        setType(property.getType());
        this.format = property.getFormat();
        if (property instanceof StringProperty) {
            final StringProperty string = (StringProperty) property;
            setEnum(string.getEnum());
        } else if (property instanceof IntegerProperty) {
            setEnumValue(((IntegerProperty) property).getEnum());
        } else if (property instanceof LongProperty) {
            setEnumValue(((LongProperty) property).getEnum());
        } else if (property instanceof FloatProperty) {
            setEnumValue(((FloatProperty) property).getEnum());
        } else if (property instanceof DoubleProperty) {
            setEnumValue(((DoubleProperty) property).getEnum());
        } else if (property instanceof ArrayProperty) {
            final ArrayProperty array = (ArrayProperty) property;
            setItems(array.getItems());
        }
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

    public Object getDefault() {
        if (defaultValue == null || defaultValue.isEmpty()) {
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

    @Override
    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    @Override
    public BigDecimal getMaximum() {
        return maximum;
    }

    @Override
    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    @Override
    public Boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    @Override
    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    @Override
    public BigDecimal getMinimum() {
        return minimum;
    }

    @Override
    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    @Override
    public Integer getMaxItems() {
        return maxItems;
    }

    @Override
    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    @Override
    public Integer getMinItems() {
        return minItems;
    }

    @Override
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

    @Override
    public Integer getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public Integer getMinLength() {
        return minLength;
    }

    @Override
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Boolean isUniqueItems() {
        return uniqueItems;
    }

    @Override
    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    @Override
    public Number getMultipleOf() {
        return multipleOf;
    }

    @Override
    public void setMultipleOf(Number multipleOf) {
        this.multipleOf = multipleOf;
    }

    @Override
    public Boolean isExclusiveMaximum() {
        return exclusiveMaximum;
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractSerializableParameter<?> other = (AbstractSerializableParameter<?>) obj;
        if (_enum == null) {
            if (other._enum != null) return false;
        } else if (!_enum.equals(other._enum)) return false;
        if (defaultValue == null) {
            if (other.defaultValue != null) return false;
        } else if (!defaultValue.equals(other.defaultValue)) return false;
        if (example == null) {
            if (other.example != null) return false;
        } else if (!example.equals(other.example)) return false;
        if (exclusiveMaximum == null) {
            if (other.exclusiveMaximum != null) return false;
        } else if (!exclusiveMaximum.equals(other.exclusiveMaximum)) return false;
        if (exclusiveMinimum == null) {
            if (other.exclusiveMinimum != null) return false;
        } else if (!exclusiveMinimum.equals(other.exclusiveMinimum)) return false;
        if (format == null) {
            if (other.format != null) return false;
        } else if (!format.equals(other.format)) return false;
        if (items == null) {
            if (other.items != null) return false;
        } else if (!items.equals(other.items)) return false;
        if (maxItems == null) {
            if (other.maxItems != null) return false;
        } else if (!maxItems.equals(other.maxItems)) return false;
        if (maxLength == null) {
            if (other.maxLength != null) return false;
        } else if (!maxLength.equals(other.maxLength)) return false;
        if (maximum == null) {
            if (other.maximum != null) return false;
        } else if (!maximum.equals(other.maximum)) return false;
        if (minItems == null) {
            if (other.minItems != null) return false;
        } else if (!minItems.equals(other.minItems)) return false;
        if (minLength == null) {
            if (other.minLength != null) return false;
        } else if (!minLength.equals(other.minLength)) return false;
        if (minimum == null) {
            if (other.minimum != null) return false;
        } else if (!minimum.equals(other.minimum)) return false;
        if (multipleOf == null) {
            if (other.multipleOf != null) return false;
        } else if (!multipleOf.equals(other.multipleOf)) return false;
        if (pattern == null) {
            if (other.pattern != null) return false;
        } else if (!pattern.equals(other.pattern)) return false;
        if (uniqueItems == null) {
            if (other.uniqueItems != null) return false;
        } else if (!uniqueItems.equals(other.uniqueItems)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((_enum == null) ? 0 : _enum.hashCode());
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + ((example == null) ? 0 : example.hashCode());
        result = prime * result + ((exclusiveMaximum == null) ? 0 : exclusiveMaximum.hashCode());
        result = prime * result + ((exclusiveMinimum == null) ? 0 : exclusiveMinimum.hashCode());
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((maxItems == null) ? 0 : maxItems.hashCode());
        result = prime * result + ((maxLength == null) ? 0 : maxLength.hashCode());
        result = prime * result + ((maximum == null) ? 0 : maximum.hashCode());
        result = prime * result + ((minItems == null) ? 0 : minItems.hashCode());
        result = prime * result + ((minLength == null) ? 0 : minLength.hashCode());
        result = prime * result + ((minimum == null) ? 0 : minimum.hashCode());
        result = prime * result + ((multipleOf == null) ? 0 : multipleOf.hashCode());
        result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
        result = prime * result + ((uniqueItems == null) ? 0 : uniqueItems.hashCode());
        return result;
    }
}
