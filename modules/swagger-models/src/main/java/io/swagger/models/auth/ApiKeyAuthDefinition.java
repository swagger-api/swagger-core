package io.swagger.models.auth;

public class ApiKeyAuthDefinition extends AbstractSecuritySchemeDefinition {
    private String type = "apiKey";
    private String name;
    private In in;

    public ApiKeyAuthDefinition() {
    }

    public ApiKeyAuthDefinition(String name, In in) {
        super();
        this.setName(name);
        this.setIn(in);
    }

    public ApiKeyAuthDefinition name(String name) {
        this.setName(name);
        return this;
    }

    public ApiKeyAuthDefinition in(In in) {
        this.setIn(in);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public In getIn() {
        return in;
    }

    public void setIn(In in) {
        this.in = in;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

        @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((in == null) ? 0 : in.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApiKeyAuthDefinition other = (ApiKeyAuthDefinition) obj;
        if (in != other.in) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
