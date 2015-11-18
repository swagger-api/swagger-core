package io.swagger.models.parameters;

import io.swagger.models.properties.Property;

import java.util.List;

public interface SerializableParameter extends Parameter {
    String getType();

    void setType(String type);

    Property getItems();

    void setItems(Property items);

    String getFormat();

    void setFormat(String format);

    String getCollectionFormat();

    void setCollectionFormat(String collectionFormat);

    List<String> getEnum();

    void setEnum(List<String> _enum);

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

    Double getMaximum();

    void setMaximum(Double maximum);

    Double getMinimum();

    void setMinimum(Double minimum);

    Integer getMaxItems();

    void setMaxItems(Integer maxItems);

    Integer getMinItems();

    void setMinItems(Integer minItems);

}
