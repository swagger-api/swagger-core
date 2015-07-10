package io.swagger.models;

import io.swagger.models.parameters.Parameter;

import java.util.List;
import java.util.Map;

/**
 * Created by russellb337 on 7/8/15.
 */
public interface Path {
    Path set(String method, Operation op);

    Path get(Operation get);

    Path put(Operation put);

    Path post(Operation post);

    Path delete(Operation delete);

    Path patch(Operation patch);

    Path options(Operation options);

    Operation getGet();

    void setGet(Operation get);

    Operation getPut();

    void setPut(Operation put);

    Operation getPost();

    void setPost(Operation post);

    Operation getDelete();

    void setDelete(Operation delete);

    Operation getPatch();

    void setPatch(Operation patch);

    Operation getOptions();

    void setOptions(Operation options);

    List<Operation> getOperations();

    Map<HttpMethod, Operation> getOperationMap();

    List<Parameter> getParameters();

    void setParameters(List<Parameter> parameters);

    void addParameter(Parameter parameter);

    boolean isEmpty();

    Map<String, Object> getVendorExtensions();

    void setVendorExtension(String name, Object value);
}
