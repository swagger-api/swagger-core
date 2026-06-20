package io.swagger.v3.java17.resolving;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import tools.jackson.databind.json.JsonMapper;

public abstract class SwaggerTestBase {
    static ObjectMapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerTestBase.class);

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = JsonMapper.builder()
                    .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
                    .changeDefaultPropertyInclusion(incl -> incl
                            .withContentInclusion(JsonInclude.Include.NON_NULL)
                            .withValueInclusion(JsonInclude.Include.NON_NULL))
                    .accessorNaming(new DefaultAccessorNamingStrategy.Provider()
                            .withFirstCharAcceptance(true, true))
                    .build();
        }
        return mapper;
    }

    protected ModelResolver modelResolver() {
        return new ModelResolver(new ObjectMapper());
    }

    protected void prettyPrint(Object o) {
        try {
            LOGGER.debug(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(o));
        } catch (Exception e) {
            LOGGER.error("Failed to pretty print object", e);
        }
    }
}
