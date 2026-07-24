package io.swagger.v3.core.jackson;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import jakarta.xml.bind.annotation.XmlElement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
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
            String issuingAuthority,
            String getawayDate
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record JsonNamingPidLookupResponse(
            String familyName,
            String expiryDate,
            String issuanceDate,
            String issuingCountry,
            String issuingAuthority,
            String getawayDate
    ) {
    }

    public record MixInPidLookupResponse(
            String familyName,
            String expiryDate,
            String issuanceDate,
            String issuingCountry,
            String issuingAuthority,
            String getawayDate
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public abstract static class SnakeCaseMixIn {
    }

    public record BooleanStatusResponse(
            boolean isActive
    ) {
    }

    public static class LegacyIsPersistentResponse {

        public boolean is_persistent() {
            return true;
        }

        public String gettersAndHaters() {
            return null;
        }
    }

    public static class XmlRenamedIsPrefixResponse {

        @XmlElement(name = "beerDrinkXmlElement")
        private String isotonicDrinkOnlyXmlElement;

        public String getIsotonicDrinkOnlyXmlElement() {
            return isotonicDrinkOnlyXmlElement;
        }
    }

    public static class CustomPrefixStrategy extends PropertyNamingStrategies.NamingBase {

        @Override
        public String translate(String propertyName) {
            return propertyName == null ? null : "custom_" + propertyName;
        }
    }

    @Test
    public void testSnakeCaseNamingStrategyAppliedToRecordComponents() {
        ModelResolver modelResolver = new ModelResolver(
                ObjectMapperFactory.createJson().rebuild().propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build());
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
        assertTrue(schema.getProperties().containsKey("getaway_date"),
                "expected getaway_date but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 6, "unexpected properties: " + schema.getProperties().keySet());
    }

    @Test
    public void testClassLevelJsonNamingAppliedToRecordComponents() {
        ModelResolver modelResolver = new ModelResolver(ObjectMapperFactory.createJson());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(JsonNamingPidLookupResponse.class), context, null);

        assertNull(modelResolver.objectMapper()
                        .serializationConfig()
                        .getPropertyNamingStrategy());
        assertTrue(schema.getProperties().containsKey("family_name"));
        assertTrue(schema.getProperties().containsKey("expiry_date"));
        assertTrue(schema.getProperties().containsKey("issuance_date"),
                "expected issuance_date but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("issuing_country"),
                "expected issuing_country but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("issuing_authority"),
                "expected issuing_authority but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("getaway_date"),
                "expected getaway_date but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 6, "unexpected properties: " + schema.getProperties().keySet());
    }

    @Test
    public void testMixInJsonNamingAppliedToRecordComponents() {
        ModelResolver modelResolver = new ModelResolver(ObjectMapperFactory.createJson().rebuild().addMixIn(MixInPidLookupResponse.class, SnakeCaseMixIn.class).build());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(MixInPidLookupResponse.class), context, null);

        assertNull(modelResolver.objectMapper()
                .serializationConfig()
                .getPropertyNamingStrategy());
        assertTrue(schema.getProperties().containsKey("family_name"));
        assertTrue(schema.getProperties().containsKey("expiry_date"));
        assertTrue(schema.getProperties().containsKey("issuance_date"),
                "expected issuance_date but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("issuing_country"),
                "expected issuing_country but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("issuing_authority"),
                "expected issuing_authority but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("getaway_date"),
                "expected getaway_date but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 6, "unexpected properties: " + schema.getProperties().keySet());
    }

    @Test
    public void testBooleanIsRecordComponentWithSnakeCaseNamingStrategy() {
        ModelResolver modelResolver = new ModelResolver(
                ObjectMapperFactory.createJson().rebuild().propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(BooleanStatusResponse.class), context, null);

        assertTrue(schema.getProperties().containsKey("is_active"),
                "expected is_active but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 1, "unexpected properties: " + schema.getProperties().keySet());
    }

    @Test
    public void testLegacyIsPersistentNameWithNoNamingStrategy() {
        ModelResolver modelResolver = new ModelResolver(ObjectMapperFactory.createJson());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(LegacyIsPersistentResponse.class), context, null);

        assertNull(modelResolver.objectMapper()
                .serializationConfig()
                .getPropertyNamingStrategy());
        assertTrue(schema.getProperties().containsKey("is_persistent"),
                "expected is_persistent but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("gettersAndHaters"),
                "expected gettersAndHaters but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 2, "unexpected properties: " + schema.getProperties().keySet());
    }

    @Test
    public void testXmlRenamedIsPrefixFieldKeepsOriginalNameWithNoNamingStrategy() {
        ModelResolver modelResolver = new ModelResolver(ObjectMapperFactory.createJson());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(XmlRenamedIsPrefixResponse.class), context, null);

        assertNull(modelResolver.objectMapper()
                .serializationConfig()
                .getPropertyNamingStrategy());
        assertTrue(schema.getProperties().containsKey("isotonicDrinkOnlyXmlElement"),
                "expected isotonicDrinkOnlyXmlElement but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 1, "unexpected properties: " + schema.getProperties().keySet());
    }

    @Test
    public void testCustomNamingStrategyAppliedToGetAndIsPrefixedRecordComponents() {
        ModelResolver modelResolver = new ModelResolver(
                ObjectMapperFactory.createJson().rebuild().propertyNamingStrategy(new CustomPrefixStrategy()).build());
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema<?> schema = modelResolver.resolve(new AnnotatedType(PidLookupResponse.class), context, null);

        assertTrue(schema.getProperties().containsKey("custom_familyName"));
        assertTrue(schema.getProperties().containsKey("custom_expiryDate"));
        assertTrue(schema.getProperties().containsKey("custom_issuanceDate"),
                "expected custom_issuanceDate but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("custom_issuingCountry"),
                "expected custom_issuingCountry but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("custom_issuingAuthority"),
                "expected custom_issuingAuthority but got " + schema.getProperties().keySet());
        assertTrue(schema.getProperties().containsKey("custom_getawayDate"),
                "expected custom_getawayDate but got " + schema.getProperties().keySet());
        assertEquals(schema.getProperties().size(), 6, "unexpected properties: " + schema.getProperties().keySet());
    }
}
