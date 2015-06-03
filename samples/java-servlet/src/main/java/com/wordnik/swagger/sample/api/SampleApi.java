package com.wordnik.swagger.sample.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.multipart.FormDataMultiPart;
import com.wordnik.swagger.sample.ApiException;
import com.wordnik.swagger.sample.ApiInvoker;
import com.wordnik.swagger.sample.model.SampleData;

public class SampleApi {
  String basePath = "http://localhost:8002/";
  ApiInvoker apiInvoker = ApiInvoker.getInstance();

  public ApiInvoker getInvoker() {
    return apiInvoker;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getBasePath() {
    return basePath;
  }

  /**
   * Resource to get a user
   * 
   * @param name User&#39;s name
   * @param email User&#39;s email
   * @param id User ID
   * @param age User ID
   * @param dateOfBirth User&#39;s date of birth, in dd-MM-yyyy format
   * @return SampleData
   */
  public SampleData getUser(String name, String email, Long id, Long age, Date dateOfBirth) throws ApiException {
    Object postBody = null;

    // create path and map variables
    String path = "/sample/users".replaceAll("\\{format\\}", "json");

    // query params
    Map<String, String> queryParams = new HashMap<String, String>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    if (name != null)
      queryParams.put("name", ApiInvoker.parameterToString(name));
    if (email != null)
      queryParams.put("email", ApiInvoker.parameterToString(email));
    if (id != null)
      queryParams.put("id", ApiInvoker.parameterToString(id));
    if (age != null)
      queryParams.put("age", ApiInvoker.parameterToString(age));
    if (dateOfBirth != null)
      queryParams.put("dateOfBirth", ApiInvoker.parameterToString(dateOfBirth));

    String[] contentTypes = {

      };

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();

      if (hasFields)
        postBody = mp;
    }
    else {

    }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams,
        contentType);
      if (response != null) {
        return (SampleData) ApiInvoker.deserialize(response, "", SampleData.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }

}
