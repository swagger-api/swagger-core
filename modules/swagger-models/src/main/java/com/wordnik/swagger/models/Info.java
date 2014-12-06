package com.wordnik.swagger.models;

public class Info {
  private String description;
  private String version;
  private String title;
  private String termsOfService;
  private Contact contact;
  private License license;

  public Info version(String version) {
    this.setVersion(version);
    return this;
  }
  public Info title(String title) {
    this.setTitle(title);
    return this;
  }
  public Info description(String description) {
    this.setDescription(description);
    return this;
  }
  public Info termsOfService(String termsOfService) {
    this.setTermsOfService(termsOfService);
    return this;
  }
  public Info contact(Contact contact) {
    this.setContact(contact);
    return this;
  }
  public Info license(License license) {
    this.setLicense(license);
    return this;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  public String getTermsOfService() {
    return termsOfService;
  }
  public void setTermsOfService(String termsOfService) {
    this.termsOfService = termsOfService;
  }

  public Contact getContact() {
    return contact;
  }
  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public License getLicense() {
    return license;
  }
  public void setLicense(License license) {
    this.license = license;
  }
}