package com.wordnik.swagger.models;

import com.wordnik.swagger.models.auth.SecuritySchemeDefinition;

import com.fasterxml.jackson.annotation.*;
import com.wordnik.swagger.models.parameters.Parameter;

import java.util.*;

public class Swagger {
  protected String swagger = "2.0";
  protected Info info;
  protected String host;
  protected String basePath;
  protected List<Tag> tags;
  protected List<Scheme> schemes;
  protected List<String> consumes;
  protected List<String> produces;
  protected List<SecurityRequirement> securityRequirements;
  protected Map<String, Path> paths;
  protected Map<String, SecuritySchemeDefinition> securityDefinitions;
  protected Map<String, Model> definitions;
  protected Map<String, Parameter> parameters;
  protected ExternalDocs externalDocs;

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
    if(this.paths == null)
      this.paths = new LinkedHashMap<String, Path>();
    this.paths.put(key, path);
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
    if(this.schemes == null)
      this.schemes = new ArrayList<Scheme>();
    boolean found = false;
    for(Scheme existing : this.schemes) {
      if(existing.equals(scheme))
        found = true;
    }
    if(!found)
      this.schemes.add(scheme);
  }

  public List<Tag> getTags() {
    return tags;
  }
  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
  public void addTag(Tag tag) {
    if(this.tags == null)
      this.tags = new ArrayList<Tag>();
    if(tag != null && tag.getName() != null) {
      boolean found = false;
      for(Tag existing : this.tags) {
        if(existing.getName().equals(tag.getName()))
          found = true;
      }
      if(!found)
        this.tags.add(tag);
    }
  }

  public List<String> getConsumes() {
    return consumes;
  }
  public void setConsumes(List<String> consumes) {
    this.consumes = consumes;
  }
  public void addConsumes(String consumes) {
    if(this.consumes == null)
      this.consumes = new ArrayList<String>();
    this.consumes.add(consumes);
  }

  public List<String> getProduces() {
    return produces;
  }
  public void setProduces(List<String> produces) {
    this.produces = produces;
  }
  public void addProduces(String produces) {
    if(this.produces == null)
      this.produces = new ArrayList<String>();
    this.produces.add(produces);
  }

  public Map<String, Path> getPaths() {
    if(paths == null)
      return null;
    Map<String, Path> sorted = new LinkedHashMap<String, Path>();
    List<String> keys = new ArrayList<String>();
    keys.addAll(paths.keySet());
    Collections.sort(keys);

    for(String key: keys) {
      sorted.put(key, paths.get(key));
    }
    return sorted;
  }
  public void setPaths(Map<String, Path> paths) {
    this.paths = paths;
  }
  public Path getPath(String path) {
    if(this.paths == null)
      return null;
    return this.paths.get(path);
  }

  public Map<String, SecuritySchemeDefinition> getSecurityDefinitions() {
    return securityDefinitions;
  }
  public void setSecurityDefinitions(Map<String, SecuritySchemeDefinition> securityDefinitions) {
    this.securityDefinitions = securityDefinitions;
  }
  public void addSecurityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
    if(this.securityDefinitions == null)
      this.securityDefinitions = new HashMap<String, SecuritySchemeDefinition>();
    this.securityDefinitions.put(name, securityDefinition);
  }

  public List<SecurityRequirement> getSecurityRequirement() {
    return securityRequirements;
  }
  public void setSecurityRequirement(List<SecurityRequirement> securityRequirements) {
    this.securityRequirements = securityRequirements;
  }
  public void addSecurityDefinition(SecurityRequirement securityRequirement) {
    if(this.securityRequirements == null)
      this.securityRequirements = new ArrayList<SecurityRequirement>();
    this.securityRequirements.add(securityRequirement);
  }

  public void setDefinitions(Map<String, Model> definitions) {
    this.definitions = definitions;
  }
  public Map<String, Model> getDefinitions() {
    return definitions;
  }
  public void addDefinition(String key, Model model) {
    if(this.definitions == null)
      this.definitions = new HashMap<String, Model>();
    this.definitions.put(key, model);
  }

  public Map<String, Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Parameter> parameters) {
    this.parameters = parameters;
  }

  public Parameter getParameter(String parameter) {
    if(this.parameters == null)
      return null;
    return this.parameters.get(parameter);
  }

  public void addParameter(String key, Parameter parameter) {
    if(this.parameters == null)
      this.parameters = new HashMap<String, Parameter>();
    this.parameters.put(key, parameter);
  }

  public ExternalDocs getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocs value) {
    externalDocs = value;
  }
}
