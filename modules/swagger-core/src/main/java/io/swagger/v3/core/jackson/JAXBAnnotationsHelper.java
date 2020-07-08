package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.lang.annotation.Annotation;

/**
 * The <code>JAXBAnnotationsHelper</code> class defines helper methods for
 * applying JAXB annotations to property definitions.
 */
class JAXBAnnotationsHelper {
    public static final String JAXB_DEFAULT = "##default";

    private JAXBAnnotationsHelper() {
    }

    /**
     * Applies annotations to property's {@link XML} definition.
     *
     * @param member   annotations provider
     * @param property property instance to be updated
     */
    public static void apply(Annotated member, Annotation[] annotations, Schema property) {

        XmlElementWrapper wrapper = member.getAnnotation(XmlElementWrapper.class);
        if (wrapper == null) {
            wrapper = AnnotationsUtils.getAnnotation(XmlElementWrapper.class, annotations);
        }
        XmlAttribute attr = member.getAnnotation(XmlAttribute.class);
        if (attr == null) {
            attr = AnnotationsUtils.getAnnotation(XmlAttribute.class, annotations);
        }
        XmlElement elem = member.getAnnotation(XmlElement.class);
        if (elem == null) {
            elem = AnnotationsUtils.getAnnotation(XmlElement.class, annotations);
        }

        if (wrapper != null) {
            applyElement(wrapper, property);
        } else if (elem != null) {
            applyElement(elem, property);
        } else if (attr != null && isAttributeAllowed(property)) {
            applyAttribute(attr, property);
        }
    }

    /**
     * Puts definitions for XML wrapper.
     *
     * @param wrapper   XmlElementWrapper
     * @param property property instance to be updated
     */
    private static void applyElement(XmlElementWrapper wrapper, Schema property) {
        if (wrapper != null) {
            final XML xml = getXml(property);
            xml.setWrapped(true);
            // No need to set the xml name if the name provided by xmlelementwrapper annotation is ##default or equal to the property name | https://github.com/swagger-api/swagger-core/pull/2050
            if (!JAXB_DEFAULT.equals(wrapper.name()) && !wrapper.name().isEmpty() && !wrapper.name().equals(property.getName())) {
                xml.setName(wrapper.name());
            }
        }
    }

    /**
     * Puts definitions for XML element.
     *
     * @param element   XmlElement
     * @param property property instance to be updated
     */
    private static void applyElement(XmlElement element, Schema property) {
        if (element != null) {
            setName(element.namespace(), element.name(), property);
        }
    }

    /**
     * Puts definitions for XML attribute.
     *
     * @param attribute   XmlAttribute
     * @param property property instance to be updated
     */
    private static void applyAttribute(XmlAttribute attribute, Schema property) {
        if (attribute != null) {
            final XML xml = getXml(property);
            xml.setAttribute(true);
            setName(attribute.namespace(), attribute.name(), property);
        }
    }

    private static XML getXml(Schema property) {
        final XML existing = property.getXml();
        if (existing != null) {
            return existing;
        }
        final XML created = new XML();
        property.setXml(created);
        return created;
    }

    /**
     * Puts name space and name for XML node or attribute.
     *
     * @param ns       name space
     * @param name     name
     * @param property property instance to be updated
     * @return <code>true</code> if name space and name have been set
     */
    private static boolean setName(String ns, String name, Schema property) {
        boolean apply = false;
        final String cleanName = StringUtils.trimToNull(name);
        final String useName;
        if (!isEmpty(cleanName) && !cleanName.equals(property.getName())) {
            useName = cleanName;
            apply = true;
        } else {
            useName = null;
        }
        final String cleanNS = StringUtils.trimToNull(ns);
        final String useNS;
        if (!isEmpty(cleanNS)) {
            useNS = cleanNS;
            apply = true;
        } else {
            useNS = null;
        }
        // Set everything or nothing
        if (apply) {
            getXml(property).name(useName).namespace(useNS);
        }
        return apply;
    }

    /**
     * Checks whether the passed property can be represented as node attribute.
     *
     * @param property property instance to be checked
     * @return <code>true</code> if the passed property can be represented as
     * node attribute
     */
    private static boolean isAttributeAllowed(Schema property) {
        for (Class<?> item : new Class<?>[]{ArraySchema.class, MapSchema.class, ObjectSchema.class}) {
            if (item.isInstance(property)) {
                return false;
            }
        }
        return StringUtils.isBlank(property.get$ref());
    }

    private static boolean isEmpty(String name) {
        return StringUtils.isEmpty(name) || JAXB_DEFAULT.equals(name);
    }
}
