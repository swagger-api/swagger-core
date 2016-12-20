package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Info {
    private String description;
    private String version;
    private String title;
    private String termsOfService;
    private Contact contact;
    private License license;
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();

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

    public Info mergeWith(Info info) {
        if (info != null) {
            if (this.description == null) {
                this.description = info.description;
            }
            if (this.version == null) {
                this.version = info.version;
            }
            if (this.title == null) {
                this.title = info.title;
            }
            if (this.termsOfService == null) {
                this.termsOfService = info.termsOfService;
            }
            if (this.contact == null) {
                this.contact = info.contact;
            }
            if (this.license == null) {
                this.license = info.license;
            }
            if (this.vendorExtensions == null) {
                this.vendorExtensions = info.vendorExtensions;
            }
        }
        return this;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contact == null) ? 0 : contact.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((license == null) ? 0 : license.hashCode());
        result = prime * result
                + ((termsOfService == null) ? 0 : termsOfService.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result
                + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        Info other = (Info) obj;
        if (contact == null) {
            if (other.contact != null) {
                return false;
            }
        } else if (!contact.equals(other.contact)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (license == null) {
            if (other.license != null) {
                return false;
            }
        } else if (!license.equals(other.license)) {
            return false;
        }
        if (termsOfService == null) {
            if (other.termsOfService != null) {
                return false;
            }
        } else if (!termsOfService.equals(other.termsOfService)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }
}
