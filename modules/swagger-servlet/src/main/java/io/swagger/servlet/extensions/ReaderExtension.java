package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.servlet.ReaderContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * The <code>ReaderExtension</code> interface defines methods for scans resources for Swagger.
 */
public interface ReaderExtension {

    /**
     * Returns this extension's priority.
     * Note: Extension will be executed first with lowest priority.
     *
     * @return this extension's priority
     */
    int getPriority();

    /**
     * Checks that a resource should be scanned.
     *
     * @param context is the resource context
     * @return true if the resource needs to be scanned, otherwise false
     */
    boolean isReadable(ReaderContext context);

    /**
     * Reads the consumes from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyConsumes(ReaderContext context, Operation operation, Method method);

    /**
     * Reads the produces from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyProduces(ReaderContext context, Operation operation, Method method);

    /**
     * Returns http method.
     *
     * @param context is the resource context
     * @param method  is the method for reading annotations
     * @return http method
     */
    String getHttpMethod(ReaderContext context, Method method);

    /**
     * Returns operation's path.
     *
     * @param context is the resource context
     * @param method  is the method for reading annotations
     * @return operation's path
     */
    String getPath(ReaderContext context, Method method);

    /**
     * Reads the operation id from the method's annotations and applies it to the operation.
     *
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyOperationId(Operation operation, Method method);

    /**
     * Reads the summary from the method's annotations and applies it to the operation.
     *
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applySummary(Operation operation, Method method);

    /**
     * Reads the description from the method's annotations and applies it to the operation.
     *
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyDescription(Operation operation, Method method);

    /**
     * Reads the schemes from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applySchemes(ReaderContext context, Operation operation, Method method);

    /**
     * Sets the deprecated flag to the operation.
     *
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void setDeprecated(Operation operation, Method method);

    /**
     * Reads the security requirement from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applySecurityRequirements(ReaderContext context, Operation operation, Method method);

    /**
     * Reads the tags from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyTags(ReaderContext context, Operation operation, Method method);

    /**
     * Reads the responses from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyResponses(ReaderContext context, Operation operation, Method method);

    /**
     * Reads the parameters from the method's annotations and applies these to the operation.
     *
     * @param context     is the resource context
     * @param operation   is the container for the operation data
     * @param type        is the type of parameter
     * @param annotations are the method's annotations
     */
    void applyParameters(ReaderContext context, Operation operation, Type type, Annotation[] annotations);

    /**
     * Reads the implicit parameters from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyImplicitParameters(ReaderContext context, Operation operation, Method method);

    /**
     * Reads the extensions from the method's annotations and applies these to the operation.
     *
     * @param context   is the resource context
     * @param operation is the container for the operation data
     * @param method    is the method for reading annotations
     */
    void applyExtensions(ReaderContext context, Operation operation, Method method);
}
