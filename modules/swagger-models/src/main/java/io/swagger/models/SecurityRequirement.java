package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SecurityRequirement {
    private String name;
    private List<String> scopes;
//    private final Map<String, Object> vendorExtensions = new HashMap<String, Object>();
    private Map<String, List<String>> requirements = new LinkedHashMap<String, List<String>>();

    public SecurityRequirement() {
    }

    @Deprecated
    public SecurityRequirement(String name) {
        setName(name);
    }

    public SecurityRequirement scope(String scope) {
        this.addScope(scope);
        return this;
    }

    public SecurityRequirement requirement(String name, List<String> scopes) {
        if(requirements == null) {
            requirements = new LinkedHashMap<String, List<String>>();
        }

        if(scopes == null) {
            scopes = new ArrayList<String>();
        }

        requirements.put(name, scopes);
        return this;
    }

    public SecurityRequirement requirement(String name) {
        return requirement(name, null);
    }

    @JsonIgnore
    @Deprecated
    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    @Deprecated
    public List<String> getScopes() {
        return scopes;
    }

    @Deprecated
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public void addScope(String scope) {
        if (scopes == null) {
            scopes = new ArrayList<String>();
        }
        scopes.add(scope);
    }



//    @JsonAnyGetter
//    public Map<String, Object> getVendorExtensions() {
//        return vendorExtensions;
//    }
//
//    @JsonAnySetter
//    public void setVendorExtension(String name, Object value) {
//        if (name.startsWith("x-")) {
//            vendorExtensions.put(name, value);
//        }
//    }

    @JsonAnyGetter
    public Map<String, List<String>> getRequirements() {
        return requirements;
    }

    @JsonAnySetter
    public void setRequirements(String name, List<String> scopes) {
        requirements.put(name, scopes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((scopes == null) ? 0 : scopes.hashCode());
//        result = prime * result
//                + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
        result = prime * result
                + ((requirements == null) ? 0 : requirements.hashCode());
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
        SecurityRequirement other = (SecurityRequirement) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (scopes == null) {
            if (other.scopes != null) {
                return false;
            }
        } else if (!scopes.equals(other.scopes)) {
            return false;
        }
//        if (vendorExtensions == null) {
//            if (other.vendorExtensions != null) {
//                return false;
//            }
//        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
//            return false;
//        }
        if (requirements == null) {
            if (other.requirements != null) {
                return false;
            }
        } else if (!requirements.equals(other.requirements)) {
            return false;
        }
        return true;
    }
}
