package io.swagger.servlet.extensions;

import com.google.common.collect.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public class ReaderExtensions {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderExtensions.class);
    private static final List<ReaderExtension> extensions;

    public static List<ReaderExtension> getExtensions() {
        return extensions;
    }

    static {
        final Ordering<ReaderExtension> ordering = new Ordering<ReaderExtension>() {
            @Override
            public int compare(ReaderExtension left, ReaderExtension right) {
                return Integer.compare(left.getPriority(), right.getPriority());
            }
        };
        final List<ReaderExtension> loadedExtensions = new ArrayList<ReaderExtension>();
        for (ReaderExtension readerExtension : ordering.sortedCopy(ServiceLoader.load(ReaderExtension.class))) {
            LOGGER.debug("adding extension " + readerExtension);
            loadedExtensions.add(readerExtension);
        }
        extensions = Collections.unmodifiableList(loadedExtensions);
    }
}
