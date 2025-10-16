package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public final class AnnotationsIntrospector {

    public static AnnotatedType toAnnotatedType(AnnotatedMember member) {
        if (member instanceof AnnotatedField) {
            AnnotatedField af = (AnnotatedField) member;
            // Underlying java.lang.reflect.Field
            return af.getAnnotated().getAnnotatedType();
        }

        if (member instanceof AnnotatedMethod) {
            AnnotatedMethod am = (AnnotatedMethod) member;
            final Method m = am.getAnnotated();
            final int pc = am.getParameterCount();
            if (pc == 0) {
                // Likely a getter
                return m.getAnnotatedReturnType();
            } else if (pc == 1) {
                // Likely a setter
                return m.getParameters()[0].getAnnotatedType();
            } else {
                // Unusual shape (e.g., @JsonCreator method with multiple params)
                // Choose the user’s param via other context if you have it; here we fail fast.
                throw new IllegalArgumentException("Method has " + pc + " parameters; cannot choose which parameter's AnnotatedType to return.");
            }
        }

        if (member instanceof AnnotatedParameter) {
            AnnotatedParameter ap = (AnnotatedParameter) member;
            // For parameters, do not use ap.getAnnotated() (often null); go via the owner Executable.
            final AnnotatedWithParams owner = ap.getOwner();
            final Executable exec = (Executable) owner.getAnnotated();
            final int index = ap.getIndex();
            final Parameter[] params = exec.getParameters();
            if (index < 0 || index >= params.length) {
                throw new IllegalArgumentException("Parameter index out of bounds: " + index + " for " + exec);
            }
            return params[index].getAnnotatedType();
        }

        // (Very rare) Other AnnotatedMember subtypes or custom implementations
        throw new IllegalArgumentException("Unsupported AnnotatedMember subtype: " + member.getClass().getName());
    }

    public static void getAnnotations(AnnotatedMember t, List<Annotation> out, boolean includeDefault) {
        if (t == null) {
            return;
        }
        if (includeDefault) {
            for (Annotation a : t.annotations()) {
                out.add(a);
            }
        }

        // Collect annotations from the declaring class of the member
        Class<?> declaringClass = t.getDeclaringClass();
        if (declaringClass != null) {
            collectClassAnnotations(declaringClass, out, true);
            collectPackageAnnotations(declaringClass, out, true);
        }

        getAnnotations(toAnnotatedType(t), out, true);
    }

    public static void getAnnotations(AnnotatedType t, List<Annotation> out, boolean onlyNullability) {
        if (t == null) {
            return;
        }
        // Collect TYPE_USE annotations from the current type level
        for (Annotation ann : t.getAnnotations()) {
            // Check if this annotation can be applied to TYPE_USE
            Target target = ann.annotationType().getAnnotation(Target.class);
            if (target != null) {
                for (ElementType elementType : target.value()) {
                    if (elementType == ElementType.TYPE_USE) {
                        String annTypeName = ann.annotationType().getName();
                        if ((annTypeName.equals("org.jspecify.annotations.NullMarked") ||
                                annTypeName.equals("org.jspecify.annotations.NullUnmarked") ||
                                annTypeName.equals("org.jspecify.annotations.Nullable") ||
                                annTypeName.equals("org.jspecify.annotations.NonNull")) && onlyNullability) {
                            out.add(ann);
                            break;
                        } else if (!onlyNullability) {
                            out.add(ann);
                            break;
                        }
                    }
                }
            }
        }

        AnnotatedType owner = ownerOf(t);
        if (owner != null) {
            getAnnotations(owner, out, onlyNullability);
        }

        if (t instanceof AnnotatedParameterizedType) {
            for (AnnotatedType arg : ((AnnotatedParameterizedType) t).getAnnotatedActualTypeArguments()) {
                getAnnotations(arg, out, onlyNullability);
            }
        } else if (t instanceof AnnotatedArrayType) {
            getAnnotations(((AnnotatedArrayType) t).getAnnotatedGenericComponentType(), out, onlyNullability);
        } else if (t instanceof AnnotatedWildcardType) {
            for (AnnotatedType lb : ((AnnotatedWildcardType) t).getAnnotatedLowerBounds()) {
                getAnnotations(lb, out, onlyNullability);
            }
            for (AnnotatedType ub : ((AnnotatedWildcardType) t).getAnnotatedUpperBounds()) {
                getAnnotations(ub, out, onlyNullability);
            }
        } else if (t instanceof AnnotatedTypeVariable) {
            for (AnnotatedType b : ((AnnotatedTypeVariable) t).getAnnotatedBounds()) {
                getAnnotations(b, out, onlyNullability);
            }
        }
    }

    private static void collectPackageAnnotations(Class<?> clazz, List<Annotation> out, boolean onlyNullability) {
        Package pkg = clazz.getPackage();
        String packageName = pkg != null ? pkg.getName() : null;

        while (packageName != null && !packageName.isEmpty()) {
            try {
                Class<?> packageInfo = Class.forName(packageName + ".package-info");
                Package packageFromInfo = packageInfo.getPackage();

                for (Annotation ann : packageFromInfo.getAnnotations()) {
                    // Check if this annotation can be applied to TYPE_USE, PACKAGE, or is specifically @NullMarked
                    Target target = ann.annotationType().getAnnotation(Target.class);
                    boolean shouldInclude = false;

                    if (target != null) {
                        for (ElementType elementType : target.value()) {
                            if (!onlyNullability && (elementType == ElementType.TYPE_USE || elementType == ElementType.PACKAGE)) {
                                shouldInclude = true;
                                break;
                            }
                        }
                    }

                    // Special handling for @NullMarked and other nullability annotations
                    String annTypeName = ann.annotationType().getName();
                    if (annTypeName.equals("org.jspecify.annotations.NullMarked") ||
                            annTypeName.equals("org.jspecify.annotations.NullUnmarked") ||
                            annTypeName.equals("org.jspecify.annotations.Nullable") ||
                            annTypeName.equals("org.jspecify.annotations.NonNull")) {
                        shouldInclude = true;
                    }

                    if (shouldInclude) {
                        out.add(ann);
                    }
                }
            } catch (ClassNotFoundException e) {
                // No package-info.java found for this package level
            }

            // Move to parent package
            int lastDot = packageName.lastIndexOf('.');
            if (lastDot > 0) {
                packageName = packageName.substring(0, lastDot);
            } else {
                break;
            }
        }
    }


    private static void collectClassAnnotations(Class<?> clazz, List<Annotation> out, boolean onlyNullability) {
        // Traverse up the class hierarchy to collect annotations
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            for (Annotation ann : currentClass.getAnnotations()) {
                // Check if this annotation can be applied to TYPE, TYPE_USE, or is specifically a nullability annotation
                Target target = ann.annotationType().getAnnotation(Target.class);
                boolean shouldInclude = false;

                if (target != null) {
                    for (ElementType elementType : target.value()) {
                        if (!onlyNullability && (elementType == ElementType.TYPE || elementType == ElementType.TYPE_USE)) {
                            shouldInclude = true;
                            break;
                        }
                    }
                }

                // Special handling for @NullMarked and other nullability annotations
                String annTypeName = ann.annotationType().getName();
                    if (annTypeName.equals("org.jspecify.annotations.NullMarked") ||
                            annTypeName.equals("org.jspecify.annotations.NullUnmarked") ||
                            annTypeName.equals("org.jspecify.annotations.Nullable") ||
                            annTypeName.equals("org.jspecify.annotations.NonNull")) {
                    shouldInclude = true;
                }

                if (shouldInclude) {
                    out.add(ann);
                }
            }

            // Move to superclass
            currentClass = currentClass.getSuperclass();
        }
    }

    private static <A extends Annotation> void walk(AnnotatedType t, Class<A> annoType, List<A> out) {
        if (t == null) {
            return;
        }

        A hit = t.getAnnotation(annoType);
        if (hit != null) {
            out.add(hit);
        }

        // Cross-JDK owner handling
        AnnotatedType owner = ownerOf(t);
        if (owner != null) {
            walk(owner, annoType, out);
        }

        if (t instanceof AnnotatedParameterizedType) {
            for (AnnotatedType arg : ((AnnotatedParameterizedType) t).getAnnotatedActualTypeArguments()) {
                walk(arg, annoType, out);
            }
        } else if (t instanceof AnnotatedArrayType) {
            walk(((AnnotatedArrayType) t).getAnnotatedGenericComponentType(), annoType, out);
        } else if (t instanceof AnnotatedWildcardType) {
            for (AnnotatedType lb : ((AnnotatedWildcardType) t).getAnnotatedLowerBounds()) {
                walk(lb, annoType, out);
            }
            for (AnnotatedType ub : ((AnnotatedWildcardType) t).getAnnotatedUpperBounds()) {
                walk(ub, annoType, out);
            }
        } else if (t instanceof AnnotatedTypeVariable) {
            for (AnnotatedType b : ((AnnotatedTypeVariable) t).getAnnotatedBounds()) {
                walk(b, annoType, out);
            }
        }
    }

    /**
     * Works on Java 8 (no method) and newer (method exists).
     */
    private static AnnotatedType ownerOf(AnnotatedType t) {
        // Java 8: only AnnotatedParameterizedType exposes an owner, use that first
        if (t instanceof AnnotatedParameterizedType) {
            try {
                Method m = AnnotatedParameterizedType.class.getMethod("getAnnotatedOwnerType");
                return (AnnotatedType) m.invoke(t);
            } catch (NoSuchMethodException e) {
                return null; // Running/compiling against Java 8
            } catch (IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }
        // Java 9+: AnnotatedType has default getAnnotatedOwnerType(); call reflectively
        try {
            Method m = AnnotatedType.class.getMethod("getAnnotatedOwnerType");
            return (AnnotatedType) m.invoke(t);
        } catch (NoSuchMethodException e) {
            return null; // Running/compiling against Java 8
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
