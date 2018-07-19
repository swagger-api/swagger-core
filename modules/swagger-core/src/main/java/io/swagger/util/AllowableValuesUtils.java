package io.swagger.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.JClassAlreadyExistsException;

import io.swagger.util.supplier.compiler.DynamicCompiler;
import io.swagger.util.supplier.compiler.InMemoryJavaFileObject;
import io.swagger.util.supplier.generator.AllowableValuesSupplierGenerator;

public class AllowableValuesUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(AllowableValuesUtils.class);

    public static AllowableValues create(final String values) {
        AllowableValues allowableValues = null;
        if (StringUtils.isNotEmpty(values)) {
            allowableValues = AllowableRangeValues.create(values);
            if (allowableValues == null) {
                allowableValues = AllowableEnumValues.create(values);
            }
        }
        return allowableValues;
    }

    /**
     * Create a AllowableValues object from an Enum class name.
     * @param enumClassName The Enum class name
     * @return the AllowableValues object created
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static AllowableValues createFromEnumClass(final String enumClassName) {

        if (StringUtils.isBlank(enumClassName)) {
            return null;
        }

        // load enum class
        LOGGER.debug("UTILS - Enum class invoking");
        Class enumClass = getClassFromName(enumClassName);

        if (enumClass == null) {
            return null;
        }

        // build an InMemory JavaFileObject and generate source code of a supplier class to give values of the enum class
        LOGGER.debug("UTILS - Supplier source code generating");
        InMemoryJavaFileObject jfo = null;
        try {
            AllowableValuesSupplierGenerator generator = new AllowableValuesSupplierGenerator();
            jfo = generator.generateSupplierClass(enumClass);
        } catch (ClassNotFoundException | JClassAlreadyExistsException | IOException e) {
            LOGGER.error("UTILS - error during generation of supplier class : {}", e.getMessage());
            return null;
        }

        // compile generated source code
        LOGGER.debug("UTILS - Supplier source code compilation");
        File classDir = DynamicCompiler.compile(jfo);
        if (classDir == null) {
            return null;
        }

        // execute supplier class compiled code and run "getValues" method to get all values of Enum class
        LOGGER.debug("UTILS - Supplier class loading and method invoking");
        Object obj = DynamicCompiler.execute(classDir, jfo.getClassName(), "getValues");
        if (obj == null) {
            return null;
        }

        // get result of executing method
        LOGGER.info("UTILS - Getting values from Supplier");
        // display results
        List<String> items = null;
        try {
            items = (List<String>) obj;
        } catch (ClassCastException e) {
            LOGGER.error("UTILS - error during classCasting results : {}", e.getMessage());
            return null;
        }

        return items == null || items.isEmpty() ? null : AllowableEnumValues.create(items);
    }

    /**
     * Get class from a class name
     * @param className Class name to load
     * @return The class loaded
     */
    @SuppressWarnings("rawtypes")
    private static Class getClassFromName(final String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Class '{}' not found : {}", className, e.getMessage());
        }
        return clazz;
    }

}
