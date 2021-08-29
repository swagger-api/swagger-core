package io.swagger.v3.core.util.reflection;

import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.core.util.reflection.resources.Child;
import io.swagger.v3.core.util.reflection.resources.IParent;
import io.swagger.v3.core.util.reflection.resources.ObjectWithManyFields;
import io.swagger.v3.core.util.reflection.resources.Parent;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.Path;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertNull;

import static java.lang.annotation.ElementType.PARAMETER;

public class ReflectionUtilsTest {

    @Test
    public void typeFromStringTest() {
        Assert.assertEquals(ReflectionUtils.typeFromString("int"), (Type) Integer.class);
        Assert.assertEquals(ReflectionUtils.typeFromString("java.lang.String"), (Type) String.class);
        Assert.assertNull(ReflectionUtils.typeFromString("FakeType"));
        Assert.assertNull(ReflectionUtils.typeFromString(null));
    }

    @Test
    public void isOverriddenMethodTest() throws NoSuchMethodException {
        for (Method method : Child.class.getMethods()) {
            if ("parametrizedMethod1".equals(method.getName())) {
                final boolean result = ReflectionUtils.isOverriddenMethod(method, Child.class);
                final Class<?> first = method.getParameterTypes()[0];
                if (Number.class.equals(first)) {
                    Assert.assertTrue(result);
                } else if (Integer.class.equals(first)) {
                    Assert.assertFalse(result);
                }
            } else if ("parametrizedMethod3".equals(method.getName())) {
                Assert.assertFalse(ReflectionUtils.isOverriddenMethod(method, Child.class));
            }
        }

        for (Method method : Object.class.getMethods()) {
            if ("equals".equals(method.getName())) {
                Assert.assertFalse(ReflectionUtils.isOverriddenMethod(method, Object.class));
            }
        }

        for (Method method : IParent.class.getMethods()) {
            if ("parametrizedMethod5".equals(method.getName())) {
                Assert.assertFalse(ReflectionUtils.isOverriddenMethod(method, IParent.class));
            } else if ("parametrizedMethod2".equals(method.getName())) {
                Assert.assertFalse(ReflectionUtils.isOverriddenMethod(method, IParent.class));
            } else {
                Assert.fail("Method not expected");
            }
        }
    }

    @Test
    public void getOverriddenMethodTest() throws NoSuchMethodException {
        final Method method1 = ReflectionUtils.getOverriddenMethod(
                Child.class.getMethod("parametrizedMethod1", Integer.class));
        Assert.assertNotNull(method1);
        Assert.assertEquals(method1.getParameterTypes()[0], Number.class);

        final Method method2 = ReflectionUtils.getOverriddenMethod(
                Child.class.getMethod("parametrizedMethod2", Long.class));
        Assert.assertNotNull(method2);
        Assert.assertEquals(method2.getParameterTypes()[0], Number.class);

        final Method method3 = ReflectionUtils.getOverriddenMethod(
                Child.class.getMethod("parametrizedMethod3", Long.class));
        Assert.assertNull(method3);

        Assert.assertNull(ReflectionUtils.getOverriddenMethod(Object.class.getMethod("equals", Object.class)));
    }

    @Test
    public void findMethodTest() throws NoSuchMethodException {
        final Method methodToFind1 = Child.class.getMethod("parametrizedMethod1", Integer.class);
        final Method method1 = ReflectionUtils.findMethod(methodToFind1, Parent.class);
        Assert.assertNotNull(method1);
        Assert.assertEquals(method1.getParameterTypes()[0], Number.class);

        final Method methodToFind2 = Child.class.getMethod("parametrizedMethod4", Long.class);
        final Method method2 = ReflectionUtils.findMethod(methodToFind2, Parent.class);
        Assert.assertNull(method2);
    }

    @Test
    public void isInjectTest() throws NoSuchMethodException {
        final Method injectableMethod = Child.class.getMethod("injectableMethod");
        Assert.assertTrue(ReflectionUtils.isInject(Arrays.asList(injectableMethod.getDeclaredAnnotations())));

        final Method methodToFind = Child.class.getMethod("parametrizedMethod1", Integer.class);
        Assert.assertFalse(ReflectionUtils.isInject(Arrays.asList(methodToFind.getDeclaredAnnotations())));
    }

