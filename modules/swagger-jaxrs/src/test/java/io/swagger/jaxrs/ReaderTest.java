package io.swagger.jaxrs;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReaderTest {

    private Reader reader;
    @Mock
    private Swagger swagger;
    @Mock
    private Map<String, Path> paths;
    @Mock
    private Path path;
    @Mock
    private Operation operation;

    public ReaderTest() {
        MockitoAnnotations.initMocks(this);
        reader = new Reader(this.swagger, null);
    }

    @Test(description = "tests to check if duplicated operation id are being fixed.")
    public void duplicateOperationIdFix() {
        final List<Path> mockedPaths = new ArrayList<Path>();
        mockedPaths.add(this.path);
        final List<Operation> mockedOperations = new ArrayList<Operation>();
        mockedOperations.add(this.operation);

        when(this.swagger.getPaths()).thenReturn(this.paths);

        when(this.paths.isEmpty()).thenReturn(false);
        when(this.paths.values()).thenReturn(mockedPaths);

        when(this.path.getOperations()).thenReturn(mockedOperations);
        when(this.operation.getOperationId())
                .thenReturn("fixDuplicated");

        Assert.assertEquals(this.reader.getOperationId("fixDuplicated"), "fixDuplicated_1");
        Assert.assertEquals(this.reader.getOperationId("newOne"), "newOne");

        when(this.operation.getOperationId())
                .thenReturn("fixDuplicated")
                .thenReturn("fixDuplicated_1")
                .thenReturn("fixDuplicated_2")
                .thenReturn("fixDuplicated_3")
                .thenReturn("fixDuplicated_4");

        Assert.assertEquals(this.reader.getOperationId("fixDuplicated"), "fixDuplicated_5");

        when(this.operation.getOperationId())
                .thenReturn("fixDuplicated")
                .thenReturn("fixDuplicated_1")
                .thenReturn("fixDuplicated_2")
                .thenReturn("fixDuplicated_3")
                .thenReturn("fixDuplicated_4")
                .thenReturn("fixDuplicated_5")
                .thenReturn("fixDuplicated_8");

        Assert.assertEquals(this.reader.getOperationId("fixDuplicated"), "fixDuplicated_6");
    }
}
