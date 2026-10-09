package io.swagger.v3.core.util;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.jackson.core.ErrorReportConfiguration;
import tools.jackson.core.FormatSchema;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.StreamWriteConstraints;
import tools.jackson.core.TSFBuilder;
import tools.jackson.core.TokenStreamFactory;
import tools.jackson.core.Version;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.cfg.JsonNodeFeature;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.io.ContentReference;
import java.io.DataInput;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import static org.testng.Assert.*;

public class ObjectMapperFactoryTest extends ObjectMapperFactoryTestBase {

    @Test
    public void switchIsOffByDefault() {
        assertFalse(ObjectMapperFactory.isJackson2Compatibility());
    }

    @Test
    public void settingTheSameValueDoesNotChangeGeneration() {
        long generation = ObjectMapperFactory.generation();
        ObjectMapperFactory.setJackson2Compatibility(false);
        assertEquals(ObjectMapperFactory.generation(), generation);
        assertSame(Json.mapper(), Json.mapper());

        ObjectMapperFactory.setJackson2Compatibility(true);
        long enabled = ObjectMapperFactory.generation();
        assertTrue(enabled > generation);
        ObjectMapperFactory.setJackson2Compatibility(true);
        assertEquals(ObjectMapperFactory.generation(), enabled);
    }

    @Test
    public void clearCustomizersDoesNotResetTheSwitch() {
        ObjectMapperFactory.setJackson2Compatibility(true);
        ObjectMapperFactory.clearCustomizers();
        assertTrue(ObjectMapperFactory.isJackson2Compatibility());
    }

    @Test
    public void togglingSwitchRebuildsCachedMappers() {
        Supplier<ObjectMapper>[] cached = cachedMappers();
        ObjectMapper[] before = new ObjectMapper[cached.length];
        for (int i = 0; i < cached.length; i++) {
            before[i] = cached[i].get();
            assertFalse(before[i].isEnabled(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS), "mapper " + i);
        }

        ObjectMapperFactory.setJackson2Compatibility(true);

        for (int i = 0; i < cached.length; i++) {
            ObjectMapper after = cached[i].get();
            assertNotSame(after, before[i], "mapper " + i + " must be rebuilt");
            assertTrue(after.isEnabled(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS), "mapper " + i);
        }

        ObjectMapperFactory.setJackson2Compatibility(false);
        for (int i = 0; i < cached.length; i++) {
            assertFalse(cached[i].get().isEnabled(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS), "mapper " + i);
        }
    }

    @SuppressWarnings("unchecked")
    private static Supplier<ObjectMapper>[] cachedMappers() {
        return new Supplier[] {
                (Supplier<ObjectMapper>) Json::mapper, (Supplier<ObjectMapper>) Yaml::mapper,
                (Supplier<ObjectMapper>) Json31::mapper, (Supplier<ObjectMapper>) Yaml31::mapper,
                (Supplier<ObjectMapper>) Json31::converterMapper};
    }

    @Test
    public void togglingSwitchKeepsExplicitlyInstalledMapper() {
        ObjectMapper installed = JsonMapper.builder().build();
        Json.mapper(installed);

        ObjectMapperFactory.setJackson2Compatibility(true);

        assertSame(Json.mapper(), installed);
    }

    @Test
    public void togglingSwitchRebuildsDefaultModelResolver() {
        ModelConverters converters = new ModelConverters();
        ModelConverter before = converters.getConverters().get(0);
        converters.read(ResolverAccessorBean.class);
        assertSame(converters.getConverters().get(0), before, "no change, no rebuild");

        ObjectMapperFactory.setJackson2Compatibility(true);
        converters.read(ResolverAccessorBean.class);

        assertNotSame(converters.getConverters().get(0), before);
    }

    /** ModelResolver derives property names from the accessor name itself, in both modes. */
    @Test(dataProvider = "modes")
    public void modelResolverKeepsAccessorPrefixForLowerCaseAndNonLetterNames(boolean compat) {
        ObjectMapperFactory.setJackson2Compatibility(compat);
        var props = new ModelConverters().read(ResolverAccessorBean.class).get("ResolverAccessorBean")
                .getProperties();
        assertTrue(props.containsKey("getvalue"), compat + ": " + props.keySet());
        assertTrue(props.containsKey("get_value"), compat + ": " + props.keySet());
    }

    @DataProvider(name = "modes")
    public Object[][] modes() {
        return new Object[][] {{false}, {true}};
    }

