package com.wordnik.swagger.sample.resource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "SC", urlPatterns = { "/*" }, initParams = {
        @WebInitParam(name = "swagger.config.reader", value = "com.wordnik.swagger.jaxrs.JerseyConfigReader"),
        @WebInitParam(name = "n2", value = "v2") }, loadOnStartup = 1)
public class SC extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();

    }

}
