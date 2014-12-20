package com.wordnik.swagger.jackson;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.*;

public class ModelResolver {
  protected final ObjectMapper _mapper;
  protected final AnnotationIntrospector _intr;

  protected TypeNameResolver _typeNameResolver = TypeNameResolver.std;
  protected Map<String, Model> innerTypes = new HashMap<String, Model>();
  protected Set<String> processedInnerTypes = new HashSet<String>();

  /**
   * Minor optimization: no need to keep on resolving same types over and over
   * again.
   */
  protected Map<JavaType, String> _resolvedTypeNames = new ConcurrentHashMap<JavaType, String>();
  
  public Map<String, Model> getDetectedTypes() {
    return innerTypes;
  }

  @SuppressWarnings("serial")
  public ModelResolver(ObjectMapper mapper) {
    mapper.registerModule(
      new SimpleModule("swagger", Version.unknownVersion()) {
        @Override
        public void setupModule(SetupContext context) {
          context.insertAnnotationIntrospector(new SwaggerAnnotationIntrospector());
        }
      });
    _mapper = mapper;
    _intr = mapper.getSerializationConfig().getAnnotationIntrospector();
  }

  public ObjectMapper objectMapper() {
    return _mapper;
  }

  public Property resolveProperty(Class<?> cls) {
    return resolveProperty(_mapper.constructType(cls));
  }

  public Property resolveProperty(JavaType propType) {
    Property property = null;

    String typeName = _typeName(propType);

    // primitive or null
    property = getPrimitiveProperty(typeName);

    // modelProp.setQualifiedType(_typeQName(propType));
    // And then properties specific to subset of property types:
    if (propType.isEnumType()) {
      // _addEnumProps(propDef, propType.getRawClass(), modelProp);
    } else if (propType.isContainerType()) {
      JavaType keyType = propType.getKeyType();
      JavaType valueType = propType.getContentType();

      if(keyType != null && valueType != null) {
        MapProperty mapProperty = new MapProperty();
        Property innerType = getPrimitiveProperty(_typeName(valueType));
        if(innerType == null) {
          String propertyTypeName = _typeName(valueType);
          Model innerModel = innerTypes.get(propertyTypeName);
          if(innerModel == null)
            innerModel = resolve(valueType);
          if(innerModel != null) {
            if(!"Object".equals(propertyTypeName)) {
              innerTypes.put(propertyTypeName, innerModel);
              innerType = new RefProperty(propertyTypeName);
              mapProperty.additionalProperties(innerType);
              property = mapProperty;
            }
            else {
              innerTypes.put(propertyTypeName, innerModel);
              innerType = new StringProperty();
              mapProperty.additionalProperties(innerType);
              property = mapProperty;
            }
          }
        }
        else {
          mapProperty.additionalProperties(innerType);
          property = mapProperty;
        }
      }
      else if(valueType != null) {
        ArrayProperty arrayProperty = new ArrayProperty();
        Property innerType = getPrimitiveProperty(_typeName(valueType));
        if(innerType == null) {
          String propertyTypeName = _typeName(valueType);
          Model innerModel = innerTypes.get(propertyTypeName);
          if(innerModel == null)
            innerModel = resolve(valueType);
          if(innerModel != null) {
            innerTypes.put(propertyTypeName, innerModel);
            innerType = new RefProperty(propertyTypeName);
            arrayProperty.setItems(innerType);
            property = arrayProperty;
          }
        }
        else {
          arrayProperty.setItems(innerType);
          property = arrayProperty;
        }
      }
    }

    if(property == null) {
      // complex type
      String propertyTypeName = _typeName(propType);
      Model innerModel = innerTypes.get(propertyTypeName);
      if(innerModel == null) {
        innerModel = resolve(propType);
      }
      if(innerModel != null) {
        innerTypes.put(propertyTypeName, innerModel);
        property = new RefProperty(propertyTypeName);
      }
    }

    return property;
  }
  
  public Model resolve(Class<?> cls) {
    return resolve(_mapper.constructType(cls));
  }

