package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class BodyParameter extends AbstractParameter implements Parameter {
    Model schema;
    Map<String, String> examples;

    public BodyParameter() {
        super.setIn("body");
    }

    public BodyParameter schema(Model schema) {
        this.setSchema(schema);
        return this;
    }

    public BodyParameter example(String mediaType, String value) {
        this.addExample(mediaType, value);
        return this;
    }

    public BodyParameter description(String description) {
        this.setDescription(description);
        return this;
    }

    public BodyParameter name(String name) {
        this.setName(name);
        return this;
    }

    public Model getSchema() {
        return schema;
    }

    public void setSchema(Model schema) {
        this.schema = schema;
    }

    public void addExample(String mediaType, String value) {
        if(examples == null) {
            examples = new LinkedHashMap<String, String>();
        }
        examples.put(mediaType, value);
    }

    @JsonProperty("x-examples")
    public Map<String, String> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, String> examples) {
        this.examples = examples;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((schema == null) ? 0 : schema.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BodyParameter other = (BodyParameter) obj;
        if (schema == null) {
            if (other.schema != null) {
                return false;
            }
        } else if (!schema.equals(other.schema)) {
            return false;
        }
        return true;
    }
}
