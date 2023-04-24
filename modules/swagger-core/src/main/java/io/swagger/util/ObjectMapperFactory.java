package io.swagger.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.jackson.ModelSerializer;
import io.swagger.jackson.PropertySerializer;
import io.swagger.jackson.mixin.OperationResponseMixin;
import io.swagger.jackson.mixin.ResponseSchemaMixin;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.properties.Property;

public class ObjectMapperFactory {

    public static ObjectMapper createJson(JsonFactory jsonFactory) {
        return create(jsonFactory, true, true);
    }
    protected static ObjectMapper createJson() {
        return createJson(true, true);
    }

    protected static ObjectMapper createJson(boolean includePathDeserializer, boolean includeResponseDeserializer) {
        return create(null, includePathDeserializer, includeResponseDeserializer);
    }

    public static ObjectMapper createYaml(YAMLFactory yamlFactory) {
        return create(yamlFactory, true, true);
    }

    protected static ObjectMapper createYaml() {
        return createYaml(true, true);
    }

    protected static ObjectMapper createYaml(boolean includePathDeserializer, boolean includeResponseDeserializer) {
        return create(new YAMLFactory(), includePathDeserializer, includeResponseDeserializer);
    }

    private static ObjectMapper create(JsonFactory jsonFactory, boolean includePathDeserializer, boolean includeResponseDeserializer) {
        ObjectMapper mapper = jsonFactory == null ? new ObjectMapper() : new ObjectMapper(jsonFactory);

        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer<?> modifySerializer(
                            SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
                        if (Property.class.isAssignableFrom(desc.getBeanClass())) {
                            return new PropertySerializer((JsonSerializer<Object>) serializer);
                        } else if (Model.class.isAssignableFrom(desc.getBeanClass())) {
                            return new ModelSerializer((JsonSerializer<Object>) serializer);
                        }
                        return serializer;
                    }
                });
            }
        });

        Module deserializerModule = new DeserializationModule(includePathDeserializer, includeResponseDeserializer);
        mapper.registerModule(deserializerModule);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.addMixIn(Response.class, ResponseSchemaMixin.class);
        mapper.addMixIn(Operation.class, OperationResponseMixin.class);

        ReferenceSerializationConfigurer.serializeAsComputedRef(mapper);

        return mapper;
    }
}
