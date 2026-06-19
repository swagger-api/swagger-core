package io.swagger.v3.java17.resolving;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.java17.matchers.SerializationMatchers;
import org.testng.annotations.Test;

public class Ticket3293Test extends SwaggerTestBase {

    @Test
    public void testSnakeCaseNamingWithIsPrefixedRecordComponents() {
        ModelResolver modelResolver = new ModelResolver(
                Json.mapper().copy().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        context.resolve(new AnnotatedType(PidLookupResponse.class));

        // Every component must be snake_cased, including the non-boolean "is"-prefixed ones
        // (issuanceDate / issuingCountry / issuingAuthority) which used to leak through as camelCase.
        // A genuine boolean accessor (isActive) keeps behaving correctly.
        String expectedYaml = "PidLookupResponse:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    family_name:\n" +
                "      type: string\n" +
                "    expiry_date:\n" +
                "      type: string\n" +
                "    issuance_date:\n" +
                "      type: string\n" +
                "    issuing_country:\n" +
                "      type: string\n" +
                "    issuing_authority:\n" +
                "      type: string\n" +
                "    is_active:\n" +
                "      type: boolean";

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), expectedYaml);
    }

    record PidLookupResponse(
            String familyName,
            String expiryDate,
            String issuanceDate,
            String issuingCountry,
            String issuingAuthority,
            boolean isActive
    ) {
    }
}
