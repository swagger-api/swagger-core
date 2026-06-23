package io.swagger.v3.java17.resolving;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Reproduces the property-naming inconsistency reported in
 * springdoc/springdoc-openapi#3293: when a {@link PropertyNamingStrategies} such as
 * {@code SNAKE_CASE} is configured, record components whose names start with the
 * {@code is}/{@code get} accessor prefixes followed by a lower-case letter (e.g.
 * {@code issuanceDate}) were not being translated, because the
 * "avoid clobbering get/is names" hack in {@code ModelResolver} replaced the
 * Jackson-resolved (translated) name with the raw component name.
 */
public class RecordPropertyNamingStrategyTest {

    public record PidLookupResponse(
            String familyName,
            String expiryDate,
            String issuanceDate,
            String issuingCountry,
            String issuingAuthority
    ) {
    }

    @Test
    public void testSnakeCaseNamingStrategyAppliedToRecordComponents() {
        ModelResolver modelResolver = new ModelResolver(
                Json.mapper().copy().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(PidLookupResponse.class), context, null);

        assertTrue(schema.getProperties().containsKey("family_name"));
        assertTrue(schema.getProperties().containsKey("expiry_date"));
        assertTrue(schema.getProperties().containsKey("issuance_date"),
                "expected issuance_date but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("issuing_country"),
                "expected issuing_country but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("issuing_authority"),
                "expected issuing_authority but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 5, "unexpected properties: " + schema.getProperties().keySet());
    }
}
