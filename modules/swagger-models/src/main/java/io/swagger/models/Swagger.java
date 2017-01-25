package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Swagger {
    protected String swagger = "2.0";
    protected Info info;
    protected String host;
    protected String basePath;
    protected List<Tag> tags;
    protected List<Scheme> schemes;
    protected List<String> consumes;
    protected List<String> produces;
    protected List<SecurityRequirement> security;
    protected Map<String, Path> paths;
    protected Map<String, SecuritySchemeDefinition> securityDefinitions;
    protected Map<String, Model> definitions;
    protected Map<String, Parameter> parameters;
    protected Map<String, Response> responses;
    protected ExternalDocs externalDocs;
    protected Map<String, Object> vendorExtensions;

    // builders
    public Swagger info(Info info) {
        this.setInfo(info);
        return this;
    }

    public Swagger host(String host) {
        this.setHost(host);
        return this;
    }

    public Swagger basePath(String basePath) {
        this.setBasePath(basePath);
        return this;
    }

    public Swagger externalDocs(ExternalDocs value) {
        this.setExternalDocs(value);
        return this;
    }

    public Swagger tags(List<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Swagger tag(Tag tag) {
        this.addTag(tag);
        return this;
    }

    public Swagger schemes(List<Scheme> schemes) {
        this.setSchemes(schemes);
        return this;
    }

    public Swagger scheme(Scheme scheme) {
        this.addScheme(scheme);
        return this;
    }

    public Swagger consumes(List<String> consumes) {
        this.setConsumes(consumes);
        return this;
    }

    public Swagger consumes(String consumes) {
        this.addConsumes(consumes);
        return this;
    }

    public Swagger produces(List<String> produces) {
        this.setProduces(produces);
        return this;
    }

    public Swagger produces(String produces) {
        this.addProduces(produces);
        return this;
    }

    public Swagger paths(Map<String, Path> paths) {
        this.setPaths(paths);
        return this;
    }

    public Swagger path(String key, Path path) {
        if (this.paths == null) {
            this.paths = new LinkedHashMap<String, Path>();
        }
        this.paths.put(key, path);
        return this;
    }

    public Swagger responses(Map<String, Response> responses) {
        this.responses = responses;
        return this;
    }

    public Swagger response(String key, Response response) {
        if (this.responses == null) {
            this.responses = new LinkedHashMap<String, Response>();
        }
        this.responses.put(key, response);
        return this;
    }

    public Swagger parameter(String key, Parameter parameter) {
        this.addParameter(key, parameter);
        return this;
    }

    public Swagger securityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
        this.addSecurityDefinition(name, securityDefinition);
        return this;
    }

    public Swagger model(String name, Model model) {
        this.addDefinition(name, model);
        return this;
    }

    public Swagger security(SecurityRequirement securityRequirement) {
        this.addSecurity(securityRequirement);
        return this;
    }

    public Swagger vendorExtension(String key, Object extension) {
        if(this.vendorExtensions == null) {
            this.vendorExtensions = new LinkedHashMap<String, Object>();
        }
        this.vendorExtensions.put(key, extension);
        return this;
    }

    // getter & setters
    public String getSwagger() {
        return swagger;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Tag getTag(String tagName) {
	Tag tag = null;
	if (this.tags != null && tagName != null) {
	    for (Tag existing : this.tags) {
		if (existing.getName().equals(tagName)) {
		    tag = existing;
		    break;
		}
	    }
	}
	return tag;
    }
    
    public void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<Tag>();
        }
        if (tag != null && tag.getName() != null) {
            if (getTag(tag.getName()) == null) {
        	this.tags.add(tag);
            }
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

        if (!this.consumes.contains(consumes)) {
            this.consumes.add(consumes);
        }
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

        if (!this.produces.contains(produces)) {
            this.produces.add(produces);
        }
    }

    public Map<String, Path> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Path> paths) {
        this.paths = paths;
    }

    public Path getPath(String path) {
        if (this.paths == null) {
            return null;
        }
        return this.paths.get(path);
    }

    public Map<String, SecuritySchemeDefinition> getSecurityDefinitions() {
        return securityDefinitions;
    }

    public void setSecurityDefinitions(Map<String, SecuritySchemeDefinition> securityDefinitions) {
        this.securityDefinitions = securityDefinitions;
    }

    public void addSecurityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
        if (this.securityDefinitions == null) {
            this.securityDefinitions = new LinkedHashMap<String, SecuritySchemeDefinition>();
        }
        this.securityDefinitions.put(name, securityDefinition);
    }

    /**
     * @deprecated Use {@link #getSecurity()}.
     */
    @JsonIgnore
    @Deprecated
    public List<SecurityRequirement> getSecurityRequirement() {
        return security;
    }

    /**
     * @deprecated Use {@link #setSecurity(List)}.
     */
    @JsonIgnore
    @Deprecated
    public void setSecurityRequirement(List<SecurityRequirement> securityRequirements) {
        this.security = securityRequirements;
    }

    /**
     * @deprecated Use {@link #addSecurity(SecurityRequirement)}.
     */
    @JsonIgnore
    @Deprecated
    public void addSecurityDefinition(SecurityRequirement securityRequirement) {
        this.addSecurity(securityRequirement);
    }

    public List<SecurityRequirement> getSecurity() {
        return security;
    }

    public void setSecurity(List<SecurityRequirement> securityRequirements) {
        this.security = securityRequirements;
    }

    public void addSecurity(SecurityRequirement securityRequirement) {
        if (this.security == null) {
            this.security = new ArrayList<SecurityRequirement>();
        }
        this.security.add(securityRequirement);
    }

    public Map<String, Model> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, Model> definitions) {
        this.definitions = definitions;
    }

    public void addDefinition(String key, Model model) {
        if (this.definitions == null) {
            this.definitions = new LinkedHashMap<String, Model>();
        }
        this.definitions.put(key, model);
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Parameter getParameter(String parameter) {
        if (this.parameters == null) {
            return null;
        }
        return this.parameters.get(parameter);
    }

    public void addParameter(String key, Parameter parameter) {
        if (this.parameters == null) {
            this.parameters = new LinkedHashMap<String, Parameter>();
        }
        this.parameters.put(key, parameter);
    }

    public Map<String, Response> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, Response> responses) {
        this.responses = responses;
    }

    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        externalDocs = value;
    }

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            vendorExtension(name, value);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((basePath == null) ? 0 : basePath.hashCode());
        result = prime * result + ((consumes == null) ? 0 : consumes.hashCode());
        result = prime * result
                + ((definitions == null) ? 0 : definitions.hashCode());
        result = prime * result
                + ((externalDocs == null) ? 0 : externalDocs.hashCode());
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((info == null) ? 0 : info.hashCode());
        result = prime * result
                + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((paths == null) ? 0 : paths.hashCode());
        result = prime * result + ((produces == null) ? 0 : produces.hashCode());
        result = prime * result + ((schemes == null) ? 0 : schemes.hashCode());
        result = prime
                * result
                + ((securityDefinitions == null) ? 0 : securityDefinitions
                .hashCode());
        result = prime
                * result
                + ((security == null) ? 0 : security
                .hashCode());
        result = prime * result + ((swagger == null) ? 0 : swagger.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
        Swagger other = (Swagger) obj;
        if (basePath == null) {
            if (other.basePath != null) {
                return false;
            }
        } else if (!basePath.equals(other.basePath)) {
            return false;
        }
        if (consumes == null) {
            if (other.consumes != null) {
                return false;
            }
        } else if (!consumes.equals(other.consumes)) {
            return false;
        }
        if (definitions == null) {
            if (other.definitions != null) {
                return false;
            }
        } else if (!definitions.equals(other.definitions)) {
            return false;
        }
        if (externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (info == null) {
            if (other.info != null) {
                return false;
            }
        } else if (!info.equals(other.info)) {
            return false;
        }
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        if (paths == null) {
            if (other.paths != null) {
                return false;
            }
        } else if (!paths.equals(other.paths)) {
            return false;
        }
        if (produces == null) {
            if (other.produces != null) {
                return false;
            }
        } else if (!produces.equals(other.produces)) {
            return false;
        }
        if (schemes == null) {
            if (other.schemes != null) {
                return false;
            }
        } else if (!schemes.equals(other.schemes)) {
            return false;
        }
        if (securityDefinitions == null) {
            if (other.securityDefinitions != null) {
                return false;
            }
        } else if (!securityDefinitions.equals(other.securityDefinitions)) {
            return false;
        }
        if (security == null) {
            if (other.security != null) {
                return false;
            }
        } else if (!security.equals(other.security)) {
            return false;
        }
        if (swagger == null) {
            if (other.swagger != null) {
                return false;
            }
        } else if (!swagger.equals(other.swagger)) {
            return false;
        }
        if (tags == null) {
            if (other.tags != null) {
                return false;
            }
        } else if (!tags.equals(other.tags)) {
            return false;
        }
        return true;
    }

    public Swagger vendorExtensions(Map<String, Object> vendorExtensions) {
        if( vendorExtensions == null ){
            return this;
        }

        if( this.vendorExtensions == null ){
            this.vendorExtensions = new LinkedHashMap<String, Object>();
        }

        this.vendorExtensions.putAll( vendorExtensions );
        return this;
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }
}
