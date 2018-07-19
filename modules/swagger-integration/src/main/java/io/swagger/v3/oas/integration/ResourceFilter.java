package io.swagger.v3.oas.integration;

import java.util.HashSet;
import java.util.Set;

public class ResourceFilter {
  private Set<String> includePackages = new HashSet<String>();
  private Set<String> excludePackages = new HashSet<String>();

  public ResourceFilter includePackage(String... prefixes) {
    for(String prefix : prefixes) {
      includePackages.add(prefix);
    }
    return this;
  }

  public ResourceFilter excludePackage(String... prefixes) {
    for(String prefix : prefixes) {
      // Reflections filtering applies a value (including packages) if it matches a pattern with '/' or with '.'.
      // This means that if you exclude a package using one or the other the filter will always apply it as if it fails one it will pass the other.
      // Therefore when excluding a package make sure it matches both the '/' and '.' patterns.
      String dotPrefix = prefix.replace('/', '.');
      excludePackages.add(dotPrefix);
      String slashPrefix = prefix.replace('.', '/');
      excludePackages.add(slashPrefix);
    }
    return this;
  }

  public Set<String> getIncludePackages() {
    return includePackages;
  }

  public Set<String> getExcludePackages() {
    return excludePackages;
  }
}
