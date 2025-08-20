package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class ByteObjectOAS31Test extends SwaggerTestBase {

    @Test(description = "Test ByteObject schema generation with OAS 3.1")
    public void testByteObjectSchemaOAS31() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(ByteObject.class));

        Map<String, io.swagger.v3.oas.models.media.Schema> definedModels = context.getDefinedModels();

        SerializationMatchers.assertEqualsToYaml31(definedModels, "ByteObject:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    primitiveWithoutSchema:\n" +
                "      type: integer\n" +
                "      writeOnly: true\n" +
                "    primitiveWithSchema:\n" +
                "      type: integer\n" +
                "      writeOnly: true\n" +
                "    primitiveWithSchemaTypeAndFormat:\n" +
                "      type: integer\n" +
                "      writeOnly: true\n" +
                "    primitiveArrayWithoutSchema:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: integer\n" +
                "      writeOnly: true\n" +
                "    primitiveArrayWithSchema:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: integer\n" +
                "      writeOnly: true"
        );
    }

    public static class ByteObject {

        private byte primitiveWithoutSchema;

        @Schema
        private byte primitiveWithSchema;

        @Schema(
                type = "string",
                format = "byte"
        )
        private byte primitiveWithSchemaTypeAndFormat;

        private byte[] primitiveArrayWithoutSchema;

        @ArraySchema(
                schema = @Schema(
                        type = "string",
                        format = "byte"
                )
        )
        private byte[] primitiveArrayWithSchema;

        public byte primitiveWithoutSchema() {
            return primitiveWithoutSchema;
        }

        public byte primitiveWithSchema() {
            return primitiveWithSchema;
        }

        public byte primitiveWithSchemaTypeAndFormat() {
            return primitiveWithSchemaTypeAndFormat;
        }

        public void setPrimitiveWithoutSchema(byte primitiveWithoutSchema) {
            this.primitiveWithoutSchema = primitiveWithoutSchema;
        }

        public void setPrimitiveWithSchema(byte primitiveWithSchema) {
            this.primitiveWithSchema = primitiveWithSchema;
        }

        public void setPrimitiveWithSchemaTypeAndFormat(byte primitiveWithSchemaTypeAndFormat) {
            this.primitiveWithSchemaTypeAndFormat = primitiveWithSchemaTypeAndFormat;
        }

        public byte[] primitiveArrayWithoutSchema() {
            return primitiveArrayWithoutSchema;
        }

        public void setPrimitiveArrayWithoutSchema(byte[] primitiveArrayWithoutSchema) {
            this.primitiveArrayWithoutSchema = primitiveArrayWithoutSchema;
        }

        public byte[] primitiveArrayWithSchema() {
            return primitiveArrayWithSchema;
        }

        public void setPrimitiveArrayWithSchema(byte[] primitiveArrayWithSchema) {
            this.primitiveArrayWithSchema = primitiveArrayWithSchema;
        }
    }
}
