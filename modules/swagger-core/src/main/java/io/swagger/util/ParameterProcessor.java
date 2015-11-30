package io.swagger.util;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;

public class ParameterProcessor {
    static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);

    public static Parameter applyAnnotations(Swagger swagger, Parameter parameter, Type type, List<Annotation> annotations) {
        final AnnotationsHelper helper = new AnnotationsHelper(annotations);
        if (helper.isContext()) {
            return null;
        }
        final ParamWrapper<?> param = helper.getApiParam();
        if (param.isHidden()) {
            return null;
        }
        final String defaultValue = helper.getDefaultValue();
        if (parameter instanceof AbstractSerializableParameter) {
            final AbstractSerializableParameter<?> p = (AbstractSerializableParameter<?>) parameter;

            if (param.isRequired()) {
                p.setRequired(true);
            }
            if (StringUtils.isNotEmpty(param.getName())) {
                p.setName(param.getName());
            }
            if (StringUtils.isNotEmpty(param.getDescription())) {
                p.setDescription(param.getDescription());
            }
            if (StringUtils.isNotEmpty(param.getExample())) {
                p.setExample(param.getExample());
            }
            if (StringUtils.isNotEmpty(param.getAccess())) {
                p.setAccess(param.getAccess());
            }
            if (StringUtils.isNotEmpty(param.getDataType())) {
                if("java.io.File".equalsIgnoreCase(param.getDataType())) {
                    p.setProperty(new FileProperty());
                }
                else {
                    p.setType(param.getDataType());
                }
            }
            if (helper.getMinItems() != null) {
                p.setMinItems(helper.getMinItems());
            }
            if (helper.getMaxItems() != null) {
                p.setMaxItems(helper.getMaxItems());
            }

            AllowableValues allowableValues = AllowableValuesUtils.create(param.getAllowableValues());

            if (p.getItems() != null || param.isAllowMultiple()) {
                if (p.getItems() == null) {
                    // Convert to array
                    final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
                    args.put(PropertyBuilder.PropertyId.DEFAULT, p.getDefaultValue());
                    p.setDefaultValue(null);
                    args.put(PropertyBuilder.PropertyId.ENUM, p.getEnum());
                    p.setEnum(null);
                    args.put(PropertyBuilder.PropertyId.MINIMUM, p.getMinimum());
                    p.setMinimum(null);
                    args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, p.isExclusiveMinimum());
                    p.setExclusiveMinimum(null);
                    args.put(PropertyBuilder.PropertyId.MAXIMUM, p.getMaximum());
                    p.setMaximum(null);
                    args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, p.isExclusiveMaximum());
                    args.put(PropertyBuilder.PropertyId.EXAMPLE, p.getExample());
                    p.setExclusiveMaximum(null);
                    Property items = PropertyBuilder.build(p.getType(), p.getFormat(), args);
                    p.type(ArrayProperty.TYPE).format(null).items(items);
                }

                final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
                if (StringUtils.isNotEmpty(defaultValue)) {
                    args.put(PropertyBuilder.PropertyId.DEFAULT, defaultValue);
                }
                if (allowableValues != null) {
                    args.putAll(allowableValues.asPropertyArguments());
                }
                PropertyBuilder.merge(p.getItems(), args);
            } else {
                if (StringUtils.isNotEmpty(defaultValue)) {
                    p.setDefaultValue(defaultValue);
                }
                processAllowedValues(allowableValues, p);
            }
        } else {
            // must be a body param
            BodyParameter bp = new BodyParameter();

            if (helper.getApiParam() != null) {
                ParamWrapper<?> pw = helper.getApiParam();

                if (pw instanceof ApiParamWrapper) {
                    ApiParamWrapper apiParam = (ApiParamWrapper) pw;
                    Example example = apiParam.getExamples();
                    if (example != null && example.value() != null) {
                        for (ExampleProperty ex : example.value()) {
                            String mediaType = ex.mediaType();
                            String value = ex.value();
                            if (!mediaType.isEmpty() && !value.isEmpty()) {
                                bp.example(mediaType.trim(), value.trim());
                            }
                        }
                    }
                } else if (pw instanceof ApiImplicitParamWrapper) {
                    ApiImplicitParamWrapper apiParam = (ApiImplicitParamWrapper) pw;
                    Example example = apiParam.getExamples();
                    if (example != null && example.value() != null) {
                        for (ExampleProperty ex : example.value()) {
                            String mediaType = ex.mediaType();
                            String value = ex.value();
                            if (!mediaType.isEmpty() && !value.isEmpty()) {
                                bp.example(mediaType.trim(), value.trim());
                            }
                        }
                    }
                }
            }
            bp.setRequired(param.isRequired());
            bp.setName(StringUtils.isNotEmpty(param.getName()) ? param.getName() : "body");

            if (StringUtils.isNotEmpty(param.getDescription())) {
                bp.setDescription(param.getDescription());
            }

            if (StringUtils.isNotEmpty(param.getAccess())) {
                bp.setAccess(param.getAccess());
            }

            final Property property = ModelConverters.getInstance().readAsProperty(type);
            if (property != null) {
                final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
                if (StringUtils.isNotEmpty(defaultValue)) {
                    args.put(PropertyBuilder.PropertyId.DEFAULT, defaultValue);
                }
                bp.setSchema(PropertyBuilder.toModel(PropertyBuilder.merge(property, args)));
                for (Map.Entry<String, Model> entry : ModelConverters.getInstance().readAll(type).entrySet()) {
                    swagger.addDefinition(entry.getKey(), entry.getValue());
                }
            }
            parameter = bp;
        }
        return parameter;
    }

    private static void processAllowedValues(AllowableValues allowableValues, AbstractSerializableParameter<?> p) {
        if (allowableValues == null) {
            return;
        }
        Map<PropertyBuilder.PropertyId, Object> args = allowableValues.asPropertyArguments();
        if (args.containsKey(PropertyBuilder.PropertyId.ENUM)) {
            p.setEnum((List<String>) args.get(PropertyBuilder.PropertyId.ENUM));
        } else {
            if (args.containsKey(PropertyBuilder.PropertyId.MINIMUM)) {
                p.setMinimum((Double) args.get(PropertyBuilder.PropertyId.MINIMUM));
            }
            if (args.containsKey(PropertyBuilder.PropertyId.MAXIMUM)) {
                p.setMaximum((Double) args.get(PropertyBuilder.PropertyId.MAXIMUM));
            }
            if (args.containsKey(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM)) {
                p.setExclusiveMinimum((Boolean) args.get(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM) ? true : null);
            }
            if (args.containsKey(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM)) {
                p.setExclusiveMaximum((Boolean) args.get(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM) ? true : null);
            }
        }
    }

    /**
     * Wraps either an @ApiParam or and @ApiImplicitParam
     */

    public interface ParamWrapper<T extends Annotation> {
        String getName();

        String getDescription();

        String getDefaultValue();

        String getAllowableValues();

        boolean isRequired();

        String getAccess();

        boolean isAllowMultiple();

        String getDataType();

        String getParamType();

        T getAnnotation();

        boolean isHidden();

        String getExample();
    }

    /**
     * The <code>AnnotationsHelper</code> class defines helper methods for
     * accessing supported parameter annotations.
     */
    private static class AnnotationsHelper {
        private static final ApiParam DEFAULT_API_PARAM = getDefaultApiParam(null);
        private boolean context;
        private ParamWrapper<?> apiParam = new ApiParamWrapper(DEFAULT_API_PARAM);
        private String defaultValue;
        private Integer minItems;
        private Integer maxItems;

        /**
         * Constructs an instance.
         *
         * @param annotations array or parameter annotations
         */
        public AnnotationsHelper(List<Annotation> annotations) {
            String rsDefault = null;
            for (Annotation item : annotations) {
                if ("javax.ws.rs.core.Context".equals(item.annotationType().getName())) {
                    context = true;
                } else if (item instanceof ApiParam) {
                    apiParam = new ApiParamWrapper((ApiParam) item);
                } else if (item instanceof ApiImplicitParam) {
                    apiParam = new ApiImplicitParamWrapper((ApiImplicitParam) item);
                } else if ("javax.ws.rs.DefaultValue".equals(item.annotationType().getName())) {
                    try {
                        rsDefault = (String) item.getClass().getMethod("value").invoke(item);
                    } catch (Exception ex) {
                        LOGGER.error("Invocation of value method failed", ex);
                    }
                } else if (item instanceof Size) {
                    final Size size = (Size) item;
                    minItems = size.min();
                    maxItems = size.max();
                }
            }
            defaultValue = StringUtils.isNotEmpty(apiParam.getDefaultValue()) ? apiParam.getDefaultValue() : rsDefault;
        }

        /**
         * Returns a default @{@link ApiParam} annotation for parameters without it.
         *
         * @param annotationHolder a placeholder for default @{@link ApiParam}
         *                         annotation
         * @return @{@link ApiParam} annotation
         */
        private static ApiParam getDefaultApiParam(@ApiParam String annotationHolder) {
            for (Method method : AnnotationsHelper.class.getDeclaredMethods()) {
                if ("getDefaultApiParam".equals(method.getName())) {
                    return (ApiParam) method.getParameterAnnotations()[0][0];
                }
            }
            throw new IllegalStateException("Failed to locate default @ApiParam");
        }

        /**
         * Checks whether the @{@link Context} annotation is present.
         *
         * @return <code>true<code> if parameter is defined with the @{@link Context} annotation
         */
        public boolean isContext() {
            return context;
        }

        /**
         * Returns @{@link ApiParam} annotation. If no @{@link ApiParam} is present
         * a default one will be returned.
         *
         * @return @{@link ApiParam} annotation
         */
        public ParamWrapper<?> getApiParam() {
            return apiParam;
        }

        /**
         * Returns default value from annotation.
         *
         * @return default value from annotation
         */
        public String getDefaultValue() {
            return defaultValue;
        }

        public Integer getMinItems() {
            return minItems;
        }

        public Integer getMaxItems() {
            return maxItems;
        }
    }

    /**
     * Wrapper implementation for ApiParam annotation
     */

    private final static class ApiParamWrapper implements ParamWrapper<ApiParam> {

        private final ApiParam apiParam;

        private ApiParamWrapper(ApiParam apiParam) {
            this.apiParam = apiParam;
        }

        @Override
        public String getName() {
            return apiParam.name();
        }

        @Override
        public String getDescription() {
            return apiParam.value();
        }

        @Override
        public String getDefaultValue() {
            return apiParam.defaultValue();
        }

        @Override
        public String getAllowableValues() {
            return apiParam.allowableValues();
        }

        @Override
        public boolean isRequired() {
            return apiParam.required();
        }

        @Override
        public String getAccess() {
            return apiParam.access();
        }

        @Override
        public boolean isAllowMultiple() {
            return apiParam.allowMultiple();
        }

        @Override
        public String getDataType() {
            return null;
        }

        @Override
        public String getParamType() {
            return null;
        }

        @Override
        public ApiParam getAnnotation() {
            return apiParam;
        }

        @Override
        public boolean isHidden() {
            return apiParam.hidden();
        }

        @Override
        public String getExample() {
            return apiParam.example();
        }

        ;

        public Example getExamples() {
            return apiParam.examples();
        }

        ;
    }

    /**
     * Wrapper implementation for ApiImplicitParam annotation
     */
    private final static class ApiImplicitParamWrapper implements ParamWrapper<ApiImplicitParam> {

        private final ApiImplicitParam apiParam;

        private ApiImplicitParamWrapper(ApiImplicitParam apiParam) {
            this.apiParam = apiParam;
        }

        @Override
        public String getName() {
            return apiParam.name();
        }

        @Override
        public String getDescription() {
            return apiParam.value();
        }

        @Override
        public String getDefaultValue() {
            return apiParam.defaultValue();
        }

        @Override
        public String getAllowableValues() {
            return apiParam.allowableValues();
        }

        @Override
        public boolean isRequired() {
            return apiParam.required();
        }

        @Override
        public String getAccess() {
            return apiParam.access();
        }

        @Override
        public boolean isAllowMultiple() {
            return apiParam.allowMultiple();
        }

        @Override
        public String getDataType() {
            return apiParam.dataType();
        }

        @Override
        public String getParamType() {
            return apiParam.paramType();
        }

        @Override
        public ApiImplicitParam getAnnotation() {
            return apiParam;
        }

        @Override
        public boolean isHidden() {
            return false;
        }

        @Override
        public String getExample() {
            return apiParam.example();
        }

        public Example getExamples() {
            return apiParam.examples();
        }
    }
}