    @Test
    public void isConstructorCompatibleTest() throws NoSuchMethodException {
        Assert.assertFalse(ReflectionUtils.isConstructorCompatible(Child.class.getDeclaredConstructor()));
        Assert.assertTrue(ReflectionUtils.isConstructorCompatible(Child.class.getDeclaredConstructor(String.class)));
    }

    @Test
    public void getAnnotationTest() throws NoSuchMethodException {
        final Method method = Child.class.getMethod("annotationHolder");
        Assert.assertNotNull(ReflectionUtils.getAnnotation(method, Schema.class));
        Assert.assertNull(ReflectionUtils.getAnnotation(method, ApiResponse.class));
    }

    @Test
    public void isVoidTest() {
        Assert.assertTrue(ReflectionUtils.isVoid(Void.class));
        Assert.assertTrue(ReflectionUtils.isVoid(Void.TYPE));
        Assert.assertFalse(ReflectionUtils.isVoid(String.class));
    }

    @Test
    public void testDerivedAnnotation() {
        final Path annotation = ReflectionUtils.getAnnotation(Child.class, javax.ws.rs.Path.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals(annotation.value(), "parentInterfacePath");
    }

    @Test
    public void getDeclaredFieldsFromInterfaceTest() throws NoSuchMethodException {
        final Class cls = IParent.class;
        Assert.assertEquals(Collections.emptyList(), ReflectionUtils.getDeclaredFields(cls));
    }

    @Test
    public void declaredFieldsShouldBeSorted() {
        final Class cls = ObjectWithManyFields.class;
        final List<Field> declaredFields = ReflectionUtils.getDeclaredFields(cls);
        Assert.assertEquals(4, declaredFields.size());
        Assert.assertEquals(Arrays.asList("a", "b", "c", "d"), declaredFields.stream().map(Field::getName).collect(Collectors.toList()));
    }

    @Test
    public void testFindMethodForNullClass() throws Exception {
        Method method = ReflectionUtilsTest.class.getMethod("testFindMethodForNullClass", (Class<?>[]) null);
        assertNull(ReflectionUtils.findMethod(method, null));
    }

    @Test
    public void getRepeatableAnnotationsArrayTest() {
        Tag[] annotations = ReflectionUtils.getRepeatableAnnotationsArray(InheritingClass.class, Tag.class);
        Assert.assertNotNull(annotations);
        Assert.assertTrue(annotations.length == 1);
        Assert.assertNotNull(annotations[0]);
        Assert.assertEquals("inherited tag", annotations[0].name());
    }

    @Test
    public void getParameterAnnotationsTest() throws NoSuchMethodException {
        Method method = SecondLevelSubClass.class.getMethod("method", String.class);
        Annotation[][] parameterAnnotations = ReflectionUtils.getParameterAnnotations(method);
        Assert.assertEquals(1, parameterAnnotations.length);
        Assert.assertEquals(1, parameterAnnotations[0].length);
        Assert.assertTrue(parameterAnnotations[0][0] instanceof AnnotationInterface);
        Assert.assertEquals("level1", ((AnnotationInterface)parameterAnnotations[0][0]).value());
    }

    @Test
    public void getParameterAnnotationsForOverriddenAnnotationTest() throws NoSuchMethodException {
        Method method = ThirdLevelSubClass.class.getMethod("method", String.class);
        Annotation[][] parameterAnnotations = ReflectionUtils.getParameterAnnotations(method);
        Assert.assertEquals(1, parameterAnnotations.length);
        Assert.assertEquals(1, parameterAnnotations[0].length);
        Assert.assertTrue(parameterAnnotations[0][0] instanceof AnnotationInterface);
        Assert.assertEquals("level4", ((AnnotationInterface)parameterAnnotations[0][0]).value());
    }

    @Tag(name = "inherited tag")
    private interface AnnotatedInterface {}

    private class InheritingClass implements AnnotatedInterface {}

    @Target({PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface AnnotationInterface {
        String value();
    }

    private static class BaseClass {
        public void method(@AnnotationInterface("level1") String example) {}
    }

    private static class FirstLevelSubClass extends BaseClass {
        @Override
        public void method(String example){}
    }

    private static class SecondLevelSubClass extends FirstLevelSubClass {
        @Override
        public void method(String example){}
    }

    private static class ThirdLevelSubClass extends SecondLevelSubClass {
        @Override
        public void method(@AnnotationInterface("level4") String example){}
    }

}
