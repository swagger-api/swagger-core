package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"get", "post", "put", "delete", "options", "patch"})
public class Path {
    private final Map<String, Object> vendorExtensions = new HashMap<String, Object>();
    private Operation get;
    private Operation put;
    private Operation post;
    private Operation delete;
    private Operation patch;
    private Operation options;
    private List<Parameter> parameters;

    public Path set(String method, Operation op) {
        if ("get".equals(method)) {
            return get(op);
        }
        if ("put".equals(method)) {
            return put(op);
        }
        if ("post".equals(method)) {
            return post(op);
        }
        if ("delete".equals(method)) {
            return delete(op);
        }
        if ("patch".equals(method)) {
            return patch(op);
        }
        if ("options".equals(method)) {
            return options(op);
        }
        return null;
    }

    public Path get(Operation get) {
        this.get = get;
        return this;
    }

    public Path put(Operation put) {
        this.put = put;
        return this;
    }

    public Path post(Operation post) {
        this.post = post;
        return this;
    }

    public Path delete(Operation delete) {
        this.delete = delete;
        return this;
    }

    public Path patch(Operation patch) {
        this.patch = patch;
        return this;
    }

    public Path options(Operation options) {
        this.options = options;
        return this;
    }

    public Operation getGet() {
        return get;
    }

    public void setGet(Operation get) {
        this.get = get;
    }

    public Operation getPut() {
        return put;
    }

    public void setPut(Operation put) {
        this.put = put;
    }

    public Operation getPost() {
        return post;
    }

    public void setPost(Operation post) {
        this.post = post;
    }

    public Operation getDelete() {
        return delete;
    }

    public void setDelete(Operation delete) {
        this.delete = delete;
    }

    public Operation getPatch() {
        return patch;
    }

    public void setPatch(Operation patch) {
        this.patch = patch;
    }

    public Operation getOptions() {
        return options;
    }

    public void setOptions(Operation options) {
        this.options = options;
    }

    @JsonIgnore
    public List<Operation> getOperations() {
        List<Operation> allOperations = new ArrayList<Operation>();
        if (get != null) {
            allOperations.add(get);
        }
        if (put != null) {
            allOperations.add(put);
        }
        if (post != null) {
            allOperations.add(post);
        }
        if (delete != null) {
            allOperations.add(delete);
        }
        if (patch != null) {
            allOperations.add(patch);
        }
        if (options != null) {
            allOperations.add(options);
        }

        return allOperations;
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

    @JsonIgnore
    public boolean isEmpty() {
        if (get == null && put == null && post == null && delete == null && patch == null && options == null) {
            return true;
        } else {
            return false;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((delete == null) ? 0 : delete.hashCode());
        result = prime * result + ((get == null) ? 0 : get.hashCode());
        result = prime * result + ((options == null) ? 0 : options.hashCode());
        result = prime * result
                + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((patch == null) ? 0 : patch.hashCode());
        result = prime * result + ((post == null) ? 0 : post.hashCode());
        result = prime * result + ((put == null) ? 0 : put.hashCode());
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
        Path other = (Path) obj;
        if (delete == null) {
            if (other.delete != null) {
                return false;
            }
        } else if (!delete.equals(other.delete)) {
            return false;
        }
        if (get == null) {
            if (other.get != null) {
                return false;
            }
        } else if (!get.equals(other.get)) {
            return false;
        }
        if (options == null) {
            if (other.options != null) {
                return false;
            }
        } else if (!options.equals(other.options)) {
            return false;
        }
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        if (patch == null) {
            if (other.patch != null) {
                return false;
            }
        } else if (!patch.equals(other.patch)) {
            return false;
        }
        if (post == null) {
            if (other.post != null) {
                return false;
            }
        } else if (!post.equals(other.post)) {
            return false;
        }
        if (put == null) {
            if (other.put != null) {
                return false;
            }
        } else if (!put.equals(other.put)) {
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
}