    @Test
    public void customizersRunLastAndOverrideSwaggerOptionsAndCompatibility() {
        ObjectMapperFactory.setJackson2Compatibility(true);
        ObjectMapperFactory.addCustomizer((builder, target) -> builder
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS));
        for (ObjectMapper mapper : List.of(ObjectMapperFactory.createJson(), ObjectMapperFactory.createYaml31(),
                ObjectMapperFactory.createJsonConverter())) {
            assertTrue(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
            assertFalse(mapper.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS));
        }
    }

    @Test(dataProvider = "modes")
    public void strictMapperDoesNotApplyCustomizers(boolean compat) {
        ObjectMapperFactory.setJackson2Compatibility(compat);
        ObjectMapperFactory.addCustomizer((builder, target) -> builder
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        ObjectMapper strict = ObjectMapperFactory.buildStrictGenericObjectMapper();
        assertFalse(strict.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        assertEquals(strict.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS), compat);
    }

    @Test
    public void preservesSuppliedJsonFactoryAndReadConstraints() {
        StreamReadConstraints constraints = StreamReadConstraints.builder().maxNestingDepth(17).build();
        JsonFactory factory = JsonFactory.builder().streamReadConstraints(constraints).build();
        for (ObjectMapper mapper : List.of(ObjectMapperFactory.createJson(factory),
                ObjectMapperFactory.createJson31(factory))) {
            assertSame(mapper.tokenStreamFactory(), factory);
            assertSame(mapper.tokenStreamFactory().streamReadConstraints(), constraints);
            assertEquals(mapper.tokenStreamFactory().streamReadConstraints().getMaxNestingDepth(), 17);
        }
    }

    @Test
    public void preservesSuppliedYamlFactoryAndReadConstraints() {
        StreamReadConstraints constraints = StreamReadConstraints.builder().maxNestingDepth(19).build();
        YAMLFactory factory = YAMLFactory.builder().streamReadConstraints(constraints).build();
        for (ObjectMapper mapper : List.of(ObjectMapperFactory.createYaml(factory),
                ObjectMapperFactory.createYaml31(factory))) {
            assertSame(mapper.tokenStreamFactory(), factory);
            assertSame(mapper.tokenStreamFactory().streamReadConstraints(), constraints);
            assertEquals(mapper.tokenStreamFactory().streamReadConstraints().getMaxNestingDepth(), 19);
        }
    }

    @Test
    public void createWithNullFactoryReturnsJsonMapper() {
        assertNotNull(ObjectMapperFactory.create(null, false));
        assertNotNull(ObjectMapperFactory.create(null, true));
    }

    @Test
    public void createWithJsonFactorySucceeds() {
        assertNotNull(ObjectMapperFactory.create(new JsonFactory(), false));
        assertNotNull(ObjectMapperFactory.create(new JsonFactory(), true));
    }

    @Test
    public void createWithYamlFactorySucceeds() {
        assertNotNull(ObjectMapperFactory.create(new YAMLFactory(), false));
        assertNotNull(ObjectMapperFactory.create(new YAMLFactory(), true));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
          expectedExceptionsMessageRegExp = ".*Unsupported TokenStreamFactory.*")
    public void createWithUnsupportedFactoryThrows() {
        ObjectMapperFactory.create(new UnsupportedFactory(), false);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
          expectedExceptionsMessageRegExp = ".*Unsupported TokenStreamFactory.*")
    public void createJson31WithUnsupportedFactoryThrows() {
        ObjectMapperFactory.createJson31(new UnsupportedFactory());
    }

    static class UnsupportedFactory extends TokenStreamFactory {

        UnsupportedFactory() {
            super(StreamReadConstraints.defaults(), StreamWriteConstraints.defaults(),
                    ErrorReportConfiguration.defaults(), 0, 0);
        }

        @Override public TokenStreamFactory copy() { throw new UnsupportedOperationException(); }
        @Override public TokenStreamFactory snapshot() { return this; }
        @Override public TSFBuilder<?, ?> rebuild() { throw new UnsupportedOperationException(); }
        @Override public boolean canHandleBinaryNatively() { return false; }
        @Override public boolean canParseAsync() { return false; }
        @Override public boolean canUseSchema(FormatSchema s) { return false; }
        @Override public String getFormatName() { return "unsupported-test-format"; }
        @Override public Version version() { return Version.unknownVersion(); }

        @Override public JsonParser createParser(ObjectReadContext ctx, File f) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, Path p) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, InputStream in) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, Reader r) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, byte[] b, int off, int len) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, String s) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, char[] c, int off, int len) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, DataInput in) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, OutputStream out, JsonEncoding enc) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, Writer w) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, File f, JsonEncoding enc) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, Path p, JsonEncoding enc) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override protected ContentReference _createContentReference(Object src) { return ContentReference.unknown(); }
        @Override protected ContentReference _createContentReference(Object src, int off, int len) { return ContentReference.unknown(); }
    }
}
