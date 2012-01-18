/**
 *  Copyright 2011 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.core;

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
    public static final String TYPE_FORM_DATA = "data";
    public static final String TYPE_COOKIE = "cookie";

    public static final String ANY = "any";
}
