package io.swagger.oas.models.media;

import java.util.HashMap;
import java.util.Map;

public class Discriminator {
    private String propertyName;
    private Map<String, String> mapping;
 
    /**
     * sets this Discriminator's propertyName property to the given propertyName and
     * returns this instance of Discriminator
     *
     * @param String propertyName
     * @return Discriminator
     */
    public Discriminator propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }
    
    /**
     * returns the propertyName property from a Discriminator instance.
     *
     * @return String propertyName
     **/
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * sets this Discriminator's propertyName property to the given propertyName.
     *
     * @param String propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * maps the given name to the given value and store it in this Discriminator's mapping property.
     * 
     * @param String name
     * @param String value
     * @return Discriminator
     */
    public Discriminator mapping(String name, String value) {
        if(this.mapping == null) {
            this.mapping = new HashMap<>();
        }
        this.mapping.put(name, value);
        return this;
    }

    /**
     * sets this Discriminator's mapping property to the given mapping and
     * returns this instance of Discriminator
     *
     * @param Map&lt;String, String&gt; mapping
     * @return Discriminator
     */
    public Discriminator mapping(Map<String, String> mapping) {
        this.mapping = mapping;
        return this;
    }

    /**
     * returns the mapping property from a Discriminator instance.
     *
     * @return Map&lt;String, String&gt; mapping
     **/
    public Map<String, String> getMapping() {
        return mapping;
    }

    /**
     * sets this Discriminator's mapping property to the given mapping.
     *
     * @param Map&lt;String, String&gt; mapping
     */
    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Discriminator)) {
            return false;
        }

        Discriminator that = (Discriminator) o;

        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) {
            return false;
        }
        return mapping != null ? mapping.equals(that.mapping) : that.mapping == null;

    }

    @Override
    public int hashCode() {
        int result = propertyName != null ? propertyName.hashCode() : 0;
        result = 31 * result + (mapping != null ? mapping.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Discriminator{" +
                "propertyName='" + propertyName + '\'' +
                ", mapping=" + mapping +
                '}';
    }
}
