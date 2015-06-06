package io.swagger.jaxrs;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DefaultParameterExtension extends AbstractSwaggerExtension {

    @Override
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<SwaggerExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ArrayList<Parameter>();
        }

        List<Parameter> parameters = new ArrayList<Parameter>();
        Parameter parameter = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) {
                QueryParam param = (QueryParam) annotation;
                QueryParameter qp = new QueryParameter()
                        .name(param.value());

                Property schema = createProperty(type);
                if (schema != null) {
                    qp.setProperty(schema);
                }
                parameter = qp;
            } else if (annotation instanceof PathParam) {
                PathParam param = (PathParam) annotation;
                PathParameter pp = new PathParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    pp.setProperty(schema);
                }
                parameter = pp;
            } else if (annotation instanceof HeaderParam) {
                HeaderParam param = (HeaderParam) annotation;
                HeaderParameter hp = new HeaderParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    hp.setProperty(schema);
                }
                parameter = hp;
            } else if (annotation instanceof CookieParam) {
                CookieParam param = (CookieParam) annotation;
                CookieParameter cp = new CookieParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    cp.setProperty(schema);
                }
                parameter = cp;
            } else if (annotation instanceof FormParam) {
                FormParam param = (FormParam) annotation;
                FormParameter fp = new FormParameter()
                        .name(param.value());
                Property schema = createProperty(type);
                if (schema != null) {
                    fp.setProperty(schema);
                }
                parameter = fp;
            }
        }
        if (parameter != null) {
            parameters.add(parameter);
        }

        return parameters;
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        return cls.getName().startsWith("javax.ws.rs.");
    }

    private Property createProperty(Type type) {
        return enforcePrimitive(ModelConverters.getInstance().readAsProperty(type), 0);
    }

    private Property enforcePrimitive(Property in, int level) {
        if (in instanceof RefProperty) {
            return new StringProperty();
        }
        if (in instanceof ArrayProperty) {
            if (level == 0) {
                final ArrayProperty array = (ArrayProperty) in;
                array.setItems(enforcePrimitive(array.getItems(), level + 1));
            } else {
                return new StringProperty();
            }
        }
        return in;
    }
}
