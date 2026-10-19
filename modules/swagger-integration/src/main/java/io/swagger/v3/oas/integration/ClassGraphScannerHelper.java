package io.swagger.v3.oas.integration;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Bridges ClassGraph 4 and 5 while retaining ClassGraph 4's scanning and loading behavior.
 */
public final class ClassGraphScannerHelper {
    private static final Method ENABLE_ALL_INFO = findMethod(ClassGraph.class, "enableAllInfo");
    private static final Method LOAD_CLASSES = findMethod(ClassInfoList.class, "loadClasses");

    private ClassGraphScannerHelper() {
    }

    public static ClassGraph createClassGraph() {
        ClassGraph graph = new ClassGraph();
        if (ENABLE_ALL_INFO != null) {
            invoke(ENABLE_ALL_INFO, graph);
        } else {
            // ClassGraph 5 no longer enables scan sources implicitly.
            invokeRequired(graph, "enableClasspath");
            invokeRequired(graph, "enableNonSystemModules");
            graph.enableClassInfo().enableAnnotationInfo().ignoreClassVisibility();
        }
        return graph;
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> loadClasses(ClassInfoList classInfo, Class<?> scannerClass) {
        if (LOAD_CLASSES != null) {
            // Preserve ClassGraph 4's choice of classloaders in existing deployments.
            return new HashSet<>((List<Class<?>>) invoke(LOAD_CLASSES, classInfo));
        }

        Set<ClassLoader> classLoaders = new LinkedHashSet<>();
        classLoaders.add(Thread.currentThread().getContextClassLoader());
        classLoaders.add(scannerClass.getClassLoader());
        classLoaders.add(ClassGraphScannerHelper.class.getClassLoader());
        classLoaders.add(ClassLoader.getSystemClassLoader());
        classLoaders.remove(null);

        Set<Class<?>> classes = new HashSet<>();
        for (String className : classInfo.getNames()) {
            classes.add(loadClass(className, classLoaders));
        }
        return classes;
    }

    private static Class<?> loadClass(String className, Set<ClassLoader> classLoaders) {
        ClassNotFoundException failure = null;
        for (ClassLoader classLoader : classLoaders) {
            try {
                return Class.forName(className, false, classLoader);
            } catch (ClassNotFoundException e) {
                failure = e;
            }
        }
        throw new IllegalArgumentException("Could not load scanned class " + className, failure);
    }

    private static Method findMethod(Class<?> type, String name) {
        try {
            return type.getMethod(name);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static void invokeRequired(ClassGraph graph, String name) {
        Method method = findMethod(ClassGraph.class, name);
        if (method == null) {
            throw new IllegalStateException("Unsupported ClassGraph version: missing " + name);
        }
        invoke(method, graph);
    }

    private static Object invoke(Method method, Object target) {
        try {
            return method.invoke(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access ClassGraph method " + method.getName(), e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new IllegalStateException("ClassGraph method failed: " + method.getName(), cause);
        }
    }
}
