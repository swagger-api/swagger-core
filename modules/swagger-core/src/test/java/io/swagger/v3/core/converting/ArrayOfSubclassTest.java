package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.oas.models.ModelWithArrayOfSubclasses;
import io.swagger.v3.core.util.Json31;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;


public class ArrayOfSubclassTest {

    @Test
    public void extractSuclassArray() {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(ModelWithArrayOfSubclasses.Holder.class);
        assertNotNull(schema);
        System.out.println(Json31.pretty(schema));
    }

}