  public Model resolve(JavaType type) {
    final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(type);
    
    // Couple of possibilities for defining
    String name = _typeName(type, beanDesc);
    if("Object".equals(name)) {
	    return new ModelImpl();
    }
    
    if(type.isMapLikeType()) {
      return null;
    }

    // if processed already, return it or return null
    if(processedInnerTypes.contains(name))
      return innerTypes.get(name);

    // avoid recursion on failures
    processedInnerTypes.add(name);

    ModelImpl model = new ModelImpl()
      .name(name)
      .description(_description(beanDesc.getClassInfo()));

    // if XmlRootElement annotation, construct an Xml object and attach it to the model
    XmlRootElement rootAnnotation = beanDesc.getClassAnnotations().get(XmlRootElement.class);
    if(rootAnnotation != null && rootAnnotation.name() != null && !"".equals(rootAnnotation.name())) {
      Xml xml = new Xml()
        .name(rootAnnotation.name());
      if(rootAnnotation.namespace() != null && !"".equals(rootAnnotation.namespace()) && !"##default".equals(rootAnnotation.namespace()))
        xml.namespace(rootAnnotation.namespace());
      model.xml(xml);
    }

    ApiModel apiModel = beanDesc.getClassAnnotations().get(ApiModel.class);
    // TODO
    if (apiModel != null) {
      if(apiModel.value() != null && !"".equals(apiModel.value())) {
        name = apiModel.value();
        model.setName(name);
      }
      Class<?> parent = apiModel.parent();
      if (parent != Void.class) {
        // model.setBaseModel(_typeName(_mapper.constructType(parent)));
      }
    }

    String disc = (apiModel == null) ? "" : apiModel.discriminator();
    if (disc.isEmpty()) {
      // longer method would involve AnnotationIntrospector.findTypeResolver(...) but:
      JsonTypeInfo typeInfo = beanDesc.getClassAnnotations().get(JsonTypeInfo.class);
      if (typeInfo != null) {
        disc = typeInfo.property();
      }
    }
    if (!disc.isEmpty()) {
      model.setDiscriminator(disc);
    }

    List<Property> props = new ArrayList<Property>();
    for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
      Property property = null;
      String propName = propDef.getName();

      PropertyMetadata md = propDef.getMetadata();

      final AnnotatedMember member = propDef.getPrimaryMember();
      if(member != null) {
        JavaType propType = member.getType(beanDesc.bindingsForBeanType());
        property = resolveProperty(propType);

        if(property != null) {
          property.setName(propName);

          Boolean required = md.getRequired();
          if(required != null)
            property.setRequired(required);

          String description = _intr.findPropertyDescription(member);
          if(description != null && !"".equals(description))
            property.setDescription(description);

          Integer index = _intr.findPropertyIndex(member);
          if (index != null) {
            property.setPosition(index);
          }
          property.setExample(_findExampleValue(member));

          if(property != null) {
            // check for XML annotations
            XmlElementWrapper wrapper = member.getAnnotation(XmlElementWrapper.class);

            if(wrapper != null) {
              Xml xml = new Xml();
              xml.setWrapped(true);

              if(wrapper.name() != null && !"".equals(wrapper.name()))
                xml.setName(wrapper.name());
              if(wrapper.namespace() != null && !"".equals(wrapper.namespace()) && !"##default".equals(wrapper.namespace()))
                xml.setNamespace(wrapper.namespace());

              property.setXml(xml);
            }

            XmlElement element = member.getAnnotation(XmlElement.class);
            if(element != null) {
              if(element.name() != null && !"".equals(element.name())) {
                Xml xml = property.getXml();
                if(xml == null) {
                  xml = new Xml();
                  property.setXml(xml);
                }
                xml.setName(element.name());
              }
            }
          }

          props.add(property);
          // model.property(propName, property);
        }
      }
    }


    List<NamedType> nts = _intr.findSubtypes(beanDesc.getClassInfo());
    if (nts != null) {
      ArrayList<String> subtypeNames = new ArrayList<String>();
      for (NamedType subtype : nts) {
        Model subtypeModel = resolve(subtype.getType());

        if(subtypeModel instanceof ModelImpl && subtypeModel != null) {
          ModelImpl impl = (ModelImpl) subtypeModel;

          // remove shared properties defined in the parent
          if(model.getProperties() != null) {
            for(String propertyName : model.getProperties().keySet()) {
              if(impl.getProperties().containsKey(propertyName)) {
                impl.getProperties().remove(propertyName);
              }
            }
          }

          impl.setDiscriminator(null);
          innerTypes.put(impl.getName(), new ComposedModel()
            .parent(new RefModel(name))
            .child(impl));
        }
      }
    }

