package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.resources.ResourceWithFormData;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class FormDataBodyPartTest {

    @Test(description = "FormDataBodyPart should be ignored when generating the Swagger document")
    public void testFormDataBodyPart() {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithFormData.class);
        final List<Parameter> parameters = swagger.getPath("/test/document/{documentName}.json").getPost().getParameters();
        assertEquals(parameters.size(), 3);
        assertEquals(parameters.get(0).getName(), "documentName");
        assertEquals(parameters.get(1).getName(), "input");
        assertEquals(parameters.get(2).getName(), "id");
    }
}
