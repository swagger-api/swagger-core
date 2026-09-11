package io.swagger.v3.core.util;

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

import tools.jackson.core.io.ContentReference;

import java.io.DataInput;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

import static org.testng.Assert.assertNotNull;

public class ObjectMapperFactoryTest {

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
