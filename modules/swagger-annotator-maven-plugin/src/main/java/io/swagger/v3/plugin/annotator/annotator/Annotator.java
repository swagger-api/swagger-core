package io.swagger.v3.plugin.annotator.annotator;

import io.swagger.v3.plugin.annotator.model.JavadocMapping;
import java.lang.annotation.ElementType;
import java.util.Set;
import net.bytebuddy.dynamic.DynamicType;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public interface Annotator {

    String DESCRIPTION_KEY = "description";

    String PARAM_TAG = "@param";

    String PARAM_NAME_KEY = "paramName";

    /**
     * annotate
     *
     * @param builder   use it to tag annotations
     * @param needToTag which annotations should be tagged
     * @param source    annotation source
     * @return byte-buddy builder
     */
    DynamicType.Builder<?> annotate(DynamicType.Builder<?> builder,
        Set<JavadocMapping> needToTag,
        JavaClassSource source);

    /**
     * What type of annotation is typed, for example, only type
     */
    ElementType annotateType();
}
