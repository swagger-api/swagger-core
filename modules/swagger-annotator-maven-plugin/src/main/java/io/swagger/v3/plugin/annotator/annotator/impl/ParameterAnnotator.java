package io.swagger.v3.plugin.annotator.annotator.impl;

import io.swagger.v3.plugin.annotator.annotator.AbstractAnnotator;
import io.swagger.v3.plugin.annotator.annotator.Annotator;
import io.swagger.v3.plugin.annotator.model.JavadocMapping;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.maven.plugin.logging.Log;
import org.jboss.forge.roaster.model.JavaDocTag;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParameterAnnotator implements Annotator {

    private static Log log;

    public ParameterAnnotator(Log log) {
        ParameterAnnotator.log = log;
    }

    @Override
    public DynamicType.Builder<?> annotate(DynamicType.Builder<?> builder, Set<JavadocMapping> needToTag, JavaClassSource source) {
        if (CollectionUtils.isEmpty(needToTag)) {
            return builder;
        }
        Map<String, Map<Integer, Set<JavadocMapping>>> tagMap = needToTag(needToTag, source);
        Map<String, Map<String, Object>> comment = initComment(source);
        for (Map.Entry<String, Map<String, Object>> commentEntry : comment.entrySet()) {
            String methodName = commentEntry.getKey();
            Map<String, Object> commentMap = commentEntry.getValue();
            Map<Integer, Set<JavadocMapping>> map = tagMap.get(methodName);
            for (Map.Entry<Integer, Set<JavadocMapping>> entry : map.entrySet()) {
                Integer index = entry.getKey();
                Set<JavadocMapping> realNeedToTag = entry.getValue();
                if (CollectionUtils.isEmpty(realNeedToTag)) {
                    continue;
                }
                Collection<AnnotationDescription> descriptions = AbstractAnnotator.buildAnnotationDescriptions(realNeedToTag, commentMap);
                builder = builder.visit(new MemberAttributeExtension.ForMethod()
                        .annotateParameter(index, descriptions)
                        .on(ElementMatchers.named(methodName)));
            }
        }
        return builder;
    }

    private Map<String, Map<Integer, Set<JavadocMapping>>> needToTag(Set<JavadocMapping> needToTag, JavaClassSource source) {
        Map<String, Map<Integer, Set<JavadocMapping>>> comment = new HashMap<>();
        List<MethodSource<JavaClassSource>> methods = source.getMethods();
        for (MethodSource<JavaClassSource> method : methods) {
            int index = 0;
            Map<Integer, Set<JavadocMapping>> map = new LinkedHashMap<>();
            for (ParameterSource<JavaClassSource> parameter : method.getParameters()) {
                map.put(index++, AbstractAnnotator.buildNeedToTag(parameter, needToTag));
            }
            comment.put(method.getName(), map);
        }
        return comment;
    }

    private Map<String, Map<String, Object>> initComment(JavaClassSource source) {
        Map<String, Map<String, Object>> comment = new LinkedHashMap<>();
        List<MethodSource<JavaClassSource>> methods = source.getMethods();
        for (MethodSource<JavaClassSource> method : methods) {
            Map<String, Object> map = new HashMap<>();
            JavaDocSource<?> methodDoc = method.getJavaDoc();
            List<JavaDocTag> methodTags = methodDoc.getTags();
            for (JavaDocTag tag : methodTags) {
                String tagName = tag.getName();
                if (Annotator.PARAM_TAG.equals(tagName)) {
                    String tagValue = tag.getValue();
                    String[] strings = tagValue.split(" ");
                    tagName = strings[0];
                    tagValue = strings[2];
                    map.put(Annotator.PARAM_NAME_KEY, tagName);
                    map.put(Annotator.DESCRIPTION_KEY, tagValue);
                }
            }
            comment.put(method.getName(), map);
        }
        return comment;
    }

    @Override
    public ElementType annotateType() {
        return ElementType.PARAMETER;
    }
}
