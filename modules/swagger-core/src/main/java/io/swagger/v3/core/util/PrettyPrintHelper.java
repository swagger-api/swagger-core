package io.swagger.v3.core.util;

import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;

class PrettyPrintHelper {

    private static final ThreadLocal<Consumer<String>> OVERRIDE = new ThreadLocal<>();

    private PrettyPrintHelper() {
        // utility class
    }

    static void setOverride(Consumer<String> consumer) {
        OVERRIDE.set(consumer);
    }

    static void clearOverride() {
        OVERRIDE.remove();
    }

    static void emit(Logger logger, String message) {
        Consumer<String> consumer = OVERRIDE.get();
        if (consumer != null) {
            consumer.accept(message);
        } else {
            logger.debug(message);
        }
    }

    static void emitError(Logger logger, String message, Throwable throwable) {
        Consumer<String> consumer = OVERRIDE.get();
        if (consumer != null) {
            StringBuilder builder = new StringBuilder(message);
            if (throwable != null) {
                builder.append(System.lineSeparator());
                StringWriter writer = new StringWriter();
                throwable.printStackTrace(new PrintWriter(writer));
                builder.append(writer);
            }
            consumer.accept(builder.toString());
        }
        logger.error(message, throwable);
    }
}
