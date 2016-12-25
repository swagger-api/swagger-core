package io.swagger.validator;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.EmailProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BeanValidator extends AbstractModelConverter implements ModelConverter {
    public BeanValidator() {
        super(Json.mapper());
    }

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
        Map<String, Annotation> annos = new HashMap<String, Annotation>();
        if (annotations != null) {
            for (Annotation anno : annotations) {
                annos.put(anno.annotationType().getName(), anno);
            }
        }
        Property property = null;

        if (chain.hasNext()) {
            property = chain.next().resolveProperty(type, context, annotations, chain);
        }
        if (property != null) {
            if (annos.containsKey("org.hibernate.validator.constraints.NotEmpty")) {
                property.setRequired(true);
                if (property instanceof StringProperty) {
                    ((StringProperty) property).minLength(1);
                } else if (property instanceof ArrayProperty){
                    ((ArrayProperty) property).setMinItems(1);
                }
            }
            if (annos.containsKey("org.hibernate.validator.constraints.NotBlank")) {
                property.setRequired(true);
                if (property instanceof StringProperty) {
                    ((StringProperty) property).minLength(1);
                }
            }
            if (annos.containsKey("org.hibernate.validator.constraints.Range")) {
                if (property instanceof AbstractNumericProperty) {
                    Range range = (Range) annos.get("org.hibernate.validator.constraints.Range");
                    AbstractNumericProperty ap = (AbstractNumericProperty) property;
                    ap.setMinimum(new BigDecimal(range.min()));
                    ap.setMaximum(new BigDecimal(range.max()));
                }
            }
            if (annos.containsKey("org.hibernate.validator.constraints.Length")) {
                if (property instanceof StringProperty) {
                    Length length = (Length) annos.get("org.hibernate.validator.constraints.Length");
                    StringProperty sp = (StringProperty) property;
                    sp.minLength(new Integer(length.min()));
                    sp.maxLength(new Integer(length.max()));
                }
            }
            if (annos.containsKey("org.hibernate.validator.constraints.Email")) {
                if (property instanceof StringProperty) {
                    EmailProperty sp = new EmailProperty((StringProperty) property);
                    property = sp;
                }
            }
            return property;
        }
        return super.resolveProperty(type, context, annotations, chain);
    }
}
