package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.EncodingProperty;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;

public class DeserializationModule extends SimpleModule {

    public DeserializationModule(boolean includePathDeserializer,
                                 boolean includeResponseDeserializer) {

        if (includePathDeserializer) {
            this.addDeserializer(PathItem.class, new PathDeserializer());
        }
        if (includeResponseDeserializer) {
            this.addDeserializer(ApiResponse.class, new ResponseDeserializer());
        }

        this.addDeserializer(Schema.class, new ModelDeserializer());
        this.addDeserializer(Parameter.class, new ParameterDeserializer());
//        this.addDeserializer(RequestBody.class, new RequestBodyDeserializer());
        this.addDeserializer(Header.StyleEnum.class, new HeaderStyleEnumDeserializer());
        this.addDeserializer(Encoding.StyleEnum.class, new EncodingStyleEnumDeserializer());
        this.addDeserializer(EncodingProperty.StyleEnum.class, new EncodingPropertyStyleEnumDeserializer());


        this.addDeserializer(SecurityScheme.class, new SecuritySchemeDeserializer());
    }

    public DeserializationModule() {
        this(true, true);
    }
}
