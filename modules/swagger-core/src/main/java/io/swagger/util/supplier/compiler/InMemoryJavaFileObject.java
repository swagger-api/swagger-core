package io.swagger.util.supplier.compiler;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java File Object represents an in-memory java source file
 * @author dedece35
 */
public class InMemoryJavaFileObject extends SimpleJavaFileObject {

    private static Logger LOGGER = LoggerFactory.getLogger(InMemoryJavaFileObject.class);

    private String className = null;

    private String sourceCode = null;

    /**
     * Constrcutor
     * @param className The class full name
     * @param sourceCode the full source code
     * @throws Exception
     */
    public InMemoryJavaFileObject(final String className, final String sourceCode) throws Exception {
        super(URI.create(
                "string:///" +
                        className.replace('.', '/') +
                        Kind.SOURCE.extension),
                Kind.SOURCE);
        this.sourceCode = sourceCode;
        this.className = className;
    }

    /**
     * Build a Java File Object
     * @param fullClassName Full class name
     * @param source Full source code
     * @return An InMemory JavaFileObject
     */
    public static InMemoryJavaFileObject buildJavaFileObject(final String fullClassName, final String source) {
        InMemoryJavaFileObject so = null;
        try {
            so = new InMemoryJavaFileObject(fullClassName, source);
        } catch (Exception exception) {
            LOGGER.error("error during creation JavaFileObject : {}", exception.getMessage());
        }
        return so;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors)
            throws IOException {
        return sourceCode;
    }

    /**
     * @return full class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return source code
     */
    public String getSourceCode() {
        return sourceCode;
    }
}
