package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.ModelWithArrayOfSubclasses;
import io.swagger.v3.core.oas.models.ModelWithManySubtypesAndRecursion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ComplexPolymorphicModelTimingTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComplexPolymorphicModelTimingTest.class);

    @Test
    public void complexPolymorphicModelTimingTest() {
        final long durationComplexModel =
                measureTiming(1, () -> ModelConverters.getInstance(true).readAllAsResolvedSchema(ModelWithManySubtypesAndRecursion.Holder.class));
        LOGGER.debug("Complex model duration: " + durationComplexModel + "ms");
        final long durationSimpleModel =
                measureTiming(10, () -> ModelConverters.getInstance(true).readAllAsResolvedSchema(ModelWithArrayOfSubclasses.Holder.class));
        LOGGER.debug("Simple model duration: " + durationSimpleModel + "ms");

        // The complex model shouldn't take 1000 times longer
        final float factor = (float) durationComplexModel / durationSimpleModel;
        LOGGER.debug("Factor: " + factor);
        assertTrue(factor <= 1000);
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
