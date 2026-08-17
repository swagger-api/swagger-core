package io.swagger.v3.core.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * The <code>OutputReplacer</code> enumerator helps to run a code with a custom output stream.
 */
public enum OutputReplacer {

    /**
     * The "standard" output stream.
     */
    OUT {
        @Override
        PrintStream getOutputStream() {
            return System.out;
        }

        @Override
        void setOutputStream(PrintStream outputStream) {
            System.setOut(outputStream);
        }
    },
    /**
     * The "standard" error output stream.
     */
    ERROR {
        @Override
        PrintStream getOutputStream() {
            return System.err;
        }

        @Override
        void setOutputStream(PrintStream outputStream) {
            System.setErr(outputStream);
        }

    };

    /**
     * Runs a code with a custom output stream.
     *
     * @param function is code to run
     * @return is output stream as string
     */
    public String run(Function function) {
        final PrintStream out = getOutputStream();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream capture = new PrintStream(outputStream);
        try {
            setOutputStream(capture);
            PrettyPrintHelper.setOverride(message -> {
                capture.println(message);
                capture.flush();
            });
            function.run();
        } finally {
            PrettyPrintHelper.clearOverride();
            setOutputStream(out);
            capture.close();
        }

        try {
            return outputStream.toString(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    abstract PrintStream getOutputStream();

    abstract void setOutputStream(PrintStream printStream);

    public interface Function {
        void run();
    }
}
