package io.swagger.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Json {

    private static ObjectMapper mapper;

    public static ObjectMapper mapper() {
        if(mapper == null) {
            mapper = ObjectMapperFactory.createJson();
        }
        return mapper;
    }
    public static ObjectWriter pretty() {
        return mapper().writer(new DefaultPrettyPrinter());
    }

    public static String pretty(Object o) {
        try {
            return pretty().writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            System.out.println(pretty().writeValueAsString(o).replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        TODO the following code is a hack to get past the fact that Path and Response are not interfaces, it can be deleted as part of the refactor to make Path and Response interfaces

        pathMapper and responseMapper are ObjectMappers that are only going to be used during deserialization of Paths and Responses.
        We need them because:
         1) RefPath extends Path
         2) RefResponse extends Response

         And when we detect we are deserializing a "normal" Path or Response (e.g. its not a ref) we need skip
         the PathDeserializer and ResponseDeserializer logic, lest we get into a stack overflow problem
     */
    private static ObjectMapper pathMapper;
    private static ObjectMapper responseMapper;


    protected static ObjectMapper pathMapper() {
        if (pathMapper == null) {
            pathMapper = ObjectMapperFactory.createJson(false, true);
        }

        return pathMapper;
    }

    protected static ObjectMapper responseMapper() {
        if (responseMapper == null) {
            responseMapper = ObjectMapperFactory.createJson(false, false);
        }

        return responseMapper;
    }
}