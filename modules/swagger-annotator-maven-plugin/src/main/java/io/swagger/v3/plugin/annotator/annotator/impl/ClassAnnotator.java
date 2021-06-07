package io.swagger.v3.plugin.annotator.annotator.impl;

import io.swagger.v3.plugin.annotator.annotator.AbstractAnnotator;
import io.swagger.v3.plugin.annotator.annotator.Annotator;
import io.swagger.v3.plugin.annotator.model.JavadocMapping;
import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.codehaus.plexus.logging.Logger;
import org.jboss.forge.roaster.model.JavaDocTag;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;

public class ClassAnnotator extends AbstractAnnotator {

    public ClassAnnotator(Logger log) {
        super(log);
    }

    @Override
    protected Map<String, Set<JavadocMapping>> needToTag(Set<JavadocMapping> needToTag, JavaClassSource source) {
        Map<String, Set<JavadocMapping>> result = new HashMap<>(2);
        result.put(source.getQualifiedName(), AbstractAnnotator.buildNeedToTag(source, needToTag));
        return result;
    }

    @Override
    protected Map<String, Map<String, Object>> initComment(JavaClassSource source) {
        Map<String, Map<String, Object>> comment = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        JavaDocSource<JavaClassSource> classDoc = source.getJavaDoc();
        map.put(Annotator.DESCRIPTION_KEY, classDoc.getText());
        List<JavaDocTag> classTags = classDoc.getTags();
        for (JavaDocTag tag : classTags) {
            map.put(tag.getName(), tag.getValue());
        }
        comment.put(source.getQualifiedName(), map);
        return comment;
    }

    @Override
    protected DynamicType.Builder<?> tagAnnotations(DynamicType.Builder<?> builder, Collection<AnnotationDescription> descriptions, String name, int index) {
        return builder.annotateType(descriptions);
    }

    @Override
    public ElementType annotateType() {
        return ElementType.TYPE;
    }
}
