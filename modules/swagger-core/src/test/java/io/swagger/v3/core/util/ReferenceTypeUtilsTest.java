package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.converter.AnnotatedType;
import org.checkerframework.checker.units.qual.A;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Unit test cases for {@link ReferenceTypeUtils}
 */
public class ReferenceTypeUtilsTest {

    @Test(description = "AtomicReference should be reference type")
    public void testIsReferenceTypeWithAtomicReference() {
        final JavaType referredType = TypeFactory.defaultInstance().constructType(String.class);
        final Class<AtomicReference> rawType = AtomicReference.class;
        final JavaType atomicReferenceType = TypeFactory.defaultInstance().constructReferenceType(rawType, referredType);

        final boolean actualIsReferenceType = ReferenceTypeUtils._isReferenceType(atomicReferenceType);

        Assert.assertEquals(actualIsReferenceType, true, rawType.getCanonicalName() + " should be reference type but was not.");
    }

    @Test(description = "AtomicReference JavaType should be unwrapped")
    public void testUnwrapWithAtomicReferenceAndJavaType() {
        final JavaType expectedReferredType = TypeFactory.defaultInstance().constructType(String.class);

        final Class<AtomicReference> rawType = AtomicReference.class;
        final JavaType atomicReferenceType = TypeFactory.defaultInstance().constructReferenceType(rawType, expectedReferredType);

        final AnnotatedType actualUnwrappedType = ReferenceTypeUtils.unwrapReference(new AnnotatedType(atomicReferenceType));

        Assert.assertEquals(actualUnwrappedType.getType(), expectedReferredType, rawType.getCanonicalName() + "Reference type not correctly unwrapped");
    }

    @Test(description = "AtomicReference should be unwrapped when read from Java bean")
    public void testUnwrapWithAtomicReferenceMemberFromJavaBean() throws Exception {
        final JavaType expectedReferredType = TypeFactory.defaultInstance().constructType(BigDecimal.class);

        final Type genericType = TypeWithAtomicReferenceMember.class.getDeclaredField("member").getGenericType();
        final AnnotatedType actualUnwrappedType = ReferenceTypeUtils.unwrapReference(new AnnotatedType(genericType));

        Assert.assertEquals(actualUnwrappedType.getType(), expectedReferredType, genericType.getTypeName() + "Reference type not correctly unwrapped");
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
