package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

public class ModelConvertersConcurrencyTest {

    @AfterMethod(alwaysRun = true)
    public void resetSingletons() {
        ModelConverters.reset();
    }

    @Test
    public void getInstancePublishesOneFullyInitializedInstance() throws Exception {
        for (boolean openapi31 : new boolean[]{false, true}) {
            for (int round = 0; round < 20; round++) {
                ModelConverters.reset();
                List<ModelConverters> instances = callConcurrently(16, () -> ModelConverters.getInstance(openapi31));
                for (ModelConverters instance : instances) {
                    assertSame(instance, instances.get(0));
                    assertTrue(instance.getSkippedPackages().contains("java.lang"), "instance published before init");
                }
            }
        }
    }

    private static List<ModelConverters> callConcurrently(int threads, java.util.concurrent.Callable<ModelConverters> task) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            CountDownLatch start = new CountDownLatch(1);
            List<Future<ModelConverters>> futures = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                futures.add(executor.submit(() -> {
                    start.await();
                    return task.call();
                }));
            }
            start.countDown();
            List<ModelConverters> instances = new ArrayList<>();
            for (Future<ModelConverters> future : futures) {
                instances.add(future.get());
            }
            return instances;
        } finally {
            executor.shutdownNow();
        }
    }
}
