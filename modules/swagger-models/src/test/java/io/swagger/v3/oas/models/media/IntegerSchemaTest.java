package io.swagger.v3.oas.models.media;

import org.testng.annotations.Test;

import java.util.Locale;

import static org.testng.Assert.*;

public class IntegerSchemaTest {

    private static final IntegerSchema INTEGER_SCHEMA = new IntegerSchema();
    private static final Locale SWEDISH_LOCALE = new Locale("se");
    private static final String INTEGER_STRING = "123";
    private static final String LONG_STRING = "1111111111111111111";
    private static final String NEGATIVE_INTEGER = "-123";
    private static final String INTEGER_WITH_SPACE_THOUSAND_SEPARATOR = "2 000";
    private static final String INTEGER_WITH_DECIMAL_THOUSAND_SEPARATOR = "2.000";
    private static final String INTEGER_WITH_COMMA_THOUSAND_SEPARATOR = "2,000";
    private static final Integer NUMBER = 123;
    private static final Integer NEGATIVE_NUMBER = -123;
    private static final Long LONG = 1111111111111111111L;

    @Test
    public void testCastInteger() {
        assertEquals(INTEGER_SCHEMA.cast(INTEGER_STRING), NUMBER);
    }

    @Test
    public void testCastLong() {
        assertEquals(INTEGER_SCHEMA.cast(LONG_STRING), LONG);
    }

    @Test
    public void testCastNegativeInteger() {
        assertEquals(INTEGER_SCHEMA.cast(NEGATIVE_INTEGER), NEGATIVE_NUMBER);
    }

    @Test
    public void testCastIntegerWithSpaceThousandSeparatorFailsWithNull() {
        assertNull(INTEGER_SCHEMA.cast(INTEGER_WITH_SPACE_THOUSAND_SEPARATOR));
    }

    @Test
    public void testCastIntegerWithDecimalThousandSeparatorFailsWithNull() {
        assertNull(INTEGER_SCHEMA.cast(INTEGER_WITH_DECIMAL_THOUSAND_SEPARATOR));
    }

    @Test
    public void testCastIntegerWithCommaThousandSeparatorFailsWithNull() {
        assertNull(INTEGER_SCHEMA.cast(INTEGER_WITH_COMMA_THOUSAND_SEPARATOR));
    }

    @Test
    public void testCastIntegerWithSwedishLocale() {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(SWEDISH_LOCALE);
            assertEquals(new IntegerSchema().cast(INTEGER_STRING), NUMBER);
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    public void testCastLongWithSwedishLocale() {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(SWEDISH_LOCALE);
            assertEquals(new IntegerSchema().cast(LONG_STRING), LONG);
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    public void testCastNegativeIntegerWithSwedishLocale() {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(SWEDISH_LOCALE);
            assertEquals(new IntegerSchema().cast(NEGATIVE_INTEGER), NEGATIVE_NUMBER);
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

}