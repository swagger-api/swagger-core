package com.wordnik.swagger.converter

import com.wordnik.swagger.core.SwaggerSpec
import com.wordnik.swagger.annotations.ApiModel

trait BaseConverter {
  def toDescriptionOpt(cls: Class[_]): Option[String] = {
    var description: Option[String] = None
    for(anno <- cls.getAnnotations) {
      anno match {
        case e: ApiModel => {
          if(e.description != null) description = Some(e.description)
        }
        case _ =>        
      }
    }
    description
  }

  def toName(cls: Class[_]): String = {
    import javax.xml.bind.annotation._

    val xmlRootElement = cls.getAnnotation(classOf[XmlRootElement])
    val xmlEnum = cls.getAnnotation(classOf[XmlEnum])

    if (xmlEnum != null && xmlEnum.value != null)
      toName(xmlEnum.value())
    else if (xmlRootElement != null) {
      if ("##default".equals(xmlRootElement.name())) {
        cls.getSimpleName 
      } else {
        xmlRootElement.name() 
      }
    } else if (cls.getName.startsWith("java.lang.")) {
      val name = cls.getName.substring("java.lang.".length)
      val lc = name.toLowerCase
      if(SwaggerSpec.baseTypes.contains(lc)) lc
      else name
    }
    else if (cls.getName.indexOf(".") < 0) cls.getName
    else cls.getSimpleName 
  }
}