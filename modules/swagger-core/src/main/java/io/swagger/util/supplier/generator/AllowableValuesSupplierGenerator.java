package io.swagger.util.supplier.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import io.swagger.util.supplier.compiler.InMemoryJavaFileObject;
import io.swagger.util.supplier.filer.DirectoryTempHelper;

/**
 * Class to generate supplier class source code to get all values of an Enum
 * @author dedece35
 */
public class AllowableValuesSupplierGenerator {

    private static Logger LOGGER = LoggerFactory.getLogger(AllowableValuesSupplierGenerator.class);

    public static final String GENERATED_CLASS_SUFFIX = "GeneratedSupplier";

    public static final String GENERATED_PACKAGE_SUFFIX = ".generatedcode";

    /**
     * Generation of a Supplier class to get Enum values.
     * @param enumClass original Enum class from what we want values
     * @return InMemory object containing generated source code
     * @throws JClassAlreadyExistsException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public InMemoryJavaFileObject generateSupplierClass(final Class<? extends Enum<?>> enumClass) throws JClassAlreadyExistsException, ClassNotFoundException, IOException {

        LOGGER.debug("GENERATOR - Init generator");
        JCodeModel codeModel = new JCodeModel();

        // generation of package (the same as source class + ".generatedcode")
        LOGGER.debug("GENERATOR - Package generation");
        String packageName = enumClass.getPackage().getName() + GENERATED_PACKAGE_SUFFIX;
        JPackage jp = codeModel._package(packageName);

        // generation of supplier class
        LOGGER.debug("GENERATOR - Supplier Class generation : init");
        JDefinedClass jc;
        String className = enumClass.getSimpleName() + GENERATED_CLASS_SUFFIX;
        try {
            jc = jp._class(className);
        } catch (JClassAlreadyExistsException e) {
            String msgErr = "GENERATOR - Error during generation of Supplier for Enum class " + enumClass.getName() + " : " + e.getMessage();
            LOGGER.error(msgErr);
            throw e;
        }

        // add method signature
        LOGGER.debug("GENERATOR - Supplier Class generation : add method signature");
        JClass clazzList;
        try {
            clazzList = (JClass) codeModel.parseType("java.util.List");
        } catch (ClassNotFoundException e) {
            LOGGER.error("GENERATOR - Impossible to generate method (preparing result object) : {}", e.getMessage());
            throw e;
        }
        JClass clazzListString = clazzList.narrow(String.class);
        JMethod getAllowableValuesMethod = jc.method(JMod.PUBLIC, clazzListString, "getValues");

        // add method body
        LOGGER.debug("GENERATOR - Supplier Class generation : add method body");
        String fieldNameLocal = "lstRes";
        JClass clazzArrayList;
        try {
            clazzArrayList = (JClass) codeModel.parseType("java.util.ArrayList");
        } catch (ClassNotFoundException e) {
            LOGGER.error("GENERATOR - Impossible to generate method (instanciation of result object) : {}", e.getMessage());
            throw e;
        }
        JClass clazzArrayListString = clazzArrayList.narrow(String.class);
        getAllowableValuesMethod.body().decl(
                clazzListString,
                fieldNameLocal,
                JExpr._new(clazzArrayListString).arg(JExpr.direct(enumClass.getSimpleName() + ".values().length")));

        // add foreach in method body
        String fieldNameEnum = "enumValue";
        JClass clazzEnum;
        try {
            clazzEnum = (JClass) codeModel.parseType(enumClass.getName());
        } catch (ClassNotFoundException e) {
            LOGGER.error("GENERATOR - Impossible to generate method (preparing loop on values) : {}", e.getMessage());
            throw e;
        }
        JForEach foreach = new JForEach(clazzEnum, fieldNameEnum, JExpr.direct(enumClass.getSimpleName() + ".values()"));
        foreach.body().directStatement(fieldNameLocal + ".add(" + fieldNameEnum + ".name());");
        getAllowableValuesMethod.body().add(foreach);

        // add return in method body
        getAllowableValuesMethod.body()._return(JExpr.ref(fieldNameLocal));

        // generate supplier source code in Temp directory
        LOGGER.debug("GENERATOR - Init Source TMP DIR");
        File genDir = DirectoryTempHelper.createTmpDir();

        LOGGER.debug("GENERATOR - Launching build of generation in directory '{}'", genDir.getAbsolutePath());
        try {
            codeModel.build(genDir);
        } catch (IOException e) {
            LOGGER.error("GENERATOR - Impossible to generate class (build generation) : {}", e.getMessage());
            throw e;
        }

        // convert file source code in memory source code
        LOGGER.debug("GENERATOR - Building InMemory Representation of source code");
        String fullClassName = packageName + "." + className;
        String sourceFilePath = genDir.getAbsolutePath() + File.separator + StringUtils.replace(fullClassName, ".", File.separator) + ".java";
        String sourceCode = readFile(sourceFilePath);
        InMemoryJavaFileObject jfo = InMemoryJavaFileObject.buildJavaFileObject(fullClassName, sourceCode);

        // clean Temp directory
        LOGGER.debug("GENERATOR - Delete Source TMP DIR '{}'", genDir.getPath());
        DirectoryTempHelper.deleteDir(genDir);

        return jfo;
    }

    /**
     * Read all file content
     * @param filePath File path to read
     * @return String representing all content of the file
     */
    private static String readFile(final String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LOGGER.error("GENERATOR - error during read of source code file {} : {}", filePath, e.getMessage());
        }
        return content;
    }

}
