package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import org.testng.annotations.Test;

public class Ticket3900Test extends SwaggerTestBase {

    @Test
    public void testArraySchemaItemsValidation() {
        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema model = context.resolve(new AnnotatedType(Route.class));
        SerializationMatchers.assertEqualsToYaml31(model, "type: object\n" +
                "properties:\n" +
                "  startPoint:\n" +
                "    $ref: '#/components/schemas/GeoPoint'\n" +
                "    description: Point where the route begins\n" +
                "  intermediatePoint:\n" +
                "    $ref: '#/components/schemas/GeoPoint'\n" +
                "    description: Intermediate point of the route\n" +
                "  endPoint:\n" +
                "    $ref: '#/components/schemas/GeoPoint'\n" +
                "    description: Point where the route ends");
    }

    private static class GeoPoint {

        @io.swagger.v3.oas.annotations.media.Schema(description = "Longitude of geo point ( -180, 180 >")
        public double lon;

        @io.swagger.v3.oas.annotations.media.Schema(description = "Latitude of geo point < -90, 90 >")
        public double lat;
    }

    private static class Route {
        @io.swagger.v3.oas.annotations.media.Schema(description = "Point where the route begins")
        public GeoPoint startPoint;

        @io.swagger.v3.oas.annotations.media.Schema(description = "Intermediate point of the route")
        public GeoPoint intermediatePoint;

        @io.swagger.v3.oas.annotations.media.Schema(description = "Point where the route ends")
        public GeoPoint endPoint;
    }
}
