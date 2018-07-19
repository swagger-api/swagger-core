package io.swagger.util.supplier.compiler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.util.supplier.filer.DirectoryTempHelper;

/**
 * @author dedece35
 */
public class DynamicCompilerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompile_NominalCase() {

        // preparing beans
        StringBuilder contents = new StringBuilder(
                "package mypackage;"
                        + "import java.util.List;"
                        + "import java.util.ArrayList;"
                        + "public class MyClass { "
                        + "  public List<String> getList() { "
                        + "    List<String> lstRes = new ArrayList<String>(2); "
                        + "    lstRes.add(\"200\");"
                        + "    lstRes.add(\"300\");"
                        + "    System.out.println(lstRes.get(1));"
                        + "    return lstRes;"
                        + "  } "
                        + "  public static void main(String[] args) {"
                        + "    MyClass mc = new MyClass();"
                        + "    mc.getList();"
                        + "  }"
                        + "}");
        String fullClassName = "mypackage.MyClass";
        InMemoryJavaFileObject jfo = InMemoryJavaFileObject.buildJavaFileObject(fullClassName, contents.toString());

        // Launch method to test
        File classDir = DynamicCompiler.compile(jfo);

        // Launch method to test BIS
        Object obj = DynamicCompiler.execute(classDir, fullClassName, "getList");

        // checks
        List<String> lstRes = null;
        try {
            lstRes = (List<String>) obj;
        } catch (ClassCastException ex) {
            Assert.fail(ex.getMessage());
        }

        Assert.assertNotNull(lstRes);
        Assert.assertEquals(lstRes.size(), 2);
        Assert.assertEquals(lstRes.get(0), "200");
        Assert.assertEquals(lstRes.get(1), "300");

    }

    @Test
    public void testCompile_CompilationErrorCase() {

        // preparing beans
        StringBuilder contents = new StringBuilder(
                "package mypackage;"
                        + "import java.util.List;"
                        + "import java.util.ArrayList;"
                        + "public class MyClass { "
                        + "  public List<String> getList() { "
                        + "    List<String> lstRes = new ArrayList<String>(2); "
                        + "    lstRes.add(\"200\");"
                        + "    lstRes.add(\"300\");"
                        + "    System.out.println(lstRes.get(1));"
                        + "    return lstResUNKNOWN;" // compilation error here
                        + "  } "
                        + "  public static void main(String[] args) {"
                        + "    MyClass mc = new MyClass();"
                        + "    mc.getList();"
                        + "  }"
                        + "}");
        String fullClassName = "mypackage.MyClass";
        InMemoryJavaFileObject jfo = InMemoryJavaFileObject.buildJavaFileObject(fullClassName, contents.toString());

        // Launch method to test
        File classDir = DynamicCompiler.compile(jfo);

        // checks
        Assert.assertNull(classDir);

    }

    @Test
    public void testCompile_JavaFileObjectNullCase() {
        // Launch method to test
        File classDir = DynamicCompiler.compile(null);
        // checks
        Assert.assertNull(classDir);
    }

    @Test
    public void testCompile_ClassDirNullCase() {
        // Launch method to test
        Object res = DynamicCompiler.execute(null, "fullClassName", "methodName");
        // checks
        Assert.assertNull(res);
    }

    @Test
    public void testCompile_ClassNameBlankCase() {
        // Launch method to test
        Object res = DynamicCompiler.execute(new File(""), "", "methodName");
        // checks
        Assert.assertNull(res);
    }

    @Test
    public void testCompile_MethodNameBlankCase() {
        // Launch method to test
        Object res = DynamicCompiler.execute(new File(""), "fullClassName", null);
        // checks
        Assert.assertNull(res);
    }

    @Test
    public void testCompile_ClassUnknownCase() throws IOException {
        // preparing Tmp Dir
        File tmpDir = DirectoryTempHelper.createTmpDir();

        // Launch method to test
        Object res = DynamicCompiler.execute(tmpDir, "fullClassName", "methodName");
        // checks
        Assert.assertNull(res);
    }

}
