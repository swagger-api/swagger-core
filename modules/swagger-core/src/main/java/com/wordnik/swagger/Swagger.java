package com.wordnik.swagger;

import com.wordnik.swagger.models.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Swagger {
  String swagger = "2.0";
  String title;
  Info info;
  String host;
  String basePath;
  List<Scheme> schemes;
  List<String> consumes;
  List<String> produces;
  Map<String, Path> paths;
  List<Security> security;
  Map<String, Model> definitions;

  public Swagger title(String title) {
    this.setTitle(title);
    return this;
  }
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
      this.paths = new HashMap<String, Path>();
    this.paths.put(key, path);
    return this;
  }
  public Swagger security(List<Security> security) {
    this.setSecurity(security);
    return this;
  }
  public Swagger security(Security security) {
    this.addSecurity(security);
    return this;
  }
  public Swagger model(String name, Model model) {
    this.addDefinition(name, model);
    return this;
  }

  public String getSwagger() {
    return swagger;
  }
  public void setSwagger(String swagger) {
    this.swagger = swagger;
  }

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
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
    this.schemes.add(scheme);
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
    return paths;
  }
  public void setPaths(Map<String, Path> paths) {
    this.paths = paths;
  }

  public List<Security> getSecurity() {
    return security;
  }
  public void setSecurity(List<Security> security) {
    this.security = security;
  }
  public void addSecurity(Security security) {
    if(this.security == null)
      this.security = new ArrayList<Security>();
    this.security.add(security);
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
}