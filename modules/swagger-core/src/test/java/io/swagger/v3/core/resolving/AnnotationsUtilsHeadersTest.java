package io.swagger.v3.core.resolving;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.headers.Header.StyleEnum;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class AnnotationsUtilsHeadersTest {

    private io.swagger.v3.oas.models.headers.Header header() {
        return new io.swagger.v3.oas.models.headers.Header().style(StyleEnum.SIMPLE);
    }

    @DataProvider
    private Object[][] expectedData() {
        return new Object[][]{
                {"methodOne", Optional.empty()},
                {"methodTwo", Optional.empty()},
                {"methodThree", Optional.empty()},
                {"methodFour", Optional.of(ImmutableMap.of(
                        "", header().description("header"),
                        "header1", header(),
                        "header2", header().description("header 2"),
                        "header3", header().$ref("#/components/schemas/header3")
                ))},
        };
    }

    @Test(dataProvider = "expectedData")
    public void extensionsTest(String methodName,
                               Optional<Map<String, io.swagger.v3.oas.models.headers.Header>> expected)
            throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod(methodName);
        final Header[] headers =
                Arrays.stream(method.getAnnotation(Operation.class).responses())
                        .flatMap(response -> Arrays.stream(response.headers())).toArray(Header[]::new);

        final Optional<Map<String, io.swagger.v3.oas.models.headers.Header>> optionalMap =
                AnnotationsUtils.getHeaders(headers, null, null);

        Assert.assertEquals(optionalMap, expected);
    }

    @Operation(description = "method")
    private void methodOne() {

    }

    @Operation(description = "method", responses = {
            @ApiResponse()
    })
    private void methodTwo() {

    }

    @Operation(description = "method", responses = {
            @ApiResponse(headers = {
                    @Header(name = "")
            })
    })
    private void methodThree() {

    }

    @Operation(description = "method", responses = {
            @ApiResponse(headers = {
                    @Header(name = "", description = "header"),
                    @Header(name = "header1"),
                    @Header(name = "header2", description = "header 2"),
                    @Header(name = "header3", ref = "#/components/schemas/header3")
            })
    })
    private void methodFour() {

    }
}