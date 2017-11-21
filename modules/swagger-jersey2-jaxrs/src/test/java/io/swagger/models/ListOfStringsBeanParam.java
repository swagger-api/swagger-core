package io.swagger.models;

import io.swagger.annotations.ApiModel;

import javax.ws.rs.QueryParam;
import java.util.List;

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
