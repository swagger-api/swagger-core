package com.wordnik.swagger.jaxrs.apt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * Annotation Processor Factory that returns a processor for types we care,
 * which are java.ws.rs.* and com.wordnik.swagger.annotations.*
 * @author Heiko W. Rupp
 */
public class SwaggerAptFactory implements AnnotationProcessorFactory {

    @Override
    public Collection<String> supportedOptions() {
        return Collections.emptySet(); // No options for the moment
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        Set<String> types = new HashSet<String>();
        types.add("com.wordnik.swagger.annotations.*");
        types.add("javax.ws.rs.*");
        return types;
    }

    @Override
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> annotationTypeDeclarations,
                                               AnnotationProcessorEnvironment env) {

        Map<String,String> options = env.getOptions();
        String targetDir = null;
        if (options.containsKey("-d"))
            targetDir = options.get("-d");

        AnnotationProcessor ap;
        if (annotationTypeDeclarations.isEmpty())
            ap = AnnotationProcessors.NO_OP;
        else
            ap = new ClassLevelProcessor(env, targetDir);
        return ap;
    }
}
