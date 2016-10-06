package io.swagger.jaxrs.ext;

import io.swagger.jaxrs.DefaultParameterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class SwaggerExtensions {
    private static Logger LOGGER = LoggerFactory.getLogger(SwaggerExtensions.class);

    private static List<SwaggerExtension> extensions = null;

    public static List<SwaggerExtension> getExtensions() {
        return extensions;
    }

    public static void setExtensions(List<SwaggerExtension> ext) {
        extensions = ext;
    }

    public static Iterator<SwaggerExtension> chain() {
        return extensions.iterator();
    }

    static {
        extensions = new ArrayList<SwaggerExtension>();
        ServiceLoader<SwaggerExtension> loader = ServiceLoader.load(SwaggerExtension.class);
        for (SwaggerExtension ext : loader) {
            LOGGER.debug("adding extension " + ext);
            extensions.add(ext);
        }
        extensions.add(new DefaultParameterExtension());
    }
}