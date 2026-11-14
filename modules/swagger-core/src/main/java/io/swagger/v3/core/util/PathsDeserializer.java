package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class PathsDeserializer extends JsonDeserializer<Paths> {

    /**
     * @deprecated kept for subclasses compiled against it; the version is read through
     * {@link #specVersion()} so that subclasses can also express versions newer than 3.1.
     */
    @Deprecated
    protected boolean openapi31;

    @Override
    public Paths deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        final ObjectMapper mapper = mapper();

        Paths result = new Paths();
        JsonNode node = jp.getCodec().readTree(jp);
        ObjectNode objectNode = (ObjectNode)node;
        Map<String, Object> extensions = new LinkedHashMap<>();
        for (Iterator<String> it = objectNode.fieldNames(); it.hasNext(); ) {
            String childName = it.next();
            JsonNode child = objectNode.get(childName);
            // if name start with `x-` consider it an extesion
            if (childName.startsWith("x-")) {
                extensions.put(childName, mapper.convertValue(child, Object.class));
            } else {
                result.put(childName, mapper.convertValue(child, PathItem.class));
            }
        }
        if (!extensions.isEmpty()) {
            result.setExtensions(extensions);
        }
        return result;
    }

    /**
     * Returns the spec version this deserializer targets. "3.1 or later" semantics are
     * shared by 3.2; subclasses override this to support newer versions.
     */
    protected SpecVersion specVersion() {
        return openapi31 ? SpecVersion.V31 : SpecVersion.V30;
    }

    protected ObjectMapper mapper() {
        return SpecVersionMappers.mapper(specVersion());
    }
}
