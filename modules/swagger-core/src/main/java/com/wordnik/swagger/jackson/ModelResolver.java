package com.wordnik.swagger.jackson;

import com.wordnik.swagger.util.Json;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.converter.ModelConverter;
import com.wordnik.swagger.converter.ModelConverterContext;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

import javax.xml.bind.annotation.*;
import javax.validation.constraints.*;

public class ModelResolver extends AbstractModelConverter implements ModelConverter {
  Logger LOGGER = LoggerFactory.getLogger(ModelResolver.class);

  @SuppressWarnings("serial")
  public ModelResolver(ObjectMapper mapper) {
    super(mapper);
  }

  public ObjectMapper objectMapper() {
    return _mapper;
  }

  protected boolean shouldIgnoreClass(Type type) {
    if(type instanceof Class) {
      Class<?> cls = (Class)type;
      if(cls.getName().equals("javax.ws.rs.Response"))
        return true;
    }
    else {
      LOGGER.debug("can't check class " + type);
    }
    return false;
  }

  public Property resolveProperty(Type type,
      ModelConverterContext context,
      Annotation[] annotations,
      Iterator<ModelConverter> next) {
    if(this.shouldIgnoreClass(type))
      return null;

    return resolveProperty(_mapper.constructType(type), context, annotations, next);
  }

  public Property resolveProperty(JavaType propType,
      ModelConverterContext context,
      Annotation[] annotations,
      Iterator<ModelConverter> next) {
    Property property = null;
    String typeName = _typeName(propType);

    LOGGER.debug("resolveProperty " + propType);

    // primitive or null
    property = getPrimitiveProperty(typeName);
    // And then properties specific to subset of property types:
    if (propType.isContainerType()) {
      JavaType keyType = propType.getKeyType();
      JavaType valueType = propType.getContentType();
      if(keyType != null && valueType != null) {
        MapProperty mapProperty = new MapProperty();
        Property innerType = getPrimitiveProperty(_typeName(valueType));

        if(innerType == null) { 
          String propertyTypeName = _typeName(valueType);
          Model innerModel = context.resolve(valueType); 
          if(innerModel != null) {
            if(!"Object".equals(propertyTypeName)) {              
              innerType = new RefProperty(propertyTypeName);
              mapProperty.additionalProperties(innerType);
              property = mapProperty;
            }
            else {
              innerType = new ObjectProperty();
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
          LOGGER.debug("no primitive property type from " + valueType);
          String propertyTypeName = _typeName(valueType);
          LOGGER.debug("using name " + propertyTypeName);
          if(!"Object".equals(propertyTypeName)) {
            Model innerModel = context.resolve(valueType);
            LOGGER.debug("got inner model " + innerModel);

            if(innerModel != null) {
              LOGGER.debug("found inner model " + innerModel);
              // model name may be overriding what was detected
              if(innerModel instanceof ModelImpl) {
                ModelImpl impl = (ModelImpl) innerModel;
                if(impl.getName() != null)
                  propertyTypeName = impl.getName();
              }
              Class<?> cls = propType.getRawClass();
              if(_isSetType(cls))
                arrayProperty.setUniqueItems(true);

              innerType = new RefProperty(propertyTypeName);
              arrayProperty.setItems(innerType);
              property = arrayProperty;
            }
          }
          else {
            LOGGER.debug("falling back to object type");
            innerType = new ObjectProperty();
            arrayProperty.setItems(innerType);
            property = arrayProperty;
          }
        }
        else {
          if(keyType == null) {
            Class<?> cls = propType.getRawClass();

            if(_isSetType(cls))
              arrayProperty.setUniqueItems(true);
          }

          arrayProperty.setItems(innerType);
          property = arrayProperty;
        }
      }
    }

    if(property == null) {
      if (propType.isEnumType()) {
        property = new StringProperty();
        _addEnumProps(propType.getRawClass(), property);
      }
      else if (_isOptionalType(propType)) {
        property = context.resolveProperty(propType.containedType(0), null);
      }
      else {
        // complex type
        String propertyTypeName = _typeName(propType);
        Model innerModel =  context.resolve(propType);      
        if(innerModel != null) {      
          property = new RefProperty(propertyTypeName);
        }
      }
    }

    return property;
  }

  private boolean _isOptionalType(JavaType propType) {
    return "com.google.common.base.Optional".equals(propType.getRawClass().getCanonicalName());
  }

  public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
    if(this.shouldIgnoreClass(type))
      return null;

    return resolve(_mapper.constructType(type),context, next);
  }

  protected void _addEnumProps(Class<?> propClass, Property property) {
    final boolean useIndex =  _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
    final boolean useToString = _mapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

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
      if(property instanceof StringProperty) {
        StringProperty sp = (StringProperty) property;
        sp._enum(n);
      }
    }
  }


