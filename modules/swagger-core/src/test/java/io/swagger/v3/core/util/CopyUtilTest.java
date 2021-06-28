package io.swagger.v3.core.util;

import static io.swagger.v3.core.util.CopyUtil.copy;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.testng.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CopyUtilTest {

    public final String path;
    public final boolean hasExamplesOrExtensions;

    public CopyUtilTest(String path, boolean hasExamplesOrExtensions) {
        this.path = path;
        this.hasExamplesOrExtensions = hasExamplesOrExtensions;
    }

    @Test
    public void testCopyOpenAPI() {
        OpenAPI oas = TestUtils.deserializeJsonFileFromClasspath(this.path, OpenAPI.class);
        OpenAPI copy = copy(oas);
        if (!hasExamplesOrExtensions) {
            assertEquals(copy, oas);
        } else {
            // If the spec has examples or extensions, equals() can't be used since it would compare Object instances
            assertEquals(Json.pretty(copy), Json.pretty(oas));
        }
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection paths() {
        return Arrays.asList(new Object[][] {
                {"specFiles/additionalpropsmodel.json", false},
                {"specFiles/brokenrefmodel.json", false},
                {"specFiles/compositionTest.json", false},
                {"specFiles/deprecatedoperationmodel.json", false},
                {"specFiles/noModels.json", false},
                {"specFiles/paramAndResponseRef.json", false},
                {"specFiles/paramAndResponseRefArray.json", false},
                {"specFiles/paramAndResponseRefComposed.json", false},
                {"specFiles/pathRef.json", false},
                {"specFiles/petstore.json", true},
                {"specFiles/petstore-3.0.json", false},
                {"specFiles/petstore-3.0-referred-schemas.json", false},
                {"specFiles/petstore-3.0-v2.json", false},
                {"specFiles/petstore-3.0-v2-ticket-3303.json", false},
                {"specFiles/petstore-3.0-v2_withoutModels.json", false},
                {"specFiles/propertiesWithConstraints.json", false},
                {"specFiles/propertyWithVendorExtensions.json", true},
                {"specFiles/recursivemodels.json", false},
                {"specFiles/responseRef.json", false},
                {"specFiles/sampleSpec.json", false},
                {"specFiles/securityDefinitions.json", false}
        });
    }
}
