package io.swagger.servlet.extensions;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReaderExtensionsTest {

    @Test
    public void getExtensionsTest() {
        final List<ReaderExtension> extensions = ReaderExtensions.getExtensions();

        Assert.assertEquals(extensions.size(), 3);

        final List<Integer> originalPriority = new ArrayList<Integer>();
        for (ReaderExtension extension : extensions) {
            originalPriority.add(extension.getPriority());
        }

        Assert.assertEquals(originalPriority, Arrays.asList(0, 10, 20));
    }
}
