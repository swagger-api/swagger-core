package io.swagger.models.utils;

import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.Xml;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.RefProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertyModelConverter {

    public Property modelToProperty(Model model){

        if(model instanceof ModelImpl) {
            ModelImpl m = (ModelImpl) model;
            if (m.getAdditionalProperties() != null){
                MapProperty mapProperty = new MapProperty();
                mapProperty.setType(m.getType());
                mapProperty.setAllowEmptyValue(m.getAllowEmptyValue());
                mapProperty.setDefault((String) m.getDefaultValue());
                mapProperty.setDescription(m.getDescription());
                mapProperty.setExample(m.getExample());
                mapProperty.setFormat(m.getFormat());
                mapProperty.setName(m.getName());
                mapProperty.setTitle(m.getTitle());
                List<String> required = m.getRequired();
                if (required != null) {
                    for (String name : required) {
                        if (name.equals(m.getName())) {
                            mapProperty.setRequired(true);
                        }
                    }
                }
                mapProperty.setXml(m.getXml());
                mapProperty.setVendorExtensions(m.getVendorExtensions());
                mapProperty.setAdditionalProperties(m.getAdditionalProperties());
                return mapProperty;
            }

            Property property = propertyByType(m);

            if(property instanceof ObjectProperty){
                ObjectProperty objectProperty = (ObjectProperty) property;
                objectProperty.setProperties(model.getProperties());
                objectProperty.setExample(model.getExample());
                return objectProperty;
            }

            return property;

        } else if(model instanceof ArrayModel) {
            ArrayModel m = (ArrayModel) model;
            ArrayProperty property = new ArrayProperty();
            Property inner = m.getItems();
            property.setItems(inner);
            property.setExample(m.getExample());
            property.setMaxItems(m.getMaxItems());
            property.setMinItems(m.getMinItems());
            property.setDescription(m.getDescription());
            property.setTitle(m.getTitle());
            property.setUniqueItems(m.getUniqueItems());
            return property;

        } else if(model instanceof RefModel) {
            RefModel ref = (RefModel) model;
            RefProperty refProperty = new RefProperty(ref.get$ref());
            return refProperty;

        } else if(model instanceof ComposedModel) {
            ObjectProperty objectProperty = new ObjectProperty();
            objectProperty.setDescription(model.getDescription());
            objectProperty.setTitle(model.getTitle());
            objectProperty.setExample(model.getExample());
            ComposedModel cm = (ComposedModel) model;
            Set<String> requiredProperties = new HashSet<>();
            for(Model item : cm.getAllOf()) {
                Property itemProperty = modelToProperty(item);
                if(itemProperty instanceof RefProperty) {
                    RefProperty refProperty = (RefProperty) itemProperty;
                    objectProperty.property(refProperty.getSimpleRef(), itemProperty);

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

    private Property propertyByType(ModelImpl model) {
        return PropertyBuilder.build(model.getType(), model.getFormat(), argsFromModel(model));
    }

    private Map<PropertyBuilder.PropertyId, Object> argsFromModel(ModelImpl model) {
        if (model == null) return Collections.emptyMap();
        final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<>(PropertyBuilder.PropertyId.class);
        args.put(PropertyBuilder.PropertyId.DESCRIPTION, model.getDescription());
        args.put(PropertyBuilder.PropertyId.EXAMPLE, model.getExample());
        args.put(PropertyBuilder.PropertyId.ENUM, model.getEnum());
        args.put(PropertyBuilder.PropertyId.TITLE, model.getTitle());
        args.put(PropertyBuilder.PropertyId.DEFAULT, model.getDefaultValue());
        args.put(PropertyBuilder.PropertyId.DESCRIMINATOR, model.getDiscriminator());
        args.put(PropertyBuilder.PropertyId.MINIMUM, model.getMinimum());
        args.put(PropertyBuilder.PropertyId.MAXIMUM, model.getMaximum());
        args.put(PropertyBuilder.PropertyId.UNIQUE_ITEMS, model.getUniqueItems());
        args.put(PropertyBuilder.PropertyId.VENDOR_EXTENSIONS, model.getVendorExtensions());
        return args;
    }




    public Model propertyToModel(Property property){

        String description = property.getDescription();
        String type = property.getType();
        String format = property.getFormat();
        String example = null;

        /*Object obj = property.getExample();
        if (obj != null) {
            example = obj.toString();
        }*/

        Boolean allowEmptyValue = property.getAllowEmptyValue();

        if(property instanceof RefProperty){
            RefProperty ref = (RefProperty) property;
            RefModel refModel = new RefModel(ref.get$ref());
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
            arrayModel.setUniqueItems(arrayProperty.getUniqueItems());

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
        model.setExample(property.getExample());//example
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
