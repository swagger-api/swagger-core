package io.swagger;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import io.swagger.models.ComposedModel;
import io.swagger.models.License;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.RefModel;
import io.swagger.models.RefResponse;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BaseIntegerProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * This class is written  in order to test all the getters and setters in this module.
 * In order to test these we just need to pass a list of classes for which the getter and setter tests should be run.
 */
@PrepareForTest({In.class, RefFormat.class, RefType.class})
public class PojosTest extends PowerMockTestCase {
    private static final String[] POJO_PACKAGES = {"io.swagger.models", "io.swagger.models.auth",
            "io.swagger.models.parameters", "io.swagger.models.properties", "io.swagger.models.refs"};

    private ArrayList<PojoClass> pojoClasses;

    @BeforeMethod
    public void setup() {
        pojoClasses = new ArrayList<PojoClass>();
        for (String pojoPackage : POJO_PACKAGES) {
            List<PojoClass> packagePojoClasses = PojoClassFactory.getPojoClasses(pojoPackage, new FilterPackageInfo());
            for (PojoClass clazz : packagePojoClasses) {
                if (clazz.getName().contains("$") || clazz.isAbstract() || clazz.isInterface() || clazz.isEnum()
                        || clazz.getName().endsWith("Test"))
                    continue;
                pojoClasses.add(clazz);
            }

        }
    }

    @Test
    public void testOpenPojo() {
        Validator validator = ValidatorBuilder.create().with(new SetterTester()).with(new GetterTester()).build();
        for (PojoClass clazz : pojoClasses) {
            try {
                validator.validate(clazz);
            } catch (AssertionError ex) {
                continue;
            }
        }
    }

    @Test
    public void testEqualsAndHashcodes() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map<Class<?>, Set<String>> classesExclusions = new HashMap<Class<?>, Set<String>>();

        classesExclusions.put(BodyParameter.class, new HashSet<String>(Arrays.asList("examples")));
        classesExclusions.put(ComposedModel.class, new HashSet<String>(Arrays.asList("reference")));
        classesExclusions.put(DoubleProperty.class, new HashSet<String>(Arrays.asList("_enum")));
        classesExclusions.put(DateProperty.class, new HashSet<String>(Arrays.asList("_enum")));
        classesExclusions.put(DateTimeProperty.class, new HashSet<String>(Arrays.asList("_enum")));
        classesExclusions.put(FloatProperty.class, new HashSet<String>(Arrays.asList("_enum")));
        classesExclusions.put(IntegerProperty.class, new HashSet<String>(Arrays.asList("_enum")));
        classesExclusions.put(License.class, new HashSet<String>(Arrays.asList("vendorExtensions")));
        classesExclusions.put(LongProperty.class, new HashSet<String>(Arrays.asList("_enum")));
        classesExclusions.put(ModelImpl.class, new HashSet<String>(Arrays.asList("_enum")));
        classesExclusions.put(ObjectProperty.class, new HashSet<String>(Arrays.asList("properties")));
        classesExclusions.put(RefModel.class, new HashSet<String>(Arrays.asList("title")));
        classesExclusions.put(RefResponse.class,
                new HashSet<String>(Arrays.asList("headers", "schema", "vendorExtensions")));
        classesExclusions.put(Swagger.class, new HashSet<String>(Arrays.asList("vendorExtensions", "responses")));
        classesExclusions.put(Tag.class, new HashSet<String>(Arrays.asList("vendorExtensions")));

        Set<Class<?>> classesUsingInheritedFields = new HashSet<Class<?>>(Arrays.asList(ApiKeyAuthDefinition.class,
                BodyParameter.class, ArrayProperty.class, BaseIntegerProperty.class, CookieParameter.class));
        Set<Class<?>> excludedClasses = new HashSet<Class<?>>(Arrays.asList(PropertyBuilder.class));
        for (PojoClass clazz : pojoClasses) {
            if (excludedClasses.contains(clazz.getClazz()))
                continue;
            Set<String> exclusions = classesExclusions.get(clazz.getClazz());
            TestUtils.testEquals(clazz.getClazz(), exclusions, classesUsingInheritedFields.contains(clazz.getClazz()));
        }
    }

    @Test
    public void testBuildersAndCommonMethods() throws Exception {
        Map<Class<?>, Set<String>> classesExclusions = new HashMap<Class<?>, Set<String>>();

        classesExclusions.put(Operation.class, new HashSet<String>(Arrays.asList("deprecated", "vendorExtensions")));
        classesExclusions.put(Swagger.class, new HashSet<String>(Arrays.asList("vendorExtensions")));

        for (PojoClass clazz : pojoClasses) {
            Set<String> exclusions = classesExclusions.get(clazz.getClazz());
            TestUtils.testBuilders(clazz.getClazz(), exclusions);
            TestUtils.testCommonMethods(clazz.getClazz(), exclusions);
        }
    }
}
