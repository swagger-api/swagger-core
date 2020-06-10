package io.swagger.v3.core.oas.models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonSubTypes({@JsonSubTypes.Type(value = Thing1.class, name = "thing3"), @JsonSubTypes.Type(value = Thing2.class, name = "thing2")})
@Schema(description = "I am an Abstract Base Model with Sub-Types", discriminatorProperty = "_type")
abstract public class AbstractBaseModelWithSubTypes {

    @Schema(description = "This value is used as a discriminator for serialization")
    public String _type;
    @Schema(description = "An arbitrary field")
    public String a;
    @Schema(description = "An arbitrary field")
    public String b;
}
