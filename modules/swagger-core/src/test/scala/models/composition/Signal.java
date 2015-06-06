package models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = DigitalSignal.class, name = "digital"),
        @Type(value = AnalogSignal.class, name = "analog")
})
public interface Signal {
    int getType();

    void setType(int type);

    byte[] getRawValue();

    void setRawValue(byte[] value);
}