package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket4059Test extends SwaggerTestBase {

    @Test
    public void testJsonIgnoreAnnotation() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(ExtendedRequest.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "ExtendedRequest:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "    id2:\n" +
                "      type: string");

    }

    static class ExtendedRequest extends Request{
        @JsonIgnore(value = false) // Don't ignore me
        private Id id;
        @JsonIgnore(value = true)
        private String fieldTwo;
        @JsonIgnore
        private String fieldThree;

        public String getFieldTwo() {
            return fieldTwo;
        }

        public void setFieldTwo(String fieldTwo) {
            this.fieldTwo = fieldTwo;
        }

        public String getFieldThree() {
            return fieldThree;
        }

        public void setFieldThree(String fieldThree) {
            this.fieldThree = fieldThree;
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }
    }

    static class Request {
        @JsonIgnore
        private Id id;

        @JsonIgnore(value = false) // Don't ignore me
        private Id id2;

        public Id getId2() {
            return id2;
        }

        public void setId2(Id id2) {
            this.id2 = id2;
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }
    }

    static class Id {
        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