  public Model resolve(JavaType type, ModelConverterContext context, Iterator<ModelConverter> next) {
    final BeanDescription beanDesc = _mapper.getSerializationConfig().introspect(type);
    if (type.isEnumType()) {
      // TODO how to handle if model provided is simply an enum
    }

    // Couple of possibilities for defining
    String name = _typeName(type, beanDesc);

    if("Object".equals(name)) {
      return new ModelImpl();
    }
    
    if(type.isMapLikeType()) {
      return null;
    }

    ModelImpl model = new ModelImpl()
      .name(name)
      .description(_description(beanDesc.getClassInfo()));

    // if XmlRootElement annotation, construct an Xml object and attach it to the model
    XmlRootElement rootAnnotation = beanDesc.getClassAnnotations().get(XmlRootElement.class);
    if(rootAnnotation != null && !"".equals(rootAnnotation.name()) && !"##default".equals(rootAnnotation.name())) {
      LOGGER.debug(rootAnnotation.toString());
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

    // see if @JsonIgnoreProperties exist
    Set<String> propertiesToIgnore = new HashSet<String>();
    JsonIgnoreProperties ignoreProperties = beanDesc.getClassAnnotations().get(JsonIgnoreProperties.class);
    if(ignoreProperties != null) {
      propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
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
      Annotation[] annotations = null;

      // hack to avoid clobbering properties with get/is names
      // it's ugly but gets around https://github.com/swagger-api/swagger-core/issues/415
      if(propDef.getPrimaryMember() != null) {
        java.lang.reflect.Member member = propDef.getPrimaryMember().getMember();
        if(member != null) {
          String altName = member.getName();
          if(altName != null) {
            if(altName.startsWith("get")) {
              if(!Character.isUpperCase(altName.charAt(3))) {
                propName = altName;
              }
            }
            else if (altName.startsWith("is")) {
              if(!Character.isUpperCase(altName.charAt(2))) {
                propName = altName;
              }
            }
          }
        }
      }

      PropertyMetadata md = propDef.getMetadata();

      boolean hasSetter = false, hasGetter = false;
      if(propDef.getSetter() == null)
        hasSetter = false;
      else
        hasSetter = true;
      if(propDef.getGetter() != null) {
        JsonProperty pd = propDef.getGetter().getAnnotation(JsonProperty.class);
        if(pd != null)
          hasGetter = true;
      }
      Boolean isReadOnly = null;
      if(!hasSetter & hasGetter)
        isReadOnly = Boolean.TRUE;
      else
        isReadOnly = Boolean.FALSE;

      final AnnotatedMember member = propDef.getPrimaryMember();

      if(member != null && !propertiesToIgnore.contains(propName)) {
        List<Annotation> annotationList = new ArrayList<Annotation>();
        for(Annotation a : member.annotations())
          annotationList.add(a);

        annotations = annotationList.toArray(new Annotation[annotationList.size()]);

        ApiModelProperty mp = member.getAnnotation(ApiModelProperty.class);

        JavaType propType = member.getType(beanDesc.bindingsForBeanType());

        // allow override of name from annotation
        if(mp != null && !mp.name().isEmpty())
          propName = mp.name();

        if(mp != null && !mp.dataType().isEmpty()) {
          String or = mp.dataType();

          JavaType innerJavaType = null;
          LOGGER.debug("overriding datatype from " + propType + " to " + or);

          if(or.toLowerCase().startsWith("list[")) {
            String innerType = or.substring(5, or.length() - 1);
            ArrayProperty p = new ArrayProperty();
            Property primitiveProperty = getPrimitiveProperty(innerType);
            if(primitiveProperty != null)
              p.setItems(primitiveProperty);
            else {
              innerJavaType = getInnerType(innerType);
              p.setItems(context.resolveProperty(innerJavaType, annotations));
            }
            property = p;
          }
          else if(or.toLowerCase().startsWith("map[")) {
            int pos = or.indexOf(",");
            if(pos > 0) {
              String innerType = or.substring(pos + 1, or.length() - 1);
              MapProperty p = new MapProperty();
              Property primitiveProperty = getPrimitiveProperty(innerType);
              if(primitiveProperty != null)
                p.setAdditionalProperties(primitiveProperty);
              else {
                innerJavaType = getInnerType(innerType);
                p.setAdditionalProperties(context.resolveProperty(innerJavaType, annotations));
              }
              property = p;
            }
          }
          else {
            Property primitiveProperty = getPrimitiveProperty(or);
            if(primitiveProperty != null)
              property = primitiveProperty;
            else {
              innerJavaType = getInnerType(or);
              property = context.resolveProperty(innerJavaType, annotations);
            }
          }
          if(innerJavaType != null) {
            context.resolve(innerJavaType);
          }
        }

        // no property from override, construct from propType
        if(property == null)
          property = context.resolveProperty(propType, annotations);

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
          property.setDefault(_findDefaultValue(member));
          property.setExample(_findExampleValue(member));
          property.setReadOnly(_findReadOnly(member));
          
          if(property.getReadOnly() == null) {
            if(isReadOnly)
              property.setReadOnly(isReadOnly);
          }

          if(property instanceof StringProperty) {
            if(mp != null) {
              String allowableValues = mp.allowableValues();
              LOGGER.debug("allowableValues " + allowableValues);
              if(!"".equals(allowableValues)) {
                String[] parts = allowableValues.split(",");
                LOGGER.debug("found " + parts.length + " parts");
                for(String part : parts) {
                  if(property instanceof StringProperty) {
                    StringProperty sp = (StringProperty) property;
                    sp._enum(part.trim());
                    LOGGER.debug("added enum value " + part);
                  }          
                }
              }
            }
          }

          if(property != null) {
            // check for XML annotations
            XmlElementWrapper wrapper = member.getAnnotation(XmlElementWrapper.class);

            if(wrapper != null) {
              Xml xml = new Xml();
              xml.setWrapped(true);

              if(wrapper.name() != null) {
                if("##default".equals(wrapper.name()))
                  xml.setName(propName);
                else if(!"".equals(wrapper.name()))
                  xml.setName(wrapper.name());
              }
              if(wrapper.namespace() != null && !"".equals(wrapper.namespace()) && !"##default".equals(wrapper.namespace()))
                xml.setNamespace(wrapper.namespace());

              property.setXml(xml);
            }

            XmlElement element = member.getAnnotation(XmlElement.class);
            if(element != null) {
              if(!element.name().isEmpty()) {
                // don't set Xml object if name is same
                if(!element.name().equals(propName) && !"##default".equals(element.name())) {
                  Xml xml = property.getXml();
                  if(xml == null) {
                    xml = new Xml();
                    property.setXml(xml);
                  }
                  xml.setName(element.name());
                }
              }
            }
            XmlAttribute attr = member.getAnnotation(XmlAttribute.class);
            if(attr != null) {
              if(!"".equals(attr.name())) {
                // don't set Xml object if name is same
                if(!attr.name().equals(propName) && !"##default".equals(attr.name())) {
                  Xml xml = property.getXml();
                  if(xml == null) {
                    xml = new Xml();
                    property.setXml(xml);
                  }
                  xml.setName(attr.name());
                }
              }
            }
            
          }
          applyBeanValidatorAnnotations(property, annotations);
          props.add(property);
        }
      }
    }

    List<NamedType> nts = _intr.findSubtypes(beanDesc.getClassInfo());
    if (nts != null) {
      ArrayList<String> subtypeNames = new ArrayList<String>();
      for (NamedType subtype : nts) {
        Model subtypeModel = context.resolve(subtype.getType());

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
          ComposedModel child = new ComposedModel()
            .parent(new RefModel(name))
            .child(impl);    
          context.defineModel(impl.getName(), child);
        }
      }
    }

