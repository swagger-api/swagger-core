package com.wordnik.swagger.core;

/**
 * @author ayush
 * @since 6/23/11 12:26 PM
 */
public interface ApiValues {
    public static final int INHERIT_FROM_ENDPOINT = 10;
    public static final int OPEN = 20;
    public static final int REQUIRE_AUTHENTICATION = 30;

    public static final int E404 = 404;
    public static final int E400 = 400;
    public static final int E403 = 403;

    public static final String TYPE_PATH = "path";
    public static final String TYPE_BODY = "body";
    public static final String TYPE_QUERY = "query";
    public static final String TYPE_MATRIX = "matrix";
    public static final String TYPE_HEADER = "header";
    public static final String TYPE_FORM = "form";
    public static final String TYPE_COOKIE = "cookie";

    public static final String ANY = "any";
}
