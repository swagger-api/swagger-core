package io.swagger.util.supplier.generator;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.util.supplier.compiler.InMemoryJavaFileObject;
import io.swagger.util.supplier.generator.bean.MyEnum;

/**
 * Test the generation of source code of supplier class for enum values
 * @author dedece35
 */
public class AllowableValuesSupplierGeneratorTest {

    @Test
    public void testGeneration_NominalCase() {
        Class<MyEnum> enumClass = MyEnum.class;

        AllowableValuesSupplierGenerator gen = new AllowableValuesSupplierGenerator();
        InMemoryJavaFileObject jfo = null;
        try {
            jfo = gen.generateSupplierClass(enumClass);
        } catch (Exception e) {
            Assert.fail(e.getMessage(), e);
        }

        Assert.assertNotNull(jfo);

        Assert.assertNotNull(jfo.getClassName());
        Assert.assertEquals(jfo.getClassName(),
                enumClass.getPackage().getName() +
                        AllowableValuesSupplierGenerator.GENERATED_PACKAGE_SUFFIX +
                        "." +
                        enumClass.getSimpleName() +
                        AllowableValuesSupplierGenerator.GENERATED_CLASS_SUFFIX);

        String expectedSourceCode = "\n" +
                "package io.swagger.util.supplier.generator.bean.generatedcode;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import io.swagger.util.supplier.generator.bean.MyEnum;\n" +
                "\n" +
                "public class MyEnumGeneratedSupplier {\n" +
                "\n" +
                "\n" +
                "    public List<String> getValues() {\n" +
                "        List<String> lstRes = new ArrayList<String>((MyEnum.values().length));\n" +
                "        for (MyEnum enumValue: (MyEnum.values())) {\n" +
                "            lstRes.add(enumValue.name());\n" +
                "        }\n" +
                "        return lstRes;\n" +
                "    }\n" +
                "\n" +
                "}\n";
        Assert.assertNotNull(jfo.getSourceCode());
        Assert.assertEquals(jfo.getSourceCode(), expectedSourceCode);

    }

}
