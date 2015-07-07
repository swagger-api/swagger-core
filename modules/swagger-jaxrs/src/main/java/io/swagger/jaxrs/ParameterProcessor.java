package io.swagger.jaxrs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ParameterProcessor {
    private static final Table<Class<? extends AllowableValues>, Boolean, AbstractAllowableValuesProcessor<?, ?>> ALLOWED_VALUES_PROCESSORS =
            HashBasedTable.create();
    static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);

    public static Parameter applyAnnotations(Swagger swagger, Parameter parameter, Type type, List<Annotation> annotations) {
        final AnnotationsHelper helper = new AnnotationsHelper(annotations);
        if (helper.isContext()) {
            return null;
        }
        final ParamWrapper<?> param = helper.getApiParam();
        final String defaultValue = helper.getDefaultValue();
        final JavaType javaType = TypeFactory.defaultInstance().constructType(type);
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
            if (StringUtils.isNotEmpty(param.getAccess())) {
                p.setAccess(param.getAccess());
            }
            if (StringUtils.isNotEmpty(param.getDataType())) {
                p.setType(param.getDataType());
            }

            AllowableValues allowableValues = null;
            if (StringUtils.isNotEmpty(param.getAllowableValues())) {
                allowableValues = AllowableRangeValues.create(param.getAllowableValues());
                if (allowableValues == null) {
                    allowableValues = AllowableEnumValues.create(param.getAllowableValues());
                }
            }

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
                    p.setExclusiveMaximum(null);
                    Property items = PropertyBuilder.build(p.getType(), p.getFormat(), args);
                    p.type(ArrayProperty.TYPE).format(null).items(items);
                }

                final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
                if (StringUtils.isNotEmpty(defaultValue)) {
                    args.put(PropertyBuilder.PropertyId.DEFAULT, defaultValue);
                }
                processAllowedValues(allowableValues, true, args);
                PropertyBuilder.merge(p.getItems(), args);
                p.collectionFormat("csv");
            } else {
                if (StringUtils.isNotEmpty(defaultValue)) {
                    p.setDefaultValue(defaultValue);
                }
                processAllowedValues(allowableValues, false, p);
            }
        } else {
            // must be a body param
            BodyParameter bp = new BodyParameter();

            bp.setRequired(param.isRequired());
            bp.setName(StringUtils.isNotEmpty(param.getName()) ? param.getName() : "body");

            if (StringUtils.isNotEmpty(param.getDescription())) {
                bp.setDescription(param.getDescription());
            }

            if (StringUtils.isNotEmpty(param.getAccess())) {
                bp.setAccess(param.getAccess());
            }

            final Property property = ModelConverters.getInstance().readAsProperty(javaType);
            if (property != null) {
                final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
                if (StringUtils.isNotEmpty(defaultValue)) {
                    args.put(PropertyBuilder.PropertyId.DEFAULT, defaultValue);
                }
                bp.setSchema(PropertyBuilder.toModel(PropertyBuilder.merge(property, args)));
                for (Map.Entry<String, Model> entry : ModelConverters.getInstance().readAll(javaType).entrySet()) {
                    swagger.addDefinition(entry.getKey(), entry.getValue());
                }
            }
            parameter = bp;
        }
        return parameter;
    }

    private static <C> void processAllowedValues(AllowableValues values, boolean key, C container) {
        if (values == null) {
            return;
        }
        @SuppressWarnings("unchecked")
        final AbstractAllowableValuesProcessor<C, AllowableValues> processor =
                (AbstractAllowableValuesProcessor<C, AllowableValues>) ALLOWED_VALUES_PROCESSORS.get(values.getClass(), key);
        processor.process(container, values);
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

        /**
         * Constructs an instance.
         *
         * @param annotations array or parameter annotations
         */
        public AnnotationsHelper(List<Annotation> annotations) {
            String rsDefault = null;
            for (Annotation item : annotations) {
                if (item instanceof Context) {
                    context = true;
                } else if (item instanceof ApiParam) {
                    apiParam = new ApiParamWrapper((ApiParam) item);
                } else if (item instanceof ApiImplicitParam) {
                    apiParam = new ApiImplicitParamWrapper((ApiImplicitParam) item);
                } else if (item instanceof DefaultValue) {
                    rsDefault = ((DefaultValue) item).value();
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
    }

    static {
        ALLOWED_VALUES_PROCESSORS.put(AllowableRangeValues.class, true, new ArgumentsRangeProcessor());
        ALLOWED_VALUES_PROCESSORS.put(AllowableRangeValues.class, false, new ParameterRangeProcessor());
        ALLOWED_VALUES_PROCESSORS.put(AllowableEnumValues.class, true, new ArgumentsEnumProcessor());
        ALLOWED_VALUES_PROCESSORS.put(AllowableEnumValues.class, false, new ParameterEnumProcessor());
    }
}
