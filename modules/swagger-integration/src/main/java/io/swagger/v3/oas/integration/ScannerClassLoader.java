package io.swagger.v3.oas.integration;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Loads scanner results without running application static initializers.
 * The thread context classloader takes precedence so application resources can
 * be resolved in containers, with the scanner and system classloaders as fallbacks.
 */
public final class ScannerClassLoader {
    private ScannerClassLoader() {
    }

    public static Set<Class<?>> loadClasses(Iterable<String> classNames, Class<?> scannerClass) {
        Set<ClassLoader> classLoaders = new LinkedHashSet<>();
        classLoaders.add(Thread.currentThread().getContextClassLoader());
        classLoaders.add(scannerClass.getClassLoader());
        classLoaders.add(ScannerClassLoader.class.getClassLoader());
        classLoaders.add(ClassLoader.getSystemClassLoader());
        classLoaders.remove(null);

        Set<Class<?>> classes = new HashSet<>();
        for (String className : classNames) {
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
}
