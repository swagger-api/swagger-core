/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        try {
            setOutputStream(new PrintStream(outputStream));
            function.run();
        } finally {
            setOutputStream(out);
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
