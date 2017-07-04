package io.swagger.models.parameters;

import io.swagger.models.properties.Property;

import java.math.BigDecimal;
import java.util.List;

public interface SerializableParameter extends Parameter {

    Property getItems();

    void setItems(Property items);

    String getFormat();

    void setFormat(String format);

    List<String> getEnum();

    void setEnum(List<String> _enum);

    List<Object> getEnumValue();

    void setEnumValue(List<?> enumValue);

    Integer getMaxLength();

    void setMaxLength(Integer maxLength);

    Integer getMinLength();

    void setMinLength(Integer minLength);

    String getPattern();

    void setPattern(String pattern);

    Boolean isUniqueItems();

    void setUniqueItems(Boolean uniqueItems);

    Number getMultipleOf();

    void setMultipleOf(Number multipleOf);

    Boolean isExclusiveMaximum();

    void setExclusiveMaximum(Boolean exclusiveMinimum);

    Boolean isExclusiveMinimum();

    void setExclusiveMinimum(Boolean exclusiveMinimum);

    BigDecimal getMaximum();

    void setMaximum(BigDecimal maximum);

    BigDecimal getMinimum();

    void setMinimum(BigDecimal minimum);

    Integer getMaxItems();

    void setMaxItems(Integer maxItems);

    Integer getMinItems();

    void setMinItems(Integer minItems);

}
