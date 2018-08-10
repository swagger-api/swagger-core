package io.swagger.v3.jaxrs2.ext;

import io.swagger.v3.jaxrs2.DefaultParameterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class OpenAPIExtensions {
    private static Logger LOGGER = LoggerFactory.getLogger(OpenAPIExtensions.class);

    private static List<OpenAPIExtension> extensions = null;

    public static List<OpenAPIExtension> getExtensions() {
        return extensions;
    }

    public static void setExtensions(List<OpenAPIExtension> ext) {
        extensions = ext;
    }

    public static Iterator<OpenAPIExtension> chain() {
        return extensions.iterator();
    }

    static {
        extensions = new ArrayList<>();
        ServiceLoader<OpenAPIExtension> loader = ServiceLoader.load(OpenAPIExtension.class);
        for (OpenAPIExtension ext : loader) {
            LOGGER.debug("adding extension " + ext);
            extensions.add(ext);
        }
        extensions.add(new DefaultParameterExtension());
    }
}