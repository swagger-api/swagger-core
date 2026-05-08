package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AnnotatedTypeCachingTest {

    @Test
    public void testAnnotatedTypeEqualityIgnoresContextualFields() {
        AnnotatedType type1 = new AnnotatedType(String.class)
                .propertyName("userStatus");
        AnnotatedType type2 = new AnnotatedType(String.class)
                .propertyName("city");
        assertEquals(type1, type2, "AnnotatedType objects with different contextual fields (e.g., propertyName) should be equal.");
        assertEquals(type1.hashCode(), type2.hashCode(), "The hash codes of equal AnnotatedType objects must be the same.");
    }

    static class User {
        public String username;
        public String email;
        public Address address;
    }

    static class Address {
        public String street;
        public String city;
    }

    private static class DummyModelConverter implements ModelConverter {
        @Override
        public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            if (type.getType().equals(User.class)) {
                context.resolve(new AnnotatedType(String.class).propertyName("username"));
                context.resolve(new AnnotatedType(String.class).propertyName("email"));
                context.resolve(new AnnotatedType(Address.class).propertyName("address"));
                return new Schema();
            }
            if (type.getType().equals(Address.class)) {
                context.resolve(new AnnotatedType(String.class).propertyName("street"));
                context.resolve(new AnnotatedType(String.class).propertyName("city"));
                return new Schema();
            }
            return new Schema();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCacheHitsForRepeatedStringTypeWithCorrectedEquals() throws Exception {
        ModelConverterContextImpl context = new ModelConverterContextImpl(new DummyModelConverter());
        Schema userSchema = context.resolve(new AnnotatedType(User.class));
        assertNotNull(userSchema);
        Field processedTypesField = ModelConverterContextImpl.class.getDeclaredField("processedTypes");
        processedTypesField.setAccessible(true);
        Set<AnnotatedType> processedTypes = (Set<AnnotatedType>) processedTypesField.get(context);
        long stringTypeCount = processedTypes.stream()
                .filter(annotatedType -> annotatedType.getType().equals(String.class))
                .count();
        assertEquals(stringTypeCount, 1, "With the correct equals/hashCode, String type should be added to the cache only once.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNoCacheHitForAFieldThatIsMarkedAsDeprecated() throws Exception {
        ModelConverterContextImpl context = new ModelConverterContextImpl(new FooBarDummyModelConverter());
        Schema fooSchema = context.resolve(new AnnotatedType(Foo.class));
        assertNotNull(fooSchema);
        Field processedTypesField = ModelConverterContextImpl.class.getDeclaredField("processedTypes");
        processedTypesField.setAccessible(true);
        Set<AnnotatedType> processedTypes = (Set<AnnotatedType>) processedTypesField.get(context);
        long stringTypeCount = processedTypes.stream()
                .filter(annotatedType -> annotatedType.getType().equals(String.class))
                .count();
        assertEquals(stringTypeCount, 2, "With the correct equals/hashCode, String type should be added to the cache twice, since one of them is deprecated.");
    }

    private static class FooBarDummyModelConverter implements ModelConverter {
        @Override
        public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            if (type.getType().equals(Foo.class)) {
                context.resolve(new AnnotatedType(String.class).propertyName("fizz").ctxAnnotations(new Annotation[]{getAnnotationInstance(Deprecated.class)}));
                context.resolve(new AnnotatedType(String.class).propertyName("buzz"));
                context.resolve(new AnnotatedType(Bar.class).propertyName("bar"));
                return new Schema();
            }
            if (type.getType().equals(Bar.class)) {
                context.resolve(new AnnotatedType(String.class).propertyName("fizz"));
                context.resolve(new AnnotatedType(String.class).propertyName("buzz"));
                return new Schema();
            }
            return new Schema();
        }
    }

    private static Annotation getAnnotationInstance(Class<? extends Annotation> clazz) {
        try {
            return Foo.class.getDeclaredField("fizz").getAnnotation(clazz);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static class Foo {
        @Deprecated
        public String fizz;
        public String buzz;
        public Bar bar;
    }

    static class Bar {
        public String fizz;
        public String buzz;
    }
}