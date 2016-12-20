package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Operation {
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();
    private List<String> tags;
    private String summary;
    private String description;
    private String operationId;
    private List<Scheme> schemes;
    private List<String> consumes;
    private List<String> produces;
    private List<Parameter> parameters = new ArrayList<Parameter>();
    private Map<String, Response> responses;
    private List<Map<String, List<String>>> security;
    private ExternalDocs externalDocs;
    private Boolean deprecated;

    public Operation summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public Operation description(String description) {
        this.setDescription(description);
        return this;
    }

    public Operation operationId(String operationId) {
        this.setOperationId(operationId);
        return this;
    }

    public Operation schemes(List<Scheme> schemes) {
        this.setSchemes(schemes);
        return this;
    }

    public Operation scheme(Scheme scheme) {
        this.addScheme(scheme);
        return this;
    }

    public Operation consumes(List<String> consumes) {
        this.setConsumes(consumes);
        return this;
    }

    public Operation consumes(String consumes) {
        this.addConsumes(consumes);
        return this;
    }

    public Operation produces(List<String> produces) {
        this.setProduces(produces);
        return this;
    }

    public Operation produces(String produces) {
        this.addProduces(produces);
        return this;
    }

    public Operation security(SecurityRequirement security) {
        this.addSecurity(security.getName(), security.getScopes());
        return this;
    }

    public Operation parameter(Parameter parameter) {
        this.addParameter(parameter);
        return this;
    }

    public Operation response(int key, Response response) {
        this.addResponse(String.valueOf(key), response);
        return this;
    }

    public Operation defaultResponse(Response response) {
        this.addResponse("default", response);
        return this;
    }

    public Operation tags(List<String> tags) {
        this.setTags(tags);
        return this;
    }

    public Operation tag(String tag) {
        this.addTag(tag);
        return this;
    }

    public Operation externalDocs(ExternalDocs externalDocs) {
        this.setExternalDocs(externalDocs);
        return this;
    }

    public Operation deprecated(Boolean deprecated) {
        this.setDeprecated(deprecated);
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<String>();
        }
        this.tags.add(tag);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }

    public void addScheme(Scheme scheme) {
        if (schemes == null) {
            schemes = new ArrayList<Scheme>();
        }
        if (!schemes.contains(scheme)) {
            schemes.add(scheme);
        }
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public void addConsumes(String consumes) {
        if (this.consumes == null) {
            this.consumes = new ArrayList<String>();
        }
        this.consumes.add(consumes);
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public void addProduces(String produces) {
        if (this.produces == null) {
            this.produces = new ArrayList<String>();
        }
        this.produces.add(produces);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter parameter) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<Parameter>();
        }
        this.parameters.add(parameter);
    }

    public Map<String, Response> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, Response> responses) {
        this.responses = responses;
    }

    public void addResponse(String key, Response response) {
        if (this.responses == null) {
            this.responses = new LinkedHashMap<String, Response>();
        }
        this.responses.put(key, response);
    }

    public List<Map<String, List<String>>> getSecurity() {
        return security;
    }

    public void setSecurity(List<Map<String, List<String>>> security) {
        this.security = security;
    }

    public void addSecurity(String name, List<String> scopes) {
        if (this.security == null) {
            this.security = new ArrayList<Map<String, List<String>>>();
        }
        Map<String, List<String>> req = new LinkedHashMap<String, List<String>>();
        if (scopes == null) {
            scopes = new ArrayList<String>();
        }
        req.put(name, scopes);
        this.security.add(req);
    }

    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        this.externalDocs = value;
    }

    public Boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean value) {
        if (value == null || value.equals(Boolean.FALSE)) {
            this.deprecated = null;
        } else {
            this.deprecated = value;
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            vendorExtensions.put(name, value);
        }
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumes == null) ? 0 : consumes.hashCode());
        result = prime * result
                + ((deprecated == null) ? 0 : deprecated.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((externalDocs == null) ? 0 : externalDocs.hashCode());
        result = prime * result
                + ((operationId == null) ? 0 : operationId.hashCode());
        result = prime * result
                + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((produces == null) ? 0 : produces.hashCode());
        result = prime * result + ((responses == null) ? 0 : responses.hashCode());
        result = prime * result + ((schemes == null) ? 0 : schemes.hashCode());
        result = prime * result + ((security == null) ? 0 : security.hashCode());
        result = prime * result + ((summary == null) ? 0 : summary.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        result = prime * result
                + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        Operation other = (Operation) obj;
        if (consumes == null) {
            if (other.consumes != null) {
                return false;
            }
        } else if (!consumes.equals(other.consumes)) {
            return false;
        }
        if (deprecated == null) {
            if (other.deprecated != null) {
                return false;
            }
        } else if (!deprecated.equals(other.deprecated)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (operationId == null) {
            if (other.operationId != null) {
                return false;
            }
        } else if (!operationId.equals(other.operationId)) {
            return false;
        }
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        if (produces == null) {
            if (other.produces != null) {
                return false;
            }
        } else if (!produces.equals(other.produces)) {
            return false;
        }
        if (responses == null) {
            if (other.responses != null) {
                return false;
            }
        } else if (!responses.equals(other.responses)) {
            return false;
        }
        if (schemes == null) {
            if (other.schemes != null) {
                return false;
            }
        } else if (!schemes.equals(other.schemes)) {
            return false;
        }
        if (security == null) {
            if (other.security != null) {
                return false;
            }
        } else if (!security.equals(other.security)) {
            return false;
        }
        if (summary == null) {
            if (other.summary != null) {
                return false;
            }
        } else if (!summary.equals(other.summary)) {
            return false;
        }
        if (tags == null) {
            if (other.tags != null) {
                return false;
            }
        } else if (!tags.equals(other.tags)) {
            return false;
        }
        if (vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        return true;
    }

    public Operation vendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions.putAll( vendorExtensions );
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + operationId + "]";
    }
}
