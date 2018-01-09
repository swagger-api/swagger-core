package io.swagger.models;

import java.util.List;

import javax.ws.rs.QueryParam;

import io.swagger.annotations.ApiModel;

@ApiModel
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