    Collections.sort(props, getPropertyComparator());

    Map<String, Property> modelProps = new LinkedHashMap<String, Property>();
    for (Property prop : props) {
      modelProps.put(prop.getName(), prop);
    }
    if(modelProps.size() == 0)
      model.setType("object");
    model.setProperties(modelProps);
    return model;
  }

  protected void applyBeanValidatorAnnotations(Property property, Annotation[] annotations) {
    Map<String, Annotation> annos = new HashMap<String, Annotation>();
    if(annotations != null) {
      for(Annotation anno: annotations)
        annos.put(anno.annotationType().getName(), anno);
    }
    if(annos.containsKey("javax.validation.constraints.NotNull")) {
      property.setRequired(true);
    }
    if(annos.containsKey("javax.validation.constraints.Min")) {
      if(property instanceof AbstractNumericProperty) {
        Min min = (Min) annos.get("javax.validation.constraints.Min");
        AbstractNumericProperty ap = (AbstractNumericProperty) property;
        ap.setMinimum(new Double(min.value()));
      }
    }
    if(annos.containsKey("javax.validation.constraints.Max")) {
      if(property instanceof AbstractNumericProperty) {
        Max max = (Max) annos.get("javax.validation.constraints.Max");
        AbstractNumericProperty ap = (AbstractNumericProperty) property;
        ap.setMaximum(new Double(max.value()));
      }
    }
    if(annos.containsKey("javax.validation.constraints.Size")) {
      Size size = (Size) annos.get("javax.validation.constraints.Size");
      if(property instanceof AbstractNumericProperty) {
        AbstractNumericProperty ap = (AbstractNumericProperty) property;
        ap.setMinimum(new Double(size.min()));
        ap.setMaximum(new Double(size.max()));
      }
      if(property instanceof StringProperty) {
        StringProperty sp = (StringProperty) property;
        sp.minLength(new Integer(size.min()));
        sp.maxLength(new Integer(size.max()));
      }
    }
    if(annos.containsKey("javax.validation.constraints.DecimalMin")) {
      DecimalMin min = (DecimalMin) annos.get("javax.validation.constraints.DecimalMin");
      if(property instanceof AbstractNumericProperty) {
        AbstractNumericProperty ap = (AbstractNumericProperty) property;
        if(min.inclusive())
          ap.setMinimum(new Double(min.value()));
        else
          ap.setExclusiveMinimum(new Double(min.value()));
      }
    }
    if(annos.containsKey("javax.validation.constraints.DecimalMax")) {
      DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
      if(property instanceof AbstractNumericProperty) {
        AbstractNumericProperty ap = (AbstractNumericProperty) property;
        if(max.inclusive())
          ap.setMaximum(new Double(max.value()));
        else
          ap.setExclusiveMaximum(new Double(max.value()));
      }
    }
  }

  protected JavaType getInnerType(String innerType) {
    try{
      Class<?> innerClass = Class.forName(innerType);
      if(innerClass != null) {
        TypeFactory tf = _mapper.getTypeFactory();
        return tf.constructType(innerClass);
      }
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
