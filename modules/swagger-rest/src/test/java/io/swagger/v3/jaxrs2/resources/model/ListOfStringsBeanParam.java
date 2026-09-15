package io.swagger.v3.jaxrs2.resources.model;

import java.util.List;

import javax.ws.rs.QueryParam;

public class ListOfStringsBeanParam {
    @QueryParam(value = "listOfStrings")
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}