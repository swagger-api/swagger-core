package io.swagger.v3.jaxrs2.resources.model;

import javax.ws.rs.QueryParam;
import java.util.List;

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