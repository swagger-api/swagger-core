package io.swagger.util;

import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.Xml;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

import java.lang.ref.Reference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertyModelConverter {

    public Property modelToProperty(Model model){

        if(model instanceof ModelImpl) {
            ModelImpl m = (ModelImpl) model;
            ObjectProperty property = new ObjectProperty();
            property.setProperties(m.getProperties());
            property.setName(m.getName());
            property.setFormat(m.getFormat());
            if(m.getDefaultValue() != null) {
                property.setDefault(m.getDefaultValue().toString());
            }
            property.setDescription(m.getDescription());
            property.setTitle(m.getTitle());
            property.setXml(m.getXml());

            if(m.getExample() != null) {
                property.setExample(m.getExample().toString());
            }

            return property;
        }
        if(model instanceof ArrayModel) {
            ArrayModel m = (ArrayModel) model;
            ArrayProperty property = new ArrayProperty();
            Property inner = m.getItems();
            property.setItems(inner);
            property.setDescription(m.getDescription());
            property.setTitle(m.getTitle());

            return property;
        }
        if(model instanceof RefModel) {
            RefModel ref = (RefModel) model;
            RefProperty refProperty = new RefProperty(ref.getSimpleRef());
            refProperty.setName(ref.getSimpleRef());
            return refProperty;
        }
        if(model instanceof ComposedModel) {
            ObjectProperty objectProperty = new ObjectProperty();
            objectProperty.setDescription(model.getDescription());
            objectProperty.setTitle(model.getTitle());
            objectProperty.setExample(model.getExample());
            ComposedModel cm = (ComposedModel) model;
            Set<String> requiredProperties = new HashSet<>();
            for(Model item : cm.getAllOf()) {
                Property itemProperty = modelToProperty(item);
                if(itemProperty instanceof RefProperty) {
                    objectProperty.property(itemProperty.getName(), itemProperty);

                }else if(itemProperty instanceof ObjectProperty) {
                    ObjectProperty itemPropertyObject = (ObjectProperty) itemProperty;
                    if(itemPropertyObject.getProperties() != null) {
                        for (String key : itemPropertyObject.getProperties().keySet()) {
                            objectProperty.property(key, itemPropertyObject.getProperties().get(key));
                        }
                    }
                    if(itemPropertyObject.getRequiredProperties() != null) {
                        for(String req : itemPropertyObject.getRequiredProperties()) {
                            requiredProperties.add(req);
                        }
                    }
                }
            }
            if(requiredProperties.size() > 0) {
                objectProperty.setRequiredProperties(new ArrayList(requiredProperties));
            }
            if(cm.getVendorExtensions() != null) {
                for(String key : cm.getVendorExtensions().keySet()) {
                    objectProperty.vendorExtension(key, cm.getVendorExtensions().get(key));
                }
            }
            return objectProperty;
        }
        return null;
    }

    public Model propertyToModel(Property property){

        String description = property.getDescription();
        String type = property.getType();
        String format = property.getFormat();
        String example = null;

        Object obj = property.getExample();
        if (obj != null) {
            example = obj.toString();
        }

        Boolean allowEmptyValue = property.getAllowEmptyValue();

        //List<String> required = property.getRequired();

        //Boolean uniqueItems = property.getUniqueItems(); ArrayProperty not in ArrayModel but yes in ModelImpl

        //boolean isSimple = property.getSimple();

        //String discriminator = property.getDiscriminator();

        //String defaultValue = property.getDefaultValue(); UUIDProperty StringProperty BinaryProperty
        // BooleanProperty(Boolean) - all numberProperties has a number default value

        //List<String> _enum = property.getEnum();
        // UUIDProperty StringProperty BinaryProperty DateProperty DateTimeProperty
        // BooleanProperty(Boolean) DoubleProperty(Double) - all numberProperties has a number enum

        //BigDecimal minimum = property.getMinimum; AbstractNumericProperty

        //BigDecimal maximum = property.getMaximum; AbstractNumericProperty

        //ExternalDocs externalDocs = property.getExternalDocs();



        if(property instanceof RefProperty){
            String reference = ((RefProperty) property).get$ref();
            RefModel refModel = new RefModel(reference);
            return refModel;
        }

        Map<String, Object> extensions = property.getVendorExtensions();

        Property additionalProperties = null;
        if (property instanceof MapProperty) {
             additionalProperties = ((MapProperty) property).getAdditionalProperties();
        }

        String name = property.getName();
        Xml xml = property.getXml();

        Map<String, Property> properties = null;

        if (property instanceof ObjectProperty) {
            ObjectProperty objectProperty = (ObjectProperty) property;
            properties = objectProperty.getProperties();
        }

        if (property instanceof ArrayProperty){
            ArrayProperty arrayProperty = (ArrayProperty) property;
            ArrayModel arrayModel = new ArrayModel();
            arrayModel.setItems(arrayProperty.getItems());
            arrayModel.setDescription(description);
            arrayModel.setExample(example);
            if(extensions != null) {
                arrayModel.setVendorExtensions(extensions);
            }

            if (properties != null) {
                arrayModel.setProperties(properties);
            }

            return arrayModel;
        }

        ModelImpl model = new ModelImpl();

        model.setDescription(description);
        model.setExample(example);
        model.setName(name);
        model.setXml(xml);
        model.setType(type);
        model.setFormat(format);
        model.setAllowEmptyValue(allowEmptyValue);

        if(extensions != null) {
            model.setVendorExtensions(extensions);
        }

        if(additionalProperties != null) {
            model.setAdditionalProperties(additionalProperties);
        }

        if (properties != null) {
            model.setProperties(properties);
        }


        return model;
    }
}
