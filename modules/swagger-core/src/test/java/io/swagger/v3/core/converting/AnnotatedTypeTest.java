package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.AnnotatedType;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

public class AnnotatedTypeTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface TestAnnA {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface TestAnnB {}

    @TestAnnA
    @TestAnnB
    @Deprecated
    private static class AnnotationHolder {}

    private Annotation getAnnotationInstance(Class<? extends Annotation> clazz) {
        return AnnotationHolder.class.getAnnotation(clazz);
    }

    /**
     * Tests that equals() and hashCode() are order-insensitive for context annotations.
     */
    @Test
    public void testEqualsAndHashCode_shouldBeOrderInsensitiveForAnnotations() {
        Annotation annA = getAnnotationInstance(TestAnnA.class);
        Annotation annB = getAnnotationInstance(TestAnnB.class);
        AnnotatedType type1 = new AnnotatedType(String.class).ctxAnnotations(new Annotation[]{annA, annB});
        AnnotatedType type2 = new AnnotatedType(String.class).ctxAnnotations(new Annotation[]{annB, annA});
        assertEquals(type1, type2, "Objects should be equal even if annotation order is different.");
        assertEquals(type1.hashCode(), type2.hashCode(), "Hash codes should be equal even if annotation order is different.");
    }

    /**
     * Tests that JDK/internal annotations are filtered out for equals() and hashCode() comparison.
     */
    @Test
    public void testEqualsAndHashCode_shouldIgnoreJdkInternalAnnotations() {
        Annotation annA = getAnnotationInstance(TestAnnA.class);
        Annotation deprecated = getAnnotationInstance(Deprecated.class);
        AnnotatedType typeWithUserAnn = new AnnotatedType(String.class).ctxAnnotations(new Annotation[]{annA});
        AnnotatedType typeWithJdkAnn = new AnnotatedType(String.class).ctxAnnotations(new Annotation[]{annA, deprecated});
        AnnotatedType typeWithOnlyJdkAnn = new AnnotatedType(String.class).ctxAnnotations(new Annotation[]{deprecated});
        AnnotatedType typeWithNoAnn = new AnnotatedType(String.class);
        assertEquals(typeWithUserAnn, typeWithJdkAnn, "JDK annotations should be ignored in equality comparison.");
        assertEquals(typeWithUserAnn.hashCode(), typeWithJdkAnn.hashCode(), "JDK annotations should be ignored in hashCode calculation.");
        assertEquals(typeWithOnlyJdkAnn, typeWithNoAnn, "An object with only JDK annotations should be equal to one with no annotations.");
        assertEquals(typeWithOnlyJdkAnn.hashCode(), typeWithNoAnn.hashCode(), "The hash code of an object with only JDK annotations should be the same as one with no annotations.");
    }

    /**
     * Tests that defensive copying prevents Set corruption from external array mutation.
     */
    @Test
    public void testImmutability_shouldPreventCorruptionInHashSet() {
        Annotation annA = getAnnotationInstance(TestAnnA.class);
        Annotation annB = getAnnotationInstance(TestAnnB.class);
        Annotation[] originalAnnotations = new Annotation[]{annA};
        AnnotatedType type = new AnnotatedType(String.class).ctxAnnotations(originalAnnotations);
        Set<AnnotatedType> typeSet = new HashSet<>();
        typeSet.add(type);
        int initialHashCode = type.hashCode();
        originalAnnotations[0] = annB;
        assertEquals(initialHashCode, type.hashCode(), "Hash code must remain the same after mutating the external array.");
        assertTrue(typeSet.contains(type), "The Set must still contain the object after mutating the external array.");
    }

    /**
     * Tests that an instance of a subclass can be equal to an instance of the parent class.
     */
    @Test
    public void testEqualsAndHashCode_shouldAllowSubclassEquality() {
        class SubAnnotatedType extends AnnotatedType {
            public SubAnnotatedType(Type type) { super(type); }
        }
        Annotation annA = getAnnotationInstance(TestAnnA.class);
        Annotation[] annotations = {annA};
        AnnotatedType parent = new AnnotatedType(Integer.class).ctxAnnotations(annotations).name("number");
        SubAnnotatedType child = new SubAnnotatedType(Integer.class);
        child.ctxAnnotations(annotations);
        child.name("number");
        AnnotatedType differentParent = new AnnotatedType(Long.class).name("number");
        assertEquals(parent, child, "Parent and child objects should be equal if their properties are the same.");
        assertEquals(child, parent, "Equality comparison should be symmetric.");
        assertEquals(parent.hashCode(), child.hashCode(), "Parent and child hash codes should be equal if their properties are the same.");
        assertNotEquals(parent, differentParent, "Objects with different properties should not be equal.");
    }
}