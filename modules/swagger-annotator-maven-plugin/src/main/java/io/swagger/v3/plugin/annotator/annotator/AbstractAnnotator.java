package io.swagger.v3.plugin.annotator.annotator;

import io.swagger.v3.plugin.annotator.model.JavadocMapping;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.apache.maven.plugin.logging.Log;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractAnnotator implements Annotator {

    protected static Log log;

    public AbstractAnnotator(Log log) {
        AbstractAnnotator.log = log;
    }

    @Override
    public final DynamicType.Builder<?> annotate(DynamicType.Builder<?> builder,
                                                 Set<JavadocMapping> needToTag,
                                                 JavaClassSource source) {
        if (null == builder) {
            throw new NullPointerException("builder is null !");
        }
        if (CollectionUtils.isEmpty(needToTag)) {
            return builder;
        }
        Map<String, Set<JavadocMapping>> tagMap = needToTag(needToTag, source);
        Map<String, Map<String, Object>> comment = initComment(source);
        for (Map.Entry<String, Map<String, Object>> entry : comment.entrySet()) {
            String key = entry.getKey();
            Map<String, Object> value = entry.getValue();
            Set<JavadocMapping> realNeedToTag = tagMap.get(key);
            if (CollectionUtils.isEmpty(realNeedToTag)) {
                continue;
            }
            Collection<AnnotationDescription> descriptions = buildAnnotationDescriptions(realNeedToTag, value);
            builder = tagAnnotations(builder, descriptions, key, 0);
        }
        return builder;
    }

    public static Set<JavadocMapping> buildNeedToTag(AnnotationTargetSource<JavaClassSource, ?> source, Set<JavadocMapping> needToTag) {
        try {
            Set<Class<? extends Annotation>> declared = new LinkedHashSet<>();
            Annotation[] declaredAnnotations = Class.forName(source.getOrigin().getQualifiedName()).getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                declared.add(declaredAnnotation.getClass());
            }
            return needToTag.stream()
                    //only need to tag if the conditions are met
                    .filter(mapping -> {
                        Set<Class<? extends Annotation>> conditions = mapping.getConditions()
                                .stream().map(condition -> {
                                    try {
                                        return (Class<? extends Annotation>) Class.forName(condition);
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }).filter(Objects::nonNull)
                                .collect(Collectors.toSet());
                        if (CollectionUtils.isEmpty(conditions)) {
                            return true;
                        }
                        for (Class<? extends Annotation> annotation : declared) {
                            for (Class<? extends Annotation> condition : conditions) {
                                if (annotation.equals(condition) ||
                                        AnnotationUtils.isAnnotationMetaPresent(annotation, condition)) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    })
                    //You don’t need to tag annotation that has already been tagged
                    .filter(mapping -> {
                        try {
                            Class<? extends Annotation> tagType = (Class<? extends Annotation>) Class.forName(mapping.getAnnotationClassName());
                            for (Class<? extends Annotation> annotation : declared) {
                                if (annotation.equals(tagType) ||
                                        AnnotationUtils.isAnnotationMetaPresent(annotation, tagType)) {
                                    return false;
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        return true;
                    })
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("buildNeedToTag failed !", e);
            return new LinkedHashSet<>();
        }
    }

    protected abstract Map<String, Set<JavadocMapping>> needToTag(Set<JavadocMapping> needToTag, JavaClassSource source);

    protected abstract Map<String, Map<String, Object>> initComment(JavaClassSource source);

    public static Collection<AnnotationDescription> buildAnnotationDescriptions(Set<JavadocMapping> needToTag, Map<String, Object> comment) {
        List<AnnotationDescription> result = new ArrayList<>(needToTag.size());
        for (JavadocMapping mapping : needToTag) {
            String annotationClassName = mapping.getAnnotationClassName();
            try {
                Class<? extends Annotation> annotationType = (Class<? extends Annotation>) Class.forName(annotationClassName);
                AnnotationDescription.Builder annotationBuilder = AnnotationDescription.Builder
                        .ofType(annotationType);
                for (Method memberMethod : annotationType.getDeclaredMethods()) {
                    String methodName = memberMethod.getName();
                    try {
                        //Give priority to the default value specified by the user
                        Object value = mapping.getDefaultValue(methodName);
                        if (null == value) {
                            //comment key
                            String tagKey = mapping.getCommentKey(methodName);
                            if (null != tagKey) {
                                value = comment.get(tagKey);
                            }
                        }
                        if (null == value) {
                            //No mapping, take the default value defined by the annotation
                            value = memberMethod.getDefaultValue();
                        }
                        annotationBuilder = defineMemberValue(annotationBuilder, memberMethod, methodName, value);
                    } catch (Exception e) {
                        log.error("define member:" + memberMethod + " failed, " + e.getMessage());
                    }
                }
                result.add(annotationBuilder.build());
            } catch (Exception e) {
                log.error("define annotation:" + annotationClassName + " failed, " + e.getMessage());
            }
        }
        return result;
    }

    public static AnnotationDescription.Builder defineMemberValue(AnnotationDescription.Builder annotationBuilder, Method memberMethod, String methodName, Object value) {
        Class<?> returnType = memberMethod.getReturnType();
        if (returnType.isArray()) {
            Class<?> componentType = returnType.getComponentType();
            if (Annotation.class.isAssignableFrom(componentType)) {
                Annotation[] array = value instanceof Annotation[] ? (Annotation[]) value : new Annotation[]{(Annotation) value};
                annotationBuilder = annotationBuilder.defineAnnotationArray(methodName, (Class<Annotation>) componentType, array);
            } else if (Class.class.isAssignableFrom(componentType)) {
                Class<?>[] array = value instanceof Class<?>[] ? (Class<?>[]) value : new Class<?>[]{(Class<?>) value};
                annotationBuilder = annotationBuilder.defineTypeArray(methodName, array);
            } else if (Enum.class.isAssignableFrom(componentType)) {
                Set<String> enums = new HashSet<>();
                for (Enum<?> anEnum : ((Enum<?>[]) value)) {
                    enums.add(anEnum.name());
                }
                annotationBuilder = annotationBuilder.defineEnumerationArray(methodName,
                        TypeDescription.ForLoadedType.of(componentType), enums.toArray(new String[0]));
            } else if (Boolean.class.isAssignableFrom(componentType)) {
                boolean[] array = value instanceof boolean[] ? (boolean[]) value : new boolean[]{(boolean) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (Byte.class.isAssignableFrom(componentType)) {
                byte[] array = value instanceof byte[] ? (byte[]) value : new byte[]{(byte) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (Short.class.isAssignableFrom(componentType)) {
                short[] array = value instanceof short[] ? (short[]) value : new short[]{(short) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (Character.class.isAssignableFrom(componentType)) {
                char[] array = value instanceof char[] ? (char[]) value : new char[]{(char) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (Integer.class.isAssignableFrom(componentType)) {
                int[] array = value instanceof int[] ? (int[]) value : new int[]{(int) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (Long.class.isAssignableFrom(componentType)) {
                long[] array = value instanceof long[] ? (long[]) value : new long[]{(long) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (Float.class.isAssignableFrom(componentType)) {
                float[] array = value instanceof float[] ? (float[]) value : new float[]{(float) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (Double.class.isAssignableFrom(componentType)) {
                double[] array = value instanceof double[] ? (double[]) value : new double[]{(double) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            } else if (String.class.isAssignableFrom(componentType)) {
                String[] array = value instanceof String[] ? (String[]) value : new String[]{(String) value};
                annotationBuilder = annotationBuilder.defineArray(methodName, array);
            }
        } else if (Class.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, (Class<?>) value);
        } else if (Annotation.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, (Annotation) value);
        } else if (Enum.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, (Enum<?>) value);
        } else if (boolean.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, Boolean.parseBoolean(value.toString()));
        } else if (byte.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, Byte.parseByte(value.toString()));
        } else if (short.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, Short.parseShort(value.toString()));
        } else if (char.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, value.toString().charAt(0));
        } else if (int.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, Integer.parseInt(value.toString()));
        } else if (long.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, Long.parseLong(value.toString()));
        } else if (float.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, Float.parseFloat(value.toString()));
        } else if (double.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, Double.parseDouble(value.toString()));
        } else if (String.class.isAssignableFrom(returnType)) {
            annotationBuilder = annotationBuilder.define(methodName, value.toString());
        }
        return annotationBuilder;
    }

    protected abstract DynamicType.Builder<?> tagAnnotations(DynamicType.Builder<?> builder, Collection<AnnotationDescription> descriptions, String name, int index);

}
