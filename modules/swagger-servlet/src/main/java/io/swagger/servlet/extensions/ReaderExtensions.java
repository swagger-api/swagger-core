package io.swagger.servlet.extensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

public class ReaderExtensions {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderExtensions.class);
    private static final List<ReaderExtension> extensions;

    public static List<ReaderExtension> getExtensions() {
        return extensions;
    }

    static {
        final List<ReaderExtension> loadedExtensions = new ArrayList<ReaderExtension>();
        for (ReaderExtension re : ServiceLoader.load(ReaderExtension.class)) {
            LOGGER.debug("adding extension " + re);
            loadedExtensions.add(re);
        }
        Collections.sort(loadedExtensions, new Comparator<ReaderExtension>() {
            @Override
            public int compare(ReaderExtension o1, ReaderExtension o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
        extensions = Collections.unmodifiableList(loadedExtensions);
    }
}
