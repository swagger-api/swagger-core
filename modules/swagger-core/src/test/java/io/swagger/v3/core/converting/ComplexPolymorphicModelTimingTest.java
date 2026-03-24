package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.ModelWithArrayOfSubclasses;
import io.swagger.v3.core.oas.models.ModelWithManySubtypesAndRecursion;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ComplexPolymorphicModelTimingTest {
    @Test
    public void complexPolymorphicModelTimingTest() {
        final long durationComplexModel =
                measureTiming(1, () -> ModelConverters.getInstance(true).readAllAsResolvedSchema(ModelWithManySubtypesAndRecursion.Holder.class));
        System.out.println("Complex model duration: " + durationComplexModel + "ms");
        final long durationSimpleModel =
                measureTiming(10, () -> ModelConverters.getInstance(true).readAllAsResolvedSchema(ModelWithArrayOfSubclasses.Holder.class));
        System.out.println("Simple model duration: " + durationSimpleModel + "ms");

        // The complex model shouldn't take 5000 times longer
        final float factor = (float) durationComplexModel / durationSimpleModel;
        System.out.println("Factor: " + factor);
        assertTrue(factor <= 5000);
    }

    private long measureTiming(int iterations, Runnable runnable) {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            runnable.run();
        }
        final long end = System.currentTimeMillis();
        return (end - start) / iterations;
    }
}
