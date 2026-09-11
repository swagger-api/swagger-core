package io.swagger.v3.core.util;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.converter.AnnotatedType;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Unit test cases for {@link ReferenceTypeUtils}
 */
public class ReferenceTypeUtilsTest {

    @Test(description = "AtomicReference should be reference type")
    public void testIsReferenceTypeWithAtomicReference() {
        final JavaType referredType = TypeFactory.createDefaultInstance().constructType(String.class);
        final Class<AtomicReference> rawType = AtomicReference.class;
        final JavaType atomicReferenceType = TypeFactory.createDefaultInstance().constructReferenceType(rawType, referredType);

        final boolean actualIsReferenceType = ReferenceTypeUtils._isReferenceType(atomicReferenceType);

        assertTrue(actualIsReferenceType, rawType.getCanonicalName() + " should be reference type but was not.");
    }

    @Test(description = "AtomicReference JavaType should be unwrapped")
    public void testUnwrapWithAtomicReferenceAndJavaType() {
        final JavaType expectedReferredType = TypeFactory.createDefaultInstance().constructType(String.class);

        final Class<AtomicReference> rawType = AtomicReference.class;
        final JavaType atomicReferenceType = TypeFactory.createDefaultInstance().constructReferenceType(rawType, expectedReferredType);

        final AnnotatedType actualUnwrappedType = ReferenceTypeUtils.unwrapReference(new AnnotatedType(atomicReferenceType));

       assertEquals(actualUnwrappedType.getType(), expectedReferredType, rawType.getCanonicalName() + "Reference type not correctly unwrapped");
    }

    @Test(description = "AtomicReference should be unwrapped when read from Java bean")
    public void testUnwrapWithAtomicReferenceMemberFromJavaBean() throws Exception {
        final JavaType expectedReferredType = TypeFactory.createDefaultInstance().constructType(BigDecimal.class);

        final Type genericType = TypeWithAtomicReferenceMember.class.getDeclaredField("member").getGenericType();
        final AnnotatedType actualUnwrappedType = ReferenceTypeUtils.unwrapReference(new AnnotatedType(genericType));

       assertEquals(actualUnwrappedType.getType(), expectedReferredType, genericType.getTypeName() + "Reference type not correctly unwrapped");
    }

    @SuppressWarnings("unused")
    private static final class TypeWithAtomicReferenceMember {
        AtomicReference<BigDecimal> member;

        public AtomicReference<BigDecimal> getMember() {
            return member;
        }

        public void setMember(AtomicReference<BigDecimal> member) {
            this.member = member;
        }
    }

}