    Collections.sort(props, getPropertyComparator());

    Map<String, Property> modelProps = new LinkedHashMap<String, Property>();
    for (Property prop : props) {
      modelProps.put(prop.getName(), prop);
    }
    model.setProperties(modelProps);
    innerTypes.put(name, model);
    return model;
  }

  protected Property getPrimitiveProperty(String typeName) {
    Property property = null;
    if("boolean".equals(typeName)) {
      property = new BooleanProperty();
    }
    if("string".equals(typeName)) {
      property = new StringProperty();
    }
    else if("integer".equals(typeName)) {
      property = new IntegerProperty();
    }
    else if("long".equals(typeName)) {
      property = new LongProperty();
    }
    else if("float".equals(typeName)) {
      property = new FloatProperty();
    }
    else if("double".equals(typeName)) {
      property = new DoubleProperty();
    }
    else if("dateTime".equals(typeName)) {
      property = new DateTimeProperty();
    }
    return property;
  }

/*  
  protected void _addEnumProps(BeanPropertyDefinition propDef, Class<?> propClass,
      ModelProperty result) {
    final boolean useIndex =  _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
    final boolean useToString = _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    List<AllowableValue> enums = new ArrayList<AllowableValue>();
    @SuppressWarnings("unchecked")
    Class<Enum<?>> enumClass = (Class<Enum<?>>) propClass;
    for (Enum<?> en : enumClass.getEnumConstants()) {
      String n;
      if (useIndex) {
        n = String.valueOf(en.ordinal());
      } else if (useToString) {
        n = en.toString();
      } else {
        n = _intr.findEnumValue(en);
      }
      enums.add(new AllowableValue(n));
    }
    result.setAllowableValues(enums);
  }
*/
  protected String _description(Annotated ann) {
    // while name suggests it's only for properties, should work for any Annotated thing.
    // also; with Swagger introspector's help, should get it from ApiModel/ApiModelProperty
    return _intr.findPropertyDescription(ann);
  }

  protected String _typeName(JavaType type) {
    return _typeName(type, null);
  }
  
  protected String _typeName(JavaType type, BeanDescription beanDesc) {
    String name = _resolvedTypeNames.get(type);
    if (name != null) {
      return name;
    }
    name = _findTypeName(type, beanDesc);
    _resolvedTypeNames.put(type,  name);
    return name;
  }

  protected String _findTypeName(JavaType type, BeanDescription beanDesc) {
    // First, handle container types; they require recursion
    if (type.isArrayType())
      return "Array";

    if (type.isMapLikeType())
      return "Map";

    if (type.isContainerType()) {
      if (Set.class.isAssignableFrom(type.getRawClass()))
        return "Set";
      return "List";
    }
    if (beanDesc == null) {
      beanDesc = _mapper.getSerializationConfig().introspectClassAnnotations(type);
    }
      
    PropertyName rootName = _intr.findRootName(beanDesc.getClassInfo());
    if (rootName != null && rootName.hasSimpleName()) {
      return rootName.getSimpleName();
    }
    return _typeNameResolver.nameForType(type);
  }

  protected String _typeQName(JavaType type) {
    return type.getRawClass().getName();
  }

  protected String _subTypeName(NamedType type) {
    // !!! TODO: should this use 'name' instead?
    return type.getType().getName();
  }

  protected String _findExampleValue(Annotated a) {
    ApiModelProperty prop = a.getAnnotation(ApiModelProperty.class);
    if (prop != null) {
      if (!prop.example().isEmpty()) {
        return prop.example();
      }
    }
    return null;
  }

  // TODO remove this
  static Comparator<Property> getPropertyComparator() {
    return new Comparator<Property>() {
      @Override
      public int compare(Property one, Property two) {
        if (one.getPosition() == null && two.getPosition() == null)
          return 0;
        if (one.getPosition() == null)
          return -1;
        if (two.getPosition() == null)
          return 1;
        return one.getPosition().compareTo(two.getPosition());
      }
    };
  }
}
