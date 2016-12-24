package io.swagger.jaxrs.listing;

import io.swagger.models.Swagger;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "application/yaml"})
public class SwaggerSerializers implements MessageBodyWriter<Swagger> {
    static boolean prettyPrint = false;
    Logger LOGGER = LoggerFactory.getLogger(SwaggerSerializers.class);

    public static void setPrettyPrint(boolean shouldPrettyPrint) {
        SwaggerSerializers.prettyPrint = shouldPrettyPrint;
    }

    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return Swagger.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Swagger data, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Swagger data,
            Class<?> type,
            Type genericType,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, Object> headers,
            OutputStream out) throws IOException {
        if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
            if (prettyPrint) {
                out.write(Json.pretty().writeValueAsBytes(data));
            } else {
                out.write(Json.mapper().writeValueAsBytes(data));
            }
        } else if (mediaType.toString().startsWith("application/yaml")) {
            headers.remove("Content-Type");
            headers.add("Content-Type", "application/yaml");
            if (prettyPrint) {
                out.write(Yaml.pretty().writeValueAsBytes(data));
            } else {
                out.write(Yaml.mapper().writeValueAsBytes(data));
            }
        } else if (mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
            headers.remove("Content-Type");
            headers.add("Content-Type", "application/json");
            if (prettyPrint) {
                out.write(Json.pretty().writeValueAsBytes(data));
            } else {
                out.write(Json.mapper().writeValueAsBytes(data));
            }
        }
    }
}

