package io.swagger.jackson;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import io.swagger.models.Xml;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * The <code>JAXBAnnotationsHelper</code> class defines helper methods for
 * applying JAXB annotations to property definitions.
 */
class JAXBAnnotationsHelper {
    private static final String JAXB_DEFAULT = "##default";

    private JAXBAnnotationsHelper() {
    }

    /**
     * Applies annotations to property's {@link Xml} definition.
     *
     * @param member   annotations provider
     * @param property property instance to be updated
     */
    public static void apply(AnnotatedMember member, Property property) {
        if (member.hasAnnotation(XmlElementWrapper.class) || member.hasAnnotation(XmlElement.class)) {
            applyElement(member, property);
        } else if (member.hasAnnotation(XmlAttribute.class) && isAttributeAllowed(property)) {
            applyAttribute(member, property);
        }
    }

    /**
     * Puts definitions for XML element.
     *
     * @param member   annotations provider
     * @param property property instance to be updated
     */
    private static void applyElement(AnnotatedMember member, Property property) {
        final XmlElementWrapper wrapper = member.getAnnotation(XmlElementWrapper.class);
        if (wrapper != null) {
            final Xml xml = getXml(property);
            xml.setWrapped(true);
            // No need to set the xml name if the name provided by xmlelementwrapper annotation is ##default or equal to the property name | https://github.com/swagger-api/swagger-core/pull/2050
            if (!"##default".equals(wrapper.name()) && !wrapper.name().isEmpty() && !wrapper.name().equals(property.getName())) {
                xml.setName(wrapper.name());
            }
        }
        else {
            final XmlElement element = member.getAnnotation(XmlElement.class);
            if (element != null) {
                setName(element.namespace(), element.name(), property);
            }
        }
    }

    /**
     * Puts definitions for XML attribute.
     *
     * @param member   annotations provider
     * @param property property instance to be updated
     */
    private static void applyAttribute(AnnotatedMember member, Property property) {
        final XmlAttribute attribute = member.getAnnotation(XmlAttribute.class);
        if (attribute != null) {
            final Xml xml = getXml(property);
            xml.setAttribute(true);
            setName(attribute.namespace(), attribute.name(), property);
        }
    }

    private static Xml getXml(Property property) {
        final Xml existing = property.getXml();
        if (existing != null) {
            return existing;
        }
        final Xml created = new Xml();
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
    private static boolean setName(String ns, String name, Property property) {
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
    private static boolean isAttributeAllowed(Property property) {
        for (Class<?> item : new Class<?>[]{ArrayProperty.class, MapProperty.class, ObjectProperty.class,
                RefProperty.class}) {
            if (item.isInstance(property)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(String name) {
        return StringUtils.isEmpty(name) || JAXB_DEFAULT.equals(name);
    }
}